package com.example.parcial2corte.exception;

import com.example.parcial2corte.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

// Manejador GLOBAL de excepciones. @RestControllerAdvice intercepta cualquier excepción
// lanzada por los controladores y la traduce a una respuesta JSON consistente (ApiError).
// Así el código de los controladores queda limpio (no hay try/catch repartidos).
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ApiError> notFound(RecursoNoEncontradoException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ApiError> negocio(ReglaNegocioException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(AccesoDenegadoException.class)
    public ResponseEntity<ApiError> accesoDenegado(AccesoDenegadoException ex, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, ex.getMessage(), req, List.of());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> springDenied(AccessDeniedException ex, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, "No tiene permisos para esta operacion", req, List.of());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> badCredentials(BadCredentialsException ex, HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, "Credenciales invalidas", req, List.of());
    }

    // Falla de validación de @Valid: armo un detalle por campo (ej. "email: must be a well-formed email").
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> validacion(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<String> det = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .toList();
        return build(HttpStatus.BAD_REQUEST, "Datos invalidos", req, det);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> otros(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), req, List.of());
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String msg, HttpServletRequest req, List<String> details) {
        ApiError body = new ApiError(msg, status.value(), req.getRequestURI(), Instant.now(), details);
        return ResponseEntity.status(status).body(body);
    }
}
