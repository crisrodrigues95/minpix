package com.example.minpix.services;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET =
            "minha-chave-secreta-jwt-strategies-backend-descomplica";

    private final SecretKey key =
            Keys.hmacShaKeyFor(
                    SECRET.getBytes(StandardCharsets.UTF_8)
            );

    public String gerarToken(String numeroConta) {

        return Jwts.builder()
                .setSubject(numeroConta)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(
                                System.currentTimeMillis() + 86400000
                        )
                )
                .signWith(key)
                .compact();
    }
}
