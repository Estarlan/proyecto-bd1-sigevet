package co.edu.unbosque.sigevet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de SIGEVET (Sistema Integral de Gestión Veterinaria).
 *
 * <p>{@code @SpringBootApplication} activa la configuración automática de Spring Boot y el escaneo de todos los
 * paquetes que cuelgan de {@code co.edu.unbosque.sigevet}: {@code controller}, {@code service}, {@code dao},
 * {@code connection}, {@code algebra}, etc. Cada clase marcada con {@code @Controller}, {@code @Service},
 * {@code @Repository} o {@code @Component} se registra sola como un bean.</p>
 *
 * <p>La aplicación sigue el patrón Modelo-Vista-Controlador sin ORM:
 * las vistas son las plantillas Thymeleaf, los controladores reciben las rutas web, los servicios concentran la
 * lógica y los DAO ejecutan el SQL escrito a mano con JDBC.</p>
 */
@SpringBootApplication
public class SigevetApplication {

    /**
     * Punto de entrada del programa: arranca el servidor web embebido de Spring Boot.
     *
     * @param args argumentos de línea de comandos (no se usan).
     */
    public static void main(String[] args) {
        SpringApplication.run(SigevetApplication.class, args);
    }
}
