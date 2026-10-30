package com.example.minpix.controllers;

import com.example.minpix.dto.request.ChavePixRequest;
import com.example.minpix.dto.response.ChavePixResponse;
import com.example.minpix.exceptions.ResourceNotFoundException;
import com.example.minpix.models.ChavePix;
import com.example.minpix.models.Conta;
import com.example.minpix.services.ChavePixService;
import com.example.minpix.services.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/chaves-pix")
@Tag(name = "Chaves Pix", description = "Gerenciamento de chaves Pix")
public class ChavePixController {

    private final ChavePixService chavePixService;
    private final ContaService contaService;

    public ChavePixController(ChavePixService chavePixService, ContaService contaService) {
        this.chavePixService = chavePixService;
        this.contaService = contaService;
    }

    @Operation(summary = "Listar chaves Pix")
    @ApiResponse(responseCode = "200", description = "Lista de chaves Pix retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<ChavePixResponse>> listar() {
        return ResponseEntity.ok(chavePixService.listar().stream()
                .map(ChavePixResponse::from)
                .toList());
    }

    @Operation(summary = "Buscar chave Pix por ID")
    @ApiResponse(responseCode = "200", description = "Chave Pix encontrada")
    @ApiResponse(responseCode = "404", description = "Chave Pix não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<ChavePixResponse> buscarPorId(@PathVariable Long id) {
        ChavePix chave = chavePixService.buscarPorId(id);
        if (chave == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ChavePixResponse.from(chave));
    }

    @Operation(summary = "Cadastrar uma nova chave Pix")
    @ApiResponse(responseCode = "201", description = "Chave Pix cadastrada com sucesso")
    @PostMapping
    public ResponseEntity<ChavePixResponse> salvar(@Valid @RequestBody ChavePixRequest request) {
        ChavePix criada = chavePixService.salvar(toEntity(request, LocalDate.now()));
        return ResponseEntity.status(HttpStatus.CREATED).body(ChavePixResponse.from(criada));
    }

    @Operation(summary = "Atualizar uma chave Pix")
    @ApiResponse(responseCode = "200", description = "Chave Pix atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Chave Pix não encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<ChavePixResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ChavePixRequest request
    ) {
        ChavePix existente = chavePixService.buscarPorId(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        ChavePix atualizada = chavePixService.atualizar(id, toEntity(request, existente.getDataCadastro()));
        return ResponseEntity.ok(ChavePixResponse.from(atualizada));
    }

    @Operation(summary = "Excluir uma chave Pix")
    @ApiResponse(responseCode = "204", description = "Chave Pix excluída com sucesso")
    @ApiResponse(responseCode = "404", description = "Chave Pix não encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!chavePixService.excluir(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    private ChavePix toEntity(ChavePixRequest request, LocalDate dataCadastro) {
        Conta conta = contaService.buscarPorId(request.contaId());
        if (conta == null) {
            throw new ResourceNotFoundException("Conta não encontrada: " + request.contaId());
        }

        ChavePix chave = new ChavePix();
        chave.setTipo(request.tipo());
        chave.setValorChave(request.valorChave());
        chave.setConta(conta);
        chave.setAtiva(request.ativa());
        chave.setDataCadastro(dataCadastro);
        return chave;
    }
}
