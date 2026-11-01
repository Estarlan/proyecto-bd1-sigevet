package co.edu.unbosque.sigevet.dao;

import co.edu.unbosque.sigevet.connection.ConexionBD;
import co.edu.unbosque.sigevet.model.Sede;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;

/**
 * Acceso a datos de las sedes. Ejecuta el SQL con JDBC y {@code PreparedStatement}, sin ORM, y convierte cada fila
 * en un objeto {@link Sede}.
 */
@Repository
public class SedeDAO {
    /** Fábrica de conexiones a la base de datos. */
    private final ConexionBD conexionBD;

    /**
     * Constructor con inyección de dependencias: Spring entrega la conexión al crear el DAO.
     *
     * @param conexionBD fábrica de conexiones a la base de datos.
     */
    public SedeDAO(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Lista todas las sedes ordenadas por nombre, con el nombre completo de su director.
     *
     * @return la lista de sedes.
     * @throws RuntimeException si la consulta falla.
     */
    public ArrayList<Sede> listar() {
        ArrayList<Sede> lista = new ArrayList<Sede>();
        // LEFT JOIN: una sede sin director asignado también aparece en la lista
        String sql = "SELECT s.id_sede, s.nombre, s.direccion, s.telefono, CONCAT(e.nombres, ' ', e.apellidos) AS director "
                + "FROM SEDE s LEFT JOIN EMPLEADO e ON e.id_empleado = s.id_director "
                + "ORDER BY s.nombre";
        // try-with-resources: la conexión y la sentencia se cierran solas al terminar
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            try (ResultSet rs = sentencia.executeQuery()) {
                // cada fila del resultado se convierte en un objeto Sede
                while (rs.next()) {
                    Sede objeto = new Sede();
                    objeto.setIdSede(rs.getInt("id_sede"));
                    objeto.setNombre(rs.getString("nombre"));
                    objeto.setDireccion(rs.getString("direccion"));
                    objeto.setTelefono(rs.getString("telefono"));
                    objeto.setDirector(rs.getString("director"));
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
