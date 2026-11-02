package co.edu.unbosque.sigevet.dao;

import co.edu.unbosque.sigevet.connection.ConexionBD;
import co.edu.unbosque.sigevet.model.Cita;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;
import co.edu.unbosque.sigevet.connection.Transaccion;
import co.edu.unbosque.sigevet.dto.CitaFormDTO;
import co.edu.unbosque.sigevet.exception.ConflictoDatosException;
import co.edu.unbosque.sigevet.util.ErroresSQL;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Acceso a datos de las citas. Ejecuta el SQL con JDBC y {@code PreparedStatement}, sin ORM, y convierte cada fila
 * en un objeto {@link Cita}.
 */
@Repository
public class CitaDAO {
    /** Fábrica de conexiones a la base de datos. */
    private final ConexionBD conexionBD;

    /**
     * Constructor con inyección de dependencias: Spring entrega la conexión al crear el DAO.
     *
     * @param conexionBD fábrica de conexiones a la base de datos.
     */
    public CitaDAO(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Lista las citas con filtros opcionales por sede y por estado.
     *
     * @param sede identificador de la sede, o {@code null} para todas las sedes.
     * @param estado estado de la cita (por ejemplo PROGRAMADA), o {@code null} para todos los estados.
     * @return las citas ordenadas por fecha y hora.
     * @throws RuntimeException si la consulta falla.
     */
    public ArrayList<Cita> listar(Integer sede, String estado) {
        ArrayList<Cita> lista = new ArrayList<Cita>();
        // cada filtro usa "(? IS NULL OR columna = ?)": si el filtro llega nulo, la condición se cumple en todas las filas
        String sql = "SELECT c.id_cita, c.fecha_hora, c.estado, c.id_mascota, c.motivo, m.nombre AS mascota, sv.nombre AS servicio, s.nombre AS sede, CONCAT(e.nombres, ' ', e.apellidos) AS veterinario "
                + "FROM CITA c JOIN MASCOTA m ON m.id_mascota = c.id_mascota "
                + "JOIN SERVICIO sv ON sv.id_servicio = c.id_servicio "
                + "JOIN EMPLEADO e ON e.id_empleado = c.id_veterinario "
                + "JOIN SEDE s ON s.id_sede = c.id_sede "
                + "WHERE (? IS NULL OR c.id_sede = ?) "
                + "AND (? IS NULL OR c.estado = ?) "
                + "ORDER BY c.fecha_hora, c.id_cita";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            // la sede ocupa los parámetros 1 y 2; el estado, los parámetros 3 y 4
            if (sede == null) {
                sentencia.setNull(1, Types.INTEGER);
                sentencia.setNull(2, Types.INTEGER);
            } else {
                sentencia.setInt(1, sede);
                sentencia.setInt(2, sede);
            }
            sentencia.setString(3, estado);
            sentencia.setString(4, estado);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    Cita objeto = new Cita();
                    objeto.setIdCita(rs.getLong("id_cita"));
                    objeto.setIdMascota(rs.getInt("id_mascota"));
                    objeto.setFechaHora(rs.getString("fecha_hora"));
                    objeto.setMotivo(rs.getString("motivo"));
                    objeto.setEstado(rs.getString("estado"));
                    objeto.setMascota(rs.getString("mascota"));
                    objeto.setServicio(rs.getString("servicio"));
                    objeto.setSede(rs.getString("sede"));
                    objeto.setVeterinario(rs.getString("veterinario"));
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
     * Lista las próximas 10 citas programadas a partir de este momento. La página de inicio las muestra.
     *
     * @return hasta 10 citas con estado PROGRAMADA y fecha futura, de la más cercana a la más lejana.
     * @throws RuntimeException si la consulta falla.
     */
    public ArrayList<Cita> proximas() {
        ArrayList<Cita> lista = new ArrayList<Cita>();
        // NOW() es la fecha y hora actual del servidor de base de datos
        String sql = "SELECT c.id_cita,c.motivo,c.fecha_hora,c.estado,m.id_mascota,m.nombre AS mascota, sv.nombre AS servicio,CONCAT(e.nombres, ' ', e.apellidos) AS veterinario,s.nombre AS sede "
                + "FROM CITA c JOIN MASCOTA m ON m.id_mascota=c.id_mascota "
                + "JOIN SERVICIO sv ON sv.id_servicio=c.id_servicio "
                + "JOIN EMPLEADO e ON e.id_empleado=c.id_veterinario "
                + "JOIN SEDE s ON s.id_sede=c.id_sede "
                + "WHERE c.estado='PROGRAMADA' AND c.fecha_hora>=NOW() "
                + "ORDER BY c.fecha_hora,c.id_cita LIMIT 10";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    Cita objeto = new Cita();
                    objeto.setIdCita(rs.getLong("id_cita"));
                    objeto.setIdMascota(rs.getInt("id_mascota"));
                    objeto.setFechaHora(rs.getString("fecha_hora"));
                    objeto.setMotivo(rs.getString("motivo"));
                    objeto.setEstado(rs.getString("estado"));
                    objeto.setMascota(rs.getString("mascota"));
                    objeto.setServicio(rs.getString("servicio"));
                    objeto.setSede(rs.getString("sede"));
                    objeto.setVeterinario(rs.getString("veterinario"));
                    lista.add(objeto);
                }
            }
            return lista;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudieron consultar las próximas citas.", e);
        }
    }

    // ------------------------------------------------------------------
    // Escritura: agendar y cambiar el estado de una cita
    // ------------------------------------------------------------------

    /**
     * Agenda una cita nueva en estado PROGRAMADA. Antes de insertar, el disparador
     * TRG_CITA_VETERINARIO_SEDE_INS revisa la regla RN-08 (el veterinario debe laborar en la sede) y la restricción
     * UQ_CITA_VETERINARIO_FECHA_HORA impide dos citas del mismo veterinario a la misma hora.
     *
     * @param form datos ya validados por el servicio.
     * @param fechaHora fecha y hora ya convertidas.
     * @return el identificador generado.
     */
    public long insertar(CitaFormDTO form, LocalDateTime fechaHora) {
        String sql = "INSERT INTO CITA (fecha_hora, motivo, estado, id_mascota, id_veterinario, id_sede, id_servicio) "
                + "VALUES (?, ?, 'PROGRAMADA', ?, ?, ?, ?)";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setTimestamp(1, Timestamp.valueOf(fechaHora));
            sentencia.setString(2, form.getMotivo());
            sentencia.setInt(3, form.getIdMascota());
            sentencia.setInt(4, form.getIdVeterinario());
            sentencia.setInt(5, form.getIdSede());
            sentencia.setInt(6, form.getIdServicio());
            sentencia.executeUpdate();
            try (ResultSet llaves = sentencia.getGeneratedKeys()) {
                llaves.next();
                return llaves.getLong(1);
            }
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e);
        }
    }

    /**
     * Cambia el estado de una cita PROGRAMADA a ATENDIDA, CANCELADA o NO_ASISTIO.
     *
     * <p>Se lee el estado actual con {@code SELECT ... FOR UPDATE} dentro de la transacción: así la fila queda
     * bloqueada y nadie más la cambia entre la lectura y el UPDATE.</p>
     *
     * @param id identificador de la cita.
     * @param estado nuevo estado.
     * @return {@code false} si la cita no existe.
     * @throws ConflictoDatosException si la cita ya no está PROGRAMADA.
     */
    public boolean cambiarEstado(long id, String estado) {
        Connection conexion = null;
        try {
            conexion = conexionBD.conectar();
            conexion.setAutoCommit(false);
            String actual = null;
            try (PreparedStatement sentencia = conexion.prepareStatement("SELECT estado FROM CITA WHERE id_cita = ? FOR UPDATE")) {
                sentencia.setLong(1, id);
                try (ResultSet rs = sentencia.executeQuery()) {
                    if (rs.next()) {
                        actual = rs.getString("estado");
                    }
                }
            }
            if (actual == null) {
                Transaccion.deshacer(conexion);
                return false;
            }
            if (!"PROGRAMADA".equals(actual)) {
                throw new ConflictoDatosException("La cita ya está " + actual + "; solo se puede cambiar una cita PROGRAMADA.");
            }
            try (PreparedStatement sentencia = conexion.prepareStatement("UPDATE CITA SET estado = ? WHERE id_cita = ?")) {
                sentencia.setString(1, estado);
                sentencia.setLong(2, id);
                sentencia.executeUpdate();
            }
            conexion.commit();
            return true;
        } catch (SQLException e) {
            Transaccion.deshacer(conexion);
            throw ErroresSQL.traducir(e);
        } catch (RuntimeException e) {
            Transaccion.deshacer(conexion);
            throw e;
        } finally {
            Transaccion.cerrar(conexion);
        }
    }

    /**
     * Cuenta las citas de un veterinario (todas o solo las programadas). La usa el servicio de personal: un
     * veterinario con citas no puede cambiar de sede, porque el disparador TRG_CITA_UPD vuelve a revisar la regla
     * RN-08 cada vez que se modifica una de sus citas.
     *
     * @param idVeterinario identificador del veterinario.
     * @param soloProgramadas {@code true} para contar solo las PROGRAMADAS.
     * @return cantidad de citas.
     */
    public int contarPorVeterinario(int idVeterinario, boolean soloProgramadas) {
        String sql = "SELECT COUNT(*) AS total FROM CITA WHERE id_veterinario = ?"
                + (soloProgramadas ? " AND estado = 'PROGRAMADA'" : "");
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idVeterinario);
            try (ResultSet rs = sentencia.executeQuery()) {
                rs.next();
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e);
        }
    }
}
