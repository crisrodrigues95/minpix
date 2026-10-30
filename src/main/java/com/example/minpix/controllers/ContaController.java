package com.example.minpix.controllers;

import com.example.minpix.dto.request.ContaRequest;
import com.example.minpix.dto.response.ContaResponse;
import com.example.minpix.exceptions.ResourceNotFoundException;
import com.example.minpix.models.Conta;
import com.example.minpix.services.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contas")
@Tag(name = "Contas", description = "Gerenciamento de contas")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @Operation(summary = "Listar contas")
    @ApiResponse(responseCode = "200", description = "Lista de contas retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<ContaResponse>> listar() {
        return ResponseEntity.ok(contaService.listarContas().stream()
                .map(ContaResponse::from)
                .toList());
    }

    @Operation(summary = "Buscar conta por ID")
    @ApiResponse(responseCode = "200", description = "Conta encontrada")
    @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<ContaResponse> buscarPorId(@PathVariable Long id) {
        Conta conta = contaService.buscarPorId(id);
        if (conta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ContaResponse.from(conta));
    }

    @Operation(summary = "Cadastrar uma nova conta")
    @ApiResponse(responseCode = "201", description = "Conta cadastrada com sucesso")
    @PostMapping
    public ResponseEntity<ContaResponse> salvar(@Valid @RequestBody ContaRequest request) {
        Conta criada = contaService.criarConta(toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ContaResponse.from(criada));
    }

    @Operation(summary = "Atualizar uma conta")
    @ApiResponse(responseCode = "200", description = "Conta atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<ContaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ContaRequest request
    ) {
        Conta atualizada = contaService.atualizar(id, toEntity(request));
        if (atualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ContaResponse.from(atualizada));
    }

    @Operation(summary = "Excluir uma conta")
    @ApiResponse(responseCode = "204", description = "Conta excluída com sucesso")
    @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!contaService.excluir(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    private static Conta toEntity(ContaRequest request) {
        Conta conta = new Conta();
        conta.setNomeTitular(request.nomeTitular());
        conta.setCpf(request.cpf());
        conta.setBanco(request.banco());
        conta.setAgencia(request.agencia());
        conta.setNumeroConta(request.numeroConta());
        conta.setSenha(request.senha());
        conta.setSaldo(request.saldo());
        conta.setLimiteDiario(request.limiteDiario());
        return conta;
    }
}