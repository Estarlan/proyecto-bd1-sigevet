package co.edu.unbosque.sigevet.controller;

import co.edu.unbosque.sigevet.dto.MascotaDetalleDTO;
import co.edu.unbosque.sigevet.service.MascotaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controlador de la página de una mascota (la "C" del patrón MVC).
 *
 * <p>Recibe la ruta web, le pide los datos al servicio, los coloca en el {@link Model} y devuelve el nombre de la
 * plantilla Thymeleaf (la vista) que los muestra. No contiene SQL ni lógica de negocio.</p>
 */
@Controller
public class MascotaController {
    /** Servicio que entrega los datos de las mascotas. */
    private final MascotaService mascotaService;

    /**
     * Constructor con inyección de dependencias: Spring entrega el servicio al crear el controlador.
     *
     * @param mascotaService servicio de mascotas.
     */
    public MascotaController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    /**
     * Ruta {@code GET /mascotas/{id}}: muestra una mascota con su historia clínica y sus consultas.
     *
     * @param id identificador de la mascota, tomado de la dirección.
     * @param model contenedor de datos que se envía a la vista.
     * @return el nombre de la vista {@code mascota}.
     */
    @GetMapping("/mascotas/{id}")
    public String detalle(@PathVariable int id, Model model) {
        MascotaDetalleDTO detalle = mascotaService.detalle(id);
        model.addAttribute("mascota", detalle.getMascota());
        model.addAttribute("historia", detalle.getHistoria());
        model.addAttribute("consultas", detalle.getConsultas());
        return "mascota";
    }
}
