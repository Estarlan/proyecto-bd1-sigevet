package co.edu.unbosque.sigevet.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import co.edu.unbosque.sigevet.exception.DatosInvalidosException;

/**
 * Validaciones de los formularios antes de enviarlos a la base. Cada método lanza
 * {@link DatosInvalidosException} (respuesta 400) con un mensaje que nombra el campo.
 *
 * <p>Los límites de longitud y los valores permitidos son los mismos de las columnas y de las restricciones CHECK de
 * 01_crear_tablas.sql, para que el usuario vea el error antes de que lo rechace MySQL.</p>
 */
public class Validador {

    /** Correo con usuario, arroba, dominio y extensión. */
    private static final String PATRON_CORREO = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$";

    // ------------------------------------------------------------------
    // Formatos de los campos. Son los mismos patrones de
    // frontend/src/app/shared/validaciones.ts, para que el navegador y el
    // servidor acepten y rechacen exactamente lo mismo.
    // ------------------------------------------------------------------

    /** Letras del español, con tildes y eñe (sin números ni símbolos). */
    public static final String LETRAS = "A-Za-zÁÉÍÓÚÜÑáéíóúüñ";
    /** Nombres y apellidos de personas: palabras de letras separadas por un espacio, apóstrofo o guion. */
    public static final String PATRON_NOMBRE_PERSONA = "^[" + LETRAS + "]+([ '\\-][" + LETRAS + "]+)*$";
    /** CC, CE y PPT: solo dígitos, de 6 a 10. */
    public static final String PATRON_DOCUMENTO_NUMERICO = "^[0-9]{6,10}$";
    /** Pasaporte: letras y dígitos, de 6 a 12. */
    public static final String PATRON_PASAPORTE = "^[A-Za-z0-9]{6,12}$";
    /** Teléfono colombiano: 7 dígitos (fijo local) a 10 dígitos (celular o fijo con indicativo). */
    public static final String PATRON_TELEFONO = "^[0-9]{7,10}$";
    /** Dirección: letras, dígitos y # . , ° - con al menos un número (Cl. 26 #7-12). */
    public static final String PATRON_DIRECCION = "^(?=.*[0-9])[" + LETRAS + "0-9#.,°\\- ]{5,120}$";
    /** Nombre de una mascota: palabras de letras separadas por espacio, punto, apóstrofo o guion. */
    public static final String PATRON_NOMBRE_MASCOTA = "^[" + LETRAS + "]+([ '.\\-]+[" + LETRAS + "]+)*$";
    /** Color del pelaje o plumaje: palabras de letras separadas por espacios, comas o guiones. */
    public static final String PATRON_COLOR = "^[" + LETRAS + "]+([ ,\\-]+[" + LETRAS + "]+)*$";
    /** Microchip ISO 11784: exactamente 15 dígitos. */
    public static final String PATRON_MICROCHIP = "^[0-9]{15}$";
    /** Grupo sanguíneo (A, B, AB, DEA 1.1+...). */
    public static final String PATRON_GRUPO_SANGUINEO = "^[A-Za-z0-9 .+\\-\\/]{1,10}$";
    /** Tarjeta profesional: 2 a 4 letras, guion opcional y dígitos (TP-20037). */
    public static final String PATRON_TARJETA = "^[A-Za-z]{2,4}-?[0-9]{3,10}$";
    /** Universidad o certificación: letras, dígitos y . , - */
    public static final String PATRON_INSTITUCION = "^[" + LETRAS + "0-9 .,\\-]{3,100}$";

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
        return correo(valor, campo, false);
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

    // ------------------------------------------------------------------
    // Validaciones de formato
    // ------------------------------------------------------------------

    /**
     * Revisa que un texto ya recortado cumpla un patrón.
     *
     * @param explicacion qué se espera, para el mensaje (por ejemplo "solo letras").
     */
    public static String formato(String valor, String campo, String patron, String explicacion) {
        if (valor != null && !valor.matches(patron)) {
            throw new DatosInvalidosException("El campo " + campo + " no es válido: " + explicacion + ".");
        }
        return valor;
    }

    /** Une los espacios repetidos en uno solo ("Juan   Carlos" → "Juan Carlos"). */
    public static String espacios(String valor) {
        return valor == null ? null : valor.trim().replaceAll("\\s+", " ");
    }

    /**
     * Número de documento según su tipo: CC, CE y PPT solo dígitos (6 a 10); el pasaporte admite letras y se guarda
     * en mayúsculas.
     *
     * @return el número limpio, listo para guardar.
     */
    public static String documento(String tipo, String numero) {
        String limpio = obligatorio(numero, "número de documento", 15).replaceAll("[\\s.]", "");
        if ("PA".equals(tipo)) {
            return formato(limpio.toUpperCase(), "número de documento", PATRON_PASAPORTE, "el pasaporte lleva de 6 a 12 letras o números");
        }
        return formato(limpio, "número de documento", PATRON_DOCUMENTO_NUMERICO, "solo números, de 6 a 10 dígitos");
    }

    /** Nombres o apellidos obligatorios: solo letras, sin números ni símbolos. */
    public static String nombrePersona(String valor, String campo) {
        String limpio = obligatorio(espacios(valor), campo, 60);
        if (limpio.length() < 2) {
            throw new DatosInvalidosException("El campo " + campo + " debe tener al menos 2 letras.");
        }
        return formato(limpio, campo, PATRON_NOMBRE_PERSONA, "solo letras, sin números ni símbolos");
    }

    /** Teléfono: se quitan espacios, guiones y paréntesis y deben quedar de 7 a 10 dígitos. */
    public static String telefono(String valor, String campo) {
        String limpio = valor == null ? "" : valor.replaceAll("[\\s\\-()]", "");
        if (limpio.isEmpty()) {
            throw new DatosInvalidosException("El campo " + campo + " es obligatorio.");
        }
        return formato(limpio, campo, PATRON_TELEFONO, "solo números, de 7 a 10 dígitos");
    }

    /** Correo obligatorio u opcional, siempre en minúsculas. */
    public static String correo(String valor, String campo, boolean obligatorio) {
        String limpio = obligatorio ? obligatorio(valor, campo, 100) : opcional(valor, campo, 100);
        if (limpio == null) {
            return null;
        }
        limpio = limpio.toLowerCase();
        return formato(limpio, campo, PATRON_CORREO, "debe tener la forma usuario@dominio.com");
    }

    /** Texto libre (síntomas, alergias, motivo...): sin caracteres de control, con largo máximo. */
    public static String textoLibre(String valor, String campo, int maximo) {
        String limpio = opcional(valor, campo, maximo);
        if (limpio != null && limpio.matches("(?s).*[\\p{Cntrl}&&[^\\n\\r\\t]].*")) {
            throw new DatosInvalidosException("El campo " + campo + " tiene caracteres no permitidos.");
        }
        return limpio;
    }

    /** Fecha obligatoria dentro de un rango (inclusive). */
    public static LocalDate fechaEntre(String valor, String campo, LocalDate desde, LocalDate hasta) {
        LocalDate fecha = fecha(valor, campo);
        if (fecha.isBefore(desde) || fecha.isAfter(hasta)) {
            throw new DatosInvalidosException("El campo " + campo + " debe estar entre " + desde + " y " + hasta + ".");
        }
        return fecha;
    }

    /** Decimal con máximo dos decimales y dentro de un rango (salarios, descuentos). */
    public static BigDecimal decimalEntre(BigDecimal valor, String campo, BigDecimal minimo, BigDecimal maximo) {
        if (valor == null || valor.compareTo(minimo) < 0 || valor.compareTo(maximo) > 0) {
            throw new DatosInvalidosException("El campo " + campo + " debe estar entre " + minimo.toPlainString()
                    + " y " + maximo.toPlainString() + ".");
        }
        if (valor.stripTrailingZeros().scale() > 2) {
            throw new DatosInvalidosException("El campo " + campo + " admite máximo dos decimales.");
        }
        return valor;
    }
}
