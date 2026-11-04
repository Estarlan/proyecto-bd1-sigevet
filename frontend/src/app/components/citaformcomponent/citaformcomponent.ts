import { Component, OnInit } from '@angular/core';
import { NgForm, NgModel } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { CatalogoService, CitaService, MascotaService, SedeService, mensajeDeError } from '../../services/api.service';
import { CitaForm, Mascota, OpcionCatalogo, Sede } from '../../models/models';
import { marcarInvalido } from '../../shared/validaciones';
import { UltimaPeticion } from '../../shared/ultima-peticion';

/**
 * Agendar una cita (/agenda/nueva?mascota=ID). Al elegir la sede se cargan solo los veterinarios que laboran en ella:
 * es la regla RN-08, que además la base hace cumplir con el disparador TRG_CITA_VETERINARIO_SEDE_INS.
 */
@Component({
  selector: 'app-citaformcomponent',
  standalone: false,
  templateUrl: './citaformcomponent.html'
})
export class Citaformcomponent implements OnInit {

  /** Datos de la cita que se está escribiendo; es lo que se envía al guardar. */
  form: CitaForm = { idMascota: null, idServicio: null, idSede: null, idVeterinario: null, fechaHora: '', motivo: null };
  /** Mascotas ACTIVAS para el select (solo ellas pueden tener citas). */
  mascotas: Mascota[] = [];
  /** Catálogo de servicios para el select. */
  servicios: OpcionCatalogo[] = [];
  /** Sedes para el select. */
  sedes: Sede[] = [];
  /** Veterinarios de la sede elegida (se llenan al elegir la sede). */
  veterinarios: OpcionCatalogo[] = [];
  /** Rango permitido aaaa-mm-ddThh:mm: desde ahora hasta dentro de un año (igual que el backend). */
  ahora = '';
  /** Límite superior del rango: dentro de un año. */
  enUnAnio = '';
  /** Se vuelve true al presionar Guardar: desde ahí se marcan en rojo todos los campos con error. */
  intentado = false;

  /** true mientras se envía la cita (deshabilita el botón). */
  guardando = false;
  /** Mensaje de error para mostrar arriba del formulario. */
  error = '';

  /** Controla que solo cuente la lista de veterinarios de la última sede elegida. */
  private ultimaSede = new UltimaPeticion();

  constructor(private route: ActivatedRoute, private router: Router, private citaService: CitaService,
              private mascotaService: MascotaService, private catalogoService: CatalogoService,
              private sedeService: SedeService) {}

  /** Calcula el rango de fechas, carga los catálogos y deja elegida la mascota si llegó en la dirección. */
  ngOnInit(): void {
    const fecha = new Date();
    fecha.setMinutes(fecha.getMinutes() - fecha.getTimezoneOffset());   // hora local, no UTC
    this.ahora = fecha.toISOString().slice(0, 16);
    fecha.setFullYear(fecha.getFullYear() + 1);
    this.enUnAnio = fecha.toISOString().slice(0, 16);

    // solo las mascotas ACTIVAS pueden tener citas
    this.mascotaService.getAll('').subscribe(datos => this.mascotas = datos.filter(m => m.estado === 'ACTIVA'));
    this.catalogoService.servicios().subscribe(datos => this.servicios = datos);
    this.sedeService.getAll().subscribe(datos => this.sedes = datos);

    const mascota = this.route.snapshot.queryParamMap.get('mascota');
    if (mascota) {
      this.form.idMascota = Number(mascota);
    }
  }

  /** Al cambiar la sede se reinicia el veterinario y se cargan los de esa sede. */
  cambiarSede(): void {
    this.form.idVeterinario = null;
    this.veterinarios = [];
    if (this.form.idSede !== null) {
      this.ultimaSede.lanzar(this.catalogoService.veterinarios(this.form.idSede), {
        next: (datos) => this.veterinarios = datos
      });
    }
  }

  /** true si un campo debe verse en rojo (tiene error y ya se tocó o se intentó guardar). */
  invalido(campo: NgModel): boolean {
    return marcarInvalido(campo, this.intentado);
  }

  /** aaaa-mm-ddThh:mm se puede comparar como texto. */
  get fechaInvalida(): boolean {
    return !!this.form.fechaHora && (this.form.fechaHora <= this.ahora || this.form.fechaHora > this.enUnAnio);
  }

  /** Valida el formulario y, si está bien, crea la cita y vuelve a la agenda. */
  guardar(f: NgForm): void {
    this.intentado = true;
    if (f.invalid || this.fechaInvalida) {
      this.error = 'Revise los campos marcados en rojo.';
      return;
    }
    this.guardando = true;
    this.error = '';
    this.citaService.crear(this.form).subscribe({
      next: () => this.router.navigate(['/agenda'], { queryParams: { creada: 1 } }),
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.guardando = false; }
    });
  }
}
