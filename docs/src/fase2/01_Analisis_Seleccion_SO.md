---
title: "Selección del sistema operativo para SIGEVET"
subtitle: "Documento de Análisis de selección de Sistema Operativo"
fase: "Fase 2"
version: "1.0"
---
<!-- Created: 18:15 19-Oct-2026 -->
<!-- Updated: 18:32 19-Oct-2026 (recursos reales de la VM) -->
<!-- Updated: 12:10 21-Oct-2026 -->

# Introducción y objetivo

Se selecciona **Oracle Linux 9 (x86_64)** como sistema operativo de la máquina virtual de SIGEVET. Ofrece MySQL 8.4 LTS en AppStream mediante el módulo `mysql:8.4`, no exige costo de licencia y coincide con la sugerencia de Bases de Datos 1 de la Universidad El Bosque. El objetivo del análisis es justificar la decisión mediante requisitos verificables y una comparación ponderada de seis alternativas Linux y Unix. [1]

SIGEVET es el Sistema Integral de Gestión Veterinaria de la organización ficticia Huella Sana S.A.S., una red de cinco clínicas en Usaquén, Chapinero, Chía, Laureles y San Fernando. Su modelo comprende 22 tablas para sedes, personal, propietarios, mascotas, atención clínica, inventarios y facturación. La aplicación web, desarrollada en Java con Servlets y JSP sobre Tomcat, será exclusivamente de consulta. El alcance es un laboratorio académico virtualizado con datos sintéticos.

# Requisitos y restricciones

## Condiciones académicas y funcionales

La asignatura admite únicamente Linux o Unix para el sistema operativo invitado y sugiere expresamente Oracle Linux. Windows y macOS quedan fuera de la selección del invitado; su uso como anfitriones es independiente de esta restricción. Se debe alojar un RDBMS relacional, ejecutar los scripts de las 22 tablas y demostrar integridad referencial, consultas y administración de usuarios. SQL Server está excluido de la selección del RDBMS.

Se conserva Oracle Linux 9 x86_64 y se adopta **MySQL 8.4 LTS Community Edition**, con la base `sigevet`, el propietario `vetdb` y el usuario de consulta `vet_app`. La aplicación Java usa JDBC con MySQL Connector/J y la administración gráfica se realiza desde MySQL Workbench en el anfitrión. La primera instalación con Oracle Database Free quedó como respaldo. La selección detallada del motor corresponde al análisis de RDBMS de esta fase.

## Hardware y presupuesto de recursos

| Elemento | Condición o asignación propuesta |
|---|---|
| Anfitrión de simulación | MacBook Pro 2019, Intel Core i7-9750H, 6 núcleos, 16 GB de RAM |
| Sistema del anfitrión | macOS Sonoma 14.8 |
| Arquitectura del invitado | x86_64, correspondiente al procesador Intel y a los paquetes del repositorio |
| Máquina virtual | 4 vCPU, 6 GiB de RAM y disco virtual de 40 GiB, con instalación mínima sin escritorio |
| Reserva del anfitrión | La asignación deja aproximadamente 10 GB de RAM fuera de la VM, antes de considerar la sobrecarga de virtualización |
| Red | Reenvío local para SSH y MySQL en el puerto 3306; acceso desde Workbench en el anfitrión |
Table: Condiciones del anfitrión y presupuesto inicial de la máquina virtual

Los recursos de la VM se comprobaron en la implementación (ver *Documento de implementación de la VM*). Workbench se ejecuta en el anfitrión. La base SIGEVET ocupa **1,08 MB** según la evidencia de implementación; este tamaño no equivale al consumo de memoria del servidor. Los 6 GiB de RAM y 40 GiB de disco también cubren el sistema operativo, la aplicación Java, registros, archivos temporales y exportaciones.

## Virtualización: equipo servidor y ambiente de simulación

La VM definitiva se ejecutará con **KVM/QEMU** (por medio de Lima) en el computador del equipo que funcionará como servidor durante la demostración, un Lenovo con Linux sobre x86_64; en un anfitrión con Windows la alternativa es **Oracle VirtualBox 7**. La simulación en macOS usa **Lima 2.2.1 y Apple Virtualization.framework**, con tipo de VM `vz` y arquitectura `x86_64`. Este backend exige coincidencia de arquitectura entre anfitrión e invitado; el MacBook Intel cumple esa condición. [2]

Lima permite conservar una configuración declarativa para la simulación disponible. En el equipo servidor con Linux se usa el hipervisor del propio kernel (KVM), sin instalar software de terceros. La elección del hipervisor depende del anfitrión y no cambia la selección de Linux como sistema invitado. [2, 3]

En el **equipo servidor** se creará una VM Linux de 64 bits con un medio oficial de Oracle Linux 9, 4 vCPU, 6 GiB de RAM y 40 GiB de disco. Se podrá usar NAT con reenvío de puertos o red puente para acceder desde Workbench. Se debe habilitar la virtualización del procesador y verificar la compatibilidad con el hipervisor activo en Windows. En ambos ambientes se conservan la versión de MySQL, la base `sigevet`, los usuarios y los scripts; la guía corresponde al documento de implementación de la VM. [3]

# Alternativas evaluadas

## Oracle Linux 9

Distribución empresarial compatible a nivel de aplicaciones con RHEL, disponible sin costo de descarga y uso, con licencias por componente y soporte comercial opcional. Ofrece Unbreakable Enterprise Kernel (UEK) y Red Hat Compatible Kernel (RHCK), repositorios públicos y un ciclo empresarial de mantenimiento. La descarga gratuita no incluye soporte contratado. [4, 5]

**Compatibilidad y ventajas:** MySQL 8.4 se instala desde AppStream con `dnf module install mysql:8.4`, sin descargar RPM por separado. La evidencia del proyecto registra `mysql-server` de la rama 8.4 y `mysql-selinux`; este último aporta la política SELinux para el servicio. `systemd` y `firewalld` permiten gestionar su arranque y el puerto 3306. **Desventajas:** el equipo debe aprender administración RPM y revisar por separado el ciclo del sistema y el del módulo MySQL; no son necesariamente iguales. [1]

## Rocky Linux 9

Distribución comunitaria empresarial gratuita y de código abierto, con licencias por componente y orientación a compatibilidad con RHEL. La rama mayor contempla diez años de soporte, entre soporte activo y mantenimiento de seguridad; debe utilizarse una revisión menor admitida. [6]

**Compatibilidad y ventajas:** comparte herramientas RPM, SELinux y administración similares a Oracle Linux; es una alternativa válida para MySQL y Java. **Desventajas:** cambiar a ella exigiría repetir la validación de los scripts y la VM, sin una mejora necesaria para esta carga. Su correspondencia con la sugerencia de la asignatura es menor. No se penaliza por una supuesta incompatibilidad con MySQL. [6]

## AlmaLinux 9

Distribución empresarial comunitaria gratuita y de código abierto, con licencias por componente y orientación a compatibilidad con RHEL. La rama 9 tiene un ciclo de diez años; las revisiones menores deben actualizarse conforme a la política del proyecto. [7]

**Compatibilidad y ventajas:** proporciona un entorno RPM con SELinux y herramientas conocidas de administración, apropiado para MySQL y Java. **Desventajas:** al igual que Rocky, requeriría repetir la validación realizada sobre Oracle Linux y ofrece menor correspondencia con la recomendación del curso. No existe una incompatibilidad funcional que la descarte. [7]

## Ubuntu Server 24.04 LTS

Distribución Linux gratuita con licencias por componente y servicios comerciales opcionales de Canonical. La versión LTS ofrece cinco años de mantenimiento estándar de seguridad para los paquetes de `Main`; las ampliaciones y la cobertura adicional dependen de Ubuntu Pro y sus condiciones. No se supone cobertura idéntica para todos los repositorios. [8]

**Compatibilidad y ventajas:** amplia documentación, administración con `apt`, integración con Java y mecanismos como AppArmor. MySQL dispone de un repositorio APT oficial para Ubuntu. **Desventajas:** habría que adaptar los comandos RPM del proyecto y comprobar el origen y la versión 8.4 de los paquetes. AppArmor es un control válido; su diferencia frente a SELinux no se considera una falla de seguridad. [8, 11]

## Debian 12

Distribución comunitaria libre, gratuita y con licencias por componente. La rama tiene tres años de soporte completo y dos adicionales de LTS, con alcance de arquitecturas y paquetes definido por el proyecto. Debian 12 se encuentra en la etapa LTS; mantenerla exige comprobar esa cobertura y planear la actualización a una rama posterior. [9]

**Compatibilidad y ventajas:** instalación mínima de bajo consumo, estabilidad y documentación consolidada. MySQL ofrece un repositorio APT oficial para Debian y Java puede desplegarse en el sistema. **Desventajas:** se deben adaptar los scripts basados en `dnf` y seleccionar explícitamente MySQL 8.4; la rama 12 ofrece un horizonte de mantenimiento menor que las ramas empresariales evaluadas. [9, 11]

## FreeBSD 14

Sistema de la familia BSD Unix, gratuito, con licencia predominantemente BSD permisiva y componentes sujetos a sus propias licencias. Es necesario seguir las revisiones menores admitidas y la política de mantenimiento de la rama 14. Se evalúa como opción Unix permitida por la asignatura. [10]

**Compatibilidad y ventajas:** sistema base coherente, documentación mediante el Handbook, administración con `pkg` y aislamiento mediante jails; puede alojar MySQL mediante sus paquetes o ports. **Desventajas:** requiere adaptar los scripts que usan `dnf`, `systemd`, SELinux y `firewalld`, y validar una VM distinta de la simulación Linux prevista. Su menor puntaje responde a este trabajo de adaptación, no a una imposibilidad de ejecutar MySQL. [10]

# Criterios y método de evaluación

Se utiliza una escala de **1 a 5**: 1 indica ajuste muy bajo; 2, ajuste bajo con adaptaciones importantes; 3, ajuste aceptable con condiciones; 4, ajuste alto; y 5, ajuste muy alto para el proyecto. Las calificaciones expresan el juicio del equipo y no son resultados de pruebas comparativas de rendimiento. Los pesos suman 100 %.

La adecuación a MySQL y Java recibe el mayor peso: todos los Linux evaluados satisfacen este requisito. Costo considera el uso gratuito sin confundirlo con soporte contratado; soporte valora el ciclo de la rama; consumo estima una instalación mínima, sin afirmar mediciones comparativas. Administración considera paquetes y servicios; documentación, manuales y comunidad; alineación, la sugerencia expresa de Oracle Linux y la continuidad del entorno ya validado. Seguridad reconoce controles equivalentes, sin exigir SELinux como condición excluyente.

En la matriz, OL9 significa Oracle Linux 9; Rocky, Rocky Linux 9; Alma, AlmaLinux 9; Ubuntu, Ubuntu Server 24.04 LTS; Debian, Debian 12; y FreeBSD, FreeBSD 14. Se aplica $T_j = \sum_i (p_i s_{ij}) / 100$, donde $p_i$ es el peso porcentual y $s_{ij}$ la calificación de la alternativa.

| Criterio | Peso (%) | OL9 | Rocky | Alma | Ubuntu | Debian | FreeBSD |
|---|---|---|---|---|---|---|---|
| Adecuación a MySQL y Java | 25 | 5 | 5 | 5 | 5 | 5 | 4 |
| Costo y licencia | 10 | 5 | 5 | 5 | 5 | 5 | 5 |
| Soporte y ciclo de vida | 15 | 5 | 5 | 5 | 5 | 3 | 3 |
| Consumo de recursos | 10 | 4 | 4 | 4 | 4 | 5 | 4 |
| Facilidad de administración | 10 | 5 | 5 | 5 | 5 | 5 | 3 |
| Documentación y comunidad | 10 | 5 | 4 | 4 | 5 | 5 | 4 |
| Alineación con la asignatura y continuidad | 15 | 5 | 3 | 3 | 3 | 3 | 2 |
| Seguridad y actualizaciones | 5 | 5 | 5 | 5 | 5 | 5 | 5 |
| Total ponderado | 100 | 4,90 | 4,50 | 4,50 | 4,60 | 4,40 | 3,60 |
Table: Matriz de decisión ponderada para la selección del sistema operativo

# Resultado y justificación

Alternativa seleccionada: **OL9** — Oracle Linux 9 (x86_64).

Oracle Linux obtiene **4,90 sobre 5**, frente a 4,60 de Ubuntu y 4,50 de Rocky Linux y AlmaLinux. El total de OL9 es $(25\times5 + 10\times5 + 15\times5 + 10\times4 + 10\times5 + 10\times5 + 15\times5 + 5\times5)/100 = 4{,}90$.

Las seis alternativas tienen opciones de uso sin costo de licencia. MySQL funciona en los Linux evaluados, por lo que la compatibilidad tampoco decide por sí sola. Oracle Linux gana por su correspondencia con la asignatura, la disponibilidad directa de `mysql:8.4` en AppStream y la continuidad de los scripts y la VM ya comprobados. Ubuntu y Debian siguen siendo opciones válidas; Rocky y AlmaLinux reciben puntajes iguales por su ajuste semejante a este caso.

La evidencia local registra Oracle Linux 9.8, MySQL 8.4, el paquete `mysql-selinux`, el servicio `mysqld` activo y la apertura del puerto 3306. Los documentos de implementación presentan las pruebas de carga, reglas y acceso de consulta. Esta comprobación del entorno elegido no sustituye una comparación de rendimiento entre los seis sistemas.

# Riesgos y mitigaciones

| Riesgo | Mitigación propuesta |
|---|---|
| Presión de memoria en el anfitrión | Asignar 6 GiB a la VM (de 16 GB del anfitrión), cerrar cargas no necesarias y apagar la VM con `limactl stop` cuando no se use |
| Cambio del módulo o de los paquetes MySQL | Comprobar `dnf module list mysql`, seleccionar `mysql:8.4` y registrar las versiones instaladas; revisar el ciclo del módulo aparte del SO |
| Diferencias entre la simulación en macOS (vz) y el servidor con Linux (KVM) | Mantener arquitectura, recursos iniciales, versiones y scripts; comprobar `mysqld`, la aplicación y la conexión de Workbench en cada anfitrión |
| Interrupción del anfitrión o pérdida del disco virtual | Conservar DDL y DML en Git y exportaciones fuera de la VM; verificar restauración en lugar de depender solo de instantáneas |
| Acceso al puerto 3306 desde una red no prevista | Limitar el reenvío o las reglas de red al entorno de demostración y proteger las claves fuera de Git |
| SELinux o firewall bloquean servicios | Conservar `mysql-selinux`, revisar registros y contextos, y permitir solo los puertos necesarios sin desactivar globalmente los controles |
Table: Riesgos de la selección del SO y medidas de mitigación

# Conclusión

Se adopta Oracle Linux 9 x86_64 para el ambiente académico virtualizado de SIGEVET con MySQL 8.4 LTS Community Edition. Cumple la restricción Linux/Unix, coincide con la sugerencia de la asignatura y permite instalar el motor desde AppStream con su política SELinux. La VM definitiva se ejecutará en VirtualBox y la simulación en macOS se mantiene con Lima.

# Referencias

1. Oracle. [Módulo MySQL 8.4 de Oracle Linux 9](https://docs.oracle.com/en/operating-systems/oracle-linux/9/relnotes9.6/ol9-features-DynamicProgramming.html) y [ciclo de los módulos AppStream](https://docs.oracle.com/en/operating-systems/oracle-linux/product-lifecycle/ol9_application_streams.html). Evidencia local: `docs/src/_generado/evidencia_instalacion_mysql.md`.
2. Lima. [Backend VZ y condiciones de Virtualization.framework](https://lima-vm.io/docs/config/vmtype/vz/).
3. Oracle. [Anfitriones y configuración de VirtualBox](https://docs.oracle.com/en/virtualization/virtualbox/7.0/user/Introduction.html).
4. Oracle. [Compatibilidad de Oracle Linux con RHEL](https://www.oracle.com/linux/technologies/certifying-hardware-and-application.html) y [repositorios públicos yum](https://yum.oracle.com/).
5. Oracle. [Soporte de Oracle Linux](https://www.oracle.com/linux/support/) y [kernels de Oracle Linux 9](https://docs.oracle.com/en/operating-systems/oracle-linux/9/boot/boot-about_linux_kernels.html).
6. Rocky Linux. [Descripción del proyecto](https://rockylinux.org/) y [política de versiones y soporte](https://docs.rockylinux.org/latest/releases/).
7. AlmaLinux. [Descripción y licenciamiento](https://almalinux.org/) y [versiones y mantenimiento](https://wiki.almalinux.org/release-notes/).
8. Canonical. [Ciclo de versiones de Ubuntu](https://ubuntu.com/about/release-cycle) y [alcance del mantenimiento ampliado](https://ubuntu.com/security/esm).
9. Debian. [Rama Debian 12 Bookworm y soporte](https://www.debian.org/releases/bookworm/) y [contrato social y software libre](https://www.debian.org/social_contract.html).
10. FreeBSD. [Descripción del sistema](https://www.freebsd.org/about/), [modelo de soporte](https://www.freebsd.org/security/) y [ports de bases de datos](https://cgit.freebsd.org/ports/tree/databases).
11. MySQL. [Repositorio APT para Debian y Ubuntu](https://dev.mysql.com/doc/mysql-apt-repo-quick-guide/en/).
