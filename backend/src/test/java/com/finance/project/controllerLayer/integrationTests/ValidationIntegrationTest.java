package com.finance.project.controllerLayer.integrationTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ValidationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createTransaction_withNegativeAmount_returns400() throws Exception {
        String invalidBody = "{" +
                "\"denominationCategory\": \"Food\"," +
                "\"type\": \"debit\"," +
                "\"description\": \"Lunch\"," +
                "\"amount\": -50.0," +
                "\"denominationAccountDeb\": \"Wallet\"," +
                "\"denominationAccountCred\": \"Bank\"," +
                "\"date\": \"2024-01-01\"" +
                "}";

        mockMvc.perform(post("/persons/miguel@gmail.com/ledgers/records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createTransaction_withBlankCategory_returns400() throws Exception {
        String invalidBody = "{" +
                "\"denominationCategory\": \"\"," +
                "\"type\": \"debit\"," +
                "\"description\": \"Lunch\"," +
                "\"amount\": 50.0," +
                "\"denominationAccountDeb\": \"Wallet\"," +
                "\"denominationAccountCred\": \"Bank\"," +
                "\"date\": \"2024-01-01\"" +
                "}";

        mockMvc.perform(post("/persons/miguel@gmail.com/ledgers/records")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createPerson_withInvalidEmail_returns400() throws Exception {
        String invalidBody = "{" +
                "\"email\": \"not-an-email\"," +
                "\"name\": \"Miguel\"," +
                "\"birthdate\": \"1990-01-01\"," +
                "\"birthplace\": \"Porto\"" +
                "}";

        mockMvc.perform(post("/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidBody))
                .andExpect(status().isBadRequest());
    }
}