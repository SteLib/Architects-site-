package it.ArchitectPlan.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.ArchitectPlan.model.Project;

public interface ProjectRepository extends CrudRepository<Project, Long>{

	public Project findProjectByName(String name);
	

	@Query(value="select * "
			+ "from project "
			+ "where architect_id <> :architectId "
			+ "or architect_id is NULL", nativeQuery=true)
	public Iterable<Project> findProjectsNotInArchitect(Long architectId);


	public boolean existsByName(String name);
}
