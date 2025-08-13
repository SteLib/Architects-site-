package it.ArchitectPlan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.ArchitectPlan.model.Project;
import it.ArchitectPlan.repository.ProjectRepository;

@Service
public class ProjectService {
	
	@Autowired
	private ProjectRepository projectRepository;

	public  Iterable<Project> findProjectsNotInArchitect(Long architectId) {
		return this.projectRepository.findProjectsNotInArchitect(architectId);
	}

	public boolean existsByName(String name) {
		return this.projectRepository.existsByName(name);
	}

}
