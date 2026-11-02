package co.edu.unbosque.sigevet.exception;

/**
 * Excepción que se lanza cuando la operación choca con los datos que ya existen o con una regla de negocio: un
 * documento repetido (UNIQUE), un cliente con historial que se quiere borrar, un disparador que rechaza la cita
 * (RN-08) o un medicamento sin existencias. La API responde 409 (conflicto).
 */
public class ConflictoDatosException extends RuntimeException {
    /** Versión de la clase para la serialización. */
    private static final long serialVersionUID = 1L;

    /**
     * @param mensaje explicación del conflicto, lista para mostrar al usuario.
     */
    public ConflictoDatosException(String mensaje) {
        super(mensaje);
    }
}
