package co.edu.unbosque.sigevet.dao;

import co.edu.unbosque.sigevet.connection.ConexionBD;
import co.edu.unbosque.sigevet.model.Factura;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import org.springframework.stereotype.Repository;
import co.edu.unbosque.sigevet.connection.Transaccion;
import co.edu.unbosque.sigevet.dto.FacturaFormDTO;
import co.edu.unbosque.sigevet.dto.LineaFacturaFormDTO;
import co.edu.unbosque.sigevet.exception.ConflictoDatosException;
import co.edu.unbosque.sigevet.util.ErroresSQL;
import java.math.BigDecimal;
import java.sql.Statement;

/**
 * Acceso a datos de las facturas. Ejecuta el SQL con JDBC y {@code PreparedStatement}, sin ORM, y convierte cada
 * fila en un objeto {@link Factura}. El total de cada factura se calcula sumando sus líneas de detalle.
 */
@Repository
public class FacturaDAO {
    /** Fábrica de conexiones a la base de datos. */
    private final ConexionBD conexionBD;

    /**
     * Constructor con inyección de dependencias: Spring entrega la conexión al crear el DAO.
     *
     * @param conexionBD fábrica de conexiones a la base de datos.
     */
    public FacturaDAO(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    /**
     * Lista las facturas con filtros opcionales por sede y por estado.
     *
     * @param sede identificador de la sede, o {@code null} para todas las sedes.
     * @param estado estado de la factura (PAGADA, PENDIENTE o ANULADA), o {@code null} para todos.
     * @return las facturas de la más reciente a la más antigua, con su total.
     * @throws RuntimeException si la consulta falla.
     */
    public ArrayList<Factura> listar(Integer sede, String estado) {
        ArrayList<Factura> lista = new ArrayList<Factura>();
        // la subconsulta "t" suma las líneas de cada factura (cantidad x precio x descuento) y se une con LEFT JOIN:
        // una factura sin líneas aparece con total 0 gracias a IFNULL. Los filtros usan "(? IS NULL OR ...)".
        String sql = "SELECT f.id_cliente,f.metodo_pago,f.id_factura, f.numero_factura, f.fecha_emision, f.estado, CONCAT(c.nombres, ' ', c.apellidos) AS cliente, s.nombre AS sede, IFNULL(t.total, 0) AS total "
                + "FROM FACTURA f JOIN CLIENTE c ON c.id_cliente = f.id_cliente "
                + "JOIN SEDE s ON s.id_sede = f.id_sede "
                + "LEFT JOIN ( "
                + "SELECT id_factura, SUM(cantidad * precio_unitario * (1 - descuento_pct / 100)) AS total "
                + "FROM DETALLE_FACTURA GROUP BY id_factura ) t ON t.id_factura = f.id_factura "
                + "WHERE (? IS NULL OR f.id_sede = ?) "
                + "AND (? IS NULL OR f.estado = ?) "
                + "ORDER BY f.fecha_emision DESC, f.id_factura DESC";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            // la sede ocupa los parámetros 1 y 2; el estado, los parámetros 3 y 4
            if (sede == null) {
                sentencia.setNull(1, Types.INTEGER);
                sentencia.setNull(2, Types.INTEGER);
            } else {
                sentencia.setInt(1, sede);
                sentencia.setInt(2, sede);
            }
            sentencia.setString(3, estado);
            sentencia.setString(4, estado);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    Factura objeto = new Factura();
                    objeto.setIdFactura(rs.getLong("id_factura"));
                    objeto.setIdCliente(rs.getInt("id_cliente"));
                    objeto.setNumeroFactura(rs.getString("numero_factura"));
                    objeto.setFechaEmision(rs.getString("fecha_emision"));
                    objeto.setMetodoPago(rs.getString("metodo_pago"));
                    objeto.setEstado(rs.getString("estado"));
                    objeto.setCliente(rs.getString("cliente"));
                    objeto.setSede(rs.getString("sede"));
                    objeto.setTotal(rs.getBigDecimal("total"));
                    lista.add(objeto);
                }
            }
            return lista;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo listar los datos.", e);
        }
    }

    /**
     * Busca una factura por su identificador, con el nombre del cliente, la sede y el total.
     *
     * @param id identificador de la factura.
     * @return la factura, o {@code null} si no existe.
     * @throws RuntimeException si la consulta falla.
     */
    public Factura buscar(long id) {
        String sql = "SELECT f.*, CONCAT(c.nombres, ' ', c.apellidos) AS cliente, s.nombre AS sede, IFNULL(t.total, 0) AS total "
                + "FROM FACTURA f JOIN CLIENTE c ON c.id_cliente = f.id_cliente "
                + "JOIN SEDE s ON s.id_sede = f.id_sede "
                + "LEFT JOIN ( "
                + "SELECT id_factura, SUM(cantidad * precio_unitario * (1 - descuento_pct / 100)) AS total "
                + "FROM DETALLE_FACTURA GROUP BY id_factura ) t ON t.id_factura = f.id_factura "
                + "WHERE f.id_factura = ?";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setLong(1, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                // el identificador es llave primaria: hay a lo sumo una fila, por eso se devuelve en la primera vuelta
                while (rs.next()) {
                    Factura objeto = new Factura();
                    objeto.setIdFactura(rs.getLong("id_factura"));
                    objeto.setIdCliente(rs.getInt("id_cliente"));
                    objeto.setNumeroFactura(rs.getString("numero_factura"));
                    objeto.setFechaEmision(rs.getString("fecha_emision"));
                    objeto.setMetodoPago(rs.getString("metodo_pago"));
                    objeto.setEstado(rs.getString("estado"));
                    objeto.setCliente(rs.getString("cliente"));
                    objeto.setSede(rs.getString("sede"));
                    objeto.setTotal(rs.getBigDecimal("total"));
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
     * Lista las facturas de un cliente.
     *
     * @param id identificador del cliente.
     * @return las facturas del cliente, de la más reciente a la más antigua, con su total.
     * @throws RuntimeException si la consulta falla.
     */
    public ArrayList<Factura> listarPorCliente(int id) {
        ArrayList<Factura> lista = new ArrayList<Factura>();
        String sql = "SELECT f.id_cliente,f.metodo_pago,f.id_factura, f.numero_factura, f.fecha_emision, f.estado, CONCAT(c.nombres, ' ', c.apellidos) AS cliente, s.nombre AS sede, IFNULL(t.total, 0) AS total "
                + "FROM FACTURA f JOIN CLIENTE c ON c.id_cliente = f.id_cliente "
                + "JOIN SEDE s ON s.id_sede = f.id_sede "
                + "LEFT JOIN ( "
                + "SELECT id_factura, SUM(cantidad * precio_unitario * (1 - descuento_pct / 100)) AS total "
                + "FROM DETALLE_FACTURA GROUP BY id_factura ) t ON t.id_factura = f.id_factura "
                + "WHERE f.id_cliente = ? "
                + "ORDER BY f.fecha_emision DESC, f.id_factura DESC";
        try (Connection conexion = conexionBD.conectar();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    Factura objeto = new Factura();
                    objeto.setIdFactura(rs.getLong("id_factura"));
                    objeto.setIdCliente(rs.getInt("id_cliente"));
                    objeto.setNumeroFactura(rs.getString("numero_factura"));
                    objeto.setFechaEmision(rs.getString("fecha_emision"));
                    objeto.setMetodoPago(rs.getString("metodo_pago"));
                    objeto.setEstado(rs.getString("estado"));
                    objeto.setCliente(rs.getString("cliente"));
                    objeto.setSede(rs.getString("sede"));
                    objeto.setTotal(rs.getBigDecimal("total"));
                    lista.add(objeto);
                }
            }
            return lista;
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudieron consultar las facturas del cliente.", e);
        }
    }

    // ------------------------------------------------------------------
    // Escritura: crear una factura con sus líneas y cambiar su estado
    // ------------------------------------------------------------------

    /**
     * Crea una factura con todas sus líneas en UNA transacción:
     * <ol>
     *   <li>Calcula el siguiente consecutivo FV-000000 bloqueando las facturas ({@code FOR UPDATE}), para que dos
     *       ventas simultáneas no tomen el mismo número.</li>
     *   <li>Inserta la FACTURA con la fecha y hora actuales.</li>
     *   <li>Por cada línea lee el precio vigente de SERVICIO o MEDICAMENTO (el navegador no envía precios) e
     *       inserta el DETALLE_FACTURA (entidad débil: su llave es id_factura + num_linea).</li>
     *   <li>Si la línea es un medicamento, descuenta las existencias del INVENTARIO de la sede.</li>
     * </ol>
     * Si cualquier paso falla, el ROLLBACK deshace todo y no queda una factura a medias.
     *
     * @param form datos ya validados por el servicio.
     * @return el identificador generado de la factura.
     */
    public long insertar(FacturaFormDTO form) {
        Connection conexion = null;
        try {
            conexion = conexionBD.conectar();
            conexion.setAutoCommit(false);

            // el empleado que emite debe laborar en la sede y estar activo
            String sqlEmpleado = "SELECT id_sede, estado FROM EMPLEADO WHERE id_empleado = ?";
            try (PreparedStatement sentencia = conexion.prepareStatement(sqlEmpleado)) {
                sentencia.setInt(1, form.getIdEmpleado());
                try (ResultSet rs = sentencia.executeQuery()) {
                    if (!rs.next() || rs.getInt("id_sede") != form.getIdSede() || !"ACTIVO".equals(rs.getString("estado"))) {
                        throw new ConflictoDatosException("El empleado que emite debe estar activo y laborar en la sede de la factura.");
                    }
                }
            }

            // 1. consecutivo: el número que sigue al mayor FV- existente
            int siguiente;
            String sqlNumero = "SELECT IFNULL(MAX(CAST(SUBSTRING(numero_factura, 4) AS UNSIGNED)), 0) + 1 AS siguiente "
                    + "FROM FACTURA FOR UPDATE";
            try (PreparedStatement sentencia = conexion.prepareStatement(sqlNumero);
                 ResultSet rs = sentencia.executeQuery()) {
                rs.next();
                siguiente = rs.getInt("siguiente");
            }
            String numero = String.format("FV-%06d", siguiente);

            // 2. encabezado de la factura
            long idFactura;
            String sqlFactura = "INSERT INTO FACTURA (numero_factura, fecha_emision, metodo_pago, estado, id_cliente, id_sede, id_empleado) "
                    + "VALUES (?, NOW(), ?, ?, ?, ?, ?)";
            try (PreparedStatement sentencia = conexion.prepareStatement(sqlFactura, Statement.RETURN_GENERATED_KEYS)) {
                sentencia.setString(1, numero);
                sentencia.setString(2, form.getMetodoPago());
                sentencia.setString(3, form.getEstado());
                sentencia.setInt(4, form.getIdCliente());
                sentencia.setInt(5, form.getIdSede());
                sentencia.setInt(6, form.getIdEmpleado());
                sentencia.executeUpdate();
                try (ResultSet llaves = sentencia.getGeneratedKeys()) {
                    llaves.next();
                    idFactura = llaves.getLong(1);
                }
            }

            // 3 y 4. líneas, con el precio vigente y el descuento de existencias
            String sqlLinea = "INSERT INTO DETALLE_FACTURA (id_factura, num_linea, id_servicio, id_medicamento, cantidad, "
                    + "precio_unitario, descuento_pct) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertarLinea = conexion.prepareStatement(sqlLinea)) {
                int numLinea = 1;
                for (LineaFacturaFormDTO linea : form.getLineas()) {
                    BigDecimal precio;
                    if (linea.getIdServicio() != null) {
                        precio = precioServicio(conexion, linea.getIdServicio());
                        insertarLinea.setInt(3, linea.getIdServicio());
                        insertarLinea.setNull(4, Types.INTEGER);
                    } else {
                        precio = venderMedicamento(conexion, form.getIdSede(), linea.getIdMedicamento(), linea.getCantidad());
                        insertarLinea.setNull(3, Types.INTEGER);
                        insertarLinea.setInt(4, linea.getIdMedicamento());
                    }
                    insertarLinea.setLong(1, idFactura);
                    insertarLinea.setInt(2, numLinea);
                    insertarLinea.setInt(5, linea.getCantidad());
                    insertarLinea.setBigDecimal(6, precio);
                    insertarLinea.setBigDecimal(7, linea.getDescuentoPct());
                    insertarLinea.executeUpdate();
                    numLinea++;
                }
            }
            conexion.commit();
            return idFactura;
        } catch (SQLException e) {
            Transaccion.deshacer(conexion);
            throw ErroresSQL.traducir(e);
        } catch (RuntimeException e) {
            Transaccion.deshacer(conexion);
            throw e;
        } finally {
            Transaccion.cerrar(conexion);
        }
    }

    /**
     * Cambia el estado de una factura. Reglas: una PENDIENTE puede pasar a PAGADA; una PAGADA o PENDIENTE puede
     * ANULARSE, y entonces las existencias de sus medicamentos vuelven al inventario de la sede. Una factura anulada
     * ya no cambia. Las líneas nunca se modifican: una factura emitida no se edita, se anula.
     *
     * @param id identificador de la factura.
     * @param nuevo PAGADA o ANULADA.
     * @return {@code false} si la factura no existe.
     * @throws ConflictoDatosException si el cambio no está permitido.
     */
    public boolean cambiarEstado(long id, String nuevo) {
        Connection conexion = null;
        try {
            conexion = conexionBD.conectar();
            conexion.setAutoCommit(false);
            String actual = null;
            int idSede = 0;
            try (PreparedStatement sentencia = conexion.prepareStatement("SELECT estado, id_sede FROM FACTURA WHERE id_factura = ? FOR UPDATE")) {
                sentencia.setLong(1, id);
                try (ResultSet rs = sentencia.executeQuery()) {
                    if (rs.next()) {
                        actual = rs.getString("estado");
                        idSede = rs.getInt("id_sede");
                    }
                }
            }
            if (actual == null) {
                Transaccion.deshacer(conexion);
                return false;
            }
            if ("ANULADA".equals(actual)) {
                throw new ConflictoDatosException("La factura ya está anulada.");
            }
            if ("PAGADA".equals(nuevo) && !"PENDIENTE".equals(actual)) {
                throw new ConflictoDatosException("Solo se puede registrar el pago de una factura PENDIENTE.");
            }
            if ("ANULADA".equals(nuevo)) {
                // se agrupa por medicamento por si la factura vendió el mismo medicamento en varias líneas
                String sqlDevolver = "UPDATE INVENTARIO i JOIN ( "
                        + "SELECT id_medicamento, SUM(cantidad) AS cantidad FROM DETALLE_FACTURA "
                        + "WHERE id_factura = ? AND id_medicamento IS NOT NULL GROUP BY id_medicamento ) d "
                        + "ON d.id_medicamento = i.id_medicamento "
                        + "SET i.stock_actual = i.stock_actual + d.cantidad, i.fecha_actualizacion = NOW() "
                        + "WHERE i.id_sede = ?";
                try (PreparedStatement sentencia = conexion.prepareStatement(sqlDevolver)) {
                    sentencia.setLong(1, id);
                    sentencia.setInt(2, idSede);
                    sentencia.executeUpdate();
                }
            }
            try (PreparedStatement sentencia = conexion.prepareStatement("UPDATE FACTURA SET estado = ? WHERE id_factura = ?")) {
                sentencia.setString(1, nuevo);
                sentencia.setLong(2, id);
                sentencia.executeUpdate();
            }
            conexion.commit();
            return true;
        } catch (SQLException e) {
            Transaccion.deshacer(conexion);
            throw ErroresSQL.traducir(e);
        } catch (RuntimeException e) {
            Transaccion.deshacer(conexion);
            throw e;
        } finally {
            Transaccion.cerrar(conexion);
        }
    }

    /** Lee el precio vigente de un servicio dentro de la transacción. */
    private BigDecimal precioServicio(Connection conexion, int idServicio) throws SQLException {
        try (PreparedStatement sentencia = conexion.prepareStatement("SELECT precio FROM SERVICIO WHERE id_servicio = ?")) {
            sentencia.setInt(1, idServicio);
            try (ResultSet rs = sentencia.executeQuery()) {
                if (!rs.next()) {
                    throw new ConflictoDatosException("El servicio " + idServicio + " no existe.");
                }
                return rs.getBigDecimal("precio");
            }
        }
    }

    /**
     * Lee el precio de venta de un medicamento y descuenta las existencias de la sede. El UPDATE solo se aplica si
     * hay suficientes unidades ({@code stock_actual >= ?}); si no cambia ninguna fila, se explica por qué.
     *
     * @return el precio de venta vigente.
     */
    private BigDecimal venderMedicamento(Connection conexion, int idSede, int idMedicamento, int cantidad) throws SQLException {
        String nombre;
        BigDecimal precio;
        try (PreparedStatement sentencia = conexion.prepareStatement(
                "SELECT nombre, presentacion, precio_venta FROM MEDICAMENTO WHERE id_medicamento = ?")) {
            sentencia.setInt(1, idMedicamento);
            try (ResultSet rs = sentencia.executeQuery()) {
                if (!rs.next()) {
                    throw new ConflictoDatosException("El medicamento " + idMedicamento + " no existe.");
                }
                nombre = rs.getString("nombre") + " " + rs.getString("presentacion");
                precio = rs.getBigDecimal("precio_venta");
            }
        }
        String sqlDescontar = "UPDATE INVENTARIO SET stock_actual = stock_actual - ?, fecha_actualizacion = NOW() "
                + "WHERE id_sede = ? AND id_medicamento = ? AND stock_actual >= ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sqlDescontar)) {
            sentencia.setInt(1, cantidad);
            sentencia.setInt(2, idSede);
            sentencia.setInt(3, idMedicamento);
            sentencia.setInt(4, cantidad);
            if (sentencia.executeUpdate() == 1) {
                return precio;
            }
        }
        // no se descontó: o la sede no maneja el medicamento, o no alcanza
        try (PreparedStatement sentencia = conexion.prepareStatement(
                "SELECT stock_actual FROM INVENTARIO WHERE id_sede = ? AND id_medicamento = ?")) {
            sentencia.setInt(1, idSede);
            sentencia.setInt(2, idMedicamento);
            try (ResultSet rs = sentencia.executeQuery()) {
                if (!rs.next()) {
                    throw new ConflictoDatosException("La sede no tiene " + nombre + " en su inventario.");
                }
                throw new ConflictoDatosException("No hay existencias suficientes de " + nombre + ": hay "
                        + rs.getInt("stock_actual") + " y se pidieron " + cantidad + ".");
            }
        }
    }
}
