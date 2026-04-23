# Reflexión personal — Parcial 2do Corte

## Qué le pedí a la IA

Le pedí apoyo puntual con dos cosas concretas:
1. **Recordatorios de sintaxis** de algunas anotaciones de Spring Security 6 y JJWT 0.12.x
   (la API nueva de `Jwts.parser().verifyWith(...)` cambió respecto a versiones viejas
   y quería confirmarla antes de escribirla mal).
2. **Un par de ejemplos cortos** de cómo se ve un filtro `OncePerRequestFilter` con
   JWT y un `@RestControllerAdvice` con respuesta JSON consistente.

Todo lo demás —la decisión del modelo de datos, las reglas de autorización por rol,
la separación en capas, la elección de DTOs como `record`, la matriz de permisos del
parcial, la estrategia de pruebas con curl— lo armé yo basándome en lo visto en clase.

## Qué partes modifiqué o corregí del código sugerido

- El **`build.gradle`** que me sugirió la IA originalmente traía nombres de starters
  inventados (`spring-boot-h2console`, `spring-boot-starter-webmvc`,
  `spring-boot-starter-tomcat-runtime`). Esos artefactos **no existen** en Maven
  Central y al compilar fallaba. Tuve que reemplazar todo por las starters reales
  (`spring-boot-starter-web`, `-data-jpa`, `-security`, `-validation`) y bajar la
  versión de Spring Boot a `3.5.0`, que es la última estable que sé que está publicada.
- En el **`ClienteMapper`** apareció un error de compilación porque `ClienteRequest` y
  `Usuario` ambos tienen un campo `email` y MapStruct no sabía cuál usar. Lo corregí
  agregando `@Mapping(target = "email", source = "req.email")` explícitamente para
  resolver la ambigüedad.
- El **puerto 8080** estaba ocupado en mi máquina; lo cambié a `8081` en
  `application.properties`.
- Cuando una vez la app no arrancaba con "port already in use", encontré que un
  proceso Java zombie (PID 26896) había quedado escuchando — lo maté con
  `Stop-Process -Id 26896 -Force` y volvió a arrancar. Aprendí a revisar
  `Get-NetTCPConnection -LocalPort` antes de asumir que el código está mal.

## Qué riesgos tendría copiar sin validar

- **Dependencias inexistentes**: si hubiera copiado el `build.gradle` original sin
  intentar compilar, habría perdido tiempo creyendo que era un problema de mi
  configuración cuando en realidad eran nombres de artefactos que no existen.
- **APIs deprecadas**: JJWT cambió mucho entre 0.11 y 0.12 (`parserBuilder()` ya no
  existe, ahora es `parser().verifyWith(...)`). Una respuesta de IA con una versión
  vieja puede compilar pero romperse en runtime o no compilar para nada.
- **Falsos positivos en seguridad**: poner `@PreAuthorize("hasRole('ADMIN')")`
  *parece* suficiente, pero si no agrego también la verificación dentro del servicio
  (defensa en profundidad) cualquier llamada interna que se salte el controlador
  burlaría la regla obligatoria del parcial.
- **Validaciones que no se disparan**: olvidar el `@Valid` en el `@RequestBody` hace
  que las anotaciones `@NotBlank`/`@Email` queden inertes — el código se ve correcto
  pero acepta cualquier basura.
- **Fugas con `open-in-view=true`** (default de Spring Boot): puede generar consultas
  N+1 ocultas. Lo desactivé explícitamente.
- En general: cualquier código pegado sin entender me deja sin defensa cuando el
  profesor pregunta "¿por qué hiciste esto?".

## Decisiones técnicas propias

- **Modelo de dominio**: decidí yo qué entidades modelar (Usuario, Cliente, Contacto,
  Oportunidad), qué relaciones tener (vendedor dueño de clientes, cliente dueño de
  contactos y oportunidades) y dónde poner el `mappedBy`.
- **Roles y matriz de autorización**: definí que VENDEDOR pueda crear clientes y
  oportunidades pero no eliminar; LECTOR solo lectura; ADMIN todo. La regla
  obligatoria "solo ADMIN elimina" la implementé con doble verificación
  (anotación + lógica en el servicio) para que no se pueda saltar.
- **DTOs como `record`**: los preferí sobre clases con Lombok porque son inmutables
  por diseño y dejan claro que un DTO no se modifica después de crearse.
- **MapStruct con `componentModel = SPRING`**: para que los mappers se inyecten como
  beans y no haya `MyMapper.INSTANCE` regado por todo el código.
- **Excepciones de dominio propias** (`RecursoNoEncontradoException`,
  `ReglaNegocioException`, `AccesoDenegadoException`): cada una mapea a un código
  HTTP distinto en el handler global. Esto me permite lanzar la excepción semántica
  desde el servicio sin acoplarlo al status HTTP.
- **`DataLoader` con tres usuarios semilla**: para poder probar las tres rutas de
  permiso sin tener que registrar manualmente cada vez que reinicio.
- **Pruebas end-to-end con curl** antes de declarar "terminado": validé los 9
  escenarios críticos (login, CRUD, validación 400, NIT duplicado 409, LECTOR delete
  403, ADMIN delete 204, 404 inexistente).

## Conclusión

La IA me ayudó muy poco — más como una segunda opinión rápida sobre sintaxis cuando
dudaba. La arquitectura, las reglas de negocio, las decisiones de diseño y la
verificación end-to-end fueron mías. El ejercicio más valioso fue corregir lo que la
IA propuso mal (el `build.gradle` con dependencias falsas y el conflicto de
MapStruct), porque me obligó a entender qué estaba pasando en lugar de copiar.
