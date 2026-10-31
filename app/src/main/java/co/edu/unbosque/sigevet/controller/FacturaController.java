package co.edu.unbosque.sigevet.controller;

import co.edu.unbosque.sigevet.dto.FacturaDetalleDTO;
import co.edu.unbosque.sigevet.service.FacturaService;
import co.edu.unbosque.sigevet.service.SedeService;
import co.edu.unbosque.sigevet.util.TextoUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador de las páginas de facturas (la "C" del patrón MVC).
 *
 * <p>Recibe las rutas web, le pide los datos a los servicios, los coloca en el {@link Model} y devuelve el nombre de
 * la plantilla Thymeleaf (la vista) que los muestra. No contiene SQL ni lógica de negocio.</p>
 */
@Controller
public class FacturaController {
    /** Servicio que entrega las facturas y sus estados. */
    private final FacturaService facturaService;
    /** Servicio que entrega las sedes para el filtro. */
    private final SedeService sedeService;

    /**
     * Constructor con inyección de dependencias: Spring entrega los servicios al crear el controlador.
     *
     * @param facturaService servicio de facturas.
     * @param sedeService servicio de sedes.
     */
    public FacturaController(FacturaService facturaService, SedeService sedeService) {
        this.facturaService = facturaService;
        this.sedeService = sedeService;
    }

    /**
     * Ruta {@code GET /facturas}: lista las facturas, con filtros opcionales por sede y por estado.
     *
     * @param sede identificador de la sede elegida en el filtro, o {@code null} si no se eligió.
     * @param estado estado elegido en el filtro; puede llegar vacío.
     * @param model contenedor de datos que se envía a la vista.
     * @return el nombre de la vista {@code facturas}.
     */
    @GetMapping("/facturas")
    public String listar(@RequestParam(required = false) Integer sede,
                         @RequestParam(required = false) String estado, Model model) {
        // un filtro vacío del formulario se trata como "sin filtro"
        String filtroEstado = TextoUtil.vacioANulo(estado);
        model.addAttribute("sedeSeleccionada", sede);
        model.addAttribute("estadoSeleccionado", filtroEstado);
        model.addAttribute("estados", facturaService.estados());
        model.addAttribute("sedes", sedeService.listar());
        model.addAttribute("facturas", facturaService.listar(sede, filtroEstado));
        return "facturas";
    }

    /**
     * Ruta {@code GET /facturas/{id}}: muestra una factura con sus líneas.
     *
     * @param id identificador de la factura, tomado de la dirección.
     * @param model contenedor de datos que se envía a la vista.
     * @return el nombre de la vista {@code factura}.
     */
    @GetMapping("/facturas/{id}")
    public String detalle(@PathVariable long id, Model model) {
        FacturaDetalleDTO detalle = facturaService.detalle(id);
        model.addAttribute("factura", detalle.getFactura());
        model.addAttribute("lineas", detalle.getLineas());
        return "factura";
    }
}
