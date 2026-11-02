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
import co.edu.unbosque.sigevet.dto.FacturaFormDTO;
import co.edu.unbosque.sigevet.dto.LineaFacturaFormDTO;
import co.edu.unbosque.sigevet.exception.DatosInvalidosException;
import co.edu.unbosque.sigevet.util.Validador;
import java.math.BigDecimal;

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

    // ------------------------------------------------------------------
    // Escritura
    // ------------------------------------------------------------------

    /** Valida y crea la factura con todas sus líneas (una transacción en el DAO). */
    public long crear(FacturaFormDTO form) {
        Validador.id(form.getIdCliente(), "el cliente");
        Validador.id(form.getIdSede(), "la sede");
        Validador.id(form.getIdEmpleado(), "el empleado que emite");
        Validador.unoDe(form.getMetodoPago(), "método de pago", "EFECTIVO", "TARJETA_DEBITO", "TARJETA_CREDITO", "TRANSFERENCIA");
        Validador.unoDe(form.getEstado(), "estado", "PAGADA", "PENDIENTE");
        if (form.getLineas() == null || form.getLineas().isEmpty()) {
            throw new DatosInvalidosException("La factura debe tener al menos una línea.");
        }
        if (form.getLineas().size() > 50) {
            throw new DatosInvalidosException("La factura admite máximo 50 líneas.");
        }
        int numero = 1;
        for (LineaFacturaFormDTO linea : form.getLineas()) {
            // arco exclusivo (CK_DETALLE_FACTURA_ITEM): un servicio O un medicamento, nunca los dos
            boolean tieneServicio = linea.getIdServicio() != null;
            boolean tieneMedicamento = linea.getIdMedicamento() != null;
            if (tieneServicio == tieneMedicamento) {
                throw new DatosInvalidosException("La línea " + numero + " debe cobrar un servicio o un medicamento.");
            }
            Validador.entero(linea.getCantidad(), "cantidad de la línea " + numero, 1, 1000);
            if (linea.getDescuentoPct() == null) {
                linea.setDescuentoPct(BigDecimal.ZERO);
            }
            // DECIMAL(5,2) y CK_DETALLE_FACTURA_DESCUENTO_PCT: de 0 a 100 con máximo dos decimales
            Validador.decimalEntre(linea.getDescuentoPct(), "descuento de la línea " + numero, BigDecimal.ZERO, new BigDecimal("100"));
            numero++;
        }
        return facturaDAO.insertar(form);
    }

    /** Registra el pago (PAGADA) o anula (ANULADA) una factura. */
    public void cambiarEstado(long id, String estado) {
        Validador.unoDe(estado, "estado", "PAGADA", "ANULADA");
        if (!facturaDAO.cambiarEstado(id, estado)) {
            throw new RecursoNoEncontradoException("No existe la factura " + id);
        }
    }
}
