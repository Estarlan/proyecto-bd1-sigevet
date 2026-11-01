package co.edu.unbosque.sigevet.service;

import co.edu.unbosque.sigevet.dao.CitaDAO;
import co.edu.unbosque.sigevet.model.Cita;
import co.edu.unbosque.sigevet.model.EstadoCita;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

/**
 * Servicio de la agenda de citas. Lo usan la página de agenda y la de inicio (próximas citas).
 */
@Service
public class AgendaService {
    /** DAO de citas. */
    private final CitaDAO citaDAO;

    /**
     * Constructor con inyección de dependencias: Spring entrega el DAO al crear el servicio.
     *
     * @param citaDAO DAO de citas.
     */
    public AgendaService(CitaDAO citaDAO) {
        this.citaDAO = citaDAO;
    }

    /**
     * Lista las citas con filtros opcionales.
     *
     * @param sede identificador de la sede, o {@code null} para todas.
     * @param estado estado de la cita, o {@code null} para todos.
     * @return las citas que cumplen los filtros, ordenadas por fecha.
     */
    public ArrayList<Cita> listar(Integer sede, String estado) {
        return citaDAO.listar(sede, estado);
    }

    /**
     * Lista las próximas citas programadas.
     *
     * @return hasta 10 citas programadas con fecha futura.
     */
    public ArrayList<Cita> proximas() {
        return citaDAO.proximas();
    }

    /**
     * Da los estados que se ofrecen en el filtro de la agenda.
     *
     * @return los nombres de los estados de una cita.
     */
    public String[] estados() {
        return EstadoCita.nombres();
    }
}
