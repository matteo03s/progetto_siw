package it.uniroma3.siw.service;


import java.util.List;

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
		return prodottoRepository.findById(id).get();
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
	
	public List <Prodotto> getOrderedByPrezzo () {
		return prodottoRepository.findAllByOrderByPrezzoAsc();
	}
	public List <Prodotto> getOrderedByNome () {
		return prodottoRepository.findAllByOrderByNomeAsc();
	}
	public List<Prodotto> getOrderedByCategoria() {
		return prodottoRepository.findAllByOrderByCategoriaAsc();
	}
	public List<Prodotto> getByCategoriaOrderedByPrezzo(String categoria) {	
		return prodottoRepository.findByCategoriaOrderByPrezzoAsc(categoria);
	}
	public List<Prodotto> getByCategoriaOrderedByNome(String categoria) {	
		return prodottoRepository.findByCategoriaOrderByNomeAsc(categoria);
	}
	
	public Prodotto save(Prodotto prodotto) {
		return prodottoRepository.save(prodotto);
	}

	public void remove(Long id) {
		prodottoRepository.deleteById(id);
	}
	

}
