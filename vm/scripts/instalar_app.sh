#!/bin/bash
# Instala la aplicación (API en Spring Boot + frontend en Angular) dentro de la VM y la deja como servicio.
# El frontend se compila con Node.js y queda dentro del mismo .jar: todo se sirve en el puerto 5000.
# Uso: sudo bash vm/scripts/instalar_app.sh
# Después la app queda en http://<ip-de-la-vm>:5000
set -euo pipefail
RAIZ="$(cd "$(dirname "$0")/../.." && pwd)"
source "$RAIZ/vm/claves.env"
USUARIO=sigevet

echo "== 1/6 Java 21 y Node.js 22"
dnf -y -q install java-21-openjdk-devel tar
# Angular 21 necesita Node.js 20.19 o superior; Oracle Linux 9 lo trae como módulo de AppStream
dnf -y -q module reset nodejs
dnf -y -q module enable nodejs:22
dnf -y -q install nodejs npm
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk
export PATH="$JAVA_HOME/bin:$PATH"
java -version 2>&1 | head -1
echo "Node.js $(node --version)"

echo "== 2/6 Usuario y directorio de la aplicación"
if ! id "$USUARIO" >/dev/null 2>&1; then
  useradd --system --home-dir /opt/sigevet --shell /sbin/nologin "$USUARIO"
fi
mkdir -p /opt/sigevet

echo "== 3/6 Compilando el frontend en Angular (la primera vez npm descarga las dependencias)"
rm -rf /opt/sigevet/fuente
mkdir -p /opt/sigevet/fuente
cp -r "$RAIZ/app" /opt/sigevet/fuente/app
# se copia el frontend sin node_modules ni dist: npm ci los reconstruye desde package-lock.json
mkdir -p /opt/sigevet/fuente/frontend
( cd "$RAIZ/frontend" && tar --exclude=node_modules --exclude=dist --exclude=.angular -cf - . ) | tar -xf - -C /opt/sigevet/fuente/frontend
cd /opt/sigevet/fuente/frontend
export NG_CLI_ANALYTICS=false   # sin preguntas interactivas de Angular CLI
npm ci --no-audit --no-fund --loglevel=error
npx ng build --configuration production

echo "== 4/6 Compilando la API y armando el jar con el frontend (la primera vez Maven descarga las dependencias)"
cd /opt/sigevet/fuente/app
sh ./mvnw -q package
cp target/sigevet.jar /opt/sigevet/sigevet.jar
cp "$RAIZ/database/03_varios/01_consultas_algebra_relacional.sql" /opt/sigevet/algebra.sql
chmod 644 /opt/sigevet/sigevet.jar /opt/sigevet/algebra.sql

echo "== 5/6 Servicio sigevet-web (Spring Boot)"
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

echo "== 6/6 Firewall"
if systemctl is-active --quiet firewalld; then
  firewall-cmd --permanent --add-port=5000/tcp
  firewall-cmd --reload
fi
sleep 15
systemctl --no-pager status sigevet-web | head -5
echo "== Listo. Abrir http://$(hostname -I | awk '{print $1}'):5000"
