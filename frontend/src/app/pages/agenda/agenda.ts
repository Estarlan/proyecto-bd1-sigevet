import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { CitaService, SedeService, mensajeDeError } from '../../services/api.service';
import { Cita, Sede } from '../../models/models';

@Component({
  selector: 'app-agenda',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './agenda.html'
})
export class Agenda implements OnInit {

  citas: Cita[] = [];
  sedes: Sede[] = [];
  estados: string[] = [];

  sedeSeleccionada: number | null = null;
  estadoSeleccionado = '';

  cargando = true;
  error = '';

  constructor(private citaService: CitaService, private sedeService: SedeService) {}

  ngOnInit(): void {
    this.sedeService.getAll().subscribe(datos => this.sedes = datos);
    // estados del CHECK CK_CITA_ESTADO: PROGRAMADA, ATENDIDA, CANCELADA, NO_ASISTIO
    this.citaService.getEstados().subscribe(datos => this.estados = datos);
    this.consultar();
  }

  consultar(): void {
    this.cargando = true;
    this.error = '';
    this.citaService.getAll(this.sedeSeleccionada, this.estadoSeleccionado).subscribe({
      next: (datos) => { this.citas = datos; this.cargando = false; },
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
    });
  }

  limpiar(): void {
    this.sedeSeleccionada = null;
    this.estadoSeleccionado = '';
    this.consultar();
  }
}
