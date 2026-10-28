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
@Table(name = "chaves_pix")
public class ChavePix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID da chave Pix", example = "1")
    private Long id;

    @Schema(description = "Tipo da chave Pix", example = "CPF")
    private String tipo;

    @Schema(description = "Valor da chave Pix", example = "123.456.789-00")
    @Column(name = "valor_chave", unique = true, nullable = false)
    private String valorChave;

    @Schema(description = "Conta vinculada à chave Pix")
    @ManyToOne
    @JoinColumn(name = "conta_id", nullable = false)
    private Conta conta;

    @Schema(description = "Indica se a chave está ativa", example = "true")
    private Boolean ativa;

    @Schema(description = "Data de cadastro da chave Pix", example = "2026-05-21")
    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;
}
