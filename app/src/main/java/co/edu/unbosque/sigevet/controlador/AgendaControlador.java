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
public class AgendaControlador {
    @Autowired
    private CitaDAO citaDAO;

    @Autowired
    private SedeDAO sedeDAO;

    @GetMapping("/agenda")
    public String listar(@RequestParam(required = false) Integer sede,
                         @RequestParam(required = false) String estado, Model model) {
        if (estado != null && estado.isBlank()) {
            estado = null;
        }
        model.addAttribute("sedeSeleccionada", sede);
        model.addAttribute("estadoSeleccionado", estado);
        model.addAttribute("estados", new String[] {"PROGRAMADA", "ATENDIDA", "CANCELADA", "NO_ASISTIO"});
        model.addAttribute("sedes", sedeDAO.listar());
        model.addAttribute("citas", citaDAO.listar(sede, estado));
        return "agenda";
    }
}
