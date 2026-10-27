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
public class ClienteDAO {
    @Autowired
    private ConexionBD conexionBD;

    public ArrayList<Cliente> listar(String buscar) {
        ArrayList<Cliente> lista = new ArrayList<Cliente>();
        String sql = "SELECT c.*, ci.nombre AS ciudad, d.nombre AS departamento "
                + "FROM CLIENTE c JOIN CIUDAD ci ON ci.id_ciudad = c.id_ciudad "
                + "JOIN DEPARTAMENTO d ON d.id_departamento = ci.id_departamento "
                + "WHERE UPPER(c.num_documento) LIKE ? OR UPPER(c.apellidos) LIKE ? "
                + "ORDER BY c.apellidos, c.nombres, c.id_cliente LIMIT 100";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            String texto = "%" + buscar.toUpperCase() + "%";
            sentencia.setString(1, texto);
            sentencia.setString(2, texto);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    Cliente objeto = new Cliente();
                    objeto.setIdPersona(rs.getInt("id_cliente"));
                    objeto.setTipoDocumento(rs.getString("tipo_documento"));
                    objeto.setNumDocumento(rs.getString("num_documento"));
                    objeto.setNombres(rs.getString("nombres"));
                    objeto.setApellidos(rs.getString("apellidos"));
                    objeto.setEmail(rs.getString("email"));
                    objeto.setDireccion(rs.getString("direccion"));
                    objeto.setCiudad(rs.getString("ciudad"));
                    objeto.setDepartamento(rs.getString("departamento"));
                    objeto.setFechaRegistro(rs.getString("fecha_registro"));
                    lista.add(objeto);
                }
            }
            return lista;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo listar los datos.", e);
        }
    }

    public Cliente buscar(int id) {
        String sql = "SELECT c.*, ci.nombre AS ciudad, d.nombre AS departamento "
                + "FROM CLIENTE c JOIN CIUDAD ci ON ci.id_ciudad = c.id_ciudad "
                + "JOIN DEPARTAMENTO d ON d.id_departamento = ci.id_departamento "
                + "WHERE c.id_cliente = ?";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    Cliente objeto = new Cliente();
                    objeto.setIdPersona(rs.getInt("id_cliente"));
                    objeto.setTipoDocumento(rs.getString("tipo_documento"));
                    objeto.setNumDocumento(rs.getString("num_documento"));
                    objeto.setNombres(rs.getString("nombres"));
                    objeto.setApellidos(rs.getString("apellidos"));
                    objeto.setEmail(rs.getString("email"));
                    objeto.setDireccion(rs.getString("direccion"));
                    objeto.setCiudad(rs.getString("ciudad"));
                    objeto.setDepartamento(rs.getString("departamento"));
                    objeto.setFechaRegistro(rs.getString("fecha_registro"));
                    return objeto;
                }
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo buscar los datos.", e);
        }
    }

    public ArrayList<Telefono> telefonos(int id) {
        ArrayList<Telefono> lista = new ArrayList<Telefono>();
        String sql = "SELECT telefono, tipo FROM TELEFONO_CLIENTE "
                + "WHERE id_cliente = ? ORDER BY tipo, telefono";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    Telefono objeto = new Telefono();
                    objeto.setTelefono(rs.getString("telefono"));
                    objeto.setTipo(rs.getString("tipo"));
                    lista.add(objeto);
                }
            }
            return lista;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudieron consultar los teléfonos del cliente.", e);
        }
    }
}
