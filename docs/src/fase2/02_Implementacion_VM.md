---
title: "Implementación de la Máquina Virtual"
subtitle: "Documento de Implementación de la Máquina Virtual"
fase: "Fase 2"
version: "1.0"
---
<!-- Created: 18:15 19-Oct-2026 -->
<!-- Updated: 18:42 19-Oct-2026 -->

# Introducción

Este documento describe cómo se creó y configuró la máquina virtual (VM) que aloja la base de datos y la aplicación
web de SIGEVET. El sistema operativo es el que se eligió en el *Documento de análisis de selección de sistema
operativo*: **Oracle Linux 9 (x86_64)**.

El proyecto usa dos ambientes con el mismo sistema operativo y los mismos scripts:

- **Ambiente de simulación**: un MacBook Intel con Lima. Ahí se hicieron todas las pruebas que aparecen en este
  documento, y las salidas que se muestran son reales.
- **Equipo servidor**: un computador Lenovo con Linux, donde la VM queda funcionando para la demostración. La VM
  corre con KVM (el hipervisor del kernel de Linux) y se crea con un solo script, `vm/migrar_linux.sh`. Los scripts
  no dependen de la ruta ni del hipervisor, así que el procedimiento es el mismo.

# Ambientes

| Elemento | Simulación (MacBook) | Equipo servidor |
|---|---|---|
| Equipo anfitrión | MacBook Pro Intel Core i7-9750H, 16 GB RAM, macOS Sonoma 14.8 | Lenovo con Linux (datos en la tabla de abajo) |
| Hipervisor | Apple Virtualization.framework (`vmType: vz`) con Lima 2.2.1 | KVM/QEMU (`vmType: qemu`) con Lima 2.2.1 |
| Imagen del sistema | Plantilla KVM oficial `OL9U8_x86_64-kvm-b293.qcow2` | La misma plantilla KVM oficial de Oracle |
| Sistema operativo invitado | Oracle Linux Server 9.8, kernel UEK 6.12 | Oracle Linux Server 9.8 |
| Recursos de la VM | 4 vCPU, 6 GiB de RAM, disco de 40 GiB | La mitad de los núcleos (2 a 4) y 4 GiB de RAM (6 GiB si el equipo tiene 12 GB o más), disco de 40 GiB |
| Red | NAT con reenvío de 3306 (MySQL) y 5000 (web) | NAT con los puertos 3306 y 5000 abiertos a la red local |
| Proyecto dentro de la VM | Carpeta montada en `/proyecto` (virtiofs, solo lectura) | Copiado a `~/proyecto` por el script |

Table: Ambientes de virtualización

| Dato del equipo servidor | Valor |
|---|---|
| Procesador | Pendiente |
| Memoria RAM | Pendiente |
| Sistema operativo anfitrión | Pendiente |
| Dirección para entrar a la app | Pendiente |

Table: Equipo servidor (se completa con lo que imprime `vm/migrar_linux.sh` en el Lenovo)

**Por qué dos ambientes.** La VM se construyó y se probó primero en el MacBook de uno de los integrantes, y la
versión final queda en el Lenovo con Linux, que es el que se lleva a la sustentación. En macOS con procesador Intel, VirtualBox pide instalar extensiones
del kernel, y Homebrew ya no publica QEMU precompilado. Por eso la simulación se hizo con Lima, que usa el hipervisor
de macOS y guarda la definición de la VM en un archivo de texto (`vm/sigevet-ol9.yaml`).

# Simulación en macOS con Lima

## Definición de la máquina virtual

La VM se declara en el archivo `vm/sigevet-ol9.yaml`, que forma parte del repositorio:

```yaml
minimumLimaVersion: 2.0.0
base:
- template:_images/oraclelinux-9   # Imagen oficial Oracle Linux 9 (KVM template de yum.oracle.com)
vmType: vz                         # Hipervisor nativo de macOS (Virtualization.framework)
cpus: 4
memory: 6GiB                       # MySQL y Tomcat usan poco; queda espacio de sobra para el SO
disk: 40GiB
mounts:
- location: "~/Developer/proyecto-bd1-sigevet"
  mountPoint: /proyecto
  writable: false
portForwards:                      # MySQL (Workbench) y aplicación web hacia el anfitrión
- guestPort: 3306
  hostPort: 3306
- guestPort: 5000
  hostPort: 5000
containerd:                        # el proyecto no usa contenedores
  system: false
  user: false
```

La plantilla `oraclelinux-9` de Lima descarga la imagen oficial de Oracle y verifica su integridad con la suma
SHA-256 `b12103391327abee8090686759c0d62dac9a7af2bf0f45fdf6b0d085a0fbb52b` antes de usarla. La configuración inicial
(usuario, llaves SSH, red) se hace con *cloud-init*.

## Procedimiento de creación

1. **Instalar Lima** (sin permisos de administrador): se descargó `lima-2.2.1-Darwin-x86_64.tar.gz` de la página
   oficial de releases en GitHub y se descomprimió en `~/.local/lima`.
2. **Crear y arrancar la VM:**

   ```bash
   limactl start --name=sigevet --tty=false vm/sigevet-ol9.yaml
   ```

   Lima descarga la imagen, la convierte a disco de arranque, crea la VM y espera a que el sistema esté listo.
3. **Habilitar la carpeta compartida.** La plantilla KVM de Oracle no incluye el módulo `virtiofs` en el kernel
   básico; se instaló el paquete `kernel-uek-modules-extra` correspondiente al kernel en ejecución, se cargó el módulo
   y se montó la carpeta con la etiqueta que expone el hipervisor. Para que el montaje sea permanente se agregó a
   `/etc/fstab` y a `/etc/modules-load.d`:

   ```bash
   sudo dnf install -y kernel-uek-modules-extra-$(uname -r)
   sudo modprobe virtiofs
   echo virtiofs | sudo tee /etc/modules-load.d/virtiofs.conf
   echo "<etiqueta> /proyecto virtiofs ro,nofail 0 0" | sudo tee -a /etc/fstab
   sudo mount /proyecto
   ```

4. **Entrar a la VM:** `limactl shell sigevet` abre una sesión SSH con el usuario del anfitrión.
5. **Operación diaria:** `limactl stop sigevet` apaga la VM y `limactl start sigevet` la vuelve a encender. La base de
   datos y la aplicación web arrancan solas porque quedan como servicios del sistema.

# Evidencias

<!-- INCLUDE: _generado/evidencia_vm.md -->

# Problemas encontrados y solución

| Problema | Causa | Solución |
|---|---|---|
| Homebrew intentaba compilar QEMU y sus dependencias desde el código fuente (horas) | Homebrew ya no publica binarios para macOS Intel | Se usó Lima con el hipervisor nativo `vz`, que no necesita QEMU |
| El primer arranque quedó esperando la instalación de `nerdctl` | La plantilla instala contenedores por defecto | Se desactivó `containerd` en la definición de la VM |
| La carpeta del proyecto no aparecía en `/proyecto` | El kernel de la plantilla no trae el módulo `virtiofs` | Se instaló `kernel-uek-modules-extra` y se montó con la etiqueta real del dispositivo |

Table: Problemas y soluciones durante la implementación de la VM

# Instalación en el equipo servidor (Lenovo con Linux)

En el servidor no se copia el disco de la VM del MacBook: ese disco usa el hipervisor de Apple y además pesaba 13 GB
porque todavía tenía Oracle. Como toda la base se construye con scripts, la VM se crea de nuevo y queda idéntica. Todo
lo hace un solo script, `vm/migrar_linux.sh`, que se ejecuta como usuario normal (con sudo) desde la carpeta del
proyecto:

```bash
bash vm/migrar_linux.sh
```

| Paso | Qué hace |
|---|---|
| 1 | Revisa el equipo: que exista `/dev/kvm` (virtualización activada en la BIOS) y cuánta RAM y cuántos núcleos tiene |
| 2 | Instala QEMU y el firmware UEFI (OVMF) con el gestor de paquetes de la distribución (apt, dnf, pacman o zypper) |
| 3 | Descarga Lima 2.2.1 de su página oficial y verifica la suma SHA-256 |
| 4 | Crea `vm/claves.env` con claves aleatorias si no existe |
| 5 | Crea la VM `sigevet` desde la misma definición `vm/sigevet-ol9.yaml`, con `vmType: qemu`, los recursos según el equipo y los puertos 3306 y 5000 abiertos a la red local |
| 6 | Copia el proyecto a la VM y ejecuta `instalar_mysql.sh`, `cargar_bd.sh` e `instalar_app.sh` |
| 7 | Comprueba que la aplicación responda y muestra las direcciones para el navegador y para MySQL Workbench |

Table: Pasos de vm/migrar_linux.sh

El script se probó completo creando una VM nueva desde cero en el MacBook (allí usa el hipervisor `vz` en lugar de
QEMU). La VM nueva quedó con MySQL 8.4, la base cargada, las 10 pruebas de reglas en OK y la aplicación web
respondiendo, tanto en `127.0.0.1` como desde la dirección IP del equipo en la red local.

Después de reiniciar el Lenovo, la VM se enciende con `limactl start sigevet`; MySQL y la aplicación arrancan solos
dentro de ella.

## Alternativa: VirtualBox

En un anfitrión con Windows se puede usar VirtualBox 7: crear una VM *Linux / Oracle (64-bit)* con 4 GB de RAM y 40 GB
de disco, instalar Oracle Linux 9 desde la ISO oficial con la opción *Server*, reenviar los puertos 3306 y 5000, copiar
el proyecto a la VM y ejecutar los mismos tres scripts (`instalar_mysql.sh`, `cargar_bd.sh` e `instalar_app.sh`). El
paso a paso está en `vm/README.md`.
