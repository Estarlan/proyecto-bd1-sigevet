package co.edu.unbosque.sigevet.dao;

import co.edu.unbosque.sigevet.connection.ConexionBD;
import co.edu.unbosque.sigevet.model.Auxiliar;
import co.edu.unbosque.sigevet.model.Empleado;
import co.edu.unbosque.sigevet.model.Veterinario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;
import co.edu.unbosque.sigevet.connection.Transaccion;
import co.edu.unbosque.sigevet.dto.EmpleadoFormDTO;
import co.edu.unbosque.sigevet.util.ErroresSQL;
import java.sql.Statement;

/**
 * Acceso a datos del personal de las sedes. Ejecuta el SQL con JDBC y {@code PreparedStatement}, sin ORM.
 *
 * <p>Aquí se ve la herencia y el polimorfismo del modelo: según el tipo de empleado, cada fila se convierte en un
 * {@link Veterinario}, un {@link Auxiliar} o un {@link Empleado} simple, y todos se guardan en la misma lista.</p>
 */
@Repository
public class PersonalDAO {
    /** Fábrica de conexiones a la base de datos. */
    private final ConexionBD conexionBD;

    /**
     * Constructor con inyección de dependencias: Spring entrega la conexión al crear el DAO.
     *
     * @param conexionBD fábrica de conexiones a la base de datos.
     */
    public PersonalDAO(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Lista el personal de todas las sedes o de una sola, con los datos propios de cada tipo de empleado.
     *
     * @param sede identificador de la sede a filtrar, o {@code null} para listar todas.
     * @return los empleados ordenados por sede, apellidos y nombres.
     * @throws RuntimeException si la consulta falla.
     */
    public ArrayList<Empleado> listar(Integer sede) {
        ArrayList<Empleado> empleados = new ArrayList<Empleado>();
        // LEFT JOIN con VETERINARIO y AUXILIAR: cada empleado trae los datos de su subtipo y los demás quedan nulos
        String sql = "SELECT e.*, s.nombre AS sede, v.tarjeta_profesional, v.universidad, a.area, a.turno "
                + "FROM EMPLEADO e JOIN SEDE s ON s.id_sede = e.id_sede "
                + "LEFT JOIN VETERINARIO v ON v.id_empleado = e.id_empleado "
                + "LEFT JOIN AUXILIAR a ON a.id_empleado = e.id_empleado "
                + "WHERE (? IS NULL OR e.id_sede = ?) "
                + "ORDER BY s.nombre, e.apellidos, e.nombres, e.id_empleado";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            if (sede == null) {
                sentencia.setNull(1, Types.INTEGER);
                sentencia.setNull(2, Types.INTEGER);
            } else {
                sentencia.setInt(1, sede);
                sentencia.setInt(2, sede);
            }
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    // se crea el objeto del subtipo correcto (polimorfismo) y se guarda como Empleado
                    Empleado empleado;
                    String tipoEmpleado = rs.getString("tipo_empleado");
                    if ("VET".equals(tipoEmpleado)) {
                        Veterinario veterinario = new Veterinario();
                        veterinario.setTarjetaProfesional(rs.getString("tarjeta_profesional"));
                        veterinario.setUniversidad(rs.getString("universidad"));
                        empleado = veterinario;
                    } else if ("AUX".equals(tipoEmpleado)) {
                        Auxiliar auxiliar = new Auxiliar();
                        auxiliar.setArea(rs.getString("area"));
                        auxiliar.setTurno(rs.getString("turno"));
                        empleado = auxiliar;
                    } else {
                        empleado = new Empleado();
                    }
                    // datos comunes a todos los empleados
                    empleado.setIdPersona(rs.getInt("id_empleado"));
                    empleado.setTipoDocumento(rs.getString("tipo_documento"));
                    empleado.setNumDocumento(rs.getString("num_documento"));
                    empleado.setNombres(rs.getString("nombres"));
                    empleado.setApellidos(rs.getString("apellidos"));
                    empleado.setEmail(rs.getString("email"));
                    empleado.setSede(rs.getString("sede"));
                    empleado.setTipoEmpleado(tipoEmpleado);
                    empleado.setFechaIngreso(rs.getString("fecha_ingreso"));
                    empleado.setSalario(rs.getBigDecimal("salario"));
                    empleado.setEstado(rs.getString("estado"));
                    empleado.setTelefono(rs.getString("telefono"));
                    empleados.add(empleado);
                }
            }
            // las especialidades se leen aparte porque un veterinario puede tener varias (relación muchos a muchos)
            cargarEspecialidades(conexion, empleados, sede);
            return empleados;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo consultar el personal.", e);
        }
    }

    /**
     * Lee las especialidades de los veterinarios y las agrega a cada uno en la lista de empleados.
     * Reutiliza la misma conexión de {@link #listar(Integer)} para no abrir otra.
     *
     * @param conexion conexión ya abierta.
     * @param empleados empleados ya leídos; los veterinarios reciben sus especialidades.
     * @param sede identificador de la sede filtrada, o {@code null} si se listan todas.
     * @throws SQLException si la consulta falla.
     */
    private void cargarEspecialidades(Connection conexion, ArrayList<Empleado> empleados, Integer sede)
            throws SQLException {
        String sql = "SELECT ve.id_empleado, es.nombre FROM VETERINARIO_ESPECIALIDAD ve "
                + "JOIN ESPECIALIDAD es ON es.id_especialidad = ve.id_especialidad "
                + "JOIN EMPLEADO e ON e.id_empleado = ve.id_empleado "
                + "WHERE (? IS NULL OR e.id_sede = ?) ORDER BY ve.id_empleado, es.nombre";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            if (sede == null) {
                sentencia.setNull(1, Types.INTEGER);
                sentencia.setNull(2, Types.INTEGER);
            } else {
                sentencia.setInt(1, sede);
                sentencia.setInt(2, sede);
            }
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    int idEmpleado = rs.getInt("id_empleado");
                    String nombre = rs.getString("nombre");
                    // se busca al veterinario dueño de la especialidad; instanceof comprueba que sea Veterinario antes del cast
                    for (int i = 0; i < empleados.size(); i++) {
                        Empleado empleado = empleados.get(i);
                        if (empleado instanceof Veterinario && empleado.getIdPersona() == idEmpleado) {
                            Veterinario veterinario = (Veterinario) empleado;
                            veterinario.getEspecialidades().add(nombre);
                        }
                    }
                }
            }
        }
    }

    // ------------------------------------------------------------------
    // Escritura: especialización de EMPLEADO en VETERINARIO y AUXILIAR.
    // El supertipo y el subtipo se guardan en la misma transacción.
    // ------------------------------------------------------------------

    /**
     * Lee un empleado con los datos de su subtipo y los identificadores de sus especialidades, para el formulario.
     *
     * @param id identificador del empleado.
     * @return el formulario lleno, o {@code null} si no existe.
     */
    public EmpleadoFormDTO formulario(int id) {
        String sql = "SELECT e.*, v.tarjeta_profesional, v.universidad, v.anio_graduacion, a.area, a.certificacion, a.turno "
                + "FROM EMPLEADO e LEFT JOIN VETERINARIO v ON v.id_empleado = e.id_empleado "
                + "LEFT JOIN AUXILIAR a ON a.id_empleado = e.id_empleado WHERE e.id_empleado = ?";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            EmpleadoFormDTO form = null;
            try (ResultSet rs = sentencia.executeQuery()) {
                if (rs.next()) {
                    form = new EmpleadoFormDTO();
                    form.setTipoDocumento(rs.getString("tipo_documento"));
                    form.setNumDocumento(rs.getString("num_documento"));
                    form.setNombres(rs.getString("nombres"));
                    form.setApellidos(rs.getString("apellidos"));
                    form.setFechaNacimiento(rs.getString("fecha_nacimiento"));
                    form.setTelefono(rs.getString("telefono"));
                    form.setEmail(rs.getString("email"));
                    form.setTipoEmpleado(rs.getString("tipo_empleado"));
                    form.setFechaIngreso(rs.getString("fecha_ingreso"));
                    form.setSalario(rs.getBigDecimal("salario"));
                    form.setIdSede(rs.getInt("id_sede"));
                    form.setEstado(rs.getString("estado"));
                    form.setTarjetaProfesional(rs.getString("tarjeta_profesional"));
                    form.setUniversidad(rs.getString("universidad"));
                    // getInt devuelve 0 cuando la columna es NULL; wasNull permite distinguirlo
                    int anio = rs.getInt("anio_graduacion");
                    form.setAnioGraduacion(rs.wasNull() ? null : anio);
                    form.setArea(rs.getString("area"));
                    form.setCertificacion(rs.getString("certificacion"));
                    form.setTurno(rs.getString("turno"));
                }
            }
            if (form != null && "VET".equals(form.getTipoEmpleado())) {
                form.setEspecialidades(especialidadesDe(conexion, id));
            }
            return form;
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e);
        }
    }

    /**
     * Crea un empleado, la fila de su subtipo y sus especialidades en una sola transacción. El disparador
     * TRG_VETERINARIO_TIPO_INS (o TRG_AUXILIAR_TIPO_INS) revisa la regla RN-09 al insertar el subtipo.
     *
     * @param form datos ya validados por el servicio.
     * @return el identificador generado.
     */
    public long insertar(EmpleadoFormDTO form) {
        String sql = "INSERT INTO EMPLEADO (tipo_documento, num_documento, nombres, apellidos, fecha_nacimiento, telefono, "
                + "email, fecha_ingreso, salario, id_sede, tipo_empleado, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'ACTIVO')";
        Connection conexion = null;
        try {
            conexion = conexionBD.conectar();
            conexion.setAutoCommit(false);
            int id;
            try (PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                llenarEmpleado(sentencia, form);
                sentencia.setString(11, form.getTipoEmpleado());
                sentencia.executeUpdate();
                try (ResultSet llaves = sentencia.getGeneratedKeys()) {
                    llaves.next();
                    id = llaves.getInt(1);
                }
            }
            if ("VET".equals(form.getTipoEmpleado())) {
                insertarVeterinario(conexion, id, form);
                for (Integer especialidad : form.getEspecialidades()) {
                    agregarEspecialidad(conexion, id, especialidad);
                }
            } else if ("AUX".equals(form.getTipoEmpleado())) {
                insertarAuxiliar(conexion, id, form);
            }
            conexion.commit();
            return id;
        } catch (SQLException e) {
            Transaccion.deshacer(conexion);
            throw ErroresSQL.traducir(e);
        } finally {
            Transaccion.cerrar(conexion);
        }
    }

    /**
     * Actualiza un empleado y su subtipo. El tipo de empleado no cambia (lo impide TRG_EMPLEADO_TIPO). Las
     * especialidades se comparan con las actuales: se borran las que se quitaron y se insertan las nuevas, así las
     * que se conservan mantienen su fecha de certificación.
     *
     * @param id identificador del empleado.
     * @param form datos ya validados.
     * @return {@code true} si el empleado existía.
     */
    public boolean actualizar(int id, EmpleadoFormDTO form) {
        String sql = "UPDATE EMPLEADO SET tipo_documento = ?, num_documento = ?, nombres = ?, apellidos = ?, "
                + "fecha_nacimiento = ?, telefono = ?, email = ?, fecha_ingreso = ?, salario = ?, id_sede = ? "
                + "WHERE id_empleado = ?";
        Connection conexion = null;
        try {
            conexion = conexionBD.conectar();
            conexion.setAutoCommit(false);
            try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                llenarEmpleado(sentencia, form);
                sentencia.setInt(11, id);
                if (sentencia.executeUpdate() == 0) {
                    Transaccion.deshacer(conexion);
                    return false;
                }
            }
            if ("VET".equals(form.getTipoEmpleado())) {
                String sqlVet = "UPDATE VETERINARIO SET tarjeta_profesional = ?, universidad = ?, anio_graduacion = ? "
                        + "WHERE id_empleado = ?";
                int filas;
                try (PreparedStatement sentencia = conexion.prepareStatement(sqlVet)) {
                    sentencia.setString(1, form.getTarjetaProfesional());
                    sentencia.setString(2, form.getUniversidad());
                    sentencia.setInt(3, form.getAnioGraduacion());
                    sentencia.setInt(4, id);
                    filas = sentencia.executeUpdate();
                }
                if (filas == 0) {
                    insertarVeterinario(conexion, id, form);
                }
                ArrayList<Integer> actuales = especialidadesDe(conexion, id);
                for (Integer especialidad : actuales) {
                    if (!form.getEspecialidades().contains(especialidad)) {
                        try (PreparedStatement sentencia = conexion.prepareStatement(
                                "DELETE FROM VETERINARIO_ESPECIALIDAD WHERE id_empleado = ? AND id_especialidad = ?")) {
                            sentencia.setInt(1, id);
                            sentencia.setInt(2, especialidad);
                            sentencia.executeUpdate();
                        }
                    }
                }
                for (Integer especialidad : form.getEspecialidades()) {
                    if (!actuales.contains(especialidad)) {
                        agregarEspecialidad(conexion, id, especialidad);
                    }
                }
            } else if ("AUX".equals(form.getTipoEmpleado())) {
                String sqlAux = "UPDATE AUXILIAR SET area = ?, certificacion = ?, turno = ? WHERE id_empleado = ?";
                int filas;
                try (PreparedStatement sentencia = conexion.prepareStatement(sqlAux)) {
                    sentencia.setString(1, form.getArea());
                    sentencia.setString(2, form.getCertificacion());
                    sentencia.setString(3, form.getTurno());
                    sentencia.setInt(4, id);
                    filas = sentencia.executeUpdate();
                }
                if (filas == 0) {
                    insertarAuxiliar(conexion, id, form);
                }
            }
            conexion.commit();
            return true;
        } catch (SQLException e) {
            Transaccion.deshacer(conexion);
            throw ErroresSQL.traducir(e);
        } finally {
            Transaccion.cerrar(conexion);
        }
    }

    /**
     * Borrado lógico: el empleado pasa a INACTIVO (o vuelve a ACTIVO). Sus citas y facturas se conservan.
     *
     * @return {@code true} si el empleado existía.
     */
    public boolean cambiarEstado(int id, String estado) {
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement("UPDATE EMPLEADO SET estado = ? WHERE id_empleado = ?")) {
            sentencia.setString(1, estado);
            sentencia.setInt(2, id);
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e);
        }
    }

    /**
     * Nombre de la sede que dirige el empleado (relación 1:1 DIRIGIR), o {@code null} si no dirige ninguna.
     */
    public String sedeQueDirige(int id) {
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement("SELECT nombre FROM SEDE WHERE id_director = ?")) {
            sentencia.setInt(1, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                return rs.next() ? rs.getString("nombre") : null;
            }
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e);
        }
    }

    /** Pone los 10 datos comunes de EMPLEADO en los primeros signos de interrogación. */
    private void llenarEmpleado(PreparedStatement sentencia, EmpleadoFormDTO form) throws SQLException {
        sentencia.setString(1, form.getTipoDocumento());
        sentencia.setString(2, form.getNumDocumento());
        sentencia.setString(3, form.getNombres());
        sentencia.setString(4, form.getApellidos());
        sentencia.setString(5, form.getFechaNacimiento());
        sentencia.setString(6, form.getTelefono());
        sentencia.setString(7, form.getEmail());
        sentencia.setString(8, form.getFechaIngreso());
        sentencia.setBigDecimal(9, form.getSalario());
        sentencia.setInt(10, form.getIdSede());
    }

    private void insertarVeterinario(Connection conexion, int id, EmpleadoFormDTO form) throws SQLException {
        String sql = "INSERT INTO VETERINARIO (id_empleado, tarjeta_profesional, universidad, anio_graduacion) VALUES (?, ?, ?, ?)";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            sentencia.setString(2, form.getTarjetaProfesional());
            sentencia.setString(3, form.getUniversidad());
            sentencia.setInt(4, form.getAnioGraduacion());
            sentencia.executeUpdate();
        }
    }

    private void insertarAuxiliar(Connection conexion, int id, EmpleadoFormDTO form) throws SQLException {
        String sql = "INSERT INTO AUXILIAR (id_empleado, area, certificacion, turno) VALUES (?, ?, ?, ?)";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            sentencia.setString(2, form.getArea());
            sentencia.setString(3, form.getCertificacion());
            sentencia.setString(4, form.getTurno());
            sentencia.executeUpdate();
        }
    }

    /** Certifica al veterinario en una especialidad con la fecha de hoy (relación N:M CERTIFICAR). */
    private void agregarEspecialidad(Connection conexion, int id, int especialidad) throws SQLException {
        String sql = "INSERT INTO VETERINARIO_ESPECIALIDAD (id_empleado, id_especialidad, fecha_certificacion) VALUES (?, ?, CURDATE())";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            sentencia.setInt(2, especialidad);
            sentencia.executeUpdate();
        }
    }

    /** Identificadores de las especialidades de un veterinario. */
    private ArrayList<Integer> especialidadesDe(Connection conexion, int id) throws SQLException {
        ArrayList<Integer> lista = new ArrayList<Integer>();
        try (PreparedStatement sentencia = conexion.prepareStatement(
                "SELECT id_especialidad FROM VETERINARIO_ESPECIALIDAD WHERE id_empleado = ? ORDER BY id_especialidad")) {
            sentencia.setInt(1, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    lista.add(rs.getInt("id_especialidad"));
                }
            }
        }
        return lista;
    }
}
