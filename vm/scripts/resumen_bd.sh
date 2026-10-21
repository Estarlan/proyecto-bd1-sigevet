#!/bin/bash
# Resumen rápido de la base con el cliente mysql (para la demo y el video). Ejecutar dentro de la VM.
set -euo pipefail
RAIZ="$(cd "$(dirname "$0")/../.." && pwd)"
source "$RAIZ/vm/claves.env"
mysql --defaults-extra-file=<(printf '[client]\nuser=vetdb\npassword=%s\n' "$VETDB_PWD") -t sigevet <<'SQL'
SELECT VERSION() AS version_mysql;
SELECT TABLE_TYPE AS tipo, COUNT(*) AS cantidad FROM information_schema.TABLES
 WHERE TABLE_SCHEMA = 'sigevet' GROUP BY TABLE_TYPE;
SELECT sede, SUM(facturas) AS facturas, ROUND(SUM(ingresos)) AS ingresos
FROM V_INGRESOS_SEDE_MES GROUP BY sede ORDER BY ingresos DESC;
SQL
