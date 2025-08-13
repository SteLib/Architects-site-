package it.ArchitectPlan.repository;

import org.springframework.data.repository.CrudRepository;

import it.ArchitectPlan.model.User;

public interface UserRepository extends CrudRepository<User, Long>{

	public boolean existsByNameAndSurname(String name, String surname);

}
