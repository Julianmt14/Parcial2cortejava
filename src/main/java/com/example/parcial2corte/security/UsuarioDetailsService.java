package com.example.parcial2corte.security;

import com.example.parcial2corte.domain.Usuario;
import com.example.parcial2corte.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Adapta nuestra entidad Usuario al modelo UserDetails que Spring Security entiende.
// Spring lo invoca cada vez que necesita validar credenciales o autorizaciones.
@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Usuario u = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        // Spring Security exige el prefijo "ROLE_" para que hasRole("ADMIN") funcione.
        return User.withUsername(u.getUsername())
                .password(u.getPassword())
                .authorities(new SimpleGrantedAuthority("ROLE_" + u.getRol().name()))
                .build();
    }
}
