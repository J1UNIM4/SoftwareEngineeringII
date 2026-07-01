package com.finance.project.modules.ledger.domain;

import com.finance.project.modules.ledger.domain.Account;
import com.finance.project.modules.ledger.domain.AccountID;
import com.finance.project.modules.ledger.domain.IAccountRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IAccountRepository {

    Account save(Account account);

    List<Account> findAll();

    Optional<Account> findById(String id, String denomination);

    boolean existsById(AccountID accountID);

    long count();

    void delete(Account account);

    List<Account> findAllById(String description, String denomination, String id);
}