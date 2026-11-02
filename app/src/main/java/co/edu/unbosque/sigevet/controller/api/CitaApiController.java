package co.edu.unbosque.sigevet.controller.api;

import co.edu.unbosque.sigevet.model.Cita;
import co.edu.unbosque.sigevet.service.AgendaService;
import co.edu.unbosque.sigevet.util.TextoUtil;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import co.edu.unbosque.sigevet.dto.CitaFormDTO;
import co.edu.unbosque.sigevet.dto.EstadoDTO;
import co.edu.unbosque.sigevet.dto.IdDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * API REST de la agenda de citas, con filtros por sede y por estado.
 */
@RestController
@RequestMapping("/api/citas")
public class CitaApiController {
    /** Servicio que entrega las citas. */
    private final AgendaService agendaService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param agendaService servicio de la agenda.
     */
    public CitaApiController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    /**
     * {@code GET /api/citas?sede=&estado=}: las citas, filtradas si se envían los parámetros.
     *
     * @param sede identificador de la sede (opcional).
     * @param estado estado de la cita (opcional; vacío equivale a sin filtro).
     * @return las citas que cumplen los filtros.
     */
    @GetMapping
    public ArrayList<Cita> listar(@RequestParam(required = false) Integer sede,
                                  @RequestParam(required = false) String estado) {
        return agendaService.listar(sede, TextoUtil.vacioANulo(estado));
    }

    /**
     * {@code GET /api/citas/estados}: los estados posibles de una cita, para el filtro del frontend.
     *
     * @return los nombres de los estados.
     */
    @GetMapping("/estados")
    public String[] estados() {
        return agendaService.estados();
    }

    /** {@code POST /api/citas}: agenda una cita PROGRAMADA. Responde 201 con el id. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdDTO crear(@RequestBody CitaFormDTO form) {
        return new IdDTO(agendaService.crear(form));
    }

    /** {@code PATCH /api/citas/{id}/estado}: ATENDIDA, CANCELADA o NO_ASISTIO. Responde 204. */
    @PatchMapping("/{id}/estado")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cambiarEstado(@PathVariable long id, @RequestBody EstadoDTO cuerpo) {
        agendaService.cambiarEstado(id, cuerpo.getEstado());
    }
}
