package co.edu.unbosque.sigevet.algebra;

import co.edu.unbosque.sigevet.modelo.EjercicioAlgebra;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

@Component
public class LectorAlgebra {

    public ArrayList<EjercicioAlgebra> listar() {
        String archivo = System.getenv("SIGEVET_ALGEBRA");
        if (archivo == null || archivo.isBlank()) {
            archivo = "../database/03_varios/01_consultas_algebra_relacional.sql";
        }
        ArrayList<EjercicioAlgebra> ejercicios = new ArrayList<EjercicioAlgebra>();
        EjercicioAlgebra ejercicio = null;
        String grupo = "General";
        StringBuilder enunciado = new StringBuilder();
        StringBuilder consulta = new StringBuilder();
        int estadoGrupo = 0;
        boolean inicioSql = false;

        try (BufferedReader lector = Files.newBufferedReader(Path.of(archivo), StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                String limpia = linea.trim();
                if (limpia.startsWith("-- Ejercicio ")) {
                    if (ejercicio != null) {
                        throw new RuntimeException("Falta el punto y coma del ejercicio " + ejercicio.getNumero());
                    }
                    String encabezado = limpia.substring(13);
                    int punto = encabezado.indexOf('.');
                    ejercicio = new EjercicioAlgebra();
                    ejercicio.setNumero(Integer.parseInt(encabezado.substring(0, punto).trim()));
                    ejercicio.setTitulo(encabezado.substring(punto + 1).trim());
                    ejercicio.setGrupo(grupo);
                    enunciado = new StringBuilder();
                    consulta = new StringBuilder();
                    inicioSql = false;
                    estadoGrupo = 0;
                } else if (limpia.startsWith("-- ===")) {
                    if (estadoGrupo == 2) {
                        estadoGrupo = 0;
                    } else {
                        estadoGrupo = 1;
                    }
                } else if (estadoGrupo == 1 && limpia.startsWith("--")) {
                    grupo = limpia.substring(2).trim();
                    estadoGrupo = 2;
                } else if (ejercicio != null && limpia.startsWith("--")) {
                    if (!inicioSql) {
                        if (enunciado.length() > 0) {
                            enunciado.append(' ');
                        }
                        enunciado.append(limpia.substring(2).trim());
                    }
                } else if (ejercicio != null && !limpia.isEmpty()) {
                    if (!inicioSql && !limpia.toUpperCase().startsWith("SELECT")) {
                        throw new RuntimeException("El ejercicio " + ejercicio.getNumero() + " debe ser solo SELECT");
                    }
                    inicioSql = true;
                    consulta.append(linea).append('\n');
                    int fin = consulta.indexOf(";");
                    if (fin >= 0) {
                        if (!consulta.substring(fin + 1).trim().isEmpty()) {
                            throw new RuntimeException("El ejercicio " + ejercicio.getNumero() + " contiene SQL adicional");
                        }
                        ejercicio.setEnunciado(enunciado.toString());
                        ejercicio.setSql(consulta.substring(0, fin).trim());
                        ejercicios.add(ejercicio);
                        ejercicio = null;
                    }
                }
            }
            if (ejercicio != null) {
                throw new RuntimeException("Falta el punto y coma del ejercicio " + ejercicio.getNumero());
            }
            return ejercicios;
        } catch (IOException error) {
            System.err.println("Error al leer el archivo de álgebra: " + error.getMessage());
            throw new RuntimeException("No fue posible leer los ejercicios de álgebra", error);
        }
    }

    public EjercicioAlgebra buscar(int numero) {
        ArrayList<EjercicioAlgebra> ejercicios = listar();
        for (EjercicioAlgebra ejercicio : ejercicios) {
            if (ejercicio.getNumero() == numero) {
                return ejercicio;
            }
        }
        return null;
    }
}
