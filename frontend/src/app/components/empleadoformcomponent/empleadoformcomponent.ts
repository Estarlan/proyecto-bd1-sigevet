import { Component, OnInit } from '@angular/core';
import { NgForm, NgModel } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { CatalogoService, PersonalService, SedeService, mensajeDeError } from '../../services/api.service';
import { EmpleadoForm, IdRespuesta, OpcionCatalogo, Sede } from '../../models/models';
import { MENSAJES, PATRONES, fechaFueraDeRango, fechaISO, marcarInvalido, mensajeDocumento, patronDocumento } from '../../shared/validaciones';

/**
 * Crear (/personal/nuevo) o editar (/personal/:id/editar) un empleado. Según el tipo se muestran los campos del
 * subtipo: VETERINARIO (con sus especialidades, relación N:M) o AUXILIAR. Al editar, el tipo no se puede cambiar
 * (regla RN-09).
 */
@Component({
  selector: 'app-empleadoformcomponent',
  standalone: false,
  templateUrl: './empleadoformcomponent.html'
})
export class Empleadoformcomponent implements OnInit {

  /** null al crear; el id del empleado al editar. */
  id: number | null = null;
  /** Datos del empleado que se está escribiendo; es lo que se envía al guardar. */
  form: EmpleadoForm = {
    tipoDocumento: 'CC', numDocumento: '', nombres: '', apellidos: '', fechaNacimiento: '', telefono: '', email: '',
    // fechaISO() da la fecha de hoy en hora local; toISOString() usa UTC y después de las 7 p. m. en Colombia ya es "mañana"
    tipoEmpleado: 'VET', fechaIngreso: fechaISO(), salario: null, idSede: null,
    tarjetaProfesional: null, universidad: null, anioGraduacion: null, especialidades: [],
    area: null, certificacion: null, turno: null
  };
  /** Sedes para el select. */
  sedes: Sede[] = [];
  /** Catálogo de especialidades (para los veterinarios). */
  especialidades: OpcionCatalogo[] = [];

  /** Tipos de documento permitidos (CHECK de la tabla PERSONA). */
  tiposDocumento = ['CC', 'CE', 'PA', 'PPT'];
  /** Subtipos de empleado: código que se guarda y texto que se muestra. */
  tipos = [
    { codigo: 'VET', texto: 'Veterinario' },
    { codigo: 'AUX', texto: 'Auxiliar' },
    { codigo: 'ADM', texto: 'Administrativo' }
  ];
  /** Áreas permitidas para un auxiliar. */
  areas = ['CLINICA', 'CIRUGIA', 'LABORATORIO', 'ESTETICA', 'HOSPITALIZACION'];
  /** Turnos permitidos para un auxiliar. */
  turnos = [
    { valor: 'MANANA', texto: 'Mañana' },
    { valor: 'TARDE', texto: 'Tarde' },
    { valor: 'NOCHE', texto: 'Noche' }
  ];

  // reglas compartidas con el backend (shared/validaciones.ts y Validador.java)
  patrones = PATRONES;
  mensajes = MENSAJES;
  patronDocumento = patronDocumento;
  mensajeDocumento = mensajeDocumento;
  /** Se vuelve true al presionar Guardar: desde ahí se marcan en rojo todos los campos con error. */
  intentado = false;
  /** Fecha de hoy (hora local) en formato aaaa-mm-dd. */
  hoy = fechaISO();
  /** Edad permitida: entre 16 y 80 años. */
  nacimientoMinimo = fechaISO(80);
  /** Fecha de nacimiento más reciente permitida (16 años cumplidos). */
  nacimientoMaximo = fechaISO(16);
  /** Año de graduación guardado al editar: si no cambia, no se le exige la regla de los 18 años (igual que el backend). */
  anioOriginal: number | null = null;

  /** true mientras se carga el empleado que se edita. */
  cargando = false;
  /** true mientras se envía el formulario (deshabilita el botón). */
  guardando = false;
  /** Mensaje de error para mostrar arriba del formulario. */
  error = '';

  constructor(private route: ActivatedRoute, private router: Router, private personalService: PersonalService,
              private catalogoService: CatalogoService, private sedeService: SedeService) {}

  /** Carga los catálogos y, si la dirección trae un id, los datos del empleado para editarlo. */
  ngOnInit(): void {
    this.sedeService.getAll().subscribe(datos => this.sedes = datos);
    this.catalogoService.especialidades().subscribe(datos => this.especialidades = datos);
    const parametro = this.route.snapshot.paramMap.get('id');
    if (parametro) {
      this.id = Number(parametro);
      this.cargando = true;
      this.personalService.getFormulario(this.id).subscribe({
        next: (datos) => { this.form = datos; this.anioOriginal = datos.anioGraduacion; this.cargando = false; },
        error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
      });
    }
  }

  /** true si el empleado ya tiene marcada esa especialidad. */
  tieneEspecialidad(id: number): boolean {
    return this.form.especialidades.includes(id);
  }

  /** Marca o desmarca una especialidad (fila de VETERINARIO_ESPECIALIDAD). */
  cambiarEspecialidad(id: number, marcada: boolean): void {
    if (marcada && !this.tieneEspecialidad(id)) {
      this.form.especialidades.push(id);
    }
    if (!marcada) {
      this.form.especialidades = this.form.especialidades.filter(e => e !== id);
    }
  }

  /** true si un campo debe verse en rojo (tiene error y ya se tocó o se intentó guardar). */
  invalido(campo: NgModel): boolean {
    return marcarInvalido(campo, this.intentado);
  }

  /** true si la fecha de nacimiento está fuera del rango de 16 a 80 años. */
  get nacimientoInvalido(): boolean {
    return !!this.form.fechaNacimiento && fechaFueraDeRango(this.form.fechaNacimiento, this.nacimientoMinimo, this.nacimientoMaximo);
  }

  /** La fecha de ingreso no puede ser futura ni de antes de que el empleado cumpliera 16 años. */
  get ingresoMinimo(): string {
    if (!this.form.fechaNacimiento) {
      return '1900-01-01';
    }
    const anio = Number(this.form.fechaNacimiento.slice(0, 4)) + 16;
    return anio + this.form.fechaNacimiento.slice(4);
  }

  /** true si la fecha de ingreso está fuera de su rango permitido. */
  get ingresoInvalido(): boolean {
    return !!this.form.fechaIngreso && fechaFueraDeRango(this.form.fechaIngreso, this.ingresoMinimo, this.hoy);
  }

  /** Rango del año de graduación: CHECK 1970–2026, no futuro y con al menos 18 años (salvo que no cambie al editar). */
  get anioMaximo(): number {
    return Math.min(2026, new Date().getFullYear());
  }

  /** Año de graduación más antiguo permitido (1970, o 18 años después del nacimiento si es mayor). */
  get anioMinimo(): number {
    if (this.id !== null && this.form.anioGraduacion === this.anioOriginal) {
      return 1970;
    }
    const nacimiento = this.form.fechaNacimiento ? Number(this.form.fechaNacimiento.slice(0, 4)) : 1952;
    return Math.max(1970, nacimiento + 18);
  }

  /** Valida el formulario y, si está bien, crea o actualiza el empleado y vuelve a la lista de personal. */
  guardar(f: NgForm): void {
    this.intentado = true;
    if (f.invalid || this.nacimientoInvalido || this.ingresoInvalido) {
      this.error = 'Revise los campos marcados en rojo.';
      return;
    }
    this.guardando = true;
    this.error = '';
    const peticion: Observable<IdRespuesta | void> = this.id === null
      ? this.personalService.crear(this.form)
      : this.personalService.actualizar(this.id, this.form);
    peticion.subscribe({
      next: () => this.router.navigate(['/personal'], { queryParams: { guardado: 1 } }),
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.guardando = false; }
    });
  }
}
