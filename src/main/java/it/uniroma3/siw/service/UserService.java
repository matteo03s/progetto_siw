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
        // Verifica unicità email, escludendo l'utente corrente
        User existingUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (existingUser != null && !existingUser.getId().equals(user.getId())) {
            throw new RuntimeException("Email già utilizzata da un altro utente, cambiala");
        }
        return userRepository.save(user);
    }
    
	public void remove(@Valid User user) {
		userRepository.delete(user);
		
	}
}
