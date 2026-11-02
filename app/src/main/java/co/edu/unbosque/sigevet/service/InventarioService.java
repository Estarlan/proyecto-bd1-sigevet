package co.edu.unbosque.sigevet.service;

import co.edu.unbosque.sigevet.dao.InventarioDAO;
import co.edu.unbosque.sigevet.model.ItemInventario;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import co.edu.unbosque.sigevet.dto.InventarioFormDTO;
import co.edu.unbosque.sigevet.exception.RecursoNoEncontradoException;
import co.edu.unbosque.sigevet.util.Validador;

/**
 * Servicio del inventario de medicamentos. Lo usan la página de inventario y la de inicio (alerta de stock bajo).
 */
@Service
public class InventarioService {
    /** DAO del inventario. */
    private final InventarioDAO inventarioDAO;

    /**
     * Constructor con inyección de dependencias: Spring entrega el DAO al crear el servicio.
     *
     * @param inventarioDAO DAO del inventario.
     */
    public InventarioService(InventarioDAO inventarioDAO) {
        this.inventarioDAO = inventarioDAO;
    }

    /**
     * Lista las existencias de medicamentos.
     *
     * @param sede identificador de la sede, o {@code null} para todas.
     * @return las existencias ordenadas por sede y medicamento.
     */
    public ArrayList<ItemInventario> listar(Integer sede) {
        return inventarioDAO.listar(sede);
    }

    /**
     * Lista los medicamentos con stock por debajo del mínimo.
     *
     * @return las existencias que necesitan reposición.
     */
    public ArrayList<ItemInventario> stockBajo() {
        return inventarioDAO.stockBajo();
    }

    // ------------------------------------------------------------------
    // Escritura
    // ------------------------------------------------------------------

    /** Agrega un medicamento al inventario de una sede. */
    public void agregar(InventarioFormDTO form) {
        Validador.id(form.getIdSede(), "la sede");
        Validador.id(form.getIdMedicamento(), "el medicamento");
        validarExistencias(form);
        inventarioDAO.insertar(form);
    }

    /** Cambia las existencias y el mínimo de un medicamento en una sede. */
    public void actualizar(int idSede, int idMedicamento, InventarioFormDTO form) {
        validarExistencias(form);
        if (!inventarioDAO.actualizar(idSede, idMedicamento, form.getStockActual(), form.getStockMinimo())) {
            throw new RecursoNoEncontradoException("La sede " + idSede + " no tiene el medicamento " + idMedicamento);
        }
    }

    /** Quita un medicamento del inventario de una sede. */
    public void eliminar(int idSede, int idMedicamento) {
        if (!inventarioDAO.eliminar(idSede, idMedicamento)) {
            throw new RecursoNoEncontradoException("La sede " + idSede + " no tiene el medicamento " + idMedicamento);
        }
    }

    /** Las existencias no pueden ser negativas (CK_INVENTARIO_STOCK_ACTUAL y CK_INVENTARIO_STOCK_MINIMO). */
    private void validarExistencias(InventarioFormDTO form) {
        Validador.entero(form.getStockActual(), "stock actual", 0, 1000000);
        Validador.entero(form.getStockMinimo(), "stock mínimo", 0, 1000000);
    }
}
