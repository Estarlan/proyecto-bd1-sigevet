package co.edu.unbosque.sigevet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción propia que lanza la capa de servicio cuando no existe el registro que se pidió (por ejemplo, un cliente
 * con un identificador que no está en la base de datos).
 *
 * <p>La anotación {@code @ResponseStatus} hace que Spring la convierta en una respuesta HTTP 404 y muestre la
 * plantilla {@code error/404.html}, sin que cada controlador tenga que revisar si el dato existe.</p>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecursoNoEncontradoException extends RuntimeException {
    /** Versión de la clase para la serialización (requerida por {@link RuntimeException}). */
    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepción con un mensaje que explica qué no se encontró.
     *
     * @param mensaje descripción del recurso que no existe.
     */
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
