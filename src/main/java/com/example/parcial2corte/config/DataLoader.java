package com.example.parcial2corte.config;

import com.example.parcial2corte.domain.Rol;
import com.example.parcial2corte.domain.Usuario;
import com.example.parcial2corte.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Crea usuarios semilla al arrancar la app, así puedo probar inmediatamente con curl/Postman
// sin tener que registrar manualmente. Solo inserta si no existen (idempotente).
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seed("admin",    "admin123",    "admin@demo.com",    Rol.ADMIN);
        seed("vendedor", "vendedor123", "vendedor@demo.com", Rol.VENDEDOR);
        seed("lector",   "lector123",   "lector@demo.com",   Rol.LECTOR);
        log.info("Usuarios semilla disponibles -> admin/admin123, vendedor/vendedor123, lector/lector123");
    }

    private void seed(String username, String rawPassword, String email, Rol rol) {
        if (usuarioRepository.existsByUsername(username)) return;
        usuarioRepository.save(Usuario.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .email(email)
                .rol(rol)
                .build());
    }
}
