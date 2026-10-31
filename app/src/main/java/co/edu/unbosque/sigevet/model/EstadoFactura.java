package co.edu.unbosque.sigevet.model;

import java.util.Arrays;

/**
 * Estados posibles de una {@link Factura}. Es la lista que ofrece el filtro de la página de facturas; los mismos
 * valores están en la restricción CHECK de la tabla FACTURA de la base de datos.
 */
public enum EstadoFactura {
    /** La factura ya fue pagada. */
    PAGADA,
    /** La factura está emitida y aún no se paga. */
    PENDIENTE,
    /** La factura fue anulada. */
    ANULADA;

    /**
     * Devuelve los nombres de todos los estados, en el orden en que están declarados.
     *
     * @return un arreglo con los nombres, listo para usarse en un filtro de la vista.
     */
    public static String[] nombres() {
        return Arrays.stream(values()).map(Enum::name).toArray(String[]::new);
    }
}
