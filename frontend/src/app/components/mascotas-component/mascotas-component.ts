import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { MascotaService, mensajeDeError } from '../../services/api.service';
import { Mascota } from '../../models/models';
import { UltimaPeticion } from '../../shared/ultima-peticion';

/** Lista de mascotas (/mascotas) con buscador por nombre, apellido del propietario o microchip. */
@Component({
  selector: 'app-mascotas-component',
  standalone: false,
  templateUrl: './mascotas-component.html'
})
export class MascotasComponent implements OnInit {

  /** Mascotas que se muestran en la tabla (la API devuelve como máximo 200). */
  mascotas: Mascota[] = [];
  /** Texto del buscador. */
  buscar = '';
  /** true mientras se espera la respuesta de la API. */
  cargando = true;
  /** Mensaje de error de la última consulta; vacío si todo salió bien. */
  error = '';

  /** Controla que solo cuente la respuesta de la búsqueda más reciente. */
  private ultima = new UltimaPeticion();

  constructor(private mascotaService: MascotaService) {}

  /** Al abrir la página se piden todas las mascotas (buscador vacío). */
  ngOnInit(): void {
    this.consultar();
  }

  /** La búsqueda la hace el SQL del backend: nombre, apellido del propietario o microchip. */
  consultar(): void {
    this.cargando = true;
    this.error = '';
    this.ultima.lanzar(this.mascotaService.getAll(this.buscar), {
      next: (datos) => { this.mascotas = datos; this.cargando = false; },
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
    });
  }

  /** Borra el texto del buscador y vuelve a pedir la lista completa. */
  limpiar(): void {
    this.buscar = '';
    this.consultar();
  }
}
