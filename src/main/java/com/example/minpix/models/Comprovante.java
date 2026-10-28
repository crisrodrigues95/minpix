package com.example.minpix.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comprovantes")
public class Comprovante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do comprovante", example = "1")
    private Long id;

    @Schema(description = "Transferência vinculada ao comprovante")
    @OneToOne
    @JoinColumn(name = "transferencia_id", nullable = false, unique = true)
    private TransferenciaPix transferencia;

    @Schema(description = "Código de autenticação do comprovante", example = "ABC123XYZ987")
    @Column(name = "codigo_autenticacao")
    private String codigoAutenticacao;

    @Schema(description = "Data de geração do comprovante", example = "2026-05-21")
    @Column(name = "data_geracao")
    private LocalDate dataGeracao;

    @Schema(description = "Status do comprovante", example = "GERADO")
    private String status;
}
