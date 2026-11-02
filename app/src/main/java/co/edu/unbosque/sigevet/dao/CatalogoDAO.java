package co.edu.unbosque.sigevet.dao;

import co.edu.unbosque.sigevet.connection.ConexionBD;
import co.edu.unbosque.sigevet.model.OpcionCatalogo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;

/**
 * Lee los catálogos que llenan los {@code <select>} de los formularios. Cada consulta devuelve las columnas
 * {@code id}, {@code nombre}, {@code detalle} y {@code valor} para poder reutilizar el mismo método de lectura.
 */
@Repository
public class CatalogoDAO {
    /** Fábrica de conexiones a la base de datos. */
    private final ConexionBD conexionBD;

    /**
     * @param conexionBD fábrica de conexiones a la base de datos.
     */
    public CatalogoDAO(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /** Ciudades con su departamento (formulario de cliente). */
    public ArrayList<OpcionCatalogo> ciudades() {
        return consultar("SELECT c.id_ciudad AS id, c.nombre, d.nombre AS detalle, NULL AS valor "
                + "FROM CIUDAD c JOIN DEPARTAMENTO d ON d.id_departamento = c.id_departamento "
                + "ORDER BY d.nombre, c.nombre", null);
    }

    /** Razas con su especie (formulario de mascota). */
    public ArrayList<OpcionCatalogo> razas() {
        return consultar("SELECT r.id_raza AS id, r.nombre, e.nombre AS detalle, NULL AS valor "
                + "FROM RAZA r JOIN ESPECIE e ON e.id_especie = r.id_especie "
                + "ORDER BY e.nombre, r.nombre", null);
    }

    /** Servicios con su categoría y precio vigente (citas y facturas). */
    public ArrayList<OpcionCatalogo> servicios() {
        return consultar("SELECT id_servicio AS id, nombre, categoria AS detalle, precio AS valor "
                + "FROM SERVICIO ORDER BY categoria, nombre", null);
    }

    /**
     * Medicamentos con su presentación y precio de venta. Si se indica una sede, solo los que esa sede tiene en
     * inventario, y el detalle incluye las existencias (formulario de factura).
     */
    public ArrayList<OpcionCatalogo> medicamentos(Integer sede) {
        if (sede == null) {
            return consultar("SELECT id_medicamento AS id, nombre, presentacion AS detalle, precio_venta AS valor "
                    + "FROM MEDICAMENTO ORDER BY nombre, presentacion", null);
        }
        return consultar("SELECT m.id_medicamento AS id, m.nombre, "
                + "CONCAT(m.presentacion, ' · ', i.stock_actual, ' disponibles') AS detalle, m.precio_venta AS valor "
                + "FROM MEDICAMENTO m JOIN INVENTARIO i ON i.id_medicamento = m.id_medicamento "
                + "WHERE i.id_sede = ? ORDER BY m.nombre, m.presentacion", sede);
    }

    /** Especialidades médicas (formulario de veterinario). */
    public ArrayList<OpcionCatalogo> especialidades() {
        return consultar("SELECT id_especialidad AS id, nombre, descripcion AS detalle, NULL AS valor "
                + "FROM ESPECIALIDAD ORDER BY nombre", null);
    }

    /** Veterinarios activos que laboran en una sede (formulario de cita: regla RN-08). */
    public ArrayList<OpcionCatalogo> veterinarios(int sede) {
        return consultar("SELECT e.id_empleado AS id, CONCAT(e.nombres, ' ', e.apellidos) AS nombre, "
                + "v.universidad AS detalle, NULL AS valor "
                + "FROM EMPLEADO e JOIN VETERINARIO v ON v.id_empleado = e.id_empleado "
                + "WHERE e.id_sede = ? AND e.estado = 'ACTIVO' ORDER BY e.apellidos, e.nombres", sede);
    }

    /** Empleados activos de una sede (quien emite una factura). */
    public ArrayList<OpcionCatalogo> empleados(int sede) {
        return consultar("SELECT id_empleado AS id, CONCAT(nombres, ' ', apellidos) AS nombre, "
                + "tipo_empleado AS detalle, NULL AS valor "
                + "FROM EMPLEADO WHERE id_sede = ? AND estado = 'ACTIVO' ORDER BY apellidos, nombres", sede);
    }

    /**
     * Ejecuta una consulta de catálogo con un parámetro opcional y convierte cada fila en una opción.
     *
     * @param sql consulta con las columnas id, nombre, detalle y valor.
     * @param parametro valor del único signo de interrogación, o null si la consulta no tiene.
     * @return las opciones leídas.
     */
    private ArrayList<OpcionCatalogo> consultar(String sql, Integer parametro) {
        ArrayList<OpcionCatalogo> lista = new ArrayList<OpcionCatalogo>();
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            if (parametro != null) {
                sentencia.setInt(1, parametro);
            }
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    lista.add(new OpcionCatalogo(rs.getInt("id"), rs.getString("nombre"),
                            rs.getString("detalle"), rs.getBigDecimal("valor")));
                }
            }
            return lista;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo leer el catálogo.", e);
        }
    }
}
