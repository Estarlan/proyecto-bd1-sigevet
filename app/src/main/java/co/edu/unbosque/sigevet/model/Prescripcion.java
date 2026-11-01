package co.edu.unbosque.sigevet.model;

import java.util.Objects;

/**
 * Medicamento recetado dentro de una {@link Consulta}, con su dosis, frecuencia y duración.
 */
public class Prescripcion {
    /** Identificador de la consulta a la que pertenece la prescripción. Sirve para asignarla a su consulta. */
    private long idConsulta;
    /** Nombre del medicamento. */
    private String medicamento;
    /** Presentación del medicamento (tabletas, jarabe, etc.). */
    private String presentacion;
    /** Dosis indicada en cada toma. */
    private String dosis;
    /** Cada cuántas horas se debe dar el medicamento. */
    private int frecuenciaHoras;
    /** Durante cuántos días se debe dar el medicamento. */
    private int duracionDias;
    /** Cantidad total de unidades recetadas. */
    private int cantidad;
    /** Indicaciones adicionales para el dueño de la mascota. */
    private String indicaciones;

    /** Constructor vacío: los atributos se llenan con los setters al leer la base de datos. */
    public Prescripcion() {
    }

    /**
     * Crea el objeto {@code Prescripcion} con todos sus atributos.
     *
     * @param idConsulta identificador de la consulta a la que pertenece la prescripción.
     * @param medicamento nombre del medicamento.
     * @param presentacion presentación del medicamento (tabletas, jarabe, etc.).
     * @param dosis dosis indicada en cada toma.
     * @param frecuenciaHoras cada cuántas horas se debe dar el medicamento.
     * @param duracionDias durante cuántos días se debe dar el medicamento.
     * @param cantidad cantidad total de unidades recetadas.
     * @param indicaciones indicaciones adicionales para el dueño de la mascota.
     */
    public Prescripcion(long idConsulta, String medicamento, String presentacion, String dosis, int frecuenciaHoras,
        int duracionDias, int cantidad, String indicaciones) {
        this.idConsulta = idConsulta;
        this.medicamento = medicamento;
        this.presentacion = presentacion;
        this.dosis = dosis;
        this.frecuenciaHoras = frecuenciaHoras;
        this.duracionDias = duracionDias;
        this.cantidad = cantidad;
        this.indicaciones = indicaciones;
    }

    /** @return el identificador de la consulta a la que pertenece. */
    public long getIdConsulta() {
        return idConsulta;
    }

    /** @param idConsulta nuevo identificador de la consulta. */
    public void setIdConsulta(long idConsulta) {
        this.idConsulta = idConsulta;
    }

    /** @return el nombre del medicamento. */
    public String getMedicamento() {
        return medicamento;
    }

    /** @param medicamento nuevo nombre del medicamento. */
    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    /** @return la presentación del medicamento. */
    public String getPresentacion() {
        return presentacion;
    }

    /** @param presentacion nueva presentación del medicamento. */
    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    /** @return la dosis de cada toma. */
    public String getDosis() {
        return dosis;
    }

    /** @param dosis nueva dosis. */
    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    /** @return cada cuántas horas se da el medicamento. */
    public int getFrecuenciaHoras() {
        return frecuenciaHoras;
    }

    /** @param frecuenciaHoras nueva frecuencia en horas. */
    public void setFrecuenciaHoras(int frecuenciaHoras) {
        this.frecuenciaHoras = frecuenciaHoras;
    }

    /** @return la duración del tratamiento en días. */
    public int getDuracionDias() {
        return duracionDias;
    }

    /** @param duracionDias nueva duración en días. */
    public void setDuracionDias(int duracionDias) {
        this.duracionDias = duracionDias;
    }

    /** @return la cantidad total recetada. */
    public int getCantidad() {
        return cantidad;
    }

    /** @param cantidad nueva cantidad recetada. */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /** @return las indicaciones adicionales. */
    public String getIndicaciones() {
        return indicaciones;
    }

    /** @param indicaciones nuevas indicaciones. */
    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    /**
     * Compara este {@code Prescripcion} con otro objeto. Dos objetos son iguales cuando son de la misma clase
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
        Prescripcion that = (Prescripcion) o;
        return idConsulta == that.idConsulta
                && Objects.equals(medicamento, that.medicamento)
                && Objects.equals(presentacion, that.presentacion)
                && Objects.equals(dosis, that.dosis)
                && frecuenciaHoras == that.frecuenciaHoras
                && duracionDias == that.duracionDias
                && cantidad == that.cantidad
                && Objects.equals(indicaciones, that.indicaciones);
    }

    /**
     * Calcula el código hash a partir de los mismos atributos que usa {@link #equals(Object)}, para que dos
     * objetos iguales tengan siempre el mismo código.
     *
     * @return el código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(idConsulta, medicamento, presentacion, dosis, frecuenciaHoras, duracionDias, cantidad,
                indicaciones);
    }

    /**
     * Devuelve el contenido del {@code Prescripcion} como texto, con el nombre y el valor de cada atributo.
     * Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "Prescripcion{idConsulta=" + idConsulta
                + ", medicamento=" + medicamento
                + ", presentacion=" + presentacion
                + ", dosis=" + dosis
                + ", frecuenciaHoras=" + frecuenciaHoras
                + ", duracionDias=" + duracionDias
                + ", cantidad=" + cantidad
                + ", indicaciones=" + indicaciones
                + "}";
    }
}
