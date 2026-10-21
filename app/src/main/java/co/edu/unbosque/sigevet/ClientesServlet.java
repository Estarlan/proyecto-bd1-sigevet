package co.edu.unbosque.sigevet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/clientes")
public class ClientesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String buscar = Parametros.buscar(request.getParameter("buscar"));
        int pagina = Parametros.pagina(request.getParameter("pagina"));
        String consulta = "%" + buscar.toUpperCase(Locale.ROOT) + "%";
        try {
            int total = ((Number) BaseDatos.consultarUno(Consultas.SQL_TOTAL_CLIENTES,
                    consulta, consulta).get("total")).intValue();
            int totalPaginas = Math.max(1, (total + 19) / 20);
            pagina = Math.min(pagina, totalPaginas);
            request.setAttribute("datos", Map.of("total", total, "filas",
                    BaseDatos.consultar(Consultas.SQL_CLIENTES, consulta, consulta, (pagina - 1) * 20, 20)));
            request.setAttribute("buscar", buscar);
            request.setAttribute("pagina", pagina);
            request.setAttribute("totalPaginas", totalPaginas);
            request.setAttribute("ruta", "/clientes");
            request.getRequestDispatcher("/WEB-INF/jsp/clientes.jsp").forward(request, response);
        } catch (SQLException error) {
            throw new ServletException(error);
        }
    }
}
