package co.edu.unbosque.sigevet.model;

import java.util.Objects;

/**
 * Número de teléfono de un cliente. Un cliente puede tener varios, por eso el teléfono (atributo
 * multivaluado) tiene su propia clase y su propia tabla.
 */
public class Telefono {
    /** Número de teléfono. */
    private String telefono;
    /** Tipo de teléfono (por ejemplo móvil o fijo). */
    private String tipo;

    /** Constructor vacío: los atributos se llenan con los setters al leer la base de datos. */
    public Telefono() {
    }

    /**
     * Crea el objeto {@code Telefono} con todos sus atributos.
     *
     * @param telefono número de teléfono.
     * @param tipo tipo de teléfono (por ejemplo móvil o fijo).
     */
    public Telefono(String telefono, String tipo) {
        this.telefono = telefono;
        this.tipo = tipo;
    }

    /** @return el número de teléfono. */
    public String getTelefono() {
        return telefono;
    }

    /** @param telefono nuevo número de teléfono. */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /** @return el tipo de teléfono. */
    public String getTipo() {
        return tipo;
    }

    /** @param tipo nuevo tipo de teléfono. */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Compara este {@code Telefono} con otro objeto. Dos objetos son iguales cuando son de la misma clase
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
        Telefono that = (Telefono) o;
        return Objects.equals(telefono, that.telefono)
                && Objects.equals(tipo, that.tipo);
    }

    /**
     * Calcula el código hash a partir de los mismos atributos que usa {@link #equals(Object)}, para que dos
     * objetos iguales tengan siempre el mismo código.
     *
     * @return el código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(telefono, tipo);
    }

    /**
     * Devuelve el contenido del {@code Telefono} como texto, con el nombre y el valor de cada atributo.
     * Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "Telefono{telefono=" + telefono
                + ", tipo=" + tipo
                + "}";
    }
}
