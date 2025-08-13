package it.ArchitectPlan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.ArchitectPlan.repository.ArchitectRepository;

@Service
public class ArchitectService {

	@Autowired
	private ArchitectRepository architectRepository;
	
	public boolean existsByName(String name) {
		return this.architectRepository.existsByName(name);
	}

}
