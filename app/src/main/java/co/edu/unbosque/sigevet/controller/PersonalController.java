package co.edu.unbosque.sigevet.controller;

import co.edu.unbosque.sigevet.model.Empleado;
import co.edu.unbosque.sigevet.service.PersonalService;
import co.edu.unbosque.sigevet.service.SedeService;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador de la página del personal (la "C" del patrón MVC).
 *
 * <p>Recibe la ruta web, le pide los datos a los servicios, los coloca en el {@link Model} y devuelve el nombre de
 * la plantilla Thymeleaf (la vista) que los muestra. La vista llama a {@code getDescripcion()} de cada empleado y
 * cada tipo (veterinario, auxiliar, administrativo) responde distinto: es el polimorfismo del modelo.</p>
 */
@Controller
public class PersonalController {
    /** Servicio que entrega el personal. */
    private final PersonalService personalService;
    /** Servicio que entrega las sedes para el filtro. */
    private final SedeService sedeService;

    /**
     * Constructor con inyección de dependencias: Spring entrega los servicios al crear el controlador.
     *
     * @param personalService servicio del personal.
     * @param sedeService servicio de sedes.
     */
    public PersonalController(PersonalService personalService, SedeService sedeService) {
        this.personalService = personalService;
        this.sedeService = sedeService;
    }

    /**
     * Ruta {@code GET /personal}: lista los empleados, con filtro opcional por sede.
     *
     * @param sede identificador de la sede elegida en el filtro, o {@code null} si no se eligió.
     * @param model contenedor de datos que se envía a la vista.
     * @return el nombre de la vista {@code personal}.
     */
    @GetMapping("/personal")
    public String listar(@RequestParam(required = false) Integer sede, Model model) {
        ArrayList<Empleado> empleados = personalService.listar(sede);
        model.addAttribute("empleados", empleados);
        model.addAttribute("sedes", sedeService.listar());
        model.addAttribute("sedeSeleccionada", sede);
        return "personal";
    }
}
