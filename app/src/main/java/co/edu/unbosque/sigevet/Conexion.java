package co.edu.unbosque.sigevet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {
    public static Connection obtener() throws SQLException {
        String url = System.getenv("SIGEVET_DB_URL");
        String usuario = System.getenv("SIGEVET_DB_USER");
        String clave = System.getenv("SIGEVET_DB_PASSWORD");
        if (url == null || url.isBlank()) {
            url = "jdbc:oracle:thin:@//localhost:1521/FREEPDB1";
        }
        if (usuario == null || usuario.isBlank()) {
            usuario = "VET_APP";
        }
        if (clave == null || clave.isBlank()) {
            throw new SQLException("Falta definir SIGEVET_DB_PASSWORD");
        }
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException error) {
            throw new SQLException("No se encontró el controlador de Oracle", error);
        }
        Connection conexion = DriverManager.getConnection(url, usuario, clave);
        // VET_APP no es dueño de las tablas, por eso se cambia al esquema VETDB
        Statement sentencia = conexion.createStatement();
        sentencia.execute("ALTER SESSION SET CURRENT_SCHEMA = VETDB");
        sentencia.close();
        return conexion;
    }
}
