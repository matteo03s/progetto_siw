package it.uniroma3.siw.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalController {
  @ModelAttribute("userDetails")
  public UserDetails getUser() {
    UserDetails user = null;
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!(authentication instanceof AnonymousAuthenticationToken)) {
      user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    return user;
  }
  
  @GetMapping("/error/404")
  public String gestisci404() {
      return "/error/404.html"; // ritorna la pagina 405.html in templates/error/
  }
  
}
