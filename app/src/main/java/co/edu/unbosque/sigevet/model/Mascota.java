package co.edu.unbosque.sigevet.model;

import java.util.Objects;

/**
 * Mascota registrada en la red de clínicas. Pertenece a un cliente y tiene una raza (que a su vez
 * pertenece a una especie).
 *
 * <p>Los atributos {@code raza}, {@code especie} y {@code cliente} guardan el nombre ya resuelto desde las
 * tablas relacionadas, para que la vista pueda mostrarlos sin hacer más consultas.</p>
 */
public class Mascota {
    /** Identificador de la mascota (llave primaria). */
    private int idMascota;
    /** Identificador del cliente dueño de la mascota (llave foránea). */
    private int idCliente;
    /** Nombre de la mascota. */
    private String nombre;
    /** Fecha de nacimiento, como texto. */
    private String fechaNacimiento;
    /** Sexo de la mascota. */
    private String sexo;
    /** Color del pelaje o plumaje. */
    private String color;
    /** Código del microchip, si la mascota lo tiene. */
    private String microchip;
    /** Indica si la mascota está esterilizada (se guarda como texto tal como lo entrega la base). */
    private String esterilizado;
    /** Estado de la mascota (por ejemplo ACTIVA). */
    private String estado;
    /** Nombre de la raza. */
    private String raza;
    /** Nombre de la especie. */
    private String especie;
    /** Nombre completo del cliente dueño. */
    private String cliente;

    /** Constructor vacío: los atributos se llenan con los setters al leer la base de datos. */
    public Mascota() {
    }

    /**
     * Crea el objeto {@code Mascota} con todos sus atributos.
     *
     * @param idMascota identificador de la mascota (llave primaria).
     * @param idCliente identificador del cliente dueño de la mascota (llave foránea).
     * @param nombre nombre de la mascota.
     * @param fechaNacimiento fecha de nacimiento, como texto.
     * @param sexo sexo de la mascota.
     * @param color color del pelaje o plumaje.
     * @param microchip código del microchip, si la mascota lo tiene.
     * @param esterilizado indica si la mascota está esterilizada (se guarda como texto tal como lo entrega la base).
     * @param estado estado de la mascota (por ejemplo ACTIVA).
     * @param raza nombre de la raza.
     * @param especie nombre de la especie.
     * @param cliente nombre completo del cliente dueño.
     */
    public Mascota(int idMascota, int idCliente, String nombre, String fechaNacimiento, String sexo, String color,
        String microchip, String esterilizado, String estado, String raza, String especie, String cliente) {
        this.idMascota = idMascota;
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.sexo = sexo;
        this.color = color;
        this.microchip = microchip;
        this.esterilizado = esterilizado;
        this.estado = estado;
        this.raza = raza;
        this.especie = especie;
        this.cliente = cliente;
    }

    /** @return el identificador de la mascota. */
    public int getIdMascota() {
        return idMascota;
    }

    /** @param idMascota nuevo identificador de la mascota. */
    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }

    /** @return el identificador del cliente dueño. */
    public int getIdCliente() {
        return idCliente;
    }

    /** @param idCliente nuevo identificador del cliente dueño. */
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    /** @return el nombre de la mascota. */
    public String getNombre() {
        return nombre;
    }

    /** @param nombre nuevo nombre de la mascota. */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** @return la fecha de nacimiento. */
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    /** @param fechaNacimiento nueva fecha de nacimiento. */
    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /** @return el sexo de la mascota. */
    public String getSexo() {
        return sexo;
    }

    /** @param sexo nuevo sexo de la mascota. */
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    /** @return el color de la mascota. */
    public String getColor() {
        return color;
    }

    /** @param color nuevo color de la mascota. */
    public void setColor(String color) {
        this.color = color;
    }

    /** @return el código del microchip. */
    public String getMicrochip() {
        return microchip;
    }

    /** @param microchip nuevo código de microchip. */
    public void setMicrochip(String microchip) {
        this.microchip = microchip;
    }

    /** @return si la mascota está esterilizada. */
    public String getEsterilizado() {
        return esterilizado;
    }

    /** @param esterilizado nuevo valor de esterilización. */
    public void setEsterilizado(String esterilizado) {
        this.esterilizado = esterilizado;
    }

    /** @return el estado de la mascota. */
    public String getEstado() {
        return estado;
    }

    /** @param estado nuevo estado de la mascota. */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /** @return el nombre de la raza. */
    public String getRaza() {
        return raza;
    }

    /** @param raza nuevo nombre de la raza. */
    public void setRaza(String raza) {
        this.raza = raza;
    }

    /** @return el nombre de la especie. */
    public String getEspecie() {
        return especie;
    }

    /** @param especie nuevo nombre de la especie. */
    public void setEspecie(String especie) {
        this.especie = especie;
    }

    /** @return el nombre del cliente dueño. */
    public String getCliente() {
        return cliente;
    }

    /** @param cliente nuevo nombre del cliente dueño. */
    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    /**
     * Compara este {@code Mascota} con otro objeto. Dos objetos son iguales cuando son de la misma clase
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
        Mascota that = (Mascota) o;
        return idMascota == that.idMascota
                && idCliente == that.idCliente
                && Objects.equals(nombre, that.nombre)
                && Objects.equals(fechaNacimiento, that.fechaNacimiento)
                && Objects.equals(sexo, that.sexo)
                && Objects.equals(color, that.color)
                && Objects.equals(microchip, that.microchip)
                && Objects.equals(esterilizado, that.esterilizado)
                && Objects.equals(estado, that.estado)
                && Objects.equals(raza, that.raza)
                && Objects.equals(especie, that.especie)
                && Objects.equals(cliente, that.cliente);
    }

    /**
     * Calcula el código hash a partir de los mismos atributos que usa {@link #equals(Object)}, para que dos
     * objetos iguales tengan siempre el mismo código.
     *
     * @return el código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(idMascota, idCliente, nombre, fechaNacimiento, sexo, color, microchip, esterilizado, estado,
                raza, especie, cliente);
    }

    /**
     * Devuelve el contenido del {@code Mascota} como texto, con el nombre y el valor de cada atributo.
     * Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "Mascota{idMascota=" + idMascota
                + ", idCliente=" + idCliente
                + ", nombre=" + nombre
                + ", fechaNacimiento=" + fechaNacimiento
                + ", sexo=" + sexo
                + ", color=" + color
                + ", microchip=" + microchip
                + ", esterilizado=" + esterilizado
                + ", estado=" + estado
                + ", raza=" + raza
                + ", especie=" + especie
                + ", cliente=" + cliente
                + "}";
    }
}
