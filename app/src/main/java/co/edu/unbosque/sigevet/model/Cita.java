package co.edu.unbosque.sigevet.model;

import java.util.Objects;

/**
 * Cita agendada para una mascota en una sede, con un veterinario y para un servicio concreto.
 *
 * <p>Los atributos de texto como {@code mascota}, {@code servicio}, {@code sede} y {@code veterinario} guardan el
 * nombre ya resuelto desde las tablas relacionadas.</p>
 */
public class Cita {
    /** Identificador de la cita (llave primaria). */
    private long idCita;
    /** Identificador de la mascota que asiste (llave foránea). */
    private int idMascota;
    /** Fecha y hora de la cita, como texto. */
    private String fechaHora;
    /** Motivo de la consulta. */
    private String motivo;
    /** Estado de la cita (PROGRAMADA, ATENDIDA, CANCELADA o NO_ASISTIO). Ver {@link EstadoCita}. */
    private String estado;
    /** Nombre de la mascota. */
    private String mascota;
    /** Nombre del servicio solicitado (por ejemplo vacunación). */
    private String servicio;
    /** Nombre de la sede donde se atiende. */
    private String sede;
    /** Nombre completo del veterinario asignado. */
    private String veterinario;

    /** Constructor vacío: los atributos se llenan con los setters al leer la base de datos. */
    public Cita() {
    }

    /**
     * Crea el objeto {@code Cita} con todos sus atributos.
     *
     * @param idCita identificador de la cita (llave primaria).
     * @param idMascota identificador de la mascota que asiste (llave foránea).
     * @param fechaHora fecha y hora de la cita, como texto.
     * @param motivo motivo de la consulta.
     * @param estado estado de la cita (PROGRAMADA, ATENDIDA, CANCELADA o NO_ASISTIO).
     * @param mascota nombre de la mascota.
     * @param servicio nombre del servicio solicitado (por ejemplo vacunación).
     * @param sede nombre de la sede donde se atiende.
     * @param veterinario nombre completo del veterinario asignado.
     */
    public Cita(long idCita, int idMascota, String fechaHora, String motivo, String estado, String mascota,
        String servicio, String sede, String veterinario) {
        this.idCita = idCita;
        this.idMascota = idMascota;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.estado = estado;
        this.mascota = mascota;
        this.servicio = servicio;
        this.sede = sede;
        this.veterinario = veterinario;
    }

    /** @return el identificador de la cita. */
    public long getIdCita() {
        return idCita;
    }

    /** @param idCita nuevo identificador de la cita. */
    public void setIdCita(long idCita) {
        this.idCita = idCita;
    }

    /** @return el identificador de la mascota. */
    public int getIdMascota() {
        return idMascota;
    }

    /** @param idMascota nuevo identificador de la mascota. */
    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }

    /** @return la fecha y hora de la cita. */
    public String getFechaHora() {
        return fechaHora;
    }

    /** @param fechaHora nueva fecha y hora de la cita. */
    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    /** @return el motivo de la cita. */
    public String getMotivo() {
        return motivo;
    }

    /** @param motivo nuevo motivo de la cita. */
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    /** @return el estado de la cita. */
    public String getEstado() {
        return estado;
    }

    /** @param estado nuevo estado de la cita. */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /** @return el nombre de la mascota. */
    public String getMascota() {
        return mascota;
    }

    /** @param mascota nuevo nombre de la mascota. */
    public void setMascota(String mascota) {
        this.mascota = mascota;
    }

    /** @return el nombre del servicio solicitado. */
    public String getServicio() {
        return servicio;
    }

    /** @param servicio nuevo nombre del servicio. */
    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    /** @return el nombre de la sede. */
    public String getSede() {
        return sede;
    }

    /** @param sede nuevo nombre de la sede. */
    public void setSede(String sede) {
        this.sede = sede;
    }

    /** @return el nombre del veterinario asignado. */
    public String getVeterinario() {
        return veterinario;
    }

    /** @param veterinario nuevo nombre del veterinario. */
    public void setVeterinario(String veterinario) {
        this.veterinario = veterinario;
    }

    /**
     * Compara este {@code Cita} con otro objeto. Dos objetos son iguales cuando son de la misma clase
     * y todos sus atributos tienen el mismo valor.
     *
     * @param o objeto con el que se compara.
     * @return {@code true} si los dos objetos tienen los mismos valores; {@code false} en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cita that = (Cita) o;
        return idCita == that.idCita
                && idMascota == that.idMascota
                && Objects.equals(fechaHora, that.fechaHora)
                && Objects.equals(motivo, that.motivo)
                && Objects.equals(estado, that.estado)
                && Objects.equals(mascota, that.mascota)
                && Objects.equals(servicio, that.servicio)
                && Objects.equals(sede, that.sede)
                && Objects.equals(veterinario, that.veterinario);
    }

    /**
     * Calcula el código hash a partir de los mismos atributos que usa {@link #equals(Object)}, para que dos
     * objetos iguales tengan siempre el mismo código.
     *
     * @return el código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(idCita, idMascota, fechaHora, motivo, estado, mascota, servicio, sede, veterinario);
    }

    /**
     * Devuelve el contenido del {@code Cita} como texto, con el nombre y el valor de cada atributo.
     * Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "Cita{idCita=" + idCita
                + ", idMascota=" + idMascota
                + ", fechaHora=" + fechaHora
                + ", motivo=" + motivo
                + ", estado=" + estado
                + ", mascota=" + mascota
                + ", servicio=" + servicio
                + ", sede=" + sede
                + ", veterinario=" + veterinario
                + "}";
    }
}
