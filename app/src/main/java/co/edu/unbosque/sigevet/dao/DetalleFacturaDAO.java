package co.edu.unbosque.sigevet.dao;

import co.edu.unbosque.sigevet.conexion.ConexionBD;
import co.edu.unbosque.sigevet.modelo.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DetalleFacturaDAO {
    @Autowired
    private ConexionBD conexionBD;

    public ArrayList<DetalleFactura> listar(long id) {
        ArrayList<DetalleFactura> lista = new ArrayList<DetalleFactura>();
        String sql = "SELECT d.num_linea, COALESCE(s.nombre, m.nombre) AS concepto, d.cantidad, d.precio_unitario, d.descuento_pct, d.cantidad * d.precio_unitario * (1 - d.descuento_pct / 100) AS subtotal "
                + "FROM DETALLE_FACTURA d "
                + "LEFT JOIN SERVICIO s ON s.id_servicio = d.id_servicio "
                + "LEFT JOIN MEDICAMENTO m ON m.id_medicamento = d.id_medicamento "
                + "WHERE d.id_factura = ? ORDER BY d.num_linea";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
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
