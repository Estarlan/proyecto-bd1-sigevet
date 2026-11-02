import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
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
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  form: LoginForm = { email: '', documento: '' };
  enviando = false;
  error = '';

  constructor(private sesion: SesionService, private router: Router) {}

  entrar(): void {
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
