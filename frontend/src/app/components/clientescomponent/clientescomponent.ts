import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { ClienteService, mensajeDeError } from '../../services/api.service';
import { Cliente } from '../../models/models';
import { UltimaPeticion } from '../../shared/ultima-peticion';

/**
 * Lista de clientes (/clientes) con buscador. La búsqueda por documento o apellido la resuelve el backend con SQL
 * (WHERE ... LIKE ?); aquí solo se pide la lista y se muestra.
 */
@Component({
  selector: 'app-clientescomponent',
  standalone: false,
  templateUrl: './clientescomponent.html'
})
export class Clientescomponent implements OnInit {

  /** Clientes que se muestran en la tabla (la API devuelve como máximo 100). */
  clientes: Cliente[] = [];
  /** Texto del buscador: documento o apellido. */
  buscar = '';
  /** true mientras se espera la respuesta de la API (se muestra el círculo de carga). */
  cargando = true;
  /** Mensaje de error de la última consulta; vacío si todo salió bien. */
  error = '';

  /** Controla que solo cuente la respuesta de la búsqueda más reciente. */
  private ultima = new UltimaPeticion();

  constructor(private clienteService: ClienteService) {}

  /** Al abrir la página se piden todos los clientes (buscador vacío). */
  ngOnInit(): void {
    this.consultar();
  }

  /** Pide los clientes a la API. La búsqueda la hace el backend con SQL (WHERE ... LIKE ?). */
  consultar(): void {
    this.cargando = true;
    this.error = '';
    this.ultima.lanzar(this.clienteService.getAll(this.buscar), {
      next: (datos) => { this.clientes = datos; this.cargando = false; },
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
    });
  }

  /** Borra el texto del buscador y vuelve a pedir la lista completa. */
  limpiar(): void {
    this.buscar = '';
    this.consultar();
  }
}
