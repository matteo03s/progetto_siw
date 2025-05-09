package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Ordine {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nome;
	private int numeroTelefonico;
	private String indirizzo;
	private Float tolale;
	private LocalDate giornoConsegna;
	private LocalTime orarioConsegna;
	
}
