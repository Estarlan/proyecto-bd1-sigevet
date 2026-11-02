package co.edu.unbosque.sigevet.dto;

import java.io.Serializable;

/**
 * Datos del empleado que inició sesión. Se guardan en la sesión HTTP del servidor y se envían a Angular para mostrar
 * quién está conectado. No lleva el documento: no hace falta mostrarlo.
 */
public class SesionDTO implements Serializable {
    /** Versión de la clase para la serialización (la sesión HTTP puede guardarse en disco). */
    private static final long serialVersionUID = 1L;
    /** Identificador del empleado. */
    private int idEmpleado;
    /** Nombres y apellidos del empleado. */
    private String nombreCompleto;
    /** Cargo según el discriminador tipo_empleado: Veterinario, Auxiliar o Administrativo. */
    private String cargo;
    /** Nombre de la sede donde labora. */
    private String sede;

    /** Constructor vacío. */
    public SesionDTO() {
    }

    /** @return el identificador del empleado. */
    public int getIdEmpleado() {
        return idEmpleado;
    }

    /** @param idEmpleado el identificador del empleado. */
    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    /** @return nombres y apellidos del empleado. */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /** @param nombreCompleto nombres y apellidos del empleado. */
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    /** @return el cargo del empleado. */
    public String getCargo() {
        return cargo;
    }

    /** @param cargo el cargo del empleado. */
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    /** @return el nombre de la sede. */
    public String getSede() {
        return sede;
    }

    /** @param sede el nombre de la sede. */
    public void setSede(String sede) {
        this.sede = sede;
    }
}
