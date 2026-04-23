# PARCIAL 2do CORTE - CRM Básico

## Requisitos
- Java 21 o superior.

## Instrucciones de ejecución
El proyecto utiliza el Gradle Wrapper incluido, por lo que no es necesario instalar Gradle manualmente.

1. Abrir una terminal en la raíz del proyecto.
2. Ejecutar el comando según su sistema operativo:

**Windows:**
```powershell
.\gradlew.bat bootRun
```

**Linux / macOS:**
```bash
./gradlew bootRun
```

La aplicación iniciará en: `http://localhost:8081`

## Usuarios de prueba
Al iniciar, el sistema carga automáticamente los siguientes perfiles:

| Usuario | Contraseña | Rol |
| :--- | :--- | :--- |
| admin | admin123 | ADMIN |
| vendedor | vendedor123 | VENDEDOR |
| lector | lector123 | LECTOR |

## Base de Datos (H2 Console)
- URL: `http://localhost:8081/h2-console`
- JDBC URL: `jdbc:h2:mem:parcial2corte`
- Usuario: `sa` (sin contraseña)
