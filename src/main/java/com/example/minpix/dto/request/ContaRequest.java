package com.example.minpix.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

public record ContaRequest(
        @NotBlank @Size(max = 120) String nomeTitular,
        @NotBlank @CPF String cpf,
        @NotBlank @Size(max = 100) String banco,
        @NotBlank @Size(max = 30) String agencia,
        @NotBlank @Size(max = 30) String numeroConta,
        @NotBlank @Size(max = 72) String senha,
        @NotNull @DecimalMin("0.00") @Digits(integer = 15, fraction = 2) BigDecimal saldo,
        @NotNull @DecimalMin("0.00") @Digits(integer = 15, fraction = 2) BigDecimal limiteDiario
) {
}
