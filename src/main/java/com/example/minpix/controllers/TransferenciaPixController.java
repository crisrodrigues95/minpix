package com.example.minpix.controllers;

import com.example.minpix.dto.request.TransferenciaPixRequest;
import com.example.minpix.dto.response.TransferenciaPixResponse;
import com.example.minpix.exceptions.ResourceNotFoundException;
import com.example.minpix.models.Conta;
import com.example.minpix.models.Favorecido;
import com.example.minpix.models.TransferenciaPix;
import com.example.minpix.services.ContaService;
import com.example.minpix.services.FavorecidoService;
import com.example.minpix.services.TransferenciaPixService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transferencias-pix")
@Tag(name = "Transferências Pix", description = "Gerenciamento de transferências Pix")
public class TransferenciaPixController {

    private final TransferenciaPixService transferenciaPixService;
    private final ContaService contaService;
    private final FavorecidoService favorecidoService;

    public TransferenciaPixController(
            TransferenciaPixService transferenciaPixService,
            ContaService contaService,
            FavorecidoService favorecidoService
    ) {
        this.transferenciaPixService = transferenciaPixService;
        this.contaService = contaService;
        this.favorecidoService = favorecidoService;
    }

    @Operation(summary = "Listar transferências Pix")
    @ApiResponse(responseCode = "200", description = "Lista de transferências retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<TransferenciaPixResponse>> listar() {
        return ResponseEntity.ok(transferenciaPixService.listar().stream()
                .map(TransferenciaPixResponse::from)
                .toList());
    }

    @Operation(summary = "Buscar transferência Pix por ID")
    @ApiResponse(responseCode = "200", description = "Transferência encontrada")
    @ApiResponse(responseCode = "404", description = "Transferência não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<TransferenciaPixResponse> buscarPorId(@PathVariable Long id) {
        TransferenciaPix transferencia = transferenciaPixService.buscarPorId(id);
        if (transferencia == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(TransferenciaPixResponse.from(transferencia));
    }

    @Operation(summary = "Cadastrar uma nova transferência Pix")
    @ApiResponse(responseCode = "201", description = "Transferência cadastrada com sucesso")
    @PostMapping
    public ResponseEntity<TransferenciaPixResponse> salvar(
            @Valid @RequestBody TransferenciaPixRequest request
    ) {
        TransferenciaPix criada = transferenciaPixService.salvar(toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(TransferenciaPixResponse.from(criada));
    }

    @Operation(summary = "Atualizar uma transferência Pix")
    @ApiResponse(responseCode = "200", description = "Transferência atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Transferência não encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<TransferenciaPixResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TransferenciaPixRequest request
    ) {
        if (transferenciaPixService.buscarPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        TransferenciaPix atualizada = transferenciaPixService.atualizar(id, toEntity(request));
        return ResponseEntity.ok(TransferenciaPixResponse.from(atualizada));
    }

    @Operation(summary = "Excluir uma transferência Pix")
    @ApiResponse(responseCode = "204", description = "Transferência excluída com sucesso")
    @ApiResponse(responseCode = "404", description = "Transferência não encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!transferenciaPixService.excluir(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    private TransferenciaPix toEntity(TransferenciaPixRequest request) {
        Conta conta = contaService.buscarPorId(request.contaOrigemId());
        if (conta == null) {
            throw new ResourceNotFoundException("Conta não encontrada: " + request.contaOrigemId());
        }
        Favorecido favorecido = favorecidoService.buscarPorId(request.favorecidoId());
        if (favorecido == null) {
            throw new ResourceNotFoundException("Favorecido não encontrado: " + request.favorecidoId());
        }

        TransferenciaPix transferencia = new TransferenciaPix();
        transferencia.setContaOrigem(conta);
        transferencia.setFavorecido(favorecido);
        transferencia.setValor(request.valor());
        transferencia.setDescricao(request.descricao());
        transferencia.setDataTransferencia(request.dataTransferencia());
        transferencia.setDataAgendamento(request.dataAgendamento());
        transferencia.setStatus(request.status());
        return transferencia;
    }
}
