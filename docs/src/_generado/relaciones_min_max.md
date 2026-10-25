<!-- Created: 22:45 09-Oct-2026 -->
<!-- Generado por docs/tools/gen_fragmentos.py: no editar a mano -->

| # | Relación | Entidad A (min,max) | Entidad B (min,max) | Tipo |
|---|---|---|---|---|
| 1 | **PERTENECE_A** | DEPARTAMENTO (0,N) | CIUDAD (1,1) | 1:N |
| 2 | **UBICADA_EN** | CIUDAD (0,N) | SEDE (1,1) | 1:N |
| 3 | **RESIDE_EN** | CIUDAD (0,N) | CLIENTE (1,1) | 1:N |
| 4 | **LABORA_EN** | SEDE (1,N) | EMPLEADO (1,1) | 1:N |
| 5 | **DIRIGE** | EMPLEADO (0,1) | SEDE (1,1) | 1:1 |
| 6 | **ES_VETERINARIO** | EMPLEADO (0,1) | VETERINARIO (1,1) | 1:1 |
| 7 | **ES_AUXILIAR** | EMPLEADO (0,1) | AUXILIAR (1,1) | 1:1 |
| 8 | **CERTIFICADO_EN** | VETERINARIO (0,N) | ESPECIALIDAD (0,N) | N:M |
| 9 | **CLASIFICA** | ESPECIE (1,N) | RAZA (1,1) | 1:N |
| 10 | **ES_DE_RAZA** | RAZA (0,N) | MASCOTA (1,1) | 1:N |
| 11 | **PROPIETARIO_DE** | CLIENTE (0,N) | MASCOTA (1,1) | 1:N |
| 12 | **TIENE** | MASCOTA (1,1) | HISTORIA_CLINICA (1,1) | 1:1 |
| 13 | **SOLICITA** | MASCOTA (0,N) | CITA (1,1) | 1:N |
| 14 | **ATIENDE** | VETERINARIO (0,N) | CITA (1,1) | 1:N |
| 15 | **SE_PROGRAMA_EN** | SEDE (0,N) | CITA (1,1) | 1:N |
| 16 | **CORRESPONDE_A** | SERVICIO (0,N) | CITA (1,1) | 1:N |
| 17 | **GENERA** | CITA (0,1) | CONSULTA (1,1) | 1:1 |
| 18 | **ASISTE** | AUXILIAR (0,N) | CONSULTA (0,1) | 1:N |
| 19 | **PRESCRIBE** | CONSULTA (0,N) | MEDICAMENTO (0,N) | N:M |
| 20 | **ALMACENA** | SEDE (0,N) | MEDICAMENTO (0,N) | N:M |
| 21 | **FACTURADA_A** | CLIENTE (0,N) | FACTURA (1,1) | 1:N |
| 22 | **EMITIDA_EN** | SEDE (0,N) | FACTURA (1,1) | 1:N |
| 23 | **EMITE** | EMPLEADO (0,N) | FACTURA (1,1) | 1:N |
| 24 | **CONTIENE** (identificadora) | FACTURA (1,N) | DETALLE_FACTURA (1,1) | 1:N |
| 25 | **INCLUYE_SERVICIO** | SERVICIO (0,N) | DETALLE_FACTURA (0,1) | 1:N |
| 26 | **INCLUYE_MEDICAMENTO** | MEDICAMENTO (0,N) | DETALLE_FACTURA (0,1) | 1:N |

Table: Restricciones estructurales (min,max) del modelo
