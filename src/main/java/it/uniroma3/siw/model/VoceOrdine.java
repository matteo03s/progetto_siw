package it.uniroma3.siw.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class VoceOrdine {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Float totParziale;
	private int quantità;
	
	@ManyToOne
	private Ordine ordine;
	
	@ManyToOne
	private Prodotto prodotto;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getTotParziale() {
		return totParziale;
	}

	public void setTotParziale(Float totParziale) {
		this.totParziale = totParziale;
	}

	public int getQuantità() {
		return quantità;
	}

	public void setQuantità(int quantità) {
		this.quantità = quantità;
	}

	public Ordine getOrdine() {
		return ordine;
	}

	public void setOrdine(Ordine ordine) {
		this.ordine = ordine;
	}

	public Prodotto getProdotto() {
		return prodotto;
	}

	public void setProdotto(Prodotto prodotto) {
		this.prodotto = prodotto;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, ordine, prodotto, quantità, totParziale);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VoceOrdine other = (VoceOrdine) obj;
		return Objects.equals(id, other.id) && Objects.equals(ordine, other.ordine)
				&& Objects.equals(prodotto, other.prodotto) && quantità == other.quantità
				&& Objects.equals(totParziale, other.totParziale);
	}

	@Override
	public String toString() {
		return "VoceOrdine [id=" + id + ", totParziale=" + totParziale + ", quantità=" + quantità + ", ordine=" + ordine
				+ ", prodotto=" + prodotto + "]";
	}
	
	public void calcolaTotParziale() {
		this.totParziale= this.quantità* this.prodotto.getPrezzo();
	}
}
