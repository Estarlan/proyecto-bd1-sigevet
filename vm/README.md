# Máquina virtual de SIGEVET

La base de datos corre en una VM con **Oracle Linux 9** y **Oracle Database Free**. Ahí mismo instalamos la
aplicación web como servicio, así que el computador anfitrión solo necesita el navegador.

Hay dos formas de montar la VM:

- **A. VirtualBox**: es la VM definitiva, la que se usa en cualquier PC con Windows, Linux o Mac Intel.
- **B. Lima**: la usamos para hacer pruebas en un MacBook Intel mientras definíamos el equipo final.

Los scripts de `vm/scripts/` sirven para las dos formas. Ubican la carpeta del proyecto por sí solos, así que no
importa en qué ruta quede dentro de la VM.

## Requisitos del equipo anfitrión

| Recurso | Mínimo | Recomendado |
|---|---|---|
| RAM libre para la VM | 4 GB | 6 GB |
| Núcleos para la VM | 2 | 4 |
| Disco libre | 30 GB | 40 GB |
| Virtualización (VT-x / AMD-V) | activada en la BIOS | |

## A. VirtualBox (equipo definitivo)

1. Instalar VirtualBox 7 y descargar la ISO de **Oracle Linux 9** (x86_64, "Full ISO") de
   <https://yum.oracle.com/oracle-linux-isos.html>.
2. Crear la VM:
   - Tipo Linux, versión Oracle Linux (64-bit).
   - 6 GB de RAM, 4 CPU y un disco de 40 GB.
   - En el instalador elegir "Server" (sin interfaz gráfica), crear el usuario `sigevet` y marcarlo como administrador.
3. Red. Hay dos opciones:
   - **NAT con reenvío de puertos** (Configuración → Red → Avanzado → Reenvío de puertos). Así la VM se ve desde el
     anfitrión en `127.0.0.1`.

     | Nombre | Puerto anfitrión | Puerto invitado |
     |---|---|---|
     | ssh | 2222 | 22 |
     | oracle | 1521 | 1521 |
     | web | 5000 | 5000 |

   - **Adaptador puente**: la VM recibe una IP de la red local (se ve con `hostname -I`) y se entra con esa IP desde
     cualquier equipo de la misma red.
4. Pasar el proyecto a la VM. Se puede clonar de GitHub (`sudo dnf -y install git` y luego `git clone ...`) o copiar
   la carpeta por `scp -P 2222`.
5. Pasar el RPM de Oracle Database Free a `vm/cache/`. Se descarga de
   <https://www.oracle.com/database/free/get-started/> (Oracle Linux 9) y pesa cerca de 1,5 GB.
6. Crear el archivo de claves: `cp vm/claves.ejemplo vm/claves.env` y poner las claves reales.
7. Instalar todo, desde la carpeta del proyecto:

   ```bash
   sudo bash vm/scripts/instalar_oracle.sh      # unos 10 minutos
   bash vm/scripts/cargar_bd.sh                 # usuarios, tablas, datos y verificación (~10 s)
   sudo bash vm/scripts/instalar_app.sh         # app web como servicio en el puerto 5000
   ```

8. Abrir en el anfitrión <http://127.0.0.1:5000> si se usa NAT, o `http://<ip-de-la-vm>:5000` si se usa puente.

Oracle y la aplicación arrancan solos cada vez que se enciende la VM.

## B. Simulación en macOS con Lima

```bash
limactl start --name=sigevet --tty=false vm/sigevet-ol9.yaml   # crea la VM la primera vez
limactl shell sigevet                                          # entrar
cd /proyecto
sudo bash vm/scripts/instalar_oracle.sh
bash vm/scripts/cargar_bd.sh
sudo bash vm/scripts/instalar_app.sh
```

Lima monta el proyecto en `/proyecto` y reenvía los puertos 1521 y 5000, así que la app queda en
<http://127.0.0.1:5000>. Para apagar la VM se usa `limactl stop sigevet` y para volver a encenderla `limactl start sigevet`.

## Scripts

| Script | Qué hace |
|---|---|
| `instalar_oracle.sh` | Instala el paquete de preinstalación y el RPM, crea la base FREE/FREEPDB1, deja Oracle con arranque automático y abre el puerto 1521 |
| `cargar_bd.sh [--reiniciar]` | Crea los usuarios VETDB y VET_APP y ejecuta `database/instalar_esquema.sql`. Con `--reiniciar` borra todo y lo vuelve a cargar |
| `instalar_app.sh` | Instala Python, Flask y python-oracledb, crea el servicio `sigevet-web` y abre el puerto 5000 |
| `resumen_bd.sh` | Muestra la versión de Oracle, los objetos del esquema y los ingresos por sede |
| `evidencias.sh vm\|rdbms` | Saca las evidencias que usamos en los documentos de implementación |

## Problemas que nos encontramos

- **La app no abre desde el anfitrión.** Revisar el reenvío del puerto 5000 o `sudo firewall-cmd --list-ports`.
- **`ORA-12541` (no hay listener).** Ejecutar `sudo systemctl start oracle-free-26ai` (en la versión 23ai el
  servicio se llama `oracle-free-23ai`).
- **Poca memoria.** Oracle Free usa hasta 2 GB. Con menos de 4 GB en la VM la instalación falla.
