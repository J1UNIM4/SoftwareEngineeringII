package com.finance.project.dtos.dtos;

import com.finance.project.modules.ledger.domain.Category;
import com.finance.project.modules.ledger.domain.Transaction;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.Objects;

public class NewPersonTransactionInfoDTO {

    @NotBlank(message = "Category denomination is required")
    private String denominationCategory;

    @NotBlank(message = "Transaction type is required")
    private String type;

    @NotBlank(message = "Description is required")
    private String description;

    @Positive(message = "Amount must be positive")
    private double amount;

    @NotBlank(message = "Debit account is required")
    private String denominationAccountDeb;

    @NotBlank(message = "Credit account is required")
    private String denominationAccountCred;

    @NotBlank(message = "Date is required")
    private String date;

    public NewPersonTransactionInfoDTO(String denominationCategory, String type, String description, double amount, String denominationAccountDeb, String denominationAccountCred, String date) {
        this.denominationCategory = denominationCategory;
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.denominationAccountDeb = denominationAccountDeb;
        this.denominationAccountCred = denominationAccountCred;
        this.date = date;
    }

    public NewPersonTransactionInfoDTO() {}

    public String getDenominationCategory() { return denominationCategory; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public String getDenominationAccountDeb() { return denominationAccountDeb; }
    public String getDenominationAccountCred() { return denominationAccountCred; }
    public String getDate() { return date; }

    public void setDenominationCategory(String denominationCategory) { this.denominationCategory = denominationCategory; }
    public void setType(String type) { this.type = type; }
    public void setDescription(String description) { this.description = description; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setDenominationAccountDeb(String denominationAccountDeb) { this.denominationAccountDeb = denominationAccountDeb; }
    public void setDenominationAccountCred(String denominationAccountCred) { this.denominationAccountCred = denominationAccountCred; }
    public void setDate(String date) { this.date = date; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewPersonTransactionInfoDTO that = (NewPersonTransactionInfoDTO) o;
        return Objects.equals(denominationCategory, that.denominationCategory) &&
                Objects.equals(type, that.type) &&
                Objects.equals(description, that.description) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(denominationAccountDeb, that.denominationAccountDeb) &&
                Objects.equals(denominationAccountCred, that.denominationAccountCred) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(denominationCategory, type, description, amount, denominationAccountDeb, denominationAccountCred, date);
    }
}