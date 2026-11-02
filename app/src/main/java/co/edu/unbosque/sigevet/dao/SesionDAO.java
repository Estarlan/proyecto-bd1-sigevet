package co.edu.unbosque.sigevet.dao;

import co.edu.unbosque.sigevet.connection.ConexionBD;
import co.edu.unbosque.sigevet.dto.SesionDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Repository;

/**
 * Acceso a datos del inicio de sesión. Busca en EMPLEADO al empleado activo que tiene ese correo y ese documento.
 * No hay tabla de usuarios: el personal de la clínica entra con los datos que ya están en la base.
 */
@Repository
public class SesionDAO {
    /** Fábrica de conexiones a la base de datos. */
    private final ConexionBD conexionBD;

    /**
     * Constructor con inyección de dependencias: Spring entrega la conexión al crear el DAO.
     *
     * @param conexionBD fábrica de conexiones a la base de datos.
     */
    public SesionDAO(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Busca al empleado activo con ese correo y ese número de documento.
     *
     * @param email correo corporativo (único en EMPLEADO).
     * @param documento número de documento.
     * @return los datos de la sesión, o {@code null} si no coincide o el empleado está inactivo.
     * @throws RuntimeException si la consulta falla.
     */
    public SesionDTO buscarEmpleadoActivo(String email, String documento) {
        // los dos valores van como parámetros ?: el texto del formulario nunca se pega dentro del SQL
        String sql = "SELECT e.id_empleado, e.nombres, e.apellidos, e.tipo_empleado, s.nombre AS sede "
                + "FROM EMPLEADO e JOIN SEDE s ON s.id_sede = e.id_sede "
                + "WHERE e.email = ? AND e.num_documento = ? AND e.estado = 'ACTIVO'";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, email);
            sentencia.setString(2, documento);
            try (ResultSet rs = sentencia.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                SesionDTO sesion = new SesionDTO();
                sesion.setIdEmpleado(rs.getInt("id_empleado"));
                sesion.setNombreCompleto(rs.getString("nombres") + " " + rs.getString("apellidos"));
                String tipo = rs.getString("tipo_empleado");
                if ("VET".equals(tipo)) {
                    sesion.setCargo("Veterinario");
                } else if ("AUX".equals(tipo)) {
                    sesion.setCargo("Auxiliar");
                } else {
                    sesion.setCargo("Administrativo");
                }
                sesion.setSede(rs.getString("sede"));
                return sesion;
            }
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo validar el inicio de sesión.", e);
        }
    }
}
