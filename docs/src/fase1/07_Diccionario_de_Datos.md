---
title: "Diccionario de Datos"
subtitle: "Diccionario de Datos"
fase: "Fase 1"
version: "1.0"
---
<!-- Created: 22:48 09-Oct-2026 -->

# Introducción

El diccionario de datos describe cada tabla y cada columna de la base de datos SIGEVET: tipo de dato de MySQL,
obligatoriedad, participación en llaves, valores por defecto, reglas de dominio (CHECK), significado y un valor de
ejemplo. Se entrega en este documento y en el libro de Excel `07_Diccionario_de_Datos.xlsx`, que permite filtrar y
buscar.

Las descripciones también quedan dentro de la base de datos: cada tabla y cada columna del script de creación
(`database/01_ddl/01_crear_tablas.sql`) lleva su `COMMENT`, así que se pueden consultar en MySQL Workbench (pestaña
*Columns* de cada tabla) o con `information_schema.TABLES` e `information_schema.COLUMNS`.

# Convenciones

| Elemento | Convención |
|---|---|
| Nombres de tablas | MAYÚSCULAS, singular, con guion bajo (`HISTORIA_CLINICA`) |
| Nombres de columnas | minúsculas con guion bajo (`fecha_nacimiento`) |
| Llave primaria | `id_<tabla>`, numérica; restricción `PK_<TABLA>` |
| Llave foránea | Mismo nombre de la PK referenciada, o un rol (`id_director`, `id_veterinario`, `id_auxiliar`); restricción `FK_<TABLA>_<REFERENCIA>` |
| Llave única | `UQ_<TABLA>_<COLUMNAS>` |
| Verificación | `CK_<TABLA>_<COLUMNA>` |
| Columna Llave | PK = llave primaria, FK = llave foránea, UK = forma parte de una llave única |
| Columna Nulo | "No" = NOT NULL (obligatoria); "Sí" = opcional |

Table: Convenciones del diccionario

| Tipo MySQL | Uso en el modelo |
|---|---|
| INT / BIGINT | Identificadores (con `AUTO_INCREMENT` en las llaves simples), cantidades, años |
| DECIMAL(p,s) | Valores monetarios, peso, temperatura y porcentajes |
| VARCHAR(n) | Textos de longitud variable |
| CHAR(n) | Códigos de longitud fija (DANE, discriminador `tipo_empleado`, banderas S/N, sexo) |
| DATE | Fechas sin hora (nacimiento, apertura, ingreso) |
| DATETIME | Fechas con hora (citas, consultas, facturas) y fechas de registro (`DEFAULT CURRENT_TIMESTAMP`) |

Table: Tipos de datos utilizados

<!-- INCLUDE: _generado/resumen_restricciones.md -->

# Diccionario por tabla

<!-- INCLUDE: _generado/diccionario.md -->
