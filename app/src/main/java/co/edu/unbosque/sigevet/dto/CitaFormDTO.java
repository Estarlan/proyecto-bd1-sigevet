package co.edu.unbosque.sigevet.dto;

/**
 * Datos que envía el formulario de agendar una cita. El estado inicial siempre es PROGRAMADA.
 */
public class CitaFormDTO {
    /** Mascota que se atenderá. */
    private Integer idMascota;
    /** Servicio solicitado. */
    private Integer idServicio;
    /** Sede de la cita. */
    private Integer idSede;
    /** Veterinario (debe laborar en la sede: regla RN-08). */
    private Integer idVeterinario;
    /** Fecha y hora aaaa-mm-ddThh:mm. */
    private String fechaHora;
    /** Motivo informado por el cliente (opcional). */
    private String motivo;

    /** Constructor vacío: lo usa Jackson para crear el objeto a partir del JSON. */
    public CitaFormDTO() {
    }

    public Integer getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(Integer idMascota) {
        this.idMascota = idMascota;
    }

    public Integer getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Integer idServicio) {
        this.idServicio = idServicio;
    }

    public Integer getIdSede() {
        return idSede;
    }

    public void setIdSede(Integer idSede) {
        this.idSede = idSede;
    }

    public Integer getIdVeterinario() {
        return idVeterinario;
    }

    public void setIdVeterinario(Integer idVeterinario) {
        this.idVeterinario = idVeterinario;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    @Override
    public String toString() {
        return "CitaFormDTO{idMascota=" + idMascota + ", idServicio=" + idServicio + ", idSede=" + idSede + ", idVeterinario=" + idVeterinario + ", fechaHora=" + fechaHora + ", motivo=" + motivo + "}";
    }
}
