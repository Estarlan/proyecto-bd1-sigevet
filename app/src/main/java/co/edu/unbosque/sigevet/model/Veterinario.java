package co.edu.unbosque.sigevet.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Médico veterinario: un {@link Empleado} con tarjeta profesional, universidad y especialidades.
 *
 * <p>Sobrescribe {@link #getDescripcion()} para mostrar sus datos profesionales (polimorfismo).</p>
 */
public class Veterinario extends Empleado {
    /** Número de la tarjeta profesional del veterinario. */
    private String tarjetaProfesional;
    /** Universidad donde se graduó. */
    private String universidad;
    /** Especialidades del veterinario. Un veterinario puede tener varias, por eso es una lista. */
    private ArrayList<String> especialidades = new ArrayList<String>();

    /** Constructor vacío: los atributos se llenan con los setters al leer la base de datos. */
    public Veterinario() {
    }

    /**
     * Crea el objeto {@code Veterinario} con todos sus atributos.
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
     * @param tarjetaProfesional número de la tarjeta profesional del veterinario.
     * @param universidad universidad donde se graduó.
     * @param especialidades especialidades del veterinario.
     */
    public Veterinario(int idPersona, String tipoDocumento, String numDocumento, String nombres, String apellidos,
        String email, String sede, String tipoEmpleado, String fechaIngreso, BigDecimal salario, String estado,
        String telefono, String tarjetaProfesional, String universidad, ArrayList<String> especialidades) {
        super(idPersona, tipoDocumento, numDocumento, nombres, apellidos, email, sede, tipoEmpleado, fechaIngreso,
            salario, estado, telefono);
        this.tarjetaProfesional = tarjetaProfesional;
        this.universidad = universidad;
        this.especialidades = especialidades;
    }

    /** @return el número de la tarjeta profesional. */
    public String getTarjetaProfesional() {
        return tarjetaProfesional;
    }

    /** @param tarjetaProfesional nuevo número de tarjeta profesional. */
    public void setTarjetaProfesional(String tarjetaProfesional) {
        this.tarjetaProfesional = tarjetaProfesional;
    }

    /** @return la universidad de graduación. */
    public String getUniversidad() {
        return universidad;
    }

    /** @param universidad nueva universidad de graduación. */
    public void setUniversidad(String universidad) {
        this.universidad = universidad;
    }

    /** @return la lista de especialidades del veterinario. */
    public ArrayList<String> getEspecialidades() {
        return especialidades;
    }

    /** @param especialidades nueva lista de especialidades. */
    public void setEspecialidades(ArrayList<String> especialidades) {
        this.especialidades = especialidades;
    }

    /**
     * Describe al veterinario con su sede, tarjeta profesional y universidad.
     *
     * @return un texto con los datos profesionales del veterinario.
     */
    @Override
    public String getDescripcion() {
        return "Veterinario de " + getSede() + ". Tarjeta: " + tarjetaProfesional + ". Universidad: " + universidad;
    }

    /**
     * Compara este {@code Veterinario} con otro objeto. Dos objetos son iguales cuando son de la misma clase
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
        Veterinario that = (Veterinario) o;
        return super.equals(o)
                && Objects.equals(tarjetaProfesional, that.tarjetaProfesional)
                && Objects.equals(universidad, that.universidad)
                && Objects.equals(especialidades, that.especialidades);
    }

    /**
     * Calcula el código hash a partir de los mismos atributos que usa {@link #equals(Object)}, para que dos
     * objetos iguales tengan siempre el mismo código.
     *
     * @return el código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tarjetaProfesional, universidad, especialidades);
    }

    /**
     * Devuelve el contenido del {@code Veterinario} como texto, con el nombre y el valor de cada atributo.
     * Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "Veterinario{tarjetaProfesional=" + tarjetaProfesional
                + ", universidad=" + universidad
                + ", especialidades=" + especialidades
                + ", super=" + super.toString()
                + "}";
    }
}
