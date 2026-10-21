package co.edu.unbosque.sigevet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/facturas")
public class FacturasServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, String> filtros = Parametros.filtros(request, "sede", "estado");
        Integer sede = Parametros.numero(filtros.get("sede"));
        String estado = Parametros.texto(filtros.get("estado"));
        try {
            request.setAttribute("filas", BaseDatos.consultar(Consultas.SQL_FACTURAS, sede, sede, estado, estado));
            request.setAttribute("opciones", Map.of("sedes", BaseDatos.consultar(Consultas.SQL_SEDES)));
            request.setAttribute("estados", new String[]{"PAGADA", "PENDIENTE", "ANULADA"});
            request.setAttribute("filtros", filtros);
            request.setAttribute("ruta", "/facturas");
            request.getRequestDispatcher("/WEB-INF/jsp/facturas.jsp").forward(request, response);
        } catch (SQLException error) {
            throw new ServletException(error);
        }
    }
}
