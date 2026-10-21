package co.edu.unbosque.sigevet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/personal")
public class PersonalServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        var filtros = Parametros.filtros(request, "sede");
        Integer sede = Parametros.numero(filtros.get("sede"));
        try {
            var empleados = BaseDatos.consultar(Consultas.SQL_EMPLEADOS, sede, sede);
            var especialidades = BaseDatos.consultar(Consultas.SQL_ESPECIALIDADES, sede, sede);
            for (var empleado : empleados) {
                empleado.put("especialidades", especialidades.stream()
                        .filter(e -> Objects.equals(e.get("id_empleado"), empleado.get("id_empleado")))
                        .map(e -> e.get("nombre")).toList());
            }
            request.setAttribute("datos", Map.of("sedes", BaseDatos.consultar(Consultas.SQL_DIRECTORES),
                    "empleados", empleados));
            request.setAttribute("filtros", filtros);
            request.setAttribute("ruta", "/personal");
            request.getRequestDispatcher("/WEB-INF/jsp/personal.jsp").forward(request, response);
        } catch (SQLException error) {
            throw new ServletException(error);
        }
    }
}
