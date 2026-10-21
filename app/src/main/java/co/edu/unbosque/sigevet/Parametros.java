package co.edu.unbosque.sigevet;

import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;

public class Parametros {
    public static Integer numero(String valor) {
        try {
            int numero = Integer.parseInt(valor);
            return numero > 0 ? numero : null;
        } catch (NumberFormatException error) {
            return null;
        }
    }

    public static Long id(String ruta) {
        if (ruta == null || !ruta.matches("/[0-9]+")) return null;
        try {
            long id = Long.parseLong(ruta.substring(1));
            return id > 0 ? id : null;
        } catch (NumberFormatException error) {
            return null;
        }
    }

    public static int pagina(String valor) {
        Integer pagina = numero(valor);
        return pagina == null ? 1 : pagina;
    }

    public static String buscar(String valor) {
        String buscar = valor == null ? "" : valor.strip();
        return buscar.substring(0, Math.min(buscar.length(), 100));
    }

    public static String texto(String valor) {
        return valor == null || valor.isEmpty() ? null : valor;
    }

    public static Timestamp fecha(String valor, boolean siguiente) {
        if (valor == null) return null;
        try {
            return Timestamp.valueOf(LocalDate.parse(valor).plusDays(siguiente ? 1 : 0).atStartOfDay());
        } catch (DateTimeException error) {
            return null;
        }
    }

    public static Map<String, String> filtros(HttpServletRequest request, String... nombres) {
        Map<String, String> filtros = new LinkedHashMap<>();
        for (String nombre : nombres) {
            String valor = request.getParameter(nombre);
            filtros.put(nombre, valor == null ? "" : valor);
        }
        return filtros;
    }
}
