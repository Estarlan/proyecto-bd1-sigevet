package co.edu.unbosque.sigevet.controller.api;

import co.edu.unbosque.sigevet.dto.AlgebraListaDTO;
import co.edu.unbosque.sigevet.dto.EjercicioDetalleDTO;
import co.edu.unbosque.sigevet.model.EjercicioAlgebra;
import co.edu.unbosque.sigevet.service.AlgebraService;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * API REST de los 55 ejercicios de álgebra relacional: la lista agrupada y la ejecución de uno.
 */
@RestController
@RequestMapping("/api/algebra")
public class AlgebraApiController {
    /** Servicio que lee y ejecuta los ejercicios. */
    private final AlgebraService algebraService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param algebraService servicio de álgebra relacional.
     */
    public AlgebraApiController(AlgebraService algebraService) {
        this.algebraService = algebraService;
    }

    /**
     * {@code GET /api/algebra}: los ejercicios y sus grupos.
     *
     * @return los grupos y los ejercicios.
     */
    @GetMapping
    public AlgebraListaDTO listar() {
        ArrayList<EjercicioAlgebra> ejercicios = algebraService.listar();
        return new AlgebraListaDTO(algebraService.grupos(ejercicios), ejercicios);
    }

    /**
     * {@code GET /api/algebra/{numero}}: un ejercicio con el resultado de ejecutar su SQL.
     *
     * @param numero número del ejercicio (1 a 55).
     * @return el ejercicio y su resultado (404 si no existe).
     */
    @GetMapping("/{numero}")
    public EjercicioDetalleDTO detalle(@PathVariable int numero) {
        return algebraService.detalle(numero);
    }
}
