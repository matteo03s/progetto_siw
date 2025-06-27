package it.uniroma3.siw.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Prodotto {
		
		@Id
		@GeneratedValue (strategy = GenerationType.IDENTITY)
		private Long id;
		
		@Column (nullable = false)
		@NotBlank
		private String nome;
		
		private String categoria;
		private String descrizione;
		
		@Column (nullable = false)
		@NotNull
		private Float prezzo;

	    @OneToOne(fetch = FetchType.LAZY)
	    private Immagine immagine;
		

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
		public String getCategoria() {
			return categoria;
		}
		public void setCategoria(String categoria) {
			this.categoria = categoria;
		}
		public String getDescrizione() {
			return descrizione;
		}
		public void setDescrizione(String descrizione) {
			this.descrizione = descrizione;
		}
		public Float getPrezzo() {
			return prezzo;
		}
		public void setPrezzo(Float prezzo) {
			this.prezzo = prezzo;
		}
		
		public Immagine getImmagine() {
			return immagine;
		}
		public void setImmagine(Immagine immagine) {
			this.immagine = immagine;
		}
		@Override
		public int hashCode() {
			return Objects.hash(categoria, descrizione, id, nome, prezzo);
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Prodotto other = (Prodotto) obj;
			return Objects.equals(categoria, other.categoria) && Objects.equals(descrizione, other.descrizione)
					&& Objects.equals(id, other.id) && Objects.equals(nome, other.nome)
					&& Objects.equals(prezzo, other.prezzo) ;
		}
		@Override
		public String toString() {
			return "Prodotto [id=" + id + ", nome=" + nome + ", categoria=" + categoria + ", descrizione=" + descrizione
					+ ", prezzo=" + prezzo + "]";
		}
		
}
