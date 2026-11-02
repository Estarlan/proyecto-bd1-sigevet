package co.edu.unbosque.sigevet.dto;

/**
 * Datos que envía el formulario de crear o editar una mascota, junto con su historia clínica (relación 1:1 TENER):
 * las dos filas se guardan en la misma transacción.
 */
public class MascotaFormDTO {
    /** Nombre de la mascota. */
    private String nombre;
    /** Fecha de nacimiento aaaa-mm-dd (opcional). */
    private String fechaNacimiento;
    /** M o H. */
    private String sexo;
    /** Color (opcional). */
    private String color;
    /** Microchip de 15 dígitos (opcional). */
    private String microchip;
    /** S o N. */
    private String esterilizado;
    /** Raza (FK a RAZA; la especie sale de la raza). */
    private Integer idRaza;
    /** Propietario (FK a CLIENTE). */
    private Integer idCliente;
    /** Estado actual (solo lectura en el formulario). */
    private String estado;
    /** Historia clínica: grupo sanguíneo. */
    private String grupoSanguineo;
    /** Historia clínica: alergias. */
    private String alergias;
    /** Historia clínica: enfermedades crónicas. */
    private String enfermedadesCronicas;
    /** Historia clínica: observaciones. */
    private String observaciones;

    /** Constructor vacío: lo usa Jackson para crear el objeto a partir del JSON. */
    public MascotaFormDTO() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMicrochip() {
        return microchip;
    }

    public void setMicrochip(String microchip) {
        this.microchip = microchip;
    }

    public String getEsterilizado() {
        return esterilizado;
    }

    public void setEsterilizado(String esterilizado) {
        this.esterilizado = esterilizado;
    }

    public Integer getIdRaza() {
        return idRaza;
    }

    public void setIdRaza(Integer idRaza) {
        this.idRaza = idRaza;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getGrupoSanguineo() {
        return grupoSanguineo;
    }

    public void setGrupoSanguineo(String grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getEnfermedadesCronicas() {
        return enfermedadesCronicas;
    }

    public void setEnfermedadesCronicas(String enfermedadesCronicas) {
        this.enfermedadesCronicas = enfermedadesCronicas;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "MascotaFormDTO{nombre=" + nombre + ", fechaNacimiento=" + fechaNacimiento + ", sexo=" + sexo + ", color=" + color + ", microchip=" + microchip + ", esterilizado=" + esterilizado + ", idRaza=" + idRaza + ", idCliente=" + idCliente + ", estado=" + estado + ", grupoSanguineo=" + grupoSanguineo + ", alergias=" + alergias + ", enfermedadesCronicas=" + enfermedadesCronicas + ", observaciones=" + observaciones + "}";
    }
}
