package com.example.minpix.controllers;



import com.example.minpix.models.Conta;
import com.example.minpix.services.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contas")
@Tag(name = "Contas", description = "Gerenciamento de contas")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @Operation(summary = "Listar contas")
    @ApiResponse(responseCode = "200", description = "Lista de contas retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<Conta>> listar() {

        return ResponseEntity.ok(contaService.listarContas());

    }

    @Operation(summary = "Buscar conta por ID")
    @ApiResponse(responseCode = "200", description = "Conta encontrada")
    @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<Conta> buscarPorId(@PathVariable Long id) {

        Conta conta = contaService.buscarPorId(id);

        if (conta == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(conta);

    }

    @Operation(summary = "Cadastrar uma nova conta")
    @ApiResponse(responseCode = "201", description = "Conta cadastrada com sucesso")
    @PostMapping
    public ResponseEntity<Conta> salvar(@RequestBody Conta conta) {

        Conta novaConta = contaService.criarConta(conta);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);

    }

    @Operation(summary = "Atualizar uma conta")
    @ApiResponse(responseCode = "200", description = "Conta atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<Conta> atualizar(@PathVariable Long id,
                                           @RequestBody Conta conta) {

        Conta contaAtualizada = contaService.atualizar(id, conta);

        if (contaAtualizada == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(contaAtualizada);

    }

    @Operation(summary = "Excluir uma conta")
    @ApiResponse(responseCode = "204", description = "Conta excluída com sucesso")
    @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        boolean excluiu = contaService.excluir(id);

        if (!excluiu) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();

    }

}
