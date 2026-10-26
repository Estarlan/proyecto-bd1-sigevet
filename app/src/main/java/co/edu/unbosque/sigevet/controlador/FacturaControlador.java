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
public class FacturaControlador {
    @Autowired
    private FacturaDAO facturaDAO;

    @Autowired
    private DetalleFacturaDAO detalleFacturaDAO;

    @Autowired
    private SedeDAO sedeDAO;

    @GetMapping("/facturas")
    public String listar(@RequestParam(required = false) Integer sede,
                         @RequestParam(required = false) String estado, Model model) {
        if (estado != null && estado.isBlank()) {
            estado = null;
        }
        model.addAttribute("sedeSeleccionada", sede);
        model.addAttribute("estadoSeleccionado", estado);
        model.addAttribute("estados", new String[] {"PAGADA", "PENDIENTE", "ANULADA"});
        model.addAttribute("sedes", sedeDAO.listar());
        model.addAttribute("facturas", facturaDAO.listar(sede, estado));
        return "facturas";
    }

    @GetMapping("/facturas/{id}")
    public String detalle(@PathVariable long id, Model model) {
        Factura factura = facturaDAO.buscar(id);
        if (factura == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        model.addAttribute("factura", factura);
        model.addAttribute("lineas", detalleFacturaDAO.listar(id));
        return "factura";
    }
}
