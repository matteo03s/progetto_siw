package it.uniroma3.siw.controller;

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
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.service.ProdottoService;
import jakarta.validation.Valid;

@Controller
public class ProdottoController {
	
	@Autowired
	private ProdottoService prodottoService;
	
	@GetMapping ("/prova")
	public String getProva () {
		return "prova.html";
	}
	
	@GetMapping ("/prodotto/{id}")
	public String getProdotto (@PathVariable("id") Long id, Model model) {
		model.addAttribute("prodotto", this.prodottoService.getProdottoById(id));
		return "prodotto.html";
	}
	
	@GetMapping ("/prodotto")
	public String getProdotti (Model model) {
		model.addAttribute("prodotti", this.prodottoService.getAllProdotti());
		model.addAttribute("tipologia", new String ("prodotti"));
		return "prodotti.html";
	}
	
	@GetMapping("/ordinaProdotti")
	public String ordinaProdotti (@RequestParam String ordine, @RequestParam String tipologia, Model model) {
	    List<Prodotto> prodotti = null;
	    
	    System.out.println("Tipologia ricevuta: " + tipologia);  // Log per la tipologia
	    System.out.println("Ordine selezionato: " + ordine);    // Log per l'ordine

	    if (!("prodotti".equals(tipologia))) {
	    	 // Ordina in base al criterio selezionato
		    if ("nome".equals(ordine)) {
		        prodotti = this.prodottoService.getByCategoriaOrderedByNome(tipologia);
		    }
		    else if ("prezzo".equals(ordine)) {
		    	prodotti = this.prodottoService.getByCategoriaOrderedByPrezzo(tipologia);
		    } else {
		        prodotti = (List)this.prodottoService.getAllProdottiCategoria(tipologia); // Default (non ordinato)
		    }
	    }
	    else {
		    // Ordina in base al criterio selezionato
		    if ("nome".equals(ordine)) {
		        prodotti = this.prodottoService.getOrderedByNome();
		    } else if ("categoria".equals(ordine)) {
		        prodotti = this.prodottoService.getOrderedByCategoria();
		    } else if ("prezzo".equals(ordine)) {
		    	prodotti = this.prodottoService.getOrderedByPrezzo();
		    } else {
		        prodotti = (List)this.prodottoService.getAllProdotti(); // Default (non ordinato)
		    }
	    }
	    model.addAttribute("prodotti", prodotti);
	    model.addAttribute("tipologia", tipologia);
	    return "prodotti.html";
	}
	
	@GetMapping ("/admin/formInserimentoMenu")
	public String formNewInserimentoMenu (Model model) {
		model.addAttribute("prodotto", new Prodotto());
		return "formInserimentoMenu.html";
	}
	
	@GetMapping ("/formNewProdotto")
	public String formNewProdotto (Model model) {
		model.addAttribute("prodotto", new Prodotto());
		return "formNewProdotto.html";
	}
	
	@PostMapping ("/prodotto")
	public String newProdotto (@Valid @ModelAttribute("prodotto") Prodotto prodotto, BindingResult bindingResult, Model model){
		if (bindingResult.hasErrors()) {
			return "formNewProdotto.html";
		}
		this.prodottoService.save(prodotto);
		model.addAttribute("prodotto", prodotto);
		return "redirect:prodotto/" + prodotto.getId();
	}
	

	@GetMapping ("/pizza")
	public String getPizze (Model model) {
		model.addAttribute("prodotti", this.prodottoService.getAllPizze());
		model.addAttribute("tipologia", new String ("pizza"));
		return "prodotti.html";
	}
	
	@GetMapping ("/formNewPizza")
	public String formNewPizza(Model model) {
		Prodotto p = new Prodotto();
		p.setCategoria("pizza");
		model.addAttribute("prodotto", p);
		return "formNewProdotto.html";
	}
	
	@GetMapping ("/sfizio")
	public String getSfizi (Model model) {
		model.addAttribute("prodotti", this.prodottoService.getAllSfizi());
		model.addAttribute("tipologia", new String ("sfizio"));
		return "prodotti.html";
	}
	@GetMapping ("/formNewSfizio")
	public String formNewSfizio(Model model) {
		Prodotto p = new Prodotto();
		p.setCategoria("sfizio");
		model.addAttribute("prodotto", p);
		return "formNewProdotto.html";
	}
	
	@GetMapping ("/dolce")
	public String getDolci (Model model) {
		model.addAttribute("prodotti", this.prodottoService.getAllDolci());
		model.addAttribute("tipologia", new String ("dolce"));
		return "prodotti.html";
	}
	@GetMapping ("/formNewDolce")
	public String formNewDolce(Model model) {
		Prodotto p = new Prodotto();
		p.setCategoria("dolce");
		model.addAttribute("prodotto", p);
		return "formNewProdotto.html";
	}
	
	@GetMapping ("/bevanda")
	public String getBevande (Model model) {
		model.addAttribute("prodotti", this.prodottoService.getAllBevande());
		model.addAttribute("tipologia", new String ("bevanda"));
		return "prodotti.html";
	}
	
	@GetMapping ("/formNewBevanda")
	public String formNewBevanda(Model model) {
		Prodotto p = new Prodotto();
		p.setCategoria("bevanda");
		model.addAttribute("prodotto", p);
		return "formNewProdotto.html";
	}
}
