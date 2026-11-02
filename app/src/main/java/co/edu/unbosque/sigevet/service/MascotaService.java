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
import co.edu.unbosque.sigevet.dto.MascotaFormDTO;
import co.edu.unbosque.sigevet.exception.DatosInvalidosException;
import co.edu.unbosque.sigevet.util.Validador;
import java.time.LocalDate;

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

    // ------------------------------------------------------------------
    // Lista y escritura
    // ------------------------------------------------------------------

    /** Busca mascotas por nombre, apellido del propietario o microchip. */
    public ArrayList<Mascota> listar(String buscar) {
        return mascotaDAO.listar(buscar == null ? "" : buscar.trim());
    }

    /**
     * Datos de la mascota y su historia para el formulario de edición.
     *
     * @throws RecursoNoEncontradoException si no existe (404).
     */
    public MascotaFormDTO formulario(int id) {
        MascotaFormDTO form = mascotaDAO.formulario(id);
        if (form == null) {
            throw new RecursoNoEncontradoException("No existe la mascota " + id);
        }
        return form;
    }

    /** Valida y crea la mascota con su historia clínica. */
    public long crear(MascotaFormDTO form) {
        validar(form);
        return mascotaDAO.insertar(form);
    }

    /** Valida y actualiza la mascota y su historia clínica. */
    public void actualizar(int id, MascotaFormDTO form) {
        validar(form);
        if (!mascotaDAO.actualizar(id, form)) {
            throw new RecursoNoEncontradoException("No existe la mascota " + id);
        }
    }

    /**
     * Borrado lógico de la mascota: cambia su estado y, si deja de estar ACTIVA, cancela sus citas programadas.
     *
     * @return mensaje con el resultado para mostrar.
     */
    public String cambiarEstado(int id, String estado) {
        Validador.unoDe(estado, "estado", "ACTIVA", "FALLECIDA", "TRASLADADA");
        int canceladas = mascotaDAO.cambiarEstado(id, estado);
        if (canceladas < 0) {
            throw new RecursoNoEncontradoException("No existe la mascota " + id);
        }
        String mensaje = "La mascota quedó en estado " + estado + ".";
        if (canceladas > 0) {
            mensaje += " Se cancelaron " + canceladas + " cita(s) programada(s).";
        }
        return mensaje;
    }

    /** Revisa cada campo con los límites y restricciones CHECK de MASCOTA e HISTORIA_CLINICA. */
    private void validar(MascotaFormDTO form) {
        form.setNombre(Validador.obligatorio(form.getNombre(), "nombre", 40));
        if (form.getFechaNacimiento() == null || form.getFechaNacimiento().isBlank()) {
            form.setFechaNacimiento(null);
        } else if (Validador.fecha(form.getFechaNacimiento(), "fecha de nacimiento").isAfter(LocalDate.now())) {
            throw new DatosInvalidosException("La fecha de nacimiento no puede ser futura.");
        }
        form.setSexo(Validador.unoDe(form.getSexo(), "sexo", "M", "H"));
        form.setColor(Validador.opcional(form.getColor(), "color", 40));
        form.setMicrochip(Validador.opcional(form.getMicrochip(), "microchip", 15));
        if (form.getMicrochip() != null) {
            Validador.digitos(form.getMicrochip(), "microchip", 15, 15);
        }
        form.setEsterilizado(Validador.unoDe(form.getEsterilizado(), "esterilizado", "S", "N"));
        Validador.id(form.getIdRaza(), "la raza");
        Validador.id(form.getIdCliente(), "el propietario");
        form.setGrupoSanguineo(Validador.opcional(form.getGrupoSanguineo(), "grupo sanguíneo", 10));
        form.setAlergias(Validador.opcional(form.getAlergias(), "alergias", 200));
        form.setEnfermedadesCronicas(Validador.opcional(form.getEnfermedadesCronicas(), "enfermedades crónicas", 200));
        form.setObservaciones(Validador.opcional(form.getObservaciones(), "observaciones", 400));
    }
}
