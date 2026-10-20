-- SIGEVET - Proyecto final Bases de Datos 1
-- Comentarios de tablas y columnas (diccionario de datos en el catálogo)
-- Oracle Database Free, esquema VETDB (PDB FREEPDB1)

COMMENT ON TABLE DEPARTAMENTO IS 'Departamentos de Colombia donde la red tiene sedes o clientes (codificación DANE).';
COMMENT ON COLUMN DEPARTAMENTO.id_departamento IS 'Identificador del departamento.';
COMMENT ON COLUMN DEPARTAMENTO.codigo_dane IS 'Código DANE de dos dígitos.';
COMMENT ON COLUMN DEPARTAMENTO.nombre IS 'Nombre oficial del departamento.';

COMMENT ON TABLE CIUDAD IS 'Municipios donde se ubican sedes y residen clientes.';
COMMENT ON COLUMN CIUDAD.id_ciudad IS 'Identificador de la ciudad.';
COMMENT ON COLUMN CIUDAD.codigo_dane IS 'Código DANE del municipio (5 dígitos).';
COMMENT ON COLUMN CIUDAD.nombre IS 'Nombre del municipio.';
COMMENT ON COLUMN CIUDAD.id_departamento IS 'Departamento al que pertenece la ciudad.';

COMMENT ON TABLE SEDE IS 'Clínica física de la red Huella Sana. Cada sede tiene exactamente un director.';
COMMENT ON COLUMN SEDE.id_sede IS 'Identificador de la sede.';
COMMENT ON COLUMN SEDE.nombre IS 'Nombre comercial de la sede.';
COMMENT ON COLUMN SEDE.direccion IS 'Dirección física.';
COMMENT ON COLUMN SEDE.telefono IS 'Teléfono principal de la sede.';
COMMENT ON COLUMN SEDE.email IS 'Correo electrónico de la sede.';
COMMENT ON COLUMN SEDE.fecha_apertura IS 'Fecha en que la sede inició operación.';
COMMENT ON COLUMN SEDE.id_ciudad IS 'Ciudad donde se ubica la sede.';
COMMENT ON COLUMN SEDE.id_director IS 'Empleado que dirige la sede (relación 1:1 DIRIGE).';

COMMENT ON TABLE EMPLEADO IS 'Supertipo de todo el personal. El discriminador tipo_empleado indica el subtipo (VET, AUX) o ADM para personal administrativo que no pertenece a ningún subtipo.';
COMMENT ON COLUMN EMPLEADO.id_empleado IS 'Identificador del empleado.';
COMMENT ON COLUMN EMPLEADO.tipo_documento IS 'Tipo de documento de identidad.';
COMMENT ON COLUMN EMPLEADO.num_documento IS 'Número del documento de identidad.';
COMMENT ON COLUMN EMPLEADO.nombres IS 'Nombres del empleado (parte del atributo compuesto nombre_completo).';
COMMENT ON COLUMN EMPLEADO.apellidos IS 'Apellidos del empleado.';
COMMENT ON COLUMN EMPLEADO.fecha_nacimiento IS 'Fecha de nacimiento.';
COMMENT ON COLUMN EMPLEADO.telefono IS 'Teléfono de contacto.';
COMMENT ON COLUMN EMPLEADO.email IS 'Correo corporativo.';
COMMENT ON COLUMN EMPLEADO.tipo_empleado IS 'Discriminador de la especialización: VET, AUX o ADM.';
COMMENT ON COLUMN EMPLEADO.fecha_ingreso IS 'Fecha de vinculación a la red.';
COMMENT ON COLUMN EMPLEADO.salario IS 'Salario mensual en pesos colombianos.';
COMMENT ON COLUMN EMPLEADO.estado IS 'Estado laboral.';
COMMENT ON COLUMN EMPLEADO.id_sede IS 'Sede donde labora el empleado (relación LABORA_EN).';

COMMENT ON TABLE VETERINARIO IS 'Subtipo de EMPLEADO: médico veterinario habilitado para atender citas y prescribir.';
COMMENT ON COLUMN VETERINARIO.id_empleado IS 'Identificador heredado del supertipo EMPLEADO.';
COMMENT ON COLUMN VETERINARIO.tarjeta_profesional IS 'Número de tarjeta profesional (COMVEZCOL).';
COMMENT ON COLUMN VETERINARIO.universidad IS 'Universidad que otorgó el título.';
COMMENT ON COLUMN VETERINARIO.anio_graduacion IS 'Año de graduación.';

COMMENT ON TABLE AUXILIAR IS 'Subtipo de EMPLEADO: auxiliar veterinario que asiste las consultas.';
COMMENT ON COLUMN AUXILIAR.id_empleado IS 'Identificador heredado del supertipo EMPLEADO.';
COMMENT ON COLUMN AUXILIAR.area IS 'Área de desempeño.';
COMMENT ON COLUMN AUXILIAR.certificacion IS 'Título técnico o tecnológico.';
COMMENT ON COLUMN AUXILIAR.turno IS 'Turno habitual.';

COMMENT ON TABLE ESPECIALIDAD IS 'Áreas de especialización médica veterinaria.';
COMMENT ON COLUMN ESPECIALIDAD.id_especialidad IS 'Identificador de la especialidad.';
COMMENT ON COLUMN ESPECIALIDAD.nombre IS 'Nombre de la especialidad.';
COMMENT ON COLUMN ESPECIALIDAD.descripcion IS 'Descripción breve.';

COMMENT ON TABLE VETERINARIO_ESPECIALIDAD IS 'Resuelve la relación N:M CERTIFICADO_EN entre VETERINARIO y ESPECIALIDAD.';
COMMENT ON COLUMN VETERINARIO_ESPECIALIDAD.id_empleado IS 'Veterinario certificado.';
COMMENT ON COLUMN VETERINARIO_ESPECIALIDAD.id_especialidad IS 'Especialidad certificada.';
COMMENT ON COLUMN VETERINARIO_ESPECIALIDAD.fecha_certificacion IS 'Fecha en que obtuvo la certificación.';

COMMENT ON TABLE CLIENTE IS 'Persona propietaria de una o más mascotas; es quien paga los servicios.';
COMMENT ON COLUMN CLIENTE.id_cliente IS 'Identificador del cliente.';
COMMENT ON COLUMN CLIENTE.tipo_documento IS 'Tipo de documento de identidad.';
COMMENT ON COLUMN CLIENTE.num_documento IS 'Número del documento de identidad.';
COMMENT ON COLUMN CLIENTE.nombres IS 'Nombres del cliente.';
COMMENT ON COLUMN CLIENTE.apellidos IS 'Apellidos del cliente.';
COMMENT ON COLUMN CLIENTE.email IS 'Correo electrónico.';
COMMENT ON COLUMN CLIENTE.direccion IS 'Dirección de residencia.';
COMMENT ON COLUMN CLIENTE.id_ciudad IS 'Ciudad de residencia.';
COMMENT ON COLUMN CLIENTE.fecha_registro IS 'Fecha de registro en el sistema.';

COMMENT ON TABLE TELEFONO_CLIENTE IS 'Atributo multivaluado teléfono de CLIENTE llevado a tabla (Primera Forma Normal).';
COMMENT ON COLUMN TELEFONO_CLIENTE.id_cliente IS 'Cliente dueño del teléfono.';
COMMENT ON COLUMN TELEFONO_CLIENTE.telefono IS 'Número telefónico.';
COMMENT ON COLUMN TELEFONO_CLIENTE.tipo IS 'Tipo de línea.';

COMMENT ON TABLE ESPECIE IS 'Especie animal atendida por la red.';
COMMENT ON COLUMN ESPECIE.id_especie IS 'Identificador de la especie.';
COMMENT ON COLUMN ESPECIE.nombre IS 'Nombre común de la especie.';

COMMENT ON TABLE RAZA IS 'Raza de una especie. Separarla de MASCOTA elimina la dependencia transitiva raza → especie (3FN).';
COMMENT ON COLUMN RAZA.id_raza IS 'Identificador de la raza.';
COMMENT ON COLUMN RAZA.nombre IS 'Nombre de la raza.';
COMMENT ON COLUMN RAZA.tamano IS 'Tamaño típico de la raza.';
COMMENT ON COLUMN RAZA.id_especie IS 'Especie a la que pertenece la raza.';

COMMENT ON TABLE MASCOTA IS 'Paciente de la red. La edad es un atributo derivado de fecha_nacimiento (no se almacena).';
COMMENT ON COLUMN MASCOTA.id_mascota IS 'Identificador de la mascota.';
COMMENT ON COLUMN MASCOTA.nombre IS 'Nombre de la mascota.';
COMMENT ON COLUMN MASCOTA.fecha_nacimiento IS 'Fecha de nacimiento (aproximada si se desconoce).';
COMMENT ON COLUMN MASCOTA.sexo IS 'M = macho, H = hembra.';
COMMENT ON COLUMN MASCOTA.color IS 'Color o patrón del pelaje/plumaje.';
COMMENT ON COLUMN MASCOTA.microchip IS 'Código del microchip de identificación (15 dígitos ISO).';
COMMENT ON COLUMN MASCOTA.esterilizado IS 'S si está esterilizada, N si no.';
COMMENT ON COLUMN MASCOTA.estado IS 'Estado del paciente.';
COMMENT ON COLUMN MASCOTA.id_raza IS 'Raza de la mascota (determina la especie).';
COMMENT ON COLUMN MASCOTA.id_cliente IS 'Cliente propietario.';
COMMENT ON COLUMN MASCOTA.fecha_registro IS 'Fecha de registro en la red.';

COMMENT ON TABLE HISTORIA_CLINICA IS 'Historia clínica única de cada mascota (relación 1:1 TIENE), compartida por todas las sedes.';
COMMENT ON COLUMN HISTORIA_CLINICA.id_historia IS 'Identificador de la historia clínica.';
COMMENT ON COLUMN HISTORIA_CLINICA.numero_historia IS 'Número legible de la historia.';
COMMENT ON COLUMN HISTORIA_CLINICA.id_mascota IS 'Mascota a la que pertenece (UNIQUE: garantiza 1:1).';
COMMENT ON COLUMN HISTORIA_CLINICA.fecha_apertura IS 'Fecha de apertura de la historia.';
COMMENT ON COLUMN HISTORIA_CLINICA.grupo_sanguineo IS 'Grupo sanguíneo si se conoce.';
COMMENT ON COLUMN HISTORIA_CLINICA.alergias IS 'Alergias conocidas.';
COMMENT ON COLUMN HISTORIA_CLINICA.enfermedades_cronicas IS 'Enfermedades crónicas diagnosticadas.';
COMMENT ON COLUMN HISTORIA_CLINICA.observaciones IS 'Notas generales.';

COMMENT ON TABLE SERVICIO IS 'Catálogo de servicios ofrecidos con su precio vigente y duración estimada.';
COMMENT ON COLUMN SERVICIO.id_servicio IS 'Identificador del servicio.';
COMMENT ON COLUMN SERVICIO.codigo IS 'Código interno del servicio.';
COMMENT ON COLUMN SERVICIO.nombre IS 'Nombre del servicio.';
COMMENT ON COLUMN SERVICIO.categoria IS 'Categoría del servicio.';
COMMENT ON COLUMN SERVICIO.precio IS 'Precio vigente en pesos (IVA incluido).';
COMMENT ON COLUMN SERVICIO.duracion_min IS 'Duración estimada en minutos.';

COMMENT ON TABLE CITA IS 'Reserva de atención de una mascota con un veterinario, en una sede, para un servicio.';
COMMENT ON COLUMN CITA.id_cita IS 'Identificador de la cita.';
COMMENT ON COLUMN CITA.fecha_hora IS 'Fecha y hora programada (DATE de Oracle guarda la hora).';
COMMENT ON COLUMN CITA.motivo IS 'Motivo de la cita informado por el cliente.';
COMMENT ON COLUMN CITA.estado IS 'Estado de la cita.';
COMMENT ON COLUMN CITA.id_mascota IS 'Mascota que será atendida.';
COMMENT ON COLUMN CITA.id_veterinario IS 'Veterinario asignado.';
COMMENT ON COLUMN CITA.id_sede IS 'Sede donde se realiza la cita.';
COMMENT ON COLUMN CITA.id_servicio IS 'Servicio solicitado.';

COMMENT ON TABLE CONSULTA IS 'Registro clínico de una cita atendida (relación 1:1 GENERA con CITA).';
COMMENT ON COLUMN CONSULTA.id_consulta IS 'Identificador de la consulta.';
COMMENT ON COLUMN CONSULTA.id_cita IS 'Cita que originó la consulta (UNIQUE: garantiza 1:1).';
COMMENT ON COLUMN CONSULTA.id_auxiliar IS 'Auxiliar que asistió la consulta (opcional).';
COMMENT ON COLUMN CONSULTA.fecha_hora_inicio IS 'Fecha y hora real de inicio de la atención.';
COMMENT ON COLUMN CONSULTA.peso_kg IS 'Peso registrado en kilogramos.';
COMMENT ON COLUMN CONSULTA.temperatura_c IS 'Temperatura corporal en °C.';
COMMENT ON COLUMN CONSULTA.frecuencia_cardiaca IS 'Latidos por minuto.';
COMMENT ON COLUMN CONSULTA.sintomas IS 'Signos y síntomas observados.';
COMMENT ON COLUMN CONSULTA.diagnostico IS 'Diagnóstico del veterinario.';
COMMENT ON COLUMN CONSULTA.observaciones IS 'Indicaciones y notas.';

COMMENT ON TABLE MEDICAMENTO IS 'Catálogo de medicamentos e insumos veterinarios con su precio de venta vigente.';
COMMENT ON COLUMN MEDICAMENTO.id_medicamento IS 'Identificador del medicamento.';
COMMENT ON COLUMN MEDICAMENTO.codigo IS 'Código interno (SKU).';
COMMENT ON COLUMN MEDICAMENTO.nombre IS 'Nombre comercial.';
COMMENT ON COLUMN MEDICAMENTO.principio_activo IS 'Principio activo.';
COMMENT ON COLUMN MEDICAMENTO.presentacion IS 'Forma farmacéutica y concentración.';
COMMENT ON COLUMN MEDICAMENTO.laboratorio IS 'Laboratorio fabricante.';
COMMENT ON COLUMN MEDICAMENTO.precio_venta IS 'Precio de venta unitario vigente (IVA incluido).';
COMMENT ON COLUMN MEDICAMENTO.requiere_formula IS 'S si solo se vende con fórmula médica.';

COMMENT ON TABLE PRESCRIPCION IS 'Resuelve la relación N:M PRESCRIBE entre CONSULTA y MEDICAMENTO (fórmula médica).';
COMMENT ON COLUMN PRESCRIPCION.id_consulta IS 'Consulta en la que se formula.';
COMMENT ON COLUMN PRESCRIPCION.id_medicamento IS 'Medicamento formulado.';
COMMENT ON COLUMN PRESCRIPCION.dosis IS 'Dosis por toma.';
COMMENT ON COLUMN PRESCRIPCION.frecuencia_horas IS 'Cada cuántas horas se administra.';
COMMENT ON COLUMN PRESCRIPCION.duracion_dias IS 'Días de tratamiento.';
COMMENT ON COLUMN PRESCRIPCION.cantidad IS 'Unidades a dispensar.';
COMMENT ON COLUMN PRESCRIPCION.indicaciones IS 'Indicaciones adicionales.';

COMMENT ON TABLE INVENTARIO IS 'Resuelve la relación N:M ALMACENA entre SEDE y MEDICAMENTO: existencias por sede.';
COMMENT ON COLUMN INVENTARIO.id_sede IS 'Sede que almacena.';
COMMENT ON COLUMN INVENTARIO.id_medicamento IS 'Medicamento almacenado.';
COMMENT ON COLUMN INVENTARIO.stock_actual IS 'Unidades disponibles.';
COMMENT ON COLUMN INVENTARIO.stock_minimo IS 'Punto de reorden.';
COMMENT ON COLUMN INVENTARIO.fecha_actualizacion IS 'Fecha del último movimiento o conteo.';

COMMENT ON TABLE FACTURA IS 'Documento de venta emitido a un cliente en una sede. El total es derivado (vista V_FACTURA_TOTAL).';
COMMENT ON COLUMN FACTURA.id_factura IS 'Identificador de la factura.';
COMMENT ON COLUMN FACTURA.numero_factura IS 'Consecutivo legible de facturación.';
COMMENT ON COLUMN FACTURA.fecha_emision IS 'Fecha y hora de emisión.';
COMMENT ON COLUMN FACTURA.metodo_pago IS 'Medio de pago.';
COMMENT ON COLUMN FACTURA.estado IS 'Estado de la factura.';
COMMENT ON COLUMN FACTURA.id_cliente IS 'Cliente al que se factura.';
COMMENT ON COLUMN FACTURA.id_sede IS 'Sede que emite la factura.';
COMMENT ON COLUMN FACTURA.id_empleado IS 'Empleado que emite la factura.';

COMMENT ON TABLE DETALLE_FACTURA IS 'Entidad débil de FACTURA: cada línea cobra un servicio O un medicamento (arco exclusivo).';
COMMENT ON COLUMN DETALLE_FACTURA.id_factura IS 'Factura a la que pertenece la línea (parte de la llave).';
COMMENT ON COLUMN DETALLE_FACTURA.num_linea IS 'Número de línea dentro de la factura (llave parcial).';
COMMENT ON COLUMN DETALLE_FACTURA.id_servicio IS 'Servicio cobrado (excluyente con id_medicamento).';
COMMENT ON COLUMN DETALLE_FACTURA.id_medicamento IS 'Medicamento vendido (excluyente con id_servicio).';
COMMENT ON COLUMN DETALLE_FACTURA.cantidad IS 'Cantidad facturada.';
COMMENT ON COLUMN DETALLE_FACTURA.precio_unitario IS 'Precio aplicado en la fecha de la venta (histórico, no derivado).';
COMMENT ON COLUMN DETALLE_FACTURA.descuento_pct IS 'Porcentaje de descuento aplicado.';
