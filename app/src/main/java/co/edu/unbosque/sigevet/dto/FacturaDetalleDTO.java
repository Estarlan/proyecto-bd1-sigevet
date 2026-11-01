package co.edu.unbosque.sigevet.dto;

import co.edu.unbosque.sigevet.model.DetalleFactura;
import co.edu.unbosque.sigevet.model.Factura;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Objeto de transferencia de datos (DTO) con todo lo que necesita la página de una factura: la factura y sus líneas.
 *
 * <p>No tiene lógica: solo guarda los datos que el servicio reunió desde varios DAO.</p>
 */
public class FacturaDetalleDTO {
    /** Datos de la factura. */
    private Factura factura;
    /** Líneas (conceptos cobrados) de la factura. */
    private ArrayList<DetalleFactura> lineas;

    /**
     * Constructor vacío: crea el DTO sin datos. Todos sus atributos quedan en {@code null}.
     */
    public FacturaDetalleDTO() {
    }

    /**
     * Crea el DTO con todos sus datos.
     *
     * @param factura datos de la factura.
     * @param lineas líneas de la factura.
     */
    public FacturaDetalleDTO(Factura factura, ArrayList<DetalleFactura> lineas) {
        this.factura = factura;
        this.lineas = lineas;
    }

    /** @return los datos de la factura. */
    public Factura getFactura() {
        return factura;
    }

    /** @return las líneas de la factura. */
    public ArrayList<DetalleFactura> getLineas() {
        return lineas;
    }

    /**
     * Compara este {@code FacturaDetalleDTO} con otro objeto. Dos objetos son iguales cuando son de la misma clase
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
        FacturaDetalleDTO that = (FacturaDetalleDTO) o;
        return Objects.equals(factura, that.factura)
                && Objects.equals(lineas, that.lineas);
    }

    /**
     * Calcula el código hash a partir de los mismos atributos que usa {@link #equals(Object)}, para que dos
     * objetos iguales tengan siempre el mismo código.
     *
     * @return el código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(factura, lineas);
    }

    /**
     * Devuelve el contenido del {@code FacturaDetalleDTO} como texto, con el nombre y el valor de cada atributo.
     * Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "FacturaDetalleDTO{factura=" + factura
                + ", lineas=" + lineas
                + "}";
    }
}
