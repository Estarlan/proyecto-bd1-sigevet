package co.edu.unbosque.sigevet.service;

import co.edu.unbosque.sigevet.dao.SesionDAO;
import co.edu.unbosque.sigevet.dto.LoginFormDTO;
import co.edu.unbosque.sigevet.dto.SesionDTO;
import co.edu.unbosque.sigevet.exception.DatosInvalidosException;
import co.edu.unbosque.sigevet.exception.NoAutorizadoException;
import org.springframework.stereotype.Service;

/**
 * Reglas del inicio de sesión: valida el formulario y pide al DAO el empleado activo.
 */
@Service
public class SesionService {
    /** DAO que consulta EMPLEADO. */
    private final SesionDAO sesionDAO;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param sesionDAO DAO del inicio de sesión.
     */
    public SesionService(SesionDAO sesionDAO) {
        this.sesionDAO = sesionDAO;
    }

    /**
     * Comprueba el correo y el documento.
     *
     * @param form datos del formulario.
     * @return los datos del empleado que inicia sesión.
     * @throws DatosInvalidosException si falta el correo o el documento (400).
     * @throws NoAutorizadoException si no coinciden con un empleado activo (401).
     */
    public SesionDTO iniciar(LoginFormDTO form) {
        String email = form.getEmail() == null ? "" : form.getEmail().trim().toLowerCase();
        String documento = form.getDocumento() == null ? "" : form.getDocumento().trim();
        if (email.isEmpty() || documento.isEmpty()) {
            throw new DatosInvalidosException("Escriba el correo y el número de documento.");
        }
        SesionDTO sesion = sesionDAO.buscarEmpleadoActivo(email, documento);
        if (sesion == null) {
            // el mismo mensaje en los dos casos: no se revela si el correo existe
            throw new NoAutorizadoException("Correo o documento incorrectos, o el empleado está inactivo.");
        }
        return sesion;
    }
}
