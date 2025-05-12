package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Prenotazione {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String nome; //nome prenotazione
	@Future
	private LocalDate data; 
	@NotNull
	@Min(1)
	private int posti;
	private String turno;
	
	@ManyToOne (fetch = FetchType.LAZY)
	private User utente;
	
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
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}
	public int getPosti() {
		return posti;
	}
	public void setPosti(int posti) {
		this.posti = posti;
	}
	public String getTurno() {
		return turno;
	}
	public void setTurno(String turno) {
		this.turno = turno;
	}
	@Override
	public int hashCode() {
		return Objects.hash(data, id, nome, posti, turno);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Prenotazione other = (Prenotazione) obj;
		return Objects.equals(data, other.data) && Objects.equals(id, other.id) && Objects.equals(nome, other.nome)
				&& posti == other.posti && Objects.equals(turno, other.turno);
	}
	@Override
	public String toString() {
		return "Prenotazione [id=" + id + ", nome=" + nome + ", data=" + data + ", posti=" + posti + ", turno=" + turno
				+ "]";
	}
	public User getUtente() {
		return utente;
	}
	public void setUtente(User utente) {
		this.utente = utente;
	}
}
