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
public class InicioDAO {
    @Autowired
    private ConexionBD conexionBD;

    public ArrayList<Resumen> conteos() {
        ArrayList<Resumen> lista = new ArrayList<Resumen>();
        String sql = "SELECT 'DEPARTAMENTO' AS tabla, COUNT(*) AS total FROM DEPARTAMENTO "
                + "UNION ALL SELECT 'CIUDAD', COUNT(*) FROM CIUDAD "
                + "UNION ALL SELECT 'SEDE', COUNT(*) FROM SEDE "
                + "UNION ALL SELECT 'EMPLEADO', COUNT(*) FROM EMPLEADO "
                + "UNION ALL SELECT 'VETERINARIO', COUNT(*) FROM VETERINARIO "
                + "UNION ALL SELECT 'AUXILIAR', COUNT(*) FROM AUXILIAR "
                + "UNION ALL SELECT 'ESPECIALIDAD', COUNT(*) FROM ESPECIALIDAD "
                + "UNION ALL SELECT 'VETERINARIO_ESPECIALIDAD', COUNT(*) FROM VETERINARIO_ESPECIALIDAD "
                + "UNION ALL SELECT 'CLIENTE', COUNT(*) FROM CLIENTE "
                + "UNION ALL SELECT 'TELEFONO_CLIENTE', COUNT(*) FROM TELEFONO_CLIENTE "
                + "UNION ALL SELECT 'ESPECIE', COUNT(*) FROM ESPECIE "
                + "UNION ALL SELECT 'RAZA', COUNT(*) FROM RAZA "
                + "UNION ALL SELECT 'MASCOTA', COUNT(*) FROM MASCOTA "
                + "UNION ALL SELECT 'HISTORIA_CLINICA', COUNT(*) FROM HISTORIA_CLINICA "
                + "UNION ALL SELECT 'SERVICIO', COUNT(*) FROM SERVICIO "
                + "UNION ALL SELECT 'CITA', COUNT(*) FROM CITA "
                + "UNION ALL SELECT 'CONSULTA', COUNT(*) FROM CONSULTA "
                + "UNION ALL SELECT 'MEDICAMENTO', COUNT(*) FROM MEDICAMENTO "
                + "UNION ALL SELECT 'PRESCRIPCION', COUNT(*) FROM PRESCRIPCION "
                + "UNION ALL SELECT 'INVENTARIO', COUNT(*) FROM INVENTARIO "
                + "UNION ALL SELECT 'FACTURA', COUNT(*) FROM FACTURA "
                + "UNION ALL SELECT 'DETALLE_FACTURA', COUNT(*) FROM DETALLE_FACTURA";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    Resumen objeto = new Resumen();
                    objeto.setNombre(rs.getString("tabla"));
                    objeto.setTotal(rs.getBigDecimal("total"));
                    lista.add(objeto);
                }
            }
            return lista;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo conteos los datos.", e);
        }
    }

    public ArrayList<Resumen> ingresos() {
        ArrayList<Resumen> lista = new ArrayList<Resumen>();
        String sql = "SELECT s.nombre AS sede, IFNULL(SUM(d.cantidad*d.precio_unitario*(1-d.descuento_pct/100)),0) AS total "
                + "FROM SEDE s LEFT JOIN FACTURA f ON f.id_sede=s.id_sede AND f.estado='PAGADA' "
                + "LEFT JOIN DETALLE_FACTURA d ON d.id_factura=f.id_factura "
                + "GROUP BY s.id_sede,s.nombre ORDER BY s.nombre";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    Resumen objeto = new Resumen();
                    objeto.setNombre(rs.getString("sede"));
                    objeto.setTotal(rs.getBigDecimal("total"));
                    lista.add(objeto);
                }
            }
            return lista;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo ingresos los datos.", e);
        }
    }

    public ArrayList<Resumen> estadosCitas() {
        ArrayList<Resumen> lista = new ArrayList<Resumen>();
        String sql = "SELECT estado,COUNT(*) AS total FROM CITA GROUP BY estado ORDER BY estado";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    Resumen objeto = new Resumen();
                    objeto.setNombre(rs.getString("estado"));
                    objeto.setTotal(rs.getBigDecimal("total"));
                    lista.add(objeto);
                }
            }
            return lista;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo estadosCitas los datos.", e);
        }
    }
}
