package com.example.minpix.models;



import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "favorecidos")
public class Favorecido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID do favorecido", example = "1")
    private Long id;

    @Schema(description = "Nome do favorecido", example = "Maria Silva")
    private String nome;

    @Schema(description = "Banco do favorecido", example = "Banco XPTO")
    private String banco;

    @Schema(description = "Chave Pix do favorecido", example = "maria@email.com")
    @Column(name = "chave_pix")
    private String chavePix;

    @Schema(description = "Tipo da chave Pix", example = "EMAIL")
    @Column(name = "tipo_chave")
    private String tipoChave;

    @Schema(description = "Conta que cadastrou o favorecido")
    @ManyToOne
    @JoinColumn(name = "conta_origem_id", nullable = false)
    private Conta contaOrigem;

    @Schema(description = "Data de cadastro do favorecido", example = "2026-05-21")
    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @Schema(description = "Transferências realizadas para o favorecido")
    @OneToMany(mappedBy = "favorecido")
    private List<TransferenciaPix> transferencias;
}
