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
public class PersonalControlador {
    @Autowired
    private PersonalDAO personalDAO;

    @Autowired
    private SedeDAO sedeDAO;

    @GetMapping("/personal")
    public String listar(@RequestParam(required = false) Integer sede, Model model) {
        ArrayList<Empleado> empleados = personalDAO.listar(sede);
        model.addAttribute("empleados", empleados);
        model.addAttribute("sedes", sedeDAO.listar());
        model.addAttribute("sedeSeleccionada", sede);
        return "personal";
    }
}
