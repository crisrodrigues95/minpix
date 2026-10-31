package com.example.minpix.integration;

import com.example.minpix.dto.request.ChavePixRequest;
import com.example.minpix.dto.request.ComprovanteRequest;
import com.example.minpix.dto.request.ContaRequest;
import com.example.minpix.dto.request.FavorecidoRequest;
import com.example.minpix.dto.request.TransferenciaPixRequest;
import com.example.minpix.enums.StatusPixEnum;
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
import java.time.LocalDate;

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
class ContaApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void createsAndReadsPixEntitiesLinkedToAnAccount() throws Exception {
        long contaId = createAndGetId("/contas", new ContaRequest(
                "Ana Costa",
                "529.982.247-25",
                "Banco XPTO",
                "0001",
                "12345-6",
                "senha-segura",
                new BigDecimal("1000.00"),
                new BigDecimal("1500.00")
        ));

        long chaveId = createAndGetId("/chaves-pix", new ChavePixRequest(
                "CPF", "52998224725", contaId, true
        ));
        mockMvc.perform(get("/chaves-pix/{id}", chaveId).with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("CPF"))
                .andExpect(jsonPath("$.valorChave").value("52998224725"))
                .andExpect(jsonPath("$.contaId").value(contaId))
                .andExpect(jsonPath("$.ativa").value(true));
        mockMvc.perform(put("/chaves-pix/{id}", chaveId)
                        .with(jwt())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(new ChavePixRequest(
                                "CPF", "52998224725", contaId, false
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ativa").value(false));

        long favorecidoId = createAndGetId("/favorecidos", new FavorecidoRequest(
                "Maria Silva", "Banco XPTO", "maria@example.com", "EMAIL", contaId
        ));
        mockMvc.perform(get("/favorecidos/{id}", favorecidoId).with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria Silva"))
                .andExpect(jsonPath("$.chavePix").value("maria@example.com"))
                .andExpect(jsonPath("$.contaOrigemId").value(contaId));
        mockMvc.perform(put("/favorecidos/{id}", favorecidoId)
                        .with(jwt())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(new FavorecidoRequest(
                                "Maria Souza", "Banco XPTO", "maria.souza@example.com", "EMAIL", contaId
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria Souza"));

        LocalDate transferDate = LocalDate.of(2026, 10, 30);
        long transferenciaId = createAndGetId("/transferencias-pix", new TransferenciaPixRequest(
                contaId,
                favorecidoId,
                new BigDecimal("25.50"),
                "Pagamento de teste",
                transferDate,
                null,
                StatusPixEnum.CONCLUIDA
        ));
        mockMvc.perform(get("/transferencias-pix/{id}", transferenciaId).with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contaOrigemId").value(contaId))
                .andExpect(jsonPath("$.favorecidoId").value(favorecidoId))
                .andExpect(jsonPath("$.valor").value(25.50))
                .andExpect(jsonPath("$.dataTransferencia").value(transferDate.toString()))
                .andExpect(jsonPath("$.status").value("CONCLUIDA"));
        mockMvc.perform(put("/transferencias-pix/{id}", transferenciaId)
                        .with(jwt())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(new TransferenciaPixRequest(
                                contaId,
                                favorecidoId,
                                new BigDecimal("30.75"),
                                "Pagamento atualizado",
                                transferDate,
                                null,
                                StatusPixEnum.CONCLUIDA
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor").value(30.75))
                .andExpect(jsonPath("$.descricao").value("Pagamento atualizado"));

        long comprovanteId = createAndGetId("/comprovantes", new ComprovanteRequest(
                transferenciaId,
                "ABCD1234",
                transferDate,
                StatusPixEnum.GERADO
        ));
        mockMvc.perform(get("/comprovantes/{id}", comprovanteId).with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transferenciaId").value(transferenciaId))
                .andExpect(jsonPath("$.codigoAutenticacao").value("ABCD1234"))
                .andExpect(jsonPath("$.dataGeracao").value(transferDate.toString()))
                .andExpect(jsonPath("$.status").value("GERADO"));
        mockMvc.perform(put("/comprovantes/{id}", comprovanteId)
                        .with(jwt())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(new ComprovanteRequest(
                                transferenciaId,
                                "EFGH5678",
                                transferDate,
                                StatusPixEnum.ATUALIZADO
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoAutenticacao").value("EFGH5678"))
                .andExpect(jsonPath("$.status").value("ATUALIZADO"));

        mockMvc.perform(get("/chaves-pix").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(chaveId));
        mockMvc.perform(get("/favorecidos").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(favorecidoId));
        mockMvc.perform(get("/transferencias-pix").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(transferenciaId));
        mockMvc.perform(get("/comprovantes").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(comprovanteId));

        mockMvc.perform(delete("/comprovantes/{id}", comprovanteId).with(jwt()))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/transferencias-pix/{id}", transferenciaId).with(jwt()))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/favorecidos/{id}", favorecidoId).with(jwt()))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/chaves-pix/{id}", chaveId).with(jwt()))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/contas/{id}", contaId).with(jwt()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/comprovantes/{id}", comprovanteId).with(jwt()))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/transferencias-pix/{id}", transferenciaId).with(jwt()))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/favorecidos/{id}", favorecidoId).with(jwt()))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/chaves-pix/{id}", chaveId).with(jwt()))
                .andExpect(status().isNotFound());
        mockMvc.perform(get("/contas/{id}", contaId).with(jwt()))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejectsPixEntityRequestsWithInvalidOrMissingReferences() throws Exception {
        mockMvc.perform(post("/chaves-pix")
                        .with(jwt())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(new ChavePixRequest(
                                "CPF", "11111111111", 1L, true
                        ))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.valorChave").exists());

        mockMvc.perform(post("/chaves-pix")
                        .with(jwt())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(new ChavePixRequest(
                                "CPF", "52998224725", 999L, true
                        ))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Conta não encontrada: 999"));
    }

    private long createAndGetId(String path, Object request) throws Exception {
        MvcResult result = mockMvc.perform(post(path)
                        .with(jwt())
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode response = mapper.readTree(result.getResponse().getContentAsString());
        assertTrue(response.hasNonNull("id"), "Expected response to include an id");
        return response.get("id").longValue();
    }
}
