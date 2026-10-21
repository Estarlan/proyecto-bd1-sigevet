package co.edu.unbosque.sigevet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    public static Connection obtener() throws SQLException {
        String url = System.getenv("SIGEVET_DB_URL");
        String usuario = System.getenv("SIGEVET_DB_USER");
        String clave = System.getenv("SIGEVET_DB_PASSWORD");
        if (url == null || url.isBlank()) {
            url = "jdbc:mysql://localhost:3306/sigevet";
        }
        if (usuario == null || usuario.isBlank()) {
            usuario = "vet_app";
        }
        if (clave == null || clave.isBlank()) {
            throw new SQLException("Falta definir SIGEVET_DB_PASSWORD");
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException error) {
            throw new SQLException("No se encontró el controlador de MySQL", error);
        }
        return DriverManager.getConnection(url, usuario, clave);
    }
}
