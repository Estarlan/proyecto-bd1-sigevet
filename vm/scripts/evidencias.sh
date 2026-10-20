#!/bin/bash
# Reúne evidencias de la VM y del RDBMS (ejecutar dentro de la VM). Uso: bash vm/scripts/evidencias.sh vm|rdbms
set -uo pipefail
RAIZ="$(cd "$(dirname "$0")/../.." && pwd)"
paso() { echo; echo "\$ $*"; eval "$@" 2>&1; }
if [ "${1:-vm}" = "vm" ]; then
  paso "cat /etc/oracle-release"
  paso "uname -srm"
  paso "hostnamectl | grep -E 'Static hostname|Operating System|Kernel|Architecture|Virtualization'"
  paso "lscpu | grep -E '^CPU\\(s\\)|Model name|Hypervisor vendor|Virtualization type'"
  paso "free -h"
  paso "df -h /"
  paso "ip -brief address"
  paso "getenforce"
  paso "systemctl is-active sshd"
else
  export ORACLE_HOME=$(ls -d /opt/oracle/product/*/dbhomeFree | head -1)
  export PATH="$ORACLE_HOME/bin:$PATH"
  paso "rpm -qa | grep -iE 'database-free|preinstall'"
  paso "echo ORACLE_HOME=\$ORACLE_HOME"
  paso "ls /etc/init.d/ | grep -i oracle"
  paso "systemctl is-enabled oracle-free-26ai"
  paso "sudo /etc/init.d/oracle-free-26ai status"
  paso "ps -eo user,pid,cmd | grep -E 'pmon|tnslsnr' | grep -v grep"
  paso "sudo -u oracle env ORACLE_HOME=\$ORACLE_HOME \$ORACLE_HOME/bin/lsnrctl status | grep -E 'Version|Uptime|Listening|Service \"(free|freepdb1)'"
  paso "ss -ltn | grep 1521"
  source "$RAIZ/vm/claves.env"
  export NLS_LANG=AMERICAN_AMERICA.AL32UTF8
  echo; echo '$ sqlplus -s VETDB/********@localhost:1521/FREEPDB1 @evidencia_rdbms.sql'
  $ORACLE_HOME/bin/sqlplus -s "VETDB/${VETDB_PWD}@localhost:1521/FREEPDB1" @$RAIZ/vm/scripts/evidencia_rdbms.sql
fi
