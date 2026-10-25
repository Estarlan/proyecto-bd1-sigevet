---
title: "Modelo Entidad-Relación — Notación Barker y Crow's Foot"
subtitle: "Modelo Entidad Relación con notación Barker / Crow's Foot"
fase: "Fase 1"
version: "1.0"
---
<!-- Created: 22:45 09-Oct-2026 -->
<!-- Updated: 14:05 10-Oct-2026 -->
<!-- Updated: 18:55 24-Oct-2026 -->
<!-- Updated: 18:55 24-Oct-2026 -->
<!-- Updated: 18:55 24-Oct-2026 -->

# Introducción

Este documento presenta los modelos lógicos **Barker** y **Crow's Foot**, elaborados en **Draw.io** a partir del esquema de 22 tablas y 30 llaves foráneas. Las hojas lógicas omiten los tipos SQL; la vista física del documento 6 los incluye. Las relaciones N:M originan VETERINARIO_ESPECIALIDAD, PRESCRIPCION e INVENTARIO; el teléfono multivaluado origina TELEFONO_CLIENTE. Los archivos históricos de Workbench se conservan como respaldo; la versión vigente es la de Draw.io.

# Notación Barker

| Elemento | Representación |
|---|---|
| Entidad | Caja de esquinas redondeadas con el nombre en la parte superior |
| `#` | Atributo que forma parte del identificador único (UID) |
| `*` | Atributo obligatorio (NOT NULL) |
| `o` | Atributo opcional |
| `(FK)` | Atributo que viene de otra entidad por una relación |
| Línea continua (mitad de la relación) | La entidad de ese extremo **debe** participar ("must be") |
| Línea discontinua (mitad de la relación) | La entidad de ese extremo **puede** participar ("may be") |
| Pata de gallina en un extremo | Ese extremo es "uno o más" (muchos) |
| Extremo sin pata de gallina | Ese extremo es "uno y solo uno" |
| `#` y `[FK]` en un atributo | La FK forma parte del identificador; el UID se indica explícitamente en los atributos |
| Relación 1:1 identificadora con EMPLEADO | Subtipos VETERINARIO y AUXILIAR (especialización llevada a tablas) |

Table: Convenciones de la notación Barker

Cada relación se lee en ambos sentidos. CIUDAD debe pertenecer a un DEPARTAMENTO (mitad junto a CIUDAD continua; extremo DEPARTAMENTO sin pata). Un DEPARTAMENTO puede tener ciudades (mitad junto a DEPARTAMENTO discontinua; extremo CIUDAD con pata). La obligatoriedad de cada mitad corresponde a la entidad junto a esa mitad.

![Modelo de SIGEVET en notación Barker (Draw.io)](diagramas/png/15_Barker_Global_191116-24Oct2026.png){width=100%}

## Observaciones del modelo Barker

- **Subtipos.** VETERINARIO y AUXILIAR aparecen como entidades unidas a EMPLEADO por relaciones 1:1
  identificadoras, que representa la especialización llevada a tablas (opción 8A del documento de
  modelo extendido). Las relaciones ATIENDE (con CITA), CERTIFICADO_EN y ASISTE se conectan al subtipo.
- **Relaciones identificadoras.** DETALLE_FACTURA (entidad débil) y las entidades de intersección marcan con `#` y `[FK]`
  las columnas del UID heredadas de la entidad padre.
- **Relaciones 1:1.** DIRIGE, TIENE y GENERA no tienen pata de gallina en ningún extremo.
- **Relación circular.** SEDE y EMPLEADO se relacionan dos veces: LABORA_EN (1:N) y DIRIGE (1:1).

# Notación Crow's Foot (pata de gallina)

| Símbolo en el extremo | Significado |
|---|---|
| Dos barras verticales | Uno y solo uno (mínimo 1, máximo 1) |
| Círculo y barra | Cero o uno |
| Barra y pata de gallina | Uno o muchos |
| Círculo y pata de gallina | Cero o muchos |

Table: Convenciones de la notación Crow's Foot

El símbolo más cercano a la entidad indica el **máximo** y el más alejado el **mínimo**. Por ejemplo, entre CLIENTE y
MASCOTA el extremo de CLIENTE tiene dos barras (toda mascota tiene exactamente un propietario) y el extremo de MASCOTA es `o<`
(un cliente puede tener cero o muchas mascotas).

![MER de SIGEVET en notación Crow's Foot (Draw.io)](diagramas/png/19_CrowsFoot_Global_191116-24Oct2026.png){width=100%}

Las marcas #, * y o indican identificador, atributo obligatorio y atributo opcional; PK/FK y UQn identifican llaves y grupos UNIQUE. Las líneas identificadoras son continuas y las demás, discontinuas. Las cardinalidades se derivan del DDL: el padre puede tener cero hijos y UNIQUE limita el máximo a uno. Las participaciones mínimas conceptuales requieren reglas adicionales.

# Entidades del modelo lógico

| Entidad lógica | Origen en el MER |
|---|---|
| DEPARTAMENTO, CIUDAD, SEDE, EMPLEADO, ESPECIALIDAD, CLIENTE, ESPECIE, RAZA, MASCOTA, HISTORIA_CLINICA, SERVICIO, CITA, CONSULTA, MEDICAMENTO, FACTURA | Entidades fuertes |
| VETERINARIO, AUXILIAR | Subtipos de EMPLEADO |
| DETALLE_FACTURA | Entidad débil de FACTURA |
| VETERINARIO_ESPECIALIDAD | Relación N:M CERTIFICADO_EN con atributo `fecha_certificacion` |
| PRESCRIPCION | Relación N:M PRESCRIBE con los atributos de la fórmula |
| INVENTARIO | Relación N:M ALMACENA con los atributos de stock |
| TELEFONO_CLIENTE | Atributo multivaluado `telefono` de CLIENTE |

Table: Correspondencia entre el MER y el modelo lógico

# Construcción y archivos editables en Draw.io

Se leyó el DDL sin modificarlo, se ubicaron las tablas por área y se dibujaron las relaciones desde nulabilidad, PK, UNIQUE y FK. Barker utiliza dos mitades con obligatoriedad independiente y pata sin barras; Crow's Foot utiliza símbolos de mínimo y máximo. Las referencias externas de cada área omiten atributos.

| Hojas Draw.io | Contenido |
|---|---|
| 15–18 | Barker global y áreas de organización, clínica y comercial |
| 19–22 | Crow's Foot global y las mismas áreas |
| 23–26 | Modelo relacional con tipos MySQL |

Las fuentes `.drawio` y sus exportaciones PNG, SVG y PDF son la entrega vigente; los `.mwb` y sus exportaciones anteriores quedan como material histórico de respaldo.
