import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { AlgebraService, mensajeDeError } from '../../services/api.service';
import { EjercicioDetalle } from '../../models/models';

/**
 * Un ejercicio de álgebra relacional: enunciado, consulta SQL equivalente y el resultado de ejecutarla en MySQL
 * con el usuario de solo lectura vet_app. La tabla de resultados se arma con las columnas que devuelve la consulta.
 */
@Component({
  selector: 'app-ejercicio',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './ejercicio.html',
  styleUrl: './ejercicio.css'
})
export class Ejercicio implements OnInit {

  detalle: EjercicioDetalle | null = null;
  numero = 0;
  /** Números de todos los ejercicios, para los botones de anterior y siguiente. */
  numeros: number[] = [];

  cargando = true;
  error = '';

  constructor(private route: ActivatedRoute, private algebraService: AlgebraService) {}

  ngOnInit(): void {
    this.algebraService.getAll().subscribe(datos => this.numeros = datos.ejercicios.map(e => e.numero));
    // paramMap cambia al usar anterior/siguiente sin salir de la página
    this.route.paramMap.subscribe(parametros => {
      this.numero = Number(parametros.get('numero'));
      this.cargar();
    });
  }

  cargar(): void {
    this.cargando = true;
    this.error = '';
    this.detalle = null;
    this.algebraService.getDetalle(this.numero).subscribe({
      next: (datos) => { this.detalle = datos; this.cargando = false; },
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
    });
  }

  get anterior(): number | null {
    const posicion = this.numeros.indexOf(this.numero);
    return posicion > 0 ? this.numeros[posicion - 1] : null;
  }

  get siguiente(): number | null {
    const posicion = this.numeros.indexOf(this.numero);
    return posicion >= 0 && posicion < this.numeros.length - 1 ? this.numeros[posicion + 1] : null;
  }
}
