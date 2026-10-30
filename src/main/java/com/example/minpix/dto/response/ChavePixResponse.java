package com.example.minpix.dto.response;

import com.example.minpix.models.ChavePix;

import java.time.LocalDate;

public record ChavePixResponse(
        Long id,
        String tipo,
        String valorChave,
        Long contaId,
        Boolean ativa,
        LocalDate dataCadastro
) {

    public static ChavePixResponse from(ChavePix chavePix) {
        return new ChavePixResponse(
                chavePix.getId(),
                chavePix.getTipo(),
                chavePix.getValorChave(),
                chavePix.getConta().getId(),
                chavePix.getAtiva(),
                chavePix.getDataCadastro()
        );
    }
}
