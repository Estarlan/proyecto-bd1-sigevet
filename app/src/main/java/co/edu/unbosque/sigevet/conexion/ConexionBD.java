package co.edu.unbosque.sigevet.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConexionBD {
    @Value("${sigevet.db.url}")
    private String url;

    @Value("${sigevet.db.usuario}")
    private String usuario;

    @Value("${sigevet.db.clave}")
    private String clave;

    public Connection conectar() throws SQLException {
        return DriverManager.getConnection(url, usuario, clave);
    }
}
