package com.example.minpix.dto.response;

import com.example.minpix.models.Comprovante;

import java.time.LocalDate;

public record ComprovanteResponse(
        Long id,
        Long transferenciaId,
        String codigoAutenticacao,
        LocalDate dataGeracao,
        String status
) {

    public static ComprovanteResponse from(Comprovante comprovante) {
        return new ComprovanteResponse(
                comprovante.getId(),
                comprovante.getTransferencia().getId(),
                comprovante.getCodigoAutenticacao(),
                comprovante.getDataGeracao(),
                comprovante.getStatus()
        );
    }
}
