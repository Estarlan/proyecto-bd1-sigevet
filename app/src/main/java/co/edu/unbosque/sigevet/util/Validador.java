package co.edu.unbosque.sigevet.util;

import co.edu.unbosque.sigevet.exception.DatosInvalidosException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

/**
 * Validaciones de los formularios antes de enviarlos a la base. Cada método lanza
 * {@link DatosInvalidosException} (respuesta 400) con un mensaje que nombra el campo.
 *
 * <p>Los límites de longitud y los valores permitidos son los mismos de las columnas y de las restricciones CHECK de
 * 01_crear_tablas.sql, para que el usuario vea el error antes de que lo rechace MySQL.</p>
 */
public class Validador {

    /** Correo con usuario, arroba, dominio y extensión. */
    private static final String PATRON_CORREO = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";

    /**
     * Revisa un texto obligatorio y devuelve el texto sin espacios en los extremos.
     */
    public static String obligatorio(String valor, String campo, int maximo) {
        if (valor == null || valor.isBlank()) {
            throw new DatosInvalidosException("El campo " + campo + " es obligatorio.");
        }
        return largo(valor.trim(), campo, maximo);
    }

    /**
     * Revisa un texto opcional: devuelve {@code null} si llegó vacío, o el texto recortado.
     */
    public static String opcional(String valor, String campo, int maximo) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return largo(valor.trim(), campo, maximo);
    }

    private static String largo(String valor, String campo, int maximo) {
        if (valor.length() > maximo) {
            throw new DatosInvalidosException("El campo " + campo + " admite máximo " + maximo + " caracteres.");
        }
        return valor;
    }

    /**
     * Revisa que el valor sea uno de los permitidos por una restricción CHECK.
     */
    public static String unoDe(String valor, String campo, String... permitidos) {
        if (valor == null || !Arrays.asList(permitidos).contains(valor)) {
            throw new DatosInvalidosException("El campo " + campo + " debe ser uno de: " + String.join(", ", permitidos) + ".");
        }
        return valor;
    }

    /**
     * Revisa un identificador obligatorio (llave foránea elegida en un select).
     */
    public static int id(Integer valor, String campo) {
        if (valor == null || valor <= 0) {
            throw new DatosInvalidosException("Debe seleccionar " + campo + ".");
        }
        return valor;
    }

    /**
     * Revisa un entero dentro de un rango.
     */
    public static int entero(Integer valor, String campo, int minimo, int maximo) {
        if (valor == null || valor < minimo || valor > maximo) {
            throw new DatosInvalidosException("El campo " + campo + " debe estar entre " + minimo + " y " + maximo + ".");
        }
        return valor;
    }

    /**
     * Revisa un valor decimal mayor que cero (salarios, precios).
     */
    public static BigDecimal positivo(BigDecimal valor, String campo) {
        if (valor == null || valor.signum() <= 0) {
            throw new DatosInvalidosException("El campo " + campo + " debe ser mayor que cero.");
        }
        return valor;
    }

    /**
     * Revisa un correo opcional.
     */
    public static String correo(String valor, String campo) {
        String correo = opcional(valor, campo, 100);
        if (correo != null && !correo.matches(PATRON_CORREO)) {
            throw new DatosInvalidosException("El campo " + campo + " no es un correo válido.");
        }
        return correo;
    }

    /**
     * Revisa un texto que solo puede tener dígitos (teléfonos, microchip).
     */
    public static String digitos(String valor, String campo, int minimo, int maximo) {
        if (valor == null || !valor.matches("^[0-9]{" + minimo + "," + maximo + "}$")) {
            throw new DatosInvalidosException("El campo " + campo + " debe tener entre " + minimo + " y " + maximo + " dígitos.");
        }
        return valor;
    }

    /**
     * Convierte una fecha con formato aaaa-mm-dd.
     */
    public static LocalDate fecha(String valor, String campo) {
        try {
            return LocalDate.parse(obligatorio(valor, campo, 10));
        } catch (DateTimeParseException e) {
            throw new DatosInvalidosException("El campo " + campo + " debe tener el formato aaaa-mm-dd.");
        }
    }

    /**
     * Convierte una fecha y hora con formato aaaa-mm-ddThh:mm (el que envía un input datetime-local).
     */
    public static LocalDateTime fechaHora(String valor, String campo) {
        try {
            return LocalDateTime.parse(obligatorio(valor, campo, 19));
        } catch (DateTimeParseException e) {
            throw new DatosInvalidosException("El campo " + campo + " debe tener fecha y hora válidas.");
        }
    }
}
