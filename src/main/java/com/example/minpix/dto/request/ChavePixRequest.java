package com.example.minpix.dto.request;

import com.example.minpix.validation.PixKeyPayload;
import com.example.minpix.validation.ValidPixKey;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@ValidPixKey
public record ChavePixRequest(
        @NotBlank @Size(max = 20) String tipo,
        @NotBlank @Size(max = 254) String valorChave,
        @NotNull @Positive Long contaId,
        @NotNull Boolean ativa
) implements PixKeyPayload {

    @Override
    public String pixKeyType() {
        return tipo;
    }

    @Override
    public String pixKeyValue() {
        return valorChave;
    }

    @Override
    public String pixKeyValueField() {
        return "valorChave";
    }
}
