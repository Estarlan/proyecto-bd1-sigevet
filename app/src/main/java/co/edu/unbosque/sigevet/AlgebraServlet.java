package co.edu.unbosque.sigevet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/algebra", "/algebra/*"})
public class AlgebraServlet extends HttpServlet {
    private Algebra algebra;

    @Override
    public void init() throws ServletException {
        try {
            algebra = new Algebra();
        } catch (IOException | IllegalArgumentException error) {
            throw new ServletException("No se pudieron cargar los ejercicios de álgebra", error);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("ruta", "/algebra");
        String ruta = request.getPathInfo();
        if (ruta == null || ruta.equals("/")) {
            Map<String, List<Map<String, Object>>> grupos = new LinkedHashMap<>();
            for (Map<String, Object> ejercicio : algebra.ejercicios()) {
                String grupo = (String) ejercicio.get("grupo");
                grupos.computeIfAbsent(grupo, clave -> new ArrayList<>()).add(ejercicio);
            }
            request.setAttribute("grupos", grupos);
            request.getRequestDispatcher("/WEB-INF/jsp/algebra.jsp").forward(request, response);
            return;
        }
        Long numero = Parametros.id(ruta);
        Map<String, Object> elegido = null;
        for (Map<String, Object> ejercicio : algebra.ejercicios()) {
            if (numero != null && ((Number) ejercicio.get("numero")).longValue() == numero) {
                elegido = ejercicio;
                break;
            }
        }
        if (elegido == null) {
            response.sendError(404);
            return;
        }
        try {
            List<Map<String, Object>> filas = BaseDatos.consultar((String) elegido.get("sql"));
            List<String> columnas = filas.isEmpty() ? List.of() : new ArrayList<>(filas.get(0).keySet());
            request.setAttribute("ruta", "/algebra/" + numero);
            request.setAttribute("columnasPesos", List.of("precio", "precio_venta", "salario", "promedio",
                    "minimo", "maximo", "total", "ingresos", "ticket_promedio", "valor_inventario"));
            request.setAttribute("ejercicio", elegido);
            request.setAttribute("filas", filas);
            request.setAttribute("columnas", columnas);
            request.getRequestDispatcher("/WEB-INF/jsp/ejercicio.jsp").forward(request, response);
        } catch (SQLException error) {
            throw new ServletException(error);
        }
    }
}
