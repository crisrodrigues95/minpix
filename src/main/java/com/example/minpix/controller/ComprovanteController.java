package com.example.minpix.controller;


import com.example.minpix.models.Comprovante;
import com.example.minpix.services.ComprovanteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comprovantes")
@Tag(name = "Comprovantes", description = "Gerenciamento de comprovantes")
public class ComprovanteController {

    @Autowired
    private ComprovanteService comprovanteService;

    @Operation(summary = "Listar comprovantes")
    @ApiResponse(responseCode = "200", description = "Lista de comprovantes retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<Comprovante>> listar() {

        return ResponseEntity.ok(comprovanteService.listar());

    }

    @Operation(summary = "Buscar comprovante por ID")
    @ApiResponse(responseCode = "200", description = "Comprovante encontrado")
    @ApiResponse(responseCode = "404", description = "Comprovante não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<Comprovante> buscarPorId(@PathVariable Long id) {

        Comprovante comprovante = comprovanteService.buscarPorId(id);

        if (comprovante == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(comprovante);

    }

    @Operation(summary = "Cadastrar um novo comprovante")
    @ApiResponse(responseCode = "201", description = "Comprovante cadastrado com sucesso")
    @PostMapping
    public ResponseEntity<Comprovante> salvar(@RequestBody Comprovante comprovante) {

        Comprovante novoComprovante = comprovanteService.salvar(comprovante);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoComprovante);

    }

    @Operation(summary = "Atualizar um comprovante")
    @ApiResponse(responseCode = "200", description = "Comprovante atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Comprovante não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<Comprovante> atualizar(@PathVariable Long id,
                                                 @RequestBody Comprovante comprovante) {

        Comprovante comprovanteAtualizado = comprovanteService.atualizar(id, comprovante);

        if (comprovanteAtualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(comprovanteAtualizado);

    }

    @Operation(summary = "Excluir um comprovante")
    @ApiResponse(responseCode = "204", description = "Comprovante excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Comprovante não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        boolean excluiu = comprovanteService.excluir(id);

        if (!excluiu) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();

    }

}
