package co.edu.unbosque.sigevet.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Consulta clínica realizada a una mascota: signos vitales, diagnóstico y medicamentos recetados.
 *
 * <p>Una consulta puede tener varias prescripciones. El servicio las asigna a la consulta correspondiente
 * después de leerlas de la base de datos.</p>
 */
public class Consulta {
    /** Identificador de la consulta (llave primaria). */
    private long idConsulta;
    /** Fecha y hora en que empezó la consulta, como texto. */
    private String fechaHoraInicio;
    /** Peso de la mascota en kilogramos. */
    private BigDecimal pesoKg;
    /** Temperatura de la mascota en grados Celsius. */
    private BigDecimal temperaturaC;
    /** Frecuencia cardíaca medida en la consulta. */
    private String frecuenciaCardiaca;
    /** Síntomas que presentaba la mascota. */
    private String sintomas;
    /** Diagnóstico del veterinario. */
    private String diagnostico;
    /** Observaciones adicionales del veterinario. */
    private String observaciones;
    /** Nombre completo del veterinario que atendió. */
    private String veterinario;
    /** Nombre de la sede donde se hizo la consulta. */
    private String sede;
    /** Medicamentos recetados en esta consulta. Empieza vacía y se llena después. */
    private ArrayList<Prescripcion> prescripciones = new ArrayList<Prescripcion>();

    /** Constructor vacío: los atributos se llenan con los setters al leer la base de datos. */
    public Consulta() {
    }

    /**
     * Crea el objeto {@code Consulta} con todos sus atributos.
     *
     * @param idConsulta identificador de la consulta (llave primaria).
     * @param fechaHoraInicio fecha y hora en que empezó la consulta, como texto.
     * @param pesoKg peso de la mascota en kilogramos.
     * @param temperaturaC temperatura de la mascota en grados Celsius.
     * @param frecuenciaCardiaca frecuencia cardíaca medida en la consulta.
     * @param sintomas síntomas que presentaba la mascota.
     * @param diagnostico diagnóstico del veterinario.
     * @param observaciones observaciones adicionales del veterinario.
     * @param veterinario nombre completo del veterinario que atendió.
     * @param sede nombre de la sede donde se hizo la consulta.
     * @param prescripciones medicamentos recetados en esta consulta.
     */
    public Consulta(long idConsulta, String fechaHoraInicio, BigDecimal pesoKg, BigDecimal temperaturaC,
        String frecuenciaCardiaca, String sintomas, String diagnostico, String observaciones, String veterinario,
        String sede, ArrayList<Prescripcion> prescripciones) {
        this.idConsulta = idConsulta;
        this.fechaHoraInicio = fechaHoraInicio;
        this.pesoKg = pesoKg;
        this.temperaturaC = temperaturaC;
        this.frecuenciaCardiaca = frecuenciaCardiaca;
        this.sintomas = sintomas;
        this.diagnostico = diagnostico;
        this.observaciones = observaciones;
        this.veterinario = veterinario;
        this.sede = sede;
        this.prescripciones = prescripciones;
    }

    /** @return el identificador de la consulta. */
    public long getIdConsulta() {
        return idConsulta;
    }

    /** @param idConsulta nuevo identificador de la consulta. */
    public void setIdConsulta(long idConsulta) {
        this.idConsulta = idConsulta;
    }

    /** @return la fecha y hora de inicio. */
    public String getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    /** @param fechaHoraInicio nueva fecha y hora de inicio. */
    public void setFechaHoraInicio(String fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    /** @return el peso en kilogramos. */
    public BigDecimal getPesoKg() {
        return pesoKg;
    }

    /** @param pesoKg nuevo peso en kilogramos. */
    public void setPesoKg(BigDecimal pesoKg) {
        this.pesoKg = pesoKg;
    }

    /** @return la temperatura en grados Celsius. */
    public BigDecimal getTemperaturaC() {
        return temperaturaC;
    }

    /** @param temperaturaC nueva temperatura en grados Celsius. */
    public void setTemperaturaC(BigDecimal temperaturaC) {
        this.temperaturaC = temperaturaC;
    }

    /** @return la frecuencia cardíaca. */
    public String getFrecuenciaCardiaca() {
        return frecuenciaCardiaca;
    }

    /** @param frecuenciaCardiaca nueva frecuencia cardíaca. */
    public void setFrecuenciaCardiaca(String frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    /** @return los síntomas. */
    public String getSintomas() {
        return sintomas;
    }

    /** @param sintomas nuevos síntomas. */
    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    /** @return el diagnóstico. */
    public String getDiagnostico() {
        return diagnostico;
    }

    /** @param diagnostico nuevo diagnóstico. */
    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    /** @return las observaciones. */
    public String getObservaciones() {
        return observaciones;
    }

    /** @param observaciones nuevas observaciones. */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /** @return el nombre del veterinario que atendió. */
    public String getVeterinario() {
        return veterinario;
    }

    /** @param veterinario nuevo nombre del veterinario. */
    public void setVeterinario(String veterinario) {
        this.veterinario = veterinario;
    }

    /** @return el nombre de la sede. */
    public String getSede() {
        return sede;
    }

    /** @param sede nuevo nombre de la sede. */
    public void setSede(String sede) {
        this.sede = sede;
    }

    /** @return la lista de prescripciones de la consulta. */
    public ArrayList<Prescripcion> getPrescripciones() {
        return prescripciones;
    }

    /** @param prescripciones nueva lista de prescripciones. */
    public void setPrescripciones(ArrayList<Prescripcion> prescripciones) {
        this.prescripciones = prescripciones;
    }

    /**
     * Compara este {@code Consulta} con otro objeto. Dos objetos son iguales cuando son de la misma clase
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
        Consulta that = (Consulta) o;
        return idConsulta == that.idConsulta
                && Objects.equals(fechaHoraInicio, that.fechaHoraInicio)
                && Objects.equals(pesoKg, that.pesoKg)
                && Objects.equals(temperaturaC, that.temperaturaC)
                && Objects.equals(frecuenciaCardiaca, that.frecuenciaCardiaca)
                && Objects.equals(sintomas, that.sintomas)
                && Objects.equals(diagnostico, that.diagnostico)
                && Objects.equals(observaciones, that.observaciones)
                && Objects.equals(veterinario, that.veterinario)
                && Objects.equals(sede, that.sede)
                && Objects.equals(prescripciones, that.prescripciones);
    }

    /**
     * Calcula el código hash a partir de los mismos atributos que usa {@link #equals(Object)}, para que dos
     * objetos iguales tengan siempre el mismo código.
     *
     * @return el código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(idConsulta, fechaHoraInicio, pesoKg, temperaturaC, frecuenciaCardiaca, sintomas,
                diagnostico, observaciones, veterinario, sede, prescripciones);
    }

    /**
     * Devuelve el contenido del {@code Consulta} como texto, con el nombre y el valor de cada atributo.
     * Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "Consulta{idConsulta=" + idConsulta
                + ", fechaHoraInicio=" + fechaHoraInicio
                + ", pesoKg=" + pesoKg
                + ", temperaturaC=" + temperaturaC
                + ", frecuenciaCardiaca=" + frecuenciaCardiaca
                + ", sintomas=" + sintomas
                + ", diagnostico=" + diagnostico
                + ", observaciones=" + observaciones
                + ", veterinario=" + veterinario
                + ", sede=" + sede
                + ", prescripciones=" + prescripciones
                + "}";
    }
}
