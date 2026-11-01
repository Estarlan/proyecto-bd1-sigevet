package co.edu.unbosque.sigevet.model;

import java.util.Objects;

/**
 * Cliente de la red de clínicas: el dueño de una o varias mascotas.
 *
 * <p>Hereda de {@link Persona} los datos de identidad y agrega los datos de ubicación y de registro.
 * La ciudad y el departamento vienen de las tablas CIUDAD y DEPARTAMENTO, unidas al leer el cliente.</p>
 */
public class Cliente extends Persona {
    /** Dirección de residencia del cliente. */
    private String direccion;
    /** Nombre de la ciudad donde vive el cliente. */
    private String ciudad;
    /** Nombre del departamento al que pertenece la ciudad. */
    private String departamento;
    /** Fecha en que el cliente se registró en el sistema, como texto. */
    private String fechaRegistro;

    /** Constructor vacío: los atributos se llenan con los setters al leer la base de datos. */
    public Cliente() {
    }

    /**
     * Crea el objeto {@code Cliente} con todos sus atributos.
     * Los primeros parámetros pertenecen a las clases padre y se entregan al constructor de {@link Persona}.
     *
     * @param idPersona identificador de la persona en la base de datos (llave primaria de su tabla).
     * @param tipoDocumento tipo de documento de identidad (por ejemplo CC o CE).
     * @param numDocumento número del documento de identidad.
     * @param nombres nombres de la persona.
     * @param apellidos apellidos de la persona.
     * @param email correo electrónico de contacto.
     * @param direccion dirección de residencia del cliente.
     * @param ciudad nombre de la ciudad donde vive el cliente.
     * @param departamento nombre del departamento al que pertenece la ciudad.
     * @param fechaRegistro fecha en que el cliente se registró en el sistema, como texto.
     */
    public Cliente(int idPersona, String tipoDocumento, String numDocumento, String nombres, String apellidos,
        String email, String direccion, String ciudad, String departamento, String fechaRegistro) {
        super(idPersona, tipoDocumento, numDocumento, nombres, apellidos, email);
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.departamento = departamento;
        this.fechaRegistro = fechaRegistro;
    }

    /** @return la dirección de residencia del cliente. */
    public String getDireccion() {
        return direccion;
    }

    /** @param direccion nueva dirección de residencia. */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /** @return el nombre de la ciudad del cliente. */
    public String getCiudad() {
        return ciudad;
    }

    /** @param ciudad nuevo nombre de la ciudad. */
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    /** @return el nombre del departamento del cliente. */
    public String getDepartamento() {
        return departamento;
    }

    /** @param departamento nuevo nombre del departamento. */
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    /** @return la fecha de registro del cliente. */
    public String getFechaRegistro() {
        return fechaRegistro;
    }

    /** @param fechaRegistro nueva fecha de registro. */
    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    /**
     * Describe al cliente con su nombre completo (implementación del método abstracto de {@link Persona}).
     *
     * @return el texto "Cliente: " seguido del nombre completo.
     */
    @Override
    public String getDescripcion() {
        return "Cliente: " + getNombreCompleto();
    }

    /**
     * Compara este {@code Cliente} con otro objeto. Dos objetos son iguales cuando son de la misma clase
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
        Cliente that = (Cliente) o;
        return super.equals(o)
                && Objects.equals(direccion, that.direccion)
                && Objects.equals(ciudad, that.ciudad)
                && Objects.equals(departamento, that.departamento)
                && Objects.equals(fechaRegistro, that.fechaRegistro);
    }

    /**
     * Calcula el código hash a partir de los mismos atributos que usa {@link #equals(Object)}, para que dos
     * objetos iguales tengan siempre el mismo código.
     *
     * @return el código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), direccion, ciudad, departamento, fechaRegistro);
    }

    /**
     * Devuelve el contenido del {@code Cliente} como texto, con el nombre y el valor de cada atributo.
     * Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "Cliente{direccion=" + direccion
                + ", ciudad=" + ciudad
                + ", departamento=" + departamento
                + ", fechaRegistro=" + fechaRegistro
                + ", super=" + super.toString()
                + "}";
    }
}
