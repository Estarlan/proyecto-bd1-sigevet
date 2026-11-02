// Interfaces con los mismos nombres de campo que el JSON de la API de SIGEVET.
// Jackson arma el JSON a partir de los getters de las clases Java (getNombres() → "nombres"),
// así que cada interfaz corresponde a una clase del paquete model o dto del backend.
// Los valores DECIMAL de MySQL (BigDecimal en Java) llegan como number; las fechas llegan como texto.

// ─────────────────────────────────────────────
// PERSONAS (herencia: Persona → Cliente / Empleado → Veterinario / Auxiliar)
// ─────────────────────────────────────────────
/** Datos comunes de toda persona; de aquí heredan Cliente y Empleado (tabla PERSONA). */
export interface Persona {
  idPersona: number;
  tipoDocumento?: string;
  numDocumento?: string;
  nombres?: string;
  apellidos?: string;
  nombreCompleto?: string;
  email?: string;
  /** Texto que arma cada subclase en Java con su propio getDescripcion() (polimorfismo). */
  descripcion?: string;
}

/** Propietario de mascotas: una Persona con dirección, ciudad y fecha de registro. */
export interface Cliente extends Persona {
  direccion?: string;
  ciudad?: string;
  departamento?: string;
  fechaRegistro?: string;
}

/** Trabajador de una sede: una Persona con su estado, sede y los campos de su subtipo (VET, AUX o ADM). */
export interface Empleado extends Persona {
  telefono?: string;
  estado?: string;
  sede?: string;
  /** Discriminador de la especialización: VET, AUX o ADM. */
  tipoEmpleado?: string;
  fechaIngreso?: string;
  salario?: number;
  // Campos que solo llegan cuando el empleado es un Veterinario
  tarjetaProfesional?: string;
  universidad?: string;
  especialidades?: string[];
  // Campos que solo llegan cuando el empleado es un Auxiliar
  area?: string;
  turno?: string;
}

/** Un teléfono de un cliente (atributo multivaluado, tabla TELEFONO_CLIENTE). */
export interface Telefono {
  telefono?: string;
  tipo?: string;
}

// ─────────────────────────────────────────────
// MASCOTAS Y ATENCIÓN CLÍNICA
// ─────────────────────────────────────────────
/** Una mascota con su raza, especie y propietario. */
export interface Mascota {
  idMascota: number;
  idCliente?: number;
  nombre?: string;
  fechaNacimiento?: string;
  sexo?: string;
  color?: string;
  microchip?: string;
  esterilizado?: string;
  estado?: string;
  raza?: string;
  especie?: string;
  cliente?: string;
}

/** Historia clínica de una mascota (relación 1:1 con MASCOTA). */
export interface HistoriaClinica {
  idHistoria: number;
  numeroHistoria?: string;
  fechaApertura?: string;
  grupoSanguineo?: string;
  alergias?: string;
  enfermedadesCronicas?: string;
  observaciones?: string;
}

/** Medicamento recetado en una consulta, con su dosis y duración. */
export interface Prescripcion {
  idConsulta: number;
  medicamento?: string;
  presentacion?: string;
  dosis?: string;
  frecuenciaHoras?: number;
  duracionDias?: number;
  cantidad?: number;
  indicaciones?: string;
}

/** Atención de un veterinario a una mascota, con sus prescripciones. */
export interface Consulta {
  idConsulta: number;
  fechaHoraInicio?: string;
  pesoKg?: number;
  temperaturaC?: number;
  frecuenciaCardiaca?: string;
  sintomas?: string;
  diagnostico?: string;
  observaciones?: string;
  veterinario?: string;
  sede?: string;
  prescripciones?: Prescripcion[];
}

/** Cita agendada para una mascota; su estado es PROGRAMADA, ATENDIDA, CANCELADA o NO_ASISTIO. */
export interface Cita {
  idCita: number;
  idMascota?: number;
  fechaHora?: string;
  motivo?: string;
  estado?: string;
  mascota?: string;
  veterinario?: string;
  sede?: string;
  servicio?: string;
}

// ─────────────────────────────────────────────
// SEDES, INVENTARIO Y FACTURACIÓN
// ─────────────────────────────────────────────
/** Una clínica de la red, con su dirección, teléfono y director. */
export interface Sede {
  idSede: number;
  nombre?: string;
  direccion?: string;
  telefono?: string;
  director?: string;
}

/** Existencias de un medicamento en una sede; la llave es (idSede, idMedicamento). */
export interface ItemInventario {
  idSede: number;
  idMedicamento: number;
  sede?: string;
  medicamento?: string;
  presentacion?: string;
  stockActual?: number;
  stockMinimo?: number;
  fechaActualizacion?: string;
  /** Viene de isStockBajo() en Java: true si el stock actual está por debajo del mínimo. */
  stockBajo?: boolean;
}

/** Cabecera de una factura; el total se calcula sumando sus líneas. */
export interface Factura {
  idFactura: number;
  idCliente?: number;
  numeroFactura?: string;
  fechaEmision?: string;
  metodoPago?: string;
  estado?: string;
  cliente?: string;
  sede?: string;
  total?: number;
}

/** Una línea de factura (entidad débil de FACTURA): cobra un servicio o un medicamento. */
export interface DetalleFactura {
  numLinea: number;
  concepto?: string;
  cantidad?: number;
  precioUnitario?: number;
  descuentoPct?: number;
  subtotal?: number;
}

// ─────────────────────────────────────────────
// INICIO Y ÁLGEBRA RELACIONAL
// ─────────────────────────────────────────────
/** Par nombre-total que usa la página de inicio (conteos, ingresos y estados de citas). */
export interface Resumen {
  nombre?: string;
  total?: number;
}

/** Un ejercicio de álgebra relacional con su enunciado y su consulta SQL. */
export interface EjercicioAlgebra {
  numero: number;
  titulo?: string;
  grupo?: string;
  enunciado?: string;
  sql?: string;
}

/** Resultado de ejecutar una consulta: nombres de columnas y filas de texto. */
export interface ResultadoConsulta {
  columnas: string[];
  filas: string[][];
}

// ─────────────────────────────────────────────
// DTO: respuestas que juntan varios datos
// ─────────────────────────────────────────────
/** Respuesta de GET /api/clientes/:id: el cliente con sus teléfonos, mascotas y facturas. */
export interface ClienteDetalle {
  cliente: Cliente;
  telefonos: Telefono[];
  mascotas: Mascota[];
  facturas: Factura[];
}

/** Respuesta de GET /api/mascotas/:id: la mascota con su historia clínica y sus consultas. */
export interface MascotaDetalle {
  mascota: Mascota;
  historia?: HistoriaClinica;
  consultas: Consulta[];
}

/** Respuesta de GET /api/facturas/:id: la factura con sus líneas. */
export interface FacturaDetalle {
  factura: Factura;
  lineas: DetalleFactura[];
}

/** Respuesta de GET /api/inicio: los datos de la página de inicio. */
export interface Inicio {
  conteos: Resumen[];
  ingresos: Resumen[];
  estadosCitas: Resumen[];
  stockBajo: ItemInventario[];
  proximas: Cita[];
}

/** Respuesta de GET /api/algebra: los grupos y la lista de ejercicios. */
export interface AlgebraLista {
  grupos: string[];
  ejercicios: EjercicioAlgebra[];
}

/** Respuesta de GET /api/algebra/:numero: el ejercicio y el resultado de ejecutarlo. */
export interface EjercicioDetalle {
  ejercicio: EjercicioAlgebra;
  resultado: ResultadoConsulta;
}

/** Cuerpo JSON que envía ManejadorErroresApi cuando algo falla (404, 400 o 500). */
export interface ErrorApi {
  estado: number;
  mensaje: string;
}

// ─────────────────────────────────────────────
// FORMULARIOS (lo que se envía en POST y PUT)
// Los nombres coinciden con los setters de los *FormDTO del backend.
// ─────────────────────────────────────────────

/** Opción de un <select>: ciudad, raza, servicio, medicamento, especialidad o empleado. */
export interface OpcionCatalogo {
  id: number;
  nombre: string;
  detalle?: string;
  /** Precio del servicio o del medicamento, cuando aplica. */
  valor?: number;
}

/** Teléfono dentro del formulario de un cliente. */
export interface TelefonoForm {
  telefono: string;
  tipo: string;
}

/** Cuerpo de POST y PUT /api/clientes. */
export interface ClienteForm {
  tipoDocumento: string;
  numDocumento: string;
  nombres: string;
  apellidos: string;
  email: string | null;
  direccion: string;
  idCiudad: number | null;
  telefonos: TelefonoForm[];
}

/** Cuerpo de POST y PUT /api/mascotas: la mascota junto con su historia clínica. */
export interface MascotaForm {
  nombre: string;
  fechaNacimiento: string | null;
  sexo: string;
  color: string | null;
  microchip: string | null;
  esterilizado: string;
  idRaza: number | null;
  idCliente: number | null;
  /** Solo lectura: lo envía el backend al editar. */
  estado?: string;
  // historia clínica (relación 1:1)
  grupoSanguineo: string | null;
  alergias: string | null;
  enfermedadesCronicas: string | null;
  observaciones: string | null;
}

/** Cuerpo de POST /api/citas. */
export interface CitaForm {
  idMascota: number | null;
  idServicio: number | null;
  idSede: number | null;
  idVeterinario: number | null;
  /** Formato aaaa-mm-ddThh:mm (input datetime-local). */
  fechaHora: string;
  motivo: string | null;
}

/** Línea dentro del formulario de factura: idServicio o idMedicamento (uno de los dos, arco exclusivo). */
export interface LineaFacturaForm {
  idServicio: number | null;
  idMedicamento: number | null;
  cantidad: number;
  descuentoPct: number;
}

/** Cuerpo de POST /api/facturas. */
export interface FacturaForm {
  idCliente: number | null;
  idSede: number | null;
  idEmpleado: number | null;
  metodoPago: string;
  estado: string;
  lineas: LineaFacturaForm[];
}

/** Cuerpo de POST y PUT /api/inventario. */
export interface InventarioForm {
  idSede: number | null;
  idMedicamento: number | null;
  stockActual: number | null;
  stockMinimo: number | null;
}

/** Cuerpo de POST y PUT /api/personal; los campos VET y AUX solo se usan según el tipo de empleado. */
export interface EmpleadoForm {
  tipoDocumento: string;
  numDocumento: string;
  nombres: string;
  apellidos: string;
  fechaNacimiento: string;
  telefono: string;
  email: string;
  tipoEmpleado: string;
  fechaIngreso: string;
  salario: number | null;
  idSede: number | null;
  estado?: string;
  // VET
  tarjetaProfesional: string | null;
  universidad: string | null;
  anioGraduacion: number | null;
  especialidades: number[];
  // AUX
  area: string | null;
  certificacion: string | null;
  turno: string | null;
}

/** Respuesta de un POST: el id que generó AUTO_INCREMENT. */
export interface IdRespuesta {
  id: number;
}

/** Respuesta con un mensaje para mostrar (por ejemplo al cambiar el estado de una mascota). */
export interface Mensaje {
  mensaje: string;
}

/** Empleado que inició sesión (GET y POST /api/sesion). */
export interface Sesion {
  idEmpleado: number;
  nombreCompleto: string;
  cargo: string;
  sede: string;
}

/** Formulario de inicio de sesión: correo corporativo y número de documento del empleado. */
export interface LoginForm {
  email: string;
  documento: string;
}
