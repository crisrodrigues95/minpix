package com.example.minpix.controllers;


import com.example.minpix.models.Favorecido;
import com.example.minpix.services.FavorecidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favorecidos")
@Tag(name = "Favorecidos", description = "Gerenciamento de favorecidos")
public class FavorecidoController {

    @Autowired
    private FavorecidoService favorecidoService;

    @Operation(summary = "Listar favorecidos")
    @ApiResponse(responseCode = "200", description = "Lista de favorecidos retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<Favorecido>> listar() {

        return ResponseEntity.ok(favorecidoService.listar());

    }

    @Operation(summary = "Buscar favorecido por ID")
    @ApiResponse(responseCode = "200", description = "Favorecido encontrado")
    @ApiResponse(responseCode = "404", description = "Favorecido não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<Favorecido> buscarPorId(@PathVariable Long id) {

        Favorecido favorecido = favorecidoService.buscarPorId(id);

        if (favorecido == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(favorecido);

    }

    @Operation(summary = "Cadastrar um novo favorecido")
    @ApiResponse(responseCode = "201", description = "Favorecido cadastrado com sucesso")
    @PostMapping
    public ResponseEntity<Favorecido> salvar(@RequestBody Favorecido favorecido) {

        Favorecido novoFavorecido = favorecidoService.salvar(favorecido);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoFavorecido);

    }

    @Operation(summary = "Atualizar um favorecido")
    @ApiResponse(responseCode = "200", description = "Favorecido atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Favorecido não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<Favorecido> atualizar(@PathVariable Long id,
                                                @RequestBody Favorecido favorecido) {

        Favorecido favorecidoAtualizado = favorecidoService.atualizar(id, favorecido);

        if (favorecidoAtualizado == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(favorecidoAtualizado);

    }

    @Operation(summary = "Excluir um favorecido")
    @ApiResponse(responseCode = "204", description = "Favorecido excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Favorecido não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {

        boolean excluiu = favorecidoService.excluir(id);

        if (!excluiu) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();

    }

}
