package it.ArchitectPlan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.ArchitectPlan.repository.RequestRepository;

@Service
public class RequestService {

	@Autowired
	private RequestRepository requestRepository;
	
	public boolean isRequestPresent(String name) {
		return this.requestRepository.existsByName(name);
	}

}
