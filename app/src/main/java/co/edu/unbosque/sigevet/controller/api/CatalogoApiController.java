package co.edu.unbosque.sigevet.controller.api;

import co.edu.unbosque.sigevet.model.OpcionCatalogo;
import co.edu.unbosque.sigevet.service.CatalogoService;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * API REST de catálogos: las opciones de los {@code <select>} de los formularios.
 */
@RestController
@RequestMapping("/api/catalogos")
public class CatalogoApiController {
    /** Servicio de catálogos. */
    private final CatalogoService catalogoService;

    /**
     * @param catalogoService servicio de catálogos.
     */
    public CatalogoApiController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    /** {@code GET /api/catalogos/ciudades}: ciudades con su departamento. */
    @GetMapping("/ciudades")
    public ArrayList<OpcionCatalogo> ciudades() {
        return catalogoService.ciudades();
    }

    /** {@code GET /api/catalogos/razas}: razas con su especie. */
    @GetMapping("/razas")
    public ArrayList<OpcionCatalogo> razas() {
        return catalogoService.razas();
    }

    /** {@code GET /api/catalogos/servicios}: servicios con su precio. */
    @GetMapping("/servicios")
    public ArrayList<OpcionCatalogo> servicios() {
        return catalogoService.servicios();
    }

    /** {@code GET /api/catalogos/medicamentos?sede=}: todos, o los que tiene una sede con sus existencias. */
    @GetMapping("/medicamentos")
    public ArrayList<OpcionCatalogo> medicamentos(@RequestParam(required = false) Integer sede) {
        return catalogoService.medicamentos(sede);
    }

    /** {@code GET /api/catalogos/especialidades}: especialidades médicas. */
    @GetMapping("/especialidades")
    public ArrayList<OpcionCatalogo> especialidades() {
        return catalogoService.especialidades();
    }

    /** {@code GET /api/catalogos/veterinarios?sede=}: veterinarios activos de una sede (RN-08). */
    @GetMapping("/veterinarios")
    public ArrayList<OpcionCatalogo> veterinarios(@RequestParam int sede) {
        return catalogoService.veterinarios(sede);
    }

    /** {@code GET /api/catalogos/empleados?sede=}: empleados activos de una sede (quien emite la factura). */
    @GetMapping("/empleados")
    public ArrayList<OpcionCatalogo> empleados(@RequestParam int sede) {
        return catalogoService.empleados(sede);
    }
}
