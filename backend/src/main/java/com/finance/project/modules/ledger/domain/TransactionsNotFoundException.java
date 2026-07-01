package com.finance.project.modules.ledger.domain;

import com.finance.project.modules.ledger.domain.TransactionsNotFoundException;

public class TransactionsNotFoundException extends RuntimeException{

    public TransactionsNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
