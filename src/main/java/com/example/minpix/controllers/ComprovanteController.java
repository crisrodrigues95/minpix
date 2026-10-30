package com.example.minpix.controllers;

import com.example.minpix.dto.request.ComprovanteRequest;
import com.example.minpix.dto.response.ComprovanteResponse;
import com.example.minpix.exceptions.ResourceNotFoundException;
import com.example.minpix.models.Comprovante;
import com.example.minpix.models.TransferenciaPix;
import com.example.minpix.services.ComprovanteService;
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
@RequestMapping("/comprovantes")
@Tag(name = "Comprovantes", description = "Gerenciamento de comprovantes")
public class ComprovanteController {

    private final ComprovanteService comprovanteService;
    private final TransferenciaPixService transferenciaPixService;

    public ComprovanteController(
            ComprovanteService comprovanteService,
            TransferenciaPixService transferenciaPixService
    ) {
        this.comprovanteService = comprovanteService;
        this.transferenciaPixService = transferenciaPixService;
    }

    @Operation(summary = "Listar comprovantes")
    @ApiResponse(responseCode = "200", description = "Lista de comprovantes retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<ComprovanteResponse>> listar() {
        return ResponseEntity.ok(comprovanteService.listar().stream()
                .map(ComprovanteResponse::from)
                .toList());
    }

    @Operation(summary = "Buscar comprovante por ID")
    @ApiResponse(responseCode = "200", description = "Comprovante encontrado")
    @ApiResponse(responseCode = "404", description = "Comprovante não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<ComprovanteResponse> buscarPorId(@PathVariable Long id) {
        Comprovante comprovante = comprovanteService.buscarPorId(id);
        if (comprovante == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ComprovanteResponse.from(comprovante));
    }

    @Operation(summary = "Cadastrar um novo comprovante")
    @ApiResponse(responseCode = "201", description = "Comprovante cadastrado com sucesso")
    @PostMapping
    public ResponseEntity<ComprovanteResponse> salvar(
            @Valid @RequestBody ComprovanteRequest request
    ) {
        Comprovante criado = comprovanteService.salvar(toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ComprovanteResponse.from(criado));
    }

    @Operation(summary = "Atualizar um comprovante")
    @ApiResponse(responseCode = "200", description = "Comprovante atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Comprovante não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<ComprovanteResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ComprovanteRequest request
    ) {
        if (comprovanteService.buscarPorId(id) == null) {
            return ResponseEntity.notFound().build();
        }
        Comprovante atualizado = comprovanteService.atualizar(id, toEntity(request));
        return ResponseEntity.ok(ComprovanteResponse.from(atualizado));
    }

    @Operation(summary = "Excluir um comprovante")
    @ApiResponse(responseCode = "204", description = "Comprovante excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Comprovante não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!comprovanteService.excluir(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    private Comprovante toEntity(ComprovanteRequest request) {
        TransferenciaPix transferencia = transferenciaPixService.buscarPorId(request.transferenciaId());
        if (transferencia == null) {
            throw new ResourceNotFoundException(
                    "Transferência não encontrada: " + request.transferenciaId()
            );
        }

        Comprovante comprovante = new Comprovante();
        comprovante.setTransferencia(transferencia);
        comprovante.setCodigoAutenticacao(request.codigoAutenticacao());
        comprovante.setDataGeracao(request.dataGeracao());
        comprovante.setStatus(request.status());
        return comprovante;
    }
}
