import { Component } from '@angular/core';

@Component({
  selector: 'app-no-encontrado',
  standalone: false,
  template: `
    <div class="vacio py-5">
      <i class="bi bi-signpost-split"></i>
      <h5 class="fw-bold text-dark">Página no encontrada</h5>
      <p>La dirección no existe o la página todavía no está construida.</p>
      <a routerLink="/" class="btn btn-dark btn-sm">Ir al inicio</a>
    </div>
  `
})
export class NoEncontrado {}
