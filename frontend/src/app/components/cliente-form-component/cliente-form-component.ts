import { Component, OnInit } from '@angular/core';
import { NgForm, NgModel } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { CatalogoService, ClienteService, mensajeDeError } from '../../services/api.service';
import { ClienteForm, IdRespuesta, OpcionCatalogo } from '../../models/models';
import { MENSAJES, PATRONES, marcarInvalido, mensajeDocumento, patronDocumento } from '../../shared/validaciones';

/**
 * Formulario para crear (/clientes/nuevo) o editar (/clientes/:id/editar) un cliente con sus teléfonos.
 * Los teléfonos son un atributo multivaluado (tabla TELEFONO_CLIENTE): se envían en la misma petición y el
 * backend los guarda en la misma transacción que el cliente.
 */
@Component({
  selector: 'app-cliente-form-component',
  standalone: false,
  templateUrl: './cliente-form-component.html'
})
export class ClienteFormComponent implements OnInit {

  /** null al crear; el id del cliente al editar. */
  id: number | null = null;
  /** Datos del cliente que se está escribiendo; es lo que se envía al guardar. */
  form: ClienteForm = {
    tipoDocumento: 'CC', numDocumento: '', nombres: '', apellidos: '', email: '', direccion: '',
    idCiudad: null, telefonos: [{ telefono: '', tipo: 'MOVIL' }]
  };
  /** Ciudades para el select. */
  ciudades: OpcionCatalogo[] = [];

  // valores permitidos por las restricciones CHECK de la base
  /** Tipos de documento permitidos (CHECK de la tabla PERSONA). */
  tiposDocumento = ['CC', 'CE', 'PA', 'PPT'];
  /** Tipos de teléfono permitidos (CHECK de TELEFONO_CLIENTE). */
  tiposTelefono = ['MOVIL', 'FIJO', 'TRABAJO'];

  // reglas de formato compartidas con el backend (shared/validaciones.ts y Validador.java)
  patrones = PATRONES;
  mensajes = MENSAJES;
  patronDocumento = patronDocumento;
  mensajeDocumento = mensajeDocumento;
  /** Se vuelve true al presionar Guardar: desde ahí se marcan en rojo todos los campos con error. */
  intentado = false;

  /** true mientras se carga el cliente que se edita. */
  cargando = false;
  /** true mientras se envía el formulario (deshabilita el botón). */
  guardando = false;
  /** Mensaje de error para mostrar arriba del formulario. */
  error = '';

  constructor(private route: ActivatedRoute, private router: Router,
              private clienteService: ClienteService, private catalogoService: CatalogoService) {}

  /** Carga las ciudades y, si la dirección trae un id, los datos del cliente para editarlo. */
  ngOnInit(): void {
    this.catalogoService.ciudades().subscribe(datos => this.ciudades = datos);
    const parametro = this.route.snapshot.paramMap.get('id');
    if (parametro) {
      this.id = Number(parametro);
      this.cargando = true;
      this.clienteService.getFormulario(this.id).subscribe({
        next: (datos) => { this.form = datos; this.cargando = false; },
        error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
      });
    }
  }

  /** Agrega una fila vacía de teléfono. */
  agregarTelefono(): void {
    this.form.telefonos.push({ telefono: '', tipo: 'MOVIL' });
  }

  /** Quita la fila de teléfono que está en esa posición. */
  quitarTelefono(posicion: number): void {
    this.form.telefonos.splice(posicion, 1);
  }

  /** true si un campo debe verse en rojo (tiene error y ya se tocó o se intentó guardar). */
  invalido(campo: NgModel): boolean {
    return marcarInvalido(campo, this.intentado);
  }

  /** Valida el formulario y, si está bien, crea o actualiza el cliente y abre su detalle. */
  guardar(f: NgForm): void {
    this.intentado = true;
    if (f.invalid) {
      this.error = 'Revise los campos marcados en rojo.';
      return;
    }
    this.guardando = true;
    this.error = '';
    // los teléfonos sin número no se envían
    const datos: ClienteForm = { ...this.form, telefonos: this.form.telefonos.filter(t => t.telefono.trim() !== '') };
    // crear devuelve { id }; actualizar no devuelve cuerpo
    const peticion: Observable<IdRespuesta | void> = this.id === null
      ? this.clienteService.crear(datos)
      : this.clienteService.actualizar(this.id, datos);
    peticion.subscribe({
      next: (respuesta) => {
        const idCliente = this.id ?? (respuesta as IdRespuesta).id;
        this.router.navigate(['/clientes', idCliente]);
      },
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.guardando = false; }
    });
  }
}
