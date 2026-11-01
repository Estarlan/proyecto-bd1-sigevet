package co.edu.unbosque.sigevet.dto;

import co.edu.unbosque.sigevet.model.Cliente;
import co.edu.unbosque.sigevet.model.Factura;
import co.edu.unbosque.sigevet.model.Mascota;
import co.edu.unbosque.sigevet.model.Telefono;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Objeto de transferencia de datos (DTO) con todo lo que necesita la página de un cliente.
 *
 * <p>El servicio reúne aquí datos que vienen de varios DAO (cliente, teléfonos, mascotas y facturas) y se los
 * entrega al controlador en un solo objeto. No tiene lógica: solo guarda los datos.</p>
 */
public class ClienteDetalleDTO {
    /** Datos personales del cliente. */
    private Cliente cliente;
    /** Teléfonos del cliente. */
    private ArrayList<Telefono> telefonos;
    /** Mascotas del cliente. */
    private ArrayList<Mascota> mascotas;
    /** Facturas emitidas al cliente. */
    private ArrayList<Factura> facturas;

    /**
     * Constructor vacío: crea el DTO sin datos. Todos sus atributos quedan en {@code null}.
     */
    public ClienteDetalleDTO() {
    }

    /**
     * Crea el DTO con todos sus datos.
     *
     * @param cliente datos personales del cliente.
     * @param telefonos teléfonos del cliente.
     * @param mascotas mascotas del cliente.
     * @param facturas facturas del cliente.
     */
    public ClienteDetalleDTO(Cliente cliente, ArrayList<Telefono> telefonos, ArrayList<Mascota> mascotas,
                          ArrayList<Factura> facturas) {
        this.cliente = cliente;
        this.telefonos = telefonos;
        this.mascotas = mascotas;
        this.facturas = facturas;
    }

    /** @return los datos personales del cliente. */
    public Cliente getCliente() {
        return cliente;
    }

    /** @return los teléfonos del cliente. */
    public ArrayList<Telefono> getTelefonos() {
        return telefonos;
    }

    /** @return las mascotas del cliente. */
    public ArrayList<Mascota> getMascotas() {
        return mascotas;
    }

    /** @return las facturas del cliente. */
    public ArrayList<Factura> getFacturas() {
        return facturas;
    }

    /**
     * Compara este {@code ClienteDetalleDTO} con otro objeto. Dos objetos son iguales cuando son de la misma clase
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
        ClienteDetalleDTO that = (ClienteDetalleDTO) o;
        return Objects.equals(cliente, that.cliente)
                && Objects.equals(telefonos, that.telefonos)
                && Objects.equals(mascotas, that.mascotas)
                && Objects.equals(facturas, that.facturas);
    }

    /**
     * Calcula el código hash a partir de los mismos atributos que usa {@link #equals(Object)}, para que dos
     * objetos iguales tengan siempre el mismo código.
     *
     * @return el código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(cliente, telefonos, mascotas, facturas);
    }

    /**
     * Devuelve el contenido del {@code ClienteDetalleDTO} como texto, con el nombre y el valor de cada atributo.
     * Sirve para depurar e imprimir el objeto.
     *
     * @return el texto con los atributos del objeto.
     */
    @Override
    public String toString() {
        return "ClienteDetalleDTO{cliente=" + cliente
                + ", telefonos=" + telefonos
                + ", mascotas=" + mascotas
                + ", facturas=" + facturas
                + "}";
    }
}
