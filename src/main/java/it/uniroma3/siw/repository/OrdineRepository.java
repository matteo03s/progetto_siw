package it.uniroma3.siw.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Ordine;


public interface OrdineRepository extends CrudRepository <Ordine, Long> {
	public List <Ordine> findByGiornoConsegna(LocalDate data);
	public List <Ordine> findAllByOrderByGiornoConsegnaAsc();
	public List <Ordine> findAllByOrderByGiornoConsegnaDesc();
	public List <Ordine> findAllByOrderByNomeAsc ();
	public List <Ordine> findAllByOrderByNomeDesc ();
	public List <Ordine> findAllByOrderByTotaleAsc ();
	public List <Ordine> findAllByOrderByTotaleDesc ();
//	public List <Prenotazione> findByUtenteCredentialsUsername (String username);
	public List <Ordine> findByUtenteEmail(String email);
	public Ordine findByGiornoConsegnaAndOrarioConsegna(LocalDate data, LocalTime orario);
	public Ordine findFirstByOrderByIdDesc();
	public void deleteById(Long id);



	
}
