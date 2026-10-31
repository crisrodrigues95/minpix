package com.example.minpix.models;

import com.example.minpix.enums.StatusPixEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transferencias_pix")
public class TransferenciaPix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID da transferência Pix", example = "1")
    private Long id;

    @Schema(description = "Conta de origem da transferência")
    @ManyToOne
    @JoinColumn(name = "conta_origem_id", nullable = false)
    private Conta contaOrigem;

    @JsonIgnore
    @Schema(description = "Favorecido da transferência")
    @ManyToOne
    @JoinColumn(name = "favorecido_id", nullable = false)
    private Favorecido favorecido;

    @Schema(description = "Valor da transferência", example = "250.00")
    private BigDecimal valor;

    @Schema(description = "Descrição da transferência", example = "Pagamento do almoço")
    private String descricao;

    @Schema(description = "Data da transferência", example = "2026-05-21")
    @Column(name = "data_transferencia")
    private LocalDate dataTransferencia;

    @Schema(description = "Data de agendamento da transferência", example = "2026-05-22")
    @Column(name = "data_agendamento")
    private LocalDate dataAgendamento;

    @Schema(description = "Status da transferência", example = "CONCLUIDA")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusPixEnum status;

    @JsonIgnore
    @Schema(description = "Comprovante da transferência")
    @OneToOne(mappedBy = "transferencia")
    private Comprovante comprovante;

}
