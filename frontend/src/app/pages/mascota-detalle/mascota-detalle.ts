import { Component, OnInit } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MascotaService, mensajeDeError } from '../../services/api.service';
import { MascotaDetalle } from '../../models/models';

@Component({
  selector: 'app-mascota-detalle',
  standalone: true,
  imports: [RouterLink, DecimalPipe],
  templateUrl: './mascota-detalle.html'
})
export class MascotaDetallePage implements OnInit {

  detalle: MascotaDetalle | null = null;
  cargando = true;
  error = '';

  constructor(private route: ActivatedRoute, private mascotaService: MascotaService) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(parametros => {
      const id = Number(parametros.get('id'));
      this.cargar(id);
    });
  }

  cargar(id: number): void {
    this.cargando = true;
    this.error = '';
    this.detalle = null;
    this.mascotaService.getDetalle(id).subscribe({
      next: (datos) => { this.detalle = datos; this.cargando = false; },
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
    });
  }

  /** En la base el sexo se guarda como M o H (CHECK CK_MASCOTA_SEXO). */
  textoSexo(sexo?: string): string {
    if (sexo === 'M') {
      return 'Macho';
    }
    if (sexo === 'H') {
      return 'Hembra';
    }
    return '—';
  }

  /** En la base se guarda S o N (CHECK CK_MASCOTA_ESTERILIZADO). */
  textoSiNo(valor?: string): string {
    return valor === 'S' ? 'Sí' : 'No';
  }
}
