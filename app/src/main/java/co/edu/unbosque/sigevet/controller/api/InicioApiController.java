package co.edu.unbosque.sigevet.controller.api;

import co.edu.unbosque.sigevet.dto.InicioDTO;
import co.edu.unbosque.sigevet.service.AgendaService;
import co.edu.unbosque.sigevet.service.InicioService;
import co.edu.unbosque.sigevet.service.InventarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API REST de la página de inicio: reúne en un solo {@link InicioDTO} los datos del tablero.
 */
@RestController
@RequestMapping("/api/inicio")
public class InicioApiController {
    /** Servicio con los conteos e ingresos. */
    private final InicioService inicioService;
    /** Servicio de la agenda (próximas citas). */
    private final AgendaService agendaService;
    /** Servicio del inventario (stock bajo). */
    private final InventarioService inventarioService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param inicioService servicio de inicio.
     * @param agendaService servicio de la agenda.
     * @param inventarioService servicio del inventario.
     */
    public InicioApiController(InicioService inicioService, AgendaService agendaService,
                               InventarioService inventarioService) {
        this.inicioService = inicioService;
        this.agendaService = agendaService;
        this.inventarioService = inventarioService;
    }

    /**
     * {@code GET /api/inicio}: los datos del tablero de inicio.
     *
     * @return conteos, ingresos, citas por estado, stock bajo y próximas citas.
     */
    @GetMapping
    public InicioDTO inicio() {
        return new InicioDTO(inicioService.conteos(), inicioService.ingresos(), inicioService.estadosCitas(),
                inventarioService.stockBajo(), agendaService.proximas());
    }
}
