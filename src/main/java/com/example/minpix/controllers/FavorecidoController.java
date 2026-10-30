package com.example.minpix.controllers;

import com.example.minpix.dto.request.FavorecidoRequest;
import com.example.minpix.dto.response.FavorecidoResponse;
import com.example.minpix.exceptions.ResourceNotFoundException;
import com.example.minpix.models.Conta;
import com.example.minpix.models.Favorecido;
import com.example.minpix.services.ContaService;
import com.example.minpix.services.FavorecidoService;
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
@RequestMapping("/favorecidos")
@Tag(name = "Favorecidos", description = "Gerenciamento de favorecidos")
public class FavorecidoController {

    private final FavorecidoService favorecidoService;
    private final ContaService contaService;

    public FavorecidoController(FavorecidoService favorecidoService, ContaService contaService) {
        this.favorecidoService = favorecidoService;
        this.contaService = contaService;
    }

    @Operation(summary = "Listar favorecidos")
    @ApiResponse(responseCode = "200", description = "Lista de favorecidos retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<FavorecidoResponse>> listar() {
        return ResponseEntity.ok(favorecidoService.listar().stream()
                .map(FavorecidoResponse::from)
                .toList());
    }

    @Operation(summary = "Buscar favorecido por ID")
    @ApiResponse(responseCode = "200", description = "Favorecido encontrado")
    @ApiResponse(responseCode = "404", description = "Favorecido não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<FavorecidoResponse> buscarPorId(@PathVariable Long id) {
        Favorecido favorecido = favorecidoService.buscarPorId(id);
        if (favorecido == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(FavorecidoResponse.from(favorecido));
    }

    @Operation(summary = "Cadastrar um novo favorecido")
    @ApiResponse(responseCode = "201", description = "Favorecido cadastrado com sucesso")
    @PostMapping
    public ResponseEntity<FavorecidoResponse> salvar(@Valid @RequestBody FavorecidoRequest request) {
        Favorecido criado = favorecidoService.salvar(toEntity(request, LocalDate.now()));
        return ResponseEntity.status(HttpStatus.CREATED).body(FavorecidoResponse.from(criado));
    }

    @Operation(summary = "Atualizar um favorecido")
    @ApiResponse(responseCode = "200", description = "Favorecido atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Favorecido não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<FavorecidoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FavorecidoRequest request
    ) {
        Favorecido existente = favorecidoService.buscarPorId(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        Favorecido atualizado = favorecidoService.atualizar(
                id,
                toEntity(request, existente.getDataCadastro())
        );
        return ResponseEntity.ok(FavorecidoResponse.from(atualizado));
    }

    @Operation(summary = "Excluir um favorecido")
    @ApiResponse(responseCode = "204", description = "Favorecido excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Favorecido não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!favorecidoService.excluir(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    private Favorecido toEntity(FavorecidoRequest request, LocalDate dataCadastro) {
        Conta conta = contaService.buscarPorId(request.contaOrigemId());
        if (conta == null) {
            throw new ResourceNotFoundException("Conta não encontrada: " + request.contaOrigemId());
        }

        Favorecido favorecido = new Favorecido();
        favorecido.setNome(request.nome());
        favorecido.setBanco(request.banco());
        favorecido.setChavePix(request.chavePix());
        favorecido.setTipoChave(request.tipoChave());
        favorecido.setContaOrigem(conta);
        favorecido.setDataCadastro(dataCadastro);
        return favorecido;
    }
}
