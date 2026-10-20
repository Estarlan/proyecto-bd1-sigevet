package co.edu.unbosque.sigevet;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.DateTimeException;
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
public class OperacionController {
    private final BaseDatos bd;

    public OperacionController(BaseDatos bd) { this.bd = bd; }

    @GetMapping("/agenda")
    public String agenda(@RequestParam Map<String, String> filtros, Model modelo) throws SQLException {
        Integer sede = numero(filtros.get("sede")), veterinario = numero(filtros.get("veterinario"));
        String estado = texto(filtros.get("estado"));
        Timestamp desde = fecha(filtros.get("desde"), false), hasta = fecha(filtros.get("hasta"), true);
        modelo.addAttribute("filas", bd.consultar(Consultas.SQL_AGENDA,
                sede, sede, veterinario, veterinario, estado, estado, desde, desde, hasta, hasta));
        modelo.addAttribute("opciones", Map.of("sedes", bd.consultar(Consultas.SQL_SEDES),
                "veterinarios", bd.consultar(Consultas.SQL_VETERINARIOS)));
        modelo.addAttribute("estados", new String[]{"PROGRAMADA", "ATENDIDA", "CANCELADA", "NO_ASISTIO"});
        modelo.addAttribute("filtros", filtros).addAttribute("ruta", "/agenda");
        return "agenda";
    }

    @GetMapping("/inventario")
    public String inventario(@RequestParam Map<String, String> filtros, Model modelo) throws SQLException {
        Integer sede = numero(filtros.get("sede"));
        modelo.addAttribute("filas", bd.consultar(Consultas.SQL_INVENTARIO, sede, sede));
        modelo.addAttribute("opciones", Map.of("sedes", bd.consultar(Consultas.SQL_SEDES)));
        modelo.addAttribute("filtros", filtros).addAttribute("ruta", "/inventario");
        return "inventario";
    }

    @GetMapping("/facturas")
    public String facturas(@RequestParam Map<String, String> filtros, Model modelo) throws SQLException {
        Integer sede = numero(filtros.get("sede"));
        String estado = texto(filtros.get("estado"));
        modelo.addAttribute("filas", bd.consultar(Consultas.SQL_FACTURAS, sede, sede, estado, estado));
        modelo.addAttribute("opciones", Map.of("sedes", bd.consultar(Consultas.SQL_SEDES)));
        modelo.addAttribute("estados", new String[]{"PAGADA", "PENDIENTE", "ANULADA"});
        modelo.addAttribute("filtros", filtros).addAttribute("ruta", "/facturas");
        return "facturas";
    }

    @GetMapping("/facturas/{id}")
    public String factura(@PathVariable long id, Model modelo) throws SQLException {
        var factura = bd.consultarUno(Consultas.SQL_FACTURA, id);
        if (factura == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        modelo.addAttribute("datos", Map.of("datos", factura, "total", factura.get("total"),
                "lineas", bd.consultar(Consultas.SQL_LINEAS, id))).addAttribute("ruta", "/facturas/" + id);
        return "factura";
    }

    @GetMapping("/personal")
    public String personal(@RequestParam Map<String, String> filtros, Model modelo) throws SQLException {
        Integer sede = numero(filtros.get("sede"));
        var empleados = bd.consultar(Consultas.SQL_EMPLEADOS, sede, sede);
        var especialidades = bd.consultar(Consultas.SQL_ESPECIALIDADES, sede, sede);
        for (var empleado : empleados) {
            empleado.put("especialidades", especialidades.stream()
                    .filter(e -> Objects.equals(e.get("id_empleado"), empleado.get("id_empleado")))
                    .map(e -> e.get("nombre")).toList());
        }
        modelo.addAttribute("datos", Map.of("sedes", bd.consultar(Consultas.SQL_DIRECTORES),
                "empleados", empleados)).addAttribute("ruta", "/personal");
        modelo.addAttribute("filtros", filtros);
        return "personal";
    }

    private Integer numero(String valor) {
        try { int numero = Integer.parseInt(valor); return numero > 0 ? numero : null; }
        catch (NumberFormatException error) { return null; }
    }

    private String texto(String valor) { return valor == null || valor.isEmpty() ? null : valor; }

    private Timestamp fecha(String valor, boolean siguiente) {
        if (valor == null) return null;
        try { return Timestamp.valueOf(LocalDate.parse(valor).plusDays(siguiente ? 1 : 0).atStartOfDay()); }
        catch (DateTimeException error) { return null; }
    }
}
