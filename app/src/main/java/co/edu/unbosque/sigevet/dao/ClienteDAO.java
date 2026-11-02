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
import co.edu.unbosque.sigevet.connection.Transaccion;
import co.edu.unbosque.sigevet.dto.ClienteFormDTO;
import co.edu.unbosque.sigevet.dto.TelefonoFormDTO;
import co.edu.unbosque.sigevet.util.ErroresSQL;
import java.sql.Statement;

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

    // ------------------------------------------------------------------
    // Escritura (CRUD). Cada método usa una transacción manual porque
    // el cliente y sus teléfonos (atributo multivaluado) se guardan juntos.
    // ------------------------------------------------------------------

    /**
     * Lee los datos de un cliente con los identificadores que necesita el formulario de edición.
     *
     * @param id identificador del cliente.
     * @return el formulario lleno, o {@code null} si el cliente no existe.
     */
    public ClienteFormDTO formulario(int id) {
        String sql = "SELECT tipo_documento, num_documento, nombres, apellidos, email, direccion, id_ciudad "
                + "FROM CLIENTE WHERE id_cliente = ?";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            ClienteFormDTO form = null;
            try (ResultSet rs = sentencia.executeQuery()) {
                if (rs.next()) {
                    form = new ClienteFormDTO();
                    form.setTipoDocumento(rs.getString("tipo_documento"));
                    form.setNumDocumento(rs.getString("num_documento"));
                    form.setNombres(rs.getString("nombres"));
                    form.setApellidos(rs.getString("apellidos"));
                    form.setEmail(rs.getString("email"));
                    form.setDireccion(rs.getString("direccion"));
                    form.setIdCiudad(rs.getInt("id_ciudad"));
                }
            }
            if (form != null) {
                for (Telefono telefono : telefonos(id)) {
                    TelefonoFormDTO t = new TelefonoFormDTO();
                    t.setTelefono(telefono.getTelefono());
                    t.setTipo(telefono.getTipo());
                    form.getTelefonos().add(t);
                }
            }
            return form;
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e);
        }
    }

    /**
     * Crea un cliente y sus teléfonos en una sola transacción.
     *
     * @param form datos ya validados por el servicio.
     * @return el identificador generado por AUTO_INCREMENT.
     */
    public long insertar(ClienteFormDTO form) {
        String sql = "INSERT INTO CLIENTE (tipo_documento, num_documento, nombres, apellidos, email, direccion, id_ciudad) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conexion = null;
        try {
            conexion = conexionBD.conectar();
            conexion.setAutoCommit(false);
            long id;
            // RETURN_GENERATED_KEYS: JDBC devuelve el id_cliente que asignó AUTO_INCREMENT
            try (PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                llenarCliente(sentencia, form);
                sentencia.executeUpdate();
                try (ResultSet llaves = sentencia.getGeneratedKeys()) {
                    llaves.next();
                    id = llaves.getLong(1);
                }
            }
            insertarTelefonos(conexion, (int) id, form);
            conexion.commit();
            return id;
        } catch (SQLException e) {
            Transaccion.deshacer(conexion);
            throw ErroresSQL.traducir(e);
        } finally {
            Transaccion.cerrar(conexion);
        }
    }

    /**
     * Actualiza un cliente y reemplaza sus teléfonos (se borran los anteriores y se insertan los del formulario),
     * todo en una transacción.
     *
     * @param id identificador del cliente.
     * @param form datos ya validados por el servicio.
     * @return {@code true} si el cliente existía.
     */
    public boolean actualizar(int id, ClienteFormDTO form) {
        String sql = "UPDATE CLIENTE SET tipo_documento = ?, num_documento = ?, nombres = ?, apellidos = ?, "
                + "email = ?, direccion = ?, id_ciudad = ? WHERE id_cliente = ?";
        Connection conexion = null;
        try {
            conexion = conexionBD.conectar();
            conexion.setAutoCommit(false);
            int filas;
            try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                llenarCliente(sentencia, form);
                sentencia.setInt(8, id);
                filas = sentencia.executeUpdate();
            }
            if (filas == 0) {
                Transaccion.deshacer(conexion);
                return false;
            }
            try (PreparedStatement borrar = conexion.prepareStatement("DELETE FROM TELEFONO_CLIENTE WHERE id_cliente = ?")) {
                borrar.setInt(1, id);
                borrar.executeUpdate();
            }
            insertarTelefonos(conexion, id, form);
            conexion.commit();
            return true;
        } catch (SQLException e) {
            Transaccion.deshacer(conexion);
            throw ErroresSQL.traducir(e);
        } finally {
            Transaccion.cerrar(conexion);
        }
    }

    /**
     * Cuenta las mascotas y facturas de un cliente. El servicio solo permite borrar clientes sin historial.
     *
     * @param id identificador del cliente.
     * @return arreglo {mascotas, facturas}.
     */
    public int[] historial(int id) {
        String sql = "SELECT (SELECT COUNT(*) FROM MASCOTA WHERE id_cliente = ?) AS mascotas, "
                + "(SELECT COUNT(*) FROM FACTURA WHERE id_cliente = ?) AS facturas";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            sentencia.setInt(2, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                rs.next();
                return new int[] {rs.getInt("mascotas"), rs.getInt("facturas")};
            }
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e);
        }
    }

    /**
     * Borra un cliente sin historial. Sus teléfonos se borran solos por {@code ON DELETE CASCADE}
     * (FK_TELEFONO_CLIENTE_CLIENTE). Si tuviera mascotas o facturas, la FK lo impediría (error 1451).
     *
     * @param id identificador del cliente.
     * @return {@code true} si se borró.
     */
    public boolean eliminar(int id) {
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement("DELETE FROM CLIENTE WHERE id_cliente = ?")) {
            sentencia.setInt(1, id);
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e);
        }
    }

    /** Pone los 7 datos del cliente en los primeros signos de interrogación del INSERT o del UPDATE. */
    private void llenarCliente(PreparedStatement sentencia, ClienteFormDTO form) throws SQLException {
        sentencia.setString(1, form.getTipoDocumento());
        sentencia.setString(2, form.getNumDocumento());
        sentencia.setString(3, form.getNombres());
        sentencia.setString(4, form.getApellidos());
        sentencia.setString(5, form.getEmail());
        sentencia.setString(6, form.getDireccion());
        sentencia.setInt(7, form.getIdCiudad());
    }

    /** Inserta los teléfonos del formulario dentro de la transacción abierta (un lote de INSERT). */
    private void insertarTelefonos(Connection conexion, int idCliente, ClienteFormDTO form) throws SQLException {
        String sql = "INSERT INTO TELEFONO_CLIENTE (id_cliente, telefono, tipo) VALUES (?, ?, ?)";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            for (TelefonoFormDTO telefono : form.getTelefonos()) {
                sentencia.setInt(1, idCliente);
                sentencia.setString(2, telefono.getTelefono());
                sentencia.setString(3, telefono.getTipo());
                sentencia.addBatch();
            }
            sentencia.executeBatch();
        }
    }
}
