#!/bin/bash
# Crea la VM de SIGEVET en un computador con Linux (o macOS) y deja todo funcionando:
# Oracle Linux 9 + MySQL 8.4 + base sigevet + aplicación web.
# Uso (desde la carpeta del proyecto, como usuario normal con sudo):  bash vm/migrar_linux.sh
set -euo pipefail
RAIZ="$(cd "$(dirname "$0")/.." && pwd)"
NOMBRE="${SIGEVET_VM:-sigevet}"
LIMA_VERSION=2.2.1
export PATH="$HOME/.local/lima/bin:$HOME/.local/bin:$PATH"

paso() { echo; echo "== $*"; }

paso "1/7 Revisando el equipo"
SO=$(uname -s)
echo "$SO $(uname -m), $(nproc 2>/dev/null || sysctl -n hw.ncpu) núcleos"
if [ "$SO" = "Linux" ]; then
  [ -e /dev/kvm ] || { echo "No existe /dev/kvm: active la virtualización (SVM / AMD-V o VT-x) en la BIOS."; exit 1; }
  RAM_GB=$(awk '/MemTotal/ {printf "%d", $2/1024/1024}' /proc/meminfo)
  TIPO_VM=qemu
else
  RAM_GB=$(( $(sysctl -n hw.memsize) / 1024 / 1024 / 1024 ))
  TIPO_VM=vz
fi
NUCLEOS=$(nproc 2>/dev/null || sysctl -n hw.ncpu)
MEMORIA=4; [ "$RAM_GB" -ge 12 ] && MEMORIA=6
CPUS=$(( NUCLEOS / 2 )); [ "$CPUS" -lt 2 ] && CPUS=2; [ "$CPUS" -gt 4 ] && CPUS=4
echo "RAM del equipo: ${RAM_GB} GB -> VM con ${MEMORIA} GB y ${CPUS} CPU"

paso "2/7 QEMU y KVM"
if [ "$SO" = "Linux" ]; then
  if command -v apt-get >/dev/null; then
    sudo apt-get update -qq && sudo apt-get install -y -qq qemu-system-x86 qemu-utils ovmf curl tar
  elif command -v dnf >/dev/null; then
    sudo dnf install -y -q qemu-kvm qemu-img edk2-ovmf curl tar
  elif command -v pacman >/dev/null; then
    sudo pacman -S --needed --noconfirm qemu-base edk2-ovmf curl tar
  elif command -v zypper >/dev/null; then
    sudo zypper -n install qemu-x86 qemu-tools qemu-ovmf-x86_64 curl tar
  else
    echo "No reconozco el gestor de paquetes; instale qemu y ovmf a mano." ; exit 1
  fi
  if [ ! -w /dev/kvm ]; then
    sudo usermod -aG kvm "$USER"
    echo "Se agregó $USER al grupo kvm. Cierre sesión, vuelva a entrar y ejecute de nuevo este script."
    exit 1
  fi
else
  echo "macOS: se usa el hipervisor de Apple (vz), no hace falta QEMU"
fi

paso "3/7 Lima $LIMA_VERSION"
if ! command -v limactl >/dev/null; then
  ARCHIVO="lima-$LIMA_VERSION-$(uname -s)-$(uname -m).tar.gz"
  URL="https://github.com/lima-vm/lima/releases/download/v$LIMA_VERSION"
  mkdir -p "$HOME/.local/lima" && cd "$HOME/.local/lima"
  curl -fsSLO "$URL/$ARCHIVO"
  curl -fsSL "$URL/SHA256SUMS" | grep " $ARCHIVO\$" | (sha256sum -c - 2>/dev/null || shasum -a 256 -c -)
  tar xzf "$ARCHIVO" && rm "$ARCHIVO"
  cd "$RAIZ"
fi
limactl --version

paso "4/7 Claves (vm/claves.env)"
if [ ! -f "$RAIZ/vm/claves.env" ]; then
  clave() { LC_ALL=C tr -dc 'A-Za-z0-9' < /dev/urandom | head -c 20; }
  printf 'MYSQL_ROOT_PWD=%s\nVETDB_PWD=%s\nVET_APP_PWD=%s\n' "$(clave)" "$(clave)" "$(clave)" > "$RAIZ/vm/claves.env"
  chmod 600 "$RAIZ/vm/claves.env"
  echo "Se generaron claves nuevas en vm/claves.env (ahí están para conectarse con Workbench)."
else
  echo "Se usan las claves que ya están en vm/claves.env"
fi

paso "5/7 Creando la VM '$NOMBRE' (Oracle Linux 9)"
mkdir -p "$RAIZ/vm/cache"
DEF="$RAIZ/vm/cache/$NOMBRE.yaml"
# misma definición de vm/sigevet-ol9.yaml, sin carpeta compartida (el proyecto se copia adentro)
# y con los puertos abiertos a la red local para entrar desde otros equipos
awk -v tipo="$TIPO_VM" -v cpus="$CPUS" -v mem="$MEMORIA" '
  /^vmType:/  { print "vmType: " tipo; next }
  /^cpus:/    { print "cpus: " cpus; next }
  /^memory:/  { print "memory: " mem "GiB"; next }
  /^mounts:/  { saltar = 1; next }
  saltar && /^[A-Za-z#]/ { saltar = 0 }
  saltar      { next }
  /^  hostPort:/ { print; print "  hostIP: \"0.0.0.0\""; next }
  { print }' "$RAIZ/vm/sigevet-ol9.yaml" > "$DEF"
echo "mounts: []" >> "$DEF"
if limactl list -q | grep -qx "$NOMBRE"; then
  limactl start "$NOMBRE" --tty=false || true
else
  limactl start --name="$NOMBRE" --tty=false "$DEF"
fi

paso "6/7 Copiando el proyecto a la VM e instalando MySQL, la base y la aplicación"
COPYFILE_DISABLE=1 tar -C "$RAIZ" --exclude=./vm/cache --exclude=./.git --exclude=./app/target --exclude=./entregables -czf - . \
  | limactl shell "$NOMBRE" -- bash -c 'rm -rf ~/proyecto && mkdir -p ~/proyecto && tar xzf - -C ~/proyecto 2>/dev/null'
limactl shell --workdir / "$NOMBRE" -- bash -c 'cd ~/proyecto && sudo bash vm/scripts/instalar_mysql.sh'
limactl shell --workdir / "$NOMBRE" -- bash -c 'cd ~/proyecto && bash vm/scripts/cargar_bd.sh --reiniciar' | tail -70
limactl shell --workdir / "$NOMBRE" -- bash -c 'cd ~/proyecto && sudo bash vm/scripts/instalar_app.sh'

paso "7/7 Comprobación"
for i in $(seq 1 30); do
  codigo=$(curl -s -o /dev/null -w '%{http_code}' http://127.0.0.1:5000/facturas/1 || true)
  [ "$codigo" = 200 ] && break
  sleep 3
done
echo "App web: HTTP $codigo"
IP=$( (hostname -I 2>/dev/null || ipconfig getifaddr en0 2>/dev/null) | awk '{print $1}')
echo
echo "Listo."
echo "  App web en este equipo:     http://127.0.0.1:5000"
echo "  Desde otro equipo de la red: http://$IP:5000"
echo "  MySQL Workbench: host $IP (o 127.0.0.1 en este equipo), puerto 3306, usuario vetdb, clave en vm/claves.env"
echo "  Encender la VM después de reiniciar el equipo: limactl start $NOMBRE"
