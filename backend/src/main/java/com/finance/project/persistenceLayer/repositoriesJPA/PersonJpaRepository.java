package com.finance.project.persistenceLayer.repositoriesJPA;


import com.finance.project.modules.person.infrastructure.PersonJpa;
import org.springframework.data.repository.CrudRepository;
import com.finance.project.modules.person.domain.PersonID;

import java.util.List;
import java.util.Optional;

public interface PersonJpaRepository extends CrudRepository<PersonJpa, PersonID> {

	List<PersonJpa> findAll();

	Optional<PersonJpa> findById(PersonID id);

	boolean existsById(PersonID id);

	long count();

	void delete(PersonJpa personJpa);
}