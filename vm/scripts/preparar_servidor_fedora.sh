#!/bin/bash
# Revisa el portátil Fedora que hace de servidor y escribe ~/INFORME_SERVIDOR.md (sin claves).
# Uso: bash vm/scripts/preparar_servidor_fedora.sh              solo revisa, no cambia nada
#      bash vm/scripts/preparar_servidor_fedora.sh --aplicar    además aplica los arreglos (pide sudo y confirma cada uno)
set -uo pipefail
RAIZ="$(cd "$(dirname "$0")/../.." && pwd)"
INFORME="$HOME/INFORME_SERVIDOR.md"
APLICAR=0
[ "${1:-}" = "--aplicar" ] && APLICAR=1
LIMACTL="$(command -v limactl || echo "$HOME/.local/lima/bin/limactl")"

pregunta() { read -r -p "$1 [s/N] " r; [ "$r" = "s" ] || [ "$r" = "S" ]; }
en_vm() { "$LIMACTL" shell sigevet -- "$@"; }
linea() { echo "$1" >> "$INFORME"; }

: > "$INFORME"
linea "# Informe del servidor SIGEVET"
linea ""
linea "## Equipo (Fedora)"
linea ""
linea "| Dato | Valor |"
linea "|---|---|"
linea "| Sistema operativo | $(cat /etc/fedora-release) |"
linea "| Kernel | $(uname -r) |"
linea "| Procesador | $(lscpu | awk -F: '/Model name/ {gsub(/^ +/, "", $2); print $2}') |"
linea "| Núcleos / hilos | $(lscpu | awk -F: '/^Core\(s\) per socket/ {gsub(/ /, "", $2); c=$2} /^CPU\(s\):/ {gsub(/ /, "", $2); h=$2} END {print c " / " h}') |"
linea "| Memoria | $(free -h | awk '/Mem:/ {print $2 " en total, " $7 " disponibles"}') |"
linea "| Disco | $(df -h / | awk 'NR==2 {print $2 " en total, " $4 " libres"}') |"
linea "| Red | $(ip -4 -o addr show scope global | awk '{print $2 " " $4}' | head -1) |"
linea "| Hora sincronizada | $(timedatectl show -p NTPSynchronized --value) |"

linea ""
linea "## Máquina virtual (Lima)"
linea ""
if [ ! -x "$LIMACTL" ]; then
  linea "- No se encontró limactl."
else
  estado="$("$LIMACTL" list --format '{{.Status}}' sigevet 2>/dev/null || echo "no existe")"
  linea "- Estado de la VM sigevet: $estado"
  if [ "$estado" != "Running" ] && pregunta "La VM sigevet no está encendida. ¿La enciendo ahora?"; then
    "$LIMACTL" start sigevet && estado="Running"
    linea "- Se encendió la VM durante esta revisión."
  fi
  if [ "$estado" = "Running" ]; then
    linea "- Sistema de la VM: $(en_vm cat /etc/oracle-release 2>/dev/null)"
    linea "- Servicio mysqld: $(en_vm systemctl is-active mysqld 2>/dev/null) (arranca solo: $(en_vm systemctl is-enabled mysqld 2>/dev/null))"
    linea "- Servicio de la app: $(en_vm systemctl is-active sigevet-web 2>/dev/null)"
    if [ -f "$RAIZ/vm/claves.env" ]; then
      linea ""
      linea "## Base de datos"
      linea ""
      linea '```'
      en_vm bash "$RAIZ/vm/scripts/resumen_bd.sh" >> "$INFORME" 2>&1
      linea '```'
    else
      linea "- Falta vm/claves.env: no se pudo revisar la base."
    fi
  fi
fi

linea ""
linea "## Preparación como servidor"
linea ""
tapa="$(loginctl show-logind -p HandleLidSwitch --value 2>/dev/null || grep -h '^HandleLidSwitch=' /etc/systemd/logind.conf.d/*.conf 2>/dev/null | tail -1)"
linea "- Al cerrar la tapa: ${tapa:-suspend (valor por defecto)}"
linea "- Arranque de la VM al encender el equipo: $(systemctl --user is-enabled sigevet-vm.service 2>/dev/null || echo "sin configurar")"
linea "- Servicio SSH (para conectarse de otro equipo): $(systemctl is-active sshd 2>/dev/null)"
linea "- Puerto 3306: la VM lo publica solo en 127.0.0.1, así que no queda abierto a la red y no necesita regla de firewall."

if [ "$APLICAR" = "1" ]; then
  echo "== Arreglos (cada uno pide confirmación)"
  if pregunta "1) Que el portátil NO se suspenda al cerrar la tapa"; then
    sudo mkdir -p /etc/systemd/logind.conf.d
    printf '[Login]\nHandleLidSwitch=ignore\nHandleLidSwitchExternalPower=ignore\n' | sudo tee /etc/systemd/logind.conf.d/servidor.conf >/dev/null
    sudo systemctl restart systemd-logind
  fi
  if pregunta "2) Que la VM sigevet arranque sola al encender el equipo"; then
    mkdir -p "$HOME/.config/systemd/user"
    printf '[Unit]\nDescription=SIGEVET Lima VM\nAfter=network.target\n\n[Service]\nType=oneshot\nRemainAfterExit=yes\nExecStart=%s start sigevet\nExecStop=%s stop sigevet\n\n[Install]\nWantedBy=default.target\n' "$LIMACTL" "$LIMACTL" > "$HOME/.config/systemd/user/sigevet-vm.service"
    systemctl --user daemon-reload
    systemctl --user enable sigevet-vm.service
    sudo loginctl enable-linger "$USER"
  fi
  if pregunta "3) (Opcional) Activar SSH para que Sergio o Juan Miguel se conecten desde su equipo"; then
    sudo systemctl enable --now sshd
  fi
  echo "Listo. Vuelve a correr el script sin --aplicar para actualizar el informe."
fi

echo "Informe escrito en $INFORME (no contiene claves). Mándaselo a Sergio."
