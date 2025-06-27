package it.uniroma3.siw.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Immagine;
import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.service.ImmagineService;
import it.uniroma3.siw.service.ProdottoService;

@Controller
public class ImmagineController {

	@Autowired
    private ImmagineService immagineService;

    @Autowired
    private ProdottoService prodottoService;
	
    
    // Serve image data
    @GetMapping("/immagine/{id}")
    public ResponseEntity<byte[]> getImmagine(@PathVariable("id") Long id) {
        Immagine immagine = immagineService.getImmagineById(id);
        if (immagine == null || immagine.getDati() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(immagine.getTipoContenuto()))
                .body(immagine.getDati());
    }
    
    @GetMapping("/admin/modificaImmagineProdotto/{prodottoId}")
    public String getCaricamentoImmagine(@PathVariable("prodottoId") Long prodottoId, Model model) {

		Prodotto prodotto = prodottoService.getProdottoById(prodottoId);
        if (prodotto == null) {
            return "redirect:/admin/modificaProdotti";
        }
        model.addAttribute("prodotto", prodotto);
        return "/admin/prodotto/caricaImmagine.html";
    }

    @PostMapping("/admin/modificaImmagineProdotto/{prodottoId}")
    public String caricaImmagine(@PathVariable("prodottoId") Long prodottoId,
                                 @RequestParam("file") MultipartFile file,
                                 Model model) {
        try {
           Prodotto prodotto = prodottoService.getProdottoById(prodottoId);
            if (prodotto == null) {
                return "redirect:/admin/modificaProdotti";
            }
            immagineService.saveImmagineProdotto(file, prodotto);
            return "redirect:/admin/modificaProdotto/" + prodottoId;
        } catch (IOException e) {
            model.addAttribute("error", "Errore nel caricamento dell'immagine.");
            model.addAttribute("prodotto", prodottoService.getProdottoById(prodottoId));
            return "admin/prodotto/caricaImmagine.html";
        }
    }
    
    @GetMapping("/admin/cancellaImmagineProdotto/{prodottoId}/{immagineId}")
    public String cancellaImmagine (@PathVariable("prodottoId") Long prodottoId, @PathVariable("immagineId") Long immagineId, Model model) {
    	Prodotto prodotto = this.prodottoService.getProdottoById(prodottoId);
    	prodotto.setImmagine(null);
    	this.immagineService.removeImmagine(immagineId);
		return "redirect:/admin/modificaImmagineProdotto/" + prodottoId;
	}
}
