package co.edu.unbosque.sigevet.dto;

import co.edu.unbosque.sigevet.model.Cita;
import co.edu.unbosque.sigevet.model.ItemInventario;
import co.edu.unbosque.sigevet.model.Resumen;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Objeto de transferencia de datos (DTO) con todo lo que muestra la página de inicio: los conteos generales, los
 * ingresos por sede, las citas por estado, los medicamentos con stock bajo y las próximas citas.
 *
 * <p>Antes el controlador ponía estos cinco datos por separado en el {@code Model} de Thymeleaf. En la API REST se
 * envían juntos en un solo objeto JSON. No tiene lógica: solo guarda los datos.</p>
 */
public class InicioDTO {
    /** Conteos generales (clientes, mascotas, citas, etc.). */
    private ArrayList<Resumen> conteos;
    /** Ingresos por sede. */
    private ArrayList<Resumen> ingresos;
    /** Cantidad de citas por estado. */
    private ArrayList<Resumen> estadosCitas;
    /** Medicamentos con existencias por debajo del mínimo. */
    private ArrayList<ItemInventario> stockBajo;
    /** Próximas citas programadas. */
    private ArrayList<Cita> proximas;

    /**
     * Constructor vacío: crea el DTO sin datos. Todos sus atributos quedan en {@code null}.
     */
    public InicioDTO() {
    }

    /**
     * Crea el DTO con todos sus datos.
     *
     * @param conteos conteos generales.
     * @param ingresos ingresos por sede.
     * @param estadosCitas cantidad de citas por estado.
     * @param stockBajo medicamentos con stock bajo.
     * @param proximas próximas citas.
     */
    public InicioDTO(ArrayList<Resumen> conteos, ArrayList<Resumen> ingresos, ArrayList<Resumen> estadosCitas,
                     ArrayList<ItemInventario> stockBajo, ArrayList<Cita> proximas) {
        this.conteos = conteos;
        this.ingresos = ingresos;
        this.estadosCitas = estadosCitas;
        this.stockBajo = stockBajo;
        this.proximas = proximas;
    }

    /** @return los conteos generales. */
    public ArrayList<Resumen> getConteos() {
        return conteos;
    }

    /** @return los ingresos por sede. */
    public ArrayList<Resumen> getIngresos() {
        return ingresos;
    }

    /** @return la cantidad de citas por estado. */
    public ArrayList<Resumen> getEstadosCitas() {
        return estadosCitas;
    }

    /** @return los medicamentos con stock bajo. */
    public ArrayList<ItemInventario> getStockBajo() {
        return stockBajo;
    }

    /** @return las próximas citas. */
    public ArrayList<Cita> getProximas() {
        return proximas;
    }

    /**
     * Compara este {@code InicioDTO} con otro objeto. Dos objetos son iguales cuando son de la misma clase y todos
     * sus atributos tienen el mismo valor.
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
        InicioDTO that = (InicioDTO) o;
        return Objects.equals(conteos, that.conteos)
                && Objects.equals(ingresos, that.ingresos)
                && Objects.equals(estadosCitas, that.estadosCitas)
                && Objects.equals(stockBajo, that.stockBajo)
                && Objects.equals(proximas, that.proximas);
    }

    /**
     * Calcula el código hash a partir de los mismos atributos que usa {@link #equals(Object)}.
     *
     * @return el código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(conteos, ingresos, estadosCitas, stockBajo, proximas);
    }

    /**
     * Devuelve el contenido del {@code InicioDTO} como texto. Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "InicioDTO{conteos=" + conteos
                + ", ingresos=" + ingresos
                + ", estadosCitas=" + estadosCitas
                + ", stockBajo=" + stockBajo
                + ", proximas=" + proximas
                + "}";
    }
}
