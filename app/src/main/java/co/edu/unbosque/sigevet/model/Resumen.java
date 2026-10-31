package co.edu.unbosque.sigevet.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Par "nombre y valor" que usa la página de inicio para mostrar los conteos generales, los ingresos por
 * sede y las citas por estado.
 */
public class Resumen {
    /** Etiqueta del dato (por ejemplo el nombre de una sede o de un estado). */
    private String nombre;
    /** Valor asociado a la etiqueta: un conteo o una suma de dinero. */
    private BigDecimal total;

    /** Constructor vacío: los atributos se llenan con los setters al leer la base de datos. */
    public Resumen() {
    }

    /**
     * Crea el objeto {@code Resumen} con todos sus atributos.
     *
     * @param nombre etiqueta del dato (por ejemplo el nombre de una sede o de un estado).
     * @param total valor asociado a la etiqueta: un conteo o una suma de dinero.
     */
    public Resumen(String nombre, BigDecimal total) {
        this.nombre = nombre;
        this.total = total;
    }

    /** @return la etiqueta del dato. */
    public String getNombre() {
        return nombre;
    }

    /** @param nombre nueva etiqueta. */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** @return el valor del dato. */
    public BigDecimal getTotal() {
        return total;
    }

    /** @param total nuevo valor. */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * Compara este {@code Resumen} con otro objeto. Dos objetos son iguales cuando son de la misma clase
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
        Resumen that = (Resumen) o;
        return Objects.equals(nombre, that.nombre)
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
        return Objects.hash(nombre, total);
    }

    /**
     * Devuelve el contenido del {@code Resumen} como texto, con el nombre y el valor de cada atributo.
     * Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "Resumen{nombre=" + nombre
                + ", total=" + total
                + "}";
    }
}
