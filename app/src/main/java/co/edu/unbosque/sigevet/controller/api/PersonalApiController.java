package co.edu.unbosque.sigevet.controller.api;

import co.edu.unbosque.sigevet.model.Empleado;
import co.edu.unbosque.sigevet.service.PersonalService;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import co.edu.unbosque.sigevet.dto.EmpleadoFormDTO;
import co.edu.unbosque.sigevet.dto.EstadoDTO;
import co.edu.unbosque.sigevet.dto.IdDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    /** {@code GET /api/personal/{id}/formulario}: datos del empleado y su subtipo, para editar. */
    @GetMapping("/{id}/formulario")
    public EmpleadoFormDTO formulario(@PathVariable int id) {
        return personalService.formulario(id);
    }

    /** {@code POST /api/personal}: crea el empleado con su subtipo. Responde 201 con el id. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdDTO crear(@RequestBody EmpleadoFormDTO form) {
        return new IdDTO(personalService.crear(form));
    }

    /** {@code PUT /api/personal/{id}}: actualiza el empleado y su subtipo. Responde 204. */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void actualizar(@PathVariable int id, @RequestBody EmpleadoFormDTO form) {
        personalService.actualizar(id, form);
    }

    /** {@code PATCH /api/personal/{id}/estado}: borrado lógico (ACTIVO o INACTIVO). Responde 204. */
    @PatchMapping("/{id}/estado")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cambiarEstado(@PathVariable int id, @RequestBody EstadoDTO cuerpo) {
        personalService.cambiarEstado(id, cuerpo.getEstado());
    }
}
