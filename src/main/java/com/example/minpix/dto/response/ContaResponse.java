package com.example.minpix.dto.response;

import com.example.minpix.models.Conta;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContaResponse(
        Long id,
        String nomeTitular,
        String cpf,
        String banco,
        String agencia,
        String numeroConta,
        BigDecimal saldo,
        BigDecimal limiteDiario,
        LocalDate dataCadastro
) {

    public static ContaResponse from(Conta conta) {
        return new ContaResponse(
                conta.getId(),
                conta.getNomeTitular(),
                conta.getCpf(),
                conta.getBanco(),
                conta.getAgencia(),
                conta.getNumeroConta(),
                conta.getSaldo(),
                conta.getLimiteDiario(),
                conta.getDataCadastro()
        );
    }
}
