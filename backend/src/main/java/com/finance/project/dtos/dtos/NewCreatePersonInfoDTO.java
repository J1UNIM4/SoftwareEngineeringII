package com.finance.project.dtos.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

public class NewCreatePersonInfoDTO {

    @Email(message = "Must be a valid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Birthdate is required")
    private String birthdate;

    @NotBlank(message = "Birthplace is required")
    private String birthplace;

    public NewCreatePersonInfoDTO(String email, String name, String birthdate, String birthplace) {
        this.email = email;
        this.name = name;
        this.birthdate = birthdate;
        this.birthplace = birthplace;
    }

    public NewCreatePersonInfoDTO() {}

    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getBirthdate() { return birthdate; }
    public String getBirthplace() { return birthplace; }

    public void setEmail(String email) { this.email = email; }
    public void setName(String name) { this.name = name; }
    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }
    public void setBirthplace(String birthplace) { this.birthplace = birthplace; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewCreatePersonInfoDTO that = (NewCreatePersonInfoDTO) o;
        return Objects.equals(email, that.email) &&
                Objects.equals(name, that.name) &&
                Objects.equals(birthdate, that.birthdate) &&
                Objects.equals(birthplace, that.birthplace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name, birthdate, birthplace);
    }
}