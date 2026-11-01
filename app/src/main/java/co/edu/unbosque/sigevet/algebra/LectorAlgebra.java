package co.edu.unbosque.sigevet.algebra;

import co.edu.unbosque.sigevet.model.EjercicioAlgebra;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

/**
 * Lee los ejercicios de álgebra relacional desde el script SQL del proyecto
 * ({@code database/03_varios/01_consultas_algebra_relacional.sql}).
 *
 * <p>El archivo tiene un formato fijo que esta clase interpreta línea por línea:</p>
 * <ul>
 *   <li>Un título de grupo entre dos líneas que empiezan con {@code -- ===}.</li>
 *   <li>Cada ejercicio empieza con una línea {@code -- Ejercicio N. Título}.</li>
 *   <li>Las líneas de comentario siguientes forman el enunciado.</li>
 *   <li>Después viene la consulta {@code SELECT}, que termina en punto y coma.</li>
 * </ul>
 *
 * <p>Solo se aceptan consultas {@code SELECT}: si algún ejercicio trae otra cosa o SQL adicional, se lanza un error
 * y la consulta no se ejecuta.</p>
 */
@Component
public class LectorAlgebra {

    /**
     * Lee el archivo completo y arma la lista de ejercicios en el orden en que aparecen.
     *
     * <p>La ruta del archivo se toma de la variable de entorno {@code SIGEVET_ALGEBRA}; si no existe, se usa la ruta
     * relativa al proyecto por defecto.</p>
     *
     * @return la lista de ejercicios con su número, título, grupo, enunciado y SQL.
     * @throws RuntimeException si el archivo no se puede leer o tiene un ejercicio mal formado.
     */
    public ArrayList<EjercicioAlgebra> listar() {
        // ruta del script: variable de entorno o, si no está definida, la ruta por defecto del repositorio
        String archivo = System.getenv("SIGEVET_ALGEBRA");
        if (archivo == null || archivo.isBlank()) {
            archivo = "../database/03_varios/01_consultas_algebra_relacional.sql";
        }
        ArrayList<EjercicioAlgebra> ejercicios = new ArrayList<EjercicioAlgebra>();
        // ejercicio que se está armando; vale null cuando no hay ninguno en curso
        EjercicioAlgebra ejercicio = null;
        // grupo vigente: se asigna a cada ejercicio que se encuentre después de su título
        String grupo = "General";
        StringBuilder enunciado = new StringBuilder();
        StringBuilder consulta = new StringBuilder();
        // 0 = fuera de un título de grupo, 1 = se abrió la línea "-- ===", 2 = ya se leyó el nombre del grupo
        int estadoGrupo = 0;
        // pasa a true cuando empieza el SQL; desde ahí las líneas ya no son parte del enunciado
        boolean inicioSql = false;

        try (BufferedReader lector = Files.newBufferedReader(Path.of(archivo), StandardCharsets.UTF_8)) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                String limpia = linea.trim();
                if (limpia.startsWith("-- Ejercicio ")) {
                    // empieza un ejercicio nuevo; el anterior debía haber terminado con punto y coma
                    if (ejercicio != null) {
                        throw new RuntimeException("Falta el punto y coma del ejercicio " + ejercicio.getNumero());
                    }
                    // el encabezado tiene la forma "N. Título": se separa en el primer punto
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
                    // línea decorativa: la primera abre el título del grupo y la segunda lo cierra
                    if (estadoGrupo == 2) {
                        estadoGrupo = 0;
                    } else {
                        estadoGrupo = 1;
                    }
                } else if (estadoGrupo == 1 && limpia.startsWith("--")) {
                    // la línea entre las dos decorativas es el nombre del grupo
                    grupo = limpia.substring(2).trim();
                    estadoGrupo = 2;
                } else if (ejercicio != null && limpia.startsWith("--")) {
                    // comentario dentro de un ejercicio: parte del enunciado, separado por espacios
                    if (!inicioSql) {
                        if (enunciado.length() > 0) {
                            enunciado.append(' ');
                        }
                        enunciado.append(limpia.substring(2).trim());
                    }
                } else if (ejercicio != null && !limpia.isEmpty()) {
                    // línea de SQL: la primera debe empezar con SELECT (solo se permiten consultas de lectura)
                    if (!inicioSql && !limpia.toUpperCase().startsWith("SELECT")) {
                        throw new RuntimeException("El ejercicio " + ejercicio.getNumero() + " debe ser solo SELECT");
                    }
                    inicioSql = true;
                    consulta.append(linea).append('\n');
                    // el punto y coma marca el final de la consulta
                    int fin = consulta.indexOf(";");
                    if (fin >= 0) {
                        // no puede haber nada después del punto y coma (evita SQL oculto)
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
            // si el archivo terminó con un ejercicio a medias, el script está incompleto
            if (ejercicio != null) {
                throw new RuntimeException("Falta el punto y coma del ejercicio " + ejercicio.getNumero());
            }
            return ejercicios;
        } catch (IOException error) {
            System.err.println("Error al leer el archivo de álgebra: " + error.getMessage());
            throw new RuntimeException("No fue posible leer los ejercicios de álgebra", error);
        }
    }

    /**
     * Busca un ejercicio por su número.
     *
     * @param numero número del ejercicio dentro del script.
     * @return el ejercicio encontrado, o {@code null} si no existe ninguno con ese número.
     */
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
