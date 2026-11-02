package co.edu.unbosque.sigevet.dto;

/**
 * Respuesta JSON que la API envía cuando algo falla, por ejemplo
 * {@code {"estado": 404, "mensaje": "No existe el cliente 99"}}. El frontend en Angular la lee para mostrar el error.
 */
public class ErrorApiDTO {
    /** Código de estado HTTP (400, 404, 500...). */
    private int estado;
    /** Explicación del error para mostrar al usuario. */
    private String mensaje;

    /**
     * Crea la respuesta de error.
     *
     * @param estado código de estado HTTP.
     * @param mensaje explicación del error.
     */
    public ErrorApiDTO(int estado, String mensaje) {
        this.estado = estado;
        this.mensaje = mensaje;
    }

    /** @return el código de estado HTTP. */
    public int getEstado() {
        return estado;
    }

    /** @return la explicación del error. */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Devuelve el error como texto. Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "ErrorApiDTO{estado=" + estado + ", mensaje=" + mensaje + "}";
    }
}
