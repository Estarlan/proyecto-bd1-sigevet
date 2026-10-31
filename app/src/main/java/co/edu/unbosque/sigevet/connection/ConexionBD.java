package co.edu.unbosque.sigevet.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Abre las conexiones JDBC con la base de datos MySQL {@code sigevet}.
 *
 * <p>No usa ORM ni un pool de conexiones: cada DAO pide una conexión nueva con {@link #conectar()} y la cierra al
 * terminar su consulta (con try-with-resources). Los datos de conexión se leen de {@code application.properties},
 * que a su vez los toma de variables de entorno, de modo que la clave nunca queda escrita en el código ni en
 * el repositorio.</p>
 */
@Component
public class ConexionBD {
    /** Dirección JDBC de la base de datos (propiedad {@code sigevet.db.url}). */
    @Value("${sigevet.db.url}")
    private String url;

    /** Usuario de MySQL con el que se conecta la aplicación (propiedad {@code sigevet.db.usuario}). */
    @Value("${sigevet.db.usuario}")
    private String usuario;

    /** Clave del usuario de MySQL (propiedad {@code sigevet.db.clave}, que viene de una variable de entorno). */
    @Value("${sigevet.db.clave}")
    private String clave;

    /**
     * Abre una conexión nueva con la base de datos. Quien la pide es responsable de cerrarla.
     *
     * @return una conexión JDBC lista para ejecutar sentencias.
     * @throws SQLException si la base de datos no responde o las credenciales son incorrectas.
     */
    public Connection conectar() throws SQLException {
        return DriverManager.getConnection(url, usuario, clave);
    }
}
