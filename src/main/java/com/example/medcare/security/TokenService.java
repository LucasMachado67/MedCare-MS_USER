package com.example.medcare.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.medcare.models.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Serviço responsável pela criação e validação de tokens JWT utilizados
 * na autenticação do microservice ms_user.
 * Este serviço encapsula toda a lógica de geração, assinatura e validação
 * de tokens, utilizando a biblioteca JJWT (io.jsonwebtoken) na versão mais recente.
 * O token gerado segue as seguintes regras:
 * - Assinatura via HMAC-SHA (chave secreta Base64)
 * - Contém issuer, subject, issuedAt e expiration
 * - Expira após 2 horas
 * O método de validação retorna o username contido no token
 * (campo "sub" — subject), caso o token seja válido e assinado corretamente.
 */
@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user){
        try {
            // Criar a chave HMAC usando a forma recomendada pelo JJWT
            // Altere esta linha no MS-User (TokenService.java)
            java.security.Key signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
            //passando a role e isFirstPassword para os outros sistemas através do 'token' gerado
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", user.getRole().name());
            claims.put("mustChangePassword", user.IsFirstPassword());
            claims.put("tenantId", user.getTenantId());
            return Jwts.builder()
                        .setIssuer("auth") // Nome do emissor (Issuer)
                        .setClaims(claims)
                        .setSubject(user.getUsername()) // O nome'ID' do usuário
                        .setIssuedAt(Date.from(Instant.now())) // Tempo de emissão
                        .setExpiration(Date.from(this.generateExpirationDate())) // Tempo de expiração
                        .signWith(signingKey) // Assinar com a chave 'HMAC256'
                        .compact();

        } catch(Exception e){
            // Use uma exceção de runtime apropriada
            throw new RuntimeException("ERROR WHILE GENERATING TOKEN with JJWT", e);
        }
    }

    public String validateToken(String token){
        try {
        // Usar JJWT para consistência com o gerador e o Gateway
        java.security.Key signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // Retorna o Subject (username)

        } catch (Exception e) {
                System.out.println("Authentication token invalid: " + e.getMessage());
            return null; // Token inválido
        }

    }

    private Instant generateExpirationDate(){
        return Instant.now().plus(2, ChronoUnit.HOURS);
    }


}
