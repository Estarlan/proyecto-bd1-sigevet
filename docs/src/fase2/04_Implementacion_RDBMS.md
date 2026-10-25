---
title: "Implementación del RDBMS"
subtitle: "Documento de Implementación del RDBMS"
fase: "Fase 2"
version: "1.0"
---
<!-- Created: 18:15 19-Oct-2026 -->
<!-- Updated: 18:36 19-Oct-2026 -->
<!-- Updated: 18:40 19-Oct-2026 -->
<!-- Updated: 18:42 19-Oct-2026 -->
<!-- Updated: 12:10 21-Oct-2026 -->

# Introducción

Este documento describe la instalación de **MySQL 8.4 LTS Community Edition** en Oracle Linux 9, la creación de la base `sigevet` y sus usuarios, y las pruebas de funcionamiento. La versión instalada es **8.4.11**. MySQL Workbench se ejecuta en el equipo anfitrión para administrar y consultar la base.

La VM definitiva corre con KVM en el computador servidor (un Lenovo con Linux) y se crea con `vm/migrar_linux.sh`; la simulación en macOS utiliza Lima. Primero se probó Oracle Database Free y esa versión quedó como respaldo. Las evidencias incluidas corresponden a la implementación actual con MySQL.

# Arquitectura de la instalación

| Elemento | Valor |
|---|---|
| Motor | MySQL 8.4.11 LTS Community Edition; tablas InnoDB |
| Paquetes | `mysql-server` y `mysql`, del módulo oficial `mysql:8.4` de AppStream |
| Base de datos | `sigevet` |
| Servicio | `mysqld`, habilitado para arrancar con la VM |
| Conexión | TCP, puerto `3306`; reenvío al anfitrión o IP de la VM en modo puente |
| Configuración del proyecto | `/etc/my.cnf.d/sigevet.cnf` |
| Dueño y usuario de la aplicación | `vetdb` y `vet_app`, respectivamente |
| Juego de caracteres y cotejamiento | `utf8mb4` / `utf8mb4_0900_ai_ci` |

Table: Arquitectura de la instalación

# Procedimiento de instalación

Desde la carpeta del proyecto, dentro de la VM, se ejecuta el instalador como administrador. El script lee la clave de `root` del archivo local `vm/claves.env`, que no se versiona:

```bash
sudo bash vm/scripts/instalar_mysql.sh
```

| Paso | Comando | Qué hace |
|---|---|---|
| 1 | `dnf -y -q module enable mysql:8.4` y `dnf -y -q install mysql-server mysql` | Instala el servidor y el cliente desde AppStream, sin descargar RPM externos; incluye la política `mysql-selinux` |
| 2 | Escribir `/etc/my.cnf.d/sigevet.cnf` | Define la escucha de red, los caracteres, la zona horaria y la creación de funciones |
| 3 | `systemctl enable --now mysqld` y `ALTER USER 'root'@'localhost' ...` | Inicia el servicio, habilita su arranque automático y establece la clave inicial de `root` si aún no tiene una |
| 4 | `firewall-cmd --permanent --add-port=3306/tcp` y `firewall-cmd --reload` | Abre el puerto de MySQL cuando `firewalld` está activo |

Table: Pasos de instalación del RDBMS

<!-- INCLUDE: _generado/evidencia_instalacion_mysql.md -->

# Configuración

| Parámetro | Valor | Motivo |
|---|---|---|
| `bind-address` | `0.0.0.0` | Permite entrar desde Workbench en el anfitrión a través de las interfaces de la VM |
| `character-set-server` | `utf8mb4` | Conserva tildes, ñ y el resto de caracteres Unicode de los datos |
| `collation-server` | `utf8mb4_0900_ai_ci` | Define el cotejamiento de texto, sin distinguir mayúsculas ni acentos en sus comparaciones |
| `default-time-zone` | `'-05:00'` | Usa la zona horaria de Colombia para las fechas y horas de registro |
| `log_bin_trust_function_creators` | `1` | Con el registro binario activo, permite crear funciones y disparadores sin conceder el privilegio global `SUPER` a `vetdb` |

Table: Configuración de MySQL para SIGEVET

El script `vm/scripts/evidencias.sh rdbms` consulta los paquetes, el estado de `mysqld`, la escucha en `3306`, el tamaño y los permisos. La salida guardada confirma el servicio habilitado y activo:

<!-- INCLUDE: _generado/evidencia_rdbms.md -->

# Usuarios, rol y seguridad

`database/01_ddl/00_crear_usuarios.sql` se ejecuta como `root` mediante `vm/scripts/cargar_bd.sh`. Crea la base y separa la administración de los objetos del acceso de la aplicación:

| Usuario / rol | Privilegios | Uso |
|---|---|---|
| `root@localhost` | Administración del servidor | Instalación y creación inicial de usuarios |
| `vetdb@%` | `ALL PRIVILEGES ON sigevet.*` | Dueño de los objetos; sus permisos se limitan a la base `sigevet` |
| `rol_sigevet_lectura@%` | `SELECT, SHOW VIEW ON sigevet.*` | Agrupa los permisos para consultar tablas y vistas |
| `vet_app@%` | `rol_sigevet_lectura` como rol por defecto | Conexión de la aplicación web, solo lectura |

Table: Usuarios y privilegios

Los usuarios `vetdb` y `vet_app` aceptan conexiones desde otros equipos mediante `%`. El rol se declara sin host, lo que equivale a `%`. Esto permite usar las cuentas desde Workbench; `root` permanece en `localhost`. Las claves están en `vm/claves.env`, excluido del repositorio.

# Prueba de conexión y de privilegios

La evidencia de `vm/scripts/evidencias.sh vet_app` muestra que `vet_app` puede leer los datos y tiene activo el rol de lectura. Los intentos de `DELETE`, `UPDATE`, `INSERT` y `DROP` son rechazados con el **error 1142**:

<!-- INCLUDE: _generado/evidencia_conexion.md -->

# Conexión con MySQL Workbench

Se utiliza **MySQL Workbench 8.0.44** en el equipo anfitrión. Se crea una conexión nueva de tipo **Standard (TCP/IP)** con `Hostname: 127.0.0.1`, `Port: 3306`, `Username: vetdb` y `Default Schema: sigevet`. Se introduce la clave local de `vetdb` al conectar.

`127.0.0.1:3306` requiere el reenvío del puerto de la VM al anfitrión. Desde otro equipo de la red se utiliza la IP del computador servidor, porque los puertos 3306 y 5000 quedan abiertos a la red local. Desde Workbench se pueden ejecutar los scripts SQL, revisar el esquema y abrir el modelo EER `SIGEVET.mwb` de la Fase 1, con notación Crow's Foot.

| Herramienta | Cadena o parámetros de conexión |
|---|---|
| Cliente `mysql` | `mysql --default-character-set=utf8mb4 -h 127.0.0.1 -P 3306 -u vetdb -p sigevet` |
| MySQL Workbench | Host `127.0.0.1`, puerto `3306`, usuario `vetdb`, base por defecto `sigevet` |
| Aplicación Java con Connector/J (Servlets y JSP en Tomcat) | `jdbc:mysql://localhost:3306/sigevet`; usuario `vet_app` y clave por separado |

Table: Cadenas de conexión

La URL JDBC con `localhost` corresponde a Tomcat dentro de la VM. Si el cliente está en otro equipo sin reenvío, se reemplaza el host por la IP de la VM.

# Operación

| Acción | Comando (dentro de la VM) |
|---|---|
| Estado del motor | `systemctl status mysqld` |
| Reiniciar el motor | `sudo systemctl restart mysqld` |
| Crear usuarios y cargar el esquema | `bash vm/scripts/cargar_bd.sh` |
| Borrar la base y cargarla de nuevo | `bash vm/scripts/cargar_bd.sh --reiniciar` |
| Consultar versión, objetos e ingresos por sede | `bash vm/scripts/resumen_bd.sh` |
| Estado de la aplicación web | `systemctl status sigevet-web` |

Table: Comandos de operación

La opción `--reiniciar` elimina la base antes de reconstruirla; se utiliza para repetir la carga de prueba.

# Resultados

La carga completa tarda aproximadamente **1–2 s**; `vm/logs/04_carga_bd.log` registra **1 s**, finalización correcta y los siguientes objetos:

| Resultado | Cantidad |
|---|---|
| Tablas | 22 |
| Vistas | 6 |
| Funciones | 2 |
| Procedimientos | 3 |
| Disparadores | 9 |
| Índices | 71 |
| Pruebas de reglas de negocio | 10 de 10 OK |

Table: Resultados de la carga y verificación

Las pruebas comprueban claves foráneas, restricciones `CHECK` y `UNIQUE`, y reglas implementadas con disparadores. La base con sus datos e índices ocupa **1,08 MB**, según la suma de `DATA_LENGTH + INDEX_LENGTH` en `information_schema.TABLES`, mostrada en la evidencia del RDBMS.

# Consideraciones y soluciones

| Situación | Solución |
|---|---|
| Codificación de los datos | NLS ya no aplica. La base usa `utf8mb4` y la carga se ejecuta con `--default-character-set=utf8mb4`; la prueba conserva Bogotá D.C. y Chía |
| Claves durante la carga y las consultas de evidencia | `cargar_bd.sh`, `evidencias.sh` y `resumen_bd.sh` usan un archivo de opciones temporal con `--defaults-extra-file`, para que la clave de conexión no aparezca en la línea de comandos |
| Nombres de tablas en Linux | Se respeta la escritura de los nombres, porque MySQL distingue mayúsculas y minúsculas en este entorno |
| Expresiones en valores por defecto | El analizador de Workbench no reconoce `DEFAULT (CURRENT_DATE)`; las fechas de registro utilizan `DATETIME DEFAULT CURRENT_TIMESTAMP` |
| Un disparador cubre un solo evento | Las reglas compartidas se definen en procedimientos llamados desde los disparadores de `INSERT` y `UPDATE` |
| Ausencia de `FULL OUTER JOIN` | El ejercicio 29 utiliza `LEFT JOIN UNION RIGHT JOIN` |

Table: Consideraciones de la implementación
