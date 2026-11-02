import { Component, OnInit } from '@angular/core';
import { NgForm, NgModel } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { CatalogoService, ClienteService, FacturaService, SedeService, mensajeDeError } from '../../services/api.service';
import { Cliente, FacturaForm, LineaFacturaForm, OpcionCatalogo, Sede } from '../../models/models';
import { marcarInvalido } from '../../shared/validaciones';
import { UltimaPeticion } from '../../shared/ultima-peticion';

/** Una línea mientras se edita: se elige primero si es servicio o medicamento. */
interface LineaEdicion {
  /** Qué se cobra en la línea (arco exclusivo: servicio O medicamento). */
  tipo: 'SERVICIO' | 'MEDICAMENTO';
  /** id del servicio o del medicamento elegido, según el tipo; null si aún no se elige. */
  idItem: number | null;
  /** Unidades cobradas (entero de 1 a 1000). */
  cantidad: number;
  /** Descuento en porcentaje (0 a 100, máximo dos decimales). */
  descuentoPct: number;
}

/**
 * Nueva factura (/facturas/nueva?cliente=ID). Cada línea cobra un servicio O un medicamento (arco exclusivo). El
 * total que se muestra aquí es solo una vista previa: el backend vuelve a leer los precios vigentes de la base y
 * guarda la factura, sus líneas y el descuento de inventario en una sola transacción.
 */
@Component({
  selector: 'app-factura-form',
  standalone: false,
  templateUrl: './factura-form.html'
})
export class FacturaFormPage implements OnInit {

  /** Cliente al que se factura. */
  idCliente: number | null = null;
  /** Sede que emite la factura (de ella salen el empleado y los medicamentos). */
  idSede: number | null = null;
  /** Empleado de la sede que emite la factura. */
  idEmpleado: number | null = null;
  /** Método de pago (CHECK CK_FACTURA_METODO_PAGO). */
  metodoPago = 'EFECTIVO';
  /** Estado inicial de la factura: PAGADA o PENDIENTE. */
  estado = 'PAGADA';
  /** Líneas de la factura; siempre hay al menos una al abrir el formulario. */
  lineas: LineaEdicion[] = [{ tipo: 'SERVICIO', idItem: null, cantidad: 1, descuentoPct: 0 }];

  /** Clientes para el select (la API devuelve como máximo 100). */
  clientes: Cliente[] = [];
  /** Sedes para el select. */
  sedes: Sede[] = [];
  /** Empleados activos de la sede elegida. */
  empleados: OpcionCatalogo[] = [];
  /** Catálogo de servicios con su precio. */
  servicios: OpcionCatalogo[] = [];
  /** Medicamentos que tiene la sede elegida (con sus existencias). */
  medicamentos: OpcionCatalogo[] = [];

  /** Métodos de pago permitidos: valor que se guarda y texto que se muestra. */
  metodos = [
    { valor: 'EFECTIVO', texto: 'Efectivo' },
    { valor: 'TARJETA_DEBITO', texto: 'Tarjeta débito' },
    { valor: 'TARJETA_CREDITO', texto: 'Tarjeta crédito' },
    { valor: 'TRANSFERENCIA', texto: 'Transferencia' }
  ];

  /** Se vuelve true al presionar Guardar: desde ahí se marcan en rojo todos los campos con error. */
  intentado = false;
  /** true mientras se envía la factura (deshabilita el botón). */
  guardando = false;
  /** Mensaje de error para mostrar arriba del formulario. */
  error = '';

  /** Controlan que solo cuenten las listas de la última sede elegida. */
  private ultimaEmpleados = new UltimaPeticion();
  private ultimaMedicamentos = new UltimaPeticion();

  constructor(private route: ActivatedRoute, private router: Router, private facturaService: FacturaService,
              private clienteService: ClienteService, private catalogoService: CatalogoService,
              private sedeService: SedeService) {}

  /** Carga los catálogos y deja elegido el cliente si llegó en la dirección (?cliente=ID). */
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
      this.ultimaEmpleados.lanzar(this.catalogoService.empleados(this.idSede), {
        next: (datos) => this.empleados = datos
      });
      this.ultimaMedicamentos.lanzar(this.catalogoService.medicamentos(this.idSede), {
        next: (datos) => this.medicamentos = datos
      });
    }
  }

  /** Agrega una línea vacía al final. */
  agregarLinea(): void {
    this.lineas.push({ tipo: 'SERVICIO', idItem: null, cantidad: 1, descuentoPct: 0 });
  }

  /** Quita la línea que está en esa posición. */
  quitarLinea(posicion: number): void {
    this.lineas.splice(posicion, 1);
  }

  /** Opciones del select de una línea según su tipo. */
  opciones(linea: LineaEdicion): OpcionCatalogo[] {
    return linea.tipo === 'SERVICIO' ? this.servicios : this.medicamentos;
  }

  /** Precio unitario del servicio o medicamento elegido en la línea (0 si aún no se elige). */
  precio(linea: LineaEdicion): number {
    const item = this.opciones(linea).find(o => o.id === linea.idItem);
    return item?.valor ?? 0;
  }

  /** Subtotal = cantidad × precio × (1 − descuento / 100), la misma fórmula de FN_TOTAL_FACTURA. */
  subtotal(linea: LineaEdicion): number {
    return (linea.cantidad || 0) * this.precio(linea) * (1 - (linea.descuentoPct || 0) / 100);
  }

  /** Total de la vista previa: suma de los subtotales de todas las líneas. */
  get total(): number {
    let suma = 0;
    for (const linea of this.lineas) {
      suma += this.subtotal(linea);
    }
    return suma;
  }

  /** true si un campo debe verse en rojo (tiene error y ya se tocó o se intentó guardar). */
  invalido(campo: NgModel): boolean {
    return marcarInvalido(campo, this.intentado);
  }

  /**
   * Cantidad entera de 1 a 1000 y descuento de 0 a 100 con máximo dos decimales (igual que el backend).
   * Los decimales se revisan con tolerancia: 0.07 × 100 da 7.000000000000001 en coma flotante y no debe rechazarse.
   */
  lineaInvalida(linea: LineaEdicion): boolean {
    const cantidadMala = !Number.isInteger(linea.cantidad) || linea.cantidad < 1 || linea.cantidad > 1000;
    const descuento = linea.descuentoPct ?? 0;
    const centesimas = descuento * 100;
    const descuentoMalo = descuento < 0 || descuento > 100 || Math.abs(centesimas - Math.round(centesimas)) > 1e-6;
    return linea.idItem === null || cantidadMala || descuentoMalo;
  }

  /** true si alguna línea tiene un error. */
  get hayLineasInvalidas(): boolean {
    return this.lineas.some(l => this.lineaInvalida(l));
  }

  /** Valida el formulario y, si está bien, crea la factura y abre su detalle. */
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
