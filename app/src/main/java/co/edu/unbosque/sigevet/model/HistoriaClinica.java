package co.edu.unbosque.sigevet.model;

import java.util.Objects;

/**
 * Historia clínica de una mascota: sus datos médicos generales (grupo sanguíneo, alergias y
 * enfermedades crónicas). Cada mascota tiene una sola historia clínica.
 */
public class HistoriaClinica {
    /** Identificador de la historia clínica (llave primaria). */
    private int idHistoria;
    /** Número de la historia clínica, visible para el personal. */
    private String numeroHistoria;
    /** Fecha en que se abrió la historia, como texto. */
    private String fechaApertura;
    /** Grupo sanguíneo de la mascota. */
    private String grupoSanguineo;
    /** Alergias conocidas de la mascota. */
    private String alergias;
    /** Enfermedades crónicas de la mascota. */
    private String enfermedadesCronicas;
    /** Observaciones generales sobre la historia. */
    private String observaciones;

    /** Constructor vacío: los atributos se llenan con los setters al leer la base de datos. */
    public HistoriaClinica() {
    }

    /**
     * Crea el objeto {@code HistoriaClinica} con todos sus atributos.
     *
     * @param idHistoria identificador de la historia clínica (llave primaria).
     * @param numeroHistoria número de la historia clínica, visible para el personal.
     * @param fechaApertura fecha en que se abrió la historia, como texto.
     * @param grupoSanguineo grupo sanguíneo de la mascota.
     * @param alergias alergias conocidas de la mascota.
     * @param enfermedadesCronicas enfermedades crónicas de la mascota.
     * @param observaciones observaciones generales sobre la historia.
     */
    public HistoriaClinica(int idHistoria, String numeroHistoria, String fechaApertura, String grupoSanguineo,
        String alergias, String enfermedadesCronicas, String observaciones) {
        this.idHistoria = idHistoria;
        this.numeroHistoria = numeroHistoria;
        this.fechaApertura = fechaApertura;
        this.grupoSanguineo = grupoSanguineo;
        this.alergias = alergias;
        this.enfermedadesCronicas = enfermedadesCronicas;
        this.observaciones = observaciones;
    }

    /** @return el identificador de la historia clínica. */
    public int getIdHistoria() {
        return idHistoria;
    }

    /** @param idHistoria nuevo identificador de la historia. */
    public void setIdHistoria(int idHistoria) {
        this.idHistoria = idHistoria;
    }

    /** @return el número de la historia clínica. */
    public String getNumeroHistoria() {
        return numeroHistoria;
    }

    /** @param numeroHistoria nuevo número de historia. */
    public void setNumeroHistoria(String numeroHistoria) {
        this.numeroHistoria = numeroHistoria;
    }

    /** @return la fecha de apertura. */
    public String getFechaApertura() {
        return fechaApertura;
    }

    /** @param fechaApertura nueva fecha de apertura. */
    public void setFechaApertura(String fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    /** @return el grupo sanguíneo. */
    public String getGrupoSanguineo() {
        return grupoSanguineo;
    }

    /** @param grupoSanguineo nuevo grupo sanguíneo. */
    public void setGrupoSanguineo(String grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }

    /** @return las alergias conocidas. */
    public String getAlergias() {
        return alergias;
    }

    /** @param alergias nuevas alergias. */
    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    /** @return las enfermedades crónicas. */
    public String getEnfermedadesCronicas() {
        return enfermedadesCronicas;
    }

    /** @param enfermedadesCronicas nuevas enfermedades crónicas. */
    public void setEnfermedadesCronicas(String enfermedadesCronicas) {
        this.enfermedadesCronicas = enfermedadesCronicas;
    }

    /** @return las observaciones generales. */
    public String getObservaciones() {
        return observaciones;
    }

    /** @param observaciones nuevas observaciones. */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    /**
     * Compara este {@code HistoriaClinica} con otro objeto. Dos objetos son iguales cuando son de la misma clase
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
        HistoriaClinica that = (HistoriaClinica) o;
        return idHistoria == that.idHistoria
                && Objects.equals(numeroHistoria, that.numeroHistoria)
                && Objects.equals(fechaApertura, that.fechaApertura)
                && Objects.equals(grupoSanguineo, that.grupoSanguineo)
                && Objects.equals(alergias, that.alergias)
                && Objects.equals(enfermedadesCronicas, that.enfermedadesCronicas)
                && Objects.equals(observaciones, that.observaciones);
    }

    /**
     * Calcula el código hash a partir de los mismos atributos que usa {@link #equals(Object)}, para que dos
     * objetos iguales tengan siempre el mismo código.
     *
     * @return el código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(idHistoria, numeroHistoria, fechaApertura, grupoSanguineo, alergias, enfermedadesCronicas,
                observaciones);
    }

    /**
     * Devuelve el contenido del {@code HistoriaClinica} como texto, con el nombre y el valor de cada atributo.
     * Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "HistoriaClinica{idHistoria=" + idHistoria
                + ", numeroHistoria=" + numeroHistoria
                + ", fechaApertura=" + fechaApertura
                + ", grupoSanguineo=" + grupoSanguineo
                + ", alergias=" + alergias
                + ", enfermedadesCronicas=" + enfermedadesCronicas
                + ", observaciones=" + observaciones
                + "}";
    }
}
