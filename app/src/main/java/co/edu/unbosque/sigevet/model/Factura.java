package co.edu.unbosque.sigevet.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Factura emitida a un cliente en una sede. Sus líneas de detalle se representan con {@link DetalleFactura}.
 */
public class Factura {
    /** Identificador de la factura (llave primaria). */
    private long idFactura;
    /** Identificador del cliente al que se factura (llave foránea). */
    private int idCliente;
    /** Número de la factura, visible para el cliente. */
    private String numeroFactura;
    /** Fecha de emisión, como texto. */
    private String fechaEmision;
    /** Método con que se pagó la factura. */
    private String metodoPago;
    /** Estado de la factura (PAGADA, PENDIENTE o ANULADA). Ver {@link EstadoFactura}. */
    private String estado;
    /** Nombre completo del cliente. */
    private String cliente;
    /** Nombre de la sede que emitió la factura. */
    private String sede;
    /** Valor total de la factura. Se usa {@link BigDecimal} para no perder precisión con el dinero. */
    private BigDecimal total;

    /** Constructor vacío: los atributos se llenan con los setters al leer la base de datos. */
    public Factura() {
    }

    /**
     * Crea el objeto {@code Factura} con todos sus atributos.
     *
     * @param idFactura identificador de la factura (llave primaria).
     * @param idCliente identificador del cliente al que se factura (llave foránea).
     * @param numeroFactura número de la factura, visible para el cliente.
     * @param fechaEmision fecha de emisión, como texto.
     * @param metodoPago método con que se pagó la factura.
     * @param estado estado de la factura (PAGADA, PENDIENTE o ANULADA).
     * @param cliente nombre completo del cliente.
     * @param sede nombre de la sede que emitió la factura.
     * @param total valor total de la factura.
     */
    public Factura(long idFactura, int idCliente, String numeroFactura, String fechaEmision, String metodoPago,
        String estado, String cliente, String sede, BigDecimal total) {
        this.idFactura = idFactura;
        this.idCliente = idCliente;
        this.numeroFactura = numeroFactura;
        this.fechaEmision = fechaEmision;
        this.metodoPago = metodoPago;
        this.estado = estado;
        this.cliente = cliente;
        this.sede = sede;
        this.total = total;
    }

    /** @return el identificador de la factura. */
    public long getIdFactura() {
        return idFactura;
    }

    /** @param idFactura nuevo identificador de la factura. */
    public void setIdFactura(long idFactura) {
        this.idFactura = idFactura;
    }

    /** @return el identificador del cliente. */
    public int getIdCliente() {
        return idCliente;
    }

    /** @param idCliente nuevo identificador del cliente. */
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    /** @return el número de la factura. */
    public String getNumeroFactura() {
        return numeroFactura;
    }

    /** @param numeroFactura nuevo número de factura. */
    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    /** @return la fecha de emisión. */
    public String getFechaEmision() {
        return fechaEmision;
    }

    /** @param fechaEmision nueva fecha de emisión. */
    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    /** @return el método de pago. */
    public String getMetodoPago() {
        return metodoPago;
    }

    /** @param metodoPago nuevo método de pago. */
    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    /** @return el estado de la factura. */
    public String getEstado() {
        return estado;
    }

    /** @param estado nuevo estado de la factura. */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /** @return el nombre completo del cliente. */
    public String getCliente() {
        return cliente;
    }

    /** @param cliente nuevo nombre del cliente. */
    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    /** @return el nombre de la sede. */
    public String getSede() {
        return sede;
    }

    /** @param sede nuevo nombre de la sede. */
    public void setSede(String sede) {
        this.sede = sede;
    }

    /** @return el valor total de la factura. */
    public BigDecimal getTotal() {
        return total;
    }

    /** @param total nuevo valor total. */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * Compara este {@code Factura} con otro objeto. Dos objetos son iguales cuando son de la misma clase
     * y todos sus atributos tienen el mismo valor.
     *
     * @param o objeto con el que se compara.
     * @return {@code true} si los dos objetos tienen los mismos valores; {@code false} en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Factura that = (Factura) o;
        return idFactura == that.idFactura
                && idCliente == that.idCliente
                && Objects.equals(numeroFactura, that.numeroFactura)
                && Objects.equals(fechaEmision, that.fechaEmision)
                && Objects.equals(metodoPago, that.metodoPago)
                && Objects.equals(estado, that.estado)
                && Objects.equals(cliente, that.cliente)
                && Objects.equals(sede, that.sede)
                && Objects.equals(total, that.total);
    }

    /**
     * Calcula el código hash a partir de los mismos atributos que usa {@link #equals(Object)}, para que dos
     * objetos iguales tengan siempre el mismo código.
     *
     * @return el código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(idFactura, idCliente, numeroFactura, fechaEmision, metodoPago, estado, cliente, sede,
                total);
    }

    /**
     * Devuelve el contenido del {@code Factura} como texto, con el nombre y el valor de cada atributo.
     * Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "Factura{idFactura=" + idFactura
                + ", idCliente=" + idCliente
                + ", numeroFactura=" + numeroFactura
                + ", fechaEmision=" + fechaEmision
                + ", metodoPago=" + metodoPago
                + ", estado=" + estado
                + ", cliente=" + cliente
                + ", sede=" + sede
                + ", total=" + total
                + "}";
    }
}
