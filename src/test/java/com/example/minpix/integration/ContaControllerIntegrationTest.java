package com.example.minpix.integration;

import com.example.minpix.dto.request.ContaRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ContaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void createsListsReadsUpdatesAndDeletesAccount() throws Exception {
        MvcResult creation = mockMvc.perform(post("/contas")
                        .with(jwt())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(validRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomeTitular").value("Maria Silva"))
                .andExpect(jsonPath("$.numeroConta").value("12345-6"))
                .andExpect(jsonPath("$.dataCadastro").isNotEmpty())
                .andExpect(jsonPath("$.senha").doesNotExist())
                .andReturn();
        long id = accountId(creation);

        mockMvc.perform(get("/contas").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].cpf").value("529.982.247-25"))
                .andExpect(jsonPath("$[0].saldo").value(1000));

        mockMvc.perform(get("/contas/{id}", id).with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nomeTitular").value("Maria Silva"));

        mockMvc.perform(put("/contas/{id}", id)
                        .with(jwt())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(new ContaRequest(
                                "Maria Souza",
                                "529.982.247-25",
                                "Banco XPTO",
                                "0001",
                                "12345-6",
                                "senha-segura",
                                new BigDecimal("1250.00"),
                                new BigDecimal("1500.00")
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeTitular").value("Maria Souza"))
                .andExpect(jsonPath("$.id").value(id));

        mockMvc.perform(delete("/contas/{id}", id).with(jwt()))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/contas/{id}", id).with(jwt()))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectsDuplicateAccountNumberWithConflict() throws Exception {
        mockMvc.perform(post("/contas")
                        .with(jwt())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(validRequest())))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/contas")
                        .with(jwt())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(new ContaRequest(
                                "Outra Titular",
                                "529.982.247-25",
                                "Outro Banco",
                                "0002",
                                "12345-6",
                                "outra-senha",
                                BigDecimal.ZERO,
                                BigDecimal.ZERO
                        ))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Já existe uma conta com o número 12345-6."));
    }

    @Test
    void rejectsInvalidAccountRequest() throws Exception {
        ContaRequest invalidRequest = new ContaRequest(
                "Maria Silva",
                "12345678900",
                "Banco XPTO",
                "0001",
                "12345-6",
                "senha-segura",
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        mockMvc.perform(post("/contas")
                        .with(jwt())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.cpf").exists());
    }

    @Test
    void requiresAuthenticationToAccessAccounts() throws Exception {
        mockMvc.perform(get("/contas"))
                .andExpect(status().isUnauthorized());
    }

    private ContaRequest validRequest() {
        return new ContaRequest(
                "Maria Silva",
                "529.982.247-25",
                "Banco XPTO",
                "0001",
                "12345-6",
                "senha-segura",
                new BigDecimal("1000.00"),
                new BigDecimal("1500.00")
        );
    }

    private long accountId(MvcResult result) throws Exception {
        JsonNode response = mapper.readTree(result.getResponse().getContentAsString());
        assertTrue(response.hasNonNull("id"), "Expected response to include an id");
        return response.get("id").longValue();
    }
}
