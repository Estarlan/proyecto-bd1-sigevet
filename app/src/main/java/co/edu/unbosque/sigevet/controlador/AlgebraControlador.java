package co.edu.unbosque.sigevet.controlador;

import co.edu.unbosque.sigevet.dao.*;
import co.edu.unbosque.sigevet.modelo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;

@Controller
public class AlgebraControlador {
    @Autowired
    private co.edu.unbosque.sigevet.algebra.LectorAlgebra lectorAlgebra;

    @Autowired
    private AlgebraDAO algebraDAO;

    @GetMapping("/algebra")
    public String listar(Model model) {
        ArrayList<EjercicioAlgebra> ejercicios = lectorAlgebra.listar();
        ArrayList<String> grupos = new ArrayList<String>();
        for (EjercicioAlgebra ejercicio : ejercicios) {
            if (!grupos.contains(ejercicio.getGrupo())) {
                grupos.add(ejercicio.getGrupo());
            }
        }
        model.addAttribute("grupos", grupos);
        model.addAttribute("ejercicios", ejercicios);
        return "algebra";
    }

    @GetMapping("/algebra/{numero}")
    public String detalle(@PathVariable int numero, Model model) {
        EjercicioAlgebra ejercicio = lectorAlgebra.buscar(numero);
        if (ejercicio == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        model.addAttribute("ejercicio", ejercicio);
        model.addAttribute("resultado", algebraDAO.ejecutar(ejercicio.getSql()));
        return "ejercicio";
    }
}
