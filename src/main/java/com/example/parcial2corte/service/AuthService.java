package com.example.parcial2corte.service;

import com.example.parcial2corte.domain.Rol;
import com.example.parcial2corte.domain.Usuario;
import com.example.parcial2corte.dto.LoginRequest;
import com.example.parcial2corte.dto.SignupRequest;
import com.example.parcial2corte.dto.TokenResponse;
import com.example.parcial2corte.dto.UsuarioResponse;
import com.example.parcial2corte.exception.ReglaNegocioException;
import com.example.parcial2corte.mapper.UsuarioMapper;
import com.example.parcial2corte.repository.UsuarioRepository;
import com.example.parcial2corte.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioMapper usuarioMapper;

    @Transactional
    public UsuarioResponse signup(SignupRequest req) {
        if (usuarioRepository.existsByUsername(req.username())) {
            throw new ReglaNegocioException("El username '" + req.username() + "' ya esta registrado");
        }
        Usuario u = Usuario.builder()
                .username(req.username())
                .password(passwordEncoder.encode(req.password()))
                .email(req.email())
                .rol(req.rol() != null ? req.rol() : Rol.LECTOR)
                .build();
        return usuarioMapper.toResponse(usuarioRepository.save(u));
    }

    public TokenResponse login(LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username(), req.password()));
        Usuario u = usuarioRepository.findByUsername(req.username()).orElseThrow();
        String token = jwtService.generar(u);
        return new TokenResponse(token, "Bearer", jwtService.getExpMinutes() * 60);
    }
}
