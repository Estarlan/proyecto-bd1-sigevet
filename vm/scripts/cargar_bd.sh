#!/bin/bash
# Crea la base sigevet, los usuarios y carga todo (dentro de la VM).
# Uso: bash vm/scripts/cargar_bd.sh              instala
#      bash vm/scripts/cargar_bd.sh --reiniciar  borra la base y la vuelve a cargar
set -euo pipefail
RAIZ="$(cd "$(dirname "$0")/../.." && pwd)"
source "$RAIZ/vm/claves.env"
cd "$RAIZ/database"

# las claves van en un archivo de opciones temporal para no escribirlas en la línea de comandos
como_root() { mysql --defaults-extra-file=<(printf '[client]\nuser=root\npassword=%s\n' "$MYSQL_ROOT_PWD") "$@"; }
como_vetdb() { mysql --defaults-extra-file=<(printf '[client]\nuser=vetdb\npassword=%s\n' "$VETDB_PWD") "$@"; }

if ! como_vetdb -e "SELECT 1" >/dev/null 2>&1; then
  echo "== Creando la base sigevet y los usuarios vetdb y vet_app (como root)"
  # las claves entran por la entrada estándar, así no aparecen en la lista de procesos
  { printf "SET @clave_vetdb = '%s'; SET @clave_vet_app = '%s';\n" "$VETDB_PWD" "$VET_APP_PWD"
    echo "SOURCE 01_ddl/00_crear_usuarios.sql"; } | como_root
fi
if [ "${1:-}" = "--reiniciar" ]; then
  echo "== Borrando la base sigevet"
  como_vetdb < 03_varios/03_reiniciar_base.sql
fi
echo "== Instalando el esquema SIGEVET"
inicio=$(date +%s)
como_vetdb --default-character-set=utf8mb4 -t < instalar_esquema.sql
echo "Tiempo: $(( $(date +%s) - inicio )) s"
