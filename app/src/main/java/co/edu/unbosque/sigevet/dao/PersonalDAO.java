package co.edu.unbosque.sigevet.dao;

import co.edu.unbosque.sigevet.connection.ConexionBD;
import co.edu.unbosque.sigevet.model.Auxiliar;
import co.edu.unbosque.sigevet.model.Empleado;
import co.edu.unbosque.sigevet.model.Veterinario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;

/**
 * Acceso a datos del personal de las sedes. Ejecuta el SQL con JDBC y {@code PreparedStatement}, sin ORM.
 *
 * <p>Aquí se ve la herencia y el polimorfismo del modelo: según el tipo de empleado, cada fila se convierte en un
 * {@link Veterinario}, un {@link Auxiliar} o un {@link Empleado} simple, y todos se guardan en la misma lista.</p>
 */
@Repository
public class PersonalDAO {
    /** Fábrica de conexiones a la base de datos. */
    private final ConexionBD conexionBD;

    /**
     * Constructor con inyección de dependencias: Spring entrega la conexión al crear el DAO.
     *
     * @param conexionBD fábrica de conexiones a la base de datos.
     */
    public PersonalDAO(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Lista el personal de todas las sedes o de una sola, con los datos propios de cada tipo de empleado.
     *
     * @param sede identificador de la sede a filtrar, o {@code null} para listar todas.
     * @return los empleados ordenados por sede, apellidos y nombres.
     * @throws RuntimeException si la consulta falla.
     */
    public ArrayList<Empleado> listar(Integer sede) {
        ArrayList<Empleado> empleados = new ArrayList<Empleado>();
        // LEFT JOIN con VETERINARIO y AUXILIAR: cada empleado trae los datos de su subtipo y los demás quedan nulos
        String sql = "SELECT e.*, s.nombre AS sede, v.tarjeta_profesional, v.universidad, a.area, a.turno "
                + "FROM EMPLEADO e JOIN SEDE s ON s.id_sede = e.id_sede "
                + "LEFT JOIN VETERINARIO v ON v.id_empleado = e.id_empleado "
                + "LEFT JOIN AUXILIAR a ON a.id_empleado = e.id_empleado "
                + "WHERE (? IS NULL OR e.id_sede = ?) "
                + "ORDER BY s.nombre, e.apellidos, e.nombres, e.id_empleado";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            if (sede == null) {
                sentencia.setNull(1, Types.INTEGER);
                sentencia.setNull(2, Types.INTEGER);
            } else {
                sentencia.setInt(1, sede);
                sentencia.setInt(2, sede);
            }
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    // se crea el objeto del subtipo correcto (polimorfismo) y se guarda como Empleado
                    Empleado empleado;
                    String tipoEmpleado = rs.getString("tipo_empleado");
                    if ("VET".equals(tipoEmpleado)) {
                        Veterinario veterinario = new Veterinario();
                        veterinario.setTarjetaProfesional(rs.getString("tarjeta_profesional"));
                        veterinario.setUniversidad(rs.getString("universidad"));
                        empleado = veterinario;
                    } else if ("AUX".equals(tipoEmpleado)) {
                        Auxiliar auxiliar = new Auxiliar();
                        auxiliar.setArea(rs.getString("area"));
                        auxiliar.setTurno(rs.getString("turno"));
                        empleado = auxiliar;
                    } else {
                        empleado = new Empleado();
                    }
                    // datos comunes a todos los empleados
                    empleado.setIdPersona(rs.getInt("id_empleado"));
                    empleado.setTipoDocumento(rs.getString("tipo_documento"));
                    empleado.setNumDocumento(rs.getString("num_documento"));
                    empleado.setNombres(rs.getString("nombres"));
                    empleado.setApellidos(rs.getString("apellidos"));
                    empleado.setEmail(rs.getString("email"));
                    empleado.setSede(rs.getString("sede"));
                    empleado.setTipoEmpleado(tipoEmpleado);
                    empleado.setFechaIngreso(rs.getString("fecha_ingreso"));
                    empleado.setSalario(rs.getBigDecimal("salario"));
                    empleado.setEstado(rs.getString("estado"));
                    empleado.setTelefono(rs.getString("telefono"));
                    empleados.add(empleado);
                }
            }
            // las especialidades se leen aparte porque un veterinario puede tener varias (relación muchos a muchos)
            cargarEspecialidades(conexion, empleados, sede);
            return empleados;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo consultar el personal.", e);
        }
    }

    /**
     * Lee las especialidades de los veterinarios y las agrega a cada uno en la lista de empleados.
     * Reutiliza la misma conexión de {@link #listar(Integer)} para no abrir otra.
     *
     * @param conexion conexión ya abierta.
     * @param empleados empleados ya leídos; los veterinarios reciben sus especialidades.
     * @param sede identificador de la sede filtrada, o {@code null} si se listan todas.
     * @throws SQLException si la consulta falla.
     */
    private void cargarEspecialidades(Connection conexion, ArrayList<Empleado> empleados, Integer sede)
            throws SQLException {
        String sql = "SELECT ve.id_empleado, es.nombre FROM VETERINARIO_ESPECIALIDAD ve "
                + "JOIN ESPECIALIDAD es ON es.id_especialidad = ve.id_especialidad "
                + "JOIN EMPLEADO e ON e.id_empleado = ve.id_empleado "
                + "WHERE (? IS NULL OR e.id_sede = ?) ORDER BY ve.id_empleado, es.nombre";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            if (sede == null) {
                sentencia.setNull(1, Types.INTEGER);
                sentencia.setNull(2, Types.INTEGER);
            } else {
                sentencia.setInt(1, sede);
                sentencia.setInt(2, sede);
            }
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    int idEmpleado = rs.getInt("id_empleado");
                    String nombre = rs.getString("nombre");
                    // se busca al veterinario dueño de la especialidad; instanceof comprueba que sea Veterinario antes del cast
                    for (int i = 0; i < empleados.size(); i++) {
                        Empleado empleado = empleados.get(i);
                        if (empleado instanceof Veterinario && empleado.getIdPersona() == idEmpleado) {
                            Veterinario veterinario = (Veterinario) empleado;
                            veterinario.getEspecialidades().add(nombre);
                        }
                    }
                }
            }
        }
    }
}
