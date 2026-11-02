package co.edu.unbosque.sigevet.util;

import co.edu.unbosque.sigevet.exception.ConflictoDatosException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Traduce los errores que devuelve MySQL al escribir (INSERT, UPDATE, DELETE) en mensajes para el usuario.
 *
 * <p>Las reglas viven en la base: las restricciones UNIQUE, CHECK y FOREIGN KEY de 01_crear_tablas.sql y los
 * disparadores de 04_programas.sql. Cuando una de ellas rechaza un dato, MySQL responde con un código de error;
 * esta clase lo reconoce y arma un mensaje que nombra la restricción, para que se vea qué regla actuó.</p>
 */
public class ErroresSQL {

    /** Mensajes para las restricciones UNIQUE y llaves primarias que se pueden violar desde los formularios. */
    private static final HashMap<String, String> MENSAJES_UNICOS = new HashMap<String, String>();

    static {
        MENSAJES_UNICOS.put("UQ_CLIENTE_TIPO_DOCUMENTO_NUM_DOCUMENTO", "Ya existe un cliente con ese tipo y número de documento.");
        MENSAJES_UNICOS.put("UQ_CLIENTE_EMAIL", "Ya existe un cliente con ese correo electrónico.");
        MENSAJES_UNICOS.put("TELEFONO_CLIENTE.PRIMARY", "El cliente tiene el mismo teléfono repetido.");
        MENSAJES_UNICOS.put("UQ_MASCOTA_MICROCHIP", "Ya existe una mascota con ese microchip.");
        MENSAJES_UNICOS.put("UQ_CITA_VETERINARIO_FECHA_HORA", "El veterinario ya tiene una cita a esa fecha y hora.");
        MENSAJES_UNICOS.put("INVENTARIO.PRIMARY", "Ese medicamento ya está en el inventario de la sede; edite sus existencias.");
        MENSAJES_UNICOS.put("UQ_EMPLEADO_TIPO_DOCUMENTO_NUM_DOCUMENTO", "Ya existe un empleado con ese tipo y número de documento.");
        MENSAJES_UNICOS.put("UQ_EMPLEADO_EMAIL", "Ya existe un empleado con ese correo electrónico.");
        MENSAJES_UNICOS.put("UQ_VETERINARIO_TARJETA_PROFESIONAL", "Ya existe un veterinario con esa tarjeta profesional.");
        MENSAJES_UNICOS.put("UQ_FACTURA_NUMERO_FACTURA", "Se repitió el consecutivo de la factura; intente de nuevo.");
    }

    /** Busca el nombre de la llave en mensajes como "Duplicate entry 'x' for key 'CLIENTE.UQ_CLIENTE_EMAIL'". */
    private static final Pattern LLAVE = Pattern.compile("for key '([^']+)'");
    /**
     * Busca el nombre de la restricción en "Check constraint 'CK_MASCOTA_SEXO' is violated." o en
     * "a foreign key constraint fails (`sigevet`.`CITA`, CONSTRAINT `FK_CITA_MASCOTA` ...)".
     */
    private static final Pattern RESTRICCION = Pattern.compile("constraint [`']([^`']+)[`']", Pattern.CASE_INSENSITIVE);

    /**
     * Convierte una {@link SQLException} en una excepción con un mensaje claro.
     *
     * @param e error devuelto por el controlador JDBC de MySQL.
     * @return una {@link ConflictoDatosException} lista para lanzar (la API responde 409), o una
     *         {@link RuntimeException} genérica si el error no viene de una regla de los datos (la API responde 500).
     */
    public static RuntimeException traducir(SQLException e) {
        System.err.println("Error de base de datos (" + e.getErrorCode() + "): " + e.getMessage());
        String mensaje = e.getMessage() == null ? "" : e.getMessage();

        // SIGNAL SQLSTATE '45000' de los disparadores: el mensaje ya viene en español desde la base
        if ("45000".equals(e.getSQLState())) {
            return new ConflictoDatosException("Regla de negocio: " + mensaje);
        }
        switch (e.getErrorCode()) {
            case 1062: { // valor repetido en una llave primaria o UNIQUE
                String llave = extraer(LLAVE, mensaje);
                String sinTabla = llave.contains(".") ? llave.substring(llave.indexOf('.') + 1) : llave;
                if (MENSAJES_UNICOS.containsKey(llave)) {
                    return new ConflictoDatosException(MENSAJES_UNICOS.get(llave));
                }
                if (MENSAJES_UNICOS.containsKey(sinTabla)) {
                    return new ConflictoDatosException(MENSAJES_UNICOS.get(sinTabla));
                }
                return new ConflictoDatosException("Ya existe un registro con ese valor (" + llave + ").");
            }
            case 3819: // CHECK
                return new ConflictoDatosException("La base rechazó el dato porque no cumple la restricción "
                        + extraer(RESTRICCION, mensaje) + ".");
            case 1451: // borrar o cambiar un padre que tiene hijos
                return new ConflictoDatosException("No se puede eliminar porque tiene registros relacionados ("
                        + extraer(RESTRICCION, mensaje) + ").");
            case 1452: // hijo que apunta a un padre que no existe
                return new ConflictoDatosException("Uno de los datos seleccionados no existe en la base ("
                        + extraer(RESTRICCION, mensaje) + ").");
            case 1048: // NOT NULL
                return new ConflictoDatosException("Falta un dato obligatorio: " + mensaje);
            case 1406: // texto más largo que la columna
                return new ConflictoDatosException("Un texto es más largo de lo que permite la base: " + mensaje);
            case 1142: // permiso denegado
                return new ConflictoDatosException("El usuario de la aplicación no tiene permiso para esta operación. "
                        + "Ejecute database/01_ddl/05_permisos_app.sql como root.");
            default:
                return new RuntimeException("No se pudo guardar en la base de datos.", e);
        }
    }

    /**
     * @return el primer grupo que encuentra la expresión en el texto, o "desconocida" si no aparece.
     */
    private static String extraer(Pattern patron, String texto) {
        Matcher m = patron.matcher(texto);
        return m.find() ? m.group(1) : "desconocida";
    }
}
