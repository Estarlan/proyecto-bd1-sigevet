package co.edu.unbosque.sigevet.exception;

/**
 * Excepción que lanza la capa de servicio cuando un formulario llega con datos incompletos o con un formato que no
 * es válido (por ejemplo, un correo sin arroba o una fecha de cita en el pasado). La API responde 400.
 *
 * <p>Se valida en Java antes de ir a la base para dar mensajes claros; aun así, las restricciones CHECK, UNIQUE y
 * FOREIGN KEY de MySQL siguen siendo la última defensa.</p>
 */
public class DatosInvalidosException extends RuntimeException {
    /** Versión de la clase para la serialización. */
    private static final long serialVersionUID = 1L;

    /**
     * @param mensaje explicación de qué dato está mal.
     */
    public DatosInvalidosException(String mensaje) {
        super(mensaje);
    }
}
