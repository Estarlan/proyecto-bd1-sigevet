package co.edu.unbosque.sigevet.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Línea de una {@link Factura}: un concepto cobrado con su cantidad, precio y descuento.
 *
 * <p>En la base de datos esta tabla es una entidad débil: su llave primaria es el par (factura, número de línea).</p>
 */
public class DetalleFactura {
    /** Número de la línea dentro de su factura. */
    private int numLinea;
    /** Concepto cobrado (servicio o medicamento). */
    private String concepto;
    /** Cantidad cobrada. */
    private int cantidad;
    /** Precio de una unidad. */
    private BigDecimal precioUnitario;
    /** Porcentaje de descuento aplicado a la línea. */
    private BigDecimal descuentoPct;
    /** Valor de la línea después de aplicar el descuento. */
    private BigDecimal subtotal;

    /** Constructor vacío: los atributos se llenan con los setters al leer la base de datos. */
    public DetalleFactura() {
    }

    /**
     * Crea el objeto {@code DetalleFactura} con todos sus atributos.
     *
     * @param numLinea número de la línea dentro de su factura.
     * @param concepto concepto cobrado (servicio o medicamento).
     * @param cantidad cantidad cobrada.
     * @param precioUnitario precio de una unidad.
     * @param descuentoPct porcentaje de descuento aplicado a la línea.
     * @param subtotal valor de la línea después de aplicar el descuento.
     */
    public DetalleFactura(int numLinea, String concepto, int cantidad, BigDecimal precioUnitario,
        BigDecimal descuentoPct, BigDecimal subtotal) {
        this.numLinea = numLinea;
        this.concepto = concepto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuentoPct = descuentoPct;
        this.subtotal = subtotal;
    }

    /** @return el número de la línea. */
    public int getNumLinea() {
        return numLinea;
    }

    /** @param numLinea nuevo número de línea. */
    public void setNumLinea(int numLinea) {
        this.numLinea = numLinea;
    }

    /** @return el concepto cobrado. */
    public String getConcepto() {
        return concepto;
    }

    /** @param concepto nuevo concepto. */
    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    /** @return la cantidad cobrada. */
    public int getCantidad() {
        return cantidad;
    }

    /** @param cantidad nueva cantidad. */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /** @return el precio de una unidad. */
    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    /** @param precioUnitario nuevo precio unitario. */
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    /** @return el porcentaje de descuento. */
    public BigDecimal getDescuentoPct() {
        return descuentoPct;
    }

    /** @param descuentoPct nuevo porcentaje de descuento. */
    public void setDescuentoPct(BigDecimal descuentoPct) {
        this.descuentoPct = descuentoPct;
    }

    /** @return el subtotal de la línea. */
    public BigDecimal getSubtotal() {
        return subtotal;
    }

    /** @param subtotal nuevo subtotal. */
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * Compara este {@code DetalleFactura} con otro objeto. Dos objetos son iguales cuando son de la misma clase
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
        DetalleFactura that = (DetalleFactura) o;
        return numLinea == that.numLinea
                && Objects.equals(concepto, that.concepto)
                && cantidad == that.cantidad
                && Objects.equals(precioUnitario, that.precioUnitario)
                && Objects.equals(descuentoPct, that.descuentoPct)
                && Objects.equals(subtotal, that.subtotal);
    }

    /**
     * Calcula el código hash a partir de los mismos atributos que usa {@link #equals(Object)}, para que dos
     * objetos iguales tengan siempre el mismo código.
     *
     * @return el código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(numLinea, concepto, cantidad, precioUnitario, descuentoPct, subtotal);
    }

    /**
     * Devuelve el contenido del {@code DetalleFactura} como texto, con el nombre y el valor de cada atributo.
     * Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "DetalleFactura{numLinea=" + numLinea
                + ", concepto=" + concepto
                + ", cantidad=" + cantidad
                + ", precioUnitario=" + precioUnitario
                + ", descuentoPct=" + descuentoPct
                + ", subtotal=" + subtotal
                + "}";
    }
}
