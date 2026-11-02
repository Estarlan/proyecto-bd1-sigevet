package co.edu.unbosque.sigevet.dto;

/**
 * Respuesta con un mensaje para mostrar al usuario después de una operación, por ejemplo
 * {@code {"mensaje": "La mascota quedó FALLECIDA. Se cancelaron 2 citas programadas."}}.
 */
public class MensajeDTO {
    /** Texto para mostrar. */
    private String mensaje;

    /**
     * @param mensaje texto para mostrar.
     */
    public MensajeDTO(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    @Override
    public String toString() {
        return "MensajeDTO{mensaje=" + mensaje + "}";
    }
}
