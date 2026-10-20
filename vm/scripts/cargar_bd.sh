#!/bin/bash
# Crea los usuarios y carga el esquema SIGEVET (dentro de la VM).
# Uso: bash vm/scripts/cargar_bd.sh              instala
#      bash vm/scripts/cargar_bd.sh --reiniciar  borra los objetos de VETDB y vuelve a cargar
set -euo pipefail
RAIZ="$(cd "$(dirname "$0")/../.." && pwd)"
source "$RAIZ/vm/claves.env"
export ORACLE_HOME=$(ls -d /opt/oracle/product/*/dbhomeFree | head -1)
export PATH="$ORACLE_HOME/bin:$PATH"
export NLS_LANG=AMERICAN_AMERICA.AL32UTF8
CONEXION=localhost:1521/FREEPDB1
cd "$RAIZ/database"

if ! echo "SELECT 1 FROM dual;" | sqlplus -s -L "VETDB/${VETDB_PWD}@${CONEXION}" >/dev/null 2>&1; then
  echo "== Creando usuarios VETDB y VET_APP (como SYSTEM)"
  sqlplus -s -L "system/${ORACLE_PWD}@${CONEXION}" @01_ddl/00_crear_usuarios.sql "$VETDB_PWD" "$VET_APP_PWD"
fi
if [ "${1:-}" = "--reiniciar" ]; then
  echo "== Eliminando objetos anteriores de VETDB"
  sqlplus -s -L "VETDB/${VETDB_PWD}@${CONEXION}" @03_varios/04_eliminar_objetos.sql </dev/null
fi
echo "== Instalando el esquema SIGEVET"
sqlplus -s -L "VETDB/${VETDB_PWD}@${CONEXION}" @instalar_esquema.sql
