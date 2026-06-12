package com.finance.project.dtos.dtos;

import jakarta.validation.constraints.NotBlank;
import org.springframework.hateoas.RepresentationModel;
import java.util.Objects;

public class NewPersonCategoryInfoDTO extends RepresentationModel<NewPersonCategoryInfoDTO> {

    @NotBlank(message = "Category denomination is required")
    private String denomination;

    public NewPersonCategoryInfoDTO(String denomination) {
        this.denomination = denomination;
    }

    public NewPersonCategoryInfoDTO() {}

    public String getDenomination() { return denomination; }

    public void setDenomination(String denomination) { this.denomination = denomination; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewPersonCategoryInfoDTO that = (NewPersonCategoryInfoDTO) o;
        return Objects.equals(denomination, that.denomination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(denomination);
    }
}