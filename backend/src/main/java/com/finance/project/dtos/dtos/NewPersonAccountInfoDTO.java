package com.finance.project.dtos.dtos;

import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

public class NewPersonAccountInfoDTO {

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Account denomination is required")
    private String denomination;

    public NewPersonAccountInfoDTO(String description, String denomination) {
        this.description = description;
        this.denomination = denomination;
    }

    public NewPersonAccountInfoDTO() {}

    public String getDenomination() { return denomination; }
    public String getDescription() { return description; }

    public void setDenomination(String denomination) { this.denomination = denomination; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewPersonAccountInfoDTO that = (NewPersonAccountInfoDTO) o;
        return Objects.equals(denomination, that.denomination) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(denomination, description);
    }
}