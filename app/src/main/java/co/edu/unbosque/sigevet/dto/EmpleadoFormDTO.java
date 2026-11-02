package co.edu.unbosque.sigevet.dto;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Datos que envía el formulario de crear o editar un empleado. Además de los datos de EMPLEADO, trae los del
 * subtipo según tipoEmpleado: VETERINARIO (con sus especialidades) o AUXILIAR. Todo se guarda en una sola
 * transacción.
 */
public class EmpleadoFormDTO {
    /** CC, CE, PA o PPT. */
    private String tipoDocumento;
    /** Número de documento. */
    private String numDocumento;
    /** Nombres. */
    private String nombres;
    /** Apellidos. */
    private String apellidos;
    /** Fecha de nacimiento aaaa-mm-dd. */
    private String fechaNacimiento;
    /** Teléfono. */
    private String telefono;
    /** Correo corporativo. */
    private String email;
    /** VET, AUX o ADM (discriminador; no se puede cambiar al editar). */
    private String tipoEmpleado;
    /** Fecha de ingreso aaaa-mm-dd. */
    private String fechaIngreso;
    /** Salario mensual. */
    private BigDecimal salario;
    /** Sede donde labora. */
    private Integer idSede;
    /** Estado actual (solo lectura en el formulario). */
    private String estado;
    /** VET: tarjeta profesional. */
    private String tarjetaProfesional;
    /** VET: universidad. */
    private String universidad;
    /** VET: año de graduación. */
    private Integer anioGraduacion;
    /** VET: identificadores de ESPECIALIDAD (relación N:M CERTIFICAR). */
    private ArrayList<Integer> especialidades = new ArrayList<Integer>();
    /** AUX: área. */
    private String area;
    /** AUX: certificación (opcional). */
    private String certificacion;
    /** AUX: turno. */
    private String turno;

    /** Constructor vacío: lo usa Jackson para crear el objeto a partir del JSON. */
    public EmpleadoFormDTO() {
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

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipoEmpleado() {
        return tipoEmpleado;
    }

    public void setTipoEmpleado(String tipoEmpleado) {
        this.tipoEmpleado = tipoEmpleado;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public Integer getIdSede() {
        return idSede;
    }

    public void setIdSede(Integer idSede) {
        this.idSede = idSede;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTarjetaProfesional() {
        return tarjetaProfesional;
    }

    public void setTarjetaProfesional(String tarjetaProfesional) {
        this.tarjetaProfesional = tarjetaProfesional;
    }

    public String getUniversidad() {
        return universidad;
    }

    public void setUniversidad(String universidad) {
        this.universidad = universidad;
    }

    public Integer getAnioGraduacion() {
        return anioGraduacion;
    }

    public void setAnioGraduacion(Integer anioGraduacion) {
        this.anioGraduacion = anioGraduacion;
    }

    public ArrayList<Integer> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(ArrayList<Integer> especialidades) {
        this.especialidades = especialidades;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCertificacion() {
        return certificacion;
    }

    public void setCertificacion(String certificacion) {
        this.certificacion = certificacion;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    @Override
    public String toString() {
        return "EmpleadoFormDTO{tipoDocumento=" + tipoDocumento + ", numDocumento=" + numDocumento + ", nombres=" + nombres + ", apellidos=" + apellidos + ", fechaNacimiento=" + fechaNacimiento + ", telefono=" + telefono + ", email=" + email + ", tipoEmpleado=" + tipoEmpleado + ", fechaIngreso=" + fechaIngreso + ", salario=" + salario + ", idSede=" + idSede + ", estado=" + estado + ", tarjetaProfesional=" + tarjetaProfesional + ", universidad=" + universidad + ", anioGraduacion=" + anioGraduacion + ", especialidades=" + especialidades + ", area=" + area + ", certificacion=" + certificacion + ", turno=" + turno + "}";
    }
}
