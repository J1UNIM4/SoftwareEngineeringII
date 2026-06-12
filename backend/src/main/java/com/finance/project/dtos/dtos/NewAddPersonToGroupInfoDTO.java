package com.finance.project.dtos.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

public class NewAddPersonToGroupInfoDTO {

    @Email(message = "Must be a valid email")
    @NotBlank(message = "Email is required")
    private String email;

    public NewAddPersonToGroupInfoDTO() {}

    public NewAddPersonToGroupInfoDTO(String email) {
        this.email = email;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewAddPersonToGroupInfoDTO)) return false;
        NewAddPersonToGroupInfoDTO that = (NewAddPersonToGroupInfoDTO) o;
        return Objects.equals(getEmail(), that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}