package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
public class Ordine {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	private String nome;
	
    //@Pattern(regexp = "\\d{10}")
    private String numeroTelefonico;
	
	@NotBlank
	private String indirizzo;
	private Float totale;
	/*
	@Future
	private LocalDate giornoConsegna;
	*/
	
	private LocalDate giornoConsegna;
	private LocalTime orarioConsegna;
	
	@ManyToOne
	private User utente;
	
	@OneToMany(mappedBy = "ordine", cascade = CascadeType.ALL)
	private List <VoceOrdine> vociOrdine;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getNumeroTelefonico() {
		return numeroTelefonico;
	}
	public void setNumeroTelefonico(String numeroTelefonico) {
		this.numeroTelefonico = numeroTelefonico;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public Float getTotale() {
		return totale;
	}
	public void setTotale(Float tolale) {
		this.totale = tolale;
	}
	public LocalDate getGiornoConsegna() {
		return giornoConsegna;
	}
	public void setGiornoConsegna(LocalDate giornoConsegna) {
		this.giornoConsegna = giornoConsegna;
	}
	public LocalTime getOrarioConsegna() {
		return orarioConsegna;
	}
	public void setOrarioConsegna(LocalTime orarioConsegna) {
		this.orarioConsegna = orarioConsegna;
	}
	public User getUtente() {
		return utente;
	}
	public void setUtente(User utente) {
		this.utente = utente;
	}
	public List<VoceOrdine> getVociOrdine() {
		return vociOrdine;
	}
	public void setVociOrdine(List<VoceOrdine> vociOrdine) {
		this.vociOrdine = vociOrdine;
	}
	@Override
	public int hashCode() {
		return Objects.hash(giornoConsegna, id, indirizzo, nome, numeroTelefonico, orarioConsegna, totale, utente,
				vociOrdine);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ordine other = (Ordine) obj;
		return Objects.equals(giornoConsegna, other.giornoConsegna) && Objects.equals(id, other.id)
				&& Objects.equals(indirizzo, other.indirizzo) && Objects.equals(nome, other.nome)
				&& numeroTelefonico == other.numeroTelefonico && Objects.equals(orarioConsegna, other.orarioConsegna)
				&& Objects.equals(totale, other.totale) && Objects.equals(utente, other.utente)
				&& Objects.equals(vociOrdine, other.vociOrdine);
	}
	@Override
	public String toString() {
		return "Ordine [id=" + id + ", nome=" + nome + ", numeroTelefonico=" + numeroTelefonico + ", indirizzo="
				+ indirizzo + ", tolale=" + totale + ", giornoConsegna=" + giornoConsegna + ", orarioConsegna="
				+ orarioConsegna + ", utente=" + utente + ", vociOrdine=" + vociOrdine + "]";
	}
	
	
	public float calculateTotal() {
		
		if(vociOrdine.isEmpty())
			return 0;
		
		float t = 0;
		for(VoceOrdine vo: this.vociOrdine) {
			t= t+ vo.getTotParziale();
		}
		return t;
	}
	
}
