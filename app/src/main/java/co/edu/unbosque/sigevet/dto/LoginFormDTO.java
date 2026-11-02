package co.edu.unbosque.sigevet.dto;

/**
 * Formulario de inicio de sesión que llega en el cuerpo de {@code POST /api/sesion}: el correo corporativo del
 * empleado y su número de documento.
 */
public class LoginFormDTO {
    /** Correo corporativo del empleado (columna EMPLEADO.email, que es UNIQUE). */
    private String email;
    /** Número de documento del empleado (columna EMPLEADO.num_documento). */
    private String documento;

    /** Constructor vacío que usa Jackson para leer el JSON. */
    public LoginFormDTO() {
    }

    /** @return el correo escrito en el formulario. */
    public String getEmail() {
        return email;
    }

    /** @param email el correo escrito en el formulario. */
    public void setEmail(String email) {
        this.email = email;
    }

    /** @return el número de documento escrito en el formulario. */
    public String getDocumento() {
        return documento;
    }

    /** @param documento el número de documento escrito en el formulario. */
    public void setDocumento(String documento) {
        this.documento = documento;
    }
}
