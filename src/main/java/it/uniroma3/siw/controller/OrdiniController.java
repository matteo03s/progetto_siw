package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Ordine;
import it.uniroma3.siw.service.OrdineService;

@Controller
public class OrdiniController {
	
	@Autowired
	private OrdineService ordineservice;
	
	@GetMapping ("/ordini")
	public String ordiniHome() {
		return "ordini.html";
	}
	
	  
	  @GetMapping("/ordiniFatti")
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
	  
	   @GetMapping("/formNewOrdine")
	    public String mostraFormOrdine(Model model) {
	        // Crea un nuovo oggetto Ordine per il form
	        Ordine ordine = new Ordine();
	        
	        // Recupera l'utente loggato
	        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        String nomeUtente;
	        
	        if (principal instanceof UserDetails) {
	            // Supponiamo che il nome sia il campo username o un altro campo
	            nomeUtente = ((UserDetails) principal).getUsername();
	            // Se hai un campo personalizzato come "fullName", dovresti accedere al tuo UserDetails personalizzato
	            // Es. nomeUtente = ((CustomUserDetails) principal).getFullName();
	        } else {
	            nomeUtente = principal.toString(); // Fallback
	        }
	        
	        // Precompila il campo nome dell'oggetto Ordine
	        ordine.setNome(nomeUtente);
	        
	        // Aggiungi l'oggetto Ordine al modello per il form
	        model.addAttribute("ordine", ordine);
	        
	        return "formNewOrdine.html"; // Nome del template Thymeleaf
	    }
	  
	  @PostMapping("/ordiniFatti")
	  public String newMovie(@ModelAttribute("ordine") Ordine movie, Model model) {
		this.ordineservice.save(movie);
		model.addAttribute("ordine", movie);
		return "ordiniFatti.html";
	  }


}
