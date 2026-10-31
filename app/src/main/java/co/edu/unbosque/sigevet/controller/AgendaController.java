package co.edu.unbosque.sigevet.controller;

import co.edu.unbosque.sigevet.service.AgendaService;
import co.edu.unbosque.sigevet.service.SedeService;
import co.edu.unbosque.sigevet.util.TextoUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador de la página de agenda de citas (la "C" del patrón MVC).
 *
 * <p>Recibe la ruta web, le pide los datos a los servicios, los coloca en el {@link Model} y devuelve el nombre de
 * la plantilla Thymeleaf (la vista) que los muestra. No contiene SQL ni lógica de negocio.</p>
 */
@Controller
public class AgendaController {
    /** Servicio que entrega las citas y sus estados. */
    private final AgendaService agendaService;
    /** Servicio que entrega las sedes para el filtro. */
    private final SedeService sedeService;

    /**
     * Constructor con inyección de dependencias: Spring entrega los servicios al crear el controlador.
     *
     * @param agendaService servicio de la agenda.
     * @param sedeService servicio de sedes.
     */
    public AgendaController(AgendaService agendaService, SedeService sedeService) {
        this.agendaService = agendaService;
        this.sedeService = sedeService;
    }

    /**
     * Ruta {@code GET /agenda}: lista las citas, con filtros opcionales por sede y por estado.
     *
     * @param sede identificador de la sede elegida en el filtro, o {@code null} si no se eligió.
     * @param estado estado elegido en el filtro; puede llegar vacío.
     * @param model contenedor de datos que se envía a la vista.
     * @return el nombre de la vista {@code agenda}.
     */
    @GetMapping("/agenda")
    public String listar(@RequestParam(required = false) Integer sede,
                         @RequestParam(required = false) String estado, Model model) {
        // un filtro vacío del formulario se trata como "sin filtro"
        String filtroEstado = TextoUtil.vacioANulo(estado);
        model.addAttribute("sedeSeleccionada", sede);
        model.addAttribute("estadoSeleccionado", filtroEstado);
        model.addAttribute("estados", agendaService.estados());
        model.addAttribute("sedes", sedeService.listar());
        model.addAttribute("citas", agendaService.listar(sede, filtroEstado));
        return "agenda";
    }
}
