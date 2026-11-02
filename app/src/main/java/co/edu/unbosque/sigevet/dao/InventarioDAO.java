package co.edu.unbosque.sigevet.dao;

import co.edu.unbosque.sigevet.connection.ConexionBD;
import co.edu.unbosque.sigevet.model.ItemInventario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;
import co.edu.unbosque.sigevet.dto.InventarioFormDTO;
import co.edu.unbosque.sigevet.util.ErroresSQL;

/**
 * Acceso a datos del inventario de medicamentos por sede. Ejecuta el SQL con JDBC y {@code PreparedStatement}, sin
 * ORM, y convierte cada fila en un objeto {@link ItemInventario}.
 */
@Repository
public class InventarioDAO {
    /** Fábrica de conexiones a la base de datos. */
    private final ConexionBD conexionBD;

    /**
     * Constructor con inyección de dependencias: Spring entrega la conexión al crear el DAO.
     *
     * @param conexionBD fábrica de conexiones a la base de datos.
     */
    public InventarioDAO(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Lista el inventario, de todas las sedes o de una sola.
     *
     * @param sede identificador de la sede a filtrar, o {@code null} para listar todas.
     * @return las existencias ordenadas por sede, medicamento y presentación.
     * @throws RuntimeException si la consulta falla.
     */
    public ArrayList<ItemInventario> listar(Integer sede) {
        ArrayList<ItemInventario> lista = new ArrayList<ItemInventario>();
        // "(? IS NULL OR i.id_sede = ?)": si el filtro llega nulo, la condición se cumple en todas las filas
        String sql = "SELECT i.id_sede, i.id_medicamento, s.nombre AS sede, m.nombre AS medicamento, m.presentacion, i.stock_actual, i.stock_minimo, i.fecha_actualizacion "
                + "FROM INVENTARIO i JOIN SEDE s ON s.id_sede = i.id_sede "
                + "JOIN MEDICAMENTO m ON m.id_medicamento = i.id_medicamento "
                + "WHERE (? IS NULL OR i.id_sede = ?) "
                + "ORDER BY s.nombre, m.nombre, m.presentacion";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            // el mismo valor se envía a los dos signos de interrogación del filtro
            if (sede == null) {
                sentencia.setNull(1, Types.INTEGER);
                sentencia.setNull(2, Types.INTEGER);
            } else {
                sentencia.setInt(1, sede);
                sentencia.setInt(2, sede);
            }
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    ItemInventario objeto = new ItemInventario();
                    objeto.setIdSede(rs.getInt("id_sede"));
                    objeto.setIdMedicamento(rs.getInt("id_medicamento"));
                    objeto.setSede(rs.getString("sede"));
                    objeto.setMedicamento(rs.getString("medicamento"));
                    objeto.setPresentacion(rs.getString("presentacion"));
                    objeto.setStockActual(rs.getInt("stock_actual"));
                    objeto.setStockMinimo(rs.getInt("stock_minimo"));
                    objeto.setFechaActualizacion(rs.getString("fecha_actualizacion"));
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
     * Lista los medicamentos cuyo stock actual está por debajo del mínimo, en todas las sedes. La página de inicio
     * los muestra como alerta de reposición.
     *
     * @return las existencias con stock bajo, ordenadas por sede y medicamento.
     * @throws RuntimeException si la consulta falla.
     */
    public ArrayList<ItemInventario> stockBajo() {
        ArrayList<ItemInventario> lista = new ArrayList<ItemInventario>();
        String sql = "SELECT i.id_sede,i.id_medicamento,s.nombre AS sede,m.nombre AS medicamento,m.presentacion,i.fecha_actualizacion,i.stock_actual,i.stock_minimo "
                + "FROM INVENTARIO i JOIN SEDE s ON s.id_sede=i.id_sede "
                + "JOIN MEDICAMENTO m ON m.id_medicamento=i.id_medicamento "
                + "WHERE i.stock_actual<i.stock_minimo ORDER BY s.nombre,m.nombre";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    ItemInventario objeto = new ItemInventario();
                    objeto.setIdSede(rs.getInt("id_sede"));
                    objeto.setIdMedicamento(rs.getInt("id_medicamento"));
                    objeto.setSede(rs.getString("sede"));
                    objeto.setMedicamento(rs.getString("medicamento"));
                    objeto.setPresentacion(rs.getString("presentacion"));
                    objeto.setStockActual(rs.getInt("stock_actual"));
                    objeto.setStockMinimo(rs.getInt("stock_minimo"));
                    objeto.setFechaActualizacion(rs.getString("fecha_actualizacion"));
                    lista.add(objeto);
                }
            }
            return lista;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo consultar el stock bajo.", e);
        }
    }

    // ------------------------------------------------------------------
    // Escritura: INVENTARIO resuelve la relación N:M ALMACENAR entre
    // SEDE y MEDICAMENTO; su llave primaria es (id_sede, id_medicamento).
    // ------------------------------------------------------------------

    /**
     * Agrega un medicamento al inventario de una sede. Si ya estaba, la llave primaria lo impide (error 1062).
     *
     * @param form datos ya validados.
     */
    public void insertar(InventarioFormDTO form) {
        String sql = "INSERT INTO INVENTARIO (id_sede, id_medicamento, stock_actual, stock_minimo, fecha_actualizacion) "
                + "VALUES (?, ?, ?, ?, NOW())";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, form.getIdSede());
            sentencia.setInt(2, form.getIdMedicamento());
            sentencia.setInt(3, form.getStockActual());
            sentencia.setInt(4, form.getStockMinimo());
            sentencia.executeUpdate();
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e);
        }
    }

    /**
     * Cambia las existencias y el punto de reorden de un medicamento en una sede (conteo o compra).
     *
     * @return {@code true} si la fila existía.
     */
    public boolean actualizar(int idSede, int idMedicamento, int stockActual, int stockMinimo) {
        String sql = "UPDATE INVENTARIO SET stock_actual = ?, stock_minimo = ?, fecha_actualizacion = NOW() "
                + "WHERE id_sede = ? AND id_medicamento = ?";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, stockActual);
            sentencia.setInt(2, stockMinimo);
            sentencia.setInt(3, idSede);
            sentencia.setInt(4, idMedicamento);
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e);
        }
    }

    /**
     * Quita un medicamento del inventario de una sede (borrado físico: la fila no tiene historial que conservar;
     * las facturas guardan su propio precio y cantidad).
     *
     * @return {@code true} si la fila existía.
     */
    public boolean eliminar(int idSede, int idMedicamento) {
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(
                     "DELETE FROM INVENTARIO WHERE id_sede = ? AND id_medicamento = ?")) {
            sentencia.setInt(1, idSede);
            sentencia.setInt(2, idMedicamento);
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e);
        }
    }
}
