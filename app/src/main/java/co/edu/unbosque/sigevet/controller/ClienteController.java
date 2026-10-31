package co.edu.unbosque.sigevet.controller;

import co.edu.unbosque.sigevet.dto.ClienteDetalleDTO;
import co.edu.unbosque.sigevet.service.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador de las páginas de clientes (la "C" del patrón MVC).
 *
 * <p>Recibe las rutas web, le pide los datos al servicio, los coloca en el {@link Model} y devuelve el nombre de la
 * plantilla Thymeleaf (la vista) que los muestra. No contiene SQL ni lógica de negocio.</p>
 */
@Controller
public class ClienteController {
    /** Servicio que entrega los datos de los clientes. */
    private final ClienteService clienteService;

    /**
     * Constructor con inyección de dependencias: Spring entrega el servicio al crear el controlador.
     *
     * @param clienteService servicio de clientes.
     */
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Ruta {@code GET /clientes}: lista los clientes y permite buscarlos por documento o apellido.
     *
     * @param buscar texto de búsqueda (vacío si no se escribió nada).
     * @param model contenedor de datos que se envía a la vista.
     * @return el nombre de la vista {@code clientes}.
     */
    @GetMapping("/clientes")
    public String listar(@RequestParam(defaultValue = "") String buscar, Model model) {
        model.addAttribute("buscar", buscar);
        model.addAttribute("clientes", clienteService.listar(buscar));
        return "clientes";
    }

    /**
     * Ruta {@code GET /clientes/{id}}: muestra un cliente con sus teléfonos, mascotas y facturas.
     *
     * @param id identificador del cliente, tomado de la dirección.
     * @param model contenedor de datos que se envía a la vista.
     * @return el nombre de la vista {@code cliente}.
     */
    @GetMapping("/clientes/{id}")
    public String detalle(@PathVariable int id, Model model) {
        ClienteDetalleDTO detalle = clienteService.detalle(id);
        model.addAttribute("cliente", detalle.getCliente());
        model.addAttribute("telefonos", detalle.getTelefonos());
        model.addAttribute("mascotas", detalle.getMascotas());
        model.addAttribute("facturas", detalle.getFacturas());
        return "cliente";
    }
}
