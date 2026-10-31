package co.edu.unbosque.sigevet.util;

/**
 * Utilidades para trabajar con texto que llega desde los formularios de las páginas.
 */
public class TextoUtil {

    /**
     * Convierte un filtro vacío en {@code null}. Cuando el usuario deja un filtro sin seleccionar, el navegador envía
     * un texto vacío; los DAO interpretan {@code null} como "sin filtro".
     *
     * @param texto valor recibido del formulario; puede ser {@code null} o estar vacío.
     * @return {@code null} si el texto es nulo o solo tiene espacios; en otro caso, el mismo texto.
     */
    public static String vacioANulo(String texto) {
        if (texto == null || texto.isBlank()) {
            return null;
        }
        return texto;
    }
}
