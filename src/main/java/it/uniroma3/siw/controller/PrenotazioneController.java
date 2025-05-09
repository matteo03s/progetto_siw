package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Prenotazione;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.service.PrenotazioneService;
import jakarta.validation.Valid;

@Controller
public class PrenotazioneController {

	@Autowired
	private PrenotazioneService prenotazioneService;
	
	@GetMapping ("/prenotazione")
	public String getPrenotazioni (Model model) {
		model.addAttribute("prenotazioni", this.prenotazioneService.getAll());
		return "prenotazioni.html";
	}
	
	@GetMapping("/ordinaPrenotazioni")
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
	        prenotazioni = (List)this.prenotazioneService.getAll(); // Default (non ordinato)
	    }

	    model.addAttribute("prenotazioni", prenotazioni);
	    return "prenotazioni.html";
	}

	
	@GetMapping ("/formNewPrenotazione")
	public String formNewPrenotazione (Model model) {
		model.addAttribute("prenotazione", new Prenotazione());
		return "/prenotazione/formNewPrenotazione.html";
	}
	
	@PostMapping("/prenotazione")
    public String newPrenotazione(@Valid @ModelAttribute("prenotazione") Prenotazione prenotazione, BindingResult bindingResult, Model model) {
       if (bindingResult.hasErrors()) {
    	   return "/prenotazione/formNewPrenotazione.html";
       }
		String s = prenotazioneService.creaPrenotazione(prenotazione);
        model.addAttribute("prenotazioni", this.prenotazioneService.getAll());
		model.addAttribute("descrizione", s);
        return "redirect:prenotazione";
// 		return "prenotazioni.html";
    }
}
