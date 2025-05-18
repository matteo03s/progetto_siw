package it.uniroma3.siw.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Prenotazione;


@Repository
public interface PrenotazioneRepository extends CrudRepository <Prenotazione, Long> {
	public List <Prenotazione> findByData(LocalDate data);
	public List <Prenotazione> findByDataAndTurno(LocalDate data, String turno);
	public List <Prenotazione> findAllByOrderByPostiAsc ();
	public List <Prenotazione> findAllByOrderByDataAsc();
	public List <Prenotazione> findAllByOrderByNomeAsc ();
//	public List <Prenotazione> findByUtenteCredentialsUsername (String username);
	public List <Prenotazione> findByUtenteEmail(String email);
	
}
