package co.edu.unbosque.sigevet.service;

import co.edu.unbosque.sigevet.dao.InventarioDAO;
import co.edu.unbosque.sigevet.model.ItemInventario;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

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
}
