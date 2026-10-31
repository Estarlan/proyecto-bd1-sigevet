package co.edu.unbosque.sigevet.dto;

import co.edu.unbosque.sigevet.model.Consulta;
import co.edu.unbosque.sigevet.model.HistoriaClinica;
import co.edu.unbosque.sigevet.model.Mascota;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Objeto de transferencia de datos (DTO) con todo lo que necesita la página de una mascota: sus datos, su historia
 * clínica y sus consultas (cada una con sus prescripciones).
 *
 * <p>No tiene lógica: solo guarda los datos que el servicio reunió desde varios DAO.</p>
 */
public class MascotaDetalleDTO {
    /** Datos de la mascota. */
    private Mascota mascota;
    /** Historia clínica de la mascota; puede ser {@code null} si no tiene. */
    private HistoriaClinica historia;
    /** Consultas de la mascota, cada una con sus prescripciones ya asignadas. */
    private ArrayList<Consulta> consultas;

    /**
     * Constructor vacío: crea el DTO sin datos. Todos sus atributos quedan en {@code null}.
     */
    public MascotaDetalleDTO() {
    }

    /**
     * Crea el DTO con todos sus datos.
     *
     * @param mascota datos de la mascota.
     * @param historia historia clínica de la mascota.
     * @param consultas consultas de la mascota con sus prescripciones.
     */
    public MascotaDetalleDTO(Mascota mascota, HistoriaClinica historia, ArrayList<Consulta> consultas) {
        this.mascota = mascota;
        this.historia = historia;
        this.consultas = consultas;
    }

    /** @return los datos de la mascota. */
    public Mascota getMascota() {
        return mascota;
    }

    /** @return la historia clínica de la mascota. */
    public HistoriaClinica getHistoria() {
        return historia;
    }

    /** @return las consultas de la mascota. */
    public ArrayList<Consulta> getConsultas() {
        return consultas;
    }

    /**
     * Compara este {@code MascotaDetalleDTO} con otro objeto. Dos objetos son iguales cuando son de la misma clase
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
        MascotaDetalleDTO that = (MascotaDetalleDTO) o;
        return Objects.equals(mascota, that.mascota)
                && Objects.equals(historia, that.historia)
                && Objects.equals(consultas, that.consultas);
    }

    /**
     * Calcula el código hash a partir de los mismos atributos que usa {@link #equals(Object)}, para que dos
     * objetos iguales tengan siempre el mismo código.
     *
     * @return el código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(mascota, historia, consultas);
    }

    /**
     * Devuelve el contenido del {@code MascotaDetalleDTO} como texto, con el nombre y el valor de cada atributo.
     * Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "MascotaDetalleDTO{mascota=" + mascota
                + ", historia=" + historia
                + ", consultas=" + consultas
                + "}";
    }
}
