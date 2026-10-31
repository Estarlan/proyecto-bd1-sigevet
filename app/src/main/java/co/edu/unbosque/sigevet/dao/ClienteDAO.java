package co.edu.unbosque.sigevet.dao;

import co.edu.unbosque.sigevet.connection.ConexionBD;
import co.edu.unbosque.sigevet.model.Cliente;
import co.edu.unbosque.sigevet.model.Telefono;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;

/**
 * Acceso a datos de los clientes y de sus teléfonos. Ejecuta el SQL con JDBC y {@code PreparedStatement}, sin ORM,
 * y convierte cada fila en un objeto {@link Cliente} o {@link Telefono}.
 */
@Repository
public class ClienteDAO {
    /** Fábrica de conexiones a la base de datos. */
    private final ConexionBD conexionBD;

    /**
     * Constructor con inyección de dependencias: Spring entrega la conexión al crear el DAO.
     *
     * @param conexionBD fábrica de conexiones a la base de datos.
     */
    public ClienteDAO(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Busca clientes por número de documento o por apellido (coincidencia parcial, sin distinguir mayúsculas).
     *
     * @param buscar texto a buscar; si está vacío, devuelve los primeros clientes.
     * @return hasta 100 clientes ordenados por apellidos y nombres.
     * @throws RuntimeException si la consulta falla.
     */
    public ArrayList<Cliente> listar(String buscar) {
        ArrayList<Cliente> lista = new ArrayList<Cliente>();
        // se unen CIUDAD y DEPARTAMENTO para traer los nombres en lugar de los identificadores
        String sql = "SELECT c.*, ci.nombre AS ciudad, d.nombre AS departamento "
                + "FROM CLIENTE c JOIN CIUDAD ci ON ci.id_ciudad = c.id_ciudad "
                + "JOIN DEPARTAMENTO d ON d.id_departamento = ci.id_departamento "
                + "WHERE UPPER(c.num_documento) LIKE ? OR UPPER(c.apellidos) LIKE ? "
                + "ORDER BY c.apellidos, c.nombres, c.id_cliente LIMIT 100";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            // los % hacen que LIKE busque el texto en cualquier parte; se pasa a mayúsculas para ignorar el tamaño de letra
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

    /**
     * Busca un cliente por su identificador.
     *
     * @param id identificador del cliente.
     * @return el cliente, o {@code null} si no existe.
     * @throws RuntimeException si la consulta falla.
     */
    public Cliente buscar(int id) {
        String sql = "SELECT c.*, ci.nombre AS ciudad, d.nombre AS departamento "
                + "FROM CLIENTE c JOIN CIUDAD ci ON ci.id_ciudad = c.id_ciudad "
                + "JOIN DEPARTAMENTO d ON d.id_departamento = ci.id_departamento "
                + "WHERE c.id_cliente = ?";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                // el identificador es llave primaria: hay a lo sumo una fila, por eso se devuelve en la primera vuelta
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

    /**
     * Lista los teléfonos de un cliente (el teléfono es un atributo multivaluado con su propia tabla).
     *
     * @param id identificador del cliente.
     * @return los teléfonos del cliente, ordenados por tipo y número.
     * @throws RuntimeException si la consulta falla.
     */
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
