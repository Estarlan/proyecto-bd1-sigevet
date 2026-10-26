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
public class SedeDAO {
    @Autowired
    private ConexionBD conexionBD;

    public ArrayList<Sede> listar() {
        ArrayList<Sede> lista = new ArrayList<Sede>();
        String sql = "SELECT s.id_sede, s.nombre, s.direccion, s.telefono, CONCAT(e.nombres, ' ', e.apellidos) AS director "
                + "FROM SEDE s LEFT JOIN EMPLEADO e ON e.id_empleado = s.id_director "
                + "ORDER BY s.nombre";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            try (ResultSet rs = sentencia.executeQuery()) {
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
