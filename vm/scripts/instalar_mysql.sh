#!/bin/bash
# Instala MySQL 8.4 LTS en Oracle Linux 9 (dentro de la VM, como root) y lo deja listo
# para conectarse desde otro equipo con MySQL Workbench.
# Uso: sudo bash vm/scripts/instalar_mysql.sh
set -euo pipefail
RAIZ="$(cd "$(dirname "$0")/../.." && pwd)"
source "$RAIZ/vm/claves.env"

echo "== Sistema: $(cat /etc/oracle-release) | kernel $(uname -r) | $(nproc) CPU | $(free -h | awk '/Mem/{print $2}') RAM"
echo "== 1/4 Paquetes de MySQL 8.4 (repositorio AppStream de Oracle Linux)"
dnf -y -q module enable mysql:8.4
dnf -y -q install mysql-server mysql
rpm -q mysql-server

echo "== 2/4 Configuración (/etc/my.cnf.d/sigevet.cnf)"
cat > /etc/my.cnf.d/sigevet.cnf <<FIN
[mysqld]
# escuchar en todas las interfaces para poder entrar desde Workbench en el equipo anfitrión
bind-address = 0.0.0.0
character-set-server = utf8mb4
collation-server = utf8mb4_0900_ai_ci
default-time-zone = '-05:00'
# permite que vetdb cree funciones y disparadores sin privilegio SUPER (el binlog está activo)
log_bin_trust_function_creators = 1
FIN

echo "== 3/4 Servicio mysqld con arranque automático"
systemctl enable --now mysqld
systemctl is-active mysqld
# La primera vez root no tiene clave: se le pone la de vm/claves.env
if mysql -u root -e "SELECT 1" >/dev/null 2>&1; then
  mysql -u root -e "ALTER USER 'root'@'localhost' IDENTIFIED BY '$MYSQL_ROOT_PWD';"
  echo "Clave de root configurada"
fi

echo "== 4/4 Firewall (puerto 3306)"
if systemctl is-active --quiet firewalld; then
  firewall-cmd --permanent --add-port=3306/tcp
  firewall-cmd --reload
fi
mysql --version
echo "== MySQL instalado"
