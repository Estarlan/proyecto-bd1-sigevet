package co.edu.unbosque.sigevet.service;

import co.edu.unbosque.sigevet.dao.CitaDAO;
import co.edu.unbosque.sigevet.dao.MascotaDAO;
import co.edu.unbosque.sigevet.model.Cita;
import co.edu.unbosque.sigevet.model.EstadoCita;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import co.edu.unbosque.sigevet.dto.CitaFormDTO;
import co.edu.unbosque.sigevet.exception.ConflictoDatosException;
import co.edu.unbosque.sigevet.exception.DatosInvalidosException;
import co.edu.unbosque.sigevet.exception.RecursoNoEncontradoException;
import co.edu.unbosque.sigevet.model.Mascota;
import co.edu.unbosque.sigevet.util.Validador;
import java.time.LocalDateTime;

/**
 * Servicio de la agenda de citas. Lo usan la página de agenda y la de inicio (próximas citas).
 */
@Service
public class AgendaService {
    /** DAO de citas. */
    private final CitaDAO citaDAO;
    /** DAO de mascotas: para revisar que la mascota esté ACTIVA antes de agendar. */
    private final MascotaDAO mascotaDAO;

    /**
     * Constructor con inyección de dependencias: Spring entrega el DAO al crear el servicio.
     *
     * @param mascotaDAO DAO de mascotas.
     * @param citaDAO DAO de citas.
     */
    public AgendaService(CitaDAO citaDAO, MascotaDAO mascotaDAO) {
        this.citaDAO = citaDAO;
        this.mascotaDAO = mascotaDAO;
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

    // ------------------------------------------------------------------
    // Escritura
    // ------------------------------------------------------------------

    /**
     * Agenda una cita. Se revisa aquí que la fecha sea futura y que la mascota esté ACTIVA; la regla RN-08 (el
     * veterinario labora en la sede) y la cita única por veterinario y hora las hace cumplir la base.
     */
    public long crear(CitaFormDTO form) {
        Validador.id(form.getIdMascota(), "la mascota");
        Validador.id(form.getIdServicio(), "el servicio");
        Validador.id(form.getIdSede(), "la sede");
        Validador.id(form.getIdVeterinario(), "el veterinario");
        LocalDateTime fechaHora = Validador.fechaHora(form.getFechaHora(), "fecha y hora");
        if (!fechaHora.isAfter(LocalDateTime.now())) {
            throw new DatosInvalidosException("La cita debe ser en una fecha y hora futuras.");
        }
        if (fechaHora.isAfter(LocalDateTime.now().plusYears(1))) {
            throw new DatosInvalidosException("La cita no puede agendarse con más de un año de anticipación.");
        }
        form.setMotivo(Validador.textoLibre(form.getMotivo(), "motivo", 200));
        Mascota mascota = mascotaDAO.buscar(form.getIdMascota());
        if (mascota == null) {
            throw new DatosInvalidosException("La mascota seleccionada no existe.");
        }
        if (!"ACTIVA".equals(mascota.getEstado())) {
            throw new ConflictoDatosException(mascota.getNombre() + " está " + mascota.getEstado()
                    + "; solo se agendan citas para mascotas ACTIVAS.");
        }
        return citaDAO.insertar(form, fechaHora);
    }

    /** Cambia el estado de una cita PROGRAMADA (atendida, cancelada o no asistió). */
    public void cambiarEstado(long id, String estado) {
        Validador.unoDe(estado, "estado", "ATENDIDA", "CANCELADA", "NO_ASISTIO");
        if (!citaDAO.cambiarEstado(id, estado)) {
            throw new RecursoNoEncontradoException("No existe la cita " + id);
        }
    }
}
