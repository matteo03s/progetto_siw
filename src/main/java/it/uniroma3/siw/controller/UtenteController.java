package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;

@Controller
public class UtenteController {

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private UserService userService;
	
	@Autowired 
	protected PasswordEncoder passwordEncoder; 
	
	@GetMapping("/utente")
	public String getPaginaUtente (Model model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		if (credentials.getRole().equals(Credentials.PROVIDER_ROLE)) {
			return "redirect:/admin/utenti";	//se ho permessi speciali allora posso accedere ad un'altra area
		}
		User utente = credentials.getUser();
		model.addAttribute("utente", utente);
		return "/utente/utente.html";
	}
	
	@GetMapping ("/admin/utenti")
	public String getPaginaUtenti (Model model) {
		model.addAttribute("utenti", credentialsService.getAllNormali());
		return "/admin/utenti.html";
	}
	
	@GetMapping ("/admin/utente/{id}")
	public String getPaginaUtente (@PathVariable ("id") Long id,
									Model model) {
//		Credentials corrente = credentialsService.getCredentials(id);
//		model.addAttribute("utente", corrente.getUser());
		model.addAttribute("utente", userService.getUser(id));
		return "/admin/utente.html";
	}
	
	@GetMapping ("/utente/modifica/{id}")
	public String getModificaUtente (@PathVariable ("id") Long id, Model model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		User utenteCorrente = credentials.getUser();
		if (utenteCorrente == null)
			return "/error/accessoNegato.html";
		if (utenteCorrente.getId().equals(id)) {
			model.addAttribute("utente", utenteCorrente);
			return "/utente/modificaUtente.html";
		}
		else
			return "/error/accessoNegato.html";
	}
	
	@PostMapping("/utente/modifica/{id}")
    public String postModificaUtente(@PathVariable("id") Long id, 
                                  @ModelAttribute("utente") User utenteForm, 
                                  BindingResult bindingResult,
                                  Model model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		User utenteCorrente = credentials.getUser();
		if (!(utenteCorrente.getId().equals(id)))
			return "/error/accessoNegato.html";
		
        if (bindingResult.hasErrors()) {
            return "redirect:/utente";
        }

        // Carica l'utente esistente dal database
        User utenteEsistente = userService.getUser(id);
        if (utenteEsistente == null) {
            return "redirect:/utente";
        }

        // Aggiorna solo i campi modificati
        if (utenteForm.getName() != null && !utenteForm.getName().trim().isEmpty()) {
            utenteEsistente.setName(utenteForm.getName());
        }
        if (utenteForm.getSurname() != null && !utenteForm.getSurname().trim().isEmpty()) {
            utenteEsistente.setSurname(utenteForm.getSurname());
        } else if (utenteForm.getSurname() != null && utenteForm.getSurname().trim().isEmpty()) {
            utenteEsistente.setSurname(null); // Permette di azzerare il cognome
        }
        if (utenteForm.getEmail() != null && !utenteForm.getEmail().trim().isEmpty()) {
            utenteEsistente.setEmail(utenteForm.getEmail());
        }
        if (utenteForm.getNumeroTelefonico() != null && !utenteForm.getNumeroTelefonico().trim().isEmpty()) {
            utenteEsistente.setNumeroTelefonico(utenteForm.getNumeroTelefonico());
        }
        
       try {
        	utenteEsistente.setId(id); // Assicura che l'ID rimanga invariato
            userService.saveUser(utenteEsistente); // Prova a salvare l'utente
            return "redirect:/utente"; // Successo: reindirizza al profilo
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage()); // Aggiunge il messaggio di errore
            model.addAttribute("utente", utenteEsistente); // Preserva i dati del form
            return "/utente/modificaUtente.html"; // Restituisce il form con l'errore
        }
    }
	
	@GetMapping ("/utente/modificaPassword/{id}")
	public String getModificaPasswordUtente (@PathVariable ("id") Long id, Model model) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		User utenteCorrente = credentials.getUser();
		if (utenteCorrente == null)
			return "/error/accessoNegato.html";
		if (utenteCorrente.getId().equals(id)) {
			model.addAttribute("utente", utenteCorrente);
			return "/utente/modificaPassword.html";
		}
		else
			return "/error/accessoNegato.html";
	}
	
	@PostMapping("/utente/modificaPassword/{id}")
	public String postModificaPasswordUtente (@PathVariable("id") Long id, 
			@RequestParam("vecchiaPassword") String vecchia,
			@RequestParam("nuovaPassword") String nuova,
			@RequestParam("confermaPassword") String conferma,
			Model model) {
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		User utenteCorrente = credentials.getUser();
		if (!(utenteCorrente.getId().equals(id)))
			return "/error/accessoNegato.html";
		
		if (!(this.passwordEncoder.matches(vecchia, credentials.getPassword()))) {
			model.addAttribute("errorMessage", "la password non corrisponde a quella vecchia");
			model.addAttribute("utente", utenteCorrente);
			return "/utente/modificaPassword.html";
		}
		if (!(nuova.equals(conferma))) {
			model.addAttribute("errorMessage", "la nuova password non corrisponde alla conferma");
			model.addAttribute("utente", utenteCorrente);
			return "/utente/modificaPassword.html";
		}
		
		 try {
			credentials.setId(credentials.getId()); // Assicura che l'ID rimanga invariato
			credentials.setPassword(nuova);
			credentialsService.save(credentials);
			return "redirect:/utente"; // Successo: reindirizza al profilo
	        } catch (RuntimeException e) {
	            model.addAttribute("errorMessage", e.getMessage()); // Aggiunge il messaggio di errore
	            model.addAttribute("utente", utenteCorrente); // Preserva i dati del form
	            return "/utente/modificaPassword.html"; // Restituisce il form con l'errore
	        }
	}
}
