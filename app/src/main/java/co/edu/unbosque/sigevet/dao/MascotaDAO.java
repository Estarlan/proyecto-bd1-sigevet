package co.edu.unbosque.sigevet.dao;

import co.edu.unbosque.sigevet.connection.ConexionBD;
import co.edu.unbosque.sigevet.model.HistoriaClinica;
import co.edu.unbosque.sigevet.model.Mascota;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;
import co.edu.unbosque.sigevet.connection.Transaccion;
import co.edu.unbosque.sigevet.dto.MascotaFormDTO;
import co.edu.unbosque.sigevet.util.ErroresSQL;
import java.sql.Statement;
import java.sql.Types;

/**
 * Acceso a datos de las mascotas y de sus historias clínicas. Ejecuta el SQL con JDBC y {@code PreparedStatement},
 * sin ORM, y convierte cada fila en un objeto {@link Mascota} o {@link HistoriaClinica}.
 */
@Repository
public class MascotaDAO {
    /** Fábrica de conexiones a la base de datos. */
    private final ConexionBD conexionBD;

    /**
     * Constructor con inyección de dependencias: Spring entrega la conexión al crear el DAO.
     *
     * @param conexionBD fábrica de conexiones a la base de datos.
     */
    public MascotaDAO(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Busca una mascota por su identificador, con el nombre de su raza, su especie y su dueño.
     *
     * @param id identificador de la mascota.
     * @return la mascota, o {@code null} si no existe.
     * @throws RuntimeException si la consulta falla.
     */
    public Mascota buscar(int id) {
        // la mascota solo guarda la raza; la especie se obtiene por la raza (MASCOTA -> RAZA -> ESPECIE)
        String sql = "SELECT m.*, r.nombre AS raza, e.nombre AS especie, CONCAT(c.nombres, ' ', c.apellidos) AS cliente "
                + "FROM MASCOTA m JOIN RAZA r ON r.id_raza = m.id_raza "
                + "JOIN ESPECIE e ON e.id_especie = r.id_especie "
                + "JOIN CLIENTE c ON c.id_cliente = m.id_cliente "
                + "WHERE m.id_mascota = ?";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                // el identificador es llave primaria: hay a lo sumo una fila, por eso se devuelve en la primera vuelta
                while (rs.next()) {
                    Mascota objeto = new Mascota();
                    objeto.setIdMascota(rs.getInt("id_mascota"));
                    objeto.setIdCliente(rs.getInt("id_cliente"));
                    objeto.setNombre(rs.getString("nombre"));
                    objeto.setFechaNacimiento(rs.getString("fecha_nacimiento"));
                    objeto.setSexo(rs.getString("sexo"));
                    objeto.setColor(rs.getString("color"));
                    objeto.setMicrochip(rs.getString("microchip"));
                    objeto.setEsterilizado(rs.getString("esterilizado"));
                    objeto.setEstado(rs.getString("estado"));
                    objeto.setRaza(rs.getString("raza"));
                    objeto.setEspecie(rs.getString("especie"));
                    objeto.setCliente(rs.getString("cliente"));
                    return objeto;
                }
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo buscar los datos.", e);
        }
    }

    /**
     * Lista las mascotas de un cliente.
     *
     * @param id identificador del cliente dueño.
     * @return las mascotas del cliente ordenadas por nombre.
     * @throws RuntimeException si la consulta falla.
     */
    public ArrayList<Mascota> listarPorCliente(int id) {
        ArrayList<Mascota> lista = new ArrayList<Mascota>();
        String sql = "SELECT m.*, r.nombre AS raza, e.nombre AS especie, CONCAT(c.nombres, ' ', c.apellidos) AS cliente "
                + "FROM MASCOTA m JOIN RAZA r ON r.id_raza = m.id_raza "
                + "JOIN ESPECIE e ON e.id_especie = r.id_especie "
                + "JOIN CLIENTE c ON c.id_cliente = m.id_cliente "
                + "WHERE m.id_cliente = ? ORDER BY m.nombre, m.id_mascota";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    Mascota objeto = new Mascota();
                    objeto.setIdMascota(rs.getInt("id_mascota"));
                    objeto.setIdCliente(rs.getInt("id_cliente"));
                    objeto.setNombre(rs.getString("nombre"));
                    objeto.setFechaNacimiento(rs.getString("fecha_nacimiento"));
                    objeto.setSexo(rs.getString("sexo"));
                    objeto.setColor(rs.getString("color"));
                    objeto.setMicrochip(rs.getString("microchip"));
                    objeto.setEsterilizado(rs.getString("esterilizado"));
                    objeto.setEstado(rs.getString("estado"));
                    objeto.setRaza(rs.getString("raza"));
                    objeto.setEspecie(rs.getString("especie"));
                    objeto.setCliente(rs.getString("cliente"));
                    lista.add(objeto);
                }
            }
            return lista;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudieron consultar las mascotas del cliente.", e);
        }
    }

    /**
     * Busca la historia clínica de una mascota (cada mascota tiene una sola).
     *
     * @param id identificador de la mascota.
     * @return la historia clínica, o {@code null} si la mascota no tiene.
     * @throws RuntimeException si la consulta falla.
     */
    public HistoriaClinica historia(int id) {
        String sql = "SELECT * FROM HISTORIA_CLINICA WHERE id_mascota = ?";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                // relación uno a uno con la mascota: hay a lo sumo una fila
                while (rs.next()) {
                    HistoriaClinica objeto = new HistoriaClinica();
                    objeto.setIdHistoria(rs.getInt("id_historia"));
                    objeto.setNumeroHistoria(rs.getString("numero_historia"));
                    objeto.setFechaApertura(rs.getString("fecha_apertura"));
                    objeto.setGrupoSanguineo(rs.getString("grupo_sanguineo"));
                    objeto.setAlergias(rs.getString("alergias"));
                    objeto.setEnfermedadesCronicas(rs.getString("enfermedades_cronicas"));
                    objeto.setObservaciones(rs.getString("observaciones"));
                    return objeto;
                }
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo consultar la historia clínica.", e);
        }
    }

    // ------------------------------------------------------------------
    // Lista y escritura (CRUD)
    // ------------------------------------------------------------------

    /**
     * Busca mascotas por nombre, por apellido del propietario o por microchip.
     *
     * @param buscar texto a buscar; vacío devuelve las primeras 200.
     * @return mascotas con su raza, especie y propietario.
     */
    public ArrayList<Mascota> listar(String buscar) {
        ArrayList<Mascota> lista = new ArrayList<Mascota>();
        String sql = "SELECT m.*, r.nombre AS raza, e.nombre AS especie, CONCAT(c.nombres, ' ', c.apellidos) AS cliente "
                + "FROM MASCOTA m JOIN RAZA r ON r.id_raza = m.id_raza "
                + "JOIN ESPECIE e ON e.id_especie = r.id_especie "
                + "JOIN CLIENTE c ON c.id_cliente = m.id_cliente "
                + "WHERE UPPER(m.nombre) LIKE ? OR UPPER(c.apellidos) LIKE ? OR m.microchip LIKE ? "
                + "ORDER BY m.nombre, m.id_mascota LIMIT 200";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            String texto = "%" + buscar.toUpperCase() + "%";
            sentencia.setString(1, texto);
            sentencia.setString(2, texto);
            sentencia.setString(3, texto);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    Mascota objeto = new Mascota();
                    objeto.setIdMascota(rs.getInt("id_mascota"));
                    objeto.setIdCliente(rs.getInt("id_cliente"));
                    objeto.setNombre(rs.getString("nombre"));
                    objeto.setFechaNacimiento(rs.getString("fecha_nacimiento"));
                    objeto.setSexo(rs.getString("sexo"));
                    objeto.setColor(rs.getString("color"));
                    objeto.setMicrochip(rs.getString("microchip"));
                    objeto.setEsterilizado(rs.getString("esterilizado"));
                    objeto.setEstado(rs.getString("estado"));
                    objeto.setRaza(rs.getString("raza"));
                    objeto.setEspecie(rs.getString("especie"));
                    objeto.setCliente(rs.getString("cliente"));
                    lista.add(objeto);
                }
            }
            return lista;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudieron listar las mascotas.", e);
        }
    }

    /**
     * Lee una mascota y su historia clínica con los identificadores que necesita el formulario de edición.
     *
     * @param id identificador de la mascota.
     * @return el formulario lleno, o {@code null} si no existe.
     */
    public MascotaFormDTO formulario(int id) {
        // LEFT JOIN: la historia clínica podría no existir
        String sql = "SELECT m.*, h.grupo_sanguineo, h.alergias, h.enfermedades_cronicas, h.observaciones "
                + "FROM MASCOTA m LEFT JOIN HISTORIA_CLINICA h ON h.id_mascota = m.id_mascota "
                + "WHERE m.id_mascota = ?";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                MascotaFormDTO form = new MascotaFormDTO();
                form.setNombre(rs.getString("nombre"));
                form.setFechaNacimiento(rs.getString("fecha_nacimiento"));
                form.setSexo(rs.getString("sexo"));
                form.setColor(rs.getString("color"));
                form.setMicrochip(rs.getString("microchip"));
                form.setEsterilizado(rs.getString("esterilizado"));
                form.setEstado(rs.getString("estado"));
                form.setIdRaza(rs.getInt("id_raza"));
                form.setIdCliente(rs.getInt("id_cliente"));
                form.setGrupoSanguineo(rs.getString("grupo_sanguineo"));
                form.setAlergias(rs.getString("alergias"));
                form.setEnfermedadesCronicas(rs.getString("enfermedades_cronicas"));
                form.setObservaciones(rs.getString("observaciones"));
                return form;
            }
        } catch (SQLException e) {
            throw ErroresSQL.traducir(e);
        }
    }

    /**
     * Crea una mascota y su historia clínica (relación 1:1 TENER) en una sola transacción. El número de historia
     * sigue el formato de los datos de prueba: HC- y el identificador de la mascota con seis dígitos.
     *
     * @param form datos ya validados por el servicio.
     * @return el identificador generado de la mascota.
     */
    public long insertar(MascotaFormDTO form) {
        String sqlMascota = "INSERT INTO MASCOTA (nombre, fecha_nacimiento, sexo, color, microchip, esterilizado, "
                + "estado, id_raza, id_cliente) VALUES (?, ?, ?, ?, ?, ?, 'ACTIVA', ?, ?)";
        String sqlHistoria = "INSERT INTO HISTORIA_CLINICA (numero_historia, id_mascota, fecha_apertura, "
                + "grupo_sanguineo, alergias, enfermedades_cronicas, observaciones) "
                + "VALUES (CONCAT('HC-', LPAD(?, 6, '0')), ?, CURDATE(), ?, ?, ?, ?)";
        Connection conexion = null;
        try {
            conexion = conexionBD.conectar();
            conexion.setAutoCommit(false);
            long id;
            try (PreparedStatement sentencia = conexion.prepareStatement(sqlMascota, Statement.RETURN_GENERATED_KEYS)) {
                llenarMascota(sentencia, form);
                sentencia.executeUpdate();
                try (ResultSet llaves = sentencia.getGeneratedKeys()) {
                    llaves.next();
                    id = llaves.getLong(1);
                }
            }
            try (PreparedStatement sentencia = conexion.prepareStatement(sqlHistoria)) {
                sentencia.setLong(1, id);
                sentencia.setLong(2, id);
                llenarHistoria(sentencia, form, 3);
                sentencia.executeUpdate();
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
     * Actualiza la mascota y su historia clínica. Si la mascota no tenía historia, la crea.
     *
     * @param id identificador de la mascota.
     * @param form datos ya validados.
     * @return {@code true} si la mascota existía.
     */
    public boolean actualizar(int id, MascotaFormDTO form) {
        String sqlMascota = "UPDATE MASCOTA SET nombre = ?, fecha_nacimiento = ?, sexo = ?, color = ?, microchip = ?, "
                + "esterilizado = ?, id_raza = ?, id_cliente = ? WHERE id_mascota = ?";
        String sqlHistoria = "UPDATE HISTORIA_CLINICA SET grupo_sanguineo = ?, alergias = ?, "
                + "enfermedades_cronicas = ?, observaciones = ? WHERE id_mascota = ?";
        String sqlNuevaHistoria = "INSERT INTO HISTORIA_CLINICA (numero_historia, id_mascota, fecha_apertura, "
                + "grupo_sanguineo, alergias, enfermedades_cronicas, observaciones) "
                + "VALUES (CONCAT('HC-', LPAD(?, 6, '0')), ?, CURDATE(), ?, ?, ?, ?)";
        Connection conexion = null;
        try {
            conexion = conexionBD.conectar();
            conexion.setAutoCommit(false);
            try (PreparedStatement sentencia = conexion.prepareStatement(sqlMascota)) {
                llenarMascota(sentencia, form);
                sentencia.setInt(9, id);
                if (sentencia.executeUpdate() == 0) {
                    Transaccion.deshacer(conexion);
                    return false;
                }
            }
            int filasHistoria;
            try (PreparedStatement sentencia = conexion.prepareStatement(sqlHistoria)) {
                llenarHistoria(sentencia, form, 1);
                sentencia.setInt(5, id);
                filasHistoria = sentencia.executeUpdate();
            }
            if (filasHistoria == 0) {
                try (PreparedStatement sentencia = conexion.prepareStatement(sqlNuevaHistoria)) {
                    sentencia.setInt(1, id);
                    sentencia.setInt(2, id);
                    llenarHistoria(sentencia, form, 3);
                    sentencia.executeUpdate();
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
     * Borrado lógico: cambia el estado de la mascota. Si deja de estar ACTIVA (FALLECIDA o TRASLADADA), sus citas
     * PROGRAMADAS se cancelan en la misma transacción.
     *
     * @param id identificador de la mascota.
     * @param estado ACTIVA, FALLECIDA o TRASLADADA.
     * @return cantidad de citas canceladas, o -1 si la mascota no existe.
     */
    public int cambiarEstado(int id, String estado) {
        Connection conexion = null;
        try {
            conexion = conexionBD.conectar();
            conexion.setAutoCommit(false);
            try (PreparedStatement sentencia = conexion.prepareStatement("UPDATE MASCOTA SET estado = ? WHERE id_mascota = ?")) {
                sentencia.setString(1, estado);
                sentencia.setInt(2, id);
                if (sentencia.executeUpdate() == 0) {
                    Transaccion.deshacer(conexion);
                    return -1;
                }
            }
            int canceladas = 0;
            if (!"ACTIVA".equals(estado)) {
                String sql = "UPDATE CITA SET estado = 'CANCELADA' WHERE id_mascota = ? AND estado = 'PROGRAMADA'";
                try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
                    sentencia.setInt(1, id);
                    canceladas = sentencia.executeUpdate();
                }
            }
            conexion.commit();
            return canceladas;
        } catch (SQLException e) {
            Transaccion.deshacer(conexion);
            throw ErroresSQL.traducir(e);
        } finally {
            Transaccion.cerrar(conexion);
        }
    }

    /** Pone los 8 datos de la mascota en los primeros signos de interrogación. */
    private void llenarMascota(PreparedStatement sentencia, MascotaFormDTO form) throws SQLException {
        sentencia.setString(1, form.getNombre());
        // una fecha vacía se guarda como NULL (la columna lo permite)
        if (form.getFechaNacimiento() == null) {
            sentencia.setNull(2, Types.DATE);
        } else {
            sentencia.setString(2, form.getFechaNacimiento());
        }
        sentencia.setString(3, form.getSexo());
        sentencia.setString(4, form.getColor());
        sentencia.setString(5, form.getMicrochip());
        sentencia.setString(6, form.getEsterilizado());
        sentencia.setInt(7, form.getIdRaza());
        sentencia.setInt(8, form.getIdCliente());
    }

    /** Pone los 4 datos de la historia clínica a partir de la posición indicada. */
    private void llenarHistoria(PreparedStatement sentencia, MascotaFormDTO form, int desde) throws SQLException {
        sentencia.setString(desde, form.getGrupoSanguineo());
        sentencia.setString(desde + 1, form.getAlergias());
        sentencia.setString(desde + 2, form.getEnfermedadesCronicas());
        sentencia.setString(desde + 3, form.getObservaciones());
    }
}
