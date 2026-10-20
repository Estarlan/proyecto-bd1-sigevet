#!/bin/bash
# Instala la aplicación web dentro de la VM y la deja como servicio (arranca sola con la VM).
# Uso: sudo bash vm/scripts/instalar_app.sh
# Después la app queda en http://<ip-de-la-vm>:5000
set -euo pipefail
RAIZ="$(cd "$(dirname "$0")/../.." && pwd)"
source "$RAIZ/vm/claves.env"
USUARIO="${SUDO_USER:-$(whoami)}"

echo "== 1/4 Python y entorno virtual en /opt/sigevet"
dnf -y -q install python3 python3-pip
mkdir -p /opt/sigevet
python3 -m venv /opt/sigevet/venv
/opt/sigevet/venv/bin/pip install -q --upgrade pip
/opt/sigevet/venv/bin/pip install -q -r "$RAIZ/app/requirements.txt"

echo "== 2/4 Conexión de la app (usuario de solo lectura VET_APP)"
cat > /opt/sigevet/app.env <<FIN
SIGEVET_DB_USER=VET_APP
SIGEVET_DB_PASSWORD=$VET_APP_PWD
SIGEVET_DB_DSN=localhost:1521/FREEPDB1
FIN
chmod 600 /opt/sigevet/app.env
chown "$USUARIO" /opt/sigevet/app.env

echo "== 3/4 Servicio sigevet-web"
cat > /etc/systemd/system/sigevet-web.service <<FIN
[Unit]
Description=SIGEVET aplicacion web
After=network-online.target oracle-free-26ai.service

[Service]
User=$USUARIO
WorkingDirectory=$RAIZ/app
EnvironmentFile=/opt/sigevet/app.env
ExecStart=/opt/sigevet/venv/bin/python app.py
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
FIN
systemctl daemon-reload
systemctl enable --now sigevet-web

echo "== 4/4 Firewall"
if systemctl is-active --quiet firewalld; then
  firewall-cmd --permanent --add-port=5000/tcp
  firewall-cmd --reload
fi
sleep 3
systemctl --no-pager status sigevet-web | head -5
echo "== Listo. Abrir http://$(hostname -I | awk '{print $1}'):5000"
