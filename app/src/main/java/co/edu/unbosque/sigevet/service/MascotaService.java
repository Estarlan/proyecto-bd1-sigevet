package co.edu.unbosque.sigevet.service;

import co.edu.unbosque.sigevet.dao.ConsultaDAO;
import co.edu.unbosque.sigevet.dao.MascotaDAO;
import co.edu.unbosque.sigevet.dto.MascotaDetalleDTO;
import co.edu.unbosque.sigevet.exception.RecursoNoEncontradoException;
import co.edu.unbosque.sigevet.model.Consulta;
import co.edu.unbosque.sigevet.model.HistoriaClinica;
import co.edu.unbosque.sigevet.model.Mascota;
import co.edu.unbosque.sigevet.model.Prescripcion;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

/**
 * Servicio de mascotas. Arma la información de la página de una mascota: sus datos, su historia clínica y su
 * historial de consultas con los medicamentos recetados en cada una.
 */
@Service
public class MascotaService {
    /** DAO de mascotas e historias clínicas. */
    private final MascotaDAO mascotaDAO;
    /** DAO de consultas y prescripciones. */
    private final ConsultaDAO consultaDAO;

    /**
     * Constructor con inyección de dependencias: Spring entrega los DAO al crear el servicio.
     *
     * @param mascotaDAO DAO de mascotas.
     * @param consultaDAO DAO de consultas.
     */
    public MascotaService(MascotaDAO mascotaDAO, ConsultaDAO consultaDAO) {
        this.mascotaDAO = mascotaDAO;
        this.consultaDAO = consultaDAO;
    }

    /**
     * Reúne todo lo que muestra la página de una mascota.
     *
     * <p>Las consultas y las prescripciones se leen por separado (dos consultas SQL) y aquí se asigna cada
     * prescripción a la consulta a la que pertenece, comparando el identificador de la consulta.</p>
     *
     * @param id identificador de la mascota.
     * @return un DTO con la mascota, su historia clínica y sus consultas con prescripciones.
     * @throws RecursoNoEncontradoException si no existe una mascota con ese identificador (se muestra un 404).
     */
    public MascotaDetalleDTO detalle(int id) {
        Mascota mascota = mascotaDAO.buscar(id);
        if (mascota == null) {
            throw new RecursoNoEncontradoException("No existe la mascota " + id);
        }
        ArrayList<Consulta> consultas = consultaDAO.listarPorMascota(id);
        ArrayList<Prescripcion> prescripciones = consultaDAO.prescripciones(id);
        // cada prescripción se asigna a la consulta a la que pertenece
        for (Consulta consulta : consultas) {
            for (Prescripcion prescripcion : prescripciones) {
                if (consulta.getIdConsulta() == prescripcion.getIdConsulta()) {
                    consulta.getPrescripciones().add(prescripcion);
                }
            }
        }
        HistoriaClinica historia = mascotaDAO.historia(id);
        return new MascotaDetalleDTO(mascota, historia, consultas);
    }
}
