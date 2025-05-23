package it.uniroma3.siw.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import javax.management.RuntimeErrorException;

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
        LocalDate data = prenotazione.getData();

        //ottieni il turno
        String turno = prenotazione.getTurno();

        //ottieneìi il giorno della settimana
        DayOfWeek giorno = data.getDayOfWeek();

        // Calcola il totale dei posti già prenotati per quel giorno e turno
        int postiPrenotati = calcolaPostiPrenotatiPerGiornoETurno(data, turno);
        
        if (giorno == DayOfWeek.MONDAY) {
        	throw new RuntimeException("Le prenotazioni non sono disponibili il lunedì.");
        }

        if ((giorno == DayOfWeek.TUESDAY || giorno == DayOfWeek.WEDNESDAY || giorno == DayOfWeek.THURSDAY) && "pranzo".equalsIgnoreCase(turno)) {
        	throw new RuntimeException("Le prenotazioni per il giorno selezionato non sono disponibili a pranzo");

        }
        
        // Controlla se ci sono ancora posti disponibili
        if (postiPrenotati + prenotazione.getPosti() > MAX_POSTI_PER_TURNO) {
            throw new RuntimeException("Non ci sono abbastanza posti disponibili per il " + prenotazione.getData() + " a " + prenotazione.getTurno()  + " . Posti disponibili: " + (MAX_POSTI_PER_TURNO - postiPrenotati));
        }
        Credentials cred = credentialService.getCredentials(username);
        prenotazione.setUtente(cred.getUser());
        // Salva la prenotazione
        prenotazioneRepository.save(prenotazione);
        return "Prenotazione confermata per il turno " + turno + "!";
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
