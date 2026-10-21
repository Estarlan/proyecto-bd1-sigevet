#!/bin/bash
# Instala la aplicación web (Servlets + JSP) en Tomcat dentro de la VM y la deja como servicio.
# Uso: sudo bash vm/scripts/instalar_app.sh
# Después la app queda en http://<ip-de-la-vm>:5000
set -euo pipefail
RAIZ="$(cd "$(dirname "$0")/../.." && pwd)"
source "$RAIZ/vm/claves.env"
USUARIO="${SUDO_USER:-$(whoami)}"
TOMCAT=10.1.59
URL_TOMCAT=https://archive.apache.org/dist/tomcat/tomcat-10/v$TOMCAT/bin/apache-tomcat-$TOMCAT.tar.gz

echo "== 1/5 Java 21"
dnf -y -q install java-21-openjdk-devel tar
java -version 2>&1 | head -1

echo "== 2/5 Tomcat $TOMCAT en /opt/tomcat"
if [ ! -d /opt/tomcat ]; then
  cd /tmp
  curl -s -O "$URL_TOMCAT"
  curl -s "$URL_TOMCAT.sha512" | awk '{print $1 "  apache-tomcat-'$TOMCAT'.tar.gz"}' | sha512sum -c -
  tar xzf "apache-tomcat-$TOMCAT.tar.gz"
  mv "apache-tomcat-$TOMCAT" /opt/tomcat
  rm -rf /opt/tomcat/webapps/*
  # el proyecto usa el puerto 5000
  sed -i 's/port="8080"/port="5000"/' /opt/tomcat/conf/server.xml
fi
chown -R "$USUARIO" /opt/tomcat
# SELinux: Tomcat se descomprimió en /tmp, hay que corregir su etiqueta para que systemd lo pueda ejecutar
restorecon -R /opt/tomcat

echo "== 3/5 Compilando la aplicación (la primera vez Maven descarga las dependencias)"
mkdir -p /opt/sigevet
rm -rf /opt/sigevet/fuente
cp -r "$RAIZ/app" /opt/sigevet/fuente
cd /opt/sigevet/fuente
./mvnw -q package
cp "$RAIZ/database/03_varios/01_consultas_algebra_relacional.sql" /opt/sigevet/algebra.sql
rm -rf /opt/tomcat/webapps/ROOT /opt/tomcat/webapps/ROOT.war
cp target/ROOT.war /opt/tomcat/webapps/ROOT.war
chown "$USUARIO" /opt/tomcat/webapps/ROOT.war

echo "== 4/5 Servicio sigevet-web (Tomcat)"
cat > /opt/sigevet/app.env <<FIN
JAVA_HOME=/usr/lib/jvm/java-21-openjdk
SIGEVET_DB_URL=jdbc:mysql://localhost:3306/sigevet
SIGEVET_DB_USER=vet_app
SIGEVET_DB_PASSWORD=$VET_APP_PWD
SIGEVET_ALGEBRA=/opt/sigevet/algebra.sql
FIN
chmod 600 /opt/sigevet/app.env
chown "$USUARIO" /opt/sigevet/app.env

cat > /etc/systemd/system/sigevet-web.service <<FIN
[Unit]
Description=SIGEVET aplicacion web (Tomcat)
After=network-online.target mysqld.service

[Service]
User=$USUARIO
EnvironmentFile=/opt/sigevet/app.env
ExecStart=/opt/tomcat/bin/catalina.sh run
Restart=on-failure
RestartSec=5

[Install]
WantedBy=multi-user.target
FIN
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
