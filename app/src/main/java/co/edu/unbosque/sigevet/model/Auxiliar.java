package co.edu.unbosque.sigevet.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Auxiliar de clínica: un {@link Empleado} asignado a un área y a un turno de trabajo.
 *
 * <p>Sobrescribe {@link #getDescripcion()} para mostrar su área y su turno (polimorfismo).</p>
 */
public class Auxiliar extends Empleado {
    /** Área de la clínica donde trabaja el auxiliar. */
    private String area;
    /** Turno de trabajo del auxiliar. */
    private String turno;

    /** Constructor vacío: los atributos se llenan con los setters al leer la base de datos. */
    public Auxiliar() {
    }

    /**
     * Crea el objeto {@code Auxiliar} con todos sus atributos.
     * Los primeros parámetros pertenecen a las clases padre y se entregan al constructor de {@link Empleado}.
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
     * @param area área de la clínica donde trabaja el auxiliar.
     * @param turno turno de trabajo del auxiliar.
     */
    public Auxiliar(int idPersona, String tipoDocumento, String numDocumento, String nombres, String apellidos,
        String email, String sede, String tipoEmpleado, String fechaIngreso, BigDecimal salario, String estado,
        String telefono, String area, String turno) {
        super(idPersona, tipoDocumento, numDocumento, nombres, apellidos, email, sede, tipoEmpleado, fechaIngreso,
            salario, estado, telefono);
        this.area = area;
        this.turno = turno;
    }

    /** @return el área donde trabaja el auxiliar. */
    public String getArea() {
        return area;
    }

    /** @param area nueva área de trabajo. */
    public void setArea(String area) {
        this.area = area;
    }

    /** @return el turno de trabajo del auxiliar. */
    public String getTurno() {
        return turno;
    }

    /** @param turno nuevo turno de trabajo. */
    public void setTurno(String turno) {
        this.turno = turno;
    }

    /**
     * Describe al auxiliar con su área y su turno.
     *
     * @return un texto con el área y el turno del auxiliar.
     */
    @Override
    public String getDescripcion() {
        return "Auxiliar de " + area + ". Turno: " + turno;
    }

    /**
     * Compara este {@code Auxiliar} con otro objeto. Dos objetos son iguales cuando son de la misma clase
     * y todos sus atributos (los propios y los que hereda de {@link Empleado}) tienen el mismo valor.
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
        Auxiliar that = (Auxiliar) o;
        return super.equals(o)
                && Objects.equals(area, that.area)
                && Objects.equals(turno, that.turno);
    }

    /**
     * Calcula el código hash a partir de los mismos atributos que usa {@link #equals(Object)}, para que dos
     * objetos iguales tengan siempre el mismo código.
     *
     * @return el código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), area, turno);
    }

    /**
     * Devuelve el contenido del {@code Auxiliar} como texto, con el nombre y el valor de cada atributo.
     * Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "Auxiliar{area=" + area
                + ", turno=" + turno
                + ", super=" + super.toString()
                + "}";
    }
}
