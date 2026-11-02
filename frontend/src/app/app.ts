import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './shared/components/navbar/navbar';
import { SesionService } from './services/sesion.service';

/**
 * Componente raíz. El menú se pone una sola vez aquí y solo aparece cuando hay un empleado conectado;
 * cada página se dibuja dentro del router-outlet.
 */
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class AppComponent {
  constructor(public sesion: SesionService) {}
}
