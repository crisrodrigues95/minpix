package com.example.minpix.dto.request;

import com.example.minpix.validation.PixKeyPayload;
import com.example.minpix.validation.ValidPixKey;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@ValidPixKey
public record FavorecidoRequest(
        @NotBlank @Size(max = 120) String nome,
        @NotBlank @Size(max = 100) String banco,
        @NotBlank @Size(max = 254) String chavePix,
        @NotBlank @Size(max = 20) String tipoChave,
        @NotNull @Positive Long contaOrigemId
) implements PixKeyPayload {

    @Override
    public String pixKeyType() {
        return tipoChave;
    }

    @Override
    public String pixKeyValue() {
        return chavePix;
    }

    @Override
    public String pixKeyValueField() {
        return "chavePix";
    }
}
