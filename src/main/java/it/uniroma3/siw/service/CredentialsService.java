package it.uniroma3.siw.service;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.repository.CredentialsRepository;


@Service
public class CredentialsService {

	@Autowired 
	protected PasswordEncoder passwordEncoder; 
	
	@Autowired 
	protected CredentialsRepository credentialsRepository;
	
	public List <Credentials> getAllNormali () {
		List <Credentials> utenti = new LinkedList <Credentials> ();
		Iterable <Credentials> tutte = credentialsRepository.findAll();
		for (Credentials c: tutte) {
			if (c.getRole().equals(Credentials.DEFAULT_ROLE))
				utenti.add(c);
			}
		return utenti;
	}
	@Transactional 
	public Credentials getCredentials(Long id) {
		Optional<Credentials> result = this.credentialsRepository.findById(id);
		return result.orElse(null);
	}

	@Transactional
	public Credentials getCredentials(String username) {
		Optional<Credentials> result = this.credentialsRepository.findByUsername(username);
		return result.orElse(null);
	}

	
	@Transactional
	public Credentials save(Credentials credentials) {
		if (this.credentialsRepository.findByUsername(credentials.getUsername()).isPresent()) {
			if (!credentialsRepository.findById(credentials.getId()).isPresent())
//			if (!credentials.equals(this.credentialsRepository.findByUsername(credentials.getUsername())))
			throw new RuntimeException("Username già utilizza, cambialo");
		}
		credentials.setRole(Credentials.DEFAULT_ROLE);
		credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
		return this.credentialsRepository.save(credentials);
	}
	
	//provo a modificare la funzione sopra
	/*@Transactional 
	public Credentials saveCredentials(Credentials credentials) {
		
		if(credentials.getRole().equals("azienda")){
			credentials.setRole(Credentials.PROVIDER_ROLE);
		}
		
		if(credentials.getRole().equals("utente")){
			credentials.setRole(Credentials.DEFAULT_ROLE);
		}
		credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
		return this.credentialsRepository.save(credentials);
	}*/
}
