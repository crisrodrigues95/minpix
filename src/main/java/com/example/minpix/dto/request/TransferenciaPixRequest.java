package com.example.minpix.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransferenciaPixRequest(
        @NotNull @Positive Long contaOrigemId,
        @NotNull @Positive Long favorecidoId,
        @NotNull @DecimalMin("0.01") @Digits(integer = 15, fraction = 2) BigDecimal valor,
        @Size(max = 255) String descricao,
        LocalDate dataTransferencia,
        LocalDate dataAgendamento,
        @NotBlank @Size(max = 30) String status
) {
}
