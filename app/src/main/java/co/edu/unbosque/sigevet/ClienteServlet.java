package co.edu.unbosque.sigevet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/clientes/*")
public class ClienteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Parametros.id(request.getPathInfo());
        if (id == null) {
            response.sendError(404);
            return;
        }
        try {
            var cliente = BaseDatos.consultarUno(Consultas.SQL_CLIENTE, id);
            if (cliente == null) {
                response.sendError(404);
                return;
            }
            Map<String, Object> datos = new HashMap<>();
            datos.put("datos", cliente);
            datos.put("telefonos", BaseDatos.consultar(Consultas.SQL_TELEFONOS, id));
            datos.put("mascotas", BaseDatos.consultar(Consultas.SQL_MASCOTAS_CLIENTE, id));
            datos.put("facturas", BaseDatos.consultar(Consultas.SQL_FACTURAS_CLIENTE, id));
            request.setAttribute("datos", datos);
            request.setAttribute("ruta", "/clientes/" + id);
            request.getRequestDispatcher("/WEB-INF/jsp/cliente.jsp").forward(request, response);
        } catch (SQLException error) {
            throw new ServletException(error);
        }
    }
}
