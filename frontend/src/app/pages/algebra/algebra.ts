import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { AlgebraService, mensajeDeError } from '../../services/api.service';
import { EjercicioAlgebra } from '../../models/models';

/**
 * Lista de los 55 ejercicios de álgebra relacional, agrupados por operación (selección, reunión, división...).
 * Los ejercicios se leen del script database/03_varios/01_consultas_algebra_relacional.sql en el backend.
 */
@Component({
  selector: 'app-algebra',
  standalone: false,
  templateUrl: './algebra.html',
  styleUrl: './algebra.css'
})
export class Algebra implements OnInit {

  grupos: string[] = [];
  ejercicios: EjercicioAlgebra[] = [];
  /** Búsqueda por número o título; se aplica en el navegador sobre la lista ya cargada. */
  buscar = '';

  cargando = true;
  error = '';

  constructor(private algebraService: AlgebraService) {}

  ngOnInit(): void {
    this.algebraService.getAll().subscribe({
      next: (datos) => {
        this.grupos = datos.grupos;
        this.ejercicios = datos.ejercicios;
        this.cargando = false;
      },
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
    });
  }

  /** Ejercicios de un grupo que coinciden con la búsqueda. */
  ejerciciosDe(grupo: string): EjercicioAlgebra[] {
    const texto = this.buscar.trim().toLowerCase();
    return this.ejercicios.filter(e =>
      e.grupo === grupo &&
      (texto === '' || String(e.numero) === texto || (e.titulo ?? '').toLowerCase().includes(texto))
    );
  }

  /** Grupos que tienen al menos un ejercicio que coincide con la búsqueda. */
  get gruposVisibles(): string[] {
    return this.grupos.filter(g => this.ejerciciosDe(g).length > 0);
  }
}
