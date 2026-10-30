package com.example.minpix.controllers;



import com.example.minpix.controllers.request.LoginRequest;
import com.example.minpix.models.Conta;
import com.example.minpix.repositories.ContaRepository;
import com.example.minpix.services.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private final ContaRepository repository;

    @Autowired
    private final JwtService jwtService;

    public AuthController(ContaRepository repository, JwtService jwtService) {
        this.repository = repository;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest request) {

        Conta conta = repository.findByNumeroConta(request.getNumeroConta())
                .orElseThrow(() ->
                        new RuntimeException("Conta não encontrada"));

        if (!conta.getSenha().equals(request.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }

        return jwtService.gerarToken(conta.getNumeroConta());
    }
}
