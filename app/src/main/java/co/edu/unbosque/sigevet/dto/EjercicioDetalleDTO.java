package co.edu.unbosque.sigevet.dto;

import co.edu.unbosque.sigevet.model.EjercicioAlgebra;
import co.edu.unbosque.sigevet.model.ResultadoConsulta;
import java.util.Objects;

/**
 * Objeto de transferencia de datos (DTO) con todo lo que necesita la página de un ejercicio de álgebra relacional:
 * el ejercicio (enunciado y SQL) y el resultado de ejecutarlo.
 *
 * <p>No tiene lógica: solo guarda los datos que el servicio reunió.</p>
 */
public class EjercicioDetalleDTO {
    /** Ejercicio con su título, enunciado y consulta SQL. */
    private EjercicioAlgebra ejercicio;
    /** Resultado de ejecutar el SQL del ejercicio (columnas y filas). */
    private ResultadoConsulta resultado;

    /**
     * Constructor vacío: crea el DTO sin datos. Todos sus atributos quedan en {@code null}.
     */
    public EjercicioDetalleDTO() {
    }

    /**
     * Crea el DTO con todos sus datos.
     *
     * @param ejercicio ejercicio de álgebra relacional.
     * @param resultado resultado de ejecutar su SQL.
     */
    public EjercicioDetalleDTO(EjercicioAlgebra ejercicio, ResultadoConsulta resultado) {
        this.ejercicio = ejercicio;
        this.resultado = resultado;
    }

    /** @return el ejercicio de álgebra relacional. */
    public EjercicioAlgebra getEjercicio() {
        return ejercicio;
    }

    /** @return el resultado de ejecutar el SQL del ejercicio. */
    public ResultadoConsulta getResultado() {
        return resultado;
    }

    /**
     * Compara este {@code EjercicioDetalleDTO} con otro objeto. Dos objetos son iguales cuando son de la misma clase
     * y todos sus atributos tienen el mismo valor.
     *
     * @param o objeto con el que se compara.
     * @return {@code true} si los dos objetos tienen los mismos valores; {@code false} en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EjercicioDetalleDTO that = (EjercicioDetalleDTO) o;
        return Objects.equals(ejercicio, that.ejercicio)
                && Objects.equals(resultado, that.resultado);
    }

    /**
     * Calcula el código hash a partir de los mismos atributos que usa {@link #equals(Object)}, para que dos
     * objetos iguales tengan siempre el mismo código.
     *
     * @return el código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(ejercicio, resultado);
    }

    /**
     * Devuelve el contenido del {@code EjercicioDetalleDTO} como texto, con el nombre y el valor de cada atributo.
     * Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "EjercicioDetalleDTO{ejercicio=" + ejercicio
                + ", resultado=" + resultado
                + "}";
    }
}
