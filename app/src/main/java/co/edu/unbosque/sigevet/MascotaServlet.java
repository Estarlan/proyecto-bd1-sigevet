package co.edu.unbosque.sigevet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/mascotas/*")
public class MascotaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long id = Parametros.id(request.getPathInfo());
        if (id == null) {
            response.sendError(404);
            return;
        }
        try {
            var mascota = BaseDatos.consultarUno(Consultas.SQL_MASCOTA, id);
            if (mascota == null) {
                response.sendError(404);
                return;
            }
            Map<String, Object> datos = new HashMap<>();
            datos.put("datos", mascota);
            datos.put("historia", BaseDatos.consultarUno(Consultas.SQL_HISTORIA, id));
            var consultas = BaseDatos.consultar(Consultas.SQL_CONSULTAS, id);
            var prescripciones = BaseDatos.consultar(Consultas.SQL_PRESCRIPCIONES, id);
            for (var consulta : consultas) {
                consulta.put("prescripciones", prescripciones.stream()
                        .filter(p -> Objects.equals(p.get("id_consulta"), consulta.get("id_consulta"))).toList());
            }
            datos.put("consultas", consultas);
            request.setAttribute("datos", datos);
            request.setAttribute("ruta", "/mascotas/" + id);
            request.getRequestDispatcher("/WEB-INF/jsp/mascota.jsp").forward(request, response);
        } catch (SQLException error) {
            throw new ServletException(error);
        }
    }
}
