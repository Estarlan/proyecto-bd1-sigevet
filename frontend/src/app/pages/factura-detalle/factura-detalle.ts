import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { FacturaService, mensajeDeError } from '../../services/api.service';
import { FacturaDetalle } from '../../models/models';

/**
 * Una factura con sus líneas. DETALLE_FACTURA es entidad débil de FACTURA y cada línea cobra un servicio o un
 * medicamento (arco exclusivo). El total no se guarda: es un atributo derivado que se calcula sumando las líneas.
 */
@Component({
  selector: 'app-factura-detalle',
  standalone: false,
  templateUrl: './factura-detalle.html'
})
export class FacturaDetallePage implements OnInit {

  detalle: FacturaDetalle | null = null;
  cargando = true;
  error = '';

  aviso = '';
  errorAccion = '';
  cambiando = false;

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

  /**
   * PAGADA registra el pago de una factura PENDIENTE; ANULADA anula la factura y el backend devuelve al
   * inventario de la sede los medicamentos vendidos, todo en una transacción. Una factura emitida no se edita.
   */
  cambiarEstado(estado: string): void {
    if (!this.detalle) {
      return;
    }
    const numero = this.detalle.factura.numeroFactura;
    if (estado === 'ANULADA' && !confirm('¿Anular la factura ' + numero + '? Los medicamentos vuelven al inventario.')) {
      return;
    }
    const id = this.detalle.factura.idFactura;
    this.cambiando = true;
    this.aviso = '';
    this.errorAccion = '';
    this.facturaService.cambiarEstado(id, estado).subscribe({
      next: () => {
        this.aviso = estado === 'ANULADA' ? 'La factura ' + numero + ' quedó anulada.' : 'Se registró el pago de la factura ' + numero + '.';
        this.cambiando = false;
        this.cargar(id);
      },
      error: (e: HttpErrorResponse) => { this.errorAccion = mensajeDeError(e); this.cambiando = false; }
    });
  }
}
