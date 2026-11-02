package co.edu.unbosque.sigevet.service;

import co.edu.unbosque.sigevet.dao.CatalogoDAO;
import co.edu.unbosque.sigevet.model.OpcionCatalogo;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

/**
 * Servicio de catálogos: entrega las opciones de los {@code <select>} de los formularios.
 */
@Service
public class CatalogoService {
    /** DAO que lee los catálogos. */
    private final CatalogoDAO catalogoDAO;

    /**
     * @param catalogoDAO DAO de catálogos.
     */
    public CatalogoService(CatalogoDAO catalogoDAO) {
        this.catalogoDAO = catalogoDAO;
    }

    public ArrayList<OpcionCatalogo> ciudades() {
        return catalogoDAO.ciudades();
    }

    public ArrayList<OpcionCatalogo> razas() {
        return catalogoDAO.razas();
    }

    public ArrayList<OpcionCatalogo> servicios() {
        return catalogoDAO.servicios();
    }

    public ArrayList<OpcionCatalogo> medicamentos(Integer sede) {
        return catalogoDAO.medicamentos(sede);
    }

    public ArrayList<OpcionCatalogo> especialidades() {
        return catalogoDAO.especialidades();
    }

    public ArrayList<OpcionCatalogo> veterinarios(int sede) {
        return catalogoDAO.veterinarios(sede);
    }

    public ArrayList<OpcionCatalogo> empleados(int sede) {
        return catalogoDAO.empleados(sede);
    }
}
