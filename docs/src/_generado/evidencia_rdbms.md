<!-- Created: 18:15 19-Oct-2026 -->

**Servicio, configuración y base sigevet en MySQL** (salida real, archivo `vm/logs/05_evidencia_rdbms.log`):

```text

$ rpm -q mysql-server mysql
mysql-server-8.4.11-1.module+el9.8.0+90998+76ddfd45.x86_64
mysql-8.4.11-1.module+el9.8.0+90998+76ddfd45.x86_64

$ systemctl is-enabled mysqld
enabled

$ systemctl is-active mysqld
active

$ cat /etc/my.cnf.d/sigevet.cnf
[mysqld]
# escuchar en todas las interfaces para poder entrar desde Workbench en el equipo anfitrión
bind-address = 0.0.0.0
character-set-server = utf8mb4
collation-server = utf8mb4_0900_ai_ci
default-time-zone = '-05:00'
# permite que vetdb cree funciones y disparadores sin privilegio SUPER (el binlog está activo)
log_bin_trust_function_creators = 1

$ ss -ltn | grep 3306
LISTEN 0      151          0.0.0.0:3306       0.0.0.0:*
LISTEN 0      70                 *:33060            *:*

$ mysql -u vetdb -p -t  (versión, base, tamaño y juego de caracteres)
+---------+---------+---------+
| version | base    | usuario |
+---------+---------+---------+
| 8.4.11  | sigevet | vetdb@% |
+---------+---------+---------+
+-----------------+
| mb_base_sigevet |
+-----------------+
|            1.08 |
+-----------------+
+---------+--------------------+
| charset | collation          |
+---------+--------------------+
| utf8mb4 | utf8mb4_0900_ai_ci |
+---------+--------------------+
+----------------------------------------------------+
| Grants for vetdb@%                                 |
+----------------------------------------------------+
| GRANT USAGE ON *.* TO `vetdb`@`%`                  |
| GRANT ALL PRIVILEGES ON `sigevet`.* TO `vetdb`@`%` |
+----------------------------------------------------+
```
