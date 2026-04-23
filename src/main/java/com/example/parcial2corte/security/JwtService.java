package com.example.parcial2corte.security;

import com.example.parcial2corte.domain.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

// Servicio responsable de generar y validar JWT. Aislado para respetar SRP.
@Service
public class JwtService {

    // Clave secreta simétrica (HS algorithm) derivada del secreto de application.properties.
    private final SecretKey key;
    private final long expMinutes;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.exp-minutes}") long expMinutes) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expMinutes = expMinutes;
    }

    // Crea un token firmado con subject=username y claim "rol" para auditoría/debug.
    public String generar(Usuario u) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(u.getUsername())
                .claim("rol", u.getRol().name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expMinutes, ChronoUnit.MINUTES)))
                .signWith(key)
                .compact();
    }

    // Valida firma + expiración y devuelve los claims. Lanza JwtException si algo falla.
    public Jws<Claims> parsear(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
    }

    public long getExpMinutes() {
        return expMinutes;
    }
}
