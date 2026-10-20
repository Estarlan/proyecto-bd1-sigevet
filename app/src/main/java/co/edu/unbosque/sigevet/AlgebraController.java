package co.edu.unbosque.sigevet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class AlgebraController {
    private final Algebra algebra;
    private final BaseDatos baseDatos;

    public AlgebraController(Algebra algebra, BaseDatos baseDatos) {
        this.algebra = algebra;
        this.baseDatos = baseDatos;
    }

    @GetMapping("/algebra")
    public String algebra(Model modelo) {
        Map<String, List<Map<String, Object>>> grupos = new LinkedHashMap<>();
        for (Map<String, Object> ejercicio : algebra.ejercicios()) {
            String grupo = (String) ejercicio.get("grupo");
            grupos.computeIfAbsent(grupo, clave -> new ArrayList<>()).add(ejercicio);
        }
        modelo.addAttribute("grupos", grupos);
        modelo.addAttribute("ruta", "/algebra");
        return "algebra";
    }

    @GetMapping("/algebra/{numero}")
    public String ejercicio(@PathVariable int numero, Model modelo) throws SQLException {
        Map<String, Object> elegido = null;
        for (Map<String, Object> ejercicio : algebra.ejercicios()) {
            if (((Integer) ejercicio.get("numero")) == numero) {
                elegido = ejercicio;
                break;
            }
        }
        if (elegido == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        List<Map<String, Object>> filas = baseDatos.consultar((String) elegido.get("sql"));
        List<String> columnas = filas.isEmpty() ? List.of() : new ArrayList<>(filas.get(0).keySet());
        modelo.addAttribute("ejercicio", elegido);
        modelo.addAttribute("filas", filas);
        modelo.addAttribute("columnas", columnas);
        modelo.addAttribute("ruta", "/algebra/" + numero);
        return "ejercicio";
    }
}
