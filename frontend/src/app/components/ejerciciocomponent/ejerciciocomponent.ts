import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { AlgebraService, mensajeDeError } from '../../services/api.service';
import { EjercicioDetalle } from '../../models/models';
import { UltimaPeticion } from '../../shared/ultima-peticion';

/**
 * Un ejercicio de álgebra relacional: enunciado, consulta SQL equivalente y el resultado de ejecutarla en MySQL
 * con el usuario de la aplicación vet_app (el lector solo acepta consultas SELECT). La tabla de resultados se arma con las columnas que devuelve la consulta.
 */
@Component({
  selector: 'app-ejerciciocomponent',
  standalone: false,
  templateUrl: './ejerciciocomponent.html',
  styleUrl: './ejerciciocomponent.css'
})
export class Ejerciciocomponent implements OnInit {

  /** Enunciado, SQL y resultado del ejercicio; null mientras carga o si falló la carga. */
  detalle: EjercicioDetalle | null = null;
  /** Número del ejercicio que se muestra (el de la dirección /algebra/:numero). */
  numero = 0;
  /** Números de todos los ejercicios, para los botones de anterior y siguiente. */
  numeros: number[] = [];

  /** true mientras se espera la respuesta de la API. */
  cargando = true;
  /** Error de la carga. */
  error = '';

  /** Controla que solo cuente la respuesta del último ejercicio pedido (anterior y siguiente seguidos). */
  private ultima = new UltimaPeticion();

  constructor(private route: ActivatedRoute, private algebraService: AlgebraService) {}

  /** Carga la lista de números y el ejercicio de la dirección; se repite cada vez que cambia el número. */
  ngOnInit(): void {
    this.algebraService.getAll().subscribe(datos => this.numeros = datos.ejercicios.map(e => e.numero));
    // paramMap cambia al usar anterior/siguiente sin salir de la página
    this.route.paramMap.subscribe(parametros => {
      this.numero = Number(parametros.get('numero'));
      this.cargar();
    });
  }

  /** Pide el ejercicio actual: enunciado, SQL y resultado de ejecutarlo. */
  cargar(): void {
    this.cargando = true;
    this.error = '';
    this.detalle = null;
    this.ultima.lanzar(this.algebraService.getDetalle(this.numero), {
      next: (datos) => { this.detalle = datos; this.cargando = false; },
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
    });
  }

  /** Número del ejercicio anterior, o null si este es el primero. */
  get anterior(): number | null {
    const posicion = this.numeros.indexOf(this.numero);
    return posicion > 0 ? this.numeros[posicion - 1] : null;
  }

  /** Número del ejercicio siguiente, o null si este es el último. */
  get siguiente(): number | null {
    const posicion = this.numeros.indexOf(this.numero);
    return posicion >= 0 && posicion < this.numeros.length - 1 ? this.numeros[posicion + 1] : null;
  }
}
