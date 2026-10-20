-- SIGEVET - Proyecto final Bases de Datos 1
-- Script de creación de tablas y restricciones
-- Oracle Database Free, esquema VETDB (PDB FREEPDB1)

-- DEPARTAMENTO: Departamentos de Colombia donde la red tiene sedes o clientes (codificación DANE).
CREATE TABLE DEPARTAMENTO (
    id_departamento  NUMBER(4) NOT NULL,
    codigo_dane      CHAR(2) NOT NULL,
    nombre           VARCHAR2(60) NOT NULL,
    CONSTRAINT PK_DEPARTAMENTO PRIMARY KEY (id_departamento),
    CONSTRAINT UQ_DEPARTAMENTO_CODIGO_DANE UNIQUE (codigo_dane),
    CONSTRAINT UQ_DEPARTAMENTO_NOMBRE UNIQUE (nombre),
    CONSTRAINT CK_DEPARTAMENTO_CODIGO_DANE CHECK (REGEXP_LIKE(codigo_dane, '^[0-9]{2}$'))
);

-- CIUDAD: Municipios donde se ubican sedes y residen clientes.
CREATE TABLE CIUDAD (
    id_ciudad        NUMBER(6) NOT NULL,
    codigo_dane      CHAR(5) NOT NULL,
    nombre           VARCHAR2(80) NOT NULL,
    id_departamento  NUMBER(4) NOT NULL,
    CONSTRAINT PK_CIUDAD PRIMARY KEY (id_ciudad),
    CONSTRAINT UQ_CIUDAD_CODIGO_DANE UNIQUE (codigo_dane),
    CONSTRAINT UQ_CIUDAD_NOMBRE_DEPARTAMENTO UNIQUE (nombre, id_departamento),
    CONSTRAINT CK_CIUDAD_CODIGO_DANE CHECK (REGEXP_LIKE(codigo_dane, '^[0-9]{5}$')),
    CONSTRAINT FK_CIUDAD_DEPARTAMENTO FOREIGN KEY (id_departamento) REFERENCES DEPARTAMENTO (id_departamento)
);

-- SEDE: Clínica física de la red Huella Sana. Cada sede tiene exactamente un director.
CREATE TABLE SEDE (
    id_sede         NUMBER(4) NOT NULL,
    nombre          VARCHAR2(80) NOT NULL,
    direccion       VARCHAR2(120) NOT NULL,
    telefono        VARCHAR2(20) NOT NULL,
    email           VARCHAR2(100) NOT NULL,
    fecha_apertura  DATE NOT NULL,
    id_ciudad       NUMBER(6) NOT NULL,
    id_director     NUMBER(6),
    CONSTRAINT PK_SEDE PRIMARY KEY (id_sede),
    CONSTRAINT UQ_SEDE_NOMBRE UNIQUE (nombre),
    CONSTRAINT UQ_SEDE_EMAIL UNIQUE (email),
    CONSTRAINT UQ_SEDE_DIRECTOR UNIQUE (id_director),
    CONSTRAINT FK_SEDE_CIUDAD FOREIGN KEY (id_ciudad) REFERENCES CIUDAD (id_ciudad)
);

-- EMPLEADO: Supertipo de todo el personal. El discriminador tipo_empleado indica el subtipo (VET, AUX) o ADM para personal administrativo que no pertenece a ningún subtipo.
CREATE TABLE EMPLEADO (
    id_empleado       NUMBER(6) NOT NULL,
    tipo_documento    VARCHAR2(3) NOT NULL,
    num_documento     VARCHAR2(15) NOT NULL,
    nombres           VARCHAR2(60) NOT NULL,
    apellidos         VARCHAR2(60) NOT NULL,
    fecha_nacimiento  DATE NOT NULL,
    telefono          VARCHAR2(20) NOT NULL,
    email             VARCHAR2(100) NOT NULL,
    tipo_empleado     CHAR(3) NOT NULL,
    fecha_ingreso     DATE NOT NULL,
    salario           NUMBER(12,2) NOT NULL,
    estado            VARCHAR2(10) DEFAULT 'ACTIVO' NOT NULL,
    id_sede           NUMBER(4) NOT NULL,
    CONSTRAINT PK_EMPLEADO PRIMARY KEY (id_empleado),
    CONSTRAINT UQ_EMPLEADO_TIPO_DOCUMENTO_NUM_DOCUMENTO UNIQUE (tipo_documento, num_documento),
    CONSTRAINT UQ_EMPLEADO_EMAIL UNIQUE (email),
    CONSTRAINT CK_EMPLEADO_TIPO_DOCUMENTO CHECK (tipo_documento IN ('CC','CE','PA','PPT')),
    CONSTRAINT CK_EMPLEADO_TIPO_EMPLEADO CHECK (tipo_empleado IN ('VET','AUX','ADM')),
    CONSTRAINT CK_EMPLEADO_SALARIO CHECK (salario > 0),
    CONSTRAINT CK_EMPLEADO_ESTADO CHECK (estado IN ('ACTIVO','INACTIVO')),
    CONSTRAINT FK_EMPLEADO_SEDE FOREIGN KEY (id_sede) REFERENCES SEDE (id_sede)
);

-- VETERINARIO: Subtipo de EMPLEADO: médico veterinario habilitado para atender citas y prescribir.
CREATE TABLE VETERINARIO (
    id_empleado          NUMBER(6) NOT NULL,
    tarjeta_profesional  VARCHAR2(20) NOT NULL,
    universidad          VARCHAR2(100) NOT NULL,
    anio_graduacion      NUMBER(4) NOT NULL,
    CONSTRAINT PK_VETERINARIO PRIMARY KEY (id_empleado),
    CONSTRAINT UQ_VETERINARIO_TARJETA_PROFESIONAL UNIQUE (tarjeta_profesional),
    CONSTRAINT CK_VETERINARIO_ANIO_GRADUACION CHECK (anio_graduacion BETWEEN 1970 AND 2026),
    CONSTRAINT FK_VETERINARIO_EMPLEADO FOREIGN KEY (id_empleado) REFERENCES EMPLEADO (id_empleado) ON DELETE CASCADE
);

-- AUXILIAR: Subtipo de EMPLEADO: auxiliar veterinario que asiste las consultas.
CREATE TABLE AUXILIAR (
    id_empleado    NUMBER(6) NOT NULL,
    area           VARCHAR2(20) NOT NULL,
    certificacion  VARCHAR2(100),
    turno          VARCHAR2(10) NOT NULL,
    CONSTRAINT PK_AUXILIAR PRIMARY KEY (id_empleado),
    CONSTRAINT CK_AUXILIAR_AREA CHECK (area IN ('CLINICA','CIRUGIA','LABORATORIO','ESTETICA','HOSPITALIZACION')),
    CONSTRAINT CK_AUXILIAR_TURNO CHECK (turno IN ('MANANA','TARDE','NOCHE')),
    CONSTRAINT FK_AUXILIAR_EMPLEADO FOREIGN KEY (id_empleado) REFERENCES EMPLEADO (id_empleado) ON DELETE CASCADE
);

-- ESPECIALIDAD: Áreas de especialización médica veterinaria.
CREATE TABLE ESPECIALIDAD (
    id_especialidad  NUMBER(4) NOT NULL,
    nombre           VARCHAR2(60) NOT NULL,
    descripcion      VARCHAR2(200),
    CONSTRAINT PK_ESPECIALIDAD PRIMARY KEY (id_especialidad),
    CONSTRAINT UQ_ESPECIALIDAD_NOMBRE UNIQUE (nombre)
);

-- VETERINARIO_ESPECIALIDAD: Resuelve la relación N:M CERTIFICADO_EN entre VETERINARIO y ESPECIALIDAD.
CREATE TABLE VETERINARIO_ESPECIALIDAD (
    id_empleado          NUMBER(6) NOT NULL,
    id_especialidad      NUMBER(4) NOT NULL,
    fecha_certificacion  DATE NOT NULL,
    CONSTRAINT PK_VETERINARIO_ESPECIALIDAD PRIMARY KEY (id_empleado, id_especialidad),
    CONSTRAINT FK_VETERINARIO_ESPECIALIDAD_VETERINARIO FOREIGN KEY (id_empleado) REFERENCES VETERINARIO (id_empleado) ON DELETE CASCADE,
    CONSTRAINT FK_VETERINARIO_ESPECIALIDAD_ESPECIALIDAD FOREIGN KEY (id_especialidad) REFERENCES ESPECIALIDAD (id_especialidad)
);

-- CLIENTE: Persona propietaria de una o más mascotas; es quien paga los servicios.
CREATE TABLE CLIENTE (
    id_cliente      NUMBER(8) NOT NULL,
    tipo_documento  VARCHAR2(3) NOT NULL,
    num_documento   VARCHAR2(15) NOT NULL,
    nombres         VARCHAR2(60) NOT NULL,
    apellidos       VARCHAR2(60) NOT NULL,
    email           VARCHAR2(100),
    direccion       VARCHAR2(120) NOT NULL,
    id_ciudad       NUMBER(6) NOT NULL,
    fecha_registro  DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT PK_CLIENTE PRIMARY KEY (id_cliente),
    CONSTRAINT UQ_CLIENTE_TIPO_DOCUMENTO_NUM_DOCUMENTO UNIQUE (tipo_documento, num_documento),
    CONSTRAINT UQ_CLIENTE_EMAIL UNIQUE (email),
    CONSTRAINT CK_CLIENTE_TIPO_DOCUMENTO CHECK (tipo_documento IN ('CC','CE','PA','PPT')),
    CONSTRAINT FK_CLIENTE_CIUDAD FOREIGN KEY (id_ciudad) REFERENCES CIUDAD (id_ciudad)
);

-- TELEFONO_CLIENTE: Atributo multivaluado teléfono de CLIENTE llevado a tabla (Primera Forma Normal).
CREATE TABLE TELEFONO_CLIENTE (
    id_cliente  NUMBER(8) NOT NULL,
    telefono    VARCHAR2(20) NOT NULL,
    tipo        VARCHAR2(10) NOT NULL,
    CONSTRAINT PK_TELEFONO_CLIENTE PRIMARY KEY (id_cliente, telefono),
    CONSTRAINT CK_TELEFONO_CLIENTE_TIPO CHECK (tipo IN ('MOVIL','FIJO','TRABAJO')),
    CONSTRAINT FK_TELEFONO_CLIENTE_CLIENTE FOREIGN KEY (id_cliente) REFERENCES CLIENTE (id_cliente) ON DELETE CASCADE
);

-- ESPECIE: Especie animal atendida por la red.
CREATE TABLE ESPECIE (
    id_especie  NUMBER(3) NOT NULL,
    nombre      VARCHAR2(40) NOT NULL,
    CONSTRAINT PK_ESPECIE PRIMARY KEY (id_especie),
    CONSTRAINT UQ_ESPECIE_NOMBRE UNIQUE (nombre)
);

-- RAZA: Raza de una especie. Separarla de MASCOTA elimina la dependencia transitiva raza → especie (3FN).
CREATE TABLE RAZA (
    id_raza     NUMBER(5) NOT NULL,
    nombre      VARCHAR2(60) NOT NULL,
    tamano      VARCHAR2(10) NOT NULL,
    id_especie  NUMBER(3) NOT NULL,
    CONSTRAINT PK_RAZA PRIMARY KEY (id_raza),
    CONSTRAINT UQ_RAZA_NOMBRE_ESPECIE UNIQUE (nombre, id_especie),
    CONSTRAINT CK_RAZA_TAMANO CHECK (tamano IN ('PEQUENO','MEDIANO','GRANDE','NA')),
    CONSTRAINT FK_RAZA_ESPECIE FOREIGN KEY (id_especie) REFERENCES ESPECIE (id_especie)
);

-- MASCOTA: Paciente de la red. La edad es un atributo derivado de fecha_nacimiento (no se almacena).
CREATE TABLE MASCOTA (
    id_mascota        NUMBER(8) NOT NULL,
    nombre            VARCHAR2(40) NOT NULL,
    fecha_nacimiento  DATE,
    sexo              CHAR(1) NOT NULL,
    color             VARCHAR2(40),
    microchip         VARCHAR2(15),
    esterilizado      CHAR(1) DEFAULT 'N' NOT NULL,
    estado            VARCHAR2(10) DEFAULT 'ACTIVA' NOT NULL,
    id_raza           NUMBER(5) NOT NULL,
    id_cliente        NUMBER(8) NOT NULL,
    fecha_registro    DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT PK_MASCOTA PRIMARY KEY (id_mascota),
    CONSTRAINT UQ_MASCOTA_MICROCHIP UNIQUE (microchip),
    CONSTRAINT CK_MASCOTA_SEXO CHECK (sexo IN ('M','H')),
    CONSTRAINT CK_MASCOTA_ESTERILIZADO CHECK (esterilizado IN ('S','N')),
    CONSTRAINT CK_MASCOTA_ESTADO CHECK (estado IN ('ACTIVA','FALLECIDA','TRASLADADA')),
    CONSTRAINT FK_MASCOTA_RAZA FOREIGN KEY (id_raza) REFERENCES RAZA (id_raza),
    CONSTRAINT FK_MASCOTA_CLIENTE FOREIGN KEY (id_cliente) REFERENCES CLIENTE (id_cliente)
);

-- HISTORIA_CLINICA: Historia clínica única de cada mascota (relación 1:1 TIENE), compartida por todas las sedes.
CREATE TABLE HISTORIA_CLINICA (
    id_historia            NUMBER(8) NOT NULL,
    numero_historia        VARCHAR2(12) NOT NULL,
    id_mascota             NUMBER(8) NOT NULL,
    fecha_apertura         DATE NOT NULL,
    grupo_sanguineo        VARCHAR2(10),
    alergias               VARCHAR2(200),
    enfermedades_cronicas  VARCHAR2(200),
    observaciones          VARCHAR2(400),
    CONSTRAINT PK_HISTORIA_CLINICA PRIMARY KEY (id_historia),
    CONSTRAINT UQ_HISTORIA_CLINICA_NUMERO_HISTORIA UNIQUE (numero_historia),
    CONSTRAINT UQ_HISTORIA_CLINICA_MASCOTA UNIQUE (id_mascota),
    CONSTRAINT FK_HISTORIA_CLINICA_MASCOTA FOREIGN KEY (id_mascota) REFERENCES MASCOTA (id_mascota) ON DELETE CASCADE
);

-- SERVICIO: Catálogo de servicios ofrecidos con su precio vigente y duración estimada.
CREATE TABLE SERVICIO (
    id_servicio   NUMBER(4) NOT NULL,
    codigo        VARCHAR2(10) NOT NULL,
    nombre        VARCHAR2(80) NOT NULL,
    categoria     VARCHAR2(20) NOT NULL,
    precio        NUMBER(10,2) NOT NULL,
    duracion_min  NUMBER(4) NOT NULL,
    CONSTRAINT PK_SERVICIO PRIMARY KEY (id_servicio),
    CONSTRAINT UQ_SERVICIO_CODIGO UNIQUE (codigo),
    CONSTRAINT UQ_SERVICIO_NOMBRE UNIQUE (nombre),
    CONSTRAINT CK_SERVICIO_CATEGORIA CHECK (categoria IN ('CONSULTA','VACUNACION','CIRUGIA','LABORATORIO','ESTETICA','IMAGENOLOGIA','HOSPITALIZACION')),
    CONSTRAINT CK_SERVICIO_PRECIO CHECK (precio > 0),
    CONSTRAINT CK_SERVICIO_DURACION_MIN CHECK (duracion_min > 0)
);

-- CITA: Reserva de atención de una mascota con un veterinario, en una sede, para un servicio.
CREATE TABLE CITA (
    id_cita         NUMBER(10) NOT NULL,
    fecha_hora      DATE NOT NULL,
    motivo          VARCHAR2(200),
    estado          VARCHAR2(12) DEFAULT 'PROGRAMADA' NOT NULL,
    id_mascota      NUMBER(8) NOT NULL,
    id_veterinario  NUMBER(6) NOT NULL,
    id_sede         NUMBER(4) NOT NULL,
    id_servicio     NUMBER(4) NOT NULL,
    CONSTRAINT PK_CITA PRIMARY KEY (id_cita),
    CONSTRAINT UQ_CITA_VETERINARIO_FECHA_HORA UNIQUE (id_veterinario, fecha_hora),
    CONSTRAINT CK_CITA_ESTADO CHECK (estado IN ('PROGRAMADA','ATENDIDA','CANCELADA','NO_ASISTIO')),
    CONSTRAINT FK_CITA_MASCOTA FOREIGN KEY (id_mascota) REFERENCES MASCOTA (id_mascota),
    CONSTRAINT FK_CITA_VETERINARIO FOREIGN KEY (id_veterinario) REFERENCES VETERINARIO (id_empleado),
    CONSTRAINT FK_CITA_SEDE FOREIGN KEY (id_sede) REFERENCES SEDE (id_sede),
    CONSTRAINT FK_CITA_SERVICIO FOREIGN KEY (id_servicio) REFERENCES SERVICIO (id_servicio)
);

-- CONSULTA: Registro clínico de una cita atendida (relación 1:1 GENERA con CITA).
CREATE TABLE CONSULTA (
    id_consulta          NUMBER(10) NOT NULL,
    id_cita              NUMBER(10) NOT NULL,
    id_auxiliar          NUMBER(6),
    fecha_hora_inicio    DATE NOT NULL,
    peso_kg              NUMBER(5,2) NOT NULL,
    temperatura_c        NUMBER(4,1),
    frecuencia_cardiaca  NUMBER(3),
    sintomas             VARCHAR2(400),
    diagnostico          VARCHAR2(400) NOT NULL,
    observaciones        VARCHAR2(400),
    CONSTRAINT PK_CONSULTA PRIMARY KEY (id_consulta),
    CONSTRAINT UQ_CONSULTA_CITA UNIQUE (id_cita),
    CONSTRAINT CK_CONSULTA_PESO_KG CHECK (peso_kg > 0),
    CONSTRAINT CK_CONSULTA_TEMPERATURA_C CHECK (temperatura_c BETWEEN 30 AND 45),
    CONSTRAINT CK_CONSULTA_FRECUENCIA_CARDIACA CHECK (frecuencia_cardiaca BETWEEN 20 AND 300),
    CONSTRAINT FK_CONSULTA_CITA FOREIGN KEY (id_cita) REFERENCES CITA (id_cita),
    CONSTRAINT FK_CONSULTA_AUXILIAR FOREIGN KEY (id_auxiliar) REFERENCES AUXILIAR (id_empleado)
);

-- MEDICAMENTO: Catálogo de medicamentos e insumos veterinarios con su precio de venta vigente.
CREATE TABLE MEDICAMENTO (
    id_medicamento    NUMBER(6) NOT NULL,
    codigo            VARCHAR2(12) NOT NULL,
    nombre            VARCHAR2(80) NOT NULL,
    principio_activo  VARCHAR2(80) NOT NULL,
    presentacion      VARCHAR2(40) NOT NULL,
    laboratorio       VARCHAR2(60) NOT NULL,
    precio_venta      NUMBER(10,2) NOT NULL,
    requiere_formula  CHAR(1) DEFAULT 'N' NOT NULL,
    CONSTRAINT PK_MEDICAMENTO PRIMARY KEY (id_medicamento),
    CONSTRAINT UQ_MEDICAMENTO_CODIGO UNIQUE (codigo),
    CONSTRAINT UQ_MEDICAMENTO_NOMBRE_PRESENTACION UNIQUE (nombre, presentacion),
    CONSTRAINT CK_MEDICAMENTO_PRECIO_VENTA CHECK (precio_venta > 0),
    CONSTRAINT CK_MEDICAMENTO_REQUIERE_FORMULA CHECK (requiere_formula IN ('S','N'))
);

-- PRESCRIPCION: Resuelve la relación N:M PRESCRIBE entre CONSULTA y MEDICAMENTO (fórmula médica).
CREATE TABLE PRESCRIPCION (
    id_consulta       NUMBER(10) NOT NULL,
    id_medicamento    NUMBER(6) NOT NULL,
    dosis             VARCHAR2(60) NOT NULL,
    frecuencia_horas  NUMBER(3) NOT NULL,
    duracion_dias     NUMBER(3) NOT NULL,
    cantidad          NUMBER(4) NOT NULL,
    indicaciones      VARCHAR2(200),
    CONSTRAINT PK_PRESCRIPCION PRIMARY KEY (id_consulta, id_medicamento),
    CONSTRAINT CK_PRESCRIPCION_FRECUENCIA_HORAS CHECK (frecuencia_horas > 0),
    CONSTRAINT CK_PRESCRIPCION_DURACION_DIAS CHECK (duracion_dias > 0),
    CONSTRAINT CK_PRESCRIPCION_CANTIDAD CHECK (cantidad > 0),
    CONSTRAINT FK_PRESCRIPCION_CONSULTA FOREIGN KEY (id_consulta) REFERENCES CONSULTA (id_consulta) ON DELETE CASCADE,
    CONSTRAINT FK_PRESCRIPCION_MEDICAMENTO FOREIGN KEY (id_medicamento) REFERENCES MEDICAMENTO (id_medicamento)
);

-- INVENTARIO: Resuelve la relación N:M ALMACENA entre SEDE y MEDICAMENTO: existencias por sede.
CREATE TABLE INVENTARIO (
    id_sede              NUMBER(4) NOT NULL,
    id_medicamento       NUMBER(6) NOT NULL,
    stock_actual         NUMBER(6) NOT NULL,
    stock_minimo         NUMBER(6) NOT NULL,
    fecha_actualizacion  DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT PK_INVENTARIO PRIMARY KEY (id_sede, id_medicamento),
    CONSTRAINT CK_INVENTARIO_STOCK_ACTUAL CHECK (stock_actual >= 0),
    CONSTRAINT CK_INVENTARIO_STOCK_MINIMO CHECK (stock_minimo >= 0),
    CONSTRAINT FK_INVENTARIO_SEDE FOREIGN KEY (id_sede) REFERENCES SEDE (id_sede),
    CONSTRAINT FK_INVENTARIO_MEDICAMENTO FOREIGN KEY (id_medicamento) REFERENCES MEDICAMENTO (id_medicamento)
);

-- FACTURA: Documento de venta emitido a un cliente en una sede. El total es derivado (vista V_FACTURA_TOTAL).
CREATE TABLE FACTURA (
    id_factura      NUMBER(10) NOT NULL,
    numero_factura  VARCHAR2(15) NOT NULL,
    fecha_emision   DATE NOT NULL,
    metodo_pago     VARCHAR2(16) NOT NULL,
    estado          VARCHAR2(10) DEFAULT 'PAGADA' NOT NULL,
    id_cliente      NUMBER(8) NOT NULL,
    id_sede         NUMBER(4) NOT NULL,
    id_empleado     NUMBER(6) NOT NULL,
    CONSTRAINT PK_FACTURA PRIMARY KEY (id_factura),
    CONSTRAINT UQ_FACTURA_NUMERO_FACTURA UNIQUE (numero_factura),
    CONSTRAINT CK_FACTURA_METODO_PAGO CHECK (metodo_pago IN ('EFECTIVO','TARJETA_DEBITO','TARJETA_CREDITO','TRANSFERENCIA')),
    CONSTRAINT CK_FACTURA_ESTADO CHECK (estado IN ('PAGADA','PENDIENTE','ANULADA')),
    CONSTRAINT FK_FACTURA_CLIENTE FOREIGN KEY (id_cliente) REFERENCES CLIENTE (id_cliente),
    CONSTRAINT FK_FACTURA_SEDE FOREIGN KEY (id_sede) REFERENCES SEDE (id_sede),
    CONSTRAINT FK_FACTURA_EMPLEADO FOREIGN KEY (id_empleado) REFERENCES EMPLEADO (id_empleado)
);

-- DETALLE_FACTURA: Entidad débil de FACTURA: cada línea cobra un servicio O un medicamento (arco exclusivo).
CREATE TABLE DETALLE_FACTURA (
    id_factura       NUMBER(10) NOT NULL,
    num_linea        NUMBER(3) NOT NULL,
    id_servicio      NUMBER(4),
    id_medicamento   NUMBER(6),
    cantidad         NUMBER(5) NOT NULL,
    precio_unitario  NUMBER(10,2) NOT NULL,
    descuento_pct    NUMBER(5,2) DEFAULT 0 NOT NULL,
    CONSTRAINT PK_DETALLE_FACTURA PRIMARY KEY (id_factura, num_linea),
    CONSTRAINT CK_DETALLE_FACTURA_NUM_LINEA CHECK (num_linea > 0),
    CONSTRAINT CK_DETALLE_FACTURA_CANTIDAD CHECK (cantidad > 0),
    CONSTRAINT CK_DETALLE_FACTURA_PRECIO_UNITARIO CHECK (precio_unitario >= 0),
    CONSTRAINT CK_DETALLE_FACTURA_DESCUENTO_PCT CHECK (descuento_pct BETWEEN 0 AND 100),
    CONSTRAINT CK_DETALLE_FACTURA_ITEM CHECK ((id_servicio IS NOT NULL AND id_medicamento IS NULL) OR (id_servicio IS NULL AND id_medicamento IS NOT NULL)),
    CONSTRAINT FK_DETALLE_FACTURA_FACTURA FOREIGN KEY (id_factura) REFERENCES FACTURA (id_factura) ON DELETE CASCADE,
    CONSTRAINT FK_DETALLE_FACTURA_SERVICIO FOREIGN KEY (id_servicio) REFERENCES SERVICIO (id_servicio),
    CONSTRAINT FK_DETALLE_FACTURA_MEDICAMENTO FOREIGN KEY (id_medicamento) REFERENCES MEDICAMENTO (id_medicamento)
);

-- Relaciones circulares: se agregan cuando ambas tablas ya existen.
ALTER TABLE SEDE ADD CONSTRAINT FK_SEDE_DIRECTOR FOREIGN KEY (id_director) REFERENCES EMPLEADO (id_empleado);
