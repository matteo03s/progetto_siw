package it.uniroma3.siw.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
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
			return "/admin/modificaProdotti.html";
		}
		model.addAttribute("prodotti", this.prodottoService.getAllProdotti());
		model.addAttribute("tipologia", new String ("prodotti"));
		return "prodotti.html";
	}
	
	@GetMapping("/ordinaProdotti")
	public String ordinaProdotti (@RequestParam String ordine, @RequestParam String tipologia, Model model) {
	    List<Prodotto> prodotti = null;
	    String tipo = null;
	    System.out.println("Tipologia ricevuta: " + tipologia);  // Log per la tipologia
	    System.out.println("Ordine selezionato: " + ordine);    // Log per l'ordine

	    if (!("prodotti".equals(tipologia))) {
	    	if(("pizze".equals(tipologia)))
	    		tipo = "pizza";
	    	if(("sfizi".equals(tipologia)))
	    		tipo = "sfizio";
	    	if(("dolci".equals(tipologia)))
	    		tipo = "sfizio";
	    	if(("bevande".equals(tipologia)))
	    		tipo = "bevanda";
	    	 // Ordina in base al criterio selezionato
		    if ("nome".equals(ordine)) {
		        prodotti = this.prodottoService.getByCategoriaOrderedByNome(tipo);
		    }
		    else if ("prezzo".equals(ordine)) {
		    	prodotti = this.prodottoService.getByCategoriaOrderedByPrezzo(tipo);
		    } else {
		        prodotti = (List)this.prodottoService.getAllProdottiCategoria(tipo); // Default (non ordinato)
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
	
	@GetMapping("/admin/ordinaProdotti")
	public String adminOrdinaProdotti (@RequestParam String ordine, Model model) {
	    List<Prodotto> prodotti = null;
	    
	    System.out.println("Ordine selezionato: " + ordine);    // Log per l'ordine

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
	    model.addAttribute("prodotti", prodotti);
	    return "/admin/modificaProdotti.html";
	}
	
	@GetMapping ("/admin/formInserimentoMenu")
	public String formNewInserimentoMenu (Model model) {
		model.addAttribute("prodotto", new Prodotto());
		return "/admin/formInserimentoMenu.html";
	}
	
	@GetMapping ("/admin/formNewProdotto")
	public String formNewProdotto (Model model) {
		model.addAttribute("prodotto", new Prodotto());
		return "/admin/formNewProdotto.html";
	}
		

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
		return "/admin/formNewProdotto.html";
	}
	
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
		return "/admin/formNewProdotto.html";
	}
	
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
		return "/admin/formNewProdotto.html";
	}
	
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
		return "/admin/formNewProdotto.html";
	}
	
	@GetMapping ("/admin/modificaProdotti")
	public String modificaMenu(Model model) {
		model.addAttribute("prodotti", this.prodottoService.getAllProdotti());
		return "/admin/modificaProdotti.html";
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
		return "/admin/modificaProdotto.html";
	}
	
	@PostMapping ("/admin/prodotto")
	public String newProdotto (@Valid @ModelAttribute("prodotto") Prodotto prodotto, BindingResult bindingResult, Model model){
		if (bindingResult.hasErrors()) {
			return "/admin/formNewProdotto.html";
		}
		this.prodottoService.save(prodotto);
		model.addAttribute("prodotto", prodotto);
		return "redirect:/admin/modificaProdotti";// + prodotto.getId();
	}
	
//	@PostMapping("/admin/prodotti/{id}")
//    public String modificaProdotto(@PathVariable("id") Long id, @Valid @ModelAttribute("prodotto") Prodotto prodotto, BindingResult bindingResult) {
//		if (bindingResult.hasErrors()) {
//			return "/admin/modificaProdotto.html";
//		}
//		prodotto.setId(id);
//        prodottoService.save(prodotto);
//        return "redirect:/admin/modificaProdotti"; // Redireziona alla lista dopo la modifica
//    }


    @PostMapping("/admin/prodotti/{id}")
    public String modificaProdotto(@PathVariable("id") Long id, 
                                  @ModelAttribute("prodotto") Prodotto prodottoForm, 
                                  BindingResult bindingResult) {
        // Validazione manuale per consentire campi vuoti
        if (prodottoForm.getNome() != null && prodottoForm.getNome().trim().isEmpty()) {
            bindingResult.rejectValue("nome", "error.nome", "Il nome non può essere vuoto");
        }

        if (bindingResult.hasErrors()) {
            return "/admin/modificaProdotto.html";
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
        if (prodottoForm.getUrlImage() != null && !prodottoForm.getUrlImage().trim().isEmpty()) {
            prodottoEsistente.setUrlImage(prodottoForm.getUrlImage());
        } else if (prodottoForm.getUrlImage() != null && prodottoForm.getUrlImage().trim().isEmpty()) {
            prodottoEsistente.setUrlImage(null); // Permette di azzerare l'URL dell'immagine
        }
        
        if (prodottoForm.getPrezzo() != null) {
            prodottoEsistente.setPrezzo(prodottoForm.getPrezzo());
        }

        // I campi categoria e prezzo non sono nel form, quindi rimangono invariati

        // Salva l'entità esistente (aggiorna il record esistente)
        prodottoService.save(prodottoEsistente);
        return "redirect:/admin/modificaProdotti";
    }
}
