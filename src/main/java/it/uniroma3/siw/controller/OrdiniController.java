package it.uniroma3.siw.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Ordine;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.model.VoceOrdine;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.OrdineService;
import it.uniroma3.siw.service.ProdottoService;
import it.uniroma3.siw.service.UserService;

@Controller
public class OrdiniController {
	
	@Autowired
	private OrdineService ordineservice;
	
	@Autowired
	private ProdottoService prodottoService;
	
	@Autowired
	private CredentialsService credentialService;
	
	@GetMapping ("/ordine/ordini")
	public String ordiniHome() {
		return "/ordine/ordini.html";
	}
	
	  
	  @GetMapping("/ordine/ordiniFatti")
	  public String ordiniFAtti(Model model) {
	
		  model.addAttribute("ordini", this.ordineservice.getAllByOrderByGiornoConsegnaAsc());
		  return "ordiniFatti.html";
	  }
	  
	  /*
	  @GetMapping("/formNewOrdine")
	    public String formNewOrdine(Model model) {
	    model.addAttribute("ordine", new Ordine());
	    return "formNewOrdine.html";
	  }
	  */
	  
	  /*
	   @GetMapping("/ordine/formNewOrdine")
	    public String mostraFormOrdine(Model model) {
		   /*
	        // Crea un nuovo oggetto Ordine per il form
	        Ordine ordine = new Ordine();
	        List<Prodotto> prodotti= prodottoService.getOrderedByCategoria();
	        

	        model.addAttribute("ordine", ordine);
	        model.addAttribute("prodotti", prodotti);
	        
	        return "/ordine/ordini.html"; // Nome del template Thymeleaf
	    */
	   
	   /*
	   @GetMapping("/ordine/formNewOrdine")
	    public String mostraFormOrdine() {
		   return "/ordine/ordini.html";	
	        
	    }
	  	*/
	   /*
	  @PostMapping("/ordine/ordiniFatti")
	  public String newMovie(@ModelAttribute("ordine") Ordine ordine, Model model) {
		this.ordineservice.save(ordine);
		model.addAttribute("ordine", ordine);
		return "/ordine/ordiniFatti.html";
	  }
		*/
	  
	      @GetMapping("/ordine/formNewOrdine")
	      public String mostraFormOrdine(Model model,Principal principal) {
	    	  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	          // Ottieni l'istanza di User (che implementa UserDetails)
	          //User user = (User) authentication.getPrincipal();
	    	  String nomeUtente= principal.getName();
	    	  Credentials userCredentials= credentialService.getCredentials(nomeUtente);
	    	  User user= userCredentials.getUser();
	          Ordine ordine = new Ordine(); // Nuovo ordine vuoto

	          List<Prodotto> prodotti = prodottoService.getOrderedByCategoria(); // Lista prodotti

	          model.addAttribute("ordine", ordine);
	          model.addAttribute("prodotti", prodotti);
	          model.addAttribute("utente", user);
	          return "/ordine/formNewOrdine.html"; // Template del form
	      }

	      // Metodo POST: Salva l'ordine con le voci d'ordine
	      
	      @PostMapping("/ordine/ordiniFatti")
	      public String newOrdine(@ModelAttribute("ordine") Ordine ordine,
	                              @RequestParam Map<String, String> allParams,
	                              Model model, Principal principal) {
	    	  String nomeUtente= principal.getName();
	    	  Credentials userCredentials= credentialService.getCredentials(nomeUtente);
	    	  User user= userCredentials.getUser();
	    	  ordine.setNumeroTelefonico(user.getNumeroTelefonico());
	    	  ordine.setUtente(user);
	    	  ordine.setNome(user.getName() + " " + user.getSurname());
	    	  ordine.setNumeroTelefonico(user.getNumeroTelefonico());
	          List<VoceOrdine> vociOrdine = new ArrayList<>();
	          boolean hasInvalidFields= false;
	          List<Prodotto> prodotti = prodottoService.getOrderedByCategoria(); // Lista prodotti
	          
	          
	          for (Map.Entry<String, String> entry : allParams.entrySet()) {
	              String paramName = entry.getKey();
	              if (paramName.startsWith("quantita_")) {
	                  String idStr = paramName.substring("quantita_".length());
	                  try {
	                      Long productId = Long.parseLong(idStr);
	                      int quantita = Integer.parseInt(entry.getValue());
	                      if (quantita > 0) {
	                          Prodotto prodotto = prodottoService.getProdottoById(productId);
	                          if (prodotto != null) {
	                              VoceOrdine voceOrdine = new VoceOrdine();
	                              voceOrdine.setProdotto(prodotto);
	                              voceOrdine.setOrdine(ordine);
	                              voceOrdine.setQuantità(quantita);
	                              voceOrdine.calcolaTotParziale();
	                              vociOrdine.add(voceOrdine);
	                          }
	                      }
	                  } catch (NumberFormatException e) {

	                  }
	              }
	          }
	          
	          String orarioConsegnaStr = allParams.get("oraConsegna"); // Assuming the form field name is "orarioConsegna"
	          if (orarioConsegnaStr != null && !orarioConsegnaStr.isEmpty()) {
	              try {
	                  LocalTime orarioConsegna = LocalTime.parse(orarioConsegnaStr); // Parse "HH:mm" format
	                  ordine.setOrarioConsegna(orarioConsegna);
	              } catch (DateTimeParseException e) {
	                  hasInvalidFields = true; // Invalid time format
	                  model.addAttribute("errore", "Formato orario non valido. Usa HH:mm (es. 14:30)");
	                  model.addAttribute("ordine", ordine);
	                  return "/ordine/formNewOrdine.html";
	              }
	          } else {
	              hasInvalidFields = true; // Missing time
	              model.addAttribute("errore", "L'orario di consegna è obbligatorio");
	              model.addAttribute("ordine", ordine);
	              return "/ordine/formNewOrdine.html";
	          }
	          
	       // Gestione del risultato

	              ordine.setVociOrdine(vociOrdine);
	              ordine.setTolale(ordine.calculateTotal());
	              ordine.setGiornoConsegna(LocalDate.now());
	              if (ordineservice.getBygiornoConsegnaAndorarioConsegna(ordine.getGiornoConsegna(), ordine.getOrarioConsegna()) != null) {
	            	    String errore= "Esiste già un' ordine: prova a cambaire ora!";
	            	    model.addAttribute("errore", errore);
	            	    model.addAttribute("ordine", ordine); // Ripopola il form con i dati inseriti
	            	    model.addAttribute("utente", user);
	            	    model.addAttribute("prodotti", prodotti);
	            	    return "/ordine/formNewOrdine.html";
	            	}
	            	ordineservice.save(ordine);
	              model.addAttribute("ordine", ordine);
	              return "/ordine/ordini"; // Procedi alla pagina di successo
	          	
	          }
	          
	          
	      
	      /*
	      @PostMapping("/ordine/ordiniFatti")
	      public String newOrdine(@ModelAttribute("ordine") Ordine ordine,
	                              @RequestParam Map<String, String> allParams,
	                              Model model) {
	          List<VoceOrdine> vociOrdine = new ArrayList<>();
	          boolean hasAtLeastOneProduct= false;
	          for (Map.Entry<String, String> entry : allParams.entrySet()) {
	              String paramName = entry.getKey();
	              if (paramName.startsWith("quantita_")) {
	                  String idStr = paramName.substring("quantita_".length());
	                  try {
	                      Long productId = Long.parseLong(idStr);
	                      int quantita = Integer.parseInt(entry.getValue());
	                      if (quantita > 0) {
	                    	  hasAtLeastOneProduct=true;
	                          Prodotto prodotto = prodottoService.getProdottoById(productId);
	                          if (prodotto != null) {
	                              VoceOrdine voceOrdine = new VoceOrdine();
	                              voceOrdine.setProdotto(prodotto);
	                              voceOrdine.setOrdine(ordine);
	                              voceOrdine.setQuantità(quantita);
	                              vociOrdine.add(voceOrdine);
	                          }
	                      }
	
	                  } catch (NumberFormatException e) {
	                	  model.addAttribute("error", "Inserisci solo numeri validi per le quantità.");
	                      return "/ordine/formNewOrdine"; // Torna al form
	                  }
	              }
	          	}
	   
	          if (!hasAtLeastOneProduct) {
	              model.addAttribute("error", "Errore: Tutte le quantità sono zero. Inserisci almeno una quantità maggiore di zero.");
	              return "/ordine/formNewOrdine"; // Torna al form con l'errore
	          }

	          // Salva l'ordine se tutto è valido
	          ordine.setVociOrdine(vociOrdine);
	          ordineservice.save(ordine);
	          model.addAttribute("ordine", ordine);
	          return "ordiniFatti"; // Vai alla pagina di successo
	          	
	      }
	      */
}
