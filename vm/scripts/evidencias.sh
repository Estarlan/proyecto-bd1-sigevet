#!/bin/bash
# Reúne evidencias de la VM y del RDBMS (ejecutar dentro de la VM). Uso: bash vm/scripts/evidencias.sh vm|rdbms|vet_app
set -uo pipefail
RAIZ="$(cd "$(dirname "$0")/../.." && pwd)"
source "$RAIZ/vm/claves.env"
paso() { echo; echo "\$ $*"; eval "$@" 2>&1; }
como() { mysql --defaults-extra-file=<(printf '[client]\nuser=%s\npassword=%s\n' "$1" "$2") -t "${@:3}"; }
case "${1:-vm}" in
  vm)
    paso "cat /etc/oracle-release"
    paso "uname -srm"
    paso "hostnamectl | grep -E 'Static hostname|Operating System|Kernel|Architecture|Virtualization'"
    paso "lscpu | grep -E '^CPU\\(s\\)|Model name|Hypervisor vendor|Virtualization type'"
    paso "free -h"
    paso "df -h /"
    paso "ip -brief address"
    paso "getenforce"
    paso "systemctl is-active sshd"
    ;;
  rdbms)
    paso "rpm -q mysql-server mysql"
    paso "systemctl is-enabled mysqld"
    paso "systemctl is-active mysqld"
    paso "cat /etc/my.cnf.d/sigevet.cnf"
    paso "ss -ltn | grep 3306"
    echo; echo '$ mysql -u vetdb -p -t  (versión, base, tamaño y juego de caracteres)'
    como vetdb "$VETDB_PWD" sigevet <<'SQL'
SELECT VERSION() AS version, DATABASE() AS base, CURRENT_USER() AS usuario;
SELECT ROUND(SUM(DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024, 2) AS mb_base_sigevet
FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'sigevet';
SELECT DEFAULT_CHARACTER_SET_NAME AS charset, DEFAULT_COLLATION_NAME AS collation
FROM information_schema.SCHEMATA WHERE SCHEMA_NAME = 'sigevet';
SHOW GRANTS;
SQL
    ;;
  vet_app)
    echo '$ mysql -u vet_app -p -t --force < vm/scripts/prueba_vet_app.sql'
    como vet_app "$VET_APP_PWD" --force < "$RAIZ/vm/scripts/prueba_vet_app.sql"
    for sentencia in "UPDATE sigevet.SERVICIO SET precio = 1" "INSERT INTO sigevet.ESPECIE VALUES (99, 'Prueba')" "DROP TABLE sigevet.CITA"; do
      echo "\$ $sentencia"
      como vet_app "$VET_APP_PWD" -e "$sentencia" 2>&1
    done
    ;;
esac
