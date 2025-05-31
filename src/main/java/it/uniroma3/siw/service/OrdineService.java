package it.uniroma3.siw.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Ordine;
import it.uniroma3.siw.repository.OrdineRepository;

@Service
public class OrdineService {
	
	@Autowired
	private OrdineRepository ordinerepository;
	
	public List<Ordine> getByUtenteEmail(String email) {
		
		return ordinerepository.findByUtenteEmail(email);
		
	}
	
	public List<Ordine> getByGiornoConsegna(LocalDate data) {
		
		return ordinerepository.findByGiornoConsegna(data);
		
	}
	
	public List<Ordine> getAllByOrderByGiornoConsegnaAsc() {
		
		return ordinerepository.findAllByOrderByNomeAsc();
		
	}
	
	public List<Ordine> getAllByOrderNomeAsc(){
		return ordinerepository.findAllByOrderByNomeAsc();
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
		return this.ordinerepository.finfById(id);
	}
	
}
