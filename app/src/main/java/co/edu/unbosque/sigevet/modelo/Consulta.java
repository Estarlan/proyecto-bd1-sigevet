package co.edu.unbosque.sigevet.modelo;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Consulta {
    private long idConsulta;
    private String fechaHoraInicio;
    private BigDecimal pesoKg;
    private BigDecimal temperaturaC;
    private String frecuenciaCardiaca;
    private String sintomas;
    private String diagnostico;
    private String observaciones;
    private String veterinario;
    private String sede;
    private ArrayList<Prescripcion> prescripciones = new ArrayList<Prescripcion>();

    public Consulta() {
    }

    public long getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(long idConsulta) {
        this.idConsulta = idConsulta;
    }

    public String getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(String fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public BigDecimal getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(BigDecimal pesoKg) {
        this.pesoKg = pesoKg;
    }

    public BigDecimal getTemperaturaC() {
        return temperaturaC;
    }

    public void setTemperaturaC(BigDecimal temperaturaC) {
        this.temperaturaC = temperaturaC;
    }

    public String getFrecuenciaCardiaca() {
        return frecuenciaCardiaca;
    }

    public void setFrecuenciaCardiaca(String frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(String veterinario) {
        this.veterinario = veterinario;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public ArrayList<Prescripcion> getPrescripciones() {
        return prescripciones;
    }

    public void setPrescripciones(ArrayList<Prescripcion> prescripciones) {
        this.prescripciones = prescripciones;
    }
}
