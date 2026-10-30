package com.example.minpix.controllers;



import com.example.minpix.models.ChavePix;
import com.example.minpix.services.ChavePixService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chaves-pix")
@Tag(name = "Chaves Pix", description = "Gerenciamento de chaves Pix")
public class ChavePixController {

    @Autowired
    private ChavePixService chavePixService;

    @Operation(summary = "Listar chaves Pix")
    @ApiResponse(responseCode = "200", description = "Lista de chaves Pix retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<ChavePix>> listar() {

        return ResponseEntity.ok(chavePixService.listar());

    }

    @Operation(summary = "Buscar chave Pix por ID")
    @ApiResponse(responseCode = "200", description = "Chave Pix encontrada")
    @ApiResponse(responseCode = "404", description = "Chave Pix não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<ChavePix> buscarPorId(@PathVariable Long id) {

        ChavePix chavePix = chavePixService.buscarPorId(id);

        if (chavePix == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(chavePix);

    }

    @Operation(summary = "Cadastrar uma nova chave Pix")
    @ApiResponse(responseCode = "201", description = "Chave Pix cadastrada com sucesso")
    @PostMapping
    public ResponseEntity<ChavePix> salvar(@RequestBody ChavePix chavePix) {

        ChavePix novaChavePix = chavePixService.salvar(chavePix);

        return ResponseEntity.status(HttpStatus.CREATED).body(novaChavePix);

    }

    @Operation(summary = "Atualizar uma chave Pix")
    @ApiResponse(responseCode = "200", description = "Chave Pix atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "Chave Pix não encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<ChavePix> atualizar(@PathVariable Long id,
                                              @RequestBody ChavePix chavePix) {

        ChavePix chavePixAtualizada = chavePixService.atualizar(id, chavePix);

        if (chavePixAtualizada == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(chavePixAtualizada);

    }

    @Operation(summary = "Excluir uma chave Pix")
    @ApiResponse(responseCode = "204", description = "Chave Pix excluída com sucesso")
    @ApiResponse(responseCode = "404", description = "Chave Pix não encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        boolean excluiu = chavePixService.excluir(id);

        if (!excluiu) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();

    }

}
