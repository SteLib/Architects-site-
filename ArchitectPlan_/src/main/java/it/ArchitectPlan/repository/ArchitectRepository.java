package it.ArchitectPlan.repository;

import org.springframework.data.repository.CrudRepository;

import it.ArchitectPlan.model.Architect;

public interface ArchitectRepository extends CrudRepository <Architect,Long>{

	public boolean existsByName(String name);

}
