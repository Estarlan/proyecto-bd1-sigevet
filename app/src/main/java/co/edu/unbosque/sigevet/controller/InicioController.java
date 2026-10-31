package co.edu.unbosque.sigevet.controller;

import co.edu.unbosque.sigevet.service.AgendaService;
import co.edu.unbosque.sigevet.service.InicioService;
import co.edu.unbosque.sigevet.service.InventarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador de la página de inicio (la "C" del patrón MVC).
 *
 * <p>Recibe la ruta web, le pide los datos a los servicios, los coloca en el {@link Model} y devuelve el nombre de
 * la plantilla Thymeleaf (la vista) que los muestra. No contiene SQL ni lógica de negocio.</p>
 */
@Controller
public class InicioController {
    /** Servicio de los resúmenes generales. */
    private final InicioService inicioService;
    /** Servicio de la agenda, para las próximas citas. */
    private final AgendaService agendaService;
    /** Servicio del inventario, para la alerta de stock bajo. */
    private final InventarioService inventarioService;

    /**
     * Constructor con inyección de dependencias: Spring entrega los servicios al crear el controlador.
     *
     * @param inicioService servicio de los resúmenes generales.
     * @param agendaService servicio de la agenda.
     * @param inventarioService servicio del inventario.
     */
    public InicioController(InicioService inicioService, AgendaService agendaService,
                             InventarioService inventarioService) {
        this.inicioService = inicioService;
        this.agendaService = agendaService;
        this.inventarioService = inventarioService;
    }

    /**
     * Ruta {@code GET /}: muestra el panel de inicio con los conteos por tabla, los ingresos por sede, las citas por
     * estado, los medicamentos con stock bajo y las próximas citas.
     *
     * @param model contenedor de datos que se envía a la vista.
     * @return el nombre de la vista {@code inicio}.
     */
    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("conteos", inicioService.conteos());
        model.addAttribute("ingresos", inicioService.ingresos());
        model.addAttribute("estadosCitas", inicioService.estadosCitas());
        model.addAttribute("stockBajo", inventarioService.stockBajo());
        model.addAttribute("proximas", agendaService.proximas());
        return "inicio";
    }
}
