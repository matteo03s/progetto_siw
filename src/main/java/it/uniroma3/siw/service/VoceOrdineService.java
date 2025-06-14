package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Ordine;
import it.uniroma3.siw.model.VoceOrdine;
import it.uniroma3.siw.repository.VoceOrdineRepository;

@Service
public class VoceOrdineService {
	
	@Autowired
	VoceOrdineRepository voceordineRepository;
	
	public VoceOrdine save(VoceOrdine vo) {
		return voceordineRepository.save(vo);
	}
}
