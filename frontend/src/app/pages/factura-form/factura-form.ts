import { Component, OnInit } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { FormsModule, NgForm, NgModel } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CatalogoService, ClienteService, FacturaService, SedeService, mensajeDeError } from '../../services/api.service';
import { Cliente, FacturaForm, LineaFacturaForm, OpcionCatalogo, Sede } from '../../models/models';
import { marcarInvalido } from '../../shared/validaciones';

/** Una línea mientras se edita: se elige primero si es servicio o medicamento. */
interface LineaEdicion {
  tipo: 'SERVICIO' | 'MEDICAMENTO';
  idItem: number | null;
  cantidad: number;
  descuentoPct: number;
}

/**
 * Nueva factura (/facturas/nueva?cliente=ID). Cada línea cobra un servicio O un medicamento (arco exclusivo). El
 * total que se muestra aquí es solo una vista previa: el backend vuelve a leer los precios vigentes de la base y
 * guarda la factura, sus líneas y el descuento de inventario en una sola transacción.
 */
@Component({
  selector: 'app-factura-form',
  standalone: true,
  imports: [FormsModule, RouterLink, CurrencyPipe],
  templateUrl: './factura-form.html'
})
export class FacturaFormPage implements OnInit {

  idCliente: number | null = null;
  idSede: number | null = null;
  idEmpleado: number | null = null;
  metodoPago = 'EFECTIVO';
  estado = 'PAGADA';
  lineas: LineaEdicion[] = [{ tipo: 'SERVICIO', idItem: null, cantidad: 1, descuentoPct: 0 }];

  clientes: Cliente[] = [];
  sedes: Sede[] = [];
  empleados: OpcionCatalogo[] = [];
  servicios: OpcionCatalogo[] = [];
  /** Medicamentos que tiene la sede elegida (con sus existencias). */
  medicamentos: OpcionCatalogo[] = [];

  metodos = [
    { valor: 'EFECTIVO', texto: 'Efectivo' },
    { valor: 'TARJETA_DEBITO', texto: 'Tarjeta débito' },
    { valor: 'TARJETA_CREDITO', texto: 'Tarjeta crédito' },
    { valor: 'TRANSFERENCIA', texto: 'Transferencia' }
  ];

  intentado = false;
  guardando = false;
  error = '';

  constructor(private route: ActivatedRoute, private router: Router, private facturaService: FacturaService,
              private clienteService: ClienteService, private catalogoService: CatalogoService,
              private sedeService: SedeService) {}

  ngOnInit(): void {
    this.clienteService.getAll('').subscribe(datos => this.clientes = datos);
    this.sedeService.getAll().subscribe(datos => this.sedes = datos);
    this.catalogoService.servicios().subscribe(datos => this.servicios = datos);
    const cliente = this.route.snapshot.queryParamMap.get('cliente');
    if (cliente) {
      this.idCliente = Number(cliente);
    }
  }

  /** Al cambiar la sede cambian quién puede emitir y qué medicamentos hay en inventario. */
  cambiarSede(): void {
    this.idEmpleado = null;
    this.empleados = [];
    this.medicamentos = [];
    for (const linea of this.lineas) {
      if (linea.tipo === 'MEDICAMENTO') {
        linea.idItem = null;
      }
    }
    if (this.idSede !== null) {
      this.catalogoService.empleados(this.idSede).subscribe(datos => this.empleados = datos);
      this.catalogoService.medicamentos(this.idSede).subscribe(datos => this.medicamentos = datos);
    }
  }

  agregarLinea(): void {
    this.lineas.push({ tipo: 'SERVICIO', idItem: null, cantidad: 1, descuentoPct: 0 });
  }

  quitarLinea(posicion: number): void {
    this.lineas.splice(posicion, 1);
  }

  /** Opciones del select de una línea según su tipo. */
  opciones(linea: LineaEdicion): OpcionCatalogo[] {
    return linea.tipo === 'SERVICIO' ? this.servicios : this.medicamentos;
  }

  precio(linea: LineaEdicion): number {
    const item = this.opciones(linea).find(o => o.id === linea.idItem);
    return item?.valor ?? 0;
  }

  /** Subtotal = cantidad × precio × (1 − descuento / 100), la misma fórmula de FN_TOTAL_FACTURA. */
  subtotal(linea: LineaEdicion): number {
    return (linea.cantidad || 0) * this.precio(linea) * (1 - (linea.descuentoPct || 0) / 100);
  }

  get total(): number {
    let suma = 0;
    for (const linea of this.lineas) {
      suma += this.subtotal(linea);
    }
    return suma;
  }

  invalido(campo: NgModel): boolean {
    return marcarInvalido(campo, this.intentado);
  }

  /** Cantidad entera de 1 a 1000 y descuento de 0 a 100 con máximo dos decimales (igual que el backend). */
  lineaInvalida(linea: LineaEdicion): boolean {
    const cantidadMala = !Number.isInteger(linea.cantidad) || linea.cantidad < 1 || linea.cantidad > 1000;
    const descuento = linea.descuentoPct ?? 0;
    const descuentoMalo = descuento < 0 || descuento > 100 || Math.round(descuento * 100) !== descuento * 100;
    return linea.idItem === null || cantidadMala || descuentoMalo;
  }

  get hayLineasInvalidas(): boolean {
    return this.lineas.some(l => this.lineaInvalida(l));
  }

  guardar(f: NgForm): void {
    this.intentado = true;
    if (f.invalid || this.hayLineasInvalidas) {
      this.error = 'Revise los campos marcados en rojo.';
      return;
    }
    this.guardando = true;
    this.error = '';
    const lineas: LineaFacturaForm[] = this.lineas.map(l => ({
      idServicio: l.tipo === 'SERVICIO' ? l.idItem : null,
      idMedicamento: l.tipo === 'MEDICAMENTO' ? l.idItem : null,
      cantidad: l.cantidad,
      descuentoPct: l.descuentoPct
    }));
    const form: FacturaForm = {
      idCliente: this.idCliente, idSede: this.idSede, idEmpleado: this.idEmpleado,
      metodoPago: this.metodoPago, estado: this.estado, lineas
    };
    this.facturaService.crear(form).subscribe({
      next: (respuesta) => this.router.navigate(['/facturas', respuesta.id]),
      error: (e: HttpErrorResponse) => { this.error = mensajeDeError(e); this.guardando = false; }
    });
  }
}
