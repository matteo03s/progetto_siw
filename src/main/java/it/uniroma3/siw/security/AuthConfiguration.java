package it.uniroma3.siw.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import it.uniroma3.siw.model.Credentials;

@Configuration
@EnableWebSecurity
//public  class WebSecurityConfig {
public class AuthConfiguration {

	@Autowired
	private DataSource dataSource;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
		/*qui ci va la query per andare a prendere i dati dal database per verificare l'utente*/
		auth.jdbcAuthentication().dataSource(dataSource)
				.authoritiesByUsernameQuery("SELECT username, role from credentials WHERE username=?")
				.usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	
	/*questo metodo ci permette di dire a quale pagine l'utente sia connesso che no può vedere*/
	@Bean
	protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().and().cors().disable().authorizeHttpRequests()
				// .requestMatchers("/**").permitAll()
				// chiunque (autenticato o no) può accedere alle pagine index, login, register,
				// ai css e alle immagini,  "/static/**"
				.requestMatchers(HttpMethod.GET, "/", "/index", "/register", "/css/**", "/images/**", "favicon.ico")
				.permitAll()
				// chiunque (autenticato o no) può mandare richieste POST al punto di accesso
				// per login e register
				.requestMatchers(HttpMethod.POST, "/register", "/login").permitAll()
				.requestMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority(Credentials.PROVIDER_ROLE)	//tipo di ruolo
				.requestMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(Credentials.PROVIDER_ROLE)
				.requestMatchers(HttpMethod.POST, "/prenotazione/**").authenticated()
				.requestMatchers(HttpMethod.GET, "/prenotazione/**").authenticated()
				.requestMatchers(HttpMethod.POST, "/utente/**").authenticated()
				.requestMatchers(HttpMethod.GET, "/utente/**").authenticated()
				// tutti gli utenti autenticati possono accedere alle pagine rimanenti
				.anyRequest().permitAll()//authenticated()//permitAll()
				// LOGIN: qui definiamo il login
				.and().formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/success", true)
				.failureUrl("/login?error=true")
				// LOGOUT: qui definiamo il logout
				.and().logout()
				// il logout è attivato con una richiesta GET a "/logout"
				.logoutUrl("/logout")
				// in caso di successo, si viene reindirizzati alla home
				.logoutSuccessUrl("/").invalidateHttpSession(true).deleteCookies("JSESSIONID")
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).clearAuthentication(true).permitAll()
//				.and().oauth2Login() // Configura login OAuth2 (Google in questo caso)
//				.loginPage("/login") // la stessa pagina di login della tua applicazione
//				.defaultSuccessUrl("/success", true)
//				.failureUrl("/login?error=true");
				// Gestione dell'errore Forbidden (403)
				.and().exceptionHandling()
//				.authenticationEntryPoint((request, response, authException) -> {
//				response.sendRedirect("/login?error=unauthorized");
//				})
				// Gestione dell'errore Forbidden (403)
				.accessDeniedPage("/error/access-denied");  // Redirect a una pagina di errore 403
		return httpSecurity.build();
	}
}
