import { Component, OnInit } from '@angular/core';
import { NgForm, NgModel } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { CatalogoService, ClienteService, MascotaService, mensajeDeError } from '../../services/api.service';
import { Cliente, IdRespuesta, MascotaForm, OpcionCatalogo } from '../../models/models';
import { MENSAJES, PATRONES, fechaFueraDeRango, fechaISO, marcarInvalido } from '../../shared/validaciones';

/** Grupo de razas de una especie, para el <optgroup> del select. */
interface GrupoRazas {
  /** Nombre de la especie (perro, gato...). */
  especie: string;
  /** Razas que pertenecen a esa especie. */
  razas: OpcionCatalogo[];
}

/**
 * Formulario para crear (/mascotas/nueva?cliente=ID) o editar (/mascotas/:id/editar) una mascota junto con su
 * historia clínica (relación 1:1 TENER). El backend guarda las dos filas en la misma transacción.
 */
@Component({
  selector: 'app-mascotaformcomponent',
  standalone: false,
  templateUrl: './mascotaformcomponent.html'
})
export class Mascotaformcomponent implements OnInit {

  /** null al crear; el id de la mascota al editar. */
  id: number | null = null;
  /** Datos de la mascota y de su historia clínica; es lo que se envía al guardar. */
  form: MascotaForm = {
    nombre: '', fechaNacimiento: null, sexo: 'M', color: null, microchip: null, esterilizado: 'N',
    idRaza: null, idCliente: null, grupoSanguineo: null, alergias: null, enfermedadesCronicas: null, observaciones: null
  };
  /** Clientes para elegir el propietario (la API devuelve como máximo 100). */
  clientes: Cliente[] = [];
  /** Razas agrupadas por especie para el select. */
  gruposRazas: GrupoRazas[] = [];
  /** Rango de la fecha de nacimiento: ni futura ni de hace más de 40 años (igual que el backend). */
  hoy = fechaISO();
  /** Límite inferior del rango: hace 40 años. */
  hace40 = fechaISO(40);

  // reglas de formato compartidas con el backend (shared/validaciones.ts y Validador.java)
  patrones = PATRONES;
  mensajes = MENSAJES;
  /** Se vuelve true al presionar Guardar: desde ahí se marcan en rojo todos los campos con error. */
  intentado = false;

  /** true mientras se carga la mascota que se edita. */
  cargando = false;
  /** true mientras se envía el formulario (deshabilita el botón). */
  guardando = false;
  /** Mensaje de error para mostrar arriba del formulario. */
  error = '';

  constructor(private route: ActivatedRoute, private router: Router, private mascotaService: MascotaService,
              private clienteService: ClienteService, private catalogoService: CatalogoService) {}

  /** Carga clientes y razas y, si la dirección trae un id, los datos de la mascota para editarla. */
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

  /** Dirección del botón "volver": la mascota si se edita, el cliente si se llegó desde él, o la lista. */
  get rutaVolver(): (string | number)[] {
    if (this.id !== null) {
      return ['/mascotas', this.id];
    }
    return this.form.idCliente !== null ? ['/clientes', this.form.idCliente] : ['/mascotas'];
  }

  /** true si un campo debe verse en rojo (tiene error y ya se tocó o se intentó guardar). */
  invalido(campo: NgModel): boolean {
    return marcarInvalido(campo, this.intentado);
  }

  /** true si la fecha de nacimiento es futura o de hace más de 40 años. */
  get fechaInvalida(): boolean {
    return fechaFueraDeRango(this.form.fechaNacimiento, this.hace40, this.hoy);
  }

  /** Valida el formulario y, si está bien, crea o actualiza la mascota y abre su detalle. */
  guardar(f: NgForm): void {
    this.intentado = true;
    if (f.invalid || this.fechaInvalida) {
      this.error = 'Revise los campos marcados en rojo.';
      return;
    }
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
