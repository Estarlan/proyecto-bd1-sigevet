# SIGEVET · API en Spring Boot

Backend del proyecto de Bases de Datos 1. Usa Java 21, Spring Boot 3.5.14 y MySQL. Escucha en el puerto 5000 y expone
una API REST (`/api`) de lectura y escritura con SQL escrito a mano (JDBC, sin ORM). La interfaz es el frontend en
Angular de la carpeta `../frontend`; al armar el jar, Maven lo incluye y Spring Boot lo sirve en el mismo puerto.

## Compilar y ejecutar

Hay dos formas, según se esté desarrollando o se quiera la aplicación completa.

**Desarrollo (dos procesos).** La API corre sola y Angular se sirve con `ng serve`, que recarga los cambios al guardar:

```sh
# en app/
./mvnw spring-boot:run            # API en http://localhost:5000/api
# en frontend/ (otra terminal)
npm start                         # http://localhost:4200, reenvía /api al puerto 5000
```

**Aplicación completa (un solo jar).** Primero se compila Angular y después se arma el jar, que ya lo trae adentro:

```sh
cd frontend && npm ci && npx ng build      # genera frontend/dist/sigevet-front/browser
cd ../app && ./mvnw package                # copia ese build a static/ dentro del jar
java -jar target/sigevet.jar               # http://localhost:5000 (frontend y API)
```

Si el wrapper no tiene permiso de ejecución, usa `sh ./mvnw package`. En Windows, `.\mvnw.cmd package`. Si se arma el
jar sin compilar antes Angular, el jar funciona igual pero solo con la API. Antes hay que tener la base `sigevet`
cargada y los permisos de `vet_app` (ver `database/README.md`).

## Variables de entorno

| Variable | Valor predeterminado |
| --- | --- |
| `SIGEVET_DB_URL` | `jdbc:mysql://localhost:3306/sigevet` |
| `SIGEVET_DB_USER` | `vet_app` |
| `SIGEVET_DB_PASSWORD` | Cadena vacía |
| `SIGEVET_ALGEBRA` | `../database/03_varios/01_consultas_algebra_relacional.sql` (o `database/...` si se arranca desde la raíz) |

La clave de la base va solo en `SIGEVET_DB_PASSWORD`. Para desarrollo se puede poner en `app/.env`
(`SIGEVET_DB_PASSWORD=...`), que Spring lee al arrancar y que el `.gitignore` deja fuera de GitHub. `vet_app` lee toda
la base y escribe solo en las tablas que modifica la aplicación (`database/01_ddl/05_permisos_app.sql`).

## Estructura

Todo el código está en `src/main/java/co/edu/unbosque/sigevet`:

- `SigevetApplication`: inicia Spring Boot.
- `connection`: `ConexionBD` abre conexiones con `DriverManager`; `Transaccion` deshace y cierra las transacciones
  manuales (`setAutoCommit(false)`, `commit()`, `rollback()`).
- `config`: `ConfiguracionWeb` sirve el frontend en Angular y devuelve `index.html` en las rutas que no son archivos ni
  `/api`, para que funcione recargar páginas como `/clientes/5`.
- `controller/api`: clases `...ApiController` con `@RestController`; reciben las peticiones HTTP y devuelven JSON.
- `service`: clases `...Service`; validan los formularios, aplican las reglas de negocio y coordinan los DAO.
- `dao`: SQL escrito a mano con `PreparedStatement` y lectura de `ResultSet`, incluidos los `INSERT`, `UPDATE` y
  `DELETE` y las transacciones que escriben en varias tablas (factura y líneas, empleado y subtipo).
- `model`: objetos con atributos privados, constructor vacío, getters y setters (y los enums de estados).
- `dto`: datos que viajan en la API: respuestas que juntan varios objetos (`ClienteDetalleDTO`) y formularios que
  llegan en el cuerpo de un `POST` o `PUT` (`ClienteFormDTO`).
- `exception`: `RecursoNoEncontradoException` (404), `DatosInvalidosException` (400), `ConflictoDatosException` (409)
  y `ManejadorErroresApi`, que las convierte en JSON `{estado, mensaje}`.
- `util`: `Validador` (reglas de los formularios), `ErroresSQL` (traduce los errores de MySQL) y `TextoUtil`.
- `algebra`: lector de los 55 ejercicios agrupados del archivo SQL.

Toda la API, menos `/api/sesion`, exige haber iniciado sesión: `config/FiltroSesion` responde 401 si la sesión HTTP
no tiene un empleado. El empleado entra con su correo corporativo y su número de documento (`SesionDAO` los busca en
EMPLEADO, solo activos).

El flujo de una petición es: Angular → `controller/api` → `service` → `dao` → MySQL, y de vuelta como JSON.

## JDBC sin ORM

No se usa ORM porque el objetivo de la materia es entender las tablas, las uniones, los filtros y los cálculos SQL.
Cada DAO abre su conexión, prepara la consulta, crea los objetos con setters y cierra los recursos con
`try-with-resources`. Cuando una operación escribe en varias tablas, el DAO abre una transacción a mano y, si algo
falla, hace `ROLLBACK`. Las reglas siguen en la base (CHECK, UNIQUE, FOREIGN KEY y disparadores); `ErroresSQL` traduce
lo que responde MySQL en un mensaje que nombra la restricción. Jackson solo convierte objetos a JSON y no toca la base.

## Herencia y polimorfismo

`Persona` es abstracta. Tiene los datos comunes, `getNombreCompleto()` y el método abstracto `getDescripcion()`.
`Cliente` y `Empleado` heredan de ella. `Veterinario` y `Auxiliar` heredan de `Empleado`. Cada clase concreta
sobrescribe `getDescripcion()` con `@Override`.

En `PersonalDAO`, el campo `tipo_empleado` decide si se crea un `Veterinario`, un `Auxiliar` o un `Empleado`. Todos
se guardan en un `ArrayList<Empleado>`. Al convertir la lista a JSON, cada elemento trae los campos de su clase real y
su propia `descripcion`, que la página de personal muestra: Java ejecuta la versión del objeto concreto aunque la
lista sea de empleados.

## API

| Recurso | Lectura | Escritura |
| --- | --- | --- |
| Sesión | `GET /api/sesion` | `POST` (correo y documento de un empleado activo), `DELETE` (salir) |
| Inicio | `GET /api/inicio` | — |
| Clientes | `GET /api/clientes?buscar=`, `GET /api/clientes/{id}` | `POST`, `PUT /{id}`, `DELETE /{id}` (solo sin historial) |
| Mascotas | `GET /api/mascotas?buscar=`, `GET /api/mascotas/{id}` | `POST`, `PUT /{id}`, `PATCH /{id}/estado` |
| Citas | `GET /api/citas?sede=&estado=`, `GET /api/citas/estados` | `POST`, `PATCH /{id}/estado` |
| Facturas | `GET /api/facturas?sede=&estado=`, `GET /api/facturas/{id}` | `POST` (con líneas), `PATCH /{id}/estado` |
| Inventario | `GET /api/inventario?sede=` | `POST`, `PUT /{idSede}/{idMedicamento}`, `DELETE /{idSede}/{idMedicamento}` |
| Personal | `GET /api/personal?sede=` | `POST`, `PUT /{id}`, `PATCH /{id}/estado` |
| Álgebra | `GET /api/algebra`, `GET /api/algebra/{numero}` | — |
| Catálogos | `GET /api/catalogos/{ciudades, razas, servicios, medicamentos, especialidades, veterinarios, empleados}` | — |

Cada recurso editable tiene además `GET /{id}/formulario`, con los identificadores que necesita el formulario de
edición. Las respuestas de error son `{"estado": 404, "mensaje": "No existe el cliente 99"}`.

## Instalación en la VM

Desde la raíz del repositorio: `sudo bash vm/scripts/instalar_app.sh`. Instala Java 21 y Node.js 22, compila Angular
con `npx ng build`, arma el jar con `sh ./mvnw -q package` y copia el jar y el SQL de álgebra a `/opt/sigevet`. Lee
`VET_APP_PWD` de `vm/claves.env` para escribir el archivo protegido `/opt/sigevet/app.env`. Crea el servicio
`sigevet-web`, que ejecuta el jar con el usuario sin privilegios `sigevet`.
