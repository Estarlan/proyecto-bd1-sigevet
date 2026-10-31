package co.edu.unbosque.sigevet.dao;

import co.edu.unbosque.sigevet.connection.ConexionBD;
import co.edu.unbosque.sigevet.model.DetalleFactura;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;

/**
 * Acceso a datos de las líneas de las facturas. Ejecuta el SQL con JDBC y {@code PreparedStatement}, sin ORM, y
 * convierte cada fila en un objeto {@link DetalleFactura}.
 */
@Repository
public class DetalleFacturaDAO {
    /** Fábrica de conexiones a la base de datos. */
    private final ConexionBD conexionBD;

    /**
     * Constructor con inyección de dependencias: Spring entrega la conexión al crear el DAO.
     *
     * @param conexionBD fábrica de conexiones a la base de datos.
     */
    public DetalleFacturaDAO(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Lista las líneas de una factura, ordenadas por número de línea.
     *
     * @param id identificador de la factura.
     * @return las líneas de la factura; vacía si la factura no tiene líneas.
     * @throws RuntimeException si la consulta falla.
     */
    public ArrayList<DetalleFactura> listar(long id) {
        ArrayList<DetalleFactura> lista = new ArrayList<DetalleFactura>();
        // cada línea cobra un servicio o un medicamento: COALESCE toma el nombre del que no sea nulo.
        // El subtotal se calcula aquí: cantidad por precio unitario, menos el porcentaje de descuento.
        String sql = "SELECT d.num_linea, COALESCE(s.nombre, m.nombre) AS concepto, d.cantidad, d.precio_unitario, d.descuento_pct, d.cantidad * d.precio_unitario * (1 - d.descuento_pct / 100) AS subtotal "
                + "FROM DETALLE_FACTURA d "
                + "LEFT JOIN SERVICIO s ON s.id_servicio = d.id_servicio "
                + "LEFT JOIN MEDICAMENTO m ON m.id_medicamento = d.id_medicamento "
                + "WHERE d.id_factura = ? ORDER BY d.num_linea";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            // el parámetro se envía aparte del SQL, así no se puede inyectar código malicioso
            sentencia.setLong(1, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    DetalleFactura objeto = new DetalleFactura();
                    objeto.setNumLinea(rs.getInt("num_linea"));
                    objeto.setConcepto(rs.getString("concepto"));
                    objeto.setCantidad(rs.getInt("cantidad"));
                    objeto.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
                    objeto.setDescuentoPct(rs.getBigDecimal("descuento_pct"));
                    objeto.setSubtotal(rs.getBigDecimal("subtotal"));
                    lista.add(objeto);
                }
            }
            return lista;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo listar los datos.", e);
        }
    }
}
