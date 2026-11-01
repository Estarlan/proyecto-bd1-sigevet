package co.edu.unbosque.sigevet.dao;

import co.edu.unbosque.sigevet.connection.ConexionBD;
import co.edu.unbosque.sigevet.model.Consulta;
import co.edu.unbosque.sigevet.model.Prescripcion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;

/**
 * Acceso a datos de las consultas clínicas y de sus prescripciones. Ejecuta el SQL con JDBC y
 * {@code PreparedStatement}, sin ORM, y convierte cada fila en un objeto {@link Consulta} o {@link Prescripcion}.
 */
@Repository
public class ConsultaDAO {
    /** Fábrica de conexiones a la base de datos. */
    private final ConexionBD conexionBD;

    /**
     * Constructor con inyección de dependencias: Spring entrega la conexión al crear el DAO.
     *
     * @param conexionBD fábrica de conexiones a la base de datos.
     */
    public ConsultaDAO(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Lista las consultas de una mascota, de la más reciente a la más antigua. Cada consulta llega sin
     * prescripciones; el servicio se las asigna después con {@link #prescripciones(int)}.
     *
     * @param id identificador de la mascota.
     * @return las consultas de la mascota.
     * @throws RuntimeException si la consulta falla.
     */
    public ArrayList<Consulta> listarPorMascota(int id) {
        ArrayList<Consulta> lista = new ArrayList<Consulta>();
        // la consulta no guarda la mascota: se llega a ella por la cita (CONSULTA -> CITA -> MASCOTA)
        String sql = "SELECT co.*, ci.fecha_hora, CONCAT(e.nombres, ' ', e.apellidos) AS veterinario, s.nombre AS sede "
                + "FROM CONSULTA co JOIN CITA ci ON ci.id_cita = co.id_cita "
                + "JOIN EMPLEADO e ON e.id_empleado = ci.id_veterinario "
                + "JOIN SEDE s ON s.id_sede = ci.id_sede "
                + "WHERE ci.id_mascota = ? "
                + "ORDER BY co.fecha_hora_inicio DESC, co.id_consulta DESC";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    Consulta objeto = new Consulta();
                    objeto.setIdConsulta(rs.getLong("id_consulta"));
                    objeto.setFechaHoraInicio(rs.getString("fecha_hora_inicio"));
                    objeto.setPesoKg(rs.getBigDecimal("peso_kg"));
                    objeto.setTemperaturaC(rs.getBigDecimal("temperatura_c"));
                    objeto.setFrecuenciaCardiaca(rs.getString("frecuencia_cardiaca"));
                    objeto.setSintomas(rs.getString("sintomas"));
                    objeto.setDiagnostico(rs.getString("diagnostico"));
                    objeto.setObservaciones(rs.getString("observaciones"));
                    objeto.setVeterinario(rs.getString("veterinario"));
                    objeto.setSede(rs.getString("sede"));
                    lista.add(objeto);
                }
            }
            return lista;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudieron consultar las consultas de la mascota.", e);
        }
    }

    /**
     * Lista todas las prescripciones de todas las consultas de una mascota. Cada una trae el identificador de su
     * consulta para que el servicio pueda asignarla.
     *
     * @param id identificador de la mascota.
     * @return las prescripciones de la mascota, agrupadas por consulta.
     * @throws RuntimeException si la consulta falla.
     */
    public ArrayList<Prescripcion> prescripciones(int id) {
        ArrayList<Prescripcion> lista = new ArrayList<Prescripcion>();
        String sql = "SELECT p.*, m.nombre AS medicamento, m.presentacion "
                + "FROM PRESCRIPCION p "
                + "JOIN MEDICAMENTO m ON m.id_medicamento = p.id_medicamento "
                + "JOIN CONSULTA co ON co.id_consulta = p.id_consulta "
                + "JOIN CITA ci ON ci.id_cita = co.id_cita "
                + "WHERE ci.id_mascota = ? "
                + "ORDER BY p.id_consulta, m.nombre, p.id_medicamento";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    Prescripcion objeto = new Prescripcion();
                    objeto.setIdConsulta(rs.getLong("id_consulta"));
                    objeto.setMedicamento(rs.getString("medicamento"));
                    objeto.setPresentacion(rs.getString("presentacion"));
                    objeto.setDosis(rs.getString("dosis"));
                    objeto.setFrecuenciaHoras(rs.getInt("frecuencia_horas"));
                    objeto.setDuracionDias(rs.getInt("duracion_dias"));
                    objeto.setCantidad(rs.getInt("cantidad"));
                    objeto.setIndicaciones(rs.getString("indicaciones"));
                    lista.add(objeto);
                }
            }
            return lista;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudieron consultar las prescripciones de la mascota.", e);
        }
    }
}
