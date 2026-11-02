package co.edu.unbosque.sigevet.connection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Ayudas para manejar transacciones a mano con JDBC, sin ningún framework.
 *
 * <p>Cuando una operación escribe en varias tablas (por ejemplo, la factura y sus líneas), el DAO hace:</p>
 * <pre>
 * conexion.setAutoCommit(false);   // empieza la transacción
 * ... varios INSERT / UPDATE ...
 * conexion.commit();               // si todo salió bien, se guarda todo junto
 * // si algo falla: Transaccion.deshacer(conexion) → ROLLBACK, no queda nada a medias
 * </pre>
 */
public class Transaccion {

    /**
     * Deshace la transacción en curso (ROLLBACK). No lanza excepciones: se usa dentro de un catch.
     *
     * @param conexion conexión con la transacción abierta; puede ser {@code null} si no se alcanzó a abrir.
     */
    public static void deshacer(Connection conexion) {
        if (conexion == null) {
            return;
        }
        try {
            conexion.rollback();
        } catch (SQLException e) {
            System.err.println("No se pudo deshacer la transacción: " + e.getMessage());
        }
    }

    /**
     * Cierra la conexión. Se usa en el finally.
     *
     * <p>Si la transacción quedó abierta (por un error inesperado que no pasó por un catch), primero se deshace.
     * No se llama a {@code setAutoCommit(true)}: en JDBC eso CONFIRMA lo pendiente, y lo que se quiere es lo
     * contrario. Después de un {@code commit()} normal el rollback no tiene efecto.</p>
     *
     * @param conexion conexión a cerrar; puede ser {@code null}.
     */
    public static void cerrar(Connection conexion) {
        if (conexion == null) {
            return;
        }
        try {
            if (!conexion.getAutoCommit()) {
                conexion.rollback();
            }
            conexion.close();
        } catch (SQLException e) {
            System.err.println("No se pudo cerrar la conexión: " + e.getMessage());
        }
    }
}
