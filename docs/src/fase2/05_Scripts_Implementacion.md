---
title: "Scripts de Implementación"
subtitle: "Scripts de Implementación del Modelo Lógico al Modelo Físico"
fase: "Fase 2"
version: "1.0"
---
<!-- Created: 18:15 19-Oct-2026 -->
<!-- Updated: 18:32 19-Oct-2026 -->

# Introducción

Este documento acompaña los scripts SQL que transforman el modelo lógico de SIGEVET en el modelo físico de **MySQL 8.4**.
Los scripts están en la carpeta `database/` del repositorio, junto con su `README.md`. Se agrupan en los tres tipos
que pide el enunciado: **creación de objetos**, **inserción de registros** y **scripts varios**. Se pueden ejecutar con el
cliente `mysql` dentro de la VM (así lo hace `vm/scripts/cargar_bd.sh`) o abriéndolos uno por uno en MySQL Workbench.

# Scripts de creación de objetos

| Script | Objetos | Cantidad |
|---|---|---|
| `01_ddl/00_crear_usuarios.sql` | Base de datos, usuarios y rol (se ejecuta como root) | Base `sigevet`, 2 usuarios (`vetdb`, `vet_app`) y 1 rol (`rol_sigevet_lectura`) |
| `01_ddl/01_crear_tablas.sql` | Tablas y restricciones con nombre, con el comentario de cada tabla y columna | 22 tablas; 22 PK, 30 FK, 25 UNIQUE, 36 CHECK; 138 columnas comentadas |
| `01_ddl/02_indices.sql` | Índices de búsqueda | 5 índices (InnoDB crea solo los de PK, UNIQUE y FK) |
| `01_ddl/03_vistas.sql` | Vistas | 6 vistas |
| `01_ddl/04_programas.sql` | Funciones, procedimientos y disparadores | 2 funciones, 3 procedimientos y 9 disparadores |

Table: Scripts de creación de objetos

## Decisiones del modelo físico

- **Motor InnoDB y utf8mb4.** InnoDB es el motor de MySQL que maneja llaves foráneas y transacciones. La base usa
  `utf8mb4` para guardar bien las tildes y la ñ.
- **Restricciones con nombre.** Todas siguen la convención `PK_`, `FK_`, `UQ_` y `CK_` + tabla + columna. Así, un error
  como `Check constraint 'CK_MASCOTA_SEXO' is violated` dice exactamente qué regla se rompió.
- **Relación circular SEDE–EMPLEADO.** `FK_SEDE_DIRECTOR` se crea con `ALTER TABLE` después de las dos tablas.
- **AUTO_INCREMENT.** Las tablas con llave numérica propia generan sus identificadores con `AUTO_INCREMENT`. Como los
  datos de prueba se insertan con su id, MySQL continúa después del último identificador cargado.
- **Comentarios en la misma tabla.** El diccionario de datos queda dentro de la base con `COMMENT` en cada tabla y en
  cada columna, y se ve en la pestaña *Columns* de Workbench.
- **Vistas para atributos derivados.** El total de la factura (`V_FACTURA_TOTAL`) y la edad de la mascota
  (`V_MASCOTA_DETALLE`) se calculan y no se guardan, para mantener la Tercera Forma Normal.
- **Disparadores para reglas que no caben en un CHECK.** Un CHECK de MySQL no puede consultar otra tabla, así que las
  reglas RN-08 y RN-09 del documento de supuestos se hicieron con disparadores. En MySQL un disparador cubre un solo
  evento (INSERT o UPDATE), por eso cada regla vive en un procedimiento que se llama desde los dos disparadores. Los
  errores se lanzan con `SIGNAL SQLSTATE '45000'` y un código propio (20001 a 20006):

| Disparador | Procedimiento / regla |
|---|---|
| `TRG_VETERINARIO_TIPO_INS` y `_UPD` | `PR_VALIDAR_TIPO_EMPLEADO`: solo un empleado con `tipo_empleado = 'VET'` puede ser veterinario (RN-09, error 20001) |
| `TRG_AUXILIAR_TIPO_INS` y `_UPD` | `PR_VALIDAR_TIPO_EMPLEADO`: solo un empleado con `tipo_empleado = 'AUX'` puede ser auxiliar (RN-09, error 20002) |
| `TRG_CITA_VETERINARIO_SEDE_INS` y `TRG_CITA_UPD` | `PR_VALIDAR_SEDE_VETERINARIO`: el veterinario de la cita debe trabajar en la sede de la cita (RN-08, error 20003) |
| `TRG_CONSULTA_CITA_ATENDIDA_INS` y `_UPD` | `PR_VALIDAR_CITA_ATENDIDA`: solo una cita ATENDIDA puede generar consulta (supuesto S-28, error 20004) |
| `TRG_EMPLEADO_TIPO` | No se puede cambiar el tipo de un empleado que ya tiene fila de subtipo (error 20005) |
| `TRG_CITA_UPD` | Además, una cita que ya generó consulta no puede dejar de estar ATENDIDA (error 20006) |

Table: Disparadores de reglas de negocio

# Scripts de inserción de registros

La carpeta `02_dml/` contiene 23 archivos (uno por tabla, más la asignación de directores) con **1.091 registros** de
prueba ficticios. El orden respeta las dependencias de las llaves foráneas:

```text
01_departamento → 02_ciudad → 03_sede (sin director) → 04_empleado → 05_sede_director (UPDATE)
→ 06_veterinario → 07_auxiliar → 08_especialidad → 09_veterinario_especialidad → 10_cliente
→ 11_telefono_cliente → 12_especie → 13_raza → 14_mascota → 15_historia_clinica → 16_servicio
→ 17_cita → 18_consulta → 19_medicamento → 20_prescripcion → 21_inventario → 22_factura → 23_detalle_factura
```

Las fechas se escriben en formato ISO (`'2025-10-08 16:13:00'`), que MySQL entiende sin importar la configuración
regional. Cada archivo empieza con `USE sigevet;` y agrupa sus INSERT en una transacción (`START TRANSACTION` …
`COMMIT`).

# Scripts varios

| Script | Propósito |
|---|---|
| `03_varios/01_consultas_algebra_relacional.sql` | Las 55 consultas SQL equivalentes a los ejercicios de álgebra relacional de la Fase 1 |
| `03_varios/02_verificacion.sql` | Objetos, restricciones y registros por tabla, y prueba de que las reglas de negocio rechazan datos inválidos |
| `03_varios/03_reiniciar_base.sql` | Borra la base `sigevet` y la crea vacía para reinstalar (los usuarios se conservan) |
| `instalar_esquema.sql` | Script maestro: ejecuta todo en orden con `SOURCE` y termina con la verificación |
| `vm/scripts/cargar_bd.sh` | Crea la base y los usuarios (si no existen) y ejecuta el script maestro dentro de la VM |

Table: Scripts varios

# Ejecución y resultados

<!-- INCLUDE: _generado/evidencia_carga_bd.md -->

# Ejecución de las consultas de álgebra relacional en MySQL

<!-- INCLUDE: _generado/evidencia_algebra_mysql.md -->

# README

El archivo `database/README.md` del repositorio describe la estructura de carpetas, el orden de ejecución, cómo
ejecutar los scripts dentro de la VM con el cliente `mysql` o desde MySQL Workbench, el manejo de credenciales y el
origen de los datos de prueba.
