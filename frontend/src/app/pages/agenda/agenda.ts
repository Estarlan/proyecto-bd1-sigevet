import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, RouterLink } from '@angular/router';
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

  aviso = '';
  errorAccion = '';
  /** id de la cita cuyo estado se está cambiando (para deshabilitar sus botones). */
  cambiando: number | null = null;

  /** Acciones posibles sobre una cita PROGRAMADA. */
  acciones = [
    { estado: 'ATENDIDA', texto: 'Atendida', icono: 'bi-check2', clase: 'btn-outline-success' },
    { estado: 'NO_ASISTIO', texto: 'No asistió', icono: 'bi-person-x', clase: 'btn-outline-secondary' },
    { estado: 'CANCELADA', texto: 'Cancelar', icono: 'bi-x-lg', clase: 'btn-outline-danger' }
  ];

  constructor(private route: ActivatedRoute, private citaService: CitaService, private sedeService: SedeService) {}

  ngOnInit(): void {
    this.sedeService.getAll().subscribe(datos => this.sedes = datos);
    // estados del CHECK CK_CITA_ESTADO: PROGRAMADA, ATENDIDA, CANCELADA, NO_ASISTIO
    this.citaService.getEstados().subscribe(datos => this.estados = datos);
    if (this.route.snapshot.queryParamMap.get('creada')) {
      this.aviso = 'La cita quedó agendada (PROGRAMADA).';
      this.estadoSeleccionado = 'PROGRAMADA';
    }
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

  /**
   * Cambia el estado de una cita PROGRAMADA. Si la cita ya cambió, o una regla de la base lo impide (por ejemplo,
   * el disparador TRG_CITA_UPD), el mensaje del backend se muestra arriba.
   */
  cambiarEstado(cita: Cita, estado: string): void {
    if (estado === 'CANCELADA' && !confirm('¿Cancelar la cita de ' + cita.mascota + ' del ' + cita.fechaHora + '?')) {
      return;
    }
    this.cambiando = cita.idCita;
    this.aviso = '';
    this.errorAccion = '';
    this.citaService.cambiarEstado(cita.idCita, estado).subscribe({
      next: () => {
        this.aviso = 'La cita de ' + cita.mascota + ' quedó ' + estado + '.';
        this.cambiando = null;
        this.consultar();
      },
      error: (e: HttpErrorResponse) => { this.errorAccion = mensajeDeError(e); this.cambiando = null; }
    });
  }
}
