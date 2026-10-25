<!-- Created: 22:45 09-Oct-2026 -->
<!-- Generado por docs/tools/gen_fragmentos.py: no editar a mano -->

| Tabla hija | Columna(s) | Tabla padre | Relación del MER | ON DELETE |
|---|---|---|---|---|
| CIUDAD | id_departamento | DEPARTAMENTO(id_departamento) | PERTENECE_A | NO ACTION |
| SEDE | id_ciudad | CIUDAD(id_ciudad) | UBICADA_EN | NO ACTION |
| SEDE | id_director | EMPLEADO(id_empleado) | DIRIGE | NO ACTION |
| EMPLEADO | id_sede | SEDE(id_sede) | LABORA_EN | NO ACTION |
| VETERINARIO | id_empleado | EMPLEADO(id_empleado) | ES_UN | CASCADE |
| AUXILIAR | id_empleado | EMPLEADO(id_empleado) | ES_UN | CASCADE |
| VETERINARIO_ESPECIALIDAD | id_empleado | VETERINARIO(id_empleado) | CERTIFICADO_EN | CASCADE |
| VETERINARIO_ESPECIALIDAD | id_especialidad | ESPECIALIDAD(id_especialidad) | CERTIFICADO_EN | NO ACTION |
| CLIENTE | id_ciudad | CIUDAD(id_ciudad) | RESIDE_EN | NO ACTION |
| TELEFONO_CLIENTE | id_cliente | CLIENTE(id_cliente) | TIENE_TELEFONO | CASCADE |
| RAZA | id_especie | ESPECIE(id_especie) | CLASIFICA | NO ACTION |
| MASCOTA | id_raza | RAZA(id_raza) | ES_DE_RAZA | NO ACTION |
| MASCOTA | id_cliente | CLIENTE(id_cliente) | PROPIETARIO_DE | NO ACTION |
| HISTORIA_CLINICA | id_mascota | MASCOTA(id_mascota) | TIENE | CASCADE |
| CITA | id_mascota | MASCOTA(id_mascota) | SOLICITA | NO ACTION |
| CITA | id_veterinario | VETERINARIO(id_empleado) | ATIENDE | NO ACTION |
| CITA | id_sede | SEDE(id_sede) | SE_PROGRAMA_EN | NO ACTION |
| CITA | id_servicio | SERVICIO(id_servicio) | CORRESPONDE_A | NO ACTION |
| CONSULTA | id_cita | CITA(id_cita) | GENERA | NO ACTION |
| CONSULTA | id_auxiliar | AUXILIAR(id_empleado) | ASISTE | NO ACTION |
| PRESCRIPCION | id_consulta | CONSULTA(id_consulta) | PRESCRIBE | CASCADE |
| PRESCRIPCION | id_medicamento | MEDICAMENTO(id_medicamento) | PRESCRIBE | NO ACTION |
| INVENTARIO | id_sede | SEDE(id_sede) | ALMACENA | NO ACTION |
| INVENTARIO | id_medicamento | MEDICAMENTO(id_medicamento) | ALMACENA | NO ACTION |
| FACTURA | id_cliente | CLIENTE(id_cliente) | FACTURADA_A | NO ACTION |
| FACTURA | id_sede | SEDE(id_sede) | EMITIDA_EN | NO ACTION |
| FACTURA | id_empleado | EMPLEADO(id_empleado) | EMITE | NO ACTION |
| DETALLE_FACTURA | id_factura | FACTURA(id_factura) | CONTIENE | CASCADE |
| DETALLE_FACTURA | id_servicio | SERVICIO(id_servicio) | INCLUYE_SERVICIO | NO ACTION |
| DETALLE_FACTURA | id_medicamento | MEDICAMENTO(id_medicamento) | INCLUYE_MEDICAMENTO | NO ACTION |

Table: Llaves foráneas del modelo relacional
