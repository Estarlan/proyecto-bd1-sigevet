package co.edu.unbosque.sigevet.model;

import java.math.BigDecimal;

/**
 * Una opción para los {@code <select>} de los formularios: una ciudad, una raza, un servicio, un medicamento, una
 * especialidad o un empleado. Se usa una sola clase para todos los catálogos porque los formularios solo necesitan
 * el identificador, el texto que se muestra y, a veces, un dato adicional.
 */
public class OpcionCatalogo {
    /** Identificador (la llave primaria de la tabla del catálogo). */
    private int id;
    /** Texto principal de la opción. */
    private String nombre;
    /** Dato adicional que se muestra entre paréntesis (departamento, especie, categoría, presentación...). */
    private String detalle;
    /** Valor numérico cuando aplica: precio del servicio o del medicamento, existencias en una sede. */
    private BigDecimal valor;

    /** Constructor vacío. */
    public OpcionCatalogo() {
    }

    /**
     * @param id identificador.
     * @param nombre texto principal.
     * @param detalle dato adicional (puede ser null).
     * @param valor valor numérico (puede ser null).
     */
    public OpcionCatalogo(int id, String nombre, String detalle, BigDecimal valor) {
        this.id = id;
        this.nombre = nombre;
        this.detalle = detalle;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDetalle() {
        return detalle;
    }

    public BigDecimal getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "OpcionCatalogo{id=" + id + ", nombre=" + nombre + ", detalle=" + detalle + ", valor=" + valor + "}";
    }
}
