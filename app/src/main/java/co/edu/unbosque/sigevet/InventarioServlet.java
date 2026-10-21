package co.edu.unbosque.sigevet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/inventario")
public class InventarioServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        var filtros = Parametros.filtros(request, "sede");
        Integer sede = Parametros.numero(filtros.get("sede"));
        try {
            request.setAttribute("filas", BaseDatos.consultar(Consultas.SQL_INVENTARIO, sede, sede));
            request.setAttribute("opciones", Map.of("sedes", BaseDatos.consultar(Consultas.SQL_SEDES)));
            request.setAttribute("filtros", filtros);
            request.setAttribute("ruta", "/inventario");
            request.getRequestDispatcher("/WEB-INF/jsp/inventario.jsp").forward(request, response);
        } catch (SQLException error) {
            throw new ServletException(error);
        }
    }
}
