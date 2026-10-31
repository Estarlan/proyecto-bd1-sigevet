package co.edu.unbosque.sigevet.model;

import java.util.Arrays;

/**
 * Estados posibles de una {@link Cita}. Es la lista que ofrece el filtro de la página de agenda; los mismos valores
 * están en la restricción CHECK de la tabla CITA de la base de datos.
 */
public enum EstadoCita {
    /** La cita está agendada y todavía no ocurre. */
    PROGRAMADA,
    /** La mascota fue atendida. */
    ATENDIDA,
    /** La cita fue cancelada. */
    CANCELADA,
    /** El cliente no se presentó a la cita. */
    NO_ASISTIO;

    /**
     * Devuelve los nombres de todos los estados, en el orden en que están declarados.
     *
     * @return un arreglo con los nombres, listo para usarse en un filtro de la vista.
     */
    public static String[] nombres() {
        return Arrays.stream(values()).map(Enum::name).toArray(String[]::new);
    }
}
