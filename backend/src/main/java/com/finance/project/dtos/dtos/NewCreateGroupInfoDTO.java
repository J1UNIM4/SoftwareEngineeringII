package com.finance.project.dtos.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class NewCreateGroupInfoDTO {

    @Email(message = "Must be a valid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Denomination is required")
    private String denomination;

    @NotBlank(message = "Description is required")
    private String description;

    public NewCreateGroupInfoDTO(String email, String denomination, String description) {
        this.email = email;
        this.denomination = denomination;
        this.description = description;
    }

    public NewCreateGroupInfoDTO() {}

    public String getEmail() { return email; }
    public String getDenomination() { return denomination; }
    public String getDescription() { return description; }

    public void setEmail(String email) { this.email = email; }
    public void setDenomination(String denomination) { this.denomination = denomination; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewCreateGroupInfoDTO)) return false;
        NewCreateGroupInfoDTO that = (NewCreateGroupInfoDTO) o;
        return Objects.equals(getEmail(), that.getEmail()) &&
                Objects.equals(getDenomination(), that.getDenomination()) &&
                Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getDenomination(), getDescription());
    }
}