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
public class MascotaControlador {
    @Autowired
    private MascotaDAO mascotaDAO;

    @Autowired
    private ConsultaDAO consultaDAO;

    @GetMapping("/mascotas/{id}")
    public String detalle(@PathVariable int id, Model model) {
        Mascota mascota = mascotaDAO.buscar(id);
        if (mascota == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        ArrayList<Consulta> consultas = consultaDAO.listarPorMascota(id);
        ArrayList<Prescripcion> prescripciones = consultaDAO.prescripciones(id);
        for (Consulta consulta : consultas) {
            for (Prescripcion prescripcion : prescripciones) {
                if (consulta.getIdConsulta() == prescripcion.getIdConsulta()) {
                    consulta.getPrescripciones().add(prescripcion);
                }
            }
        }
        model.addAttribute("mascota", mascota);
        model.addAttribute("historia", mascotaDAO.historia(id));
        model.addAttribute("consultas", consultas);
        return "mascota";
    }
}
