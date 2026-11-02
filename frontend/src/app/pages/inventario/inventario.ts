import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { CatalogoService, InventarioService, SedeService, mensajeDeError } from '../../services/api.service';
import { InventarioForm, ItemInventario, OpcionCatalogo, Sede } from '../../models/models';
import { UltimaPeticion } from '../../shared/ultima-peticion';

/**
 * Existencias de la tabla INVENTARIO, que resuelve la relación N:M ALMACENAR entre SEDE y MEDICAMENTO.
 * Permite filtrar por sede, ver solo el stock bajo, agregar un medicamento a una sede, editar las existencias
 * y quitar un medicamento del inventario.
 */
@Component({
  selector: 'app-inventario',
  standalone: false,
  templateUrl: './inventario.html'
})
export class Inventario implements OnInit {

  /** Filas de inventario que devolvió la API para la sede elegida. */
  inventario: ItemInventario[] = [];
  /** Sedes para el filtro y para el formulario de agregar. */
  sedes: Sede[] = [];

  /** Filtro de sede: null significa "todas". */
  sedeSeleccionada: number | null = null;
  /** Si está activo, se muestran solo los medicamentos por debajo del mínimo (filtro en el navegador). */
  soloStockBajo = false;

  /** true mientras se espera el inventario. */
  cargando = true;
  /** Error de la carga del inventario. */
  error = '';

  // --- agregar un medicamento a una sede
  /** true cuando el formulario de agregar está desplegado. */
  mostrarNuevo = false;
  /** Catálogo de todos los medicamentos, para el select del formulario de agregar. */
  medicamentos: OpcionCatalogo[] = [];
  /** Datos del medicamento que se va a agregar. */
  nuevo: InventarioForm = { idSede: null, idMedicamento: null, stockActual: 0, stockMinimo: 0 };

  // --- edición en la fila: la llave de INVENTARIO es (id_sede, id_medicamento)
  /** Fila que se está editando, o null si ninguna. */
  editando: ItemInventario | null = null;
  /** Existencias actuales escritas en la fila que se edita. */
  editStock = 0;
  /** Stock mínimo escrito en la fila que se edita. */
  editMinimo = 0;

  /** Mensaje de éxito de la última acción. */
  aviso = '';
  /** Error de la última acción, separado del error de carga. */
  errorAccion = '';
  /** true mientras se guarda un cambio (deshabilita los botones). */
  guardando = false;

  /** Controla que solo cuente la respuesta de la consulta más reciente. */
  private ultima = new UltimaPeticion();

  constructor(private inventarioService: InventarioService, private sedeService: SedeService,
              private catalogoService: CatalogoService) {}

  /** Carga las sedes, el catálogo de medicamentos y el inventario inicial. */
  ngOnInit(): void {
    this.sedeService.getAll().subscribe(datos => this.sedes = datos);
    this.catalogoService.medicamentos(null).subscribe(datos => this.medicamentos = datos);
    this.consultar();
  }

  /** Pide el inventario de la sede elegida (o de todas). */
  consultar(): void {
    this.cargando = true;
    this.error = '';
    this.ultima.lanzar(this.inventarioService.getAll(this.sedeSeleccionada), {
      next: (datos) => { this.inventario = datos; this.cargando = false; },
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
    });
  }

  /** Quita el filtro de sede y el de stock bajo, y vuelve a pedir todo el inventario. */
  limpiar(): void {
    this.sedeSeleccionada = null;
    this.soloStockBajo = false;
    this.consultar();
  }

  /** Filas que se muestran en la tabla: todas, o solo las de stock bajo. */
  get itemsFiltrados(): ItemInventario[] {
    return this.soloStockBajo ? this.inventario.filter(i => i.stockBajo) : this.inventario;
  }

  /** Cuántas filas del inventario cargado tienen el stock por debajo del mínimo. */
  get cantidadStockBajo(): number {
    return this.inventario.filter(i => i.stockBajo).length;
  }

  /** Muestra u oculta el formulario de agregar; si hay una sede filtrada, la deja elegida. */
  abrirNuevo(): void {
    this.mostrarNuevo = !this.mostrarNuevo;
    this.nuevo = { idSede: this.sedeSeleccionada, idMedicamento: null, stockActual: 0, stockMinimo: 0 };
  }

  /** Existencias: enteros de 0 a 1.000.000 (CK_INVENTARIO_STOCK_ACTUAL y CK_INVENTARIO_STOCK_MINIMO). */
  existenciaValida(valor: number | null): boolean {
    return valor !== null && Number.isInteger(valor) && valor >= 0 && valor <= 1000000;
  }

  /** INSERT en INVENTARIO. Si la sede ya tenía el medicamento, la llave primaria lo impide (409). */
  agregar(): void {
    if (this.nuevo.idSede === null || this.nuevo.idMedicamento === null
        || !this.existenciaValida(this.nuevo.stockActual) || !this.existenciaValida(this.nuevo.stockMinimo)) {
      this.errorAccion = 'Elija la sede y el medicamento; las existencias deben ser números enteros de 0 en adelante.';
      return;
    }
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

  /** Pone una fila en modo edición con sus valores actuales. */
  editar(item: ItemInventario): void {
    this.editando = item;
    this.editStock = item.stockActual ?? 0;
    this.editMinimo = item.stockMinimo ?? 0;
    this.limpiarMensajes();
  }

  /** true si la fila dada es la que está en edición (se compara por la llave compuesta). */
  estaEditando(item: ItemInventario): boolean {
    return this.editando !== null && this.editando.idSede === item.idSede && this.editando.idMedicamento === item.idMedicamento;
  }

  /** UPDATE de las existencias y el mínimo (conteo físico o compra). */
  guardarEdicion(): void {
    if (!this.editando) {
      return;
    }
    const item = this.editando;
    if (!this.existenciaValida(this.editStock) || !this.existenciaValida(this.editMinimo)) {
      this.errorAccion = 'Las existencias deben ser números enteros de 0 en adelante.';
      return;
    }
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

  /** Borra el aviso y el error de la acción anterior antes de empezar otra. */
  private limpiarMensajes(): void {
    this.aviso = '';
    this.errorAccion = '';
  }
}
