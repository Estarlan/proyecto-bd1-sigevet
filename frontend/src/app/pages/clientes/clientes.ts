import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { ClienteService, mensajeDeError } from '../../services/api.service';
import { Cliente } from '../../models/models';

@Component({
  selector: 'app-clientes',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './clientes.html'
})
export class Clientes implements OnInit {

  clientes: Cliente[] = [];
  buscar = '';
  cargando = true;
  error = '';

  constructor(private clienteService: ClienteService) {}

  ngOnInit(): void {
    this.consultar();
  }

  /** Pide los clientes a la API. La búsqueda la hace el backend con SQL (WHERE ... LIKE ?). */
  consultar(): void {
    this.cargando = true;
    this.error = '';
    this.clienteService.getAll(this.buscar).subscribe({
      next: (datos) => { this.clientes = datos; this.cargando = false; },
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
    });
  }

  limpiar(): void {
    this.buscar = '';
    this.consultar();
  }
}
