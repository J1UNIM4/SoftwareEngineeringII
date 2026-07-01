package com.finance.project.modules.ledger.infrastructure;

import com.finance.project.modules.ledger.infrastructure.AccountJpa;
import com.finance.project.modules.ledger.infrastructure.AccountJpaRepository;

import com.finance.project.dataModel.dataModel.AbstractIdJpa;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AccountJpaRepository extends CrudRepository<AccountJpa, AbstractIdJpa> {

	List<AccountJpa> findAll();

	Optional<AccountJpa> findById(AbstractIdJpa id);

	boolean existsById(AbstractIdJpa id);

	long count();

	void delete(AccountJpa accountJpa);

	List<AccountJpa> findAllById(AbstractIdJpa id);
}