import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { CatalogoService, InventarioService, SedeService, mensajeDeError } from '../../services/api.service';
import { InventarioForm, ItemInventario, OpcionCatalogo, Sede } from '../../models/models';

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

  // --- agregar un medicamento a una sede
  mostrarNuevo = false;
  medicamentos: OpcionCatalogo[] = [];
  nuevo: InventarioForm = { idSede: null, idMedicamento: null, stockActual: 0, stockMinimo: 0 };

  // --- edición en la fila: la llave de INVENTARIO es (id_sede, id_medicamento)
  editando: ItemInventario | null = null;
  editStock = 0;
  editMinimo = 0;

  aviso = '';
  errorAccion = '';
  guardando = false;

  constructor(private inventarioService: InventarioService, private sedeService: SedeService,
              private catalogoService: CatalogoService) {}

  ngOnInit(): void {
    this.sedeService.getAll().subscribe(datos => this.sedes = datos);
    this.catalogoService.medicamentos(null).subscribe(datos => this.medicamentos = datos);
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

  /** Muestra u oculta el formulario de agregar; si hay una sede filtrada, la deja elegida. */
  abrirNuevo(): void {
    this.mostrarNuevo = !this.mostrarNuevo;
    this.nuevo = { idSede: this.sedeSeleccionada, idMedicamento: null, stockActual: 0, stockMinimo: 0 };
  }

  /** INSERT en INVENTARIO. Si la sede ya tenía el medicamento, la llave primaria lo impide (409). */
  agregar(): void {
    this.guardando = true;
    this.limpiarMensajes();
    this.inventarioService.agregar(this.nuevo).subscribe({
      next: () => {
        this.aviso = 'Medicamento agregado al inventario.';
        this.mostrarNuevo = false;
        this.guardando = false;
        this.consultar();
      },
      error: (e: HttpErrorResponse) => { this.errorAccion = mensajeDeError(e); this.guardando = false; }
    });
  }

  editar(item: ItemInventario): void {
    this.editando = item;
    this.editStock = item.stockActual ?? 0;
    this.editMinimo = item.stockMinimo ?? 0;
    this.limpiarMensajes();
  }

  estaEditando(item: ItemInventario): boolean {
    return this.editando !== null && this.editando.idSede === item.idSede && this.editando.idMedicamento === item.idMedicamento;
  }

  /** UPDATE de las existencias y el mínimo (conteo físico o compra). */
  guardarEdicion(): void {
    if (!this.editando) {
      return;
    }
    const item = this.editando;
    const form: InventarioForm = { idSede: item.idSede, idMedicamento: item.idMedicamento, stockActual: this.editStock, stockMinimo: this.editMinimo };
    this.guardando = true;
    this.inventarioService.actualizar(item.idSede, item.idMedicamento, form).subscribe({
      next: () => {
        this.aviso = 'Existencias de ' + item.medicamento + ' actualizadas en ' + item.sede + '.';
        this.editando = null;
        this.guardando = false;
        this.consultar();
      },
      error: (e: HttpErrorResponse) => { this.errorAccion = mensajeDeError(e); this.guardando = false; }
    });
  }

  /** DELETE físico: la fila de inventario no tiene historial (las facturas guardan su propio precio y cantidad). */
  quitar(item: ItemInventario): void {
    if (!confirm('¿Quitar ' + item.medicamento + ' del inventario de ' + item.sede + '?')) {
      return;
    }
    this.limpiarMensajes();
    this.inventarioService.eliminar(item.idSede, item.idMedicamento).subscribe({
      next: () => { this.aviso = item.medicamento + ' se quitó del inventario de ' + item.sede + '.'; this.consultar(); },
      error: (e: HttpErrorResponse) => { this.errorAccion = mensajeDeError(e); }
    });
  }

  private limpiarMensajes(): void {
    this.aviso = '';
    this.errorAccion = '';
  }
}
