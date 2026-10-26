package co.edu.unbosque.sigevet.modelo;

import java.math.BigDecimal;

public class DetalleFactura {
    private int numLinea;
    private String concepto;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuentoPct;
    private BigDecimal subtotal;

    public DetalleFactura() {
    }

    public int getNumLinea() {
        return numLinea;
    }

    public void setNumLinea(int numLinea) {
        this.numLinea = numLinea;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getDescuentoPct() {
        return descuentoPct;
    }

    public void setDescuentoPct(BigDecimal descuentoPct) {
        this.descuentoPct = descuentoPct;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
