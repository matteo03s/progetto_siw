package it.uniroma3.siw.service;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Prodotto;
import it.uniroma3.siw.repository.ProdottoRepository;

@Service
public class ProdottoService {
	
	@Autowired
	private ProdottoRepository prodottoRepository;
	
	public Iterable <Prodotto> getAllProdotti () {
		return prodottoRepository.findAll();
	}
	
	public Prodotto getProdottoById (Long id) {
		return prodottoRepository.findById(id)
		.orElseThrow(() -> new NoSuchElementException("Prodotto non trovato con id: " + id));
	}
	
	public List <Prodotto> getAllProdottiCategoria (String categoria) {
		return prodottoRepository.findByCategoria(categoria);
	}
	
	public List <Prodotto> getProdottiPrezzo (Float prezzo) {
		return prodottoRepository.findByPrezzo(prezzo);
	}

	public List <Prodotto> getAllPizze () { 
		return prodottoRepository.findByCategoria("pizza");
	}
	public List <Prodotto> getAllSfizi () { 
		return prodottoRepository.findByCategoria("sfizio");
	}
	public List <Prodotto> getAllDolci () { 
		return prodottoRepository.findByCategoria("dolce");
	}
	public List <Prodotto> getAllBevande () { 
		return prodottoRepository.findByCategoria("bevanda");
	}
	
	public List <Prodotto> getOrderedByPrezzoAsc () {
		return prodottoRepository.findAllByOrderByPrezzoAsc();
	}
	public List <Prodotto> getOrderedByNomeAsc () {
		return prodottoRepository.findAllByOrderByNomeAsc();
	}
	public List<Prodotto> getOrderedByCategoriaAsc () {
		return prodottoRepository.findAllByOrderByCategoriaAsc();
	}
	public List<Prodotto> getByCategoriaOrderedByPrezzoAsc(String categoria) {	
		return prodottoRepository.findByCategoriaOrderByPrezzoAsc(categoria);
	}
	public List<Prodotto> getByCategoriaOrderedByNomeAsc(String categoria) {	
		return prodottoRepository.findByCategoriaOrderByNomeAsc(categoria);
	}
	
	public List <Prodotto> getOrderedByPrezzoDesc () {
		return prodottoRepository.findAllByOrderByPrezzoDesc();
	}
	public List <Prodotto> getOrderedByNomeDesc () {
		return prodottoRepository.findAllByOrderByNomeDesc();
	}
	public List<Prodotto> getOrderedByCategoriaDesc() {
		return prodottoRepository.findAllByOrderByCategoriaDesc();
	}
	public List<Prodotto> getByCategoriaOrderedByPrezzoDesc(String categoria) {	
		return prodottoRepository.findByCategoriaOrderByPrezzoDesc(categoria);
	}
	public List<Prodotto> getByCategoriaOrderedByNomeDesc(String categoria) {	
		return prodottoRepository.findByCategoriaOrderByNomeDesc(categoria);
	}
	public List <Prodotto> getVetrina () {
		List <Prodotto> vetrina = new ArrayList<>();
		List <Prodotto> pizze = this.getAllPizze();
		List <Prodotto> sfizi = this.getAllSfizi();
		List <Prodotto> dolci = this.getAllDolci();
		Collections.shuffle(pizze);
		Collections.shuffle(sfizi);
		Collections.shuffle(dolci);
		vetrina.addAll(pizze.subList(0, 2));
		vetrina.addAll(sfizi.subList(0, 2));
		vetrina.addAll(dolci.subList(0, 2));
//		vetrina.add(pizze.getFirst());
//		vetrina.add(dolci.getFirst());
		
		return vetrina;
	}
	
	public Prodotto save(Prodotto prodotto) {
		return prodottoRepository.save(prodotto);
	}

	public void remove(Long id) {
		if (prodottoRepository.findById(id).isPresent())
			prodottoRepository.deleteById(id);
		else
			throw new NoSuchElementException("Prodotto non trovato con id: " + id);
	}
	

}
