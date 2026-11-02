package co.edu.unbosque.sigevet.dto;

/**
 * Datos para agregar un medicamento al inventario de una sede o para cambiar sus existencias.
 */
public class InventarioFormDTO {
    /** Sede. */
    private Integer idSede;
    /** Medicamento. */
    private Integer idMedicamento;
    /** Unidades disponibles. */
    private Integer stockActual;
    /** Punto de reorden. */
    private Integer stockMinimo;

    /** Constructor vacío: lo usa Jackson para crear el objeto a partir del JSON. */
    public InventarioFormDTO() {
    }

    public Integer getIdSede() {
        return idSede;
    }

    public void setIdSede(Integer idSede) {
        this.idSede = idSede;
    }

    public Integer getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(Integer idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public Integer getStockActual() {
        return stockActual;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    @Override
    public String toString() {
        return "InventarioFormDTO{idSede=" + idSede + ", idMedicamento=" + idMedicamento + ", stockActual=" + stockActual + ", stockMinimo=" + stockMinimo + "}";
    }
}
