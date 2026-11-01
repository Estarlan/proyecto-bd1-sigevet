package co.edu.unbosque.sigevet.model;

import java.util.Objects;

/**
 * Ejercicio de álgebra relacional leído del script SQL del proyecto: su enunciado y la consulta SQL
 * equivalente. La página de álgebra ejecuta el SQL y muestra el resultado.
 */
public class EjercicioAlgebra {
    /** Número del ejercicio dentro del script. */
    private int numero;
    /** Título corto del ejercicio. */
    private String titulo;
    /** Grupo temático al que pertenece (selección, reunión, división, etc.). */
    private String grupo;
    /** Enunciado del ejercicio. */
    private String enunciado;
    /** Consulta SQL que resuelve el ejercicio. */
    private String sql;

    /** Constructor vacío: los atributos se llenan con los setters al leer el script. */
    public EjercicioAlgebra() {
    }

    /**
     * Crea el objeto {@code EjercicioAlgebra} con todos sus atributos.
     *
     * @param numero número del ejercicio dentro del script.
     * @param titulo título corto del ejercicio.
     * @param grupo grupo temático al que pertenece (selección, reunión, división, etc.).
     * @param enunciado enunciado del ejercicio.
     * @param sql consulta SQL que resuelve el ejercicio.
     */
    public EjercicioAlgebra(int numero, String titulo, String grupo, String enunciado, String sql) {
        this.numero = numero;
        this.titulo = titulo;
        this.grupo = grupo;
        this.enunciado = enunciado;
        this.sql = sql;
    }

    /** @return el número del ejercicio. */
    public int getNumero() {
        return numero;
    }

    /** @param numero nuevo número del ejercicio. */
    public void setNumero(int numero) {
        this.numero = numero;
    }

    /** @return el título del ejercicio. */
    public String getTitulo() {
        return titulo;
    }

    /** @param titulo nuevo título. */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /** @return el grupo temático del ejercicio. */
    public String getGrupo() {
        return grupo;
    }

    /** @param grupo nuevo grupo temático. */
    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    /** @return el enunciado del ejercicio. */
    public String getEnunciado() {
        return enunciado;
    }

    /** @param enunciado nuevo enunciado. */
    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    /** @return la consulta SQL del ejercicio. */
    public String getSql() {
        return sql;
    }

    /** @param sql nueva consulta SQL. */
    public void setSql(String sql) {
        this.sql = sql;
    }

    /**
     * Compara este {@code EjercicioAlgebra} con otro objeto. Dos objetos son iguales cuando son de la misma clase
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
        EjercicioAlgebra that = (EjercicioAlgebra) o;
        return numero == that.numero
                && Objects.equals(titulo, that.titulo)
                && Objects.equals(grupo, that.grupo)
                && Objects.equals(enunciado, that.enunciado)
                && Objects.equals(sql, that.sql);
    }

    /**
     * Calcula el código hash a partir de los mismos atributos que usa {@link #equals(Object)}, para que dos
     * objetos iguales tengan siempre el mismo código.
     *
     * @return el código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(numero, titulo, grupo, enunciado, sql);
    }

    /**
     * Devuelve el contenido del {@code EjercicioAlgebra} como texto, con el nombre y el valor de cada atributo.
     * Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "EjercicioAlgebra{numero=" + numero
                + ", titulo=" + titulo
                + ", grupo=" + grupo
                + ", enunciado=" + enunciado
                + ", sql=" + sql
                + "}";
    }
}
