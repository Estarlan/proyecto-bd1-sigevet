package co.edu.unbosque.sigevet.dao;

import co.edu.unbosque.sigevet.connection.ConexionBD;
import co.edu.unbosque.sigevet.model.Resumen;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;

/**
 * Acceso a datos de los resúmenes que muestra la página de inicio (conteos, ingresos y citas por estado). Ejecuta
 * el SQL con JDBC y {@code PreparedStatement}, sin ORM, y convierte cada fila en un objeto {@link Resumen}.
 */
@Repository
public class InicioDAO {
    /** Fábrica de conexiones a la base de datos. */
    private final ConexionBD conexionBD;

    /**
     * Constructor con inyección de dependencias: Spring entrega la conexión al crear el DAO.
     *
     * @param conexionBD fábrica de conexiones a la base de datos.
     */
    public InicioDAO(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Cuenta los registros de cada una de las 22 tablas del modelo.
     *
     * @return un {@link Resumen} por tabla, con el nombre de la tabla y su cantidad de registros.
     * @throws RuntimeException si la consulta falla.
     */
    public ArrayList<Resumen> conteos() {
        ArrayList<Resumen> lista = new ArrayList<Resumen>();
        // UNION ALL junta un COUNT por cada tabla en un solo resultado de dos columnas: tabla y total
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
            throw new RuntimeException("No se pudieron contar los registros de las tablas.", e);
        }
    }

    /**
     * Calcula los ingresos por sede: la suma de las líneas de las facturas pagadas.
     *
     * @return un {@link Resumen} por sede, con su nombre y su total de ingresos (cero si no tiene facturas pagadas).
     * @throws RuntimeException si la consulta falla.
     */
    public ArrayList<Resumen> ingresos() {
        ArrayList<Resumen> lista = new ArrayList<Resumen>();
        // LEFT JOIN: una sede sin facturas pagadas también aparece, y IFNULL convierte su suma nula en 0
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
            throw new RuntimeException("No se pudieron consultar los ingresos por sede.", e);
        }
    }

    /**
     * Cuenta las citas agrupadas por estado.
     *
     * @return un {@link Resumen} por estado, con el nombre del estado y la cantidad de citas.
     * @throws RuntimeException si la consulta falla.
     */
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
            throw new RuntimeException("No se pudieron contar las citas por estado.", e);
        }
    }
}
