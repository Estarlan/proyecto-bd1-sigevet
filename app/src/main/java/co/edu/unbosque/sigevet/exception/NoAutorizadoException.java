package co.edu.unbosque.sigevet.exception;

/**
 * Excepción que se lanza cuando alguien intenta usar la aplicación sin haber iniciado sesión, o cuando el correo y el
 * documento no corresponden a un empleado activo. La API responde 401.
 */
public class NoAutorizadoException extends RuntimeException {
    /** Versión de la clase para la serialización. */
    private static final long serialVersionUID = 1L;

    /**
     * @param mensaje explicación para mostrar en la página de inicio de sesión.
     */
    public NoAutorizadoException(String mensaje) {
        super(mensaje);
    }
}
