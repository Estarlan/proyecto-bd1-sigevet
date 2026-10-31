package co.edu.unbosque.sigevet.service;

import co.edu.unbosque.sigevet.algebra.LectorAlgebra;
import co.edu.unbosque.sigevet.dao.AlgebraDAO;
import co.edu.unbosque.sigevet.dto.EjercicioDetalleDTO;
import co.edu.unbosque.sigevet.exception.RecursoNoEncontradoException;
import co.edu.unbosque.sigevet.model.EjercicioAlgebra;
import co.edu.unbosque.sigevet.model.ResultadoConsulta;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

/**
 * Servicio de los ejercicios de álgebra relacional. Combina el lector del script SQL con el DAO que ejecuta cada
 * consulta, para que el controlador reciba el ejercicio y su resultado ya listos.
 */
@Service
public class AlgebraService {
    /** Lee los ejercicios desde el script SQL del proyecto. */
    private final LectorAlgebra lectorAlgebra;
    /** Ejecuta la consulta de un ejercicio en la base de datos. */
    private final AlgebraDAO algebraDAO;

    /**
     * Constructor con inyección de dependencias: Spring entrega el lector y el DAO al crear el servicio.
     *
     * @param lectorAlgebra lector del script de ejercicios.
     * @param algebraDAO DAO que ejecuta las consultas de los ejercicios.
     */
    public AlgebraService(LectorAlgebra lectorAlgebra, AlgebraDAO algebraDAO) {
        this.lectorAlgebra = lectorAlgebra;
        this.algebraDAO = algebraDAO;
    }

    /**
     * Lista todos los ejercicios del script.
     *
     * @return los ejercicios en el orden en que aparecen en el archivo.
     */
    public ArrayList<EjercicioAlgebra> listar() {
        return lectorAlgebra.listar();
    }

    /**
     * Obtiene los grupos temáticos distintos, en el orden en que aparecen los ejercicios.
     *
     * @param ejercicios lista de ejercicios de la que se sacan los grupos.
     * @return los nombres de los grupos sin repetir.
     */
    public ArrayList<String> grupos(ArrayList<EjercicioAlgebra> ejercicios) {
        ArrayList<String> grupos = new ArrayList<String>();
        for (EjercicioAlgebra ejercicio : ejercicios) {
            if (!grupos.contains(ejercicio.getGrupo())) {
                grupos.add(ejercicio.getGrupo());
            }
        }
        return grupos;
    }

    /**
     * Busca un ejercicio por su número y ejecuta su consulta SQL.
     *
     * @param numero número del ejercicio.
     * @return un DTO con el ejercicio y el resultado de su consulta.
     * @throws RecursoNoEncontradoException si no existe un ejercicio con ese número (se muestra un 404).
     */
    public EjercicioDetalleDTO detalle(int numero) {
        EjercicioAlgebra ejercicio = lectorAlgebra.buscar(numero);
        if (ejercicio == null) {
            throw new RecursoNoEncontradoException("No existe el ejercicio " + numero);
        }
        ResultadoConsulta resultado = algebraDAO.ejecutar(ejercicio.getSql());
        return new EjercicioDetalleDTO(ejercicio, resultado);
    }
}
