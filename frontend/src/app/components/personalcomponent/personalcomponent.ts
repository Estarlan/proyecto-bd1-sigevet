import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { PersonalService, SedeService, mensajeDeError } from '../../services/api.service';
import { Empleado, Sede } from '../../models/models';
import { UltimaPeticion } from '../../shared/ultima-peticion';

/**
 * Personal de la red. La API devuelve una lista de Empleado, pero cada elemento es en Java un objeto de su
 * clase real (Veterinario, Auxiliar o Empleado administrativo). Por eso en el JSON cada uno trae los campos de
 * su subtipo y su propia "descripcion", que sale del método getDescripcion() sobrescrito en cada clase.
 */
@Component({
  selector: 'app-personalcomponent',
  standalone: false,
  templateUrl: './personalcomponent.html',
  styleUrl: './personalcomponent.css'
})
export class Personalcomponent implements OnInit {

  /** Empleados de la sede elegida (o de todas), tal como los devolvió la API. */
  empleados: Empleado[] = [];
  /** Sedes para el filtro y para la tabla de directores. */
  sedes: Sede[] = [];

  /** Filtro de sede: null significa "todas". */
  sedeSeleccionada: number | null = null;
  /** Filtro por subtipo que se aplica en el navegador sobre la lista ya cargada: '' = todos. */
  tipoSeleccionado = '';

  /** Subtipos de empleado (discriminador tipo_empleado) para los botones del filtro. */
  tipos = [
    { codigo: 'VET', texto: 'Veterinarios' },
    { codigo: 'AUX', texto: 'Auxiliares' },
    { codigo: 'ADM', texto: 'Administrativos' }
  ];

  /** true mientras se espera la lista de empleados. */
  cargando = true;
  /** Error de la carga de la lista. */
  error = '';

  /** Mensaje de éxito de la última acción. */
  aviso = '';
  /** Error de la última acción, separado del error de carga. */
  errorAccion = '';

  /** Controla que solo cuente la respuesta de la consulta más reciente. */
  private ultima = new UltimaPeticion();

  constructor(private route: ActivatedRoute, private personalService: PersonalService, private sedeService: SedeService) {}

  /** Avisa si se llegó desde el formulario de empleado, carga las sedes y pide el personal. */
  ngOnInit(): void {
    if (this.route.snapshot.queryParamMap.get('guardado')) {
      this.aviso = 'Los datos del empleado quedaron guardados.';
    }
    this.sedeService.getAll().subscribe(datos => this.sedes = datos);
    this.consultar();
  }

  /** Pide el personal de la sede elegida (el filtro de sede lo aplica el SQL del DAO). */
  consultar(): void {
    this.cargando = true;
    this.error = '';
    this.ultima.lanzar(this.personalService.getAll(this.sedeSeleccionada), {
      next: (datos) => { this.empleados = datos; this.cargando = false; },
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
    });
  }

  /** Quita los filtros de sede y de subtipo, y vuelve a pedir todo el personal. */
  limpiar(): void {
    this.sedeSeleccionada = null;
    this.tipoSeleccionado = '';
    this.consultar();
  }

  /** Empleados que se muestran en la tabla, según el filtro de subtipo. */
  get empleadosFiltrados(): Empleado[] {
    if (this.tipoSeleccionado === '') {
      return this.empleados;
    }
    return this.empleados.filter(e => e.tipoEmpleado === this.tipoSeleccionado);
  }

  /** Cuántos empleados hay de un subtipo en la lista cargada. */
  cantidad(tipo: string): number {
    return this.empleados.filter(e => e.tipoEmpleado === tipo).length;
  }

  /** Nombre legible del discriminador tipo_empleado (CHECK CK_EMPLEADO_TIPO_EMPLEADO). */
  textoTipo(tipo?: string): string {
    switch (tipo) {
      case 'VET': return 'Veterinario';
      case 'AUX': return 'Auxiliar';
      case 'ADM': return 'Administrativo';
      default: return '—';
    }
  }

  /** Sedes que se muestran en la tabla de directores, según el filtro de sede. */
  get sedesFiltradas(): Sede[] {
    if (this.sedeSeleccionada === null) {
      return this.sedes;
    }
    return this.sedes.filter(s => s.idSede === this.sedeSeleccionada);
  }

  /**
   * Borrado lógico de un empleado: ACTIVO o INACTIVO. El backend no deja inactivar a un director de sede ni a un
   * veterinario con citas programadas.
   */
  cambiarEstado(e: Empleado): void {
    const nuevo = e.estado === 'ACTIVO' ? 'INACTIVO' : 'ACTIVO';
    if (nuevo === 'INACTIVO' && !confirm('¿Inactivar a ' + e.nombreCompleto + '?')) {
      return;
    }
    this.aviso = '';
    this.errorAccion = '';
    this.personalService.cambiarEstado(e.idPersona, nuevo).subscribe({
      next: () => { this.aviso = e.nombreCompleto + ' quedó ' + nuevo + '.'; this.consultar(); },
      error: (err: HttpErrorResponse) => { this.errorAccion = mensajeDeError(err); }
    });
  }
}
