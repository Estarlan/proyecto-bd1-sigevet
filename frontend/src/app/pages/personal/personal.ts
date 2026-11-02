import { Component, OnInit } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { PersonalService, SedeService, mensajeDeError } from '../../services/api.service';
import { Empleado, Sede } from '../../models/models';

/**
 * Personal de la red. La API devuelve una lista de Empleado, pero cada elemento es en Java un objeto de su
 * clase real (Veterinario, Auxiliar o Empleado administrativo). Por eso en el JSON cada uno trae los campos de
 * su subtipo y su propia "descripcion", que sale del método getDescripcion() sobrescrito en cada clase.
 */
@Component({
  selector: 'app-personal',
  standalone: true,
  imports: [FormsModule, CurrencyPipe, RouterLink],
  templateUrl: './personal.html',
  styleUrl: './personal.css'
})
export class Personal implements OnInit {

  empleados: Empleado[] = [];
  sedes: Sede[] = [];

  sedeSeleccionada: number | null = null;
  /** Filtro por subtipo que se aplica en el navegador sobre la lista ya cargada: '' = todos. */
  tipoSeleccionado = '';

  tipos = [
    { codigo: 'VET', texto: 'Veterinarios' },
    { codigo: 'AUX', texto: 'Auxiliares' },
    { codigo: 'ADM', texto: 'Administrativos' }
  ];

  cargando = true;
  error = '';

  aviso = '';
  errorAccion = '';

  constructor(private route: ActivatedRoute, private personalService: PersonalService, private sedeService: SedeService) {}

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
    this.personalService.getAll(this.sedeSeleccionada).subscribe({
      next: (datos) => { this.empleados = datos; this.cargando = false; },
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
    });
  }

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
