package com.finance.project.controllerLayer.unitTests;

import com.finance.project.modules.person.application.CreatePersonTransactionService;
import com.finance.project.controllerLayer.integrationTests.AbstractTest;
import com.finance.project.domainLayer.exceptions.NotFoundArgumentsBusinessException;
import com.finance.project.domainLayer.exceptions.InvalidArgumentsBusinessException;
import com.finance.project.dtos.dtos.CreatePersonTransactionDTO;
import com.finance.project.dtos.dtos.PersonDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Lab07 - TDD: tests escritos ANTES del refactoring.
// T2, T4, T5, T6 fallan intencionalmente hasta aplicar los fixes M8 y M9.
class CreatePersonTransactionControllerRESTTest extends AbstractTest {

    @MockBean
    private CreatePersonTransactionService service;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    protected void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    // -------------------------------------------------------------------------
    // T1: createPersonTransaction — persona y datos validos -> 201 CREATED
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("T1: POST transaccion valida retorna 201 CREATED con links HATEOAS")
    void createPersonTransaction_validData_returns201() throws Exception {

        // Arrange
        String personEmail = "miguel@gmail.com";
        PersonDTO mockPerson = new PersonDTO(personEmail, "Miguel Lemos", "2000-10-23", "Vila Nova de Gaia", "Is Not Defined", "Is Not Defined", "");
        Mockito.when(service.createTransaction(Mockito.any(CreatePersonTransactionDTO.class)))
               .thenReturn(mockPerson);

        String body = "{" +
            "\"denominationCategory\":\"SALARY\"," +
            "\"type\":\"credit\"," +
            "\"description\":\"Monthly salary\"," +
            "\"amount\":1500.0," +
            "\"denominationAccountDeb\":\"Bank Account\"," +
            "\"denominationAccountCred\":\"Wallet\"," +
            "\"date\":\"2024-01-15\"}";

        // Act & Assert
        mvc.perform(post("/persons/{email}/ledgers/records", personEmail)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.email").value(personEmail));
    }

    // -------------------------------------------------------------------------
    // T2: createPersonTransaction — persona inexistente -> 404 NOT FOUND
    // FALLA antes del refactoring (M8: sin manejo de excepciones)
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("T2 [TDD-FALLA]: POST persona inexistente retorna 404 NOT FOUND")
    void createPersonTransaction_personNotFound_returns404() throws Exception {

        // Arrange
        String unknownEmail = "noexiste@gmail.com";
        Mockito.when(service.createTransaction(Mockito.any(CreatePersonTransactionDTO.class)))
               .thenThrow(new NotFoundArgumentsBusinessException("Person does not exist"));

        String body = "{" +
            "\"denominationCategory\":\"SALARY\"," +
            "\"type\":\"credit\"," +
            "\"description\":\"Test\"," +
            "\"amount\":100.0," +
            "\"denominationAccountDeb\":\"Bank\"," +
            "\"denominationAccountCred\":\"Wallet\"," +
            "\"date\":\"2024-01-15\"}";

        // Act & Assert
        mvc.perform(post("/persons/{email}/ledgers/records", unknownEmail)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
           .andExpect(status().isNotFound());
    }

    // -------------------------------------------------------------------------
    // T3: updatePersonTransaction — datos validos -> 200 OK con links HATEOAS
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("T3: PUT transaccion valida retorna 200 OK con links HATEOAS")
    void updatePersonTransaction_validData_returns200() throws Exception {

        // Arrange
        String personEmail = "miguel@gmail.com";
        int transactionNumber = 1;
        PersonDTO mockPerson = new PersonDTO(personEmail, "Miguel Lemos", "2000-10-23", "Vila Nova de Gaia", "Is Not Defined", "Is Not Defined", "");
        Mockito.when(service.updateTransaction(Mockito.any()))
               .thenReturn(mockPerson);

        String body = "{" +
            "\"denominationCategory\":\"SALARY\"," +
            "\"type\":\"credit\"," +
            "\"description\":\"Updated salary\"," +
            "\"amount\":2000.0," +
            "\"denominationAccountDeb\":\"Bank Account\"," +
            "\"denominationAccountCred\":\"Wallet\"," +
            "\"date\":\"2024-01-20\"}";

        // Act & Assert
        mvc.perform(put("/persons/{email}/ledgers/records/{num}", personEmail, transactionNumber)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.email").value(personEmail));
    }

    // -------------------------------------------------------------------------
    // T4: updatePersonTransaction — transactionNumber negativo -> 400 BAD REQUEST
    // FALLA antes del refactoring (M8: sin validacion en delete)
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("T4 [TDD-FALLA]: PUT con transactionNumber invalido retorna 400 BAD REQUEST")
    void updatePersonTransaction_invalidNumber_returns400() throws Exception {

        // Arrange
        String personEmail = "miguel@gmail.com";
        int invalidNumber = -1;
        Mockito.when(service.updateTransaction(Mockito.any()))
               .thenThrow(new InvalidArgumentsBusinessException("Invalid transaction number"));

        String body = "{" +
            "\"denominationCategory\":\"SALARY\"," +
            "\"type\":\"credit\"," +
            "\"description\":\"Test\"," +
            "\"amount\":100.0," +
            "\"denominationAccountDeb\":\"Bank\"," +
            "\"denominationAccountCred\":\"Wallet\"," +
            "\"date\":\"2024-01-15\"}";

        // Act & Assert
        mvc.perform(put("/persons/{email}/ledgers/records/{num}", personEmail, invalidNumber)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
           .andExpect(status().isBadRequest());
    }

    // -------------------------------------------------------------------------
    // T5: deletePersonTransaction — valido -> 204 NO_CONTENT
    // FALLA antes del refactoring (M9: actualmente devuelve 200 en vez de 204)
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("T5 [TDD-FALLA]: DELETE valido retorna 204 NO_CONTENT")
    void deletePersonTransaction_validRequest_returns204() throws Exception {

        // Arrange
        String personEmail = "miguel@gmail.com";
        int transactionNumber = 1;
        PersonDTO mockPerson = new PersonDTO(personEmail, "Miguel Lemos", "2000-10-23", "Vila Nova de Gaia", "Is Not Defined", "Is Not Defined", "");
        Mockito.when(service.deleteTransaction(Mockito.any()))
               .thenReturn(mockPerson);

        // Act & Assert
        mvc.perform(delete("/persons/{email}/ledgers/records/{num}", personEmail, transactionNumber))
           .andExpect(status().isNoContent());
    }

    // -------------------------------------------------------------------------
    // T6: deletePersonTransaction — persona inexistente -> 404 NOT FOUND
    // FALLA antes del refactoring (M8: sin manejo de excepciones)
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("T6 [TDD-FALLA]: DELETE persona inexistente retorna 404 NOT FOUND")
    void deletePersonTransaction_personNotFound_returns404() throws Exception {

        // Arrange
        String unknownEmail = "noexiste@gmail.com";
        Mockito.when(service.deleteTransaction(Mockito.any()))
               .thenThrow(new NotFoundArgumentsBusinessException("Person does not exist"));

        // Act & Assert
        mvc.perform(delete("/persons/{email}/ledgers/records/{num}", unknownEmail, 1))
           .andExpect(status().isNotFound());
    }

    // -------------------------------------------------------------------------
    // T7: links HATEOAS presentes en respuesta exitosa de create
    // -------------------------------------------------------------------------
    @Test
    @DisplayName("T7: Respuesta exitosa incluye los 4 links HATEOAS")
    void createPersonTransaction_returnsAllHateoasLinks() throws Exception {

        // Arrange
        String personEmail = "miguel@gmail.com";
        PersonDTO mockPerson = new PersonDTO(personEmail, "Miguel Lemos", "2000-10-23", "Vila Nova de Gaia", "Is Not Defined", "Is Not Defined", "");
        Mockito.when(service.createTransaction(Mockito.any(CreatePersonTransactionDTO.class)))
               .thenReturn(mockPerson);

        String body = "{" +
            "\"denominationCategory\":\"SALARY\"," +
            "\"type\":\"credit\"," +
            "\"description\":\"Monthly salary\"," +
            "\"amount\":1500.0," +
            "\"denominationAccountDeb\":\"Bank Account\"," +
            "\"denominationAccountCred\":\"Wallet\"," +
            "\"date\":\"2024-01-15\"}";

        // Act & Assert
        mvc.perform(post("/persons/{email}/ledgers/records", personEmail)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$._links.siblings").exists())
           .andExpect(jsonPath("$._links.records").exists())
           .andExpect(jsonPath("$._links.accounts").exists())
           .andExpect(jsonPath("$._links.categories").exists());
    }
}
