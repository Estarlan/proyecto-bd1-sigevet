// Interfaces con los mismos nombres de campo que el JSON de la API de SIGEVET.
// Jackson arma el JSON a partir de los getters de las clases Java (getNombres() → "nombres"),
// así que cada interfaz corresponde a una clase del paquete model o dto del backend.
// Los valores DECIMAL de MySQL (BigDecimal en Java) llegan como number; las fechas llegan como texto.

// ─────────────────────────────────────────────
// PERSONAS (herencia: Persona → Cliente / Empleado → Veterinario / Auxiliar)
// ─────────────────────────────────────────────
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

export interface Cliente extends Persona {
  direccion?: string;
  ciudad?: string;
  departamento?: string;
  fechaRegistro?: string;
}

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

export interface Telefono {
  telefono?: string;
  tipo?: string;
}

// ─────────────────────────────────────────────
// MASCOTAS Y ATENCIÓN CLÍNICA
// ─────────────────────────────────────────────
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

export interface HistoriaClinica {
  idHistoria: number;
  numeroHistoria?: string;
  fechaApertura?: string;
  grupoSanguineo?: string;
  alergias?: string;
  enfermedadesCronicas?: string;
  observaciones?: string;
}

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
export interface Sede {
  idSede: number;
  nombre?: string;
  direccion?: string;
  telefono?: string;
  director?: string;
}

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
export interface Resumen {
  nombre?: string;
  total?: number;
}

export interface EjercicioAlgebra {
  numero: number;
  titulo?: string;
  grupo?: string;
  enunciado?: string;
  sql?: string;
}

export interface ResultadoConsulta {
  columnas: string[];
  filas: string[][];
}

// ─────────────────────────────────────────────
// DTO: respuestas que juntan varios datos
// ─────────────────────────────────────────────
export interface ClienteDetalle {
  cliente: Cliente;
  telefonos: Telefono[];
  mascotas: Mascota[];
  facturas: Factura[];
}

export interface MascotaDetalle {
  mascota: Mascota;
  historia?: HistoriaClinica;
  consultas: Consulta[];
}

export interface FacturaDetalle {
  factura: Factura;
  lineas: DetalleFactura[];
}

export interface Inicio {
  conteos: Resumen[];
  ingresos: Resumen[];
  estadosCitas: Resumen[];
  stockBajo: ItemInventario[];
  proximas: Cita[];
}

export interface AlgebraLista {
  grupos: string[];
  ejercicios: EjercicioAlgebra[];
}

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

export interface TelefonoForm {
  telefono: string;
  tipo: string;
}

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

export interface CitaForm {
  idMascota: number | null;
  idServicio: number | null;
  idSede: number | null;
  idVeterinario: number | null;
  /** Formato aaaa-mm-ddThh:mm (input datetime-local). */
  fechaHora: string;
  motivo: string | null;
}

export interface LineaFacturaForm {
  idServicio: number | null;
  idMedicamento: number | null;
  cantidad: number;
  descuentoPct: number;
}

export interface FacturaForm {
  idCliente: number | null;
  idSede: number | null;
  idEmpleado: number | null;
  metodoPago: string;
  estado: string;
  lineas: LineaFacturaForm[];
}

export interface InventarioForm {
  idSede: number | null;
  idMedicamento: number | null;
  stockActual: number | null;
  stockMinimo: number | null;
}

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
