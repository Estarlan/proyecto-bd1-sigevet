package co.edu.unbosque.sigevet.service;

import co.edu.unbosque.sigevet.dao.ClienteDAO;
import co.edu.unbosque.sigevet.dao.FacturaDAO;
import co.edu.unbosque.sigevet.dao.MascotaDAO;
import co.edu.unbosque.sigevet.dto.ClienteDetalleDTO;
import co.edu.unbosque.sigevet.exception.RecursoNoEncontradoException;
import co.edu.unbosque.sigevet.model.Cliente;
import co.edu.unbosque.sigevet.model.Factura;
import co.edu.unbosque.sigevet.model.Mascota;
import co.edu.unbosque.sigevet.model.Telefono;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

/**
 * Servicio de clientes. Concentra la lógica de las páginas de clientes: coordina varios DAO y arma los datos que
 * necesita el controlador, para que este no tenga que conocer la base de datos.
 */
@Service
public class ClienteService {
    /** DAO de clientes y sus teléfonos. */
    private final ClienteDAO clienteDAO;
    /** DAO de mascotas. */
    private final MascotaDAO mascotaDAO;
    /** DAO de facturas. */
    private final FacturaDAO facturaDAO;

    /**
     * Constructor con inyección de dependencias: Spring entrega los DAO al crear el servicio.
     *
     * @param clienteDAO DAO de clientes.
     * @param mascotaDAO DAO de mascotas.
     * @param facturaDAO DAO de facturas.
     */
    public ClienteService(ClienteDAO clienteDAO, MascotaDAO mascotaDAO, FacturaDAO facturaDAO) {
        this.clienteDAO = clienteDAO;
        this.mascotaDAO = mascotaDAO;
        this.facturaDAO = facturaDAO;
    }

    /**
     * Busca clientes por documento o apellido.
     *
     * @param buscar texto a buscar; se le quitan los espacios de los extremos.
     * @return los clientes que coinciden (máximo 100).
     */
    public ArrayList<Cliente> listar(String buscar) {
        return clienteDAO.listar(buscar.trim());
    }

    /**
     * Reúne todo lo que muestra la página de un cliente: sus datos, teléfonos, mascotas y facturas.
     *
     * @param id identificador del cliente.
     * @return un DTO con los datos del cliente.
     * @throws RecursoNoEncontradoException si no existe un cliente con ese identificador (se muestra un 404).
     */
    public ClienteDetalleDTO detalle(int id) {
        Cliente cliente = clienteDAO.buscar(id);
        if (cliente == null) {
            throw new RecursoNoEncontradoException("No existe el cliente " + id);
        }
        ArrayList<Telefono> telefonos = clienteDAO.telefonos(id);
        ArrayList<Mascota> mascotas = mascotaDAO.listarPorCliente(id);
        ArrayList<Factura> facturas = facturaDAO.listarPorCliente(id);
        return new ClienteDetalleDTO(cliente, telefonos, mascotas, facturas);
    }
}
