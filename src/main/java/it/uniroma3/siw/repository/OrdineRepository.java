package it.uniroma3.siw.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Ordine;


public interface OrdineRepository extends CrudRepository <Ordine, Long> {
	public List <Ordine> findByGiornoConsegna(LocalDate data);
	public List <Ordine> findAllByOrderByGiornoConsegnaAsc();
	public List <Ordine> findAllByOrderByNomeAsc ();
//	public List <Prenotazione> findByUtenteCredentialsUsername (String username);
	public List <Ordine> findByUtenteEmail(String email);


	
}
