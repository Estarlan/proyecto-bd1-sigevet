package co.edu.unbosque.sigevet.dto;

import java.util.ArrayList;

/**
 * Datos que envía el formulario de crear o editar un cliente. Los teléfonos van en la misma petición y se guardan
 * en la misma transacción.
 */
public class ClienteFormDTO {
    /** CC, CE, PA o PPT. */
    private String tipoDocumento;
    /** Número de documento. */
    private String numDocumento;
    /** Nombres. */
    private String nombres;
    /** Apellidos. */
    private String apellidos;
    /** Correo (opcional). */
    private String email;
    /** Dirección de residencia. */
    private String direccion;
    /** Ciudad de residencia (FK a CIUDAD). */
    private Integer idCiudad;
    /** Teléfonos del cliente. */
    private ArrayList<TelefonoFormDTO> telefonos = new ArrayList<TelefonoFormDTO>();

    /** Constructor vacío: lo usa Jackson para crear el objeto a partir del JSON. */
    public ClienteFormDTO() {
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(Integer idCiudad) {
        this.idCiudad = idCiudad;
    }

    public ArrayList<TelefonoFormDTO> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(ArrayList<TelefonoFormDTO> telefonos) {
        this.telefonos = telefonos;
    }

    @Override
    public String toString() {
        return "ClienteFormDTO{tipoDocumento=" + tipoDocumento + ", numDocumento=" + numDocumento + ", nombres=" + nombres + ", apellidos=" + apellidos + ", email=" + email + ", direccion=" + direccion + ", idCiudad=" + idCiudad + ", telefonos=" + telefonos + "}";
    }
}
