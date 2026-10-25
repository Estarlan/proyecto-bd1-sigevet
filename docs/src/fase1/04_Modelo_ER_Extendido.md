---
title: "Modelo Entidad-Relación Extendido (EER)"
subtitle: "Modelo Entidad Relación Extendido"
fase: "Fase 1"
version: "1.0"
---
<!-- Created: 22:45 09-Oct-2026 -->
<!-- Updated: 18:55 24-Oct-2026 -->
<!-- Updated: 18:55 24-Oct-2026 -->

# Introducción

El Modelo Entidad-Relación Extendido (EER) añade al MER de Chen conceptos para representar jerarquías de entidades
(superclases y subclases), restricciones de especialización y restricciones estructurales más precisas. En SIGEVET el
EER **sí aplica**, porque el personal comparte datos comunes pero los veterinarios y los auxiliares tienen atributos y
relaciones propias.

# Conceptos aplicados

| Concepto | Aplicación en SIGEVET |
|---|---|
| Superclase | EMPLEADO: datos comunes de todo el personal |
| Subclases | VETERINARIO (tarjeta profesional, universidad, año de graduación) y AUXILIAR (área, certificación, turno) |
| Herencia | Las subclases heredan todos los atributos y relaciones de EMPLEADO (por ejemplo LABORA_EN y EMITE) |
| Relaciones propias de subclase | Solo VETERINARIO participa en ATIENDE y CERTIFICADO_EN; solo AUXILIAR participa en ASISTE |
| Restricción de disyunción (d) | Un empleado pertenece a lo sumo a una subclase |
| Restricción de completitud parcial | Hay empleados (administrativos) que no pertenecen a ninguna subclase |
| Atributo discriminador | `tipo_empleado` con dominio {VET, AUX, ADM} |
| Restricciones estructurales (min,max) | Cada extremo de relación indica el mínimo y el máximo de participaciones |
| Restricción de exclusión | Una línea de factura cobra un servicio **o** un medicamento ({XOR}) |

Table: Conceptos del modelo extendido utilizados

# Especialización de EMPLEADO

![Especialización disyunta y parcial de EMPLEADO](diagramas/png/10_EER_Especializacion_EMPLEADO_191116-24Oct2026.png){width=90%}

**Lectura del diagrama.** La línea sencilla entre EMPLEADO y el círculo indica participación **parcial**: no todo
empleado es veterinario o auxiliar (el personal administrativo permanece solo en la superclase). La letra **d** dentro
del círculo indica que la especialización es **disyunta**: un empleado no puede ser veterinario y auxiliar a la vez.
El símbolo de subconjunto (⊂) en cada línea hacia una subclase indica que el conjunto de veterinarios y el de
auxiliares son subconjuntos del conjunto de empleados.

**Por qué es disyunta y parcial.** El supuesto S-09 establece que un empleado tiene un único rol clínico, y el
supuesto S-10 que el personal administrativo no tiene atributos propios que justifiquen una tercera subclase. La
especialización está **definida por atributo**: el valor de `tipo_empleado` determina la subclase.

# Restricciones estructurales (min,max)

En el EER se reemplaza la razón de cardinalidad 1/N por la notación (min,max) de Elmasri y Navathe, que se lee
"mirando aquí": el par escrito junto a una entidad indica en cuántas instancias de la relación participa cada
ocurrencia de esa entidad. Un mínimo 0 equivale a participación parcial y un mínimo 1 a participación total.

<!-- INCLUDE: _generado/relaciones_min_max.md -->

Ejemplos de lectura:

- LABORA_EN: SEDE (1,N) — cada sede tiene entre 1 y N empleados; EMPLEADO (1,1) — cada empleado labora en
  exactamente una sede.
- DIRIGE: EMPLEADO (0,1) — un empleado dirige ninguna o una sede; SEDE (1,1) — cada sede tiene exactamente un director.
- PRESCRIBE: CONSULTA (0,N) y MEDICAMENTO (0,N) — puede haber consultas sin fórmula y medicamentos nunca formulados.

# Restricción de exclusión en el detalle de factura

Cada línea de DETALLE_FACTURA cobra exactamente un ítem, que puede ser un SERVICIO o un MEDICAMENTO. Las relaciones
INCLUYE_SERVICIO e INCLUYE_MEDICAMENTO son, por tanto, **mutuamente excluyentes** y una de las dos debe existir. En el
diagrama se anota con una nota {XOR}. En el modelo relacional se implementa con dos llaves foráneas que admiten nulos
y la restricción `CK_DETALLE_FACTURA_ITEM`:

```sql
CHECK ((id_servicio IS NOT NULL AND id_medicamento IS NULL)
    OR (id_servicio IS NULL AND id_medicamento IS NOT NULL))
```

Se evaluó modelar una categoría (tipo unión) ITEM_FACTURABLE = SERVICIO ∪ MEDICAMENTO. Se descartó porque obliga a
crear una tabla adicional con llave sustituta, mientras que el arco exclusivo cumple la misma regla con menos tablas y
consultas más simples.

# Diagrama EER completo

La vista EER muestra relaciones y especialización; los atributos se consultan en los catálogos del documento de Chen. Las entidades que se repiten entre filas son referencias a la misma entidad.

![MER Extendido de SIGEVET con notación (min,max) y especialización](diagramas/png/14_EER_Completo_191116-24Oct2026.png){width=100%}

# Transformación de la especialización al modelo relacional

Elmasri y Navathe proponen cuatro opciones para llevar una especialización a tablas:

| Opción | Descripción | Ventaja | Desventaja |
|---|---|---|---|
| 8A | Una tabla para la superclase y una por subclase (la PK de la subclase es FK a la superclase) | Sin nulos, funciona para cualquier tipo de especialización | Requiere JOIN para ver datos completos |
| 8B | Solo tablas para las subclases, con los atributos heredados | Sin JOIN | Solo sirve si la especialización es total; repite columnas |
| 8C | Una sola tabla con un atributo discriminador | Sin JOIN | Muchas columnas nulas; solo para disyuntas |
| 8D | Una sola tabla con banderas booleanas por subclase | Admite solapamiento | Muchas columnas nulas |

Table: Opciones de transformación de una especialización

**Opción elegida: 8A.** Como la especialización es parcial, la opción 8B no es posible: los administrativos se
perderían. Las opciones 8C y 8D dejarían nulas las columnas de veterinario en los auxiliares y viceversa, y además
VETERINARIO y AUXILIAR tienen relaciones propias (ATIENDE, CERTIFICADO_EN, ASISTE) cuyas llaves foráneas deben apuntar
exactamente a un veterinario o a un auxiliar. El resultado es:

- **EMPLEADO**([id_empleado]{.underline}, …, tipo_empleado, …, *id_sede*)
- **VETERINARIO**(*[id_empleado]{.underline}*, tarjeta_profesional, universidad, anio_graduacion)
- **AUXILIAR**(*[id_empleado]{.underline}*, area, certificacion, turno)

Se conserva además el discriminador `tipo_empleado` en EMPLEADO para identificar a los administrativos sin consultar
las subtablas. La coherencia entre el discriminador y la subtabla (regla RN-09) se garantizará con un disparador en
la Fase 2.
