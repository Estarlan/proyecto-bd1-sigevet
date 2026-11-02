package co.edu.unbosque.sigevet.dto;

import java.util.ArrayList;

/**
 * Datos que envía el formulario de nueva factura. La factura y todas sus líneas se guardan en una sola
 * transacción.
 */
public class FacturaFormDTO {
    /** Cliente al que se factura. */
    private Integer idCliente;
    /** Sede que emite. */
    private Integer idSede;
    /** Empleado que emite (de esa sede). */
    private Integer idEmpleado;
    /** EFECTIVO, TARJETA_DEBITO, TARJETA_CREDITO o TRANSFERENCIA. */
    private String metodoPago;
    /** PAGADA o PENDIENTE. */
    private String estado;
    /** Líneas de la factura. */
    private ArrayList<LineaFacturaFormDTO> lineas = new ArrayList<LineaFacturaFormDTO>();

    /** Constructor vacío: lo usa Jackson para crear el objeto a partir del JSON. */
    public FacturaFormDTO() {
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdSede() {
        return idSede;
    }

    public void setIdSede(Integer idSede) {
        this.idSede = idSede;
    }

    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public ArrayList<LineaFacturaFormDTO> getLineas() {
        return lineas;
    }

    public void setLineas(ArrayList<LineaFacturaFormDTO> lineas) {
        this.lineas = lineas;
    }

    @Override
    public String toString() {
        return "FacturaFormDTO{idCliente=" + idCliente + ", idSede=" + idSede + ", idEmpleado=" + idEmpleado + ", metodoPago=" + metodoPago + ", estado=" + estado + ", lineas=" + lineas + "}";
    }
}
