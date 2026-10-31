package com.example.minpix.services;

import com.example.minpix.exceptions.DuplicateAccountNumberException;
import com.example.minpix.exceptions.ApiExceptionHandler;
import com.example.minpix.models.Conta;
import com.example.minpix.repositories.ContaRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void createsAccountAndSetsRegistrationDate() {
        Conta conta = new Conta();
        conta.setNumeroConta("98765-4");
        when(contaRepository.save(conta)).thenReturn(conta);

        Conta criada = contaService.criarConta(conta);

        assertSame(conta, criada);
        assertEquals(LocalDate.now(), criada.getDataCadastro());
        verify(contaRepository).existsByNumeroConta("98765-4");
        verify(contaRepository).save(conta);
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
    void updatesAccountAndPreservesOriginalIdAndRegistrationDate() {
        LocalDate registrationDate = LocalDate.of(2025, 1, 15);
        Conta existing = new Conta();
        existing.setId(1L);
        existing.setDataCadastro(registrationDate);
        Conta updated = new Conta();
        updated.setNumeroConta("98765-4");
        when(contaRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(contaRepository.save(updated)).thenReturn(updated);

        Conta result = contaService.atualizar(1L, updated);

        assertSame(updated, result);
        assertEquals(1L, updated.getId());
        assertEquals(registrationDate, updated.getDataCadastro());
        verify(contaRepository).existsByNumeroContaAndIdNot("98765-4", 1L);
        verify(contaRepository).save(updated);
    }

    @Test
    void doesNotUpdateAccountWhenIdDoesNotExist() {
        Conta updated = new Conta();
        when(contaRepository.findById(99L)).thenReturn(Optional.empty());

        assertNull(contaService.atualizar(99L, updated));

        verify(contaRepository, never()).existsByNumeroContaAndIdNot(anyString(), anyLong());
        verify(contaRepository, never()).save(any(Conta.class));
    }

    @Test
    void listsAccounts() {
        List<Conta> contas = List.of(new Conta(), new Conta());
        when(contaRepository.findAll()).thenReturn(contas);

        assertSame(contas, contaService.listarContas());
    }

    @Test
    void returnsAccountWhenFoundAndNullWhenMissing() {
        Conta conta = new Conta();
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(contaRepository.findById(2L)).thenReturn(Optional.empty());

        assertSame(conta, contaService.buscarPorId(1L));
        assertNull(contaService.buscarPorId(2L));
    }

    @Test
    void deletesExistingAccount() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(new Conta()));

        assertTrue(contaService.excluir(1L));

        verify(contaRepository).deleteById(1L);
    }

    @Test
    void doesNotDeleteMissingAccount() {
        when(contaRepository.findById(1L)).thenReturn(Optional.empty());

        assertFalse(contaService.excluir(1L));

        verify(contaRepository, never()).deleteById(anyLong());
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
