package com.finance.project.modules.ledger.infrastructure;

import com.finance.project.modules.ledger.infrastructure.LedgerJpa;
import com.finance.project.modules.ledger.infrastructure.TransactionJpa;
import com.finance.project.modules.ledger.infrastructure.TransactionJpaRepository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface TransactionJpaRepository extends CrudRepository<TransactionJpa, Long> {

    List<TransactionJpa> findAll();

    Optional<TransactionJpa> findById(Long id);

    List<TransactionJpa> findAllByLedger(LedgerJpa id);
}