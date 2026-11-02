package co.edu.unbosque.sigevet.controller.api;

import co.edu.unbosque.sigevet.dto.MascotaDetalleDTO;
import co.edu.unbosque.sigevet.service.MascotaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import co.edu.unbosque.sigevet.dto.EstadoDTO;
import co.edu.unbosque.sigevet.dto.IdDTO;
import co.edu.unbosque.sigevet.dto.MascotaFormDTO;
import co.edu.unbosque.sigevet.dto.MensajeDTO;
import co.edu.unbosque.sigevet.model.Mascota;
import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * API REST de mascotas: entrega la ficha de una mascota con su historia clínica y sus consultas.
 */
@RestController
@RequestMapping("/api/mascotas")
public class MascotaApiController {
    /** Servicio que entrega los datos de las mascotas. */
    private final MascotaService mascotaService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param mascotaService servicio de mascotas.
     */
    public MascotaApiController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    /**
     * {@code GET /api/mascotas/{id}}: una mascota con su historia clínica y sus consultas.
     *
     * @param id identificador de la mascota.
     * @return el detalle de la mascota (404 si no existe).
     */
    @GetMapping("/{id}")
    public MascotaDetalleDTO detalle(@PathVariable int id) {
        return mascotaService.detalle(id);
    }

    /** {@code GET /api/mascotas?buscar=}: busca por nombre, apellido del propietario o microchip. */
    @GetMapping
    public ArrayList<Mascota> listar(@RequestParam(defaultValue = "") String buscar) {
        return mascotaService.listar(buscar);
    }

    /** {@code GET /api/mascotas/{id}/formulario}: datos con identificadores, para editar. */
    @GetMapping("/{id}/formulario")
    public MascotaFormDTO formulario(@PathVariable int id) {
        return mascotaService.formulario(id);
    }

    /** {@code POST /api/mascotas}: crea la mascota y su historia clínica. Responde 201 con el id. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdDTO crear(@RequestBody MascotaFormDTO form) {
        return new IdDTO(mascotaService.crear(form));
    }

    /** {@code PUT /api/mascotas/{id}}: actualiza la mascota y su historia clínica. Responde 204. */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void actualizar(@PathVariable int id, @RequestBody MascotaFormDTO form) {
        mascotaService.actualizar(id, form);
    }

    /** {@code PATCH /api/mascotas/{id}/estado}: borrado lógico (ACTIVA, FALLECIDA o TRASLADADA). */
    @PatchMapping("/{id}/estado")
    public MensajeDTO cambiarEstado(@PathVariable int id, @RequestBody EstadoDTO cuerpo) {
        return new MensajeDTO(mascotaService.cambiarEstado(id, cuerpo.getEstado()));
    }
}
