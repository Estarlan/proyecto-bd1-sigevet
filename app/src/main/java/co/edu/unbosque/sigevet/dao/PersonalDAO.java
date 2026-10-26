package co.edu.unbosque.sigevet.dao;

import co.edu.unbosque.sigevet.conexion.ConexionBD;
import co.edu.unbosque.sigevet.modelo.Auxiliar;
import co.edu.unbosque.sigevet.modelo.Empleado;
import co.edu.unbosque.sigevet.modelo.Veterinario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PersonalDAO {
    @Autowired
    private ConexionBD conexionBD;

    public ArrayList<Empleado> listar(Integer sede) {
        ArrayList<Empleado> empleados = new ArrayList<Empleado>();
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
            cargarEspecialidades(conexion, empleados, sede);
            return empleados;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo consultar el personal.", e);
        }
    }

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
