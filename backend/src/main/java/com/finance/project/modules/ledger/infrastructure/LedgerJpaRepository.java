package com.finance.project.modules.ledger.infrastructure;

import com.finance.project.modules.ledger.infrastructure.LedgerJpa;
import com.finance.project.modules.ledger.infrastructure.LedgerJpaRepository;

import org.springframework.data.repository.CrudRepository;
import com.finance.project.modules.ledger.domain.LedgerID;

import java.util.List;
import java.util.Optional;

public interface LedgerJpaRepository extends CrudRepository<LedgerJpa, LedgerID> {

    List<LedgerJpa> findAll();

    Optional<LedgerJpa> findById(LedgerID id);

    boolean existsById(LedgerID id);

    long count();
}