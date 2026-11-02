import { Component, OnInit } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { RouterLink } from '@angular/router';
import { FacturaService, SedeService, mensajeDeError } from '../../services/api.service';
import { Factura, Sede } from '../../models/models';

@Component({
  selector: 'app-facturas',
  standalone: true,
  imports: [FormsModule, RouterLink, CurrencyPipe],
  templateUrl: './facturas.html'
})
export class Facturas implements OnInit {

  facturas: Factura[] = [];
  sedes: Sede[] = [];
  estados: string[] = [];

  // filtros: null y '' significan "sin filtro"
  sedeSeleccionada: number | null = null;
  estadoSeleccionado = '';

  cargando = true;
  error = '';

  constructor(private facturaService: FacturaService, private sedeService: SedeService) {}

  ngOnInit(): void {
    // las opciones de los <select> salen de la base: SEDE y los estados del CHECK CK_FACTURA_ESTADO
    this.sedeService.getAll().subscribe(datos => this.sedes = datos);
    this.facturaService.getEstados().subscribe(datos => this.estados = datos);
    this.consultar();
  }

  /** Se llama al cargar la página y cada vez que cambia un filtro. El filtro lo aplica el SQL del DAO. */
  consultar(): void {
    this.cargando = true;
    this.error = '';
    this.facturaService.getAll(this.sedeSeleccionada, this.estadoSeleccionado).subscribe({
      next: (datos) => { this.facturas = datos; this.cargando = false; },
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
    });
  }

  limpiar(): void {
    this.sedeSeleccionada = null;
    this.estadoSeleccionado = '';
    this.consultar();
  }

  /** Suma de los totales de las facturas que se están mostrando. */
  get totalMostrado(): number {
    let suma = 0;
    for (const f of this.facturas) {
      suma += f.total ?? 0;
    }
    return suma;
  }
}
