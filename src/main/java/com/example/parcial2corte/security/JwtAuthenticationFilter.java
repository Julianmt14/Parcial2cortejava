package com.example.parcial2corte.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Filtro que corre UNA VEZ por request (OncePerRequestFilter).
// Lee el header Authorization: Bearer <token>, valida el JWT y pone al usuario en el SecurityContext.
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        // 1. Saco el header Authorization y verifico el prefijo "Bearer ".
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); // quito los 7 chars de "Bearer "
            try {
                // 2. Valido firma + expiración y extraigo los claims.
                Claims claims = jwtService.parsear(token).getPayload();
                String username = claims.getSubject();
                // 3. Si aún no hay autenticación en el contexto, cargo el usuario y la registro.
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails user = userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (JwtException ignored) {
                // Token inválido/expirado: sigue sin autenticar. Si la ruta requiere login,
                // Spring responderá 401/403 automáticamente.
            }
        }
        // 4. Continúa la cadena de filtros (siempre, haya o no token).
        filterChain.doFilter(request, response);
    }
}
