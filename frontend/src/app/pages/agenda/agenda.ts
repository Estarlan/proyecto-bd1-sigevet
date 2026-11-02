import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { CitaService, SedeService, mensajeDeError } from '../../services/api.service';
import { Cita, Sede } from '../../models/models';
import { UltimaPeticion } from '../../shared/ultima-peticion';

/**
 * Agenda de citas (/agenda) con filtros por sede y estado. Desde aquí se cambia el estado de una cita PROGRAMADA
 * (atendida, no asistió o cancelada); las reglas de transición las hace cumplir el backend y la base de datos.
 */
@Component({
  selector: 'app-agenda',
  standalone: false,
  templateUrl: './agenda.html'
})
export class Agenda implements OnInit {

  /** Citas que se muestran en la tabla, ya filtradas por el SQL del DAO. */
  citas: Cita[] = [];
  /** Sedes para el filtro (salen de la tabla SEDE). */
  sedes: Sede[] = [];
  /** Estados para el filtro (salen del CHECK CK_CITA_ESTADO). */
  estados: string[] = [];

  /** Filtro de sede: null significa "todas". */
  sedeSeleccionada: number | null = null;
  /** Filtro de estado: '' significa "todos". */
  estadoSeleccionado = '';

  /** true mientras se espera la lista de citas. */
  cargando = true;
  /** Error de la carga de la lista. */
  error = '';

  /** Mensaje de éxito de la última acción (por ejemplo, "La cita quedó agendada"). */
  aviso = '';
  /** Error de la última acción, separado del error de carga. */
  errorAccion = '';
  /** id de la cita cuyo estado se está cambiando (para deshabilitar sus botones). */
  cambiando: number | null = null;

  /** Acciones posibles sobre una cita PROGRAMADA. */
  acciones = [
    { estado: 'ATENDIDA', texto: 'Atendida', icono: 'bi-check2', clase: 'btn-outline-success' },
    { estado: 'NO_ASISTIO', texto: 'No asistió', icono: 'bi-person-x', clase: 'btn-outline-secondary' },
    { estado: 'CANCELADA', texto: 'Cancelar', icono: 'bi-x-lg', clase: 'btn-outline-danger' }
  ];

  /** Controla que solo cuente la respuesta del filtro más reciente. */
  private ultima = new UltimaPeticion();

  constructor(private route: ActivatedRoute, private citaService: CitaService, private sedeService: SedeService) {}

  /** Carga los filtros, avisa si se llegó desde "agendar cita" y pide la lista. */
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

  /** Pide las citas con los filtros actuales. Se llama al abrir la página y cada vez que cambia un filtro. */
  consultar(): void {
    this.cargando = true;
    this.error = '';
    this.ultima.lanzar(this.citaService.getAll(this.sedeSeleccionada, this.estadoSeleccionado), {
      next: (datos) => { this.citas = datos; this.cargando = false; },
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
    });
  }

  /** Quita los dos filtros y vuelve a pedir todas las citas. */
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
