package co.edu.unbosque.sigevet.service;

import co.edu.unbosque.sigevet.dao.InicioDAO;
import co.edu.unbosque.sigevet.model.Resumen;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

/**
 * Servicio de la página de inicio: entrega los resúmenes generales del sistema (conteos, ingresos y citas por
 * estado).
 */
@Service
public class InicioService {
    /** DAO de los resúmenes de la página de inicio. */
    private final InicioDAO inicioDAO;

    /**
     * Constructor con inyección de dependencias: Spring entrega el DAO al crear el servicio.
     *
     * @param inicioDAO DAO de los resúmenes.
     */
    public InicioService(InicioDAO inicioDAO) {
        this.inicioDAO = inicioDAO;
    }

    /**
     * Cuenta los registros de cada tabla del modelo.
     *
     * @return un resumen por tabla con su cantidad de registros.
     */
    public ArrayList<Resumen> conteos() {
        return inicioDAO.conteos();
    }

    /**
     * Calcula los ingresos por sede de las facturas pagadas.
     *
     * @return un resumen por sede con su total de ingresos.
     */
    public ArrayList<Resumen> ingresos() {
        return inicioDAO.ingresos();
    }

    /**
     * Cuenta las citas agrupadas por estado.
     *
     * @return un resumen por estado con la cantidad de citas.
     */
    public ArrayList<Resumen> estadosCitas() {
        return inicioDAO.estadosCitas();
    }
}
