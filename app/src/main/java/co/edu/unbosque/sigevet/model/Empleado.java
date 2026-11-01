package co.edu.unbosque.sigevet.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Empleado de una sede de la red de clínicas.
 *
 * <p>Hereda de {@link Persona} y es la clase base de {@link Veterinario} y {@link Auxiliar}. Un empleado que no
 * es ninguno de los dos (por ejemplo, personal administrativo) se representa con esta misma clase.</p>
 */
public class Empleado extends Persona {
    /** Nombre de la sede donde trabaja el empleado. */
    private String sede;
    /** Tipo de empleado (veterinario, auxiliar o administrativo). */
    private String tipoEmpleado;
    /** Fecha en que ingresó a la empresa, como texto. */
    private String fechaIngreso;
    /** Salario mensual del empleado. Se usa {@link BigDecimal} para no perder precisión con el dinero. */
    private BigDecimal salario;
    /** Estado laboral del empleado (por ejemplo ACTIVO). */
    private String estado;
    /** Teléfono de contacto del empleado. */
    private String telefono;

    /** Constructor vacío: los atributos se llenan con los setters al leer la base de datos. */
    public Empleado() {
    }

    /**
     * Crea el objeto {@code Empleado} con todos sus atributos.
     * Los primeros parámetros pertenecen a las clases padre y se entregan al constructor de {@link Persona}.
     *
     * @param idPersona identificador de la persona en la base de datos (llave primaria de su tabla).
     * @param tipoDocumento tipo de documento de identidad (por ejemplo CC o CE).
     * @param numDocumento número del documento de identidad.
     * @param nombres nombres de la persona.
     * @param apellidos apellidos de la persona.
     * @param email correo electrónico de contacto.
     * @param sede nombre de la sede donde trabaja el empleado.
     * @param tipoEmpleado tipo de empleado (veterinario, auxiliar o administrativo).
     * @param fechaIngreso fecha en que ingresó a la empresa, como texto.
     * @param salario salario mensual del empleado.
     * @param estado estado laboral del empleado (por ejemplo ACTIVO).
     * @param telefono teléfono de contacto del empleado.
     */
    public Empleado(int idPersona, String tipoDocumento, String numDocumento, String nombres, String apellidos,
        String email, String sede, String tipoEmpleado, String fechaIngreso, BigDecimal salario, String estado,
        String telefono) {
        super(idPersona, tipoDocumento, numDocumento, nombres, apellidos, email);
        this.sede = sede;
        this.tipoEmpleado = tipoEmpleado;
        this.fechaIngreso = fechaIngreso;
        this.salario = salario;
        this.estado = estado;
        this.telefono = telefono;
    }

    /** @return el nombre de la sede donde trabaja. */
    public String getSede() {
        return sede;
    }

    /** @param sede nuevo nombre de la sede. */
    public void setSede(String sede) {
        this.sede = sede;
    }

    /** @return el tipo de empleado. */
    public String getTipoEmpleado() {
        return tipoEmpleado;
    }

    /** @param tipoEmpleado nuevo tipo de empleado. */
    public void setTipoEmpleado(String tipoEmpleado) {
        this.tipoEmpleado = tipoEmpleado;
    }

    /** @return la fecha de ingreso del empleado. */
    public String getFechaIngreso() {
        return fechaIngreso;
    }

    /** @param fechaIngreso nueva fecha de ingreso. */
    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    /** @return el salario del empleado. */
    public BigDecimal getSalario() {
        return salario;
    }

    /** @param salario nuevo salario. */
    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    /** @return el estado laboral del empleado. */
    public String getEstado() {
        return estado;
    }

    /** @param estado nuevo estado laboral. */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /** @return el teléfono de contacto del empleado. */
    public String getTelefono() {
        return telefono;
    }

    /** @param telefono nuevo teléfono de contacto. */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Describe al empleado que no es veterinario ni auxiliar. Las subclases lo sobrescriben.
     *
     * @return un texto que indica que es empleado administrativo y su sede.
     */
    @Override
    public String getDescripcion() {
        return "Empleado administrativo de " + getSede();
    }

    /**
     * Compara este {@code Empleado} con otro objeto. Dos objetos son iguales cuando son de la misma clase
     * y todos sus atributos (los propios y los que hereda de {@link Persona}) tienen el mismo valor.
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
        Empleado that = (Empleado) o;
        return super.equals(o)
                && Objects.equals(sede, that.sede)
                && Objects.equals(tipoEmpleado, that.tipoEmpleado)
                && Objects.equals(fechaIngreso, that.fechaIngreso)
                && Objects.equals(salario, that.salario)
                && Objects.equals(estado, that.estado)
                && Objects.equals(telefono, that.telefono);
    }

    /**
     * Calcula el código hash a partir de los mismos atributos que usa {@link #equals(Object)}, para que dos
     * objetos iguales tengan siempre el mismo código.
     *
     * @return el código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sede, tipoEmpleado, fechaIngreso, salario, estado, telefono);
    }

    /**
     * Devuelve el contenido del {@code Empleado} como texto, con el nombre y el valor de cada atributo.
     * Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "Empleado{sede=" + sede
                + ", tipoEmpleado=" + tipoEmpleado
                + ", fechaIngreso=" + fechaIngreso
                + ", salario=" + salario
                + ", estado=" + estado
                + ", telefono=" + telefono
                + ", super=" + super.toString()
                + "}";
    }
}
