package com.finance.project.dtos.dtos;

import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

public class NewGroupAccountInfoDTO {

    @NotBlank(message = "Account description is required")
    private String accountDescription;

    @NotBlank(message = "Account denomination is required")
    private String accountDenomination;

    public NewGroupAccountInfoDTO(String accountDescription, String accountDenomination) {
        this.accountDescription = accountDescription;
        this.accountDenomination = accountDenomination;
    }

    public NewGroupAccountInfoDTO() {}

    public String getAccountDenomination() { return accountDenomination; }
    public String getAccountDescription() { return accountDescription; }

    public void setAccountDenomination(String accountDenomination) { this.accountDenomination = accountDenomination; }
    public void setAccountDescription(String accountDescription) { this.accountDescription = accountDescription; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewGroupAccountInfoDTO that = (NewGroupAccountInfoDTO) o;
        return Objects.equals(accountDenomination, that.accountDenomination) &&
                Objects.equals(accountDescription, that.accountDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountDenomination, accountDescription);
    }
}