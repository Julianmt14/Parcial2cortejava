package com.example.parcial2corte.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Configuración central de Spring Security 6.
// @EnableMethodSecurity habilita anotaciones como @PreAuthorize en los controladores.
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Define la cadena de filtros de seguridad y las reglas de autorización por ruta.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // CSRF deshabilitado: API stateless con JWT, no usamos cookies de sesión.
                .csrf(AbstractHttpConfigurer::disable)
                // STATELESS: el servidor NO guarda sesión. Cada request se autentica por el token.
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos de autenticación (signup/login).
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        // Consola H2 para desarrollo.
                        .requestMatchers("/h2-console/**").permitAll()
                        // GETs: cualquier rol autenticado puede consultar.
                        .requestMatchers(HttpMethod.GET,    "/api/v1/**").hasAnyRole("ADMIN", "VENDEDOR", "LECTOR")
                        // Crear clientes/contactos/oportunidades: ADMIN o VENDEDOR.
                        .requestMatchers(HttpMethod.POST,   "/api/v1/clientes/**").hasAnyRole("ADMIN", "VENDEDOR")
                        // Actualizar cliente: solo ADMIN.
                        .requestMatchers(HttpMethod.PUT,    "/api/v1/clientes/**").hasRole("ADMIN")
                        // REGLA OBLIGATORIA DEL PARCIAL: solo ADMIN puede eliminar clientes.
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/clientes/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                // Permite que la consola H2 cargue dentro de un iframe.
                .headers(h -> h.frameOptions(f -> f.sameOrigin()))
                // Inserta nuestro filtro JWT antes del filtro estándar de login por formulario.
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    // Codificador de contraseñas: BCrypt es el estándar recomendado (añade salt automático).
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Expone el AuthenticationManager para que el AuthService pueda validar credenciales en el login.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}
