package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;
import jakarta.validation.Valid;

@Controller
public class AuthenticatioController {

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private UserService userService;
	
	@GetMapping("/login")
	public String showLogin(Model model) {
		return "login.html";
	}
	
	@GetMapping("/register")
	public String showRegister(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "register.html";
	}
	
	@GetMapping(value = "/")
	public String index(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "index.html";
		} else {
			UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals(Credentials.PROVIDER_ROLE)) {
				return "admin/indexAdmin.html";
			}
		}
		return "index.html";
	}
	
	@GetMapping(value = "/success")
	public String defaultAfterLogin(Model model) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		if (credentials.getRole().equals(Credentials.PROVIDER_ROLE)) {
			return "admin/indexAdmin.html";	//se ho permessi speciali allora posso accedere ad un'altra area
		}
		return "index.html"; //se mi sono autenticato e sono un utente normale torno alla homepage
	}

	@PostMapping(value = { "/register" })
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult userBindingResult,
			@Valid @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult,
			Model model) {
		if (userBindingResult.hasErrors() || credentialsBindingResult.hasErrors()) {
			return "register.html";
		}
		
		try {
			this.userService.saveUser(user);
		} catch (RuntimeException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "register.html";
		}
		try {
			credentials.setUser(user);
			this.credentialsService.save(credentials);
			return "redirect:/login";
		} catch (RuntimeException e) {
			this.userService.remove (user);
			model.addAttribute("errorMessage", e.getMessage());
			return "register.html";
		}
	}
		// se user e credential hanno entrambi contenuti validi, memorizza User e the
		// Credentials nel DB
/*		if (!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
			userService.saveUser(user);
			credentials.setUser(user);
			credentialsService.save(credentials);
			model.addAttribute("user", user);
			return "login.html";
		}
		return "register.html";
	}
*/
	
	@GetMapping("/access-denied")
    public String accessDeniedPage() {
        return "accessoNegato.html";  // La vista della pagina di errore 403
    }
}
