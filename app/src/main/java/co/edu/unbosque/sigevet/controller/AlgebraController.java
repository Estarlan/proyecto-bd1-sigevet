package co.edu.unbosque.sigevet.controller;

import co.edu.unbosque.sigevet.dto.EjercicioDetalleDTO;
import co.edu.unbosque.sigevet.model.EjercicioAlgebra;
import co.edu.unbosque.sigevet.service.AlgebraService;
import java.util.ArrayList;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controlador de las páginas de álgebra relacional (la "C" del patrón MVC).
 *
 * <p>Recibe las rutas web, le pide los datos al servicio, los coloca en el {@link Model} y devuelve el nombre de la
 * plantilla Thymeleaf (la vista) que los muestra. No contiene SQL ni lógica de negocio.</p>
 */
@Controller
public class AlgebraController {
    /** Servicio que entrega los ejercicios de álgebra y sus resultados. */
    private final AlgebraService algebraService;

    /**
     * Constructor con inyección de dependencias: Spring entrega el servicio al crear el controlador.
     *
     * @param algebraService servicio de álgebra relacional.
     */
    public AlgebraController(AlgebraService algebraService) {
        this.algebraService = algebraService;
    }

    /**
     * Ruta {@code GET /algebra}: lista todos los ejercicios agrupados por tema.
     *
     * @param model contenedor de datos que se envía a la vista.
     * @return el nombre de la vista {@code algebra}.
     */
    @GetMapping("/algebra")
    public String listar(Model model) {
        ArrayList<EjercicioAlgebra> ejercicios = algebraService.listar();
        model.addAttribute("grupos", algebraService.grupos(ejercicios));
        model.addAttribute("ejercicios", ejercicios);
        return "algebra";
    }

    /**
     * Ruta {@code GET /algebra/{numero}}: muestra un ejercicio con su enunciado, su SQL y el resultado de ejecutarlo.
     *
     * @param numero número del ejercicio, tomado de la dirección.
     * @param model contenedor de datos que se envía a la vista.
     * @return el nombre de la vista {@code ejercicio}.
     */
    @GetMapping("/algebra/{numero}")
    public String detalle(@PathVariable int numero, Model model) {
        EjercicioDetalleDTO detalle = algebraService.detalle(numero);
        model.addAttribute("ejercicio", detalle.getEjercicio());
        model.addAttribute("resultado", detalle.getResultado());
        return "ejercicio";
    }
}
