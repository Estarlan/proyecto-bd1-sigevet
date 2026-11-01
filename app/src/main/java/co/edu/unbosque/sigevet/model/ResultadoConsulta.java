package co.edu.unbosque.sigevet.model;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Resultado genérico de ejecutar una consulta SQL: los nombres de las columnas y las filas como texto.
 *
 * <p>Se usa en la página de álgebra, donde cada ejercicio devuelve columnas distintas y por eso no se
 * puede usar una clase fija para cada resultado.</p>
 */
public class ResultadoConsulta {
    /** Nombres de las columnas, en el orden en que las devolvió la consulta. */
    private ArrayList<String> columnas = new ArrayList<String>();
    /** Filas del resultado. Cada fila es una lista de valores en el mismo orden que las columnas. */
    private ArrayList<ArrayList<String>> filas = new ArrayList<ArrayList<String>>();

    /** Constructor vacío: las listas empiezan vacías y se llenan al leer el resultado. */
    public ResultadoConsulta() {
    }

    /**
     * Crea el objeto {@code ResultadoConsulta} con todos sus atributos.
     *
     * @param columnas nombres de las columnas, en el orden en que las devolvió la consulta.
     * @param filas filas del resultado.
     */
    public ResultadoConsulta(ArrayList<String> columnas, ArrayList<ArrayList<String>> filas) {
        this.columnas = columnas;
        this.filas = filas;
    }

    /** @return los nombres de las columnas. */
    public ArrayList<String> getColumnas() {
        return columnas;
    }

    /** @param columnas nuevos nombres de columnas. */
    public void setColumnas(ArrayList<String> columnas) {
        this.columnas = columnas;
    }

    /** @return las filas del resultado. */
    public ArrayList<ArrayList<String>> getFilas() {
        return filas;
    }

    /** @param filas nuevas filas del resultado. */
    public void setFilas(ArrayList<ArrayList<String>> filas) {
        this.filas = filas;
    }

    /**
     * Compara este {@code ResultadoConsulta} con otro objeto. Dos objetos son iguales cuando son de la misma clase
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
        ResultadoConsulta that = (ResultadoConsulta) o;
        return Objects.equals(columnas, that.columnas)
                && Objects.equals(filas, that.filas);
    }

    /**
     * Calcula el código hash a partir de los mismos atributos que usa {@link #equals(Object)}, para que dos
     * objetos iguales tengan siempre el mismo código.
     *
     * @return el código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(columnas, filas);
    }

    /**
     * Devuelve el contenido del {@code ResultadoConsulta} como texto, con el nombre y el valor de cada atributo.
     * Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "ResultadoConsulta{columnas=" + columnas
                + ", filas=" + filas
                + "}";
    }
}
