package co.edu.unbosque.sigevet.dto;

/**
 * Respuesta de una creación: el identificador que MySQL generó (AUTO_INCREMENT), para que el frontend pueda abrir
 * el registro nuevo. Ejemplo: {@code {"id": 60}}.
 */
public class IdDTO {
    /** Identificador generado. */
    private long id;

    /**
     * @param id identificador generado.
     */
    public IdDTO(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "IdDTO{id=" + id + "}";
    }
}
