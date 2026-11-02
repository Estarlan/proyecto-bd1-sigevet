import { Component, OnInit } from '@angular/core';
import { FormsModule, NgForm, NgModel } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
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
  selector: 'app-empleado-form',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './empleado-form.html'
})
export class EmpleadoFormPage implements OnInit {

  id: number | null = null;
  form: EmpleadoForm = {
    tipoDocumento: 'CC', numDocumento: '', nombres: '', apellidos: '', fechaNacimiento: '', telefono: '', email: '',
    tipoEmpleado: 'VET', fechaIngreso: new Date().toISOString().slice(0, 10), salario: null, idSede: null,
    tarjetaProfesional: null, universidad: null, anioGraduacion: null, especialidades: [],
    area: null, certificacion: null, turno: null
  };
  sedes: Sede[] = [];
  especialidades: OpcionCatalogo[] = [];

  tiposDocumento = ['CC', 'CE', 'PA', 'PPT'];
  tipos = [
    { codigo: 'VET', texto: 'Veterinario' },
    { codigo: 'AUX', texto: 'Auxiliar' },
    { codigo: 'ADM', texto: 'Administrativo' }
  ];
  areas = ['CLINICA', 'CIRUGIA', 'LABORATORIO', 'ESTETICA', 'HOSPITALIZACION'];
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
  intentado = false;
  hoy = fechaISO();
  /** Edad permitida: entre 16 y 80 años. */
  nacimientoMinimo = fechaISO(80);
  nacimientoMaximo = fechaISO(16);
  /** Año de graduación guardado al editar: si no cambia, no se le exige la regla de los 18 años (igual que el backend). */
  anioOriginal: number | null = null;

  cargando = false;
  guardando = false;
  error = '';

  constructor(private route: ActivatedRoute, private router: Router, private personalService: PersonalService,
              private catalogoService: CatalogoService, private sedeService: SedeService) {}

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

  invalido(campo: NgModel): boolean {
    return marcarInvalido(campo, this.intentado);
  }

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

  get ingresoInvalido(): boolean {
    return !!this.form.fechaIngreso && fechaFueraDeRango(this.form.fechaIngreso, this.ingresoMinimo, this.hoy);
  }

  /** Rango del año de graduación: CHECK 1970–2026, no futuro y con al menos 18 años (salvo que no cambie al editar). */
  get anioMaximo(): number {
    return Math.min(2026, new Date().getFullYear());
  }

  get anioMinimo(): number {
    if (this.id !== null && this.form.anioGraduacion === this.anioOriginal) {
      return 1970;
    }
    const nacimiento = this.form.fechaNacimiento ? Number(this.form.fechaNacimiento.slice(0, 4)) : 1952;
    return Math.max(1970, nacimiento + 18);
  }

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
