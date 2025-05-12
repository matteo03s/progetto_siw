package it.uniroma3.siw.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Prenotazione;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.PrenotazioneRepository;

@Service
public class PrenotazioneService {
	
	@Autowired
	private PrenotazioneRepository prenotazioneRepository;

	@Autowired
	private CredentialsService credentialService;

	private static final int MAX_POSTI_PER_TURNO = 50;

    public Prenotazione getById(Long id) {
    	return prenotazioneRepository.findById(id).get();
    }
    
    public List <Prenotazione> getByData(LocalDate data) {
    	return prenotazioneRepository.findByData(data);
    }
    
    public List <Prenotazione> getAll() {
    	return (List<Prenotazione>)prenotazioneRepository.findAll();
    }
    
    public List <Prenotazione> getOrderedByPosti () {
    	return prenotazioneRepository.findAllByOrderByPostiAsc();
    }
    
    public List <Prenotazione> getOrderedByData () {
    	return prenotazioneRepository.findAllByOrderByDataAsc();
    }
    
    public List <Prenotazione> getOrderedByNome () {
    	return prenotazioneRepository.findAllByOrderByNomeAsc();
    }
    
    public List <Prenotazione> getPrenotazioniUsername (String username) {
    	String email = credentialService.getCredentials(username).getUser().getEmail();
    	return prenotazioneRepository.findByUtenteEmail(email);
    }
    
    // Metodo per creare una nuova prenotazione
    public String creaPrenotazione(Prenotazione prenotazione, String username) {
    	
        // Ottieni la data della prenotazione
        LocalDate dataPrenotazione = prenotazione.getData();
        
        // Calcola il totale dei posti già prenotati per quel giorno e turno
        int postiPrenotati = calcolaPostiPrenotatiPerGiornoETurno(dataPrenotazione, prenotazione.getTurno());

        // Controlla se ci sono ancora posti disponibili
        if (postiPrenotati + prenotazione.getPosti() > MAX_POSTI_PER_TURNO) {
            throw new RuntimeException("Non ci sono abbastanza posti disponibili per il " + prenotazione.getTurno() + " del " + prenotazione.getData() + " . Posti disponibili: " + (MAX_POSTI_PER_TURNO - postiPrenotati));
        }
        Credentials cred = credentialService.getCredentials(username);
        prenotazione.setUtente(cred.getUser());
        // Salva la prenotazione
        prenotazioneRepository.save(prenotazione);
        return "Prenotazione confermata per il turno " + prenotazione.getTurno() + "!";
    }

	// Metodo per calcolare i posti già prenotati per un determinato giorno e turno
	public int calcolaPostiPrenotatiPerGiornoETurno(LocalDate dataPrenotazione, String turno) {
		List<Prenotazione> prenotazioniPerGiornoETurno = prenotazioneRepository.findByDataAndTurno(dataPrenotazione, turno);
		int prenotati = 0;
		for (Prenotazione p: prenotazioniPerGiornoETurno) {
			prenotati = prenotati + p.getPosti();
		}
		return prenotati;	
	}
	
/*    public Prenotazione getAndCheck(Long id, String username, boolean isAdmin){
        Prenotazione p = prenotazioneRepository.findById(id).orElseThrow();
        Credentials cred = credentialService.getCredentials(username);
        User u = cred.getUser();
        if (!isAdmin && !p.getUtente().getUsername().equals(username))
            throw new AccessDeniedException("Non autorizzato");
        return p;
    }
*/
}
