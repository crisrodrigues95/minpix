package com.example.minpix.dto.request;

import com.example.minpix.enums.StatusPixEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record ComprovanteRequest(
        @NotNull @Positive Long transferenciaId,
        @NotBlank @Pattern(regexp = "[A-Za-z0-9]{8,64}") String codigoAutenticacao,
        LocalDate dataGeracao,
        @NotNull StatusPixEnum status
) {
}
