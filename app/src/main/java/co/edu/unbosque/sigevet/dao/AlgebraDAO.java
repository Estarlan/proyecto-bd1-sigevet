package co.edu.unbosque.sigevet.dao;

import co.edu.unbosque.sigevet.conexion.ConexionBD;
import co.edu.unbosque.sigevet.modelo.ResultadoConsulta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AlgebraDAO {
    @Autowired
    private ConexionBD conexionBD;

    public ResultadoConsulta ejecutar(String sql) {
        ResultadoConsulta resultado = new ResultadoConsulta();
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet rs = sentencia.executeQuery()) {
            ResultSetMetaData metadatos = rs.getMetaData();
            int cantidadColumnas = metadatos.getColumnCount();
            for (int columna = 1; columna <= cantidadColumnas; columna++) {
                resultado.getColumnas().add(metadatos.getColumnLabel(columna));
            }
            while (rs.next()) {
                ArrayList<String> fila = new ArrayList<String>();
                for (int columna = 1; columna <= cantidadColumnas; columna++) {
                    String valor = rs.getString(columna);
                    if (valor == null) {
                        valor = "NULL";
                    }
                    fila.add(valor);
                }
                resultado.getFilas().add(fila);
            }
            return resultado;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo ejecutar el ejercicio de álgebra.", e);
        }
    }
}
