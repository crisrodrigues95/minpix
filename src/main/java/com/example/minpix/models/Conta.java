package com.example.minpix.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contas")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID da conta", example = "1")
    private Long id;

    @Schema(description = "Nome do titular", example = "Maria Silva")
    @Column(name = "nome_titular")
    private String nomeTitular;

    @Schema(description = "CPF do titular", example = "123.456.789-00")
    private String cpf;

    @Schema(description = "Nome do banco", example = "Banco XPTO")
    private String banco;

    @Schema(description = "Número da agência", example = "0001")
    private String agencia;

    @Schema(description = "Número da conta", example = "12345-6")
    @Column(name = "numero_conta", unique = true, nullable = false)
    private String numeroConta;

    @Schema(description = "Senha da conta", example = "12345")
    @Column(nullable = false)
    private String senha;

    @Schema(description = "Saldo da conta", example = "1500.50")
    private BigDecimal saldo;

    @Schema(description = "Limite diário", example = "1000.00")
    @Column(name = "limite_diario")
    private BigDecimal limiteDiario;

    @Schema(description = "Data de cadastro", example = "2026-05-21")
    @Column(name = "data_cadastro")
    private LocalDate dataCadastro;

    @JsonIgnore
    @Schema(description = "Lista de chaves Pix da conta")
    @OneToMany(mappedBy = "conta")
    private List<ChavePix> chavesPix;

    @JsonIgnore
    @Schema(description = "Transferências realizadas pela conta")
    @OneToMany(mappedBy = "contaOrigem")
    private List<TransferenciaPix> transferenciasEnviadas;

    @JsonIgnore
    @Schema(description = "Favorecidos cadastrados pela conta")
    @OneToMany(mappedBy = "contaOrigem")
    private List<Favorecido> favorecidos;
}
