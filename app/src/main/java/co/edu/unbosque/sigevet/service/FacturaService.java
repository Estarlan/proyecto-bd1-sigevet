package co.edu.unbosque.sigevet.service;

import co.edu.unbosque.sigevet.dao.DetalleFacturaDAO;
import co.edu.unbosque.sigevet.dao.FacturaDAO;
import co.edu.unbosque.sigevet.dto.FacturaDetalleDTO;
import co.edu.unbosque.sigevet.exception.RecursoNoEncontradoException;
import co.edu.unbosque.sigevet.model.DetalleFactura;
import co.edu.unbosque.sigevet.model.EstadoFactura;
import co.edu.unbosque.sigevet.model.Factura;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

/**
 * Servicio de facturas. Arma la información de las páginas de facturas: el listado con filtros y el detalle de una
 * factura con sus líneas.
 */
@Service
public class FacturaService {
    /** DAO de facturas. */
    private final FacturaDAO facturaDAO;
    /** DAO de las líneas de las facturas. */
    private final DetalleFacturaDAO detalleFacturaDAO;

    /**
     * Constructor con inyección de dependencias: Spring entrega los DAO al crear el servicio.
     *
     * @param facturaDAO DAO de facturas.
     * @param detalleFacturaDAO DAO de las líneas de factura.
     */
    public FacturaService(FacturaDAO facturaDAO, DetalleFacturaDAO detalleFacturaDAO) {
        this.facturaDAO = facturaDAO;
        this.detalleFacturaDAO = detalleFacturaDAO;
    }

    /**
     * Lista las facturas con filtros opcionales.
     *
     * @param sede identificador de la sede, o {@code null} para todas.
     * @param estado estado de la factura, o {@code null} para todos.
     * @return las facturas que cumplen los filtros, de la más reciente a la más antigua.
     */
    public ArrayList<Factura> listar(Integer sede, String estado) {
        return facturaDAO.listar(sede, estado);
    }

    /**
     * Da los estados que se ofrecen en el filtro de facturas.
     *
     * @return los nombres de los estados de una factura.
     */
    public String[] estados() {
        return EstadoFactura.nombres();
    }

    /**
     * Reúne todo lo que muestra la página de una factura: la factura y sus líneas.
     *
     * @param id identificador de la factura.
     * @return un DTO con la factura y sus líneas.
     * @throws RecursoNoEncontradoException si no existe una factura con ese identificador (se muestra un 404).
     */
    public FacturaDetalleDTO detalle(long id) {
        Factura factura = facturaDAO.buscar(id);
        if (factura == null) {
            throw new RecursoNoEncontradoException("No existe la factura " + id);
        }
        ArrayList<DetalleFactura> lineas = detalleFacturaDAO.listar(id);
        return new FacturaDetalleDTO(factura, lineas);
    }
}
