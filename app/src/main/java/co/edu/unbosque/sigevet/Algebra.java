package co.edu.unbosque.sigevet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Algebra {
    private final List<Map<String, Object>> ejercicios = new ArrayList<>();

    public Algebra(@Value("${sigevet.algebra:../database/03_varios/01_consultas_algebra_relacional.sql}") String archivo)
            throws IOException {
        String texto = Files.readString(Path.of(archivo), StandardCharsets.UTF_8);
        Pattern encabezado = Pattern.compile("^--\\s*Ejercicio\\s+(\\d+)\\.\\s*(.+)$", Pattern.MULTILINE);
        Pattern separador = Pattern.compile("^-- =+\\R-- (.+)\\R-- =+", Pattern.MULTILINE);
        List<MatchResult> partes = encabezado.matcher(texto).results().toList();
        String grupo = "General";

        for (int posicion = 0; posicion < partes.size(); posicion++) {
            MatchResult parte = partes.get(posicion);
            int anterior = posicion == 0 ? 0 : partes.get(posicion - 1).end();
            Matcher grupos = separador.matcher(texto.substring(anterior, parte.start()));
            while (grupos.find()) {
                grupo = grupos.group(1).strip();
            }
            int numero = Integer.parseInt(parte.group(1));
            int fin = posicion + 1 < partes.size() ? partes.get(posicion + 1).start() : texto.length();
            String bloque = texto.substring(parte.end(), fin);
            List<String> enunciado = new ArrayList<>();
            StringBuilder consulta = new StringBuilder();
            boolean inicioSql = false;
            for (String linea : bloque.lines().toList()) {
                String limpia = linea.stripLeading();
                if (limpia.toUpperCase(Locale.ROOT).startsWith("SELECT")) {
                    inicioSql = true;
                }
                if (limpia.startsWith("--")) {
                    if (!inicioSql) {
                        enunciado.add(limpia.substring(2).strip());
                    }
                } else {
                    consulta.append(linea).append('\n');
                }
            }
            String sql = consulta.toString().strip();
            if (!sql.endsWith(";")) {
                throw new IllegalArgumentException("Falta el punto y coma del ejercicio " + numero);
            }
            sql = sql.substring(0, sql.length() - 1).stripTrailing();
            if (!sql.toUpperCase(Locale.ROOT).startsWith("SELECT")) {
                throw new IllegalArgumentException("El ejercicio " + numero + " debe ser solo SELECT");
            }
            Map<String, Object> ejercicio = new LinkedHashMap<>();
            ejercicio.put("numero", numero);
            ejercicio.put("titulo", parte.group(2).strip());
            ejercicio.put("grupo", grupo);
            ejercicio.put("enunciado", String.join(" ", enunciado));
            ejercicio.put("sql", sql);
            ejercicios.add(ejercicio);
        }
    }

    public List<Map<String, Object>> ejercicios() {
        return ejercicios;
    }
}
