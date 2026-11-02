import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import {
  AlgebraLista, Cita, Cliente, ClienteDetalle, EjercicioDetalle, Empleado, ErrorApi,
  Factura, FacturaDetalle, Inicio, ItemInventario, MascotaDetalle, Sede
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
