package com.example.medcare.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.medcare.models.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Service
public class TokenService {
    
    @Value("${api.security.token.secret}")
    private String secret;

    // public String generateToken(User user){
    //     try {
    //         Algorithm algorithm = Algorithm.HMAC256(secret.getBytes(java.nio.charset.StandardCharsets.UTF_8));

    //         return JWT.create()
    //                 .withIssuer("auth")
    //                 .withSubject(user.getUsername())
    //                 .withExpiresAt(this.generateExpirationDate())
    //                 .sign(algorithm);
    //     } catch(JWTCreationException e){
    //         throw new RuntimeException("ERROR WHILE GENERATING TOKEN", e);
    //     }
    // }
    public String generateToken(User user){
        try {
            // Criar a chave HMAC usando a forma recomendada pelo JJWT
            java.security.Key signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
            return Jwts.builder()
                        .setIssuer("auth") // Nome do emissor (Issuer)
                        .setSubject(user.getUsername()) // O nome/ID do usuário
                        .setIssuedAt(Date.from(Instant.now())) // Tempo de emissão
                        .setExpiration(Date.from(this.generateExpirationDate())) // Tempo de expiração
                        .signWith(signingKey) // Assinar com a chave HMAC256
                        .compact();

        } catch(Exception e){
            // Use uma exceção de runtime apropriada
            throw new RuntimeException("ERROR WHILE GENERATING TOKEN with JJWT", e);
        }
    }

    public String validateToken(String token){
        try {
        // Usar JJWT para consistência com o gerador e o Gateway
        java.security.Key signingKey = Keys.hmacShaKeyFor(secret.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // Retorna o Subject (username)

    } catch (Exception e) {
        e.printStackTrace();
        return null; // Token inválido
    }

    }

    private Instant generateExpirationDate(){
        return Instant.now().plus(2, ChronoUnit.HOURS);
    }


}
