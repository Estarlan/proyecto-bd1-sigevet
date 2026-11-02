package co.edu.unbosque.sigevet.controller.api;

import co.edu.unbosque.sigevet.dto.FacturaDetalleDTO;
import co.edu.unbosque.sigevet.model.Factura;
import co.edu.unbosque.sigevet.service.FacturaService;
import co.edu.unbosque.sigevet.util.TextoUtil;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import co.edu.unbosque.sigevet.dto.EstadoDTO;
import co.edu.unbosque.sigevet.dto.FacturaFormDTO;
import co.edu.unbosque.sigevet.dto.IdDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * API REST de facturas: lista con filtros y detalle de una factura con sus líneas.
 */
@RestController
@RequestMapping("/api/facturas")
public class FacturaApiController {
    /** Servicio que entrega las facturas. */
    private final FacturaService facturaService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param facturaService servicio de facturas.
     */
    public FacturaApiController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    /**
     * {@code GET /api/facturas?sede=&estado=}: las facturas, filtradas si se envían los parámetros.
     *
     * @param sede identificador de la sede (opcional).
     * @param estado estado de la factura (opcional; vacío equivale a sin filtro).
     * @return las facturas que cumplen los filtros.
     */
    @GetMapping
    public ArrayList<Factura> listar(@RequestParam(required = false) Integer sede,
                                     @RequestParam(required = false) String estado) {
        return facturaService.listar(sede, TextoUtil.vacioANulo(estado));
    }

    /**
     * {@code GET /api/facturas/estados}: los estados posibles de una factura, para el filtro del frontend.
     *
     * @return los nombres de los estados.
     */
    @GetMapping("/estados")
    public String[] estados() {
        return facturaService.estados();
    }

    /**
     * {@code GET /api/facturas/{id}}: una factura con sus líneas de detalle.
     *
     * @param id identificador de la factura.
     * @return el detalle de la factura (404 si no existe).
     */
    @GetMapping("/{id}")
    public FacturaDetalleDTO detalle(@PathVariable long id) {
        return facturaService.detalle(id);
    }

    /** {@code POST /api/facturas}: crea la factura con sus líneas en una transacción. Responde 201 con el id. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdDTO crear(@RequestBody FacturaFormDTO form) {
        return new IdDTO(facturaService.crear(form));
    }

    /** {@code PATCH /api/facturas/{id}/estado}: PAGADA (registrar pago) o ANULADA. Responde 204. */
    @PatchMapping("/{id}/estado")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cambiarEstado(@PathVariable long id, @RequestBody EstadoDTO cuerpo) {
        facturaService.cambiarEstado(id, cuerpo.getEstado());
    }
}
