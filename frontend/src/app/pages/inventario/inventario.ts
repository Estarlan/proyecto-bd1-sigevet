import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { InventarioService, SedeService, mensajeDeError } from '../../services/api.service';
import { ItemInventario, Sede } from '../../models/models';

/** Existencias de la tabla INVENTARIO, que resuelve la relación N:M ALMACENAR entre SEDE y MEDICAMENTO. */
@Component({
  selector: 'app-inventario',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './inventario.html'
})
export class Inventario implements OnInit {

  inventario: ItemInventario[] = [];
  sedes: Sede[] = [];

  sedeSeleccionada: number | null = null;
  /** Si está activo, se muestran solo los medicamentos por debajo del mínimo (filtro en el navegador). */
  soloStockBajo = false;

  cargando = true;
  error = '';

  constructor(private inventarioService: InventarioService, private sedeService: SedeService) {}

  ngOnInit(): void {
    this.sedeService.getAll().subscribe(datos => this.sedes = datos);
    this.consultar();
  }

  consultar(): void {
    this.cargando = true;
    this.error = '';
    this.inventarioService.getAll(this.sedeSeleccionada).subscribe({
      next: (datos) => { this.inventario = datos; this.cargando = false; },
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
    });
  }

  limpiar(): void {
    this.sedeSeleccionada = null;
    this.soloStockBajo = false;
    this.consultar();
  }

  get itemsFiltrados(): ItemInventario[] {
    return this.soloStockBajo ? this.inventario.filter(i => i.stockBajo) : this.inventario;
  }

  get cantidadStockBajo(): number {
    return this.inventario.filter(i => i.stockBajo).length;
  }
}
