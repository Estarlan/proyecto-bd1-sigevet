---
title: "Conexión de la Aplicación Web"
subtitle: "Conexión de la Aplicación Web a la Base de Datos Relacional"
fase: "Fase 2"
version: "1.0"
---
<!-- Created: 18:15 19-Oct-2026 -->

# Introducción

SIGEVET incluye una aplicación web de consulta que se conecta a la base de datos MySQL de la máquina virtual y
muestra, en tiempo real, la información de la red de clínicas: clientes, mascotas e historias clínicas, agenda,
inventario, facturas, personal y los 55 ejercicios de álgebra relacional. Su propósito es demostrar la conectividad y
la lectura de datos que pide el enunciado.

# Arquitectura

La aplicación está hecha en **Java 21 con Servlets y JSP**, sin frameworks, y se despliega como `ROOT.war` en
**Apache Tomcat 10.1**. Tomcat corre **dentro de la máquina virtual**, junto a MySQL, como un servicio del sistema
(`sigevet-web`) en el puerto 5000. Desde el equipo anfitrión solo se abre el navegador.

```text
Navegador ──HTTP 5000──▶ Tomcat ──▶ Servlets ──▶ SQL escrito a mano (Consultas.java)
                                       │              │ JDBC: DriverManager + PreparedStatement (Connector/J)
                                       ▼              ▼
                                 Páginas JSP    MySQL 8.4 · base sigevet (usuario vet_app)
```

| Archivo (`app/src/main/`) | Qué hace |
|---|---|
| `java/.../Conexion.java` | Abre la conexión con `DriverManager` usando las variables de entorno |
| `java/.../BaseDatos.java` | Ejecuta el SQL con `PreparedStatement` y `ResultSet` y devuelve las filas |
| `java/.../Consultas.java` | Las consultas SQL de cada página, escritas a mano como texto |
| `java/.../*Servlet.java` | Un servlet por página: inicio, clientes, cliente, mascota, agenda, inventario, facturas, factura, personal y álgebra |
| `java/.../Parametros.java` | Lee y valida los parámetros de la URL (números, fechas y página) |
| `java/.../Algebra.java` | Lee los 55 ejercicios del script `01_consultas_algebra_relacional.sql` |
| `java/.../Formato.java` y `webapp/WEB-INF/sigevet.tld` | Formato de fechas (dd/MM/yyyy) y de dinero ($ 364.000) para usar en las JSP |
| `webapp/WEB-INF/jsp/*.jsp` | Páginas HTML con JSP y JSTL |
| `webapp/WEB-INF/web.xml` | Páginas de error 404 y 500, y codificación UTF-8 |

Table: Archivos de la aplicación

**Sin frameworks ni ORM.** La aplicación no usa Spring, JPA, Hibernate ni ninguna herramienta que genere SQL de
forma automática. Cada servlet recibe la petición, ejecuta su consulta SQL con JDBC y le pasa las filas a una página
JSP. Así se ve la relación directa entre las consultas de la aplicación, el modelo relacional y el álgebra relacional
de la Fase 1.

# Conexión a MySQL

La conexión usa **MySQL Connector/J** (`mysql-connector-j`), el controlador JDBC oficial de MySQL. Cada consulta abre una
conexión con `DriverManager` y la cierra al terminar (`Conexion.java`):

```java
Class.forName("com.mysql.cj.jdbc.Driver");
return DriverManager.getConnection(url, usuario, clave);
```

La URL ya incluye la base de datos (`jdbc:mysql://localhost:3306/sigevet`), así que las consultas usan los nombres de
las tablas sin prefijo.

Ejemplo de una consulta (`Consultas.java`). Los `?` son parámetros que se llenan con `PreparedStatement`:

```sql
SELECT c.id_cliente, c.tipo_documento, c.num_documento, c.nombres, c.apellidos, c.email, ci.nombre AS ciudad
FROM CLIENTE c JOIN CIUDAD ci ON ci.id_ciudad = c.id_ciudad
WHERE UPPER(c.num_documento) LIKE ? OR UPPER(c.apellidos) LIKE ?
ORDER BY c.apellidos, c.nombres, c.id_cliente
OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
```

Así se ejecuta (`BaseDatos.java`, resumido):

```java
try (Connection conexion = Conexion.obtener();
     PreparedStatement sentencia = conexion.prepareStatement(sql)) {
    for (int i = 0; i < parametros.length; i++) {
        sentencia.setObject(i + 1, parametros[i]);
    }
    try (ResultSet resultado = sentencia.executeQuery()) {
        while (resultado.next()) {
            // cada fila se guarda como un Map columna -> valor
        }
    }
}
```

Y un servlet completo (`ClienteServlet.java`, resumido):

```java
@WebServlet("/clientes/*")
public class ClienteServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) ... {
        Long id = Parametros.id(request.getPathInfo());
        var cliente = BaseDatos.consultarUno(Consultas.SQL_CLIENTE, id);
        if (cliente == null) { response.sendError(404); return; }
        ...
        request.setAttribute("datos", datos);
        request.getRequestDispatcher("/WEB-INF/jsp/cliente.jsp").forward(request, response);
    }
}
```

| Dato | Valor usado en el proyecto | Variable de entorno |
|---|---|---|
| Usuario | `vet_app` (solo lectura) | `SIGEVET_DB_USER` |
| Clave | No se escribe en el código | `SIGEVET_DB_PASSWORD` |
| URL JDBC | `jdbc:mysql://localhost:3306/sigevet` (la app corre en la misma VM) | `SIGEVET_DB_URL` |

Table: Configuración de la conexión

En la VM, `vm/scripts/instalar_app.sh` guarda estos valores en `/opt/sigevet/app.env` (con permisos solo para el
dueño), y el servicio de Tomcat los lee de ahí.

# Seguridad

- **Mínimo privilegio:** la aplicación usa `vet_app`, que solo tiene `SELECT` mediante el rol `rol_sigevet_lectura`.
  Aunque la aplicación tuviera un error, no podría modificar ni borrar datos.
- **Consultas parametrizadas:** los filtros del usuario (búsqueda, fechas, sede, estado) se pasan como parámetros
  `?` del `PreparedStatement` y nunca se pegan al texto del SQL. Así se evita la inyección de SQL.
- **Escape en las páginas:** los textos que vienen de la base se muestran con `<c:out>` de JSTL, que escapa el HTML.
- **Álgebra relacional:** la página solo ejecuta las 55 consultas del script del proyecto. La URL elige un número de
  ejercicio, no envía SQL.

# Ejecución

En la máquina virtual (forma recomendada), desde la raíz del proyecto:

```bash
sudo bash vm/scripts/instalar_app.sh     # instala Java 21 y Tomcat 10.1, compila ROOT.war y deja el servicio activo
systemctl status sigevet-web
```

En cualquier equipo con Java 21 y Tomcat 10.1:

```bash
cd app
./mvnw package                            # genera target/ROOT.war
cp target/ROOT.war <tomcat>/webapps/
export SIGEVET_DB_PASSWORD='<clave de vet_app>'
<tomcat>/bin/catalina.sh run              # http://localhost:8080
```

# Evidencias de funcionamiento

<!-- INCLUDE: _generado/evidencia_app.md -->
