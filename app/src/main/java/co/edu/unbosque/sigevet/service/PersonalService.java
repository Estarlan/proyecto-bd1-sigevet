package co.edu.unbosque.sigevet.service;

import co.edu.unbosque.sigevet.dao.CitaDAO;
import co.edu.unbosque.sigevet.dao.PersonalDAO;
import co.edu.unbosque.sigevet.model.Empleado;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import co.edu.unbosque.sigevet.dto.EmpleadoFormDTO;
import co.edu.unbosque.sigevet.exception.ConflictoDatosException;
import co.edu.unbosque.sigevet.exception.DatosInvalidosException;
import co.edu.unbosque.sigevet.exception.RecursoNoEncontradoException;
import co.edu.unbosque.sigevet.util.Validador;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;

/**
 * Servicio del personal. Entrega la lista de empleados, donde cada uno es un {@link Empleado}, un
 * {@link co.edu.unbosque.sigevet.model.Veterinario} o un {@link co.edu.unbosque.sigevet.model.Auxiliar}
 * (herencia y polimorfismo).
 */
@Service
public class PersonalService {
    /** DAO del personal. */
    private final PersonalDAO personalDAO;
    /** DAO de citas: un veterinario con citas no puede cambiar de sede (RN-08) ni inactivarse con citas programadas. */
    private final CitaDAO citaDAO;

    /**
     * Constructor con inyección de dependencias: Spring entrega el DAO al crear el servicio.
     *
     * @param citaDAO DAO de citas.
     * @param personalDAO DAO del personal.
     */
    public PersonalService(PersonalDAO personalDAO, CitaDAO citaDAO) {
        this.personalDAO = personalDAO;
        this.citaDAO = citaDAO;
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

    // ------------------------------------------------------------------
    // Escritura
    // ------------------------------------------------------------------

    /**
     * Datos del empleado y de su subtipo para el formulario de edición.
     *
     * @throws RecursoNoEncontradoException si no existe (404).
     */
    public EmpleadoFormDTO formulario(int id) {
        EmpleadoFormDTO form = personalDAO.formulario(id);
        if (form == null) {
            throw new RecursoNoEncontradoException("No existe el empleado " + id);
        }
        return form;
    }

    /** Valida y crea el empleado con su subtipo y especialidades. */
    public long crear(EmpleadoFormDTO form) {
        validar(form, null);
        return personalDAO.insertar(form);
    }

    /**
     * Actualiza un empleado. No se permite cambiar el tipo (lo impide también TRG_EMPLEADO_TIPO). Un veterinario con
     * citas no puede cambiar de sede: el disparador TRG_CITA_UPD revisa la regla RN-08 cada vez que se modifica una
     * cita, así que después no se podría marcar ninguna de sus citas como atendida o cancelada.
     */
    public void actualizar(int id, EmpleadoFormDTO form) {
        EmpleadoFormDTO actual = formulario(id);
        if (form.getTipoEmpleado() == null || !form.getTipoEmpleado().equals(actual.getTipoEmpleado())) {
            throw new DatosInvalidosException("El tipo de empleado no se puede cambiar (regla RN-09).");
        }
        validar(form, actual.getAnioGraduacion());
        if (!form.getIdSede().equals(actual.getIdSede())) {
            if ("VET".equals(actual.getTipoEmpleado()) && citaDAO.contarPorVeterinario(id, false) > 0) {
                throw new ConflictoDatosException("El veterinario tiene citas registradas en su sede; si cambia de sede, "
                        + "esas citas violarían la regla RN-08. Registre un empleado nuevo en la otra sede.");
            }
            String dirige = personalDAO.sedeQueDirige(id);
            if (dirige != null) {
                throw new ConflictoDatosException("El empleado dirige la sede " + dirige + "; no puede trasladarse a otra sede.");
            }
        }
        personalDAO.actualizar(id, form);
    }

    /**
     * Borrado lógico: ACTIVO o INACTIVO. No se inactiva a un director de sede ni a un veterinario con citas
     * programadas, para no dejar sedes sin director ni citas sin médico.
     */
    public void cambiarEstado(int id, String estado) {
        Validador.unoDe(estado, "estado", "ACTIVO", "INACTIVO");
        EmpleadoFormDTO actual = formulario(id);
        if ("INACTIVO".equals(estado)) {
            String dirige = personalDAO.sedeQueDirige(id);
            if (dirige != null) {
                throw new ConflictoDatosException("El empleado dirige la sede " + dirige + "; asigne otro director antes de inactivarlo.");
            }
            if ("VET".equals(actual.getTipoEmpleado())) {
                int programadas = citaDAO.contarPorVeterinario(id, true);
                if (programadas > 0) {
                    throw new ConflictoDatosException("El veterinario tiene " + programadas
                            + " cita(s) programada(s); cancélelas antes de inactivarlo.");
                }
            }
        }
        personalDAO.cambiarEstado(id, estado);
    }

    /**
     * Revisa los datos de EMPLEADO y los del subtipo según tipo_empleado; borra los campos que no aplican.
     *
     * @param form datos del formulario.
     * @param anioGuardado año de graduación que ya tiene el veterinario en la base (null al crear). Si no cambia, no
     *                     se le exige la regla de los 18 años: algunos datos de prueba no la cumplen y el ejercicio 15
     *                     de álgebra depende de ellos, así que se pueden editar sin tocar ese año.
     */
    private void validar(EmpleadoFormDTO form, Integer anioGuardado) {
        form.setTipoDocumento(Validador.unoDe(form.getTipoDocumento(), "tipo de documento", "CC", "CE", "PA", "PPT"));
        form.setNumDocumento(Validador.documento(form.getTipoDocumento(), form.getNumDocumento()));
        form.setNombres(Validador.nombrePersona(form.getNombres(), "nombres"));
        form.setApellidos(Validador.nombrePersona(form.getApellidos(), "apellidos"));
        LocalDate hoy = LocalDate.now();
        // entre 16 y 80 años de edad
        LocalDate nacimiento = Validador.fechaEntre(form.getFechaNacimiento(), "fecha de nacimiento", hoy.minusYears(80), hoy.minusYears(16));
        form.setTelefono(Validador.telefono(form.getTelefono(), "teléfono"));
        form.setEmail(Validador.correo(form.getEmail(), "correo", true));
        form.setTipoEmpleado(Validador.unoDe(form.getTipoEmpleado(), "tipo de empleado", "VET", "AUX", "ADM"));
        // no futura y con el empleado de al menos 16 años
        Validador.fechaEntre(form.getFechaIngreso(), "fecha de ingreso", nacimiento.plusYears(16), hoy);
        // DECIMAL(12,2) y CK_EMPLEADO_SALARIO (> 0)
        Validador.decimalEntre(form.getSalario(), "salario", new BigDecimal("1"), new BigDecimal("50000000"));
        Validador.id(form.getIdSede(), "la sede");

        if ("VET".equals(form.getTipoEmpleado())) {
            String tarjeta = Validador.obligatorio(form.getTarjetaProfesional(), "tarjeta profesional", 20).toUpperCase();
            form.setTarjetaProfesional(Validador.formato(tarjeta, "tarjeta profesional", Validador.PATRON_TARJETA, "letras y número, por ejemplo TP-20037"));
            String universidad = Validador.obligatorio(Validador.espacios(form.getUniversidad()), "universidad", 100);
            form.setUniversidad(Validador.formato(universidad, "universidad", Validador.PATRON_INSTITUCION, "letras, números y . , -"));
            // CK_VETERINARIO_ANIO_GRADUACION (1970 a 2026), no en el futuro y con al menos 18 años de edad
            int maximo = Math.min(2026, hoy.getYear());
            boolean anioSinCambio = anioGuardado != null && anioGuardado.equals(form.getAnioGraduacion());
            int minimo = anioSinCambio ? 1970 : Math.max(1970, nacimiento.getYear() + 18);
            if (minimo > maximo) {
                throw new DatosInvalidosException("Con esa fecha de nacimiento no puede haberse graduado todavía (mínimo 18 años).");
            }
            Validador.entero(form.getAnioGraduacion(), "año de graduación", minimo, maximo);
            // LinkedHashSet quita repetidos y conserva el orden
            ArrayList<Integer> especialidades = new ArrayList<Integer>();
            if (form.getEspecialidades() != null) {
                especialidades.addAll(new LinkedHashSet<Integer>(form.getEspecialidades()));
            }
            form.setEspecialidades(especialidades);
        } else {
            form.setTarjetaProfesional(null);
            form.setUniversidad(null);
            form.setAnioGraduacion(null);
            form.setEspecialidades(new ArrayList<Integer>());
        }
        if ("AUX".equals(form.getTipoEmpleado())) {
            form.setArea(Validador.unoDe(form.getArea(), "área", "CLINICA", "CIRUGIA", "LABORATORIO", "ESTETICA", "HOSPITALIZACION"));
            String certificacion = Validador.opcional(Validador.espacios(form.getCertificacion()), "certificación", 100);
            form.setCertificacion(Validador.formato(certificacion, "certificación", Validador.PATRON_INSTITUCION, "letras, números y . , -"));
            form.setTurno(Validador.unoDe(form.getTurno(), "turno", "MANANA", "TARDE", "NOCHE"));
        } else {
            form.setArea(null);
            form.setCertificacion(null);
            form.setTurno(null);
        }
    }
}
