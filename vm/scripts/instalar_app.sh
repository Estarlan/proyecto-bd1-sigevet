#!/bin/bash
# Compila la aplicación web (Java + Spring Boot) dentro de la VM y la deja como servicio.
# Uso: sudo bash vm/scripts/instalar_app.sh
# Después la app queda en http://<ip-de-la-vm>:5000
set -euo pipefail
RAIZ="$(cd "$(dirname "$0")/../.." && pwd)"
source "$RAIZ/vm/claves.env"
USUARIO="${SUDO_USER:-$(whoami)}"

echo "== 1/4 Java 21"
dnf -y -q install java-21-openjdk-devel
java -version 2>&1 | head -1

echo "== 2/4 Compilando la aplicación (la primera vez Maven descarga las dependencias)"
mkdir -p /opt/sigevet
rm -rf /opt/sigevet/fuente
cp -r "$RAIZ/app" /opt/sigevet/fuente
cd /opt/sigevet/fuente
./mvnw -q -DskipTests package
cp target/sigevet-*.jar /opt/sigevet/sigevet.jar
cp "$RAIZ/database/03_varios/01_consultas_algebra_relacional.sql" /opt/sigevet/algebra.sql

echo "== 3/4 Servicio sigevet-web"
cat > /opt/sigevet/app.env <<FIN
SIGEVET_DB_URL=jdbc:oracle:thin:@//localhost:1521/FREEPDB1
SIGEVET_DB_USER=VET_APP
SIGEVET_DB_PASSWORD=$VET_APP_PWD
SIGEVET_ALGEBRA=/opt/sigevet/algebra.sql
FIN
chmod 600 /opt/sigevet/app.env
chown "$USUARIO" /opt/sigevet/app.env

cat > /etc/systemd/system/sigevet-web.service <<FIN
[Unit]
Description=SIGEVET aplicacion web
After=network-online.target oracle-free-26ai.service

[Service]
User=$USUARIO
WorkingDirectory=/opt/sigevet
EnvironmentFile=/opt/sigevet/app.env
ExecStart=/usr/bin/java -jar /opt/sigevet/sigevet.jar
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
FIN
systemctl daemon-reload
systemctl enable sigevet-web
systemctl restart sigevet-web

echo "== 4/4 Firewall"
if systemctl is-active --quiet firewalld; then
  firewall-cmd --permanent --add-port=5000/tcp
  firewall-cmd --reload
fi
sleep 15
systemctl --no-pager status sigevet-web | head -5
echo "== Listo. Abrir http://$(hostname -I | awk '{print $1}'):5000"
