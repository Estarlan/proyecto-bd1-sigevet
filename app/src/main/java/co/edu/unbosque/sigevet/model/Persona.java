package co.edu.unbosque.sigevet.model;

import java.util.Objects;

/**
 * Clase abstracta que reúne los datos comunes de cualquier persona del sistema (clientes y empleados).
 *
 * <p>Es la raíz de la jerarquía de herencia del modelo:
 * {@code Persona} -> {@link Cliente} y {@code Persona} -> {@link Empleado} -> {@link Veterinario} / {@link Auxiliar}.
 * Cada subclase implementa {@link #getDescripcion()} a su manera, lo que permite mostrar una lista de
 * personas distintas con la misma llamada (polimorfismo).</p>
 */
public abstract class Persona {
    /** Identificador de la persona en la base de datos (llave primaria de su tabla). */
    private int idPersona;
    /** Tipo de documento de identidad (por ejemplo CC o CE). */
    private String tipoDocumento;
    /** Número del documento de identidad. */
    private String numDocumento;
    /** Nombres de la persona. */
    private String nombres;
    /** Apellidos de la persona. */
    private String apellidos;
    /** Correo electrónico de contacto. */
    private String email;

    /** Constructor vacío: los atributos se llenan con los setters al leer la base de datos. */
    public Persona() {
    }

    /**
     * Crea el objeto {@code Persona} con todos sus atributos.
     *
     * @param idPersona identificador de la persona en la base de datos (llave primaria de su tabla).
     * @param tipoDocumento tipo de documento de identidad (por ejemplo CC o CE).
     * @param numDocumento número del documento de identidad.
     * @param nombres nombres de la persona.
     * @param apellidos apellidos de la persona.
     * @param email correo electrónico de contacto.
     */
    public Persona(int idPersona, String tipoDocumento, String numDocumento, String nombres, String apellidos,
        String email) {
        this.idPersona = idPersona;
        this.tipoDocumento = tipoDocumento;
        this.numDocumento = numDocumento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
    }

    /** @return el identificador de la persona. */
    public int getIdPersona() {
        return idPersona;
    }

    /** @param idPersona nuevo identificador de la persona. */
    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    /** @return el tipo de documento de identidad. */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /** @param tipoDocumento nuevo tipo de documento de identidad. */
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /** @return el número del documento de identidad. */
    public String getNumDocumento() {
        return numDocumento;
    }

    /** @param numDocumento nuevo número de documento. */
    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    /** @return los nombres de la persona. */
    public String getNombres() {
        return nombres;
    }

    /** @param nombres nuevos nombres de la persona. */
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    /** @return los apellidos de la persona. */
    public String getApellidos() {
        return apellidos;
    }

    /** @param apellidos nuevos apellidos de la persona. */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /** @return el correo electrónico de la persona. */
    public String getEmail() {
        return email;
    }

    /** @param email nuevo correo electrónico. */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Une nombres y apellidos en un solo texto para mostrarlo en las vistas.
     *
     * @return el nombre completo, con los nombres primero y los apellidos después.
     */
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    /**
     * Describe a la persona según su tipo. Cada subclase da su propia descripción (polimorfismo).
     *
     * @return un texto corto que identifica el rol de la persona.
     */
    public abstract String getDescripcion();

    /**
     * Compara este {@code Persona} con otro objeto. Dos objetos son iguales cuando son de la misma clase
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
        Persona that = (Persona) o;
        return idPersona == that.idPersona
                && Objects.equals(tipoDocumento, that.tipoDocumento)
                && Objects.equals(numDocumento, that.numDocumento)
                && Objects.equals(nombres, that.nombres)
                && Objects.equals(apellidos, that.apellidos)
                && Objects.equals(email, that.email);
    }

    /**
     * Calcula el código hash a partir de los mismos atributos que usa {@link #equals(Object)}, para que dos
     * objetos iguales tengan siempre el mismo código.
     *
     * @return el código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(idPersona, tipoDocumento, numDocumento, nombres, apellidos, email);
    }

    /**
     * Devuelve el contenido del {@code Persona} como texto, con el nombre y el valor de cada atributo.
     * Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "Persona{idPersona=" + idPersona
                + ", tipoDocumento=" + tipoDocumento
                + ", numDocumento=" + numDocumento
                + ", nombres=" + nombres
                + ", apellidos=" + apellidos
                + ", email=" + email
                + "}";
    }
}
