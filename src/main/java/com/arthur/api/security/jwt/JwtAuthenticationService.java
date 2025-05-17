package com.arthur.api.security.jwt;


import com.arthur.api.domain.Usuario;
import com.arthur.api.security.dto.TokenInfo;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JwtAuthenticationService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.ttl}")
    private Long jwtTimeToLive;

    @Value("${server.timezone.id}")
    private String zoneSetId;

    public TokenInfo generateToken(Usuario user){
        try {
            Instant expirationDate = getExpirationDate();
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(expirationDate)
                    .sign(getAlgorithm());
            return new TokenInfo(token, expirationDate);
        } catch (JWTCreationException e){
            throw new RuntimeException("Erro ao gerar token jwt", e);
        }
    }

    public String validateToken(String token){
        try {
            return JWT.require(getAlgorithm())
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e){
            return "";
        }
    }

    private Instant getExpirationDate(){
        return LocalDateTime.now().plusSeconds(jwtTimeToLive).toInstant(ZoneOffset.of(zoneSetId));
    }

    private Algorithm getAlgorithm(){
        return Algorithm.HMAC512(jwtSecret);
    }

}
