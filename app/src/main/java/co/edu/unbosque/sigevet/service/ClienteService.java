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
import co.edu.unbosque.sigevet.dto.ClienteFormDTO;
import co.edu.unbosque.sigevet.dto.TelefonoFormDTO;
import co.edu.unbosque.sigevet.exception.ConflictoDatosException;
import co.edu.unbosque.sigevet.exception.DatosInvalidosException;
import co.edu.unbosque.sigevet.util.Validador;
import java.util.HashSet;

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

    // ------------------------------------------------------------------
    // Escritura
    // ------------------------------------------------------------------

    /**
     * Datos del cliente para el formulario de edición.
     *
     * @throws RecursoNoEncontradoException si no existe (404).
     */
    public ClienteFormDTO formulario(int id) {
        ClienteFormDTO form = clienteDAO.formulario(id);
        if (form == null) {
            throw new RecursoNoEncontradoException("No existe el cliente " + id);
        }
        return form;
    }

    /** Valida y crea un cliente con sus teléfonos. */
    public long crear(ClienteFormDTO form) {
        validar(form);
        return clienteDAO.insertar(form);
    }

    /** Valida y actualiza un cliente; sus teléfonos se reemplazan por los del formulario. */
    public void actualizar(int id, ClienteFormDTO form) {
        validar(form);
        if (!clienteDAO.actualizar(id, form)) {
            throw new RecursoNoEncontradoException("No existe el cliente " + id);
        }
    }

    /**
     * Borra un cliente solo si no tiene historial. Las mascotas y las facturas son historia clínica y contable de la
     * red: un cliente con cualquiera de ellas se conserva (la FK de MASCOTA y FACTURA también lo impediría).
     */
    public void eliminar(int id) {
        if (clienteDAO.buscar(id) == null) {
            throw new RecursoNoEncontradoException("No existe el cliente " + id);
        }
        int[] historial = clienteDAO.historial(id);
        if (historial[0] > 0 || historial[1] > 0) {
            throw new ConflictoDatosException("El cliente tiene " + historial[0] + " mascota(s) y " + historial[1]
                    + " factura(s); se conserva para no perder su historial.");
        }
        clienteDAO.eliminar(id);
    }

    /** Revisa cada campo con los mismos límites de la tabla CLIENTE y deja los valores limpios en el formulario. */
    private void validar(ClienteFormDTO form) {
        form.setTipoDocumento(Validador.unoDe(form.getTipoDocumento(), "tipo de documento", "CC", "CE", "PA", "PPT"));
        form.setNumDocumento(Validador.obligatorio(form.getNumDocumento(), "número de documento", 15));
        if (!form.getNumDocumento().matches("^[A-Za-z0-9]+$")) {
            throw new DatosInvalidosException("El número de documento solo puede tener letras y números.");
        }
        form.setNombres(Validador.obligatorio(form.getNombres(), "nombres", 60));
        form.setApellidos(Validador.obligatorio(form.getApellidos(), "apellidos", 60));
        form.setEmail(Validador.correo(form.getEmail(), "correo"));
        form.setDireccion(Validador.obligatorio(form.getDireccion(), "dirección", 120));
        Validador.id(form.getIdCiudad(), "la ciudad");
        if (form.getTelefonos() == null) {
            form.setTelefonos(new ArrayList<TelefonoFormDTO>());
        }
        HashSet<String> vistos = new HashSet<String>();
        for (TelefonoFormDTO telefono : form.getTelefonos()) {
            String numero = telefono.getTelefono() == null ? "" : telefono.getTelefono().replaceAll("[\\s-]", "");
            telefono.setTelefono(Validador.digitos(numero, "teléfono", 7, 20));
            telefono.setTipo(Validador.unoDe(telefono.getTipo(), "tipo de teléfono", "MOVIL", "FIJO", "TRABAJO"));
            if (!vistos.add(telefono.getTelefono())) {
                throw new DatosInvalidosException("El teléfono " + telefono.getTelefono() + " está repetido.");
            }
        }
    }
}
