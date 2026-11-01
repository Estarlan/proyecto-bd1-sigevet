package co.edu.unbosque.sigevet.controller;

import co.edu.unbosque.sigevet.service.InventarioService;
import co.edu.unbosque.sigevet.service.SedeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador de la página de inventario de medicamentos (la "C" del patrón MVC).
 *
 * <p>Recibe la ruta web, le pide los datos a los servicios, los coloca en el {@link Model} y devuelve el nombre de
 * la plantilla Thymeleaf (la vista) que los muestra. No contiene SQL ni lógica de negocio.</p>
 */
@Controller
public class InventarioController {
    /** Servicio que entrega las existencias de medicamentos. */
    private final InventarioService inventarioService;
    /** Servicio que entrega las sedes para el filtro. */
    private final SedeService sedeService;

    /**
     * Constructor con inyección de dependencias: Spring entrega los servicios al crear el controlador.
     *
     * @param inventarioService servicio del inventario.
     * @param sedeService servicio de sedes.
     */
    public InventarioController(InventarioService inventarioService, SedeService sedeService) {
        this.inventarioService = inventarioService;
        this.sedeService = sedeService;
    }

    /**
     * Ruta {@code GET /inventario}: lista las existencias, con filtro opcional por sede.
     *
     * @param sede identificador de la sede elegida en el filtro, o {@code null} si no se eligió.
     * @param model contenedor de datos que se envía a la vista.
     * @return el nombre de la vista {@code inventario}.
     */
    @GetMapping("/inventario")
    public String listar(@RequestParam(required = false) Integer sede, Model model) {
        model.addAttribute("sedeSeleccionada", sede);
        model.addAttribute("sedes", sedeService.listar());
        model.addAttribute("inventario", inventarioService.listar(sede));
        return "inventario";
    }
}
