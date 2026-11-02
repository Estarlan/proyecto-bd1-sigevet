package co.edu.unbosque.sigevet.config;

import java.io.IOException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

/**
 * Sirve el frontend en Angular desde el mismo Spring Boot (puerto 5000).
 *
 * <p>Angular es una aplicación de una sola página: el navegador siempre carga {@code index.html} y el router de
 * Angular decide qué página mostrar. Si alguien abre directamente {@code /clientes/5} o recarga la página, Spring no
 * tiene ningún archivo con ese nombre; por eso, cuando no existe el archivo pedido, se devuelve {@code index.html}.</p>
 *
 * <p>Orden de búsqueda de cada petición:</p>
 * <ol>
 *   <li>Las rutas {@code /api/...} las atienden los controladores REST (tienen prioridad sobre los archivos).</li>
 *   <li>Si existe un archivo en {@code static/} (JavaScript, CSS, íconos), se entrega ese archivo.</li>
 *   <li>Cualquier otra ruta que no empiece por {@code api/} recibe {@code index.html} y Angular la resuelve.</li>
 * </ol>
 */
@Configuration
public class ConfiguracionWeb implements WebMvcConfigurer {
    /** Filtro que exige haber iniciado sesión para usar la API. */
    private final FiltroSesion filtroSesion;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param filtroSesion filtro de sesión de la API.
     */
    public ConfiguracionWeb(FiltroSesion filtroSesion) {
        this.filtroSesion = filtroSesion;
    }

    /**
     * Toda la API exige sesión, menos {@code /api/sesion} (iniciar, consultar y cerrar sesión). Las páginas de Angular
     * no pasan por el filtro: Angular muestra el inicio de sesión cuando la API responde 401.
     *
     * @param registry registro de filtros de Spring MVC.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(filtroSesion).addPathPatterns("/api/**").excludePathPatterns("/api/sesion");
    }

    /**
     * Registra la carpeta {@code static/} del jar (donde Maven copia el build de Angular) con la regla de respaldo.
     *
     * @param registry registro de manejadores de archivos estáticos de Spring MVC.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .resourceChain(true)
                .addResolver(new PathResourceResolver() {
                    @Override
                    protected Resource getResource(String ruta, Resource carpeta) throws IOException {
                        Resource archivo = carpeta.createRelative(ruta);
                        if (archivo.exists() && archivo.isReadable()) {
                            return archivo;
                        }
                        // una ruta de la API que no existe debe dar 404, no la página de Angular
                        if (ruta.startsWith("api/")) {
                            return null;
                        }
                        Resource indice = new ClassPathResource("/static/index.html");
                        return indice.exists() ? indice : null;
                    }
                });
    }
}
