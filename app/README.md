# SIGEVET

Aplicación de consulta de la base de datos de la red veterinaria Huella Sana.
Requiere Java 21 y acceso a Oracle. Los comandos se ejecutan desde `app/`.

Configura la conexión con estas variables:

```bash
export SIGEVET_DB_USER=VET_APP
export SIGEVET_DB_PASSWORD='tu clave'
export SIGEVET_DB_URL='jdbc:oracle:thin:@//localhost:1521/FREEPDB1'
```

Compila y ejecuta:

```bash
./mvnw package
java -jar target/sigevet-0.0.1-SNAPSHOT.jar
```

Abre `http://localhost:5000`. La aplicación arranca aunque Oracle esté apagado;
las páginas que consultan datos muestran un error de conexión.

No se usa ORM: todas las consultas están escritas a mano y se ejecutan con JDBC
(`PreparedStatement`). Los 55 ejercicios se leen del archivo SQL del proyecto;
se puede cambiar su ruta con la propiedad `sigevet.algebra`.

En la VM se instala con `sudo bash vm/scripts/instalar_app.sh` desde la raíz del proyecto.
