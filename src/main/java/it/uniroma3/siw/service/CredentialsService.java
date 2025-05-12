package it.uniroma3.siw.service;


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
