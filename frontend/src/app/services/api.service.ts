import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import {
  AlgebraLista, Cita, CitaForm, Cliente, ClienteDetalle, ClienteForm, EjercicioDetalle, Empleado, EmpleadoForm,
  ErrorApi, Factura, FacturaDetalle, FacturaForm, IdRespuesta, Inicio, InventarioForm, ItemInventario, Mascota,
  MascotaDetalle, MascotaForm, Mensaje, OpcionCatalogo, Sede
} from '../models/models';

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
@Injectable({ providedIn: 'root' })
export class InicioService {
  constructor(private http: HttpClient) {}

  getResumen(): Observable<Inicio> {
    return this.http.get<Inicio>(`${API}/inicio`);
  }
}

// ─────────────────────────────────────────────
// CLIENTE
// ─────────────────────────────────────────────
@Injectable({ providedIn: 'root' })
export class ClienteService {
  constructor(private http: HttpClient) {}

  getAll(buscar: string): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(`${API}/clientes`, { params: filtros({ buscar: buscar.trim() }) });
  }
  getDetalle(id: number): Observable<ClienteDetalle> {
    return this.http.get<ClienteDetalle>(`${API}/clientes/${id}`);
  }
  getFormulario(id: number): Observable<ClienteForm> {
    return this.http.get<ClienteForm>(`${API}/clientes/${id}/formulario`);
  }
  crear(form: ClienteForm): Observable<IdRespuesta> {
    return this.http.post<IdRespuesta>(`${API}/clientes`, form);
  }
  actualizar(id: number, form: ClienteForm): Observable<void> {
    return this.http.put<void>(`${API}/clientes/${id}`, form);
  }
  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${API}/clientes/${id}`);
  }
}

// ─────────────────────────────────────────────
// MASCOTA
// ─────────────────────────────────────────────
@Injectable({ providedIn: 'root' })
export class MascotaService {
  constructor(private http: HttpClient) {}

  getDetalle(id: number): Observable<MascotaDetalle> {
    return this.http.get<MascotaDetalle>(`${API}/mascotas/${id}`);
  }
  getAll(buscar: string): Observable<Mascota[]> {
    return this.http.get<Mascota[]>(`${API}/mascotas`, { params: filtros({ buscar: buscar.trim() }) });
  }
  getFormulario(id: number): Observable<MascotaForm> {
    return this.http.get<MascotaForm>(`${API}/mascotas/${id}/formulario`);
  }
  crear(form: MascotaForm): Observable<IdRespuesta> {
    return this.http.post<IdRespuesta>(`${API}/mascotas`, form);
  }
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
@Injectable({ providedIn: 'root' })
export class CitaService {
  constructor(private http: HttpClient) {}

  getAll(sede: number | null, estado: string): Observable<Cita[]> {
    return this.http.get<Cita[]>(`${API}/citas`, { params: filtros({ sede, estado }) });
  }
  getEstados(): Observable<string[]> {
    return this.http.get<string[]>(`${API}/citas/estados`);
  }
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
@Injectable({ providedIn: 'root' })
export class FacturaService {
  constructor(private http: HttpClient) {}

  getAll(sede: number | null, estado: string): Observable<Factura[]> {
    return this.http.get<Factura[]>(`${API}/facturas`, { params: filtros({ sede, estado }) });
  }
  getEstados(): Observable<string[]> {
    return this.http.get<string[]>(`${API}/facturas/estados`);
  }
  getDetalle(id: number): Observable<FacturaDetalle> {
    return this.http.get<FacturaDetalle>(`${API}/facturas/${id}`);
  }
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
@Injectable({ providedIn: 'root' })
export class InventarioService {
  constructor(private http: HttpClient) {}

  getAll(sede: number | null): Observable<ItemInventario[]> {
    return this.http.get<ItemInventario[]>(`${API}/inventario`, { params: filtros({ sede }) });
  }
  agregar(form: InventarioForm): Observable<void> {
    return this.http.post<void>(`${API}/inventario`, form);
  }
  actualizar(idSede: number, idMedicamento: number, form: InventarioForm): Observable<void> {
    return this.http.put<void>(`${API}/inventario/${idSede}/${idMedicamento}`, form);
  }
  eliminar(idSede: number, idMedicamento: number): Observable<void> {
    return this.http.delete<void>(`${API}/inventario/${idSede}/${idMedicamento}`);
  }
}

// ─────────────────────────────────────────────
// PERSONAL
// ─────────────────────────────────────────────
@Injectable({ providedIn: 'root' })
export class PersonalService {
  constructor(private http: HttpClient) {}

  getAll(sede: number | null): Observable<Empleado[]> {
    return this.http.get<Empleado[]>(`${API}/personal`, { params: filtros({ sede }) });
  }
  getFormulario(id: number): Observable<EmpleadoForm> {
    return this.http.get<EmpleadoForm>(`${API}/personal/${id}/formulario`);
  }
  crear(form: EmpleadoForm): Observable<IdRespuesta> {
    return this.http.post<IdRespuesta>(`${API}/personal`, form);
  }
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
@Injectable({ providedIn: 'root' })
export class SedeService {
  constructor(private http: HttpClient) {}

  getAll(): Observable<Sede[]> {
    return this.http.get<Sede[]>(`${API}/sedes`);
  }
}

// ─────────────────────────────────────────────
// ÁLGEBRA RELACIONAL
// ─────────────────────────────────────────────
@Injectable({ providedIn: 'root' })
export class AlgebraService {
  constructor(private http: HttpClient) {}

  getAll(): Observable<AlgebraLista> {
    return this.http.get<AlgebraLista>(`${API}/algebra`);
  }
  getDetalle(numero: number): Observable<EjercicioDetalle> {
    return this.http.get<EjercicioDetalle>(`${API}/algebra/${numero}`);
  }
}

// ─────────────────────────────────────────────
// CATÁLOGOS (opciones de los <select> de los formularios)
// ─────────────────────────────────────────────
@Injectable({ providedIn: 'root' })
export class CatalogoService {
  constructor(private http: HttpClient) {}

  ciudades(): Observable<OpcionCatalogo[]> {
    return this.http.get<OpcionCatalogo[]>(`${API}/catalogos/ciudades`);
  }
  razas(): Observable<OpcionCatalogo[]> {
    return this.http.get<OpcionCatalogo[]>(`${API}/catalogos/razas`);
  }
  servicios(): Observable<OpcionCatalogo[]> {
    return this.http.get<OpcionCatalogo[]>(`${API}/catalogos/servicios`);
  }
  /** Todos los medicamentos, o solo los que tiene una sede (con sus existencias en el detalle). */
  medicamentos(sede: number | null): Observable<OpcionCatalogo[]> {
    return this.http.get<OpcionCatalogo[]>(`${API}/catalogos/medicamentos`, { params: filtros({ sede }) });
  }
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
