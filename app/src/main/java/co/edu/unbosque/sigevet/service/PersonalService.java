package co.edu.unbosque.sigevet.service;

import co.edu.unbosque.sigevet.dao.PersonalDAO;
import co.edu.unbosque.sigevet.model.Empleado;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

/**
 * Servicio del personal. Entrega la lista de empleados, donde cada uno es un {@link Empleado}, un
 * {@link co.edu.unbosque.sigevet.model.Veterinario} o un {@link co.edu.unbosque.sigevet.model.Auxiliar}
 * (herencia y polimorfismo).
 */
@Service
public class PersonalService {
    /** DAO del personal. */
    private final PersonalDAO personalDAO;

    /**
     * Constructor con inyección de dependencias: Spring entrega el DAO al crear el servicio.
     *
     * @param personalDAO DAO del personal.
     */
    public PersonalService(PersonalDAO personalDAO) {
        this.personalDAO = personalDAO;
    }

    /**
     * Lista el personal de las sedes.
     *
     * @param sede identificador de la sede, o {@code null} para todas.
     * @return los empleados ordenados por sede, apellidos y nombres.
     */
    public ArrayList<Empleado> listar(Integer sede) {
        return personalDAO.listar(sede);
    }
}
