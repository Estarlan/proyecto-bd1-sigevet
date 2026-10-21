package co.edu.unbosque.sigevet;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/agenda")
public class AgendaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        var filtros = Parametros.filtros(request, "sede", "veterinario", "estado", "desde", "hasta");
        Integer sede = Parametros.numero(filtros.get("sede"));
        Integer veterinario = Parametros.numero(filtros.get("veterinario"));
        String estado = Parametros.texto(filtros.get("estado"));
        Timestamp desde = Parametros.fecha(filtros.get("desde"), false);
        Timestamp hasta = Parametros.fecha(filtros.get("hasta"), true);
        try {
            request.setAttribute("filas", BaseDatos.consultar(Consultas.SQL_AGENDA,
                    sede, sede, veterinario, veterinario, estado, estado, desde, desde, hasta, hasta));
            request.setAttribute("opciones", Map.of("sedes", BaseDatos.consultar(Consultas.SQL_SEDES),
                    "veterinarios", BaseDatos.consultar(Consultas.SQL_VETERINARIOS)));
            request.setAttribute("estados", new String[]{"PROGRAMADA", "ATENDIDA", "CANCELADA", "NO_ASISTIO"});
            request.setAttribute("filtros", filtros);
            request.setAttribute("ruta", "/agenda");
            request.getRequestDispatcher("/WEB-INF/jsp/agenda.jsp").forward(request, response);
        } catch (SQLException error) {
            throw new ServletException(error);
        }
    }
}
