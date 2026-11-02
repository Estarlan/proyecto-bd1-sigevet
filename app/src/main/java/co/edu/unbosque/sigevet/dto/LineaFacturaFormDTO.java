package co.edu.unbosque.sigevet.dto;

import java.math.BigDecimal;

/**
 * Una línea de la factura nueva: cobra un servicio O un medicamento (arco exclusivo CK_DETALLE_FACTURA_ITEM). El
 * precio no lo envía el navegador: el DAO lo lee de SERVICIO o MEDICAMENTO.
 */
public class LineaFacturaFormDTO {
    /** Servicio cobrado (o null). */
    private Integer idServicio;
    /** Medicamento vendido (o null). */
    private Integer idMedicamento;
    /** Cantidad. */
    private Integer cantidad;
    /** Descuento en porcentaje (0 a 100). */
    private BigDecimal descuentoPct;

    /** Constructor vacío: lo usa Jackson para crear el objeto a partir del JSON. */
    public LineaFacturaFormDTO() {
    }

    public Integer getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Integer idServicio) {
        this.idServicio = idServicio;
    }

    public Integer getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(Integer idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getDescuentoPct() {
        return descuentoPct;
    }

    public void setDescuentoPct(BigDecimal descuentoPct) {
        this.descuentoPct = descuentoPct;
    }

    @Override
    public String toString() {
        return "LineaFacturaFormDTO{idServicio=" + idServicio + ", idMedicamento=" + idMedicamento + ", cantidad=" + cantidad + ", descuentoPct=" + descuentoPct + "}";
    }
}
