package co.edu.unbosque.sigevet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"", "/inicio"})
public class InicioServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("datos", Map.of(
                    "conteos", BaseDatos.consultar(Consultas.SQL_CONTEOS),
                    "ingresos", BaseDatos.consultar(Consultas.SQL_INGRESOS),
                    "estados_citas", BaseDatos.consultar(Consultas.SQL_ESTADOS_CITAS),
                    "stock_bajo", BaseDatos.consultar(Consultas.SQL_STOCK_BAJO),
                    "proximas", BaseDatos.consultar(Consultas.SQL_PROXIMAS_CITAS)));
            request.setAttribute("ruta", "/");
            request.getRequestDispatcher("/WEB-INF/jsp/inicio.jsp").forward(request, response);
        } catch (SQLException error) {
            throw new ServletException(error);
        }
    }
}
