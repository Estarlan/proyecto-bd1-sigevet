---
title: "Repositorio Git del Proyecto"
subtitle: "Repositorio en Git con la Aplicación Web y los Scripts de Base de Datos"
fase: "Fase 2"
version: "1.0"
---
<!-- Created: 18:15 19-Oct-2026 -->

# Introducción

El código y la documentación de SIGEVET se manejan con **Git** y se publican en **GitHub**. El repositorio tiene la
aplicación web, los scripts de base de datos, los scripts de la máquina virtual y los documentos entregados. Con él,
cualquier integrante (o el docente) puede montar el proyecto completo desde cero.

# Estructura

| Carpeta | Contenido |
|---|---|
| `database/` | Scripts SQL de creación de objetos, inserción de registros y varios, con su `README.md` |
| `app/` | Aplicación web en Java (Servlets + JSP) de solo lectura, con su `README.md`. Todo el SQL está escrito a mano y se ejecuta con JDBC, sin ORM |
| `vm/` | Scripts para instalar MySQL, cargar la base e instalar la app en la VM, la definición de la VM de simulación (Lima) y las evidencias |
| `documentos/` | Documentos entregados en la Fase 1, la Fase 2 y los transversales, los modelos de MySQL Workbench (`.mwb`) y los diagramas |

Table: Estructura del repositorio

# Lo que no se versiona

| Elemento | Motivo |
|---|---|
| `vm/claves.env` | Tiene las claves; cada integrante crea el suyo a partir de `vm/claves.ejemplo` |
| `vm/cache/` | Archivos temporales y descargas de la VM |
| `app/target/` | Archivos compilados por Maven (el `ROOT.war` se genera con `./mvnw package`) |

Table: Exclusiones del archivo .gitignore

# Flujo de trabajo del equipo

1. Cada integrante clona el repositorio y trabaja en su parte (modelado, scripts, infraestructura, aplicación).
2. Antes de subir cambios a la base de datos, prueba los scripts con `bash vm/scripts/cargar_bd.sh --reiniciar` en su
   VM.
3. Los mensajes de commit siguen el formato `tipo: descripción` (`feat`, `fix`, `docs`, `chore`).

| Integrante | Aportes principales en el repositorio |
|---|---|
| Juan David Polo Herrera | Especificación del modelo, scripts DDL y DML, carga en MySQL, verificación |
| Sebastián Castellanos Parra | Supuestos, modelos Chen/EER, modelos en MySQL Workbench, diccionario de datos |
| Juan Miguel Ramos | Máquina virtual, instalación del RDBMS, aplicación web |

Table: Aportes por integrante

# Evidencias

<!-- INCLUDE: _generado/evidencia_git.md -->
