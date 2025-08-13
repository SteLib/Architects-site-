package it.ArchitectPlan.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.ArchitectPlan.model.Credentials;

public interface CredentialsRepository  extends CrudRepository<Credentials, Long>{

	public Optional<Credentials> findByUsername(String username);
	
	@Query("SELECT c FROM Credentials c WHERE c.user.id = :userId")
    public Credentials findByUserId(@Param("userId") Long userId);
	
}
