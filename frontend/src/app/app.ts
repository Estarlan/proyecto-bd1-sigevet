import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './shared/components/navbar/navbar';

/**
 * Componente raíz. SIGEVET no tiene inicio de sesión ni roles de usuario,
 * así que el menú se pone una sola vez aquí y cada página se dibuja dentro del router-outlet.
 */
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, NavbarComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class AppComponent {}
