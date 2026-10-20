package co.edu.unbosque.sigevet;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class ClientesController {
    private final BaseDatos bd;

    public ClientesController(BaseDatos bd) {
        this.bd = bd;
    }

    @GetMapping("/clientes")
    public String clientes(@RequestParam(defaultValue = "") String buscar,
            @RequestParam(defaultValue = "1") String pagina, Model modelo) throws SQLException {
        buscar = buscar.strip();
        buscar = buscar.substring(0, Math.min(100, buscar.length()));
        int numero;
        try {
            numero = Math.max(1, Integer.parseInt(pagina));
        } catch (NumberFormatException error) {
            numero = 1;
        }
        String consulta = "%" + buscar.toUpperCase(Locale.ROOT) + "%";
        int total = ((Number) bd.consultarUno(Consultas.SQL_TOTAL_CLIENTES, consulta, consulta).get("total")).intValue();
        int totalPaginas = Math.max(1, (total + 19) / 20);
        numero = Math.min(numero, totalPaginas);
        modelo.addAttribute("datos", Map.of("total", total, "filas",
                bd.consultar(Consultas.SQL_CLIENTES, consulta, consulta, (numero - 1) * 20, 20)));
        modelo.addAttribute("buscar", buscar).addAttribute("pagina", numero);
        modelo.addAttribute("totalPaginas", totalPaginas).addAttribute("ruta", "/clientes");
        return "clientes";
    }

    @GetMapping("/clientes/{id}")
    public String cliente(@PathVariable long id, Model modelo) throws SQLException {
        Map<String, Object> datos = new HashMap<>();
        var cliente = bd.consultarUno(Consultas.SQL_CLIENTE, id);
        if (cliente == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        datos.put("datos", cliente);
        datos.put("telefonos", bd.consultar(Consultas.SQL_TELEFONOS, id));
        datos.put("mascotas", bd.consultar(Consultas.SQL_MASCOTAS_CLIENTE, id));
        datos.put("facturas", bd.consultar(Consultas.SQL_FACTURAS_CLIENTE, id));
        modelo.addAttribute("datos", datos).addAttribute("ruta", "/clientes/" + id);
        return "cliente";
    }

    @GetMapping("/mascotas/{id}")
    public String mascota(@PathVariable long id, Model modelo) throws SQLException {
        var mascota = bd.consultarUno(Consultas.SQL_MASCOTA, id);
        if (mascota == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Map<String, Object> datos = new HashMap<>();
        datos.put("datos", mascota);
        datos.put("historia", bd.consultarUno(Consultas.SQL_HISTORIA, id));
        var consultas = bd.consultar(Consultas.SQL_CONSULTAS, id);
        var prescripciones = bd.consultar(Consultas.SQL_PRESCRIPCIONES, id);
        for (var consulta : consultas) {
            consulta.put("prescripciones", prescripciones.stream()
                    .filter(p -> Objects.equals(p.get("id_consulta"), consulta.get("id_consulta"))).toList());
        }
        datos.put("consultas", consultas);
        modelo.addAttribute("datos", datos).addAttribute("ruta", "/mascotas/" + id);
        return "mascota";
    }
}
