<!-- Created: 22:45 09-Oct-2026 -->
<!-- Generado por docs/tools/gen_fragmentos.py: no editar a mano -->

| Entidad | Tipo | Área | Identificador | Descripción |
|---|---|---|---|---|
| **DEPARTAMENTO** | Catálogo | Organización | id_departamento | Departamentos de Colombia donde la red tiene sedes o clientes (codificación DANE). |
| **CIUDAD** | Catálogo | Organización | id_ciudad | Municipios donde se ubican sedes y residen clientes. |
| **SEDE** | Fuerte | Organización | id_sede | Clínica física de la red Huella Sana. Cada sede tiene exactamente un director. |
| **EMPLEADO** | Supertipo | Organización | id_empleado | Supertipo de todo el personal. El discriminador tipo_empleado indica el subtipo (VET, AUX) o ADM para personal administrativo que no pertenece a ningún subtipo. |
| **VETERINARIO** | Subtipo de EMPLEADO | Organización | id_empleado | Subtipo de EMPLEADO: médico veterinario habilitado para atender citas y prescribir. |
| **AUXILIAR** | Subtipo de EMPLEADO | Organización | id_empleado | Subtipo de EMPLEADO: auxiliar veterinario que asiste las consultas. |
| **ESPECIALIDAD** | Catálogo | Organización | id_especialidad | Áreas de especialización médica veterinaria. |
| **CLIENTE** | Fuerte | Clínica | id_cliente | Persona propietaria de una o más mascotas; es quien paga los servicios. |
| **ESPECIE** | Catálogo | Clínica | id_especie | Especie animal atendida por la red. |
| **RAZA** | Catálogo | Clínica | id_raza | Raza de una especie. Separarla de MASCOTA elimina la dependencia transitiva raza → especie (3FN). |
| **MASCOTA** | Fuerte | Clínica | id_mascota | Paciente de la red. La edad es un atributo derivado de fecha_nacimiento (no se almacena). |
| **HISTORIA_CLINICA** | Fuerte | Clínica | id_historia | Historia clínica única de cada mascota (relación 1:1 TIENE), compartida por todas las sedes. |
| **SERVICIO** | Catálogo | Clínica | id_servicio | Catálogo de servicios ofrecidos con su precio vigente y duración estimada. |
| **CITA** | Fuerte | Clínica | id_cita | Reserva de atención de una mascota con un veterinario, en una sede, para un servicio. |
| **CONSULTA** | Fuerte | Clínica | id_consulta | Registro clínico de una cita atendida (relación 1:1 GENERA con CITA). |
| **MEDICAMENTO** | Catálogo | Comercial | id_medicamento | Catálogo de medicamentos e insumos veterinarios con su precio de venta vigente. |
| **FACTURA** | Fuerte | Comercial | id_factura | Documento de venta emitido a un cliente en una sede. El total es derivado (vista V_FACTURA_TOTAL). |
| **DETALLE_FACTURA** | Débil | Comercial | num_linea | Entidad débil de FACTURA: cada línea cobra un servicio O un medicamento (arco exclusivo). |

Table: Catálogo de entidades del MER
