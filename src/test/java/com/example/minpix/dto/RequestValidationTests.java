package com.example.minpix.dto;

import com.example.minpix.dto.request.ChavePixRequest;
import com.example.minpix.dto.request.ContaRequest;
import com.example.minpix.dto.request.FavorecidoRequest;
import com.example.minpix.dto.response.ContaResponse;
import com.example.minpix.models.Conta;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestValidationTests {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidatorFactory() {
        validatorFactory.close();
    }

    @Test
    void acceptsValidCpfAndNonNegativeAmountsForAccount() {
        ContaRequest request = new ContaRequest(
                "Maria Silva",
                "529.982.247-25",
                "Banco XPTO",
                "0001",
                "12345-6",
                "senha-segura",
                new BigDecimal("100.00"),
                new BigDecimal("500.00")
        );

        assertTrue(validator.validate(request).isEmpty());

        ContaRequest unformattedCpfRequest = new ContaRequest(
                "Maria Silva",
                "52998224725",
                "Banco XPTO",
                "0001",
                "12345-6",
                "senha-segura",
                new BigDecimal("100.00"),
                new BigDecimal("500.00")
        );
        assertTrue(validator.validate(unformattedCpfRequest).isEmpty());
    }

    @Test
    void rejectsCpfWithInvalidCheckDigits() {
        ContaRequest request = new ContaRequest(
                "Maria Silva",
                "529.982.247-24",
                "Banco XPTO",
                "0001",
                "12345-6",
                "senha-segura",
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        assertTrue(validator.validate(request).stream()
                .anyMatch(error -> error.getPropertyPath().toString().equals("cpf")));
    }

    @Test
    void validatesPixKeyValueAgainstItsDeclaredType() {
        assertTrue(validator.validate(new ChavePixRequest("CPF", "52998224725", 1L, true)).isEmpty());
        assertTrue(validator.validate(new ChavePixRequest("CNPJ", "11.222.333/0001-81", 1L, true)).isEmpty());
        assertTrue(validator.validate(new ChavePixRequest("TELEFONE", "+5511999999999", 1L, true)).isEmpty());
        assertTrue(validator.validate(new ChavePixRequest(
                "ALEATORIA", "550e8400-e29b-41d4-a716-446655440000", 1L, true
        )).isEmpty());
        assertFalse(validator.validate(new ChavePixRequest("CPF", "11111111111", 1L, true)).isEmpty());
        assertFalse(validator.validate(new ChavePixRequest("CNPJ", "11.222.333/0001-80", 1L, true)).isEmpty());
        assertFalse(validator.validate(new ChavePixRequest("TIPO_DESCONHECIDO", "abc", 1L, true)).isEmpty());
        assertFalse(validator.validate(new ChavePixRequest("ALEATORIA", "1-1-1-1-1", 1L, true)).isEmpty());
    }

    @Test
    void validatesFavorecidoPixEmail() {
        FavorecidoRequest validRequest = new FavorecidoRequest(
                "Maria Silva", "Banco XPTO", "maria@example.com", "EMAIL", 1L
        );
        FavorecidoRequest invalidRequest = new FavorecidoRequest(
                "Maria Silva", "Banco XPTO", "maria@", "EMAIL", 1L
        );

        assertTrue(validator.validate(validRequest).isEmpty());
        assertTrue(validator.validate(invalidRequest).stream()
                .anyMatch(error -> error.getPropertyPath().toString().equals("chavePix")));
    }

    @Test
    void accountResponseDoesNotSerializePassword() {
        Conta conta = new Conta();
        conta.setId(1L);
        conta.setNumeroConta("12345-6");
        conta.setSenha("senha-secreta");

        var json = new ObjectMapper().valueToTree(ContaResponse.from(conta));

        assertFalse(json.has("senha"));
        assertTrue(json.has("numeroConta"));
    }
}
