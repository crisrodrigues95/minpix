package com.example.minpix.services;

import com.example.minpix.exceptions.DuplicateAccountNumberException;
import com.example.minpix.exceptions.ApiExceptionHandler;
import com.example.minpix.models.Conta;
import com.example.minpix.repositories.ContaRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ContaServiceTests {

    private final ContaRepository contaRepository = mock(ContaRepository.class);
    private final ContaService contaService = new ContaService(contaRepository);

    @Test
    void rejectsCreatingAccountWithExistingNumber() {
        Conta conta = new Conta();
        conta.setNumeroConta("98765-4");
        when(contaRepository.existsByNumeroConta("98765-4")).thenReturn(true);

        assertThrows(DuplicateAccountNumberException.class, () -> contaService.criarConta(conta));
        verify(contaRepository, never()).save(conta);
    }

    @Test
    void rejectsUpdatingAccountToAnotherAccountsNumber() {
        Conta existing = new Conta();
        existing.setId(1L);
        Conta updated = new Conta();
        updated.setNumeroConta("98765-4");

        when(contaRepository.findById(1L)).thenReturn(java.util.Optional.of(existing));
        when(contaRepository.existsByNumeroContaAndIdNot("98765-4", 1L)).thenReturn(true);

        assertThrows(DuplicateAccountNumberException.class, () -> contaService.atualizar(1L, updated));
        verify(contaRepository, never()).save(updated);
    }

    @Test
    void mapsDuplicateAccountNumberToConflictResponse() {
        ApiExceptionHandler handler = new ApiExceptionHandler();

        var response = handler.handleDuplicateAccountNumber(
                new DuplicateAccountNumberException("98765-4")
        );

        assertEquals(409, response.getStatusCode().value());
        assertEquals("Já existe uma conta com o número 98765-4.", response.getBody().get("error"));
    }
}
