<!-- Created: 22:45 09-Oct-2026 -->
<!-- Generado por docs/tools/gen_fragmentos.py: no editar a mano -->

| # | Relación | Entidades | Tipo | Participación | Atributos propios |
|---|---|---|---|---|---|
| 1 | **PERTENECE_A** | DEPARTAMENTO (1) — CIUDAD (N) | 1:N | DEPARTAMENTO: parcial; CIUDAD: total | — |
| 2 | **UBICADA_EN** | CIUDAD (1) — SEDE (N) | 1:N | CIUDAD: parcial; SEDE: total | — |
| 3 | **RESIDE_EN** | CIUDAD (1) — CLIENTE (N) | 1:N | CIUDAD: parcial; CLIENTE: total | — |
| 4 | **LABORA_EN** | SEDE (1) — EMPLEADO (N) | 1:N | SEDE: total; EMPLEADO: total | — |
| 5 | **DIRIGE** | EMPLEADO (1) — SEDE (1) | 1:1 | EMPLEADO: parcial; SEDE: total | — |
| 6 | **ES_VETERINARIO** | EMPLEADO (1) — VETERINARIO (1) | 1:1 | EMPLEADO: parcial; VETERINARIO: total | — |
| 7 | **ES_AUXILIAR** | EMPLEADO (1) — AUXILIAR (1) | 1:1 | EMPLEADO: parcial; AUXILIAR: total | — |
| 8 | **CERTIFICADO_EN** | VETERINARIO (M) — ESPECIALIDAD (N) | N:M | VETERINARIO: parcial; ESPECIALIDAD: parcial | fecha_certificacion |
| 9 | **CLASIFICA** | ESPECIE (1) — RAZA (N) | 1:N | ESPECIE: total; RAZA: total | — |
| 10 | **ES_DE_RAZA** | RAZA (1) — MASCOTA (N) | 1:N | RAZA: parcial; MASCOTA: total | — |
| 11 | **PROPIETARIO_DE** | CLIENTE (1) — MASCOTA (N) | 1:N | CLIENTE: parcial; MASCOTA: total | — |
| 12 | **TIENE** | MASCOTA (1) — HISTORIA_CLINICA (1) | 1:1 | MASCOTA: total; HISTORIA_CLINICA: total | — |
| 13 | **SOLICITA** | MASCOTA (1) — CITA (N) | 1:N | MASCOTA: parcial; CITA: total | — |
| 14 | **ATIENDE** | VETERINARIO (1) — CITA (N) | 1:N | VETERINARIO: parcial; CITA: total | — |
| 15 | **SE_PROGRAMA_EN** | SEDE (1) — CITA (N) | 1:N | SEDE: parcial; CITA: total | — |
| 16 | **CORRESPONDE_A** | SERVICIO (1) — CITA (N) | 1:N | SERVICIO: parcial; CITA: total | — |
| 17 | **GENERA** | CITA (1) — CONSULTA (1) | 1:1 | CITA: parcial; CONSULTA: total | — |
| 18 | **ASISTE** | AUXILIAR (1) — CONSULTA (N) | 1:N | AUXILIAR: parcial; CONSULTA: parcial | — |
| 19 | **PRESCRIBE** | CONSULTA (M) — MEDICAMENTO (N) | N:M | CONSULTA: parcial; MEDICAMENTO: parcial | dosis, frecuencia_horas, duracion_dias, cantidad, indicaciones |
| 20 | **ALMACENA** | SEDE (M) — MEDICAMENTO (N) | N:M | SEDE: parcial; MEDICAMENTO: parcial | stock_actual, stock_minimo, fecha_actualizacion |
| 21 | **FACTURADA_A** | CLIENTE (1) — FACTURA (N) | 1:N | CLIENTE: parcial; FACTURA: total | — |
| 22 | **EMITIDA_EN** | SEDE (1) — FACTURA (N) | 1:N | SEDE: parcial; FACTURA: total | — |
| 23 | **EMITE** | EMPLEADO (1) — FACTURA (N) | 1:N | EMPLEADO: parcial; FACTURA: total | — |
| 24 | **CONTIENE** (identificadora) | FACTURA (1) — DETALLE_FACTURA (N) | 1:N | FACTURA: total; DETALLE_FACTURA: total | — |
| 25 | **INCLUYE_SERVICIO** | SERVICIO (1) — DETALLE_FACTURA (N) | 1:N | SERVICIO: parcial; DETALLE_FACTURA: parcial | — |
| 26 | **INCLUYE_MEDICAMENTO** | MEDICAMENTO (1) — DETALLE_FACTURA (N) | 1:N | MEDICAMENTO: parcial; DETALLE_FACTURA: parcial | — |

Table: Catálogo de relaciones del modelo
