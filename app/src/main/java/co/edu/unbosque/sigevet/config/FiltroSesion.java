package co.edu.unbosque.sigevet.config;

import co.edu.unbosque.sigevet.controller.api.SesionApiController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Revisa cada petición a la API antes de que llegue al controlador: si no hay un empleado en la sesión HTTP, responde
 * 401 y la petición no se ejecuta. Se registra en {@link ConfiguracionWeb} para {@code /api/**}, excepto
 * {@code /api/sesion}, que es por donde se inicia sesión.
 */
@Component
public class FiltroSesion implements HandlerInterceptor {

    /**
     * @param peticion petición HTTP.
     * @param respuesta respuesta HTTP.
     * @param manejador controlador que atendería la petición.
     * @return {@code true} si hay sesión y la petición sigue; {@code false} si se respondió 401.
     * @throws Exception si no se puede escribir la respuesta.
     */
    @Override
    public boolean preHandle(HttpServletRequest peticion, HttpServletResponse respuesta, Object manejador)
            throws Exception {
        HttpSession sesion = peticion.getSession(false);
        if (sesion != null && sesion.getAttribute(SesionApiController.ATRIBUTO) != null) {
            return true;
        }
        respuesta.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        respuesta.setContentType("application/json;charset=UTF-8");
        respuesta.getWriter().write("{\"estado\":401,\"mensaje\":\"Inicie sesión para usar la aplicación.\"}");
        return false;
    }
}
