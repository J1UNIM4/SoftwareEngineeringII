package com.finance.project.modules.ledger.infrastructure;

import com.finance.project.modules.ledger.domain.Ledger;
import com.finance.project.modules.ledger.infrastructure.LedgerJpa;
import com.finance.project.modules.ledger.infrastructure.TransactionJpa;

import org.hibernate.annotations.Fetch;
import com.finance.project.modules.ledger.domain.LedgerID;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ledgers")
public class LedgerJpa {

    @EmbeddedId
    private LedgerID id;

    @OneToMany(mappedBy = "ledger", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
    List<TransactionJpa> transactions;

    // CONSTRUCTOR


    public LedgerJpa() {
    }

    public LedgerJpa(String id) {
        this.id = new LedgerID(id);
        this.transactions = new ArrayList<>();
    }

    public LedgerJpa(LedgerID ledgerID){
        this.id = ledgerID;
        this.transactions = new ArrayList<>();
    }


    // GETTERS AND SETTERS


    public LedgerID getId() {
        return id;
    }

    public void setId(LedgerID id) {
        this.id = id;
    }

    public List<TransactionJpa> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionJpa> transactions) {
        this.transactions = transactions;
    }

    // TO STRING

    public String toString() {
        return "Ledger {" +
                "id='" + id.toString() + '\'' +
                '}';
    }

    // EQUALS AND HASHCODE


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LedgerJpa ledgerJpa = (LedgerJpa) o;
        return Objects.equals(id, ledgerJpa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

