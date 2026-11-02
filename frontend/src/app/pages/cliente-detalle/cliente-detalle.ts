import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { ClienteService, mensajeDeError } from '../../services/api.service';
import { ClienteDetalle } from '../../models/models';

@Component({
  selector: 'app-cliente-detalle',
  standalone: false,
  templateUrl: './cliente-detalle.html'
})
export class ClienteDetallePage implements OnInit {

  detalle: ClienteDetalle | null = null;
  cargando = true;
  error = '';

  /** Error de una acción (eliminar), separado del error de carga. */
  errorAccion = '';
  eliminando = false;

  constructor(private route: ActivatedRoute, private router: Router, private clienteService: ClienteService) {}

  ngOnInit(): void {
    // Se escucha paramMap (y no solo el valor inicial) para que la página se actualice
    // si se navega de un cliente a otro sin salir de esta ruta.
    this.route.paramMap.subscribe(parametros => {
      const id = Number(parametros.get('id'));
      this.cargar(id);
    });
  }

  cargar(id: number): void {
    this.cargando = true;
    this.error = '';
    this.detalle = null;
    this.clienteService.getDetalle(id).subscribe({
      next: (datos) => { this.detalle = datos; this.cargando = false; },
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
    });
  }

  /**
   * Borra el cliente. El backend solo lo permite si no tiene mascotas ni facturas (responde 409 si las tiene),
   * porque son historia clínica y contable que se conserva.
   */
  eliminar(): void {
    if (!this.detalle || !confirm('¿Eliminar al cliente ' + this.detalle.cliente.nombreCompleto + '?')) {
      return;
    }
    this.eliminando = true;
    this.errorAccion = '';
    this.clienteService.eliminar(this.detalle.cliente.idPersona).subscribe({
      next: () => this.router.navigate(['/clientes']),
      error: (e: HttpErrorResponse) => { this.errorAccion = mensajeDeError(e); this.eliminando = false; }
    });
  }
}
