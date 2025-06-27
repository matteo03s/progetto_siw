package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Prodotto;

public interface ProdottoRepository extends CrudRepository <Prodotto, Long> {
	//trova il prodotto tramite il nome
	public Prodotto findByNome (String nome);
	//trova tutti i prodotti che hanno un certo prezzo
	public List <Prodotto> findByPrezzo (float prezzo);
	//trova tutti i prodotti di una certa categoria
	public List <Prodotto> findByCategoria (String categoria);
	//trova tutti i prodotti che hanno un prezzo non minore di quello passato
	public List <Prodotto> findByPrezzoGreaterThanEqual (float prezzo);
	//trova tutti i prodotti che hanno un prezzo non maggiore di quello passato
	public List <Prodotto> findByPrezzoLessThanEqual (float prezzo);
	//ordina i prodotti per prezzo ascendente
	public List <Prodotto> findAllByOrderByPrezzoAsc();
	//ordina i prodotti per prezzo discendente
	public List <Prodotto> findAllByOrderByPrezzoDesc();
	public List<Prodotto> findAllByOrderByNomeAsc();
	public List<Prodotto> findAllByOrderByNomeDesc();
	public List<Prodotto> findAllByOrderByCategoriaAsc();
	public List<Prodotto> findAllByOrderByCategoriaDesc();
	// Metodo per trovare tutti i prodotti di una specifica categoria e ordinarli per prezzo
    public List<Prodotto> findByCategoriaOrderByPrezzoAsc(String categoria);
    public List<Prodotto> findByCategoriaOrderByPrezzoDesc(String categoria);
    // Metodo per trovare tutti i prodotti di una specifica categoria e ordinarli per prezzo
    public List<Prodotto> findByCategoriaOrderByNomeAsc(String categoria);
    public List<Prodotto> findByCategoriaOrderByNomeDesc(String categoria);
}
