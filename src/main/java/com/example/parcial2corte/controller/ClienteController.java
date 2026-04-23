package com.example.parcial2corte.controller;

import com.example.parcial2corte.dto.ClienteRequest;
import com.example.parcial2corte.dto.ClienteResponse;
import com.example.parcial2corte.dto.ContactoRequest;
import com.example.parcial2corte.dto.ContactoResponse;
import com.example.parcial2corte.dto.OportunidadRequest;
import com.example.parcial2corte.dto.OportunidadResponse;
import com.example.parcial2corte.service.ClienteService;
import com.example.parcial2corte.service.ContactoService;
import com.example.parcial2corte.service.OportunidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

// Controlador REST de clientes. SOLO se encarga de:
//   - recibir DTOs de entrada y devolver DTOs de salida,
//   - validar autorización por método (@PreAuthorize),
//   - delegar TODA la lógica al servicio.
// No accede a repositorios ni a entidades JPA directamente.
@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final ContactoService contactoService;
    private final OportunidadService oportunidadService;

    // POST /api/v1/clientes -> 201 Created + header Location apuntando al nuevo recurso.
    // @Valid dispara las validaciones declarativas del DTO (@NotBlank, @Email, etc.).
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody ClienteRequest req) {
        ClienteResponse out = clienteService.crear(req);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(out.id()).toUri();
        return ResponseEntity.created(uri).body(out);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> porId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
        return ResponseEntity.ok(clienteService.listar());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponse> actualizar(@PathVariable Long id,
                                                      @Valid @RequestBody ClienteRequest req) {
        return ResponseEntity.ok(clienteService.actualizar(id, req));
    }

    // DELETE -> 204 No Content. Doble protección: @PreAuthorize aquí + verificación en el service.
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, Authentication auth) {
        clienteService.eliminar(id, auth.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/contactos")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ResponseEntity<ContactoResponse> agregarContacto(@PathVariable Long id,
                                                            @Valid @RequestBody ContactoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(contactoService.asociarACliente(id, req));
    }

    @PostMapping("/{id}/oportunidades")
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public ResponseEntity<OportunidadResponse> crearOportunidad(@PathVariable Long id,
                                                                @Valid @RequestBody OportunidadRequest req,
                                                                Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(oportunidadService.crearParaCliente(id, req, auth.getName()));
    }
}
