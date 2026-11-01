package co.edu.unbosque.sigevet.dao;

import co.edu.unbosque.sigevet.connection.ConexionBD;
import co.edu.unbosque.sigevet.model.ResultadoConsulta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;

/**
 * Ejecuta las consultas SQL de los ejercicios de álgebra relacional y devuelve el resultado en una forma genérica
 * (columnas y filas de texto), porque cada ejercicio devuelve columnas distintas.
 *
 * <p>Solo recibe consultas {@code SELECT} ya validadas por
 * {@link co.edu.unbosque.sigevet.algebra.LectorAlgebra}; además la aplicación se conecta con un usuario de solo
 * lectura, por lo que no puede modificar datos.</p>
 */
@Repository
public class AlgebraDAO {
    /** Fábrica de conexiones a la base de datos. */
    private final ConexionBD conexionBD;

    /**
     * Constructor con inyección de dependencias: Spring entrega la conexión al crear el DAO.
     *
     * @param conexionBD fábrica de conexiones a la base de datos.
     */
    public AlgebraDAO(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Ejecuta una consulta {@code SELECT} y guarda sus columnas y filas.
     *
     * @param sql consulta SELECT del ejercicio.
     * @return el resultado con los nombres de las columnas y las filas como texto; los valores nulos se muestran
     *         como "NULL".
     * @throws RuntimeException si la consulta falla.
     */
    public ResultadoConsulta ejecutar(String sql) {
        ResultadoConsulta resultado = new ResultadoConsulta();
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet rs = sentencia.executeQuery()) {
            // los metadatos dicen cuántas columnas hay y cómo se llaman, porque no se conocen de antemano
            ResultSetMetaData metadatos = rs.getMetaData();
            int cantidadColumnas = metadatos.getColumnCount();
            for (int columna = 1; columna <= cantidadColumnas; columna++) {
                resultado.getColumnas().add(metadatos.getColumnLabel(columna));
            }
            // cada fila se guarda como lista de textos, en el mismo orden que las columnas
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
