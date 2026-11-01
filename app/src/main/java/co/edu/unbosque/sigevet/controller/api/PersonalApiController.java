package co.edu.unbosque.sigevet.controller.api;

import co.edu.unbosque.sigevet.model.Empleado;
import co.edu.unbosque.sigevet.service.PersonalService;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * API REST del personal. La lista es polimórfica: cada elemento es un {@code Empleado}, un {@code Veterinario} o un
 * {@code Auxiliar}, y el JSON de cada uno trae los campos de su clase real y su propia {@code descripcion}.
 */
@RestController
@RequestMapping("/api/personal")
public class PersonalApiController {
    /** Servicio que entrega el personal. */
    private final PersonalService personalService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param personalService servicio del personal.
     */
    public PersonalApiController(PersonalService personalService) {
        this.personalService = personalService;
    }

    /**
     * {@code GET /api/personal?sede=}: los empleados, de una sede o de todas.
     *
     * @param sede identificador de la sede (opcional).
     * @return los empleados con su subtipo.
     */
    @GetMapping
    public ArrayList<Empleado> listar(@RequestParam(required = false) Integer sede) {
        return personalService.listar(sede);
    }
}
