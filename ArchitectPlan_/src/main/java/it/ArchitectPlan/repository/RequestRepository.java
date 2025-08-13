package it.ArchitectPlan.repository;

import org.springframework.data.repository.CrudRepository;

import it.ArchitectPlan.model.Request;
import it.ArchitectPlan.model.User;

public interface RequestRepository extends CrudRepository<Request, Long>{

	public boolean existsByName(String name);

	public Request findByUser(User user);

}
