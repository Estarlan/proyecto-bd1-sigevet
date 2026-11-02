package co.edu.unbosque.sigevet.controller.api;

import co.edu.unbosque.sigevet.model.ItemInventario;
import co.edu.unbosque.sigevet.service.InventarioService;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import co.edu.unbosque.sigevet.dto.InventarioFormDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    /** {@code POST /api/inventario}: agrega un medicamento a una sede. Responde 201. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void agregar(@RequestBody InventarioFormDTO form) {
        inventarioService.agregar(form);
    }

    /** {@code PUT /api/inventario/{idSede}/{idMedicamento}}: cambia existencias y mínimo. Responde 204. */
    @PutMapping("/{idSede}/{idMedicamento}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void actualizar(@PathVariable int idSede, @PathVariable int idMedicamento, @RequestBody InventarioFormDTO form) {
        inventarioService.actualizar(idSede, idMedicamento, form);
    }

    /** {@code DELETE /api/inventario/{idSede}/{idMedicamento}}: quita el medicamento de la sede. Responde 204. */
    @DeleteMapping("/{idSede}/{idMedicamento}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable int idSede, @PathVariable int idMedicamento) {
        inventarioService.eliminar(idSede, idMedicamento);
    }
}
