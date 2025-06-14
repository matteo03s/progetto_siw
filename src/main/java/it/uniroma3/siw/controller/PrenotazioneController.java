package it.uniroma3.siw.controller;

import java.security.Principal;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Prenotazione;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.PrenotazioneService;
import it.uniroma3.siw.service.UserService;
import jakarta.validation.Valid;

@Controller
public class PrenotazioneController {

	@Autowired
	private PrenotazioneService prenotazioneService;

	@Autowired
	private UserService userService;

	
	/* lista prenotazioni dell'utente (username passato automaticamente grazie a principal) */
	@GetMapping ("/prenotazione/leMiePrenotazioni")
	public String prenotazioniUtente (Model model, Principal principal) {
		model.addAttribute("prenotazioni", this.prenotazioneService.getPrenotazioniUsername(principal.getName()));
		return "/prenotazione/prenotazioni.html";
	}
	
	/* lista tutte le prenotazioni (accessibile solo dall'admin) */
	@GetMapping ("/admin/prenotazioni")
	public String tuttePrenotazioni (Model model) {
		model.addAttribute("prenotazioni", prenotazioneService.getAll());
		return "/admin/prenotazioni.html";
	}
	
	/* ordinamento lista prenotazioni */
	@GetMapping("/admin/ordinaPrenotazioni")
	public String ordinaPrenotazioni(@RequestParam String ordine, Model model) {
	    List<Prenotazione> prenotazioni;
	    
	    // Ordina in base al criterio selezionato
	    if ("nome".equals(ordine)) {
	        prenotazioni = this.prenotazioneService.getOrderedByNome();
	    } else if ("data".equals(ordine)) {
	        prenotazioni = this.prenotazioneService.getOrderedByData();
	    } else if ("posti".equals(ordine)) {
	        prenotazioni = this.prenotazioneService.getOrderedByPosti();
	    } else {
	        prenotazioni = (List<Prenotazione>)this.prenotazioneService.getAll(); // Default (non ordinato)
	    }

	    model.addAttribute("prenotazioni", prenotazioni);
	    return "/admin/prenotazioni.html";
	}
	
	/* per inserire una nuova prenotazione */
	@GetMapping ("/prenotazione/formNewPrenotazione")
	public String formNewPrenotazione (Model model) {
		model.addAttribute("prenotazione", new Prenotazione());
		return "/prenotazione/formNewPrenotazione.html";
	}
	
	/* creazione nuova prenotazione (con controllo se ci sono abbastanza posti) */
	@PostMapping("/prenotazione/prenotazione")
    public String newPrenotazione(@Valid @ModelAttribute("prenotazione") Prenotazione prenotazione, BindingResult bindingResult, Model model, Principal principal) {
       if (bindingResult.hasErrors()) {
    	   return "/prenotazione/formNewPrenotazione.html";
       }
       try {
    	   String message = (prenotazioneService.creaPrenotazione(prenotazione, principal.getName()));
    	   model.addAttribute("message", message);
    	   return "redirect:/prenotazione/leMiePrenotazioni";
       } catch (RuntimeException e) {
    	   model.addAttribute("errorMessage", e.getMessage());
    	   return "/prenotazione/formNewPrenotazione.html";
       }
//		String s = prenotazioneService.creaPrenotazione(prenotazione);
//        model.addAttribute("prenotazioni", this.prenotazioneService.getAll());
//		model.addAttribute("descrizione", s);
// 		return "prenotazioni.html";
    }
	
	@GetMapping("/admin/prenotazioni/{id}") 
	public String getPrenotazioniUtente (@PathVariable ("id") Long id,
										Model model) {
		User utente = userService.getUser(id);
		
		model.addAttribute("prenotazioni", utente.getPrenotazioni());
		return "/admin/prenotazioni.html";
	}
	
    @GetMapping ("/admin/cercaPrenotazioni")
    public String adminFiltraPrenotazioni (@RequestParam String filtro, Model model) {
    	List <Prenotazione> prenotazioni = new LinkedList<Prenotazione>();
    	
    	for (Prenotazione p: this.prenotazioneService.getAll()){
    		if (p.getNome().toLowerCase().contains(filtro.toLowerCase()))
    			prenotazioni.add(p);
    	}

    	model.addAttribute ("numero", prenotazioni.size());
    	model.addAttribute ("prenotazioni", prenotazioni);
    	return "/admin/prenotazioni.html"; 
    }
}
