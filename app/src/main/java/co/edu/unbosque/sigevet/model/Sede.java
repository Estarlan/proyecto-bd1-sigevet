package co.edu.unbosque.sigevet.model;

import java.util.Objects;

/**
 * Sede (clínica) de la red de veterinarias Huella Sana.
 */
public class Sede {
    /** Identificador de la sede (llave primaria). */
    private int idSede;
    /** Nombre de la sede. */
    private String nombre;
    /** Dirección de la sede. */
    private String direccion;
    /** Teléfono de la sede. */
    private String telefono;
    /** Nombre completo del director de la sede. */
    private String director;

    /** Constructor vacío: los atributos se llenan con los setters al leer la base de datos. */
    public Sede() {
    }

    /**
     * Crea el objeto {@code Sede} con todos sus atributos.
     *
     * @param idSede identificador de la sede (llave primaria).
     * @param nombre nombre de la sede.
     * @param direccion dirección de la sede.
     * @param telefono teléfono de la sede.
     * @param director nombre completo del director de la sede.
     */
    public Sede(int idSede, String nombre, String direccion, String telefono, String director) {
        this.idSede = idSede;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.director = director;
    }

    /** @return el identificador de la sede. */
    public int getIdSede() {
        return idSede;
    }

    /** @param idSede nuevo identificador de la sede. */
    public void setIdSede(int idSede) {
        this.idSede = idSede;
    }

    /** @return el nombre de la sede. */
    public String getNombre() {
        return nombre;
    }

    /** @param nombre nuevo nombre de la sede. */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** @return la dirección de la sede. */
    public String getDireccion() {
        return direccion;
    }

    /** @param direccion nueva dirección. */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /** @return el teléfono de la sede. */
    public String getTelefono() {
        return telefono;
    }

    /** @param telefono nuevo teléfono. */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /** @return el nombre del director. */
    public String getDirector() {
        return director;
    }

    /** @param director nuevo nombre del director. */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * Compara este {@code Sede} con otro objeto. Dos objetos son iguales cuando son de la misma clase
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
        Sede that = (Sede) o;
        return idSede == that.idSede
                && Objects.equals(nombre, that.nombre)
                && Objects.equals(direccion, that.direccion)
                && Objects.equals(telefono, that.telefono)
                && Objects.equals(director, that.director);
    }

    /**
     * Calcula el código hash a partir de los mismos atributos que usa {@link #equals(Object)}, para que dos
     * objetos iguales tengan siempre el mismo código.
     *
     * @return el código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(idSede, nombre, direccion, telefono, director);
    }

    /**
     * Devuelve el contenido del {@code Sede} como texto, con el nombre y el valor de cada atributo.
     * Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "Sede{idSede=" + idSede
                + ", nombre=" + nombre
                + ", direccion=" + direccion
                + ", telefono=" + telefono
                + ", director=" + director
                + "}";
    }
}
