package co.edu.unbosque.sigevet;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component("formato")
public class Formato {
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public String fecha(Object valor) {
        if (valor == null) {
            return "-";
        }
        if (valor instanceof LocalDateTime fecha) {
            return fecha.format(fecha.toLocalTime().withNano(0).equals(LocalTime.MIDNIGHT) ? FECHA : FECHA_HORA);
        }
        if (valor instanceof LocalDate fecha) {
            return fecha.format(FECHA);
        }
        return valor.toString();
    }

    public String pesos(Object valor) {
        DecimalFormat formato = new DecimalFormat("#,##0", DecimalFormatSymbols.getInstance(Locale.GERMANY));
        formato.setRoundingMode(RoundingMode.HALF_EVEN);
        return "$ " + formato.format(valor == null ? 0 : valor);
    }
}
