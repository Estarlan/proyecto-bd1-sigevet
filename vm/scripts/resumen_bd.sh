#!/bin/bash
# Resumen rápido del esquema con SQL*Plus (para la demo y el video). Ejecutar dentro de la VM.
set -euo pipefail
RAIZ="$(cd "$(dirname "$0")/../.." && pwd)"
source "$RAIZ/vm/claves.env"
export ORACLE_HOME=$(ls -d /opt/oracle/product/*/dbhomeFree | head -1)
export NLS_LANG=AMERICAN_AMERICA.AL32UTF8
"$ORACLE_HOME/bin/sqlplus" -s "VETDB/${VETDB_PWD}@localhost:1521/FREEPDB1" <<'SQL'
SET PAGESIZE 40 LINESIZE 110 FEEDBACK OFF
COLUMN banner_full FORMAT A84
COLUMN object_type FORMAT A12
COLUMN sede FORMAT A28
SELECT banner_full FROM v$version;
SELECT object_type, COUNT(*) AS cantidad FROM user_objects GROUP BY object_type ORDER BY 1;
SELECT sede, SUM(facturas) AS facturas, ROUND(SUM(ingresos)) AS ingresos
FROM V_INGRESOS_SEDE_MES GROUP BY sede ORDER BY ingresos DESC;
EXIT
SQL
