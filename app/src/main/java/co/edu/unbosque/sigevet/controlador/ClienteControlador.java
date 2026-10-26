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
public class ClienteControlador {
    @Autowired
    private ClienteDAO clienteDAO;

    @Autowired
    private MascotaDAO mascotaDAO;

    @Autowired
    private FacturaDAO facturaDAO;

    @GetMapping("/clientes")
    public String listar(@RequestParam(required = false) String buscar, Model model) {
        if (buscar == null) {
            buscar = "";
        }
        model.addAttribute("buscar", buscar);
        model.addAttribute("clientes", clienteDAO.listar(buscar.trim()));
        return "clientes";
    }

    @GetMapping("/clientes/{id}")
    public String detalle(@PathVariable int id, Model model) {
        Cliente cliente = clienteDAO.buscar(id);
        if (cliente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        model.addAttribute("cliente", cliente);
        model.addAttribute("telefonos", clienteDAO.telefonos(id));
        model.addAttribute("mascotas", mascotaDAO.listarPorCliente(id));
        model.addAttribute("facturas", facturaDAO.listarPorCliente(id));
        return "cliente";
    }
}
