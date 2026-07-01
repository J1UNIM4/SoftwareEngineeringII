package com.finance.project.dtos.dtosAssemblers;

import com.finance.project.modules.ledger.domain.Transaction;
import com.finance.project.dtos.dtos.TransactionDTOout;

/**
 * The type Transaction dt oout assembler.
 */
public class TransactionDTOoutAssembler {
    private TransactionDTOoutAssembler() {
    }

    /**
     * Create transaction dt oout transaction dt oout.
     *
     * @param transaction the transaction
     * @return the transaction dt oout
     */
    public static TransactionDTOout createTransactionDTOout (Transaction transaction){
        TransactionDTOout transactionDTOout = new TransactionDTOout();
        transactionDTOout.setCategory(transaction.getCategoryID().getDenomination().getDenomination());
        transactionDTOout.setType(transaction.getType().getType());
        transactionDTOout.setDescription(transaction.getDescription().getDescription());
        transactionDTOout.setAmount(transaction.getAmount().getAmount());
        transactionDTOout.setDate(transaction.getDate().getDate().toString());
        transactionDTOout.setDebitAccount(transaction.getDebitAccountID().getDenomination().getDenomination());
        transactionDTOout.setCreditAccount(transaction.getCreditAccountID().getDenomination().getDenomination());
        return transactionDTOout;
    }
}
