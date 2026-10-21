package co.edu.unbosque.sigevet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BaseDatos {
    public static List<Map<String, Object>> consultar(String sql, Object... parametros) throws SQLException {
        try (Connection conexion = Conexion.obtener();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            for (int i = 0; i < parametros.length; i++) {
                Object valor = parametros[i];
                if (valor instanceof LocalDateTime fecha) {
                    valor = Timestamp.valueOf(fecha);
                } else if (valor instanceof LocalDate fecha) {
                    valor = java.sql.Date.valueOf(fecha);
                }
                sentencia.setObject(i + 1, valor);
            }
            try (ResultSet resultado = sentencia.executeQuery()) {
                ResultSetMetaData columnas = resultado.getMetaData();
                List<Map<String, Object>> filas = new ArrayList<>();
                while (resultado.next()) {
                    Map<String, Object> fila = new LinkedHashMap<>();
                    for (int i = 1; i <= columnas.getColumnCount(); i++) {
                        Object valor = resultado.getObject(i);
                        if (valor instanceof Timestamp fecha) {
                            valor = fecha.toLocalDateTime();
                        } else if (valor instanceof java.sql.Date fecha) {
                            valor = fecha.toLocalDate();
                        }
                        fila.put(columnas.getColumnLabel(i).toLowerCase(Locale.ROOT), valor);
                    }
                    filas.add(fila);
                }
                return filas;
            }
        }
    }

    public static Map<String, Object> consultarUno(String sql, Object... parametros) throws SQLException {
        List<Map<String, Object>> filas = consultar(sql, parametros);
        return filas.isEmpty() ? null : filas.get(0);
    }
}
