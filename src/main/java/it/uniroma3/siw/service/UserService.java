package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.UserRepository;
import jakarta.validation.Valid;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User saveUser(User user) {
		if (this.userRepository.findByEmail(user.getEmail()).isPresent()) {
			throw new RuntimeException("Email già utilizza, cambialo");
		}
        return userRepository.save(user);  // Salva solo l'utente
    }

	public void remove(@Valid User user) {
		userRepository.delete(user);
		
	}
}
