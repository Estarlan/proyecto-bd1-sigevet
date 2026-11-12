import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import {
  Cita, CitaForm, Cliente, ClienteDetalle, ClienteForm, Empleado, EmpleadoForm,
  ErrorApi, Factura, FacturaDetalle, FacturaForm, IdRespuesta, Inicio, InventarioForm, ItemInventario, Mascota,
  MascotaDetalle, MascotaForm, Mensaje, OpcionCatalogo, Sede
} from '../models/models';

// Servicios que hablan con la API REST de Spring Boot (/api/*). Hay un servicio por recurso; cada método devuelve
// un Observable de HttpClient y la página que lo llama se suscribe. El frontend nunca se conecta a MySQL: el SQL
// vive en los DAO del backend.

/** Dirección base de la API ('/api'; en desarrollo ng serve la reenvía al backend con proxy.conf.json). */
const API = environment.apiUrl;

/**
 * Arma los parámetros de búsqueda sin incluir los filtros vacíos.
 * Ejemplo: { sede: 2, estado: '' } → ?sede=2
 */
function filtros(valores: Record<string, string | number | null | undefined>): HttpParams {
  let params = new HttpParams();
  for (const clave of Object.keys(valores)) {
    const valor = valores[clave];
    if (valor !== null && valor !== undefined && valor !== '') {
      params = params.set(clave, valor);
    }
  }
  return params;
}

/**
 * Convierte un error de HttpClient en un texto para mostrar.
 * Si la API respondió con un ErrorApi ({ estado, mensaje }) se usa su mensaje.
 */
export function mensajeDeError(error: HttpErrorResponse): string {
  const cuerpo = error.error as ErrorApi | null;
  if (cuerpo && cuerpo.mensaje) {
    return cuerpo.mensaje;
  }
  if (error.status === 0) {
    return 'No se pudo conectar con el servidor de SIGEVET.';
  }
  return 'Ocurrió un error al consultar los datos (código ' + error.status + ').';
}

// ─────────────────────────────────────────────
// INICIO
// ─────────────────────────────────────────────
/** Resumen de la página de inicio. */
@Injectable({ providedIn: 'root' })
export class InicioService {
  constructor(private http: HttpClient) {}

  /** GET /api/inicio: conteos por tabla, ingresos, estado de citas, stock bajo y próximas citas. */
  getResumen(): Observable<Inicio> {
    return this.http.get<Inicio>(`${API}/inicio`);
  }
}

// ─────────────────────────────────────────────
// CLIENTE
// ─────────────────────────────────────────────
/** CRUD de clientes. */
@Injectable({ providedIn: 'root' })
export class ClienteService {
  constructor(private http: HttpClient) {}

  /** GET /api/clientes?buscar=...: lista (máximo 100) filtrada por documento o apellido; sin texto trae todos. */
  getAll(buscar: string): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(`${API}/clientes`, { params: filtros({ buscar: buscar.trim() }) });
  }
  /** GET /api/clientes/:id: el cliente con sus teléfonos, mascotas y facturas. */
  getDetalle(id: number): Observable<ClienteDetalle> {
    return this.http.get<ClienteDetalle>(`${API}/clientes/${id}`);
  }
  /** GET /api/clientes/:id/formulario: los datos del cliente con la forma del formulario de edición. */
  getFormulario(id: number): Observable<ClienteForm> {
    return this.http.get<ClienteForm>(`${API}/clientes/${id}/formulario`);
  }
  /** POST /api/clientes: crea el cliente y sus teléfonos en una transacción; devuelve el id generado. */
  crear(form: ClienteForm): Observable<IdRespuesta> {
    return this.http.post<IdRespuesta>(`${API}/clientes`, form);
  }
  /** PUT /api/clientes/:id: actualiza el cliente y reemplaza sus teléfonos. */
  actualizar(id: number, form: ClienteForm): Observable<void> {
    return this.http.put<void>(`${API}/clientes/${id}`, form);
  }
  /** DELETE /api/clientes/:id: solo si no tiene mascotas ni facturas (si no, el backend responde 409). */
  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${API}/clientes/${id}`);
  }
}

// ─────────────────────────────────────────────
// MASCOTA
// ─────────────────────────────────────────────
/** Consulta, alta, edición y cambio de estado de mascotas. */
@Injectable({ providedIn: 'root' })
export class MascotaService {
  constructor(private http: HttpClient) {}

  /** GET /api/mascotas/:id: la mascota con su historia clínica y sus consultas. */
  getDetalle(id: number): Observable<MascotaDetalle> {
    return this.http.get<MascotaDetalle>(`${API}/mascotas/${id}`);
  }
  /** GET /api/mascotas?buscar=...: lista (máximo 200) filtrada por nombre, apellido del propietario o microchip. */
  getAll(buscar: string): Observable<Mascota[]> {
    return this.http.get<Mascota[]>(`${API}/mascotas`, { params: filtros({ buscar: buscar.trim() }) });
  }
  /** GET /api/mascotas/:id/formulario: los datos de la mascota y su historia con la forma del formulario. */
  getFormulario(id: number): Observable<MascotaForm> {
    return this.http.get<MascotaForm>(`${API}/mascotas/${id}/formulario`);
  }
  /** POST /api/mascotas: crea la mascota y su historia clínica en una transacción; devuelve el id generado. */
  crear(form: MascotaForm): Observable<IdRespuesta> {
    return this.http.post<IdRespuesta>(`${API}/mascotas`, form);
  }
  /** PUT /api/mascotas/:id: actualiza la mascota y su historia clínica. */
  actualizar(id: number, form: MascotaForm): Observable<void> {
    return this.http.put<void>(`${API}/mascotas/${id}`, form);
  }
  /** Borrado lógico: ACTIVA, FALLECIDA o TRASLADADA. */
  cambiarEstado(id: number, estado: string): Observable<Mensaje> {
    return this.http.patch<Mensaje>(`${API}/mascotas/${id}/estado`, { estado });
  }
}

// ─────────────────────────────────────────────
// CITA (agenda)
// ─────────────────────────────────────────────
/** Agenda de citas. */
@Injectable({ providedIn: 'root' })
export class CitaService {
  constructor(private http: HttpClient) {}

  /** GET /api/citas?sede=...&estado=...: citas filtradas; null y '' significan "sin filtro". */
  getAll(sede: number | null, estado: string): Observable<Cita[]> {
    return this.http.get<Cita[]>(`${API}/citas`, { params: filtros({ sede, estado }) });
  }
  /** GET /api/citas/estados: los estados permitidos por el CHECK CK_CITA_ESTADO. */
  getEstados(): Observable<string[]> {
    return this.http.get<string[]>(`${API}/citas/estados`);
  }
  /** POST /api/citas: agenda una cita (queda PROGRAMADA); devuelve el id generado. */
  crear(form: CitaForm): Observable<IdRespuesta> {
    return this.http.post<IdRespuesta>(`${API}/citas`, form);
  }
  /** ATENDIDA, CANCELADA o NO_ASISTIO (solo para citas PROGRAMADAS). */
  cambiarEstado(id: number, estado: string): Observable<void> {
    return this.http.patch<void>(`${API}/citas/${id}/estado`, { estado });
  }
}

// ─────────────────────────────────────────────
// FACTURA
// ─────────────────────────────────────────────
/** Facturación. */
@Injectable({ providedIn: 'root' })
export class FacturaService {
  constructor(private http: HttpClient) {}

  /** GET /api/facturas?sede=...&estado=...: facturas filtradas; null y '' significan "sin filtro". */
  getAll(sede: number | null, estado: string): Observable<Factura[]> {
    return this.http.get<Factura[]>(`${API}/facturas`, { params: filtros({ sede, estado }) });
  }
  /** GET /api/facturas/estados: los estados permitidos por el CHECK CK_FACTURA_ESTADO. */
  getEstados(): Observable<string[]> {
    return this.http.get<string[]>(`${API}/facturas/estados`);
  }
  /** GET /api/facturas/:id: la factura con sus líneas. */
  getDetalle(id: number): Observable<FacturaDetalle> {
    return this.http.get<FacturaDetalle>(`${API}/facturas/${id}`);
  }
  /** POST /api/facturas: guarda factura, líneas y descuento de inventario en una transacción; devuelve el id. */
  crear(form: FacturaForm): Observable<IdRespuesta> {
    return this.http.post<IdRespuesta>(`${API}/facturas`, form);
  }
  /** PAGADA (registrar pago de una PENDIENTE) o ANULADA. */
  cambiarEstado(id: number, estado: string): Observable<void> {
    return this.http.patch<void>(`${API}/facturas/${id}/estado`, { estado });
  }
}

// ─────────────────────────────────────────────
// INVENTARIO
// ─────────────────────────────────────────────
/** Existencias de medicamentos por sede. La llave de cada fila es (idSede, idMedicamento). */
@Injectable({ providedIn: 'root' })
export class InventarioService {
  constructor(private http: HttpClient) {}

  /** GET /api/inventario?sede=...: existencias de una sede o de todas. */
  getAll(sede: number | null): Observable<ItemInventario[]> {
    return this.http.get<ItemInventario[]>(`${API}/inventario`, { params: filtros({ sede }) });
  }
  /** POST /api/inventario: agrega un medicamento a una sede. */
  agregar(form: InventarioForm): Observable<void> {
    return this.http.post<void>(`${API}/inventario`, form);
  }
  /** PUT /api/inventario/:sede/:medicamento: cambia las existencias y el mínimo. */
  actualizar(idSede: number, idMedicamento: number, form: InventarioForm): Observable<void> {
    return this.http.put<void>(`${API}/inventario/${idSede}/${idMedicamento}`, form);
  }
  /** DELETE /api/inventario/:sede/:medicamento: quita el medicamento del inventario de la sede. */
  eliminar(idSede: number, idMedicamento: number): Observable<void> {
    return this.http.delete<void>(`${API}/inventario/${idSede}/${idMedicamento}`);
  }
}

// ─────────────────────────────────────────────
// PERSONAL
// ─────────────────────────────────────────────
/** Empleados (veterinarios, auxiliares y administrativos). */
@Injectable({ providedIn: 'root' })
export class PersonalService {
  constructor(private http: HttpClient) {}

  /** GET /api/personal?sede=...: empleados de una sede o de todas. */
  getAll(sede: number | null): Observable<Empleado[]> {
    return this.http.get<Empleado[]>(`${API}/personal`, { params: filtros({ sede }) });
  }
  /** GET /api/personal/:id/formulario: los datos del empleado con la forma del formulario de edición. */
  getFormulario(id: number): Observable<EmpleadoForm> {
    return this.http.get<EmpleadoForm>(`${API}/personal/${id}/formulario`);
  }
  /** POST /api/personal: crea el empleado y los datos de su subtipo en una transacción; devuelve el id. */
  crear(form: EmpleadoForm): Observable<IdRespuesta> {
    return this.http.post<IdRespuesta>(`${API}/personal`, form);
  }
  /** PUT /api/personal/:id: actualiza el empleado y los datos de su subtipo. */
  actualizar(id: number, form: EmpleadoForm): Observable<void> {
    return this.http.put<void>(`${API}/personal/${id}`, form);
  }
  /** Borrado lógico: ACTIVO o INACTIVO. */
  cambiarEstado(id: number, estado: string): Observable<void> {
    return this.http.patch<void>(`${API}/personal/${id}/estado`, { estado });
  }
}

// ─────────────────────────────────────────────
// SEDE (para los filtros por sede)
// ─────────────────────────────────────────────
/** Sedes de la red de clínicas. */
@Injectable({ providedIn: 'root' })
export class SedeService {
  constructor(private http: HttpClient) {}

  /** GET /api/sedes: todas las sedes, con su director. */
  getAll(): Observable<Sede[]> {
    return this.http.get<Sede[]>(`${API}/sedes`);
  }
}

// ─────────────────────────────────────────────
// CATÁLOGOS (opciones de los <select> de los formularios)
// ─────────────────────────────────────────────
/** Listas de opciones para los select de los formularios. Todas devuelven { id, nombre, detalle?, valor? }. */
@Injectable({ providedIn: 'root' })
export class CatalogoService {
  constructor(private http: HttpClient) {}

  /** Ciudades (con su departamento en "detalle"). */
  ciudades(): Observable<OpcionCatalogo[]> {
    return this.http.get<OpcionCatalogo[]>(`${API}/catalogos/ciudades`);
  }
  /** Razas (con su especie en "detalle"). */
  razas(): Observable<OpcionCatalogo[]> {
    return this.http.get<OpcionCatalogo[]>(`${API}/catalogos/razas`);
  }
  /** Servicios con su precio en "valor". */
  servicios(): Observable<OpcionCatalogo[]> {
    return this.http.get<OpcionCatalogo[]>(`${API}/catalogos/servicios`);
  }
  /** Todos los medicamentos, o solo los que tiene una sede (con sus existencias en el detalle). */
  medicamentos(sede: number | null): Observable<OpcionCatalogo[]> {
    return this.http.get<OpcionCatalogo[]>(`${API}/catalogos/medicamentos`, { params: filtros({ sede }) });
  }
  /** Especialidades de los veterinarios. */
  especialidades(): Observable<OpcionCatalogo[]> {
    return this.http.get<OpcionCatalogo[]>(`${API}/catalogos/especialidades`);
  }
  /** Veterinarios activos de una sede (regla RN-08). */
  veterinarios(sede: number): Observable<OpcionCatalogo[]> {
    return this.http.get<OpcionCatalogo[]>(`${API}/catalogos/veterinarios`, { params: filtros({ sede }) });
  }
  /** Empleados activos de una sede (quien emite una factura). */
  empleados(sede: number): Observable<OpcionCatalogo[]> {
    return this.http.get<OpcionCatalogo[]>(`${API}/catalogos/empleados`, { params: filtros({ sede }) });
  }
}
