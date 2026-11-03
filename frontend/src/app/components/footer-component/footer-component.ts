import { Component } from '@angular/core';

/** Pie de página de la aplicación. Se dibuja una sola vez en AppComponent, debajo del router-outlet. */
@Component({
  selector: 'app-footer-component',
  standalone: false,
  templateUrl: './footer-component.html',
  styleUrl: './footer-component.css'
})
export class FooterComponent {}
