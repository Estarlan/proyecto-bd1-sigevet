import { Component, OnInit } from '@angular/core';
import { CurrencyPipe, DecimalPipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FacturaService, mensajeDeError } from '../../services/api.service';
import { FacturaDetalle } from '../../models/models';

/**
 * Una factura con sus líneas. DETALLE_FACTURA es entidad débil de FACTURA y cada línea cobra un servicio o un
 * medicamento (arco exclusivo). El total no se guarda: lo calcula la vista V_FACTURA_TOTAL.
 */
@Component({
  selector: 'app-factura-detalle',
  standalone: true,
  imports: [RouterLink, CurrencyPipe, DecimalPipe],
  templateUrl: './factura-detalle.html'
})
export class FacturaDetallePage implements OnInit {

  detalle: FacturaDetalle | null = null;
  cargando = true;
  error = '';

  constructor(private route: ActivatedRoute, private facturaService: FacturaService) {}

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
    this.facturaService.getDetalle(id).subscribe({
      next: (datos) => { this.detalle = datos; this.cargando = false; },
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.cargando = false; }
    });
  }

  /** Texto legible del método de pago (CHECK CK_FACTURA_METODO_PAGO). */
  textoMetodo(metodo?: string): string {
    return (metodo ?? '—').replace('_', ' ').toLowerCase();
  }
}
