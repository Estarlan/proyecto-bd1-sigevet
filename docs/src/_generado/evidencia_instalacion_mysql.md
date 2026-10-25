<!-- Created: 18:15 19-Oct-2026 -->

**Instalación y configuración de MySQL 8.4** (salida real, archivo `vm/logs/02_instalacion_mysql.log`):

```text
== Sistema: Oracle Linux Server release 9.8 | kernel 6.12.0-204.92.4.2.el9uek.x86_64 | 4 CPU | 5.8Gi RAM
== 1/4 Paquetes de MySQL 8.4 (repositorio AppStream de Oracle Linux)

Installed:
  mysql-8.4.11-1.module+el9.8.0+90998+76ddfd45.x86_64
  mysql-common-8.4.11-1.module+el9.8.0+90998+76ddfd45.noarch
  mysql-errmsg-8.4.11-1.module+el9.8.0+90998+76ddfd45.noarch
  mysql-selinux-1.0.14-1.el9_6.noarch
  mysql-server-8.4.11-1.module+el9.8.0+90998+76ddfd45.x86_64

mysql-server-8.4.11-1.module+el9.8.0+90998+76ddfd45.x86_64
== 2/4 Configuración (/etc/my.cnf.d/sigevet.cnf)
== 3/4 Servicio mysqld con arranque automático
Created symlink /etc/systemd/system/multi-user.target.wants/mysqld.service → /usr/lib/systemd/system/mysqld.service.
active
Clave de root configurada
== 4/4 Firewall (puerto 3306)
success
success
mysql  Ver 8.4.11 for Linux on x86_64 (Source distribution)
== MySQL instalado
EXIT 0
```
