package co.edu.unbosque.sigevet.service;

import co.edu.unbosque.sigevet.dao.SedeDAO;
import co.edu.unbosque.sigevet.model.Sede;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

/**
 * Servicio de sedes. Lo usan los controladores que necesitan la lista de sedes para llenar el filtro de sus páginas
 * (agenda, facturas, inventario y personal). Capa intermedia entre el controlador y el DAO.
 */
@Service
public class SedeService {
    /** DAO que consulta las sedes en la base de datos. */
    private final SedeDAO sedeDAO;

    /**
     * Constructor con inyección de dependencias: Spring entrega el DAO al crear el servicio.
     *
     * @param sedeDAO DAO de sedes.
     */
    public SedeService(SedeDAO sedeDAO) {
        this.sedeDAO = sedeDAO;
    }

    /**
     * Lista todas las sedes.
     *
     * @return la lista de sedes ordenada por nombre.
     */
    public ArrayList<Sede> listar() {
        return sedeDAO.listar();
    }
}
