#!/bin/bash
# Instala la aplicación Spring Boot dentro de la VM y la deja como servicio.
# Uso: sudo bash vm/scripts/instalar_app.sh
# Después la app queda en http://<ip-de-la-vm>:5000
set -euo pipefail
RAIZ="$(cd "$(dirname "$0")/../.." && pwd)"
source "$RAIZ/vm/claves.env"
USUARIO=sigevet

echo "== 1/5 Java 21"
dnf -y -q install java-21-openjdk-devel tar
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk
export PATH="$JAVA_HOME/bin:$PATH"
java -version 2>&1 | head -1

echo "== 2/5 Usuario y directorio de la aplicación"
if ! id "$USUARIO" >/dev/null 2>&1; then
  useradd --system --home-dir /opt/sigevet --shell /sbin/nologin "$USUARIO"
fi
mkdir -p /opt/sigevet

echo "== 3/5 Compilando la aplicación (la primera vez Maven descarga las dependencias)"
rm -rf /opt/sigevet/fuente
cp -r "$RAIZ/app" /opt/sigevet/fuente
cd /opt/sigevet/fuente
sh ./mvnw -q package
cp target/sigevet.jar /opt/sigevet/sigevet.jar
cp "$RAIZ/database/03_varios/01_consultas_algebra_relacional.sql" /opt/sigevet/algebra.sql
chmod 644 /opt/sigevet/sigevet.jar /opt/sigevet/algebra.sql

echo "== 4/5 Servicio sigevet-web (Spring Boot)"
umask 077
cat > /opt/sigevet/app.env <<FIN
SIGEVET_DB_URL=jdbc:mysql://localhost:3306/sigevet
SIGEVET_DB_USER=vet_app
SIGEVET_DB_PASSWORD=$VET_APP_PWD
SIGEVET_ALGEBRA=/opt/sigevet/algebra.sql
FIN
chmod 600 /opt/sigevet/app.env
chown root:root /opt/sigevet/app.env
restorecon -R /opt/sigevet

cat > /etc/systemd/system/sigevet-web.service <<FIN
[Unit]
Description=SIGEVET aplicacion web (Spring Boot)
After=network-online.target mysqld.service

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
chmod 644 /etc/systemd/system/sigevet-web.service
umask 022
systemctl daemon-reload
systemctl enable sigevet-web
systemctl restart sigevet-web

echo "== 5/5 Firewall"
if systemctl is-active --quiet firewalld; then
  firewall-cmd --permanent --add-port=5000/tcp
  firewall-cmd --reload
fi
sleep 15
systemctl --no-pager status sigevet-web | head -5
echo "== Listo. Abrir http://$(hostname -I | awk '{print $1}'):5000"
