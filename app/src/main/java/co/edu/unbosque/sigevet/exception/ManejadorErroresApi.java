package co.edu.unbosque.sigevet.exception;

import co.edu.unbosque.sigevet.dto.ErrorApiDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Convierte las excepciones de los controladores de la API en respuestas JSON con el código HTTP correcto, para que
 * Angular reciba siempre un {@link ErrorApiDTO} en lugar de una página HTML de error.
 *
 * <p>Atiende los controladores del paquete {@code controller.api}, que son todos los de la aplicación.</p>
 */
@RestControllerAdvice(basePackages = "co.edu.unbosque.sigevet.controller.api")
public class ManejadorErroresApi {

    /**
     * El registro pedido no existe (por ejemplo {@code /api/clientes/9999}): responde 404.
     *
     * @param e excepción lanzada por la capa de servicio.
     * @return respuesta 404 con el mensaje de la excepción.
     */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorApiDTO> noEncontrado(RecursoNoEncontradoException e) {
        ErrorApiDTO error = new ErrorApiDTO(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * No hay sesión o el correo y el documento no corresponden a un empleado activo: responde 401.
     *
     * @param e excepción lanzada por el inicio de sesión.
     * @return respuesta 401 con el mensaje de la excepción.
     */
    @ExceptionHandler(NoAutorizadoException.class)
    public ResponseEntity<ErrorApiDTO> noAutorizado(NoAutorizadoException e) {
        ErrorApiDTO error = new ErrorApiDTO(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * Un parámetro de la dirección no tiene el tipo esperado (por ejemplo {@code /api/clientes/abc}): responde 400.
     *
     * @param e excepción que lanza Spring al no poder convertir el parámetro.
     * @return respuesta 400 que indica qué parámetro está mal.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorApiDTO> parametroInvalido(MethodArgumentTypeMismatchException e) {
        ErrorApiDTO error = new ErrorApiDTO(HttpStatus.BAD_REQUEST.value(),
                "El parámetro '" + e.getName() + "' no es válido.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Un formulario llegó con datos incompletos o mal escritos (validación del servicio): responde 400.
     *
     * @param e excepción con el mensaje que nombra el campo.
     * @return respuesta 400 con ese mensaje.
     */
    @ExceptionHandler(DatosInvalidosException.class)
    public ResponseEntity<ErrorApiDTO> datosInvalidos(DatosInvalidosException e) {
        ErrorApiDTO error = new ErrorApiDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * La base o una regla de negocio rechazó la operación (UNIQUE, CHECK, FK, disparador, borrado con historial,
     * existencias insuficientes): responde 409.
     *
     * @param e excepción con el mensaje del conflicto.
     * @return respuesta 409 con ese mensaje.
     */
    @ExceptionHandler(ConflictoDatosException.class)
    public ResponseEntity<ErrorApiDTO> conflicto(ConflictoDatosException e) {
        ErrorApiDTO error = new ErrorApiDTO(HttpStatus.CONFLICT.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * El cuerpo de la petición no es un JSON válido o un valor no tiene el tipo esperado: responde 400.
     *
     * @param e excepción que lanza Jackson al leer el cuerpo.
     * @return respuesta 400.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorApiDTO> cuerpoInvalido(HttpMessageNotReadableException e) {
        ErrorApiDTO error = new ErrorApiDTO(HttpStatus.BAD_REQUEST.value(),
                "Los datos enviados no tienen el formato esperado (revise números y fechas).");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Falta un parámetro obligatorio de la dirección (por ejemplo {@code ?sede=}): responde 400.
     *
     * @param e excepción de Spring.
     * @return respuesta 400 que nombra el parámetro.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorApiDTO> parametroFaltante(MissingServletRequestParameterException e) {
        ErrorApiDTO error = new ErrorApiDTO(HttpStatus.BAD_REQUEST.value(),
                "Falta el parámetro '" + e.getParameterName() + "'.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Cualquier otro error (por ejemplo, la base de datos no responde): responde 500. El detalle se escribe en la
     * consola del servidor y no se envía al navegador.
     *
     * @param e excepción no controlada.
     * @return respuesta 500 con un mensaje general.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorApiDTO> errorInterno(Exception e) {
        System.err.println("Error en la API: " + e.getMessage());
        ErrorApiDTO error = new ErrorApiDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocurrió un error en el servidor. Intente de nuevo.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
