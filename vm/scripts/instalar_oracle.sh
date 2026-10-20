#!/bin/bash
# Instala Oracle Database Free en Oracle Linux 9 (dentro de la VM, como root).
# Uso: sudo bash vm/scripts/instalar_oracle.sh [ruta_del_rpm]
# Si no se da la ruta, busca el .rpm en vm/cache/. El RPM se descarga de
# https://www.oracle.com/database/free/get-started/ (Oracle Linux 9, x86_64).
set -euo pipefail
RAIZ="$(cd "$(dirname "$0")/../.." && pwd)"
source "$RAIZ/vm/claves.env"

RPM="${1:-$(ls "$RAIZ"/vm/cache/oracle-*database-free*.rpm 2>/dev/null | head -1)}"
if [ ! -f "$RPM" ]; then
  echo "No encuentro el RPM de Oracle. Descárguelo y páselo como parámetro."
  exit 1
fi

echo "== Sistema: $(cat /etc/oracle-release) | kernel $(uname -r) | $(nproc) CPU | $(free -h | awk '/Mem/{print $2}') RAM"
echo "== 1/4 Paquete de preinstalación (usuario oracle, grupos, parámetros del kernel)"
dnf -y -q install oracle-ai-database-preinstall-26ai || dnf -y -q install oracle-database-preinstall-23ai
rpm -qa | grep -i preinstall

echo "== 2/4 Instalando $RPM"
dnf -y -q localinstall "$RPM"

echo "== 3/4 Configuración: CDB FREE, PDB FREEPDB1 y listener en el puerto 1521"
INIT=$(ls /etc/init.d/oracle-free-* | head -1)
printf '%s\n%s\n' "$ORACLE_PWD" "$ORACLE_PWD" | "$INIT" configure

echo "== 4/4 Arranque automático y firewall"
systemctl enable "$(basename "$INIT")" 2>&1 || chkconfig "$(basename "$INIT")" on
if systemctl is-active --quiet firewalld; then
  firewall-cmd --permanent --add-port=1521/tcp
  firewall-cmd --reload
fi
"$INIT" status || true
echo "== Instalación terminada"
