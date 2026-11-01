package co.edu.unbosque.sigevet.controller.api;

import co.edu.unbosque.sigevet.model.ItemInventario;
import co.edu.unbosque.sigevet.service.InventarioService;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * API REST del inventario de medicamentos por sede.
 */
@RestController
@RequestMapping("/api/inventario")
public class InventarioApiController {
    /** Servicio que entrega el inventario. */
    private final InventarioService inventarioService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param inventarioService servicio del inventario.
     */
    public InventarioApiController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    /**
     * {@code GET /api/inventario?sede=}: las existencias, de una sede o de todas.
     *
     * @param sede identificador de la sede (opcional).
     * @return los ítems del inventario.
     */
    @GetMapping
    public ArrayList<ItemInventario> listar(@RequestParam(required = false) Integer sede) {
        return inventarioService.listar(sede);
    }
}
