package com.example.minpix.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ComprovanteRequest(
        @NotNull @Positive Long transferenciaId,
        @NotBlank @Pattern(regexp = "[A-Za-z0-9]{8,64}") String codigoAutenticacao,
        LocalDate dataGeracao,
        @NotBlank @Size(max = 30) String status
) {
}
