package co.edu.unbosque.sigevet.dto;

/**
 * Cuerpo de las peticiones PATCH que solo cambian el estado de un registro (borrado lógico de mascotas y
 * empleados, estado de citas y facturas).
 */
public class EstadoDTO {
    /** Nuevo estado. */
    private String estado;

    /** Constructor vacío: lo usa Jackson para crear el objeto a partir del JSON. */
    public EstadoDTO() {
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "EstadoDTO{estado=" + estado + "}";
    }
}
