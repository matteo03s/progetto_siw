package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Credentials;

public interface CredentialsRepository extends CrudRepository <Credentials, Long> {

	public Optional<Credentials> findByUsername(String username);
	public List <Credentials> findByUserEmail (String email);
	public List <Credentials> findByUserNumeroTelefonico (String numero);
	
}
