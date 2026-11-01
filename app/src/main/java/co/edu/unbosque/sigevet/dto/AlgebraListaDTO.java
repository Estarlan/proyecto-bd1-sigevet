package co.edu.unbosque.sigevet.dto;

import co.edu.unbosque.sigevet.model.EjercicioAlgebra;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Objeto de transferencia de datos (DTO) con la lista de ejercicios de álgebra relacional y los grupos en que se
 * organizan (selección, reunión, división, agregación...), en el mismo orden del archivo SQL.
 *
 * <p>No tiene lógica: solo guarda los datos que el servicio reunió.</p>
 */
public class AlgebraListaDTO {
    /** Nombres de los grupos, en el orden en que aparecen en el archivo. */
    private ArrayList<String> grupos;
    /** Los ejercicios, cada uno con el nombre de su grupo. */
    private ArrayList<EjercicioAlgebra> ejercicios;

    /**
     * Constructor vacío: crea el DTO sin datos. Todos sus atributos quedan en {@code null}.
     */
    public AlgebraListaDTO() {
    }

    /**
     * Crea el DTO con todos sus datos.
     *
     * @param grupos nombres de los grupos.
     * @param ejercicios ejercicios de álgebra relacional.
     */
    public AlgebraListaDTO(ArrayList<String> grupos, ArrayList<EjercicioAlgebra> ejercicios) {
        this.grupos = grupos;
        this.ejercicios = ejercicios;
    }

    /** @return los nombres de los grupos. */
    public ArrayList<String> getGrupos() {
        return grupos;
    }

    /** @return los ejercicios de álgebra relacional. */
    public ArrayList<EjercicioAlgebra> getEjercicios() {
        return ejercicios;
    }

    /**
     * Compara este {@code AlgebraListaDTO} con otro objeto. Dos objetos son iguales cuando son de la misma clase y
     * todos sus atributos tienen el mismo valor.
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
        AlgebraListaDTO that = (AlgebraListaDTO) o;
        return Objects.equals(grupos, that.grupos) && Objects.equals(ejercicios, that.ejercicios);
    }

    /**
     * Calcula el código hash a partir de los mismos atributos que usa {@link #equals(Object)}.
     *
     * @return el código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(grupos, ejercicios);
    }

    /**
     * Devuelve el contenido del {@code AlgebraListaDTO} como texto. Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "AlgebraListaDTO{grupos=" + grupos + ", ejercicios=" + ejercicios + "}";
    }
}
