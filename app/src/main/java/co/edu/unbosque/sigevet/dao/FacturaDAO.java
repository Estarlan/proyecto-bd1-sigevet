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
public class FacturaDAO {
    @Autowired
    private ConexionBD conexionBD;

    public ArrayList<Factura> listar(Integer sede, String estado) {
        ArrayList<Factura> lista = new ArrayList<Factura>();
        String sql = "SELECT f.id_cliente,f.metodo_pago,f.id_factura, f.numero_factura, f.fecha_emision, f.estado, CONCAT(c.nombres, ' ', c.apellidos) AS cliente, s.nombre AS sede, IFNULL(t.total, 0) AS total "
                + "FROM FACTURA f JOIN CLIENTE c ON c.id_cliente = f.id_cliente "
                + "JOIN SEDE s ON s.id_sede = f.id_sede "
                + "LEFT JOIN ( "
                + "SELECT id_factura, SUM(cantidad * precio_unitario * (1 - descuento_pct / 100)) AS total "
                + "FROM DETALLE_FACTURA GROUP BY id_factura ) t ON t.id_factura = f.id_factura "
                + "WHERE (? IS NULL OR f.id_sede = ?) "
                + "AND (? IS NULL OR f.estado = ?) "
                + "ORDER BY f.fecha_emision DESC, f.id_factura DESC";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            if (sede == null) {
                sentencia.setNull(1, Types.INTEGER);
                sentencia.setNull(2, Types.INTEGER);
            } else {
                sentencia.setInt(1, sede);
                sentencia.setInt(2, sede);
            }
            sentencia.setString(3, estado);
            sentencia.setString(4, estado);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    Factura objeto = new Factura();
                    objeto.setIdFactura(rs.getLong("id_factura"));
                    objeto.setIdCliente(rs.getInt("id_cliente"));
                    objeto.setNumeroFactura(rs.getString("numero_factura"));
                    objeto.setFechaEmision(rs.getString("fecha_emision"));
                    objeto.setMetodoPago(rs.getString("metodo_pago"));
                    objeto.setEstado(rs.getString("estado"));
                    objeto.setCliente(rs.getString("cliente"));
                    objeto.setSede(rs.getString("sede"));
                    objeto.setTotal(rs.getBigDecimal("total"));
                    lista.add(objeto);
                }
            }
            return lista;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo listar los datos.", e);
        }
    }

    public Factura buscar(long id) {
        String sql = "SELECT f.*, CONCAT(c.nombres, ' ', c.apellidos) AS cliente, s.nombre AS sede, IFNULL(t.total, 0) AS total "
                + "FROM FACTURA f JOIN CLIENTE c ON c.id_cliente = f.id_cliente "
                + "JOIN SEDE s ON s.id_sede = f.id_sede "
                + "LEFT JOIN ( "
                + "SELECT id_factura, SUM(cantidad * precio_unitario * (1 - descuento_pct / 100)) AS total "
                + "FROM DETALLE_FACTURA GROUP BY id_factura ) t ON t.id_factura = f.id_factura "
                + "WHERE f.id_factura = ?";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setLong(1, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    Factura objeto = new Factura();
                    objeto.setIdFactura(rs.getLong("id_factura"));
                    objeto.setIdCliente(rs.getInt("id_cliente"));
                    objeto.setNumeroFactura(rs.getString("numero_factura"));
                    objeto.setFechaEmision(rs.getString("fecha_emision"));
                    objeto.setMetodoPago(rs.getString("metodo_pago"));
                    objeto.setEstado(rs.getString("estado"));
                    objeto.setCliente(rs.getString("cliente"));
                    objeto.setSede(rs.getString("sede"));
                    objeto.setTotal(rs.getBigDecimal("total"));
                    return objeto;
                }
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo buscar los datos.", e);
        }
    }

    public ArrayList<Factura> listarPorCliente(int id) {
        ArrayList<Factura> lista = new ArrayList<Factura>();
        String sql = "SELECT f.id_cliente,f.metodo_pago,f.id_factura, f.numero_factura, f.fecha_emision, f.estado, CONCAT(c.nombres, ' ', c.apellidos) AS cliente, s.nombre AS sede, IFNULL(t.total, 0) AS total "
                + "FROM FACTURA f JOIN CLIENTE c ON c.id_cliente = f.id_cliente "
                + "JOIN SEDE s ON s.id_sede = f.id_sede "
                + "LEFT JOIN ( "
                + "SELECT id_factura, SUM(cantidad * precio_unitario * (1 - descuento_pct / 100)) AS total "
                + "FROM DETALLE_FACTURA GROUP BY id_factura ) t ON t.id_factura = f.id_factura "
                + "WHERE f.id_cliente = ? "
                + "ORDER BY f.fecha_emision DESC, f.id_factura DESC";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    Factura objeto = new Factura();
                    objeto.setIdFactura(rs.getLong("id_factura"));
                    objeto.setIdCliente(rs.getInt("id_cliente"));
                    objeto.setNumeroFactura(rs.getString("numero_factura"));
                    objeto.setFechaEmision(rs.getString("fecha_emision"));
                    objeto.setMetodoPago(rs.getString("metodo_pago"));
                    objeto.setEstado(rs.getString("estado"));
                    objeto.setCliente(rs.getString("cliente"));
                    objeto.setSede(rs.getString("sede"));
                    objeto.setTotal(rs.getBigDecimal("total"));
                    lista.add(objeto);
                }
            }
            return lista;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudieron consultar las facturas del cliente.", e);
        }
    }
}
