package it.uniroma3.siw.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Ordine;
import it.uniroma3.siw.model.Prenotazione;
import it.uniroma3.siw.repository.OrdineRepository;

@Service
public class OrdineService {
	
	@Autowired
	private OrdineRepository ordinerepository;
	@Autowired
	private CredentialsService credentialsService;
	
	public List <Ordine> getOrdiniUsername (String username) {
    	String email = credentialsService.getCredentials(username).getUser().getEmail();
    	return ordinerepository.findByUtenteEmail(email);
    }
	
	public List <Ordine> getAll() {
    	return (List<Ordine>)ordinerepository.findAll();
    }
	public List<Ordine> getByUtenteEmail(String email) {
		
		return ordinerepository.findByUtenteEmail(email);
		
	}
	
	public List<Ordine> getByGiornoConsegna(LocalDate data) {	
		return ordinerepository.findByGiornoConsegna(data);
		
	}
	
	
	public List<Ordine> getAllByOrderNomeAsc(){
		return ordinerepository.findAllByOrderByNomeAsc();
	}
	public List<Ordine> getAllByOrderByGiornoConsegnaAsc() {
		return ordinerepository.findAllByOrderByNomeAsc();
	}
	public List<Ordine> getAllByOrderByTotaleAsc() {
		return ordinerepository.findAllByOrderByTotaleAsc();
	}
	public List<Ordine> getAllByOrderTotaleDesc(){
		return ordinerepository.findAllByOrderByTotaleDesc();
	}
	public List<Ordine> getAllByOrderNomeDesc(){
		return ordinerepository.findAllByOrderByNomeDesc();
	}
	public List<Ordine> getAllByOrderByGiornoConsegnaDesc() {
		return ordinerepository.findAllByOrderByNomeDesc();
	}
	
	public Ordine getBygiornoConsegnaAndorarioConsegna(LocalDate giorno, LocalTime orario) {
		return ordinerepository.findByGiornoConsegnaAndOrarioConsegna(giorno, orario);
	}
	
	public Ordine save(Ordine ordine) {
			return ordinerepository.save(ordine);
	}
	
	public Ordine getOrdinepiùRecente() {
		return ordinerepository.findFirstByOrderByIdDesc();
	}
	
	public void eliminaOrdinePerID(Long id) {
		ordinerepository.deleteById(id);
		return;
	}
	
	public Ordine getOrdineById(Long id) {
		return this.ordinerepository.findById(id).get();
	}
	
}
