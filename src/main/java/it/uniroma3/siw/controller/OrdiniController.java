package it.uniroma3.siw.controller;

import java.security.Principal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Ordine;
import it.uniroma3.siw.model.Prenotazione;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.model.VoceOrdine;
import it.uniroma3.siw.repository.VoceOrdineRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.OrdineService;
import it.uniroma3.siw.service.ProdottoService;
import it.uniroma3.siw.service.UserService;
import it.uniroma3.siw.service.VoceOrdineService;
import jakarta.validation.Valid;

@Controller
public class OrdiniController {


	@Autowired
	private OrdineService ordineservice;

	@Autowired
	private ProdottoService prodottoService;

	@Autowired
	private CredentialsService credentialService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private VoceOrdineService voceordineService;




	@GetMapping ("/ordine/ordini")
	public String getOrdini (Model model, Principal principal) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Credentials credentials = credentialService.getCredentials(userDetails.getUsername());
		if (credentials.getRole().equals(Credentials.PROVIDER_ROLE)) {
			return "redirect:/admin/ordini";
		}
		model.addAttribute("ordini", this.ordineservice.getOrdiniUsername(principal.getName()));
		return "ordine/ordini.html";
	}
	@GetMapping ("/ordine/{id}")
	public String getOrdine (Model model, Principal principal, @PathVariable ("id") Long id) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialService.getCredentials(userDetails.getUsername());
		Ordine ordine = this.ordineservice.getOrdineById(id);
		if (!credentials.getRole().equals("PROVIDER")) {
			User user = credentials.getUser();
			if (!user.getId().equals(ordine.getUtente().getId()))
				return "redirect:/error/access-denied";
		}
		model.addAttribute("ordine", ordine);
		return "ordine/dettagliOrdine.html";
	}
	
	@GetMapping ("/admin/ordini/{id}")
	public String getOrdiniUtente (Model model, @PathVariable ("id") Long id) {
		User utente = userService.getUser(id);
		model.addAttribute("ordini", utente.getOrdini());
		return "admin/ordini.html";
	}

	@GetMapping("/ordine/formNewOrdine")
	public String mostraFormOrdine(Model model,Principal principal) {
		//Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// Ottieni l'istanza di User (che implementa UserDetails)
		//User user = (User) authentication.getPrincipal();
		String nomeUtente= principal.getName();
		Credentials userCredentials= credentialService.getCredentials(nomeUtente);
		User user= userCredentials.getUser();
		Ordine ordine = new Ordine(); // Nuovo ordine vuoto

		List<Prodotto> prodotti = prodottoService.getOrderedByCategoriaAsc(); // Lista prodotti

		model.addAttribute("ordine", ordine);
		model.addAttribute("prodotti", prodotti);
		model.addAttribute("utente", user);
		return "/ordine/formNewOrdine.html"; // Template del form
	}

	// Metodo POST: Salva l'ordine con le voci d'ordine
	/*
	@PostMapping("/ordine/riepilogoOrdine")
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
					hasInvalidFields = true;
					model.addAttribute("errore", "Quantità o ID prodotto non valido. Assicurati di inserire numeri validi.");
					model.addAttribute("utente", user);
					model.addAttribute("prodotti", prodotti);
					return "/ordine/formNewOrdine.html";

				}
			}
		}
		if(ordine.getOrarioConsegna()==null) {
			String orarioConsegnaStr = allParams.get("oraConsegna"); // Assuming the form field name is "orarioConsegna"
			if (orarioConsegnaStr != null && !orarioConsegnaStr.isEmpty()) {
				try {
					LocalTime orarioConsegna = LocalTime.parse(orarioConsegnaStr); // Parse "HH:mm" format
					ordine.setOrarioConsegna(orarioConsegna);
				} catch (DateTimeParseException e) {
					hasInvalidFields = true; // Invalid time format
					model.addAttribute("errore", "Formato orario non valido. Usa HH:mm (es. 14:30)");
					model.addAttribute("utente", user);
					model.addAttribute("prodotti", prodotti);
					return "/ordine/formNewOrdine.html";
				}
			} else {
				hasInvalidFields = true; // Missing time
				model.addAttribute("errore", "L'orario di consegna è obbligatorio");
				model.addAttribute("utente", user);
				model.addAttribute("prodotti", prodotti);
				return "/ordine/formNewOrdine.html";
			}
		}




		// Gestione del risultato
		if(vociOrdine.isEmpty()) {
			model.addAttribute("errore", "Seleziona almeno un prodotto");
			model.addAttribute("utente", user);
			model.addAttribute("prodotti", prodotti);
			return "/ordine/formNewOrdine.html";

		}
		ordine.setVociOrdine(vociOrdine);
		ordine.setTotale(ordine.calculateTotal());
		ordine.setGiornoConsegna(LocalDate.now());
		ordine.setIndirizzo(allParams.get("via"));
		if (ordineservice.getBygiornoConsegnaAndorarioConsegna(ordine.getGiornoConsegna(), ordine.getOrarioConsegna()) != null) {
			String errore= "Esiste già un' ordine: prova a cambaire ora!";
			model.addAttribute("errore", errore);
			model.addAttribute("utente", user);
			model.addAttribute("prodotti", prodotti);
			return "/ordine/formNewOrdine.html";
		}

		this.ordineservice.save(ordine);
		for (VoceOrdine voce : vociOrdine) {
			voce.setOrdine(ordine); // Assicurati che l'ordine sia quello salvato
			this.voceordineService.save(voce);
		}
		model.addAttribute("ordine", ordine);
		return "/ordine/riepilogoOrdine"; // Procedi alla pagina di successo

	}
	 */

	@PostMapping("/ordine/riepilogoOrdine")
	public String newOrdine(@Valid @ModelAttribute("ordine") Ordine ordine,
			@RequestParam Map<String, String> allParams,
			Model model, Principal principal, BindingResult bindingResult) {

		String nomeUtente= principal.getName();
		Credentials userCredentials= credentialService.getCredentials(nomeUtente);
		User user= userCredentials.getUser();
		List<VoceOrdine> vociOrdine = new ArrayList<>();

		List<Prodotto> prodotti = prodottoService.getOrderedByCategoriaAsc(); // Lista prodotti	
		
		  //ottieneìi il giorno della settimana
        DayOfWeek giorno = ordine.getGiornoConsegna().getDayOfWeek();
		if (bindingResult.hasErrors() || ordine.getGiornoConsegna().isBefore(LocalDate.now()) || giorno == DayOfWeek.MONDAY) {
			// Validazione automatica
		    if (bindingResult.hasErrors()) {
		        model.addAttribute("errore", "Errore nei dati inseriti. Controlla i campi e riprova.");
		    }
		    else if (ordine.getGiornoConsegna().isBefore(LocalDate.now())) {
		    	model.addAttribute("errore", "La data di consegna non può essere precedente a oggi.");
		    }
		    else if (giorno == DayOfWeek.MONDAY) {
		    	model.addAttribute("errore", "Gli ordini non sono disponibili il lunedì.");
	        }
	        model.addAttribute("utente", user);
	        model.addAttribute("prodotti", prodotti);
	        return "/ordine/formNewOrdine.html";
		    
		}

	 // Confronto dell'orario di consegna
	    LocalTime maxTime = LocalTime.of(23, 0); // 23:00
	    LocalTime minTime= LocalTime.of(11, 0);	 // 11:00
	    if (ordine.getOrarioConsegna() != null && ordine.getOrarioConsegna().isAfter(maxTime)
	    		|| ordine.getOrarioConsegna().isBefore(minTime)) {
	        model.addAttribute("errore", "L'orario di consegna non è accettabile: fai un ordine tra le 11:00 e le 23:00.");
	        model.addAttribute("utente", user);
	        model.addAttribute("prodotti", prodotti);
	        return "/ordine/formNewOrdine.html";
	    }
		
		
		
		//prendo tutte le coppire " quantita_idProdotto" - "quantita" 
		for (Map.Entry<String, String> entry : allParams.entrySet()) {
			String paramName = entry.getKey();					// prendo la chiave: "quantita_id"
			if (paramName.startsWith("quantita_")) {
				String idStr = paramName.substring("quantita_".length());		// prendo solo "id"
				try {
					Long productId = Long.parseLong(idStr);						//conversione id da string a long
					int quantita = Integer.parseInt(entry.getValue());			//conversione quantità relativo a quel prodotto da string a int
					if (quantita > 0) {											//se maggiore di zero rapresenta un ordine selezionato dal' utente
						Prodotto prodotto = prodottoService.getProdottoById(productId);
						if (prodotto != null) {
							//aggiungo una nuova voce ordine
							VoceOrdine voceOrdine = new VoceOrdine();
							voceOrdine.setProdotto(prodotto);
							voceOrdine.setOrdine(ordine);
							voceOrdine.setQuantità(quantita);
							voceOrdine.calcolaTotParziale();
							vociOrdine.add(voceOrdine);
						}
					}
				} catch (NumberFormatException e) {

					model.addAttribute("errore", "Quantità o ID prodotto non valido. Assicurati di inserire numeri validi.");
					model.addAttribute("utente", user);
					model.addAttribute("prodotti", prodotti);
					return "/ordine/formNewOrdine.html";

				}
			}
		}
		if(vociOrdine.isEmpty()) {
			model.addAttribute("errore", "Seleziona almeno un prodotto");
			model.addAttribute("utente", user);
			model.addAttribute("prodotti", prodotti);
			return "/ordine/formNewOrdine.html";

		}

		ordine.setVociOrdine(vociOrdine);
		ordine.setTotale(ordine.calculateTotal());
		ordine.setGiornoConsegna(LocalDate.now());
		ordine.setUtente(user);
		if (ordineservice.getBygiornoConsegnaAndorarioConsegna(ordine.getGiornoConsegna(), ordine.getOrarioConsegna()) != null) {
			String errore= "Esiste già un' ordine: prova a cambaire ora!";
			model.addAttribute("errore", errore);
			model.addAttribute("utente", user);
			model.addAttribute("prodotti", prodotti);
			return "/ordine/formNewOrdine.html";
		}

		//salvataggio dell' ordine e delle voci d'ordine
		this.ordineservice.save(ordine);

		model.addAttribute("ordine", ordine); 
		ordineservice.save(ordine);

		return "redirect:/ordine/riepilogo/" + ordine.getId();
	}


	@GetMapping("/ordine/riepilogo/{id}")
	public String mostraRiepilogoOrdine(@PathVariable("id") Long id, Model model) {
		// Validate ID
		if (id == null || id <= 0) {
			model.addAttribute("errorMessage", "ID ordine non valido.");
			return "/error/500.html"; // Assumes an error page exists
		}

		// Recupera l'ordine dal database
		Ordine ordine = ordineservice.getOrdineById(id);
		if (ordine == null) {
			model.addAttribute("errorMessage", "Ordine non trovato.");
			return "/error/500.html";
		}

		model.addAttribute("ordine", ordine);
		return "/ordine/riepilogoOrdine.html";
	}

	@PostMapping("/ordine/annullaOrdine")
	public String annullaOrdine(@RequestParam("id")Long id) {
		this.ordineservice.eliminaOrdinePerID(id);
		return "index.html";
	}

	@PostMapping("/ordine/modificaOrdine")
	public String modificaOrdine(@RequestParam Long id, Model model) {

		Ordine o=this.ordineservice.getOrdineById(id);
		//mappa per la visualizzazioni dei prodotti nel form
		Map<Prodotto, Integer> pro_qt=  this.getMappaProdottoQuantitàDiUnOrdine(o); 
		model.addAttribute("ordine", o); 
		model.addAttribute("prodotti",pro_qt);

		return "/ordine/modificaOrdine.html";
	}



	@PostMapping("/ordine/riepilogoOrdineModificato")
	public String riepilogoOrdineModificato(@RequestParam Long id,
			@RequestParam Map<String, String> allParams, Model model) {

		// Recupera l'ordine
		Ordine ordine = this.ordineservice.getOrdineById(id);
		if (ordine == null) {
			model.addAttribute("errore", "Ordine non trovato per l'ID: " + id);
			return "/ordine/modificaOrdine.html";
		}
		//booleano per la verifica di un ordine vuoto
		boolean allZero = true;


		// Inizializza la lista se null
		if (ordine.getVociOrdine() == null) {
			ordine.setVociOrdine(new ArrayList<>());
		}

		// Lista temporanea per raccogliere le voci da rimuovere
		List<VoceOrdine> vociDaRimuovere = new ArrayList<>();

		// Gestione dei parametri (es. quantità per prodotto)
		//mappa: "quantita_idProdotto" "quantità"
		for (Map.Entry<String, String> entry : allParams.entrySet()) {
			//coppia "quantità" "prodotto"
			String paramName = entry.getKey();	//ottengo la chiave quantita_idProdotto
			if (paramName.startsWith("quantita_")) {
				String idStr = paramName.substring("quantita_".length());		//prendo solo l'id del prodtto
				try {
					Long productId = Long.parseLong(idStr);						//conversione da string a long
					int quantita = Integer.parseInt(entry.getValue());

					//per evitare che si creino ordini senza prodotti
					if (quantita > 0) {
						allZero = false;
					}

					Prodotto prodotto = prodottoService.getProdottoById(productId);
					if (prodotto == null) {
						model.addAttribute("errore", "Prodotto non trovato: ID=" + productId);
						model.addAttribute("ordine", ordine);
						model.addAttribute("prodotti", this.getMappaProdottoQuantitàDiUnOrdine(ordine));
						return "/ordine/modificaOrdine.html";
					}

					//una volta trovato il prodotto lo aggiungo alla lista di voci ordine oppure aggiorno tale lista
					boolean trovato = false;
					for (VoceOrdine voce : ordine.getVociOrdine()) {
						if (voce.getProdotto() != null && voce.getProdotto().getId().equals(productId)) {
							if (quantita > 0) {
								voce.setQuantità(quantita);
								voce.calcolaTotParziale();
							} else {
								voce.setQuantità(0);
								vociDaRimuovere.add(voce);
							}
							trovato = true;
							break;
						}
					}

					if (!trovato && quantita > 0) {
						VoceOrdine voceOrdine = new VoceOrdine();
						voceOrdine.setProdotto(prodotto);
						voceOrdine.setOrdine(ordine);
						voceOrdine.setQuantità(quantita);
						voceOrdine.calcolaTotParziale();
						ordine.getVociOrdine().add(voceOrdine);
					}

				} catch (NumberFormatException e) {
					model.addAttribute("errore", "Quantità non valida per il prodotto: " + idStr);
					model.addAttribute("ordine", ordine);
					model.addAttribute("prodotti", this.getMappaProdottoQuantitàDiUnOrdine(ordine));
					return "/ordine/modificaOrdine.html";
				}
			}
		}

		// Rimuove le voci marcate per l'eliminazione
		ordine.getVociOrdine().removeAll(vociDaRimuovere);

		// Verifica se l'ordine è vuoto
		if (allZero) {
			model.addAttribute("errore", "Ordine vuoto: nessun prodotto inserito");
			model.addAttribute("ordine", ordine);
			model.addAttribute("prodotti", this.getMappaProdottoQuantitàDiUnOrdine(ordine));
			return "/ordine/modificaOrdine.html";
		}

		// Calcola il totale e salva l'ordine
		ordine.setTotale(ordine.calculateTotal());
		this.ordineservice.save(ordine);

		return "redirect:/ordine/riepilogo/" + id;
	}


	/* lista tutti gli ordini (accessibile solo dall'admin) */
	@GetMapping ("/admin/ordini")
	public String tuttiOrdini (Model model) {
		model.addAttribute("ordini", ordineservice.getAll());
		return "/admin/ordini.html";
	}

	/* ordinamento lista ordini */
	@GetMapping("/admin/ordinaOrdini")
	public String ordinaOrdini(@RequestParam String ordine, @RequestParam String tipoRicerca, Model model) {
	    List<Ordine> ordini;
	    
	    // Ordina in base al criterio selezionato
	    if ("nome".equals(ordine)) {
	    	if (tipoRicerca.equals("ascendente"))
	    		ordini = this.ordineservice.getAllByOrderNomeAsc();
	    	else
	    		ordini = this.ordineservice.getAllByOrderNomeDesc();
	    } else if ("data".equals(ordine)) {
	    	if (tipoRicerca.equals("ascendente"))
	    		ordini = this.ordineservice.getAllByOrderByGiornoConsegnaAsc();
	    	else
	    		ordini = this.ordineservice.getAllByOrderByGiornoConsegnaDesc();
	    } else if ("totale".equals(ordine)) {
	    	if (tipoRicerca.equals("ascendente"))
	    		ordini = this.ordineservice.getAllByOrderByTotaleAsc();
	    	else
	    		ordini = this.ordineservice.getAllByOrderTotaleDesc();
	    } else {
	        ordini = (List<Ordine>)this.ordineservice.getAll(); // Default (non ordinato)
	    }

	    model.addAttribute("ordini", ordini);
	    return "/admin/ordini.html";
	}


	//Metodo ausiliario
	public Map<Prodotto, Integer> getMappaProdottoQuantitàDiUnOrdine(Ordine o) {
		Map<Prodotto, Integer> pro_qt= new HashMap<>();
		for(Prodotto p: this.prodottoService.getOrderedByCategoriaAsc()) {
			pro_qt.put(p, 0);
		}

		for(VoceOrdine v: o.getVociOrdine()) {
			pro_qt.replace(v.getProdotto(), v.getQuantità());
		}
		return pro_qt;
	}

}
