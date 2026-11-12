import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { FacturaService, mensajeDeError } from '../../services/api.service';
import { FacturaDetalle } from '../../models/models';
import { UltimaPeticion } from '../../shared/ultima-peticion';

/**
 * Una factura con sus líneas. DETALLE_FACTURA es entidad débil de FACTURA y cada línea cobra un servicio o un
 * medicamento (arco exclusivo). El total no se guarda: es un atributo derivado que se calcula sumando las líneas.
 */
@Component({
  selector: 'app-facturadetallecomponent',
  standalone: false,
  templateUrl: './facturadetallecomponent.html'
})
export class Facturadetallecomponent implements OnInit {

  /** La factura con sus líneas; null mientras carga o si falló la carga. */
  detalle: FacturaDetalle | null = null;
  /** true mientras se espera la respuesta de la API. */
  cargando = true;
  /** Error de la carga. */
  error = '';

  /** Mensaje de éxito de la última acción (pago o anulación). */
  aviso = '';
  /** Error de la última acción, separado del error de carga. */
  errorAccion = '';
  /** true mientras se cambia el estado de la factura (deshabilita los botones). */
  cambiando = false;

  /** Controla que solo cuente la respuesta de la factura más reciente. */
  private ultima = new UltimaPeticion();

  constructor(private route: ActivatedRoute, private facturaService: FacturaService) {}

  /** Carga la factura de la dirección y vuelve a cargar si el id de la dirección cambia. */
  ngOnInit(): void {
    this.route.paramMap.subscribe(parametros => {
      const id = Number(parametros.get('id'));
      this.cargar(id);
    });
  }

  /** Pide la factura con ese id y sus líneas. */
  cargar(id: number): void {
    this.cargando = true;
    this.error = '';
    this.detalle = null;
    this.ultima.lanzar(this.facturaService.getDetalle(id), {
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
