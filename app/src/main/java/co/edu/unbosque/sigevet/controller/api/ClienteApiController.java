package co.edu.unbosque.sigevet.controller.api;

import co.edu.unbosque.sigevet.dto.ClienteDetalleDTO;
import co.edu.unbosque.sigevet.model.Cliente;
import co.edu.unbosque.sigevet.service.ClienteService;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import co.edu.unbosque.sigevet.dto.ClienteFormDTO;
import co.edu.unbosque.sigevet.dto.IdDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * API REST de clientes. A diferencia de la versión anterior con Thymeleaf, no devuelve el nombre de una vista: devuelve los
 * datos y Spring los convierte a JSON para que los muestre el frontend en Angular.
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteApiController {
    /** Servicio que entrega los datos de los clientes. */
    private final ClienteService clienteService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param clienteService servicio de clientes.
     */
    public ClienteApiController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * {@code GET /api/clientes?buscar=}: lista los clientes, con búsqueda por documento o apellido.
     *
     * @param buscar texto de búsqueda (vacío si no se envía).
     * @return los clientes que coinciden.
     */
    @GetMapping
    public ArrayList<Cliente> listar(@RequestParam(defaultValue = "") String buscar) {
        return clienteService.listar(buscar);
    }

    /**
     * {@code GET /api/clientes/{id}}: un cliente con sus teléfonos, mascotas y facturas.
     *
     * @param id identificador del cliente.
     * @return el detalle del cliente (404 si no existe).
     */
    @GetMapping("/{id}")
    public ClienteDetalleDTO detalle(@PathVariable int id) {
        return clienteService.detalle(id);
    }

    /** {@code GET /api/clientes/{id}/formulario}: datos con identificadores, para editar. */
    @GetMapping("/{id}/formulario")
    public ClienteFormDTO formulario(@PathVariable int id) {
        return clienteService.formulario(id);
    }

    /** {@code POST /api/clientes}: crea un cliente con sus teléfonos. Responde 201 con el id generado. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdDTO crear(@RequestBody ClienteFormDTO form) {
        return new IdDTO(clienteService.crear(form));
    }

    /** {@code PUT /api/clientes/{id}}: actualiza el cliente y reemplaza sus teléfonos. Responde 204. */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void actualizar(@PathVariable int id, @RequestBody ClienteFormDTO form) {
        clienteService.actualizar(id, form);
    }

    /** {@code DELETE /api/clientes/{id}}: borra un cliente sin historial (409 si tiene mascotas o facturas). */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable int id) {
        clienteService.eliminar(id);
    }
}
