package co.edu.unbosque.sigevet.model;

import java.util.Objects;

/**
 * Existencia de un medicamento en el inventario de una sede.
 */
public class ItemInventario {
    /** Nombre de la sede donde está el inventario. */
    private String sede;
    /** Nombre del medicamento. */
    private String medicamento;
    /** Presentación del medicamento. */
    private String presentacion;
    /** Unidades disponibles ahora. */
    private int stockActual;
    /** Unidades mínimas que debe haber; por debajo de este valor se considera stock bajo. */
    private int stockMinimo;
    /** Fecha de la última actualización del inventario, como texto. */
    private String fechaActualizacion;

    /** Constructor vacío: los atributos se llenan con los setters al leer la base de datos. */
    public ItemInventario() {
    }

    /**
     * Crea el objeto {@code ItemInventario} con todos sus atributos.
     *
     * @param sede nombre de la sede donde está el inventario.
     * @param medicamento nombre del medicamento.
     * @param presentacion presentación del medicamento.
     * @param stockActual unidades disponibles ahora.
     * @param stockMinimo unidades mínimas que debe haber; por debajo de este valor se considera stock bajo.
     * @param fechaActualizacion fecha de la última actualización del inventario, como texto.
     */
    public ItemInventario(String sede, String medicamento, String presentacion, int stockActual, int stockMinimo,
        String fechaActualizacion) {
        this.sede = sede;
        this.medicamento = medicamento;
        this.presentacion = presentacion;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.fechaActualizacion = fechaActualizacion;
    }

    /** @return el nombre de la sede. */
    public String getSede() {
        return sede;
    }

    /** @param sede nuevo nombre de la sede. */
    public void setSede(String sede) {
        this.sede = sede;
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

    /** @param presentacion nueva presentación. */
    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    /** @return las unidades disponibles. */
    public int getStockActual() {
        return stockActual;
    }

    /** @param stockActual nuevas unidades disponibles. */
    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    /** @return el stock mínimo. */
    public int getStockMinimo() {
        return stockMinimo;
    }

    /** @param stockMinimo nuevo stock mínimo. */
    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    /** @return la fecha de la última actualización. */
    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    /** @param fechaActualizacion nueva fecha de actualización. */
    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    /**
     * Indica si el medicamento necesita reposición. La vista lo usa para resaltar la fila.
     *
     * @return {@code true} si las unidades actuales son menores que el mínimo.
     */
    public boolean isStockBajo() {
        return stockActual < stockMinimo;
    }

    /**
     * Compara este {@code ItemInventario} con otro objeto. Dos objetos son iguales cuando son de la misma clase
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
        ItemInventario that = (ItemInventario) o;
        return Objects.equals(sede, that.sede)
                && Objects.equals(medicamento, that.medicamento)
                && Objects.equals(presentacion, that.presentacion)
                && stockActual == that.stockActual
                && stockMinimo == that.stockMinimo
                && Objects.equals(fechaActualizacion, that.fechaActualizacion);
    }

    /**
     * Calcula el código hash a partir de los mismos atributos que usa {@link #equals(Object)}, para que dos
     * objetos iguales tengan siempre el mismo código.
     *
     * @return el código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(sede, medicamento, presentacion, stockActual, stockMinimo, fechaActualizacion);
    }

    /**
     * Devuelve el contenido del {@code ItemInventario} como texto, con el nombre y el valor de cada atributo.
     * Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "ItemInventario{sede=" + sede
                + ", medicamento=" + medicamento
                + ", presentacion=" + presentacion
                + ", stockActual=" + stockActual
                + ", stockMinimo=" + stockMinimo
                + ", fechaActualizacion=" + fechaActualizacion
                + "}";
    }
}
