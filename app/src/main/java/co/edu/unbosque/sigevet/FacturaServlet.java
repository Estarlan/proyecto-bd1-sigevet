package co.edu.unbosque.sigevet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/facturas/*")
public class FacturaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Parametros.id(request.getPathInfo());
        if (id == null) {
            response.sendError(404);
            return;
        }
        try {
            var factura = BaseDatos.consultarUno(Consultas.SQL_FACTURA, id);
            if (factura == null) {
                response.sendError(404);
                return;
            }
            request.setAttribute("datos", Map.of("datos", factura, "total", factura.get("total"),
                    "lineas", BaseDatos.consultar(Consultas.SQL_LINEAS, id)));
            request.setAttribute("ruta", "/facturas/" + id);
            request.getRequestDispatcher("/WEB-INF/jsp/factura.jsp").forward(request, response);
        } catch (SQLException error) {
            throw new ServletException(error);
        }
    }
}
