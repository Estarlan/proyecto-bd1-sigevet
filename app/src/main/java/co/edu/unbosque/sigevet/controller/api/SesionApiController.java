package co.edu.unbosque.sigevet.controller.api;

import co.edu.unbosque.sigevet.dto.LoginFormDTO;
import co.edu.unbosque.sigevet.dto.SesionDTO;
import co.edu.unbosque.sigevet.exception.NoAutorizadoException;
import co.edu.unbosque.sigevet.service.SesionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * API REST del inicio de sesión. La sesión es la sesión HTTP del servidor (cookie JSESSIONID): al iniciar sesión se
 * guarda en ella el {@link SesionDTO} del empleado, y {@code FiltroSesion} la exige en el resto de la API.
 */
@RestController
@RequestMapping("/api/sesion")
public class SesionApiController {
    /** Nombre del atributo de la sesión HTTP donde se guarda el empleado conectado. */
    public static final String ATRIBUTO = "empleado";
    /** Servicio del inicio de sesión. */
    private final SesionService sesionService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param sesionService servicio del inicio de sesión.
     */
    public SesionApiController(SesionService sesionService) {
        this.sesionService = sesionService;
    }

    /**
     * {@code POST /api/sesion}: inicia sesión con el correo y el documento del empleado.
     *
     * @param form correo y documento.
     * @param peticion petición HTTP, para crear la sesión.
     * @return los datos del empleado conectado.
     */
    @PostMapping
    public SesionDTO iniciar(@RequestBody LoginFormDTO form, HttpServletRequest peticion) {
        SesionDTO sesion = sesionService.iniciar(form);
        // se descarta cualquier sesión anterior y se crea una nueva (otra cookie) al iniciar sesión
        HttpSession anterior = peticion.getSession(false);
        if (anterior != null) {
            anterior.invalidate();
        }
        peticion.getSession(true).setAttribute(ATRIBUTO, sesion);
        return sesion;
    }

    /**
     * {@code GET /api/sesion}: quién está conectado. Angular lo pregunta al cargar la página.
     *
     * @param peticion petición HTTP.
     * @return los datos del empleado conectado.
     * @throws NoAutorizadoException si no hay sesión (401).
     */
    @GetMapping
    public SesionDTO actual(HttpServletRequest peticion) {
        HttpSession sesion = peticion.getSession(false);
        if (sesion == null || sesion.getAttribute(ATRIBUTO) == null) {
            throw new NoAutorizadoException("No ha iniciado sesión.");
        }
        return (SesionDTO) sesion.getAttribute(ATRIBUTO);
    }

    /**
     * {@code DELETE /api/sesion}: cierra la sesión. Responde 204.
     *
     * @param peticion petición HTTP.
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cerrar(HttpServletRequest peticion) {
        HttpSession sesion = peticion.getSession(false);
        if (sesion != null) {
            sesion.invalidate();
        }
    }
}
