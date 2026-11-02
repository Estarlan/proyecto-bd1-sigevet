package co.edu.unbosque.sigevet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción propia que lanza la capa de servicio cuando no existe el registro que se pidió (por ejemplo, un cliente
 * con un identificador que no está en la base de datos).
 *
 * <p>{@code ManejadorErroresApi} la convierte en una respuesta HTTP 404 con un JSON {@code {estado, mensaje}}, sin que cada
 * controlador tenga que revisar si el dato existe. La anotación {@code @ResponseStatus} deja el 404 como respaldo.</p>
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
