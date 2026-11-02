import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Observable } from 'rxjs';
import { CatalogoService, PersonalService, SedeService, mensajeDeError } from '../../services/api.service';
import { EmpleadoForm, IdRespuesta, OpcionCatalogo, Sede } from '../../models/models';

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
        next: (datos) => { this.form = datos; this.cargando = false; },
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

  guardar(): void {
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
