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
public class MascotaDAO {
    @Autowired
    private ConexionBD conexionBD;

    public Mascota buscar(int id) {
        String sql = "SELECT m.*, r.nombre AS raza, e.nombre AS especie, CONCAT(c.nombres, ' ', c.apellidos) AS cliente "
                + "FROM MASCOTA m JOIN RAZA r ON r.id_raza = m.id_raza "
                + "JOIN ESPECIE e ON e.id_especie = r.id_especie "
                + "JOIN CLIENTE c ON c.id_cliente = m.id_cliente "
                + "WHERE m.id_mascota = ?";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    Mascota objeto = new Mascota();
                    objeto.setIdMascota(rs.getInt("id_mascota"));
                    objeto.setIdCliente(rs.getInt("id_cliente"));
                    objeto.setNombre(rs.getString("nombre"));
                    objeto.setFechaNacimiento(rs.getString("fecha_nacimiento"));
                    objeto.setSexo(rs.getString("sexo"));
                    objeto.setColor(rs.getString("color"));
                    objeto.setMicrochip(rs.getString("microchip"));
                    objeto.setEsterilizado(rs.getString("esterilizado"));
                    objeto.setEstado(rs.getString("estado"));
                    objeto.setRaza(rs.getString("raza"));
                    objeto.setEspecie(rs.getString("especie"));
                    objeto.setCliente(rs.getString("cliente"));
                    return objeto;
                }
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo buscar los datos.", e);
        }
    }

    public ArrayList<Mascota> listarPorCliente(int id) {
        ArrayList<Mascota> lista = new ArrayList<Mascota>();
        String sql = "SELECT m.*, r.nombre AS raza, e.nombre AS especie, CONCAT(c.nombres, ' ', c.apellidos) AS cliente "
                + "FROM MASCOTA m JOIN RAZA r ON r.id_raza = m.id_raza "
                + "JOIN ESPECIE e ON e.id_especie = r.id_especie "
                + "JOIN CLIENTE c ON c.id_cliente = m.id_cliente "
                + "WHERE m.id_cliente = ? ORDER BY m.nombre, m.id_mascota";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    Mascota objeto = new Mascota();
                    objeto.setIdMascota(rs.getInt("id_mascota"));
                    objeto.setIdCliente(rs.getInt("id_cliente"));
                    objeto.setNombre(rs.getString("nombre"));
                    objeto.setFechaNacimiento(rs.getString("fecha_nacimiento"));
                    objeto.setSexo(rs.getString("sexo"));
                    objeto.setColor(rs.getString("color"));
                    objeto.setMicrochip(rs.getString("microchip"));
                    objeto.setEsterilizado(rs.getString("esterilizado"));
                    objeto.setEstado(rs.getString("estado"));
                    objeto.setRaza(rs.getString("raza"));
                    objeto.setEspecie(rs.getString("especie"));
                    objeto.setCliente(rs.getString("cliente"));
                    lista.add(objeto);
                }
            }
            return lista;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudieron consultar las mascotas del cliente.", e);
        }
    }

    public HistoriaClinica historia(int id) {
        String sql = "SELECT * FROM HISTORIA_CLINICA WHERE id_mascota = ?";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    HistoriaClinica objeto = new HistoriaClinica();
                    objeto.setIdHistoria(rs.getInt("id_historia"));
                    objeto.setNumeroHistoria(rs.getString("numero_historia"));
                    objeto.setFechaApertura(rs.getString("fecha_apertura"));
                    objeto.setGrupoSanguineo(rs.getString("grupo_sanguineo"));
                    objeto.setAlergias(rs.getString("alergias"));
                    objeto.setEnfermedadesCronicas(rs.getString("enfermedades_cronicas"));
                    objeto.setObservaciones(rs.getString("observaciones"));
                    return objeto;
                }
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo consultar la historia clínica.", e);
        }
    }
}
