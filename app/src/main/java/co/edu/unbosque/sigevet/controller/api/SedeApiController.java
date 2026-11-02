package co.edu.unbosque.sigevet.controller.api;

import co.edu.unbosque.sigevet.model.Sede;
import co.edu.unbosque.sigevet.service.SedeService;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API REST de sedes. La usan los filtros por sede de la agenda, el inventario, las facturas y el personal.
 */
@RestController
@RequestMapping("/api/sedes")
public class SedeApiController {
    /** Servicio que entrega las sedes. */
    private final SedeService sedeService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param sedeService servicio de sedes.
     */
    public SedeApiController(SedeService sedeService) {
        this.sedeService = sedeService;
    }

    /**
     * {@code GET /api/sedes}: todas las sedes de la red.
     *
     * @return las sedes.
     */
    @GetMapping
    public ArrayList<Sede> listar() {
        return sedeService.listar();
    }
}
