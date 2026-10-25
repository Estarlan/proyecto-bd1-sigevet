<!-- Created: 22:45 09-Oct-2026 -->
<!-- Generado por docs/tools/gen_fragmentos.py: no editar a mano -->

## DEPARTAMENTO

**Descripción:** Departamentos de Colombia donde la red tiene sedes o clientes (codificación DANE).  
**Tipo:** Catálogo · **Área:** Organización · **Llave primaria:** id_departamento

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_departamento | INT | No | PK | AUTO_INCREMENT | Identificador del departamento. | 1 |
| codigo_dane | CHAR(2) | No | UK | CHECK codigo_dane REGEXP '^[0-9]{2}$' | Código DANE de dos dígitos. | 11 |
| nombre | VARCHAR(60) | No | UK | — | Nombre oficial del departamento. | Bogotá D.C. |

Table: Diccionario de la tabla DEPARTAMENTO

## CIUDAD

**Descripción:** Municipios donde se ubican sedes y residen clientes.  
**Tipo:** Catálogo · **Área:** Organización · **Llave primaria:** id_ciudad

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_ciudad | INT | No | PK | AUTO_INCREMENT | Identificador de la ciudad. | 1 |
| codigo_dane | CHAR(5) | No | UK | CHECK codigo_dane REGEXP '^[0-9]{5}$' | Código DANE del municipio (5 dígitos). | 11001 |
| nombre | VARCHAR(80) | No | UK | — | Nombre del municipio. | Bogotá D.C. |
| id_departamento | INT | No | FK, UK | — | Departamento al que pertenece la ciudad. | 1 |

Table: Diccionario de la tabla CIUDAD

**Integridad referencial y reglas:** id_departamento → DEPARTAMENTO(id_departamento).

## SEDE

**Descripción:** Clínica física de la red Huella Sana. Cada sede tiene exactamente un director.  
**Tipo:** Fuerte · **Área:** Organización · **Llave primaria:** id_sede

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_sede | INT | No | PK | AUTO_INCREMENT | Identificador de la sede. | 1 |
| nombre | VARCHAR(80) | No | UK | — | Nombre comercial de la sede. | Huella Sana Usaquén |
| direccion | VARCHAR(120) | No | — | — | Dirección física. | Cl. 119 #6-24 |
| telefono | VARCHAR(20) | No | — | — | Teléfono principal de la sede. | 6016123456 |
| email | VARCHAR(100) | No | UK | — | Correo electrónico de la sede. | usaquen@huellasana.co |
| fecha_apertura | DATE | No | — | — | Fecha en que la sede inició operación. | 2019-03-01 |
| id_ciudad | INT | No | FK | — | Ciudad donde se ubica la sede. | 1 |
| id_director | INT | Sí | FK, UK | — | Empleado que dirige la sede (relación 1:1 DIRIGE). | 1 |

Table: Diccionario de la tabla SEDE

**Integridad referencial y reglas:** id_ciudad → CIUDAD(id_ciudad); id_director → EMPLEADO(id_empleado).

## EMPLEADO

**Descripción:** Supertipo de todo el personal. El discriminador tipo_empleado indica el subtipo (VET, AUX) o ADM para personal administrativo que no pertenece a ningún subtipo.  
**Tipo:** Supertipo · **Área:** Organización · **Llave primaria:** id_empleado

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_empleado | INT | No | PK | AUTO_INCREMENT | Identificador del empleado. | 1 |
| tipo_documento | VARCHAR(3) | No | UK | CHECK tipo_documento IN ('CC','CE','PA','PPT') | Tipo de documento de identidad. | CC |
| num_documento | VARCHAR(15) | No | UK | — | Número del documento de identidad. | 1020345678 |
| nombres | VARCHAR(60) | No | — | — | Nombres del empleado (parte del atributo compuesto nombre_completo). | Laura Marcela |
| apellidos | VARCHAR(60) | No | — | — | Apellidos del empleado. | Gómez Rincón |
| fecha_nacimiento | DATE | No | — | — | Fecha de nacimiento. | 1990-05-14 |
| telefono | VARCHAR(20) | No | — | — | Teléfono de contacto. | 3104567890 |
| email | VARCHAR(100) | No | UK | — | Correo corporativo. | lgomez@huellasana.co |
| tipo_empleado | CHAR(3) | No | — | CHECK tipo_empleado IN ('VET','AUX','ADM') | Discriminador de la especialización: VET, AUX o ADM. | VET |
| fecha_ingreso | DATE | No | — | — | Fecha de vinculación a la red. | 2021-02-01 |
| salario | DECIMAL(12,2) | No | — | CHECK salario > 0 | Salario mensual en pesos colombianos. | 4800000 |
| estado | VARCHAR(10) | No | — | DEFAULT 'ACTIVO'; CHECK estado IN ('ACTIVO','INACTIVO') | Estado laboral. | ACTIVO |
| id_sede | INT | No | FK | — | Sede donde labora el empleado (relación LABORA_EN). | 1 |

Table: Diccionario de la tabla EMPLEADO

**Integridad referencial y reglas:** id_sede → SEDE(id_sede).

## VETERINARIO

**Descripción:** Subtipo de EMPLEADO: médico veterinario habilitado para atender citas y prescribir.  
**Tipo:** Subtipo · **Área:** Organización · **Llave primaria:** id_empleado

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_empleado | INT | No | PK, FK | — | Identificador heredado del supertipo EMPLEADO. | 1 |
| tarjeta_profesional | VARCHAR(20) | No | UK | — | Número de tarjeta profesional (COMVEZCOL). | TP-25410 |
| universidad | VARCHAR(100) | No | — | — | Universidad que otorgó el título. | Universidad Nacional de Colombia |
| anio_graduacion | INT | No | — | CHECK anio_graduacion BETWEEN 1970 AND 2026 | Año de graduación. | 2014 |

Table: Diccionario de la tabla VETERINARIO

**Integridad referencial y reglas:** id_empleado → EMPLEADO(id_empleado).

## AUXILIAR

**Descripción:** Subtipo de EMPLEADO: auxiliar veterinario que asiste las consultas.  
**Tipo:** Subtipo · **Área:** Organización · **Llave primaria:** id_empleado

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_empleado | INT | No | PK, FK | — | Identificador heredado del supertipo EMPLEADO. | 10 |
| area | VARCHAR(20) | No | — | CHECK area IN ('CLINICA','CIRUGIA','LABORATORIO','ESTETICA','HOSPITALIZACION') | Área de desempeño. | CLINICA |
| certificacion | VARCHAR(100) | Sí | — | — | Título técnico o tecnológico. | Técnico en asistencia veterinaria |
| turno | VARCHAR(10) | No | — | CHECK turno IN ('MANANA','TARDE','NOCHE') | Turno habitual. | MANANA |

Table: Diccionario de la tabla AUXILIAR

**Integridad referencial y reglas:** id_empleado → EMPLEADO(id_empleado).

## ESPECIALIDAD

**Descripción:** Áreas de especialización médica veterinaria.  
**Tipo:** Catálogo · **Área:** Organización · **Llave primaria:** id_especialidad

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_especialidad | INT | No | PK | AUTO_INCREMENT | Identificador de la especialidad. | 1 |
| nombre | VARCHAR(60) | No | UK | — | Nombre de la especialidad. | Cirugía |
| descripcion | VARCHAR(200) | Sí | — | — | Descripción breve. | Procedimientos quirúrgicos de tejidos blandos y ortopedia |

Table: Diccionario de la tabla ESPECIALIDAD

## VETERINARIO_ESPECIALIDAD

**Descripción:** Resuelve la relación N:M CERTIFICADO_EN entre VETERINARIO y ESPECIALIDAD.  
**Tipo:** Asociativa (N:M) · **Área:** Organización · **Llave primaria:** id_empleado, id_especialidad

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_empleado | INT | No | PK, FK | — | Veterinario certificado. | 1 |
| id_especialidad | INT | No | PK, FK | — | Especialidad certificada. | 2 |
| fecha_certificacion | DATE | No | — | — | Fecha en que obtuvo la certificación. | 2018-11-30 |

Table: Diccionario de la tabla VETERINARIO_ESPECIALIDAD

**Integridad referencial y reglas:** id_empleado → VETERINARIO(id_empleado); id_especialidad → ESPECIALIDAD(id_especialidad).

## CLIENTE

**Descripción:** Persona propietaria de una o más mascotas; es quien paga los servicios.  
**Tipo:** Fuerte · **Área:** Clínica · **Llave primaria:** id_cliente

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_cliente | INT | No | PK | AUTO_INCREMENT | Identificador del cliente. | 1 |
| tipo_documento | VARCHAR(3) | No | UK | CHECK tipo_documento IN ('CC','CE','PA','PPT') | Tipo de documento de identidad. | CC |
| num_documento | VARCHAR(15) | No | UK | — | Número del documento de identidad. | 52987654 |
| nombres | VARCHAR(60) | No | — | — | Nombres del cliente. | Andrés Felipe |
| apellidos | VARCHAR(60) | No | — | — | Apellidos del cliente. | Martínez Ruiz |
| email | VARCHAR(100) | Sí | UK | — | Correo electrónico. | afmartinez@correo.com |
| direccion | VARCHAR(120) | No | — | — | Dirección de residencia. | Cra. 15 #93-60 Apto 402 |
| id_ciudad | INT | No | FK | — | Ciudad de residencia. | 1 |
| fecha_registro | DATETIME | No | — | DEFAULT CURRENT_TIMESTAMP | Fecha de registro en el sistema. | 2025-01-15 |

Table: Diccionario de la tabla CLIENTE

**Integridad referencial y reglas:** id_ciudad → CIUDAD(id_ciudad).

## TELEFONO_CLIENTE

**Descripción:** Atributo multivaluado teléfono de CLIENTE llevado a tabla (Primera Forma Normal).  
**Tipo:** Multivaluado · **Área:** Clínica · **Llave primaria:** id_cliente, telefono

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_cliente | INT | No | PK, FK | — | Cliente dueño del teléfono. | 1 |
| telefono | VARCHAR(20) | No | PK | — | Número telefónico. | 3157894561 |
| tipo | VARCHAR(10) | No | — | CHECK tipo IN ('MOVIL','FIJO','TRABAJO') | Tipo de línea. | MOVIL |

Table: Diccionario de la tabla TELEFONO_CLIENTE

**Integridad referencial y reglas:** id_cliente → CLIENTE(id_cliente).

## ESPECIE

**Descripción:** Especie animal atendida por la red.  
**Tipo:** Catálogo · **Área:** Clínica · **Llave primaria:** id_especie

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_especie | INT | No | PK | AUTO_INCREMENT | Identificador de la especie. | 1 |
| nombre | VARCHAR(40) | No | UK | — | Nombre común de la especie. | Canino |

Table: Diccionario de la tabla ESPECIE

## RAZA

**Descripción:** Raza de una especie. Separarla de MASCOTA elimina la dependencia transitiva raza → especie (3FN).  
**Tipo:** Catálogo · **Área:** Clínica · **Llave primaria:** id_raza

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_raza | INT | No | PK | AUTO_INCREMENT | Identificador de la raza. | 1 |
| nombre | VARCHAR(60) | No | UK | — | Nombre de la raza. | Labrador Retriever |
| tamano | VARCHAR(10) | No | — | CHECK tamano IN ('PEQUENO','MEDIANO','GRANDE','NA') | Tamaño típico de la raza. | GRANDE |
| id_especie | INT | No | FK, UK | — | Especie a la que pertenece la raza. | 1 |

Table: Diccionario de la tabla RAZA

**Integridad referencial y reglas:** id_especie → ESPECIE(id_especie).

## MASCOTA

**Descripción:** Paciente de la red. La edad es un atributo derivado de fecha_nacimiento (no se almacena).  
**Tipo:** Fuerte · **Área:** Clínica · **Llave primaria:** id_mascota

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_mascota | INT | No | PK | AUTO_INCREMENT | Identificador de la mascota. | 1 |
| nombre | VARCHAR(40) | No | — | — | Nombre de la mascota. | Toby |
| fecha_nacimiento | DATE | Sí | — | — | Fecha de nacimiento (aproximada si se desconoce). | 2020-06-10 |
| sexo | CHAR(1) | No | — | CHECK sexo IN ('M','H') | M = macho, H = hembra. | M |
| color | VARCHAR(40) | Sí | — | — | Color o patrón del pelaje/plumaje. | Dorado |
| microchip | VARCHAR(15) | Sí | UK | — | Código del microchip de identificación (15 dígitos ISO). | 985112003456789 |
| esterilizado | CHAR(1) | No | — | DEFAULT 'N'; CHECK esterilizado IN ('S','N') | S si está esterilizada, N si no. | S |
| estado | VARCHAR(10) | No | — | DEFAULT 'ACTIVA'; CHECK estado IN ('ACTIVA','FALLECIDA','TRASLADADA') | Estado del paciente. | ACTIVA |
| id_raza | INT | No | FK | — | Raza de la mascota (determina la especie). | 1 |
| id_cliente | INT | No | FK | — | Cliente propietario. | 1 |
| fecha_registro | DATETIME | No | — | DEFAULT CURRENT_TIMESTAMP | Fecha de registro en la red. | 2025-01-15 |

Table: Diccionario de la tabla MASCOTA

**Integridad referencial y reglas:** id_raza → RAZA(id_raza); id_cliente → CLIENTE(id_cliente).

## HISTORIA_CLINICA

**Descripción:** Historia clínica única de cada mascota (relación 1:1 TIENE), compartida por todas las sedes.  
**Tipo:** Fuerte · **Área:** Clínica · **Llave primaria:** id_historia

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_historia | INT | No | PK | AUTO_INCREMENT | Identificador de la historia clínica. | 1 |
| numero_historia | VARCHAR(12) | No | UK | — | Número legible de la historia. | HC-000001 |
| id_mascota | INT | No | FK, UK | — | Mascota a la que pertenece (UNIQUE: garantiza 1:1). | 1 |
| fecha_apertura | DATE | No | — | — | Fecha de apertura de la historia. | 2025-01-15 |
| grupo_sanguineo | VARCHAR(10) | Sí | — | — | Grupo sanguíneo si se conoce. | DEA 1.1+ |
| alergias | VARCHAR(200) | Sí | — | — | Alergias conocidas. | Penicilina |
| enfermedades_cronicas | VARCHAR(200) | Sí | — | — | Enfermedades crónicas diagnosticadas. | Dermatitis atópica |
| observaciones | VARCHAR(400) | Sí | — | — | Notas generales. | Paciente nervioso, usar bozal |

Table: Diccionario de la tabla HISTORIA_CLINICA

**Integridad referencial y reglas:** id_mascota → MASCOTA(id_mascota).

## SERVICIO

**Descripción:** Catálogo de servicios ofrecidos con su precio vigente y duración estimada.  
**Tipo:** Catálogo · **Área:** Clínica · **Llave primaria:** id_servicio

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_servicio | INT | No | PK | AUTO_INCREMENT | Identificador del servicio. | 1 |
| codigo | VARCHAR(10) | No | UK | — | Código interno del servicio. | SRV-CON01 |
| nombre | VARCHAR(80) | No | UK | — | Nombre del servicio. | Consulta general |
| categoria | VARCHAR(20) | No | — | CHECK categoria IN ('CONSULTA','VACUNACION','CIRUGIA','LABORATORIO','ESTETICA','IMAGENOLOGIA','HOSPITALIZACION') | Categoría del servicio. | CONSULTA |
| precio | DECIMAL(10,2) | No | — | CHECK precio > 0 | Precio vigente en pesos (IVA incluido). | 65000 |
| duracion_min | INT | No | — | CHECK duracion_min > 0 | Duración estimada en minutos. | 30 |

Table: Diccionario de la tabla SERVICIO

## CITA

**Descripción:** Reserva de atención de una mascota con un veterinario, en una sede, para un servicio.  
**Tipo:** Fuerte · **Área:** Clínica · **Llave primaria:** id_cita

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_cita | BIGINT | No | PK | AUTO_INCREMENT | Identificador de la cita. | 1 |
| fecha_hora | DATETIME | No | UK | — | Fecha y hora programada de la cita. | 2026-03-12 09:30 |
| motivo | VARCHAR(200) | Sí | — | — | Motivo de la cita informado por el cliente. | Vómito desde ayer |
| estado | VARCHAR(12) | No | — | DEFAULT 'PROGRAMADA'; CHECK estado IN ('PROGRAMADA','ATENDIDA','CANCELADA','NO_ASISTIO') | Estado de la cita. | ATENDIDA |
| id_mascota | INT | No | FK | — | Mascota que será atendida. | 1 |
| id_veterinario | INT | No | FK, UK | — | Veterinario asignado. | 1 |
| id_sede | INT | No | FK | — | Sede donde se realiza la cita. | 1 |
| id_servicio | INT | No | FK | — | Servicio solicitado. | 1 |

Table: Diccionario de la tabla CITA

**Integridad referencial y reglas:** id_mascota → MASCOTA(id_mascota); id_veterinario → VETERINARIO(id_empleado); id_sede → SEDE(id_sede); id_servicio → SERVICIO(id_servicio).

## CONSULTA

**Descripción:** Registro clínico de una cita atendida (relación 1:1 GENERA con CITA).  
**Tipo:** Fuerte · **Área:** Clínica · **Llave primaria:** id_consulta

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_consulta | BIGINT | No | PK | AUTO_INCREMENT | Identificador de la consulta. | 1 |
| id_cita | BIGINT | No | FK, UK | — | Cita que originó la consulta (UNIQUE: garantiza 1:1). | 1 |
| id_auxiliar | INT | Sí | FK | — | Auxiliar que asistió la consulta (opcional). | 10 |
| fecha_hora_inicio | DATETIME | No | — | — | Fecha y hora real de inicio de la atención. | 2026-03-12 09:35 |
| peso_kg | DECIMAL(5,2) | No | — | CHECK peso_kg > 0 | Peso registrado en kilogramos. | 28.40 |
| temperatura_c | DECIMAL(4,1) | Sí | — | CHECK temperatura_c BETWEEN 30 AND 45 | Temperatura corporal en °C. | 38.6 |
| frecuencia_cardiaca | INT | Sí | — | CHECK frecuencia_cardiaca BETWEEN 20 AND 300 | Latidos por minuto. | 96 |
| sintomas | VARCHAR(400) | Sí | — | — | Signos y síntomas observados. | Vómito, decaimiento |
| diagnostico | VARCHAR(400) | No | — | — | Diagnóstico del veterinario. | Gastroenteritis aguda |
| observaciones | VARCHAR(400) | Sí | — | — | Indicaciones y notas. | Dieta blanda 3 días |

Table: Diccionario de la tabla CONSULTA

**Integridad referencial y reglas:** id_cita → CITA(id_cita); id_auxiliar → AUXILIAR(id_empleado).

## MEDICAMENTO

**Descripción:** Catálogo de medicamentos e insumos veterinarios con su precio de venta vigente.  
**Tipo:** Catálogo · **Área:** Comercial · **Llave primaria:** id_medicamento

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_medicamento | INT | No | PK | AUTO_INCREMENT | Identificador del medicamento. | 1 |
| codigo | VARCHAR(12) | No | UK | — | Código interno (SKU). | MED-0001 |
| nombre | VARCHAR(80) | No | UK | — | Nombre comercial. | Amoxicilina 500 |
| principio_activo | VARCHAR(80) | No | — | — | Principio activo. | Amoxicilina |
| presentacion | VARCHAR(40) | No | UK | — | Forma farmacéutica y concentración. | Tableta 500 mg |
| laboratorio | VARCHAR(60) | No | — | — | Laboratorio fabricante. | Lab. Andino Vet |
| precio_venta | DECIMAL(10,2) | No | — | CHECK precio_venta > 0 | Precio de venta unitario vigente (IVA incluido). | 1800 |
| requiere_formula | CHAR(1) | No | — | DEFAULT 'N'; CHECK requiere_formula IN ('S','N') | S si solo se vende con fórmula médica. | S |

Table: Diccionario de la tabla MEDICAMENTO

## PRESCRIPCION

**Descripción:** Resuelve la relación N:M PRESCRIBE entre CONSULTA y MEDICAMENTO (fórmula médica).  
**Tipo:** Asociativa (N:M) · **Área:** Comercial · **Llave primaria:** id_consulta, id_medicamento

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_consulta | BIGINT | No | PK, FK | — | Consulta en la que se formula. | 1 |
| id_medicamento | INT | No | PK, FK | — | Medicamento formulado. | 1 |
| dosis | VARCHAR(60) | No | — | — | Dosis por toma. | 1 tableta |
| frecuencia_horas | INT | No | — | CHECK frecuencia_horas > 0 | Cada cuántas horas se administra. | 12 |
| duracion_dias | INT | No | — | CHECK duracion_dias > 0 | Días de tratamiento. | 7 |
| cantidad | INT | No | — | CHECK cantidad > 0 | Unidades a dispensar. | 14 |
| indicaciones | VARCHAR(200) | Sí | — | — | Indicaciones adicionales. | Administrar con comida |

Table: Diccionario de la tabla PRESCRIPCION

**Integridad referencial y reglas:** id_consulta → CONSULTA(id_consulta); id_medicamento → MEDICAMENTO(id_medicamento).

## INVENTARIO

**Descripción:** Resuelve la relación N:M ALMACENA entre SEDE y MEDICAMENTO: existencias por sede.  
**Tipo:** Asociativa (N:M) · **Área:** Comercial · **Llave primaria:** id_sede, id_medicamento

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_sede | INT | No | PK, FK | — | Sede que almacena. | 1 |
| id_medicamento | INT | No | PK, FK | — | Medicamento almacenado. | 1 |
| stock_actual | INT | No | — | CHECK stock_actual >= 0 | Unidades disponibles. | 120 |
| stock_minimo | INT | No | — | CHECK stock_minimo >= 0 | Punto de reorden. | 30 |
| fecha_actualizacion | DATETIME | No | — | DEFAULT CURRENT_TIMESTAMP | Fecha del último movimiento o conteo. | 2026-09-30 |

Table: Diccionario de la tabla INVENTARIO

**Integridad referencial y reglas:** id_sede → SEDE(id_sede); id_medicamento → MEDICAMENTO(id_medicamento).

## FACTURA

**Descripción:** Documento de venta emitido a un cliente en una sede. El total es derivado (vista V_FACTURA_TOTAL).  
**Tipo:** Fuerte · **Área:** Comercial · **Llave primaria:** id_factura

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_factura | BIGINT | No | PK | AUTO_INCREMENT | Identificador de la factura. | 1 |
| numero_factura | VARCHAR(15) | No | UK | — | Consecutivo legible de facturación. | FV-000001 |
| fecha_emision | DATETIME | No | — | — | Fecha y hora de emisión. | 2026-03-12 10:20 |
| metodo_pago | VARCHAR(16) | No | — | CHECK metodo_pago IN ('EFECTIVO','TARJETA_DEBITO','TARJETA_CREDITO','TRANSFERENCIA') | Medio de pago. | TARJETA_DEBITO |
| estado | VARCHAR(10) | No | — | DEFAULT 'PAGADA'; CHECK estado IN ('PAGADA','PENDIENTE','ANULADA') | Estado de la factura. | PAGADA |
| id_cliente | INT | No | FK | — | Cliente al que se factura. | 1 |
| id_sede | INT | No | FK | — | Sede que emite la factura. | 1 |
| id_empleado | INT | No | FK | — | Empleado que emite la factura. | 20 |

Table: Diccionario de la tabla FACTURA

**Integridad referencial y reglas:** id_cliente → CLIENTE(id_cliente); id_sede → SEDE(id_sede); id_empleado → EMPLEADO(id_empleado).

## DETALLE_FACTURA

**Descripción:** Entidad débil de FACTURA: cada línea cobra un servicio O un medicamento (arco exclusivo).  
**Tipo:** Débil · **Área:** Comercial · **Llave primaria:** id_factura, num_linea

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_factura | BIGINT | No | PK, FK | — | Factura a la que pertenece la línea (parte de la llave). | 1 |
| num_linea | INT | No | PK | CHECK num_linea > 0 | Número de línea dentro de la factura (llave parcial). | 1 |
| id_servicio | INT | Sí | FK | — | Servicio cobrado (excluyente con id_medicamento). | 1 |
| id_medicamento | INT | Sí | FK | — | Medicamento vendido (excluyente con id_servicio). | — |
| cantidad | INT | No | — | CHECK cantidad > 0 | Cantidad facturada. | 1 |
| precio_unitario | DECIMAL(10,2) | No | — | CHECK precio_unitario >= 0 | Precio aplicado en la fecha de la venta (histórico, no derivado). | 65000 |
| descuento_pct | DECIMAL(5,2) | No | — | DEFAULT 0; CHECK descuento_pct BETWEEN 0 AND 100 | Porcentaje de descuento aplicado. | 0 |

Table: Diccionario de la tabla DETALLE_FACTURA

**Integridad referencial y reglas:** id_factura → FACTURA(id_factura); id_servicio → SERVICIO(id_servicio); id_medicamento → MEDICAMENTO(id_medicamento); CHECK (id_servicio IS NOT NULL AND id_medicamento IS NULL) OR (id_servicio IS NULL AND id_medicamento IS NOT NULL).
