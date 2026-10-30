package com.example.minpix.services;



import com.example.minpix.models.Conta;
import com.example.minpix.repositories.ContaRepository;
import com.example.minpix.exceptions.DuplicateAccountNumberException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContaService {

    private final ContaRepository contaRepository;

    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }
/*
    public Conta getContaSample(){

        Conta conta = new Conta();

        conta.setId(1L);
        conta.setNomeTitular("Nome Sample");
        conta.setCpf("123.456.789-10");
        conta.setBanco("002");
        conta.setAgencia("1234");
        conta.setNumeroConta("1010-11");
        conta.setSaldo(BigDecimal.valueOf(2500));
        conta.setLimiteDiario(BigDecimal.valueOf(1500));
        conta.setDataCadastro(LocalDate.now());

        return conta;
        
    }*/

    public Conta criarConta(Conta conta) {

        if (contaRepository.existsByNumeroConta(conta.getNumeroConta())) {
            throw new DuplicateAccountNumberException(conta.getNumeroConta());
        }

        conta.setDataCadastro(LocalDate.now());

        return contaRepository.save(conta);
    }

    public List<Conta> listarContas() {
        return contaRepository.findAll();
    }

    public Conta buscarPorId(Long id) {
        return contaRepository.findById(id).orElse(null);
    }

    public Conta atualizar(Long id, Conta contaAtualizada) {

        Conta conta = buscarPorId(id);

        if (conta == null) {
            return null;
        }

        if (contaRepository.existsByNumeroContaAndIdNot(contaAtualizada.getNumeroConta(), id)) {
            throw new DuplicateAccountNumberException(contaAtualizada.getNumeroConta());
        }

        contaAtualizada.setId(id);
        contaAtualizada.setDataCadastro(conta.getDataCadastro());

        return contaRepository.save(contaAtualizada);
    }

    public boolean excluir(Long id) {

        Conta conta = buscarPorId(id);

        if (conta == null) {
            return false;
        }

        contaRepository.deleteById(id);

        return true;
    }

}
