package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.User;
import it.uniroma3.siw.model.VoceOrdine;

public interface VoceOrdineRepository extends CrudRepository <VoceOrdine, Long>{
	
	public VoceOrdine save(VoceOrdine vo);
}
