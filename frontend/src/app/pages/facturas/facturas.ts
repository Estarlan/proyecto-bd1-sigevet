import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FacturaService, SedeService, mensajeDeError } from '../../services/api.service';
import { Factura, Sede } from '../../models/models';
import { UltimaPeticion } from '../../shared/ultima-peticion';

/** Lista de facturas (/facturas) con filtros por sede y estado y la suma de lo que se está mostrando. */
@Component({
  selector: 'app-facturas',
  standalone: false,
  templateUrl: './facturas.html'
})
export class Facturas implements OnInit {

  /** Facturas que se muestran en la tabla, ya filtradas por el SQL del DAO. */
  facturas: Factura[] = [];
  /** Sedes para el filtro (salen de la tabla SEDE). */
  sedes: Sede[] = [];
  /** Estados para el filtro (salen del CHECK CK_FACTURA_ESTADO). */
  estados: string[] = [];

  // filtros: null y '' significan "sin filtro"
  /** Filtro de sede: null significa "todas". */
  sedeSeleccionada: number | null = null;
  /** Filtro de estado: '' significa "todos". */
  estadoSeleccionado = '';

  /** true mientras se espera la lista de facturas. */
  cargando = true;
  /** Error de la última consulta; vacío si todo salió bien. */
  error = '';

  /** Controla que solo cuente la respuesta del filtro más reciente. */
  private ultima = new UltimaPeticion();

  constructor(private facturaService: FacturaService, private sedeService: SedeService) {}

  /** Carga las opciones de los filtros y la lista inicial. */
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
    this.ultima.lanzar(this.facturaService.getAll(this.sedeSeleccionada, this.estadoSeleccionado), {
      next: (datos) => { this.facturas = datos; this.cargando = false; },
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
    });
  }

  /** Quita los dos filtros y vuelve a pedir todas las facturas. */
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
