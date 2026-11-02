import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { MascotaService, mensajeDeError } from '../../services/api.service';
import { MascotaDetalle } from '../../models/models';
import { UltimaPeticion } from '../../shared/ultima-peticion';

/**
 * Detalle de una mascota (/mascotas/:id): sus datos, la historia clínica (relación 1:1) y las consultas con sus
 * prescripciones. Desde aquí se cambia su estado (borrado lógico) y se pasa a editarla o a agendarle una cita.
 */
@Component({
  selector: 'app-mascota-detalle',
  standalone: false,
  templateUrl: './mascota-detalle.html'
})
export class MascotaDetallePage implements OnInit {

  /** La mascota con su historia y consultas; null mientras carga o si falló la carga. */
  detalle: MascotaDetalle | null = null;
  /** true mientras se espera la respuesta de la API. */
  cargando = true;
  /** Error de la carga. */
  error = '';

  /** Estado elegido en el select de borrado lógico. */
  nuevoEstado = '';
  /** Estados posibles de una mascota (CHECK CK_MASCOTA_ESTADO). */
  estados = ['ACTIVA', 'FALLECIDA', 'TRASLADADA'];
  /** Mensaje de éxito de la última acción. */
  aviso = '';
  /** Error de la última acción, separado del error de carga. */
  errorAccion = '';
  /** true mientras se cambia el estado (deshabilita el botón). */
  cambiando = false;

  /** Controla que solo cuente la respuesta de la mascota más reciente. */
  private ultima = new UltimaPeticion();

  constructor(private route: ActivatedRoute, private mascotaService: MascotaService) {}

  /** Carga la mascota de la dirección y vuelve a cargar si el id de la dirección cambia. */
  ngOnInit(): void {
    this.route.paramMap.subscribe(parametros => {
      const id = Number(parametros.get('id'));
      this.cargar(id);
    });
  }

  /** Pide la mascota con ese id y deja su estado actual elegido en el select. */
  cargar(id: number): void {
    this.cargando = true;
    this.error = '';
    this.detalle = null;
    this.ultima.lanzar(this.mascotaService.getDetalle(id), {
      next: (datos) => { this.detalle = datos; this.nuevoEstado = datos.mascota.estado ?? ''; this.cargando = false; },
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
    });
  }

  /** En la base el sexo se guarda como M o H (CHECK CK_MASCOTA_SEXO). */
  textoSexo(sexo?: string): string {
    if (sexo === 'M') {
      return 'Macho';
    }
    if (sexo === 'H') {
      return 'Hembra';
    }
    return '—';
  }

  /** En la base se guarda S o N (CHECK CK_MASCOTA_ESTERILIZADO). */
  textoSiNo(valor?: string): string {
    return valor === 'S' ? 'Sí' : 'No';
  }

  /**
   * Borrado lógico: la mascota no se borra, cambia de estado. Si deja de estar ACTIVA, el backend cancela sus citas
   * programadas en la misma transacción y lo informa en el mensaje.
   */
  cambiarEstado(): void {
    if (!this.detalle || this.nuevoEstado === this.detalle.mascota.estado) {
      return;
    }
    if (this.nuevoEstado !== 'ACTIVA' &&
        !confirm('¿Marcar a ' + this.detalle.mascota.nombre + ' como ' + this.nuevoEstado + '? Sus citas programadas se cancelarán.')) {
      this.nuevoEstado = this.detalle.mascota.estado ?? '';
      return;
    }
    const id = this.detalle.mascota.idMascota;
    this.cambiando = true;
    this.aviso = '';
    this.errorAccion = '';
    this.mascotaService.cambiarEstado(id, this.nuevoEstado).subscribe({
      next: (respuesta) => { this.aviso = respuesta.mensaje; this.cambiando = false; this.cargar(id); },
      error: (e: HttpErrorResponse) => { this.errorAccion = mensajeDeError(e); this.cambiando = false; }
    });
  }
}
