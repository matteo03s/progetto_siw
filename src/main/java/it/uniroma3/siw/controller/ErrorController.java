package it.uniroma3.siw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error/405")
    public String gestisci405() {
        return "/error/405.html"; // ritorna la pagina 405.html in templates/error/
    }
//    @GetMapping("/error/404")
//    public String gestisci404() {
//        return "/error/404.html"; // ritorna la pagina 405.html in templates/error/
//    }
}
