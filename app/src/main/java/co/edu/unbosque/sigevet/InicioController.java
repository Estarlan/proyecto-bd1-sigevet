package co.edu.unbosque.sigevet;

import java.sql.SQLException;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {
    private final BaseDatos bd;

    public InicioController(BaseDatos bd) {
        this.bd = bd;
    }

    @GetMapping("/")
    public String inicio(Model modelo) throws SQLException {
        modelo.addAttribute("datos", Map.of(
                "conteos", bd.consultar(Consultas.SQL_CONTEOS),
                "ingresos", bd.consultar(Consultas.SQL_INGRESOS),
                "estados_citas", bd.consultar(Consultas.SQL_ESTADOS_CITAS),
                "stock_bajo", bd.consultar(Consultas.SQL_STOCK_BAJO),
                "proximas", bd.consultar(Consultas.SQL_PROXIMAS_CITAS)));
        modelo.addAttribute("ruta", "/");
        return "inicio";
    }
}
