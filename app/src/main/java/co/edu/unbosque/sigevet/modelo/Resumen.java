package co.edu.unbosque.sigevet.modelo;

import java.math.BigDecimal;

public class Resumen {
    private String nombre;
    private BigDecimal total;

    public Resumen() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
