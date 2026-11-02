import { Component, OnInit } from '@angular/core';
import { NgForm, NgModel } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { CatalogoService, CitaService, MascotaService, SedeService, mensajeDeError } from '../../services/api.service';
import { CitaForm, Mascota, OpcionCatalogo, Sede } from '../../models/models';
import { marcarInvalido } from '../../shared/validaciones';

/**
 * Agendar una cita (/agenda/nueva?mascota=ID). Al elegir la sede se cargan solo los veterinarios que laboran en ella:
 * es la regla RN-08, que además la base hace cumplir con el disparador TRG_CITA_VETERINARIO_SEDE_INS.
 */
@Component({
  selector: 'app-cita-form',
  standalone: false,
  templateUrl: './cita-form.html'
})
export class CitaFormPage implements OnInit {

  form: CitaForm = { idMascota: null, idServicio: null, idSede: null, idVeterinario: null, fechaHora: '', motivo: null };
  mascotas: Mascota[] = [];
  servicios: OpcionCatalogo[] = [];
  sedes: Sede[] = [];
  veterinarios: OpcionCatalogo[] = [];
  /** Rango permitido aaaa-mm-ddThh:mm: desde ahora hasta dentro de un año (igual que el backend). */
  ahora = '';
  enUnAnio = '';
  intentado = false;

  guardando = false;
  error = '';

  constructor(private route: ActivatedRoute, private router: Router, private citaService: CitaService,
              private mascotaService: MascotaService, private catalogoService: CatalogoService,
              private sedeService: SedeService) {}

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
      this.catalogoService.veterinarios(this.form.idSede).subscribe(datos => this.veterinarios = datos);
    }
  }

  invalido(campo: NgModel): boolean {
    return marcarInvalido(campo, this.intentado);
  }

  /** aaaa-mm-ddThh:mm se puede comparar como texto. */
  get fechaInvalida(): boolean {
    return !!this.form.fechaHora && (this.form.fechaHora <= this.ahora || this.form.fechaHora > this.enUnAnio);
  }

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
