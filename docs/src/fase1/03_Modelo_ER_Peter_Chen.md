---
title: "Modelo Entidad-Relación — Notación Peter Chen"
subtitle: "Modelo Entidad Relación con notación Peter Chen"
fase: "Fase 1"
version: "1.0"
---
<!-- Created: 22:45 09-Oct-2026 -->
<!-- Updated: 18:55 24-Oct-2026 -->
<!-- Updated: 18:55 24-Oct-2026 -->

# Introducción

El Modelo Entidad-Relación (MER) propuesto por Peter Chen (1976) representa el nivel **conceptual** de la base de
datos: qué cosas del mundo real interesan a la organización (entidades), qué se sabe de ellas (atributos) y cómo se
asocian (relaciones), sin decisiones de implementación. Este documento presenta el MER de SIGEVET construido a partir de
la propuesta y del documento de supuestos.

# Notación utilizada

| Símbolo | Significado |
|---|---|
| Rectángulo | Entidad fuerte |
| Rectángulo doble | Entidad débil (DETALLE_FACTURA) |
| Rombo | Relación |
| Rombo doble | Relación identificadora de la entidad débil (CONTIENE) |
| Elipse | Atributo simple |
| Elipse con nombre subrayado | Atributo clave (identificador) |
| Elipse con nombre en cursiva y subrayado discontinuo | Llave parcial de la entidad débil (`num_linea`) |
| Elipse unida a otras elipses | Atributo compuesto (`documento`, `nombre_completo`) |
| Elipse doble | Atributo multivaluado (`telefono` de CLIENTE) |
| Elipse punteada | Atributo derivado (`edad`, `antiguedad`, `total`, `subtotal`) |
| Línea sencilla / línea doble | Participación parcial / participación total |
| 1, N, M junto a la entidad | Razón de cardinalidad de la relación |

Table: Convenciones de la notación Chen

La cardinalidad se lee "a través" de la relación: en DEPARTAMENTO —1— PERTENECE_A —N— CIUDAD, un departamento se
relaciona con N ciudades y cada ciudad con un solo departamento. Las llaves foráneas **no** aparecen como atributos: en
el nivel conceptual la asociación se expresa con la relación.

# Diagrama completo

La vista global muestra la estructura de las 18 entidades y 26 relaciones, sin atributos. Los atributos completos se presentan por entidad en los tres catálogos gráficos de este capítulo. Las fuentes editables se entregan en Draw.io y las figuras en PNG, SVG y PDF.

![MER de SIGEVET en notación Peter Chen (completo)](diagramas/png/04_Chen_Global_191116-24Oct2026.png){width=100%}

# Vistas por área

Cada área se presenta en filas independientes, una por relación. Una misma entidad puede repetirse como referencia para evitar cruces de líneas; cada repetición representa la misma entidad del modelo. Los atributos se consultan en los catálogos gráficos siguientes.

## Área Organización

Agrupa la ubicación geográfica, las sedes y el personal. Contiene la relación 1:1 **DIRIGE** (cada sede tiene un
director) y la relación N:M **CERTIFICADO_EN** (veterinarios y especialidades, con la fecha de certificación como
atributo de la relación). Los subtipos VETERINARIO y AUXILIAR se asocian a EMPLEADO mediante relaciones 1:1 (ES_UN);
en el modelo extendido esto se representa con una especialización.

![MER Chen — Área Organización](diagramas/png/01_Chen_Organizacion_191116-24Oct2026.png){width=100%}

## Área Clínica

Contiene clientes, mascotas, historias clínicas, servicios, citas y consultas. Se destacan la relación 1:1 **TIENE**
(una historia clínica por mascota, total en ambos lados), la relación 1:1 **GENERA** (una consulta por cita atendida),
el atributo multivaluado `telefono` y el atributo derivado `edad`.

![MER Chen — Área Clínica](diagramas/png/02_Chen_Clinica_191116-24Oct2026.png){width=100%}

## Área Comercial

Contiene medicamentos y facturación. La relación N:M **PRESCRIBE** une consultas y medicamentos con los atributos de
la fórmula; la relación N:M **ALMACENA** une sedes y medicamentos con el stock. DETALLE_FACTURA es una **entidad
débil**: no tiene identificador propio y se identifica por la factura que la contiene más su número de línea.

![MER Chen — Área Comercial](diagramas/png/03_Chen_Comercial_191116-24Oct2026.png){width=100%}

# Catálogos gráficos de atributos

Cada entidad tiene un identificador, sus atributos y sus convenciones Chen. Las repeticiones de entidad entre hojas son referencias al mismo objeto conceptual.

![Catálogo Chen de atributos — Organizacion](diagramas/png/11_Atributos_Organizacion_191116-24Oct2026.png){width=100%}

![Catálogo Chen de atributos — Clinica](diagramas/png/12_Atributos_Clinica_191116-24Oct2026.png){width=100%}

![Catálogo Chen de atributos — Comercial](diagramas/png/13_Atributos_Comercial_191116-24Oct2026.png){width=100%}

# Catálogo de entidades

<!-- INCLUDE: _generado/catalogo_entidades.md -->

# Clasificación de atributos

<!-- INCLUDE: _generado/catalogo_atributos.md -->

# Catálogo de relaciones

<!-- INCLUDE: _generado/catalogo_relaciones.md -->

# Justificación de cardinalidades relevantes

- **DIRIGE (1:1):** una sede tiene exactamente un director (participación total de SEDE) y un empleado dirige como
  máximo una sede (parcial de EMPLEADO: la mayoría no son directores). Supuestos S-04 y S-05.
- **TIENE (1:1):** la historia clínica es única por mascota y se comparte entre sedes; ninguna existe sin la otra
  (total en ambos lados). Supuesto S-23.
- **GENERA (1:1):** solo una cita atendida produce consulta (parcial de CITA) y toda consulta proviene de una cita
  (total de CONSULTA). Supuestos S-27 y S-28.
- **PROPIETARIO_DE (1:N):** un cliente puede tener varias mascotas o ninguna (parcial); toda mascota tiene un dueño
  (total). Supuestos S-16 y S-17.
- **PRESCRIBE (N:M):** una consulta puede formular varios medicamentos y un medicamento aparece en muchas consultas;
  ambas participaciones son parciales (hay consultas sin fórmula y medicamentos nunca formulados). Supuesto S-30.
- **CONTIENE (1:N identificadora):** una factura tiene una o más líneas y cada línea pertenece a una sola factura,
  de la que depende su existencia. Supuesto S-36.

# Verificación de los requisitos del enunciado

| Requisito | Resultado |
|---|---|
| Al menos 8 entidades | 18 entidades |
| Relaciones 1:1 | 5 (DIRIGE, TIENE, GENERA, ES_VETERINARIO, ES_AUXILIAR) |
| Relaciones 1:N | 18 |
| Relaciones N:M | 3 (CERTIFICADO_EN, PRESCRIBE, ALMACENA) |
| Tipos de atributo | Clave, simple, compuesto, multivaluado, derivado y llave parcial |
| Entidad débil | DETALLE_FACTURA con relación identificadora CONTIENE |

Table: Verificación del MER frente al enunciado
