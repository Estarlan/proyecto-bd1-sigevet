import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Observable } from 'rxjs';
import { CatalogoService, ClienteService, MascotaService, mensajeDeError } from '../../services/api.service';
import { Cliente, IdRespuesta, MascotaForm, OpcionCatalogo } from '../../models/models';

/** Grupo de razas de una especie, para el <optgroup> del select. */
interface GrupoRazas {
  especie: string;
  razas: OpcionCatalogo[];
}

/**
 * Formulario para crear (/mascotas/nueva?cliente=ID) o editar (/mascotas/:id/editar) una mascota junto con su
 * historia clínica (relación 1:1 TENER). El backend guarda las dos filas en la misma transacción.
 */
@Component({
  selector: 'app-mascota-form',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './mascota-form.html'
})
export class MascotaFormPage implements OnInit {

  id: number | null = null;
  form: MascotaForm = {
    nombre: '', fechaNacimiento: null, sexo: 'M', color: null, microchip: null, esterilizado: 'N',
    idRaza: null, idCliente: null, grupoSanguineo: null, alergias: null, enfermedadesCronicas: null, observaciones: null
  };
  clientes: Cliente[] = [];
  gruposRazas: GrupoRazas[] = [];
  /** Hoy en formato aaaa-mm-dd, para que el calendario no deje elegir una fecha futura. */
  hoy = new Date().toISOString().slice(0, 10);

  cargando = false;
  guardando = false;
  error = '';

  constructor(private route: ActivatedRoute, private router: Router, private mascotaService: MascotaService,
              private clienteService: ClienteService, private catalogoService: CatalogoService) {}

  ngOnInit(): void {
    this.clienteService.getAll('').subscribe(datos => this.clientes = datos);
    this.catalogoService.razas().subscribe(datos => this.agruparRazas(datos));

    const parametro = this.route.snapshot.paramMap.get('id');
    if (parametro) {
      this.id = Number(parametro);
      this.cargando = true;
      this.mascotaService.getFormulario(this.id).subscribe({
        next: (datos) => { this.form = datos; this.cargando = false; },
        error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
      });
    } else {
      // desde el detalle de un cliente llega ?cliente=ID para dejarlo seleccionado
      const cliente = this.route.snapshot.queryParamMap.get('cliente');
      if (cliente) {
        this.form.idCliente = Number(cliente);
      }
    }
  }

  /** Agrupa las razas por especie (el catálogo trae la especie en "detalle"). */
  agruparRazas(razas: OpcionCatalogo[]): void {
    const grupos: GrupoRazas[] = [];
    for (const raza of razas) {
      const especie = raza.detalle ?? 'Otra';
      let grupo = grupos.find(g => g.especie === especie);
      if (!grupo) {
        grupo = { especie, razas: [] };
        grupos.push(grupo);
      }
      grupo.razas.push(raza);
    }
    this.gruposRazas = grupos;
  }

  get rutaVolver(): (string | number)[] {
    if (this.id !== null) {
      return ['/mascotas', this.id];
    }
    return this.form.idCliente !== null ? ['/clientes', this.form.idCliente] : ['/mascotas'];
  }

  guardar(): void {
    this.guardando = true;
    this.error = '';
    const peticion: Observable<IdRespuesta | void> = this.id === null
      ? this.mascotaService.crear(this.form)
      : this.mascotaService.actualizar(this.id, this.form);
    peticion.subscribe({
      next: (respuesta) => {
        const idMascota = this.id ?? (respuesta as IdRespuesta).id;
        this.router.navigate(['/mascotas', idMascota]);
      },
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.guardando = false; }
    });
  }
}
