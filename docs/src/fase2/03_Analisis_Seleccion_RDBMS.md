---
title: "Selección del RDBMS para SIGEVET"
subtitle: "Documento de Análisis de selección de RDBMS"
fase: "Fase 2"
version: "1.0"
---
<!-- Created: 18:15 19-Oct-2026 -->
<!-- Updated: 18:32 19-Oct-2026 (recursos reales de la VM) -->
<!-- Updated: 18:36 19-Oct-2026 (tamaño medido del esquema) -->
<!-- Updated: 12:10 21-Oct-2026 -->

# Introducción y objetivo

Se selecciona **MySQL 8.4 LTS Community Edition** para la base de datos de SIGEVET, proyecto de Bases de Datos 1 de la Universidad El Bosque. La decisión considera las funciones del modelo, la instalación en Oracle Linux 9, el uso de MySQL Workbench desde el anfitrión y la conexión de la aplicación Java. Primero se probó Oracle Database Free; esa versión quedó como respaldo y MySQL es el motor de la implementación principal.

SIGEVET centraliza la información de Huella Sana S.A.S., organización ficticia con cinco clínicas. El modelo tiene 22 tablas para sedes, empleados, clientes, mascotas, citas, historias clínicas, medicamentos, inventario y facturación. La aplicación usa Servlets y JSP en Tomcat y consulta los datos con una cuenta de solo lectura. Los scripts del equipo crean el esquema y cargan los datos sintéticos.

# Requisitos y restricciones

## Alcance académico y relacional

El enunciado exige un RDBMS, prohíbe SQL Server y acepta «Data Modeler o Workbench» para el modelado. Se necesitan claves primarias y foráneas, unicidad, obligatoriedad, restricciones de dominio, transacciones y consultas de álgebra relacional. También se requieren vistas, funciones, procedimientos y disparadores para las reglas de negocio. La evaluación compara MySQL, PostgreSQL, Oracle Database Free y MariaDB; ninguna alternativa se presenta como plenamente conforme con todo el estándar SQL.

La base se llama `sigevet`. El usuario `vetdb` administra sus objetos y `vet_app` consulta mediante el rol `rol_sigevet_lectura`. MySQL Workbench Community permite modelar diagramas EER en notación Crow's Foot, administrar la base y ejecutar consultas desde el anfitrión. La aplicación Java se conecta mediante el controlador JDBC oficial **MySQL Connector/J**, con consultas parametrizadas y sin usar la cuenta dueña del esquema. [1, 2, 3]

## Infraestructura y operación

| Elemento | Condición del proyecto |
|---|---|
| Sistema invitado | Oracle Linux 9 x86_64, instalación mínima sin escritorio |
| VM definitiva | KVM/QEMU con Lima en el computador servidor con Linux; puertos 3306 y 5000 abiertos a la red local |
| Simulación | Lima sobre Virtualization.framework en macOS Intel |
| Recursos asignados | 4 vCPU, 6 GiB de RAM y 40 GiB de disco virtual |
| Instalación del motor | Módulo `mysql:8.4` de AppStream, con `mysql-server` y `mysql` |
| Administración gráfica | MySQL Workbench 8.0.44 en el anfitrión, puerto 3306 |
| Tamaño medido de la base | 1,08 MB de datos e índices según `information_schema.TABLES` |
Table: Condiciones de infraestructura para evaluar el RDBMS

AppStream ofrece el módulo `mysql:8.4` para Oracle Linux 9, por lo que la instalación no requiere descargar RPM aparte ni agregar un repositorio externo. El instalador y las evidencias del proyecto identifican la revisión instalada de la rama **8.4 LTS**; el documento de implementación presenta su número completo. [4]

Community Edition es gratuita y tiene licencia GPLv2. No impone límites de tamaño, CPU o RAM por edición como Oracle Free; la capacidad depende del hardware, el sistema de archivos y los límites técnicos del motor. La descarga comunitaria no incluye un contrato de soporte comercial. [1, 5]

# Alternativas evaluadas

## MySQL 8.4 LTS Community Edition

Con InnoDB ofrece transacciones e integridad referencial mediante claves foráneas. Aplica restricciones `CHECK` desde 8.0.16 e incorpora `EXCEPT` e `INTERSECT` desde 8.0.31, funciones disponibles en 8.4. También admite vistas, funciones, procedimientos y disparadores con `SIGNAL` para rechazar operaciones que incumplan las reglas. [5, 6, 7, 8]

**Ventajas:** licencia gratuita sin límites de edición equivalentes a Oracle Free; paquete oficial en AppStream; Workbench Community gratuito para modelado, administración y consultas; Connector/J oficial; familiaridad del equipo y una comunidad amplia. La base cargada ocupa 1,08 MB, lo que muestra una necesidad baja de almacenamiento para este conjunto académico. Ese tamaño no mide el consumo de RAM ni demuestra por sí solo mayor rendimiento que otros motores.

**Desventajas:** carece de `FULL OUTER JOIN`; cada disparador atiende un solo evento; y, con la configuración habitual de Linux, los nombres de tabla distinguen mayúsculas. El analizador de Workbench usado en el proyecto no reconoce valores por defecto con expresión como `DEFAULT (CURRENT_DATE)`, aunque el servidor los admite. Las adaptaciones se explican en los riesgos y están reflejadas en los scripts. [8, 9, 10, 11]

## PostgreSQL 17

Motor gratuito de código abierto con licencia PostgreSQL permisiva. Ofrece transacciones, integridad referencial, restricciones y una amplia cobertura de SQL, incluido `FULL OUTER JOIN`. Puede ejecutarse en Oracle Linux y conectarse desde Java mediante JDBC; es una alternativa sólida para el modelo. [12]

**Ventajas:** expresividad SQL, controles de integridad, documentación y comunidad amplias, sin límites de edición comparables a Oracle Free. **Desventajas para este equipo:** exige aprender y validar otro flujo de modelado y administración; Workbench está orientado a MySQL. No se penaliza su capacidad relacional ni se afirma que tenga peor rendimiento: la diferencia está en las herramientas y la experiencia disponibles.

## Oracle Database Free

Fue el motor de la primera prueba y su implementación quedó como respaldo. Ofrece transacciones, restricciones, vistas, rutinas, disparadores y herramientas gratuitas como SQL Developer Data Modeler, además de un controlador JDBC oficial.

**Ventajas:** funciones relacionales suficientes y una instalación que el equipo ya probó. **Desventajas:** licencia propietaria de uso gratuito, límites de **2 CPU, 2 GB de RAM y 12 GB de datos de usuario**, y una instalación que requirió preinstalador y RPM separado. El límite de memoria corresponde al motor, no a toda la VM. Estas restricciones no impiden alojar los datos académicos, pero reducen el margen y añaden pasos frente al módulo MySQL de AppStream. [13]

## MariaDB 11.4 LTS

Motor gratuito de código abierto bajo GPLv2, con InnoDB, transacciones, claves foráneas, restricciones y conectividad Java. Es viable para SIGEVET y tiene una comunidad propia. [14]

**Ventajas:** licencia libre, recursos configurables y funciones relacionales suficientes. **Desventajas para el proyecto:** MariaDB y MySQL han divergido; habría que validar dialecto, conector y herramientas. Workbench se diseña para MySQL y no se presupone compatibilidad completa con MariaDB. La familiaridad del equipo con MySQL no sustituye esa comprobación.

# Criterios y método de evaluación

Se califica de **1 a 5**: 1 indica ajuste muy bajo; 2, bajo; 3, aceptable con adaptaciones; 4, alto; y 5, muy alto para este proyecto. Los pesos suman **100 %**. Son valoraciones del equipo para el laboratorio, no resultados de un benchmark entre motores.

SQL e integridad suman 30 % porque son el centro de la asignatura. Modelado e instalación suman otro 30 % por la necesidad de trabajar con una herramienta admitida y reproducir el ambiente. Costo valora licencia y límites; recursos considera la operación de una VM pequeña; familiaridad reconoce la experiencia del equipo. Todos reciben 5 en JDBC porque cuentan con controladores adecuados para Java.

MySQL recibe 4 en SQL por la ausencia de `FULL OUTER JOIN`. PostgreSQL recibe 4 en herramientas por requerir un flujo distinto; Oracle recibe 5 por Data Modeler y MariaDB 3 por la validación adicional con Workbench. En instalación, MySQL recibe 5 por el módulo probado de AppStream; PostgreSQL y MariaDB 4 por sus opciones de paquetes Linux, y Oracle 3 por sus pasos adicionales. En recursos no se atribuye superioridad medida a MySQL frente a PostgreSQL o MariaDB: los tres reciben 4 y Oracle 3 por el mayor trabajo de configuración de su instalación de respaldo.

El puntaje ponderado se calcula como $T_j = \sum_i (p_i \times s_{ij}) / 100$, donde $p_i$ es el peso porcentual y $s_{ij}$ la nota. Los nombres de las columnas corresponden a las cuatro versiones descritas.

| Criterio | Peso (%) | MySQL | PostgreSQL | Oracle | MariaDB |
|---|---|---|---|---|---|
| Cumplimiento SQL y álgebra relacional | 15 | 4 | 5 | 5 | 4 |
| Integridad referencial y transacciones | 15 | 5 | 5 | 5 | 5 |
| Herramientas de modelado y administración | 15 | 5 | 4 | 5 | 3 |
| Costo, licencia y límites | 10 | 5 | 5 | 3 | 5 |
| Instalación y mantenimiento en Oracle Linux 9 | 15 | 5 | 4 | 3 | 4 |
| Recursos y operación en la VM | 10 | 4 | 4 | 3 | 4 |
| Conectividad desde Java (JDBC) | 5 | 5 | 5 | 5 | 5 |
| Familiaridad del equipo | 10 | 5 | 3 | 4 | 3 |
| Comunidad y documentación | 5 | 5 | 5 | 5 | 4 |
| Total ponderado | 100 | 4,75 | 4,40 | 4,20 | 4,05 |
Table: Matriz de decisión ponderada para la selección del RDBMS

# Resultado y justificación

Alternativa seleccionada: **MySQL** — MySQL 8.4 LTS Community Edition.

MySQL obtiene **4,75 sobre 5**. El cálculo es $(15\times4 + 15\times5 + 15\times5 + 10\times5 + 15\times5 + 10\times4 + 5\times5 + 10\times5 + 5\times5)/100 = 4{,}75$. Le siguen PostgreSQL con 4,40, Oracle con 4,20 y MariaDB con 4,05.

La ventaja resulta de la instalación directa en AppStream, Workbench como herramienta admitida y conocida, la licencia sin límites de edición equivalentes a Oracle Free y el ajuste a Java con Connector/J. La base de 1,08 MB y la carga completa en 1–2 s respaldan la viabilidad de la implementación académica; no constituyen una comparación de rendimiento entre motores. Las evidencias están en los registros de instalación, carga y verificación de `vm/logs/`.

La elección depende del contexto. Si se trasladan 10 puntos de modelado y 5 de familiaridad al criterio SQL, MySQL y PostgreSQL empatan en 4,60. PostgreSQL sería especialmente competitivo si se priorizara expresividad SQL. MySQL gana con los pesos adoptados por sus ventajas prácticas para este equipo, aceptando adaptaciones concretas del dialecto.

# Riesgos y mitigaciones

| Riesgo o limitación | Mitigación aplicada o prevista |
|---|---|
| No hay `FULL OUTER JOIN` nativo | El ejercicio 29 combina `LEFT JOIN` y `RIGHT JOIN` con `UNION` para representar la reunión externa completa del inventario |
| Un disparador cubre un solo evento | Las reglas compartidas se concentran en procedimientos llamados por los disparadores de `INSERT` y de `UPDATE`; `SIGNAL` comunica el incumplimiento |
| Nombres de tabla sensibles a mayúsculas en Linux | Respetar los nombres de los scripts, como `MASCOTA` y `CITA`, también desde Workbench y Java |
| Workbench no analiza `DEFAULT (CURRENT_DATE)` en el flujo usado | Las fechas de registro se definieron como `DATETIME DEFAULT CURRENT_TIMESTAMP`; se comprueba el DDL en el servidor |
| Acceso remoto y permisos excesivos | Reservar `vetdb` para administración; usar `vet_app` con lectura, guardar claves en `vm/claves.env` y limitar el acceso de red al entorno del proyecto |
| Cambios de paquetes o diferencias entre hipervisores | Mantener la rama `mysql:8.4`, registrar paquetes instalados y repetir la carga y las pruebas en la simulación y en el servidor |
| Pérdida de datos o dependencia del dialecto | Conservar scripts y exportaciones fuera de la VM, probar reconstrucción y documentar las diferencias con la versión Oracle de respaldo |
Table: Riesgos de la selección de MySQL y medidas de mitigación

La implementación registra 22 tablas, 6 vistas, 2 funciones, 3 procedimientos, 9 disparadores y 71 índices. Las 10 pruebas de reglas resultaron correctas; además, `vet_app` puede consultar y recibe el error 1142 al intentar `DELETE`, `UPDATE`, `INSERT` o `DROP`. Son resultados del ambiente probado, no una garantía de disponibilidad para una clínica en producción.

# Referencias

1. MySQL. [Community Edition: licencia GPL, herramientas y conectores](https://www.mysql.com/products/community/index.html).
2. MySQL. [Workbench Community y ediciones](https://dev.mysql.com/doc/workbench/en/wb-editions.html) y [relaciones EER en Crow's Foot](https://dev.mysql.com/doc/workbench/en/wb-relationship-tools.html).
3. MySQL. [Descripción de Connector/J](https://dev.mysql.com/doc/connector-j/en/connector-j-overview.html).
4. Oracle. [Oracle Linux 9: módulo MySQL 8.4](https://docs.oracle.com/en/operating-systems/oracle-linux/9/relnotes9.6/ol9-features-DynamicProgramming.html).
5. MySQL. [InnoDB: transacciones, claves foráneas y límites técnicos](https://dev.mysql.com/doc/refman/8.4/en/innodb-introduction.html).
6. MySQL. [Restricciones CHECK desde 8.0.16](https://dev.mysql.com/doc/refman/8.0/en/create-table-check-constraints.html).
7. MySQL. [Operaciones UNION, INTERSECT y EXCEPT](https://dev.mysql.com/doc/refman/8.0/en/set-operations.html).
8. MySQL. [CREATE TRIGGER](https://dev.mysql.com/doc/refman/8.4/en/create-trigger.html).
9. MySQL. [Sintaxis JOIN](https://dev.mysql.com/doc/refman/8.4/en/join.html).
10. MySQL. [Mayúsculas y minúsculas en identificadores](https://dev.mysql.com/doc/refman/8.4/en/identifier-case-sensitivity.html).
11. MySQL. [Valores por defecto y expresiones admitidas por el servidor](https://dev.mysql.com/doc/refman/8.4/en/data-type-defaults.html).
12. PostgreSQL. [Características del motor](https://www.postgresql.org/about/) y [licencia PostgreSQL](https://www.postgresql.org/about/licence/).
13. Oracle. [Límites de CPU, memoria y datos de Database Free](https://docs.oracle.com/en/database/oracle/oracle-database/26/xeinl/licensing-restrictions.html).
14. MariaDB Foundation. [MariaDB Server y licencia GPLv2](https://mariadb.org/about/).
