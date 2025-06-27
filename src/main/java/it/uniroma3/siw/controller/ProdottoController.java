package it.uniroma3.siw.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Prenotazione;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ProdottoService;
import jakarta.validation.Valid;

@Controller
public class ProdottoController {
	
	@Autowired
	private ProdottoService prodottoService;
	@Autowired
	private CredentialsService credentialsService;
	
	@GetMapping ("/prova")
	public String getProva () {
		return "prova.html";
	}
	
	@GetMapping ("/prodotto/{id}")
	public String getProdotto (@PathVariable("id") Long id, Model model) {
		Prodotto p;
		try {
			p = this.prodottoService.getProdottoById(id);
		} catch (NoSuchElementException e) {
			model.addAttribute ("errorMessage", e.getMessage());
			return "error/500.html";
		}
		model.addAttribute("prodotto", p);
		return "prodotto.html";
	}
	
	@Transactional
	@GetMapping ("/menu")
	public String getProdotti (Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			model.addAttribute("prodotti", this.prodottoService.getAllProdotti());
			model.addAttribute("tipologia", new String ("prodotti"));
			return "prodotti.html";
		}
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		if (credentials.getRole().equals(Credentials.PROVIDER_ROLE)) {
			model.addAttribute("prodotti", this.prodottoService.getAllProdotti());
			return "/admin/prodotto/modificaProdotti.html";
		}
		model.addAttribute("prodotti", this.prodottoService.getAllProdotti());
		model.addAttribute("tipologia", new String ("prodotti"));
		return "prodotti.html";
	}
	
	@Transactional
	@GetMapping("/ordinaProdotti")
	public String ordinaProdotti (@RequestParam String ordine, @RequestParam String tipoRicerca, @RequestParam String tipologia, Model model) {
	    List<Prodotto> prodotti = null;
	    String tipo = null;

	    if (!("prodotti".equals(tipologia))) {
	    	if(("pizze".equals(tipologia)))
	    		tipo = "pizza";
	    	if(("sfizi".equals(tipologia)))
	    		tipo = "sfizio";
	    	if(("dolci".equals(tipologia)))
	    		tipo = "dolce";
	    	if(("bevande".equals(tipologia)))
	    		tipo = "bevanda";

	    	// Ordina in base al criterio selezionato
		    if ("nome".equals(ordine)) {
		    	if (tipoRicerca.equals("ascendente"))
		    		prodotti = this.prodottoService.getByCategoriaOrderedByNomeAsc(tipo);
		    	else
		    		prodotti = this.prodottoService.getByCategoriaOrderedByNomeDesc(tipo);
		    }
		    else if ("prezzo".equals(ordine)) {
		    	if (tipoRicerca.equals("ascendente"))
		    		prodotti = this.prodottoService.getByCategoriaOrderedByPrezzoAsc(tipo);
		    	else
		    		prodotti = this.prodottoService.getByCategoriaOrderedByPrezzoDesc(tipo);
		    } else {
		        prodotti = (List)this.prodottoService.getAllProdottiCategoria(tipo); // Default (non ordinato)
		    }
	    }
	    else {
		    // Ordina in base al criterio selezionato
		    if ("nome".equals(ordine)) {
		    	if (tipoRicerca.equals("ascendente"))
		    		prodotti = this.prodottoService.getOrderedByNomeAsc();
		    	else
		    		prodotti = this.prodottoService.getOrderedByNomeDesc();
		    } else if ("categoria".equals(ordine)) {
		    	if (tipoRicerca.equals("ascendente"))
		    		prodotti = this.prodottoService.getOrderedByCategoriaAsc();
		    	else
		    		prodotti = this.prodottoService.getOrderedByCategoriaDesc();
		    } else if ("prezzo".equals(ordine)) {
		    	if (tipoRicerca.equals("ascendente"))
		    		prodotti = this.prodottoService.getOrderedByPrezzoAsc();
		    	else
		    		prodotti = this.prodottoService.getOrderedByPrezzoDesc();
		    } else {
		        prodotti = (List)this.prodottoService.getAllProdotti(); // Default (non ordinato)
		    }
	    }
	    model.addAttribute("prodotti", prodotti);
	    model.addAttribute("tipologia", tipologia);
	    return "prodotti.html";
	}
	
	@GetMapping("/admin/ordinaProdotti")
	public String adminOrdinaProdotti (@RequestParam String ordine, @RequestParam String tipoRicerca, Model model) {
	    List<Prodotto> prodotti = null;
	    
	    // Ordina in base al criterio selezionato
	    if ("nome".equals(ordine)) {
	    	if (tipoRicerca.equals("ascendente"))
	    		prodotti = this.prodottoService.getOrderedByNomeAsc();
	    	else
	    		prodotti = this.prodottoService.getOrderedByNomeDesc();
	    } else if ("categoria".equals(ordine)) {
	    	if (tipoRicerca.equals("ascendente"))
	    		prodotti = this.prodottoService.getOrderedByCategoriaAsc();
	    	else
	    		prodotti = this.prodottoService.getOrderedByCategoriaDesc();
	    } else if ("prezzo".equals(ordine)) {
	    	if (tipoRicerca.equals("ascendente"))
	    		prodotti = this.prodottoService.getOrderedByPrezzoAsc();
	    	else
	    		prodotti = this.prodottoService.getOrderedByPrezzoDesc();
	    } else {
	        prodotti = (List)this.prodottoService.getAllProdotti(); // Default (non ordinato)
	    }
	    model.addAttribute("prodotti", prodotti);
	    return "/admin/prodotto/modificaProdotti.html";
	}
	
	@GetMapping ("/admin/formNewProdotto")
	public String formNewProdotto (Model model) {
		model.addAttribute("prodotto", new Prodotto());
		return "/admin/prodotto/formNewProdotto.html";
	}
		
	@Transactional
	@GetMapping ("/menu/pizza")
	public String getPizze (Model model) {
		model.addAttribute("prodotti", this.prodottoService.getAllPizze());
		model.addAttribute("tipologia", new String ("pizze"));
		return "prodotti.html";
	}

	@GetMapping ("/admin/newPizza")
	public String formNewPizza(Model model) {
		Prodotto p = new Prodotto();
		p.setCategoria("pizza");
		model.addAttribute("prodotto", p);
		return "/admin/prodotto/formNewProdotto.html";
	}
	@Transactional
	@GetMapping ("/menu/sfizio")
	public String getSfizi (Model model) {
		model.addAttribute("prodotti", this.prodottoService.getAllSfizi());
		model.addAttribute("tipologia", new String ("sfizi"));
		return "prodotti.html";
	}
	@GetMapping ("/admin/newSfizio")
	public String formNewSfizio(Model model) {
		Prodotto p = new Prodotto();
		p.setCategoria("sfizio");
		model.addAttribute("prodotto", p);
		return "/admin/prodotto/formNewProdotto.html";
	}
	@Transactional
	@GetMapping ("/menu/dolce")
	public String getDolci (Model model) {
		model.addAttribute("prodotti", this.prodottoService.getAllDolci());
		model.addAttribute("tipologia", new String ("dolci"));
		return "prodotti.html";
	}
	@GetMapping ("/admin/newDolce")
	public String formNewDolce(Model model) {
		Prodotto p = new Prodotto();
		p.setCategoria("dolce");
		model.addAttribute("prodotto", p);
		return "/admin/prodotto/formNewProdotto.html";
	}
	@Transactional
	@GetMapping ("/menu/bevanda")
	public String getBevande (Model model) {
		model.addAttribute("prodotti", this.prodottoService.getAllBevande());
		model.addAttribute("tipologia", new String ("bevande"));
		return "prodotti.html";
	}
	
	@GetMapping ("/admin/newBevanda")
	public String formNewBevanda(Model model) {
		Prodotto p = new Prodotto();
		p.setCategoria("bevanda");
		model.addAttribute("prodotto", p);
		return "/admin/prodotto/formNewProdotto.html";
	}
	
	@GetMapping ("/admin/modificaProdotti")
	public String modificaMenu(Model model) {
		model.addAttribute("prodotti", this.prodottoService.getAllProdotti());
		return "/admin/prodotto/modificaProdotti.html";
	}
	
	@GetMapping ("/admin/cancellaProdotto/{id}")
	public String cancellaProdotto (@PathVariable ("id") Long id, Model model) {
		this.prodottoService.remove(id);
		return "redirect:/admin/modificaProdotti";
	}
		
	@GetMapping ("/admin/modificaProdotto/{id}")
	public String modificaProdotto (@PathVariable ("id") Long id, Model model) {
		Prodotto prodotto = this.prodottoService.getProdottoById(id);
		model.addAttribute("prodotto", prodotto);
		return "/admin/prodotto/modificaProdotto.html";
	}
	
	@PostMapping ("/admin/prodotto")
	public String newProdotto (@Valid @ModelAttribute("prodotto") Prodotto prodotto, BindingResult bindingResult, Model model){
		if (bindingResult.hasErrors()) {
			return "/admin/prodotto/formNewProdotto.html";
		}
		this.prodottoService.save(prodotto);
		model.addAttribute("prodotto", prodotto);
		return "redirect:/admin/modificaProdotto/" + prodotto.getId();
	}


    @PostMapping("/admin/prodotti/{id}")
    public String modificaProdotto(@PathVariable("id") Long id, 
                                  @ModelAttribute("prodotto") Prodotto prodottoForm, 
                                  BindingResult bindingResult) {
//        // Validazione manuale per consentire campi vuoti
//        if (prodottoForm.getNome() != null && prodottoForm.getNome().trim().isEmpty()) {
//            bindingResult.rejectValue("nome", "error.nome", "Il nome non può essere vuoto");
//        }

        if (bindingResult.hasErrors()) {
            return "/admin/prodotto/modificaProdotto.html";
        }

        // Carica il prodotto esistente dal database
        Prodotto prodottoEsistente = prodottoService.getProdottoById(id);
        if (prodottoEsistente == null) {
            return "redirect:/admin/modificaProdotti";
        }

        // Aggiorna solo i campi modificati
        if (prodottoForm.getNome() != null && !prodottoForm.getNome().trim().isEmpty()) {
            prodottoEsistente.setNome(prodottoForm.getNome());
        }
        if (prodottoForm.getDescrizione() != null && !prodottoForm.getDescrizione().trim().isEmpty()) {
            prodottoEsistente.setDescrizione(prodottoForm.getDescrizione());
        } else if (prodottoForm.getDescrizione() != null && prodottoForm.getDescrizione().trim().isEmpty()) {
            prodottoEsistente.setDescrizione(null); // Permette di azzerare la descrizione
        }

        if (prodottoForm.getPrezzo() != null) {
            prodottoEsistente.setPrezzo(prodottoForm.getPrezzo());
        }

        // I campi categoria e prezzo non sono nel form, quindi rimangono invariati

        // Salva l'entità esistente (aggiorna il record esistente)
        prodottoService.save(prodottoEsistente);
        return "redirect:/admin/modificaProdotti";
    }
    
    @Transactional
    @GetMapping ("/cercaProdotti")
    public String filtraProdotti (@RequestParam String filtro, @RequestParam String tipologia, Model model) {
    	List <Prodotto> prodotti = new LinkedList<Prodotto>();
    	List <Prodotto> totali = new LinkedList<Prodotto>();
    	String tipo = null;
    	
    	if (!("prodotti".equals(tipologia))) {
	    	if(("pizze".equals(tipologia)))
	    		tipo = "pizza";
	    	if(("sfizi".equals(tipologia)))
	    		tipo = "sfizio";
	    	if(("dolci".equals(tipologia)))
	    		tipo = "dolce";
	    	if(("bevande".equals(tipologia)))
	    		tipo = "bevanda";
	    	totali = (List<Prodotto>) this.prodottoService.getAllProdottiCategoria(tipo);
    	}
    	else
    		totali = (List<Prodotto>) this.prodottoService.getAllProdotti();
    	
    	for (Prodotto p: totali) {
    		if (p.getNome().toLowerCase().contains(filtro.toLowerCase()))
    			prodotti.add(p);
    	}

    	model.addAttribute ("numero", prodotti.size());
    	model.addAttribute ("tipologia", tipologia);
    	model.addAttribute ("prodotti", prodotti);
    	return "prodotti.html"; 
    }
    
    @GetMapping ("/admin/cercaProdotti")
    public String adminFiltraProdotti (@RequestParam String filtro, @RequestParam String tipoRicerca, Model model) {
    	List <Prodotto> prodotti = new LinkedList<Prodotto>();
    	
    	for (Prodotto p: this.prodottoService.getAllProdotti()) {
    		if (tipoRicerca.equals("nome")) {
    			if (p.getNome().toLowerCase().contains(filtro.toLowerCase()))
        			prodotti.add(p);
    		}
    		
    		else {
    			if (p.getCategoria().toLowerCase().contains(filtro.toLowerCase()))
        			prodotti.add(p);
    		}
    	}

    	model.addAttribute ("numero", prodotti.size());
    	model.addAttribute ("prodotti", prodotti);
    	return "/admin/prodotto/modificaProdotti.html"; 
    }
}
