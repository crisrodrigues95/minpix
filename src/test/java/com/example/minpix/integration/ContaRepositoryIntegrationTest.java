package com.example.minpix.integration;



import com.example.minpix.models.Conta;
import com.example.minpix.repositories.ContaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class ContaRepositoryIntegrationTest {

    @Autowired
    private ContaRepository contaRepository;

    @Test
    void deveSalvarConta() {

        Conta conta = new Conta();

        conta.setNomeTitular("Maria");
        conta.setCpf("12345678900");
        conta.setBanco("Banco XPTO");
        conta.setAgencia("0001");
        conta.setNumeroConta("12345");
        conta.setSenha("1234");
        conta.setSaldo(new BigDecimal("1000"));
        conta.setLimiteDiario(new BigDecimal("500"));
        conta.setDataCadastro(LocalDate.now());
        Conta salva = contaRepository.save(conta);

        assertNotNull(
                salva.getId()
        );
    }

    @Test
    void deveListarContas() {

        Conta conta = new Conta();

        conta.setNomeTitular("Maria");
        conta.setCpf("12345678900");
        conta.setBanco("Banco XPTO");
        conta.setAgencia("0001");
        conta.setNumeroConta("12345");
        conta.setSenha("1234");
        conta.setSaldo(new BigDecimal("1000"));
        conta.setLimiteDiario(new BigDecimal("500"));
        conta.setDataCadastro(LocalDate.now());

        contaRepository.save(conta);

        List<Conta> contas =
                contaRepository.findAll();

        assertFalse(
                contas.isEmpty()
        );
    }
}
