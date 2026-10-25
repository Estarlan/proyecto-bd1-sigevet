---
title: "Modelo Relacional y Normalización"
subtitle: "Modelo Relacional con notación Crow's Foot y Normalización (3FN)"
fase: "Fase 1"
version: "1.0"
---
<!-- Created: 22:47 09-Oct-2026 -->
<!-- Updated: 22:48 09-Oct-2026 -->
<!-- Updated: 14:05 10-Oct-2026 -->
<!-- Updated: 18:55 24-Oct-2026 -->
<!-- Updated: 18:55 24-Oct-2026 -->

# Introducción

El modelo relacional traduce el MER a **relaciones (tablas)** con sus atributos, llaves primarias, llaves foráneas y
restricciones. Este documento presenta las reglas de transformación aplicadas, el esquema resultante de 22 tablas, el
diagrama en notación Crow's Foot y el proceso de normalización que demuestra que el esquema está en Tercera Forma
Normal (3FN).

# Transformación del MER al modelo relacional

Se aplicó el algoritmo de transformación de Elmasri y Navathe:

| Paso | Regla | Aplicación en SIGEVET |
|---|---|---|
| 1 | Cada entidad fuerte es una tabla con sus atributos simples; el identificador es la PK | 15 tablas (DEPARTAMENTO, CIUDAD, SEDE, EMPLEADO, …, FACTURA) |
| 2 | La entidad débil es una tabla cuya PK es la PK del propietario más la llave parcial | DETALLE_FACTURA(id_factura, num_linea) |
| 3 | Relación 1:1: FK con UNIQUE en el lado de participación total | SEDE.id_director (DIRIGE), HISTORIA_CLINICA.id_mascota (TIENE), CONSULTA.id_cita (GENERA) |
| 4 | Relación 1:N: FK en la tabla del lado N | 18 relaciones, por ejemplo MASCOTA.id_cliente (PROPIETARIO_DE) |
| 5 | Relación N:M: nueva tabla con las dos PK como PK compuesta y los atributos de la relación | VETERINARIO_ESPECIALIDAD, PRESCRIPCION, INVENTARIO |
| 6 | Atributo multivaluado: nueva tabla con la PK de la entidad y el valor | TELEFONO_CLIENTE(id_cliente, telefono) |
| 7 | Atributo compuesto: se guardan sus componentes | nombres, apellidos, tipo_documento, num_documento |
| 8 | Especialización: opción 8A (tabla por superclase y por subclase) | VETERINARIO y AUXILIAR con PK = FK a EMPLEADO |
| — | Atributos derivados: no se almacenan | edad, antigüedad, subtotal y total se calculan en consultas o vistas |

Table: Reglas de transformación aplicadas

## Ejemplos gráficos de transformación

![CLIENTE y teléfonos multivaluados](diagramas/png/05_Transformacion_Multivaluados_CLIENTE_191116-24Oct2026.png){width=100%}

![Relación N:M PRESCRIBE](diagramas/png/06_Transformacion_NM_PRESCRIPCION_191116-24Oct2026.png){width=100%}

![Relación N:M ALMACENA](diagramas/png/07_Transformacion_NM_INVENTARIO_191116-24Oct2026.png){width=100%}

![Relación N:M CERTIFICADO_EN](diagramas/png/08_Transformacion_NM_ESPECIALIDAD_191116-24Oct2026.png){width=100%}

![Entidad débil DETALLE_FACTURA y llave parcial](diagramas/png/09_Entidad_Debil_DETALLE_FACTURA_191116-24Oct2026.png){width=100%}

**Relación circular SEDE–EMPLEADO.** EMPLEADO referencia a SEDE (LABORA_EN) y SEDE referencia a EMPLEADO (DIRIGE).
Para poder crear ambas tablas, la FK `FK_SEDE_DIRECTOR` se agrega con `ALTER TABLE` cuando EMPLEADO ya existe, y
`id_director` admite nulos para registrar la sede antes que su director (supuesto S-07).

# Esquema relacional

Notación: **TABLA**(llave primaria subrayada, columnas, *llaves foráneas en cursiva*).

<!-- INCLUDE: _generado/esquema_relacional.md -->

# Diagrama relacional (Draw.io)

El diagrama reproduce las 22 tablas, los tipos MySQL y las 30 llaves foráneas del DDL. # identifica la PK; * indica NOT NULL y o admite nulos; FK y UQn muestran llaves foráneas y grupos UNIQUE. Las relaciones identificadoras son continuas; las demás, discontinuas. La cardinalidad del padre es 1 u 0..1 según nulabilidad; la inversa es 0..N u 0..1 según UNIQUE. El DDL no impone por sí solo todas las participaciones mínimas del modelo conceptual.

![Modelo relacional completo de SIGEVET — Draw.io](diagramas/png/23_Relacional_Global_191116-24Oct2026.png){width=100%}

## Vistas por área

Cada vista incluye las entidades de su área y referencias externas sin atributos.

![Modelo relacional — Organizacion](diagramas/png/24_Relacional_Organizacion_191116-24Oct2026.png){width=100%}

![Modelo relacional — Clinica](diagramas/png/25_Relacional_Clinica_191116-24Oct2026.png){width=100%}

![Modelo relacional — Comercial](diagramas/png/26_Relacional_Comercial_191116-24Oct2026.png){width=100%}

Los `.drawio` y sus exportaciones son la versión vigente; el modelo `SIGEVET.mwb` se conserva como respaldo histórico.

# Integridad referencial

<!-- INCLUDE: _generado/lista_fks.md -->

<!-- INCLUDE: _generado/resumen_restricciones.md -->

# Normalización

![Proceso de normalización de 0FN a 3FN](diagramas/png/27_Normalizacion_191116-24Oct2026.png){width=100%}

## Conceptos

- **Dependencia funcional (DF):** $X \rightarrow Y$ si cada valor de X determina un único valor de Y.
- **1FN:** todos los atributos son atómicos; no hay grupos repetitivos ni atributos multivaluados.
- **2FN:** está en 1FN y ningún atributo no primo depende de **parte** de una llave candidata (sin dependencias parciales).
- **3FN:** está en 2FN y ningún atributo no primo depende **transitivamente** de una llave candidata
  ($X \rightarrow Y \rightarrow Z$ con Y no llave).

## Punto de partida: el formato actual de "Orden de atención y factura"

Hoy cada sede registra en un solo formato la atención y el cobro. Llevado a una tabla sin normalizar (0FN), un
registro contiene grupos repetitivos (varias líneas cobradas) y un atributo multivaluado (varios teléfonos):

| num_factura | fecha | sede / ciudad / depto | cliente (doc, nombre, ciudad, depto) | teléfonos | mascota / raza / especie | veterinario | ítems (código, nombre, cant., precio) |
|---|---|---|---|---|---|---|---|
| FV-000041 | 2026-03-12 | Usaquén / Bogotá D.C. / Bogotá D.C. | CC 52987654, Andrés Martínez, Bogotá D.C., Bogotá D.C. | 3157894561, 6017451230 | Toby / Labrador / Canino | Laura Gómez | (SRV-CON01, Consulta general, 1, 65000); (MED-0001, Amoxivet, 14, 1800) |
| FV-000042 | 2026-03-12 | Chía / Chía / Cundinamarca | CC 1032456789, Paula Rojas, Chía, Cundinamarca | 3204567812 | Luna / Siamés / Felino | Diego Díaz | (SRV-VAC02, Vacuna triple felina, 1, 70000) |

Table: Formato de orden de atención y factura sin normalizar (0FN)

**Anomalías:** si cambia el teléfono de un cliente hay que cambiarlo en todas sus facturas (actualización); no se
puede registrar un medicamento que aún no se ha vendido (inserción); al borrar la única factura de un cliente se pierde
el cliente (eliminación).

## Primera Forma Normal (1FN)

Se eliminan el grupo repetitivo y el atributo multivaluado: cada línea cobrada es una fila identificada por
(num_factura, num_linea) y los teléfonos pasan a una relación aparte.

- **ORDEN_1FN**([num_factura, num_linea]{.underline}, fecha, sede, ciudad_sede, depto_sede, doc_cliente,
  nombre_cliente, ciudad_cliente, depto_cliente, mascota, raza, especie, veterinario, codigo_item, nombre_item,
  cantidad, precio_unitario)
- **TELEFONO_1FN**([doc_cliente, telefono]{.underline})

Dependencias funcionales identificadas:

| # | Dependencia funcional | Tipo respecto a la PK (num_factura, num_linea) |
|---|---|---|
| DF1 | num_factura, num_linea → codigo_item, cantidad, precio_unitario | Completa |
| DF2 | num_factura → fecha, sede, doc_cliente, mascota, veterinario | **Parcial** (depende solo de num_factura) |
| DF3 | codigo_item → nombre_item, precio_vigente | **Parcial** a través de un atributo no primo |
| DF4 | sede → ciudad_sede → depto_sede | **Transitiva** |
| DF5 | doc_cliente → nombre_cliente, ciudad_cliente | **Transitiva** |
| DF6 | ciudad → departamento | **Transitiva** |
| DF7 | mascota → raza → especie | **Transitiva** |
| DF8 | mascota → doc_cliente (dueño) | **Transitiva** |

Table: Dependencias funcionales del formato en 1FN

## Segunda Forma Normal (2FN)

Se eliminan las dependencias parciales (DF2 y DF3) separando lo que depende solo de la factura y lo que depende solo
del ítem:

- **FACTURA_2FN**([num_factura]{.underline}, fecha, sede, ciudad_sede, depto_sede, doc_cliente, nombre_cliente,
  ciudad_cliente, depto_cliente, mascota, raza, especie, veterinario)
- **DETALLE_2FN**([num_factura, num_linea]{.underline}, *codigo_item*, cantidad, precio_unitario)
- **ITEM_2FN**([codigo_item]{.underline}, nombre_item, precio_vigente)

El `precio_unitario` permanece en el detalle porque es el precio **histórico** del día de la venta, distinto del
precio vigente del catálogo (supuesto S-37); por eso no es redundante.

## Tercera Forma Normal (3FN)

Se eliminan las dependencias transitivas (DF4 a DF8), creando una relación por cada determinante:

| Dependencia transitiva | Relación creada |
|---|---|
| sede → ciudad → departamento | SEDE(id_sede, …, *id_ciudad*), CIUDAD(id_ciudad, …, *id_departamento*), DEPARTAMENTO |
| doc_cliente → nombre, ciudad | CLIENTE(id_cliente, tipo_documento, num_documento, nombres, apellidos, …, *id_ciudad*) |
| mascota → raza → especie | MASCOTA(id_mascota, …, *id_raza*, *id_cliente*), RAZA(id_raza, …, *id_especie*), ESPECIE |
| factura → veterinario, mascota | La atención se separa del cobro: CITA y CONSULTA registran mascota y veterinario; FACTURA solo cliente, sede y empleado |
| codigo_item → servicio o medicamento | El ítem se especializa en SERVICIO y MEDICAMENTO; DETALLE_FACTURA referencia uno de los dos |

Table: Eliminación de dependencias transitivas

Al completar el análisis para todo el dominio (personal, inventario, fórmulas) se obtiene exactamente el esquema de
22 tablas presentado en la sección de esquema relacional.

## Verificación de 3FN del esquema final

| Tabla | Determinantes | Dependencias parciales | Dependencias transitivas | Forma |
|---|---|---|---|---|
| DEPARTAMENTO, CIUDAD, ESPECIE, RAZA, ESPECIALIDAD, SERVICIO, MEDICAMENTO | PK y llaves únicas (código, nombre) | No (PK simple) | No | 3FN |
| SEDE, EMPLEADO, CLIENTE, MASCOTA, HISTORIA_CLINICA | PK y llaves únicas (documento, email, microchip) | No (PK simple) | No: ciudad, raza y sede se guardan solo como FK | 3FN |
| VETERINARIO, AUXILIAR | id_empleado | No | No | 3FN |
| CITA | id_cita; (id_veterinario, fecha_hora) | No | No: los datos de mascota, sede y servicio están en sus tablas | 3FN |
| CONSULTA, FACTURA | PK y llaves únicas (id_cita, numero_factura) | No | No: el total es derivado y no se almacena | 3FN |
| TELEFONO_CLIENTE | (id_cliente, telefono) | No | No | 3FN |
| VETERINARIO_ESPECIALIDAD | (id_empleado, id_especialidad) | No: fecha_certificacion depende de ambas | No | 3FN |
| PRESCRIPCION | (id_consulta, id_medicamento) | No: dosis, frecuencia y cantidad dependen de la combinación | No | 3FN |
| INVENTARIO | (id_sede, id_medicamento) | No: el stock depende de la sede y del medicamento | No | 3FN |
| DETALLE_FACTURA | (id_factura, num_linea) | No: cantidad y precio dependen de la línea concreta | No | 3FN |

Table: Verificación de 3FN por tabla

En todas las tablas los únicos determinantes son llaves candidatas (PK o UNIQUE); por lo tanto el esquema cumple
además la Forma Normal de Boyce-Codd (FNBC).

# Script DDL

El modelo físico correspondiente se encuentra en `database/01_ddl/01_crear_tablas.sql` (tablas y 113 restricciones
con nombre) y `database/01_ddl/02_comentarios.sql` (comentarios de tablas y columnas). Ambos se generan desde la misma
especificación del modelo, lo que garantiza que el diccionario de datos, los diagramas y el script coinciden.
