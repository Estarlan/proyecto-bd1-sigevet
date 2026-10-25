---
title: "Guion del Video de Demostración"
subtitle: "Guion del Video de Demostración (máximo 15 minutos)"
fase: "Fase 2"
version: "1.0"
---
<!-- Created: 18:15 19-Oct-2026 -->

# Propósito y requisitos

El enunciado pide un video de **máximo 15 minutos** que muestre el proceso de desarrollo, la manipulación de la base
de datos y la aplicación final, con la participación de **todos** los integrantes **con audio y con cámara**. Este
guion reparte el contenido en bloques cronometrados (total previsto: 13 min 30 s, con margen).

| Requisito | Cómo se cumple |
|---|---|
| Duración ≤ 15 min | Bloques que suman 13:30 |
| Todos los integrantes | Cada integrante presenta al menos dos bloques y aparece en la introducción y el cierre |
| Audio y cámara | Grabar con cámara activa en recuadro (por ejemplo, Zoom, Teams u OBS con cámara superpuesta) |
| Proceso de desarrollo | Bloques 2 y 3 (modelado y decisiones) |
| Manipulación de la base de datos | Bloques 4 a 6 (VM, scripts, consultas) |
| Resultado final | Bloques 7 y 8 (aplicación web y repositorio) |

Table: Requisitos del video

# Preparación (antes de grabar)

1. Encender la VM en el computador servidor (`limactl start sigevet`) y esperar a que arranquen MySQL y la aplicación web
   (`systemctl status mysqld sigevet-web`).
2. Abrir: una terminal SSH dentro de la VM, MySQL Workbench conectado a `127.0.0.1:3306` (usuario `vetdb`),
   la aplicación web en `http://127.0.0.1:5000`, el modelo `SIGEVET.mwb` en otra pestaña de Workbench y el
   repositorio en GitHub.
3. Configurar la grabación con pantalla compartida y cámara visible de quien habla.

# Guion

| # | Tiempo | Integrante | Qué se muestra | Qué se dice (idea principal) |
|---|---|---|---|---|
| 1 | 0:00–1:00 | Los tres | Cámaras de los tres; portada del proyecto | Presentación del grupo, la red de clínicas Huella Sana y el problema que resuelve SIGEVET |
| 2 | 1:00–3:00 | Sebastián Castellanos Parra | Diagrama Chen completo, EER y documento de supuestos | Entidades, relaciones 1:1, 1:N y N:M, especialización de EMPLEADO, supuestos clave |
| 3 | 3:00–4:30 | Sebastián Castellanos Parra | Workbench: modelo EER (Barker y Crow's Foot) y modelo relacional `SIGEVET.mwb`; diccionario de datos | Paso del MER al modelo relacional, normalización hasta 3FN (teléfonos, raza→especie, ciudad→departamento) |
| 4 | 4:30–6:00 | Juan Miguel Ramos | Terminal en el servidor: `limactl list`, `limactl shell sigevet`, `cat /etc/oracle-release`, `free -h`, `systemctl status mysqld` | Por qué Oracle Linux; recursos y red de la VM; simulación previa en Mac con Lima |
| 5 | 6:00–8:00 | Juan David Polo Herrera | Terminal: `bash vm/scripts/cargar_bd.sh --reiniciar` y la salida de la verificación | Orden de los scripts, 22 tablas, 113 restricciones, 1.091 registros, disparadores que rechazan datos inválidos |
| 6 | 8:00–10:00 | Juan David Polo Herrera | MySQL Workbench: 3 o 4 consultas del script de álgebra (una reunión, una división, una agregación) | Relación entre la expresión de álgebra relacional y el SQL; resultado de cada consulta |
| 7 | 10:00–12:30 | Juan Miguel Ramos | Aplicación web: inicio, clientes, historia clínica de una mascota, agenda, inventario, facturas y página de álgebra | La app corre dentro de la VM como servicio; es Java con Servlets y JSP sobre Tomcat y se conecta por JDBC al usuario de solo lectura VET_APP; todo el SQL está escrito a mano (sin ORM) |
| 8 | 12:30–13:30 | Los tres | GitHub: estructura del repositorio y commits de cada integrante | Trabajo colaborativo, lecciones aprendidas y cierre |

Table: Guion por bloques

# Consultas sugeridas para el bloque 6

- Ejercicio 22: sedes con su ciudad, departamento y director (reunión de cuatro tablas).
- Ejercicio 41: medicamentos disponibles en todas las sedes (división).
- Ejercicio 46: ingresos por sede (agregación con atributo calculado).
- Ejercicio 26: clientes sin mascotas (reunión externa).

# Recomendaciones de grabación

- Resolución 1920×1080, letra del terminal y del editor SQL en tamaño grande (16 pt o más).
- Ensayar una vez completo con cronómetro; si se pasa de 14 minutos, recortar el bloque 6 a dos consultas.
- Exportar en MP4 (H.264) y subir a YouTube como *no listado* o a OneDrive/Drive con enlace para el docente.
