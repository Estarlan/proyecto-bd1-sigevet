import { Component } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { SesionService } from '../../services/sesion.service';
import { mensajeDeError } from '../../services/api.service';
import { LoginForm } from '../../models/models';

/**
 * Inicio de sesión del personal (/login). El empleado escribe su correo corporativo y su número de documento;
 * el backend los busca en la tabla EMPLEADO y solo deja entrar a los empleados activos.
 */
@Component({
  selector: 'app-login-component',
  standalone: false,
  templateUrl: './login-component.html',
  styleUrl: './login-component.css'
})
export class LoginComponent {
  /** Correo y documento que escribe el empleado. */
  form: LoginForm = { email: '', documento: '' };
  /** true mientras se espera la respuesta del servidor (deshabilita el botón). */
  enviando = false;
  /** Mensaje de error para mostrar debajo del formulario (por ejemplo, "credenciales incorrectas"). */
  error = '';

  constructor(private sesion: SesionService, private router: Router) {}

  /** Envía el correo y el documento; si son correctos, guarda la sesión y abre la página de inicio. */
  entrar(): void {
    if (this.enviando) {
      return;   // un segundo clic (o Enter) mientras se espera no envía otra petición
    }
    this.error = '';
    this.enviando = true;
    this.sesion.iniciar(this.form).subscribe({
      next: () => {
        this.enviando = false;
        this.router.navigate(['/']);
      },
      error: (e: HttpErrorResponse) => {
        this.enviando = false;
        this.error = mensajeDeError(e);
      }
    });
  }
}
