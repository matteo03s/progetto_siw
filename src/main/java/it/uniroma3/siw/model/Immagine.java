package it.uniroma3.siw.model;

import java.util.Arrays;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Immagine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeFile;

    private String tipoContenuto;

    @Lob
    @Column(nullable = false)
    private byte[] dati;

    public Immagine() {}

    public Immagine(String nomeFile, String tipoContenuto, byte[] dati) {
        this.nomeFile = nomeFile;
        this.tipoContenuto = tipoContenuto;
        this.dati = dati;
    }
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public String getTipoContenuto() {
		return tipoContenuto;
	}

	public void setTipoContenuto(String tipoContenuto) {
		this.tipoContenuto = tipoContenuto;
	}

	public byte[] getDati() {
		return dati;
	}

	public void setDati(byte[] dati) {
		this.dati = dati;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(dati);
		result = prime * result + Objects.hash(id, nomeFile, tipoContenuto);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Immagine other = (Immagine) obj;
		return Arrays.equals(dati, other.dati) && Objects.equals(id, other.id)
				&& Objects.equals(nomeFile, other.nomeFile) && Objects.equals(tipoContenuto, other.tipoContenuto);
	}

	@Override
	public String toString() {
		return "Immagine [id=" + id + ", nomeFile=" + nomeFile + ", tipoContenuto=" + tipoContenuto + "]";
	}
    
}
