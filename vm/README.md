# Máquina virtual de SIGEVET

La base de datos corre en una VM con **Oracle Linux 9** y **MySQL 8.4 LTS**. Ahí mismo instalamos la aplicación web
(Tomcat) como servicio. En el equipo anfitrión solo hacen falta el navegador y **MySQL Workbench** para administrar
la base.

Hay dos formas de montar la VM:

- **A. VirtualBox**: es la VM definitiva, la que se usa en cualquier PC con Windows, Linux o Mac Intel.
- **B. Lima**: la usamos para hacer pruebas en un MacBook Intel mientras definíamos el equipo final.

Los scripts de `vm/scripts/` sirven para las dos formas. Ubican la carpeta del proyecto por sí solos, así que no
importa en qué ruta quede dentro de la VM.

## Requisitos del equipo anfitrión

| Recurso | Mínimo | Recomendado |
|---|---|---|
| RAM libre para la VM | 2 GB | 4 GB |
| Núcleos para la VM | 2 | 4 |
| Disco libre | 20 GB | 40 GB |
| Virtualización (VT-x / AMD-V) | activada en la BIOS | |

## A. VirtualBox (equipo definitivo)

1. Instalar VirtualBox 7 y descargar la ISO de **Oracle Linux 9** (x86_64, "Full ISO") de
   <https://yum.oracle.com/oracle-linux-isos.html>.
2. Crear la VM:
   - Tipo Linux, versión Oracle Linux (64-bit).
   - 4 GB de RAM, 2 a 4 CPU y un disco de 40 GB.
   - En el instalador elegir "Server" (sin interfaz gráfica), crear el usuario `sigevet` y marcarlo como administrador.
3. Red. Hay dos opciones:
   - **NAT con reenvío de puertos** (Configuración → Red → Avanzado → Reenvío de puertos). Así la VM se ve desde el
     anfitrión en `127.0.0.1`.

     | Nombre | Puerto anfitrión | Puerto invitado |
     |---|---|---|
     | ssh | 2222 | 22 |
     | mysql | 3306 | 3306 |
     | web | 5000 | 5000 |

   - **Adaptador puente**: la VM recibe una IP de la red local (se ve con `hostname -I`) y se entra con esa IP desde
     cualquier equipo de la misma red.
4. Pasar el proyecto a la VM. Se puede clonar de GitHub (`sudo dnf -y install git` y luego `git clone ...`) o copiar
   la carpeta por `scp -P 2222`.
5. Crear el archivo de claves: `cp vm/claves.ejemplo vm/claves.env` y poner las claves reales.
6. Instalar todo, desde la carpeta del proyecto:

   ```bash
   sudo bash vm/scripts/instalar_mysql.sh       # MySQL 8.4 desde los repositorios de Oracle Linux (~2 minutos)
   bash vm/scripts/cargar_bd.sh                 # base, usuarios, tablas, datos y verificación (~2 s)
   sudo bash vm/scripts/instalar_app.sh         # Java 21, Tomcat y la app web como servicio en el puerto 5000
   ```

7. Abrir en el anfitrión <http://127.0.0.1:5000> si se usa NAT, o `http://<ip-de-la-vm>:5000` si se usa puente.

MySQL y la aplicación arrancan solos cada vez que se enciende la VM.

## Conectarse con MySQL Workbench

En el equipo anfitrión, en MySQL Workbench: **Database → Manage Connections → New**.

| Campo | Valor |
|---|---|
| Connection Name | SIGEVET (VM) |
| Hostname | `127.0.0.1` con NAT, o la IP de la VM con adaptador puente |
| Port | 3306 |
| Username | `vetdb` (dueño de la base) o `vet_app` (solo lectura) |
| Default Schema | `sigevet` |

La clave es la de `vm/claves.env`. Desde ahí se ven las tablas, vistas, procedimientos y disparadores, y se pueden
ejecutar los scripts de `database/` y las 55 consultas de álgebra. Además, *Database → Reverse Engineer* genera el
diagrama EER directamente desde la base.

## B. Simulación en macOS con Lima

```bash
limactl start --name=sigevet --tty=false vm/sigevet-ol9.yaml   # crea la VM la primera vez
limactl shell sigevet                                          # entrar
cd /proyecto
sudo bash vm/scripts/instalar_mysql.sh
bash vm/scripts/cargar_bd.sh
sudo bash vm/scripts/instalar_app.sh
```

Lima monta el proyecto en `/proyecto` y reenvía los puertos 3306 y 5000, así que Workbench se conecta a
`127.0.0.1:3306` y la app queda en <http://127.0.0.1:5000>. Para apagar la VM se usa `limactl stop sigevet` y para
volver a encenderla `limactl start sigevet`.

## Scripts

| Script | Qué hace |
|---|---|
| `instalar_mysql.sh` | Instala MySQL 8.4 (módulo `mysql:8.4` de Oracle Linux), lo configura para aceptar conexiones de Workbench, le pone clave a root, lo deja con arranque automático y abre el puerto 3306 |
| `cargar_bd.sh [--reiniciar]` | Crea la base `sigevet` y los usuarios `vetdb` y `vet_app` y ejecuta `database/instalar_esquema.sql`. Con `--reiniciar` borra la base y la vuelve a cargar |
| `instalar_app.sh` | Instala Java 21 y Tomcat 10.1, compila la app (`ROOT.war`) con Maven, crea el servicio `sigevet-web` y abre el puerto 5000 |
| `resumen_bd.sh` | Muestra la versión de MySQL, los objetos de la base y los ingresos por sede |
| `evidencias.sh vm\|rdbms\|vet_app` | Saca las evidencias que usamos en los documentos de implementación |
| `prueba_vet_app.sql` | Prueba que `vet_app` puede leer pero no modificar |

## Problemas que nos encontramos

- **Workbench no conecta.** Revisar el reenvío del puerto 3306 en VirtualBox o `sudo firewall-cmd --list-ports` en la
  VM. MySQL escucha en todas las interfaces por `bind-address = 0.0.0.0` (`/etc/my.cnf.d/sigevet.cnf`).
- **`Access denied for user 'vetdb'@'...'`.** La clave no coincide con la de `vm/claves.env`.
- **La app no abre desde el anfitrión.** Revisar el reenvío del puerto 5000 y `systemctl status sigevet-web`.
- **SELinux bloqueaba Tomcat.** Como Tomcat se descomprime en `/tmp`, `instalar_app.sh` corrige su etiqueta con
  `restorecon` antes de crear el servicio.
