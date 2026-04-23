# Reflexión sobre el uso de IA - Parcial 2

## 1. Apoyo de la IA
Utilicé la IA como herramienta de consulta rápida para:
- **Sintaxis de Spring Security 6**: Específicamente para la estructura del `SecurityFilterChain` y la nueva API de JJWT 0.12 (que cambió mucho respecto a versiones anteriores).
- **Estructura base**: Generación de esqueletos iniciales para controladores y mappers para ahorrar tiempo en código repetitivo.

## 2. Lo que tuve que corregir (Validación)
No fue "copiar y pegar", ya que surgieron errores que requerían conocimiento técnico:
- **build.gradle**: La IA sugirió dependencias inexistentes. Tuve que buscar manualmente los artefactos correctos en Maven Central y ajustar versiones compatibles.
- **Conflictos en MapStruct**: Tuve que definir mapeos explícitos `@Mapping` porque había ambigüedad entre campos de diferentes DTOs.
- **Configuración de red**: Ajusté el puerto a `8081` y gestioné procesos bloqueados en mi máquina local para que la app arrancara.

## 3. Riesgos de copiar sin entender
- **Seguridad**: Confiar ciegamente en una sugerencia de IA puede dejar huecos en la autorización de roles si no se valida en la capa de servicio (defensa en profundidad).
- **Obsolescencia**: La IA suele sugerir métodos deprecados de versiones antiguas de Spring Boot que no compilan en la versión 3.x.

## 4. Decisiones Propias
El diseño del **modelo de dominio**, la **lógica de negocio** (validación de NIT único, lógica de eliminación permitida solo para ADMIN) y la **matriz de permisos** fueron definidos por mí siguiendo los requerimientos del parcial. La IA fue un apoyo de sintaxis, pero la arquitectura y la integridad del sistema son responsabilidad mía.

