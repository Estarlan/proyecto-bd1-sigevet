import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { MascotaService, mensajeDeError } from '../../services/api.service';
import { Mascota } from '../../models/models';

@Component({
  selector: 'app-mascotas',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './mascotas.html'
})
export class Mascotas implements OnInit {

  mascotas: Mascota[] = [];
  buscar = '';
  cargando = true;
  error = '';

  constructor(private mascotaService: MascotaService) {}

  ngOnInit(): void {
    this.consultar();
  }

  /** La búsqueda la hace el SQL del backend: nombre, apellido del propietario o microchip. */
  consultar(): void {
    this.cargando = true;
    this.error = '';
    this.mascotaService.getAll(this.buscar).subscribe({
      next: (datos) => { this.mascotas = datos; this.cargando = false; },
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
    });
  }

  limpiar(): void {
    this.buscar = '';
    this.consultar();
  }
}
