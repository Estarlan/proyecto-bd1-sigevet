package co.edu.unbosque.sigevet.dao;

import co.edu.unbosque.sigevet.conexion.ConexionBD;
import co.edu.unbosque.sigevet.modelo.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CitaDAO {
    @Autowired
    private ConexionBD conexionBD;

    public ArrayList<Cita> listar(Integer sede, String estado) {
        ArrayList<Cita> lista = new ArrayList<Cita>();
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

    public ArrayList<Cita> proximas() {
        ArrayList<Cita> lista = new ArrayList<Cita>();
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
}
