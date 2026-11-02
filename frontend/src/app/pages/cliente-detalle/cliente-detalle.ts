import { Component, OnInit } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ClienteService, mensajeDeError } from '../../services/api.service';
import { ClienteDetalle } from '../../models/models';

@Component({
  selector: 'app-cliente-detalle',
  standalone: true,
  imports: [RouterLink, CurrencyPipe],
  templateUrl: './cliente-detalle.html'
})
export class ClienteDetallePage implements OnInit {

  detalle: ClienteDetalle | null = null;
  cargando = true;
  error = '';

  constructor(private route: ActivatedRoute, private clienteService: ClienteService) {}

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
}
