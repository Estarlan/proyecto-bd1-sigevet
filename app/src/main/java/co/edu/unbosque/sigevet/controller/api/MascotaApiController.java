package co.edu.unbosque.sigevet.controller.api;

import co.edu.unbosque.sigevet.dto.MascotaDetalleDTO;
import co.edu.unbosque.sigevet.service.MascotaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
