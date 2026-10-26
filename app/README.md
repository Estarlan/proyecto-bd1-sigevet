# SIGEVET

Aplicación de consulta para el proyecto de Bases de Datos 1. Usa Java 21,
Spring Boot 3.5.14, Thymeleaf y MySQL. Escucha en el puerto 5000 y solo lee datos.

## Compilar y ejecutar

Desde la carpeta `app`, con un JDK 21 instalado:

```sh
./mvnw package
java -jar target/sigevet.jar
```

Si el wrapper no tiene permiso de ejecución, usa `sh ./mvnw package`.
Abre `http://localhost:5000`. El jar ya incluye el servidor web.

## Variables de entorno

| Variable | Valor predeterminado |
| --- | --- |
| `SIGEVET_DB_URL` | `jdbc:mysql://localhost:3306/sigevet` |
| `SIGEVET_DB_USER` | `vet_app` |
| `SIGEVET_DB_PASSWORD` | Cadena vacía |
| `SIGEVET_ALGEBRA` | `../database/03_varios/01_consultas_algebra_relacional.sql` |

Exporta estas variables antes de ejecutar el jar. `vet_app` debe tener solo
permiso de lectura. Si usas la ruta relativa de álgebra, ejecuta desde `app`.
Para correr el jar desde otra carpeta, configura una ruta absoluta en
`SIGEVET_ALGEBRA`. El archivo se lee línea por línea cuando se abre álgebra.

## Estructura

Todo el código está en `src/main/java/co/edu/unbosque/sigevet`:

- `SigevetApplication`: inicia Spring Boot.
- `conexion`: `ConexionBD` lee las propiedades y abre conexiones con `DriverManager`.
- `modelo`: objetos con atributos privados, constructor vacío, getters y setters.
- `dao`: consultas SQL escritas a mano, parámetros con `PreparedStatement` y lectura de `ResultSet`.
- `controlador`: recibe las rutas, consulta los DAO y entrega los datos al `Model`.
- `algebra`: lector de los 55 ejercicios agrupados del archivo SQL.

Las vistas están en `src/main/resources/templates`. El menú y el pie son
fragmentos compartidos. El CSS original está en `src/main/resources/static`.

## JDBC sin ORM

No se usa ORM porque el objetivo de la materia es entender las tablas, las
uniones, los filtros y los cálculos SQL. Cada DAO abre su conexión, prepara la
consulta, crea los objetos con setters y cierra los recursos con
`try-with-resources`. Si MySQL falla, imprime el error y lanza una excepción
con un mensaje en español. Los totales y descuentos se calculan en SQL.

## Herencia y polimorfismo

`Persona` es abstracta. Tiene los datos comunes, `getNombreCompleto()` y el
método abstracto `getDescripcion()`. `Cliente` y `Empleado` heredan de ella.
`Veterinario` y `Auxiliar` heredan de `Empleado`. Cada clase concreta sobrescribe
`getDescripcion()` con `@Override`.

En `PersonalDAO`, el campo `tipo_empleado` decide si se crea un `Veterinario`,
un `Auxiliar` o un `Empleado`. Todos se guardan en un `ArrayList<Empleado>`.
La vista `personal.html` llama a `fila.getDescripcion()`: Java ejecuta la
versión del objeto concreto aunque la lista sea de empleados. Esa llamada
muestra el polimorfismo. Las especialidades se cargan con una segunda consulta.

## Páginas

- `/`: conteos de las 22 tablas, ingresos pagados, citas y stock bajo.
- `/clientes?buscar=` y `/clientes/{id}`: búsqueda de hasta 100 clientes y detalle.
- `/mascotas/{id}`: mascota, historia clínica, consultas y prescripciones.
- `/agenda?sede=&estado=`, `/inventario?sede=` y `/personal?sede=`.
- `/facturas?sede=&estado=` y `/facturas/{id}`: facturación y líneas.
- `/algebra` y `/algebra/{numero}`: enunciado, SQL y resultado genérico.

Un registro inexistente devuelve 404. Los fallos de consulta muestran una página
500 sencilla sin publicar credenciales ni detalles internos.

## Instalación en la VM

Desde la raíz del repositorio: `sudo bash vm/scripts/instalar_app.sh`.
Instala Java 21, compila con `sh ./mvnw -q package` y copia el jar y el SQL
a `/opt/sigevet`. Lee `VET_APP_PWD` de `vm/claves.env` para escribir el archivo
protegido `/opt/sigevet/app.env`. Crea el servicio `sigevet-web`, que ejecuta
el jar con el usuario sin privilegios `sigevet`.
