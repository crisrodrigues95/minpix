package com.example.minpix.dto.response;

import com.example.minpix.models.Favorecido;

import java.time.LocalDate;

public record FavorecidoResponse(
        Long id,
        String nome,
        String banco,
        String chavePix,
        String tipoChave,
        Long contaOrigemId,
        LocalDate dataCadastro
) {

    public static FavorecidoResponse from(Favorecido favorecido) {
        return new FavorecidoResponse(
                favorecido.getId(),
                favorecido.getNome(),
                favorecido.getBanco(),
                favorecido.getChavePix(),
                favorecido.getTipoChave(),
                favorecido.getContaOrigem().getId(),
                favorecido.getDataCadastro()
        );
    }
}
