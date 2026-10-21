# SIGEVET

Aplicación de consulta de la base de datos de la red veterinaria Huella Sana.
Usamos Java 21, Tomcat 10.1 y Oracle. No se usa ningún framework ni ORM:
Servlets, JSP y JDBC con PreparedStatement. Los comandos se ejecutan desde `app/`.

Configura la conexión con estas variables:

```bash
export SIGEVET_DB_PASSWORD='tu clave'
```

Opcionalmente puedes definir `SIGEVET_DB_URL` (por defecto
`jdbc:oracle:thin:@//localhost:1521/FREEPDB1`) y `SIGEVET_DB_USER` (por defecto
`VET_APP`). La conexión usa el esquema `VETDB`.

Compila:

```bash
./mvnw package
```

Se genera `target/ROOT.war`. Copia ese archivo a `webapps/` de Tomcat y arranca
Tomcat con las variables de entorno definidas. Abre `http://localhost:8080/`.

Para los 55 ejercicios se usa `SIGEVET_ALGEBRA`. Si no está definida, se lee
`../database/03_varios/01_consultas_algebra_relacional.sql`, relativo al directorio
desde el que se arranca Tomcat. Conviene definir la variable con una ruta absoluta.

En la VM todo lo hace `sudo bash vm/scripts/instalar_app.sh` desde la raíz del proyecto.
