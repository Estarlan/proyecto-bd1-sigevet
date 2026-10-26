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
public class InicioControlador {
    @Autowired
    private InicioDAO inicioDAO;

    @Autowired
    private CitaDAO citaDAO;

    @Autowired
    private InventarioDAO inventarioDAO;

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("conteos", inicioDAO.conteos());
        model.addAttribute("ingresos", inicioDAO.ingresos());
        model.addAttribute("estadosCitas", inicioDAO.estadosCitas());
        model.addAttribute("stockBajo", inventarioDAO.stockBajo());
        model.addAttribute("proximas", citaDAO.proximas());
        return "inicio";
    }
}
