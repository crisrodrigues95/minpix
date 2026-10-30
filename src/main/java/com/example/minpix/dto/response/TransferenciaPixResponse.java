package com.example.minpix.dto.response;

import com.example.minpix.models.TransferenciaPix;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransferenciaPixResponse(
        Long id,
        Long contaOrigemId,
        Long favorecidoId,
        BigDecimal valor,
        String descricao,
        LocalDate dataTransferencia,
        LocalDate dataAgendamento,
        String status,
        Long comprovanteId
) {

    public static TransferenciaPixResponse from(TransferenciaPix transferencia) {
        return new TransferenciaPixResponse(
                transferencia.getId(),
                transferencia.getContaOrigem().getId(),
                transferencia.getFavorecido().getId(),
                transferencia.getValor(),
                transferencia.getDescricao(),
                transferencia.getDataTransferencia(),
                transferencia.getDataAgendamento(),
                transferencia.getStatus(),
                transferencia.getComprovante() == null ? null : transferencia.getComprovante().getId()
        );
    }
}
