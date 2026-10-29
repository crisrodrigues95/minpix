package com.example.minpix.controller;


import com.example.minpix.models.TransferenciaPix;
import com.example.minpix.services.TransferenciaPixService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transferencias-pix")
@Tag(name = "Transferências Pix", description = "Gerenciamento de transferências Pix")
public class TransferenciaPixController {

    @Autowired
    private TransferenciaPixService transferenciaPixService;

    @Operation(summary = "Listar transferências Pix")
    @ApiResponse(responseCode = "200", description = "Lista de transferências retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<TransferenciaPix>> listar() {

        return ResponseEntity.ok(transferenciaPixService.listar());

    }

    @Operation(summary = "Buscar transferência Pix por ID")
    @ApiResponse(responseCode = "200", description = "Transferência encontrada")
    @ApiResponse(responseCode = "404", description = "Transferência não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<TransferenciaPix> buscarPorId(@PathVariable Long id) {

        TransferenciaPix transferenciaPix = transferenciaPixService.buscarPorId(id);

        if (transferenciaPix == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(transferenciaPix);

    }

    @Operation(summary = "Cadastrar uma nova transferência Pix")
    @ApiResponse(responseCode = "201", description = "Transferência cadastrada com sucesso")
    @PostMapping
    public ResponseEntity<TransferenciaPix> salvar(@RequestBody TransferenciaPix transferenciaPix) {

        TransferenciaPix novaTransferencia = transferenciaPixService.salvar(transferenciaPix);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaTransferencia);

    }

    @Operation(summary = "Atualizar uma transferência Pix")
    @ApiResponse(responseCode = "200", description = "Transferência atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Transferência não encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<TransferenciaPix> atualizar(@PathVariable Long id,
                                                      @RequestBody TransferenciaPix transferenciaPix) {

        TransferenciaPix transferenciaAtualizada = transferenciaPixService.atualizar(id, transferenciaPix);

        if (transferenciaAtualizada == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(transferenciaAtualizada);

    }

    @Operation(summary = "Excluir uma transferência Pix")
    @ApiResponse(responseCode = "204", description = "Transferência excluída com sucesso")
    @ApiResponse(responseCode = "404", description = "Transferência não encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        boolean excluiu = transferenciaPixService.excluir(id);

        if (!excluiu) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();

    }

}
