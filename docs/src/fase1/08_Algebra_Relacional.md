---
title: "Álgebra Relacional"
subtitle: "Documento de Implementación — Sentencias de Álgebra Relacional"
fase: "Fase 1"
version: "1.0"
---
<!-- Created: 22:55 09-Oct-2026 -->

# Introducción

El álgebra relacional es el lenguaje formal y procedimental sobre el que se fundamentan los motores relacionales: cada
consulta SQL se traduce internamente a un árbol de operaciones del álgebra. Este documento presenta **55 ejercicios**
sobre la base de datos SIGEVET. Cada ejercicio incluye el enunciado en lenguaje natural, la expresión en álgebra
relacional, la sentencia SQL equivalente para MySQL y el **resultado real** obtenido al ejecutarla sobre el conjunto de
datos de prueba del proyecto.

# Operadores y notación

| Operador | Símbolo | Tipo | Descripción |
|---|---|---|---|
| Selección | $\sigma_{condición}(R)$ | Unario | Filtra las tuplas de R que cumplen la condición (equivale a WHERE) |
| Proyección | $\pi_{atributos}(R)$ | Unario | Conserva solo los atributos indicados y elimina duplicados (SELECT DISTINCT) |
| Renombramiento | $\rho_{S(a_1, \ldots, a_n)}(R)$ | Unario | Cambia el nombre de la relación o de sus atributos (alias) |
| Unión | $R \cup S$ | Binario de conjuntos | Tuplas que están en R, en S o en ambas (UNION) |
| Intersección | $R \cap S$ | Binario de conjuntos | Tuplas que están en R y en S (INTERSECT) |
| Diferencia | $R - S$ | Binario de conjuntos | Tuplas de R que no están en S (MINUS) |
| Producto cartesiano | $R \times S$ | Binario | Todas las combinaciones de tuplas (CROSS JOIN) |
| Reunión theta / equirreunión | $R \bowtie_{condición} S$ | Binario | Producto seguido de selección (JOIN … ON) |
| Reunión natural | $R \bowtie S$ | Binario | Reunión por igualdad de los atributos con el mismo nombre (NATURAL JOIN) |
| Reunión externa izquierda, derecha y completa | $⟕$, $⟖$, $⟗$ | Binario | Conserva las tuplas sin pareja completándolas con nulos (LEFT, RIGHT, FULL OUTER JOIN) |
| Semirreunión | $R \ltimes S$ | Binario | Tuplas de R que tienen pareja en S (EXISTS) |
| Antirreunión | $R \triangleright S$ | Binario | Tuplas de R que no tienen pareja en S (NOT EXISTS) |
| División | $R \div S$ | Binario | Valores de R asociados con **todas** las tuplas de S (doble NOT EXISTS) |
| Agregación | ${}_{G}\gamma_{F}(R)$ | Extendido | Agrupa por G y calcula las funciones F: COUNT, SUM, AVG, MIN, MAX (GROUP BY) |
| Asignación | $T \leftarrow E$ | Auxiliar | Guarda un resultado intermedio para usarlo en el paso siguiente |

Table: Operadores del álgebra relacional utilizados

Convenciones adicionales: ∧ (y), ∨ (o), ≠ (diferente), ≥ y ≤ (comparaciones), NULO (valor nulo). En la proyección
generalizada, la expresión `a · b → nombre` calcula un atributo nuevo. Para las fechas se usan literales con formato
ISO (`'2026-03-01'`), que en SQL se escriben `DATE '2026-03-01'`.

# Esquema de referencia

Las consultas se formulan sobre el esquema relacional de SIGEVET (llave primaria subrayada, *llaves foráneas en
cursiva*):

<!-- INCLUDE: _generado/esquema_relacional.md -->

# Metodología de verificación

1. Cada ejercicio se escribió primero en álgebra relacional y luego se tradujo a SQL de MySQL.
2. Las 55 sentencias SQL se reúnen en el script `database/03_varios/01_consultas_algebra_relacional.sql`.
   Están escritas para MySQL 8.4: la diferencia de conjuntos se escribe `EXCEPT`, el límite de filas `LIMIT` y, como
   MySQL no tiene `FULL OUTER JOIN`, la reunión externa completa del ejercicio 29 se arma como la unión (`UNION`) de la
   reunión externa izquierda y la derecha, que es justo su definición en álgebra relacional.
3. Para comprobar que cada expresión es correcta, las consultas se ejecutaron sobre los datos de prueba del proyecto
   (1.091 registros generados de forma reproducible). Los resultados de este documento son la salida real de esas
   ejecuciones; cuando el resultado tiene más de 8 filas se muestran las primeras 8 y se indica el total.

| Tabla | Registros | Tabla | Registros |
|---|---|---|---|
| DEPARTAMENTO | 6 | CIUDAD | 11 |
| SEDE | 5 | EMPLEADO | 27 |
| VETERINARIO | 12 | AUXILIAR | 8 |
| ESPECIALIDAD | 8 | VETERINARIO_ESPECIALIDAD | 12 |
| CLIENTE | 45 | TELEFONO_CLIENTE | 66 |
| ESPECIE | 5 | RAZA | 20 |
| MASCOTA | 59 | HISTORIA_CLINICA | 59 |
| SERVICIO | 14 | CITA | 144 |
| CONSULTA | 95 | MEDICAMENTO | 18 |
| PRESCRIPCION | 88 | INVENTARIO | 79 |
| FACTURA | 109 | DETALLE_FACTURA | 201 |

Table: Volumen de los datos de prueba

# Resumen de ejercicios

| Grupo | Ejercicios | Cantidad |
|---|---|---|
| Selección y proyección | 1 a 12 | 12 |
| Producto cartesiano | 13 y 14 | 2 |
| Renombramiento | 15 y 16 | 2 |
| Reunión natural y reunión theta | 17 a 25 | 9 |
| Reuniones externas | 26 a 29 | 4 |
| Semirreunión y antirreunión | 30 a 32 | 3 |
| Operaciones de conjuntos | 33 a 40 | 8 |
| División | 41 a 43 | 3 |
| Agregación y agrupamiento | 44 a 55 | 12 |
| **Total** | | **55** |

Table: Distribución de los ejercicios por tipo de operación

# Ejercicios

<!-- INCLUDE: _generado/algebra_ejercicios.md -->

# Conclusiones

- Las 55 expresiones del álgebra relacional tienen una sentencia SQL equivalente y todas producen resultados sobre los
  datos de prueba, lo que valida el modelo: las llaves foráneas permiten reconstruir cualquier relación del MER.
- La proyección del álgebra elimina duplicados, mientras que en SQL se necesita `DISTINCT` (ejercicio 10).
- Las reuniones externas y la antirreunión responden preguntas sobre "lo que falta" (clientes sin mascotas, mascotas
  sin atenciones) que la reunión interna no puede responder.
- La división no tiene un operador directo en SQL; se implementa con doble `NOT EXISTS` (ejercicios 41 a 43).
- Los atributos derivados del MER (edad, totales de factura) se obtienen con proyección generalizada y agregación, sin
  necesidad de almacenarlos, lo que mantiene el esquema en 3FN.
