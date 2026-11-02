package co.edu.unbosque.sigevet.dto;

/**
 * Un teléfono del cliente dentro del formulario de cliente (tabla TELEFONO_CLIENTE).
 */
public class TelefonoFormDTO {
    /** Número telefónico (solo dígitos). */
    private String telefono;
    /** MOVIL, FIJO o TRABAJO (CHECK CK_TELEFONO_CLIENTE_TIPO). */
    private String tipo;

    /** Constructor vacío: lo usa Jackson para crear el objeto a partir del JSON. */
    public TelefonoFormDTO() {
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "TelefonoFormDTO{telefono=" + telefono + ", tipo=" + tipo + "}";
    }
}
