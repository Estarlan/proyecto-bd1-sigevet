---
title: "EDT y diccionario de paquetes de trabajo"
subtitle: "EDT y diccionario de paquetes de trabajo"
fase: "Transversal"
version: "1.0"
---
<!-- Created: 18:15 19-Oct-2026 -->
<!-- Updated: 18:17 19-Oct-2026 -->

# Estructura orientada a entregables

La estructura de desglose del trabajo organiza el alcance completo de SIGEVET bajo la regla del cien por ciento: cada resultado se asigna a un único paquete de tercer nivel y cada paquete tiene un responsable de integración. El cronograma utiliza los mismos códigos; H1, H2 y H3 son hitos de control y no paquetes adicionales.

![EDT/WBS del proyecto SIGEVET](diagramas/png/edt_wbs.png){width=100%}

# Jerarquía y límites

| Código | Componente | Contenido consolidado |
|---|---|---|
| 1 | SIGEVET | Proyecto académico de base de datos y consulta web para Huella Sana. |
| 1.1 | Gestión del proyecto | Constitución, interesados y seguimiento de las líneas base. |
| 1.2 | Fase 1 Análisis y modelado | Propuesta, supuestos, modelos, normalización, diccionario y álgebra. |
| 1.3 | Fase 2 Implementación | Análisis de plataforma, VM, RDBMS, scripts, aplicación, repositorio y video. |
| 1.4 | Cierre | Sustentación, aceptación, balance y acta de cierre. |
Table: Niveles primero y segundo de la EDT

# Diccionario de la EDT

Para facilitar lectura de la tabla, JD corresponde a Juan David Polo Herrera, Gerente del proyecto y DBA; SC a Sebastián Castellanos Parra, Analista y modelador de datos; JM a Juan Miguel Ramos, Desarrollador web e infraestructura. La responsabilidad identifica al integrador del paquete; el equipo participa en las revisiones y en la sustentación.

## Gestión del proyecto

| Código | Paquete | Descripción | Responsable | Entregable |
|---|---|---|---|---|
| 1.1.1 | Constitución | Definir objetivos, alcance, riesgos y autorización académica | JD | Acta de constitución. |
| 1.1.2 | Interesados | Registrar expectativas, poder, interés y participación | SC | Registro, matriz y estrategia de interesados. |
| 1.1.3 | Seguimiento | Integrar EDT, cronograma y presupuesto; revisar avance y cambios semanalmente | JD | EDT y diccionario; cronograma Excel y Gantt; presupuesto Excel; decisiones de revisión. |
Table: Paquetes de gestión

## Fase 1 Análisis y modelado

| Código | Paquete | Descripción | Responsable | Entregable |
|---|---|---|---|---|
| 1.2.1 | Propuesta | Delimitar problema, requisitos y exclusiones de Huella Sana | SC | Propuesta del proyecto. |
| 1.2.2 | Supuestos | Precisar reglas de negocio y condiciones del caso simulado | SC | Documento de supuestos. |
| 1.2.3 | MER Chen | Representar entidades, atributos, relaciones y cardinalidades | SC | MER notación Peter Chen. |
| 1.2.4 | MER Extendido | Representar especialización disjunta y parcial de EMPLEADO | SC | MER Extendido. |
| 1.2.5 | Modelo lógico | Elaborar MER Barker / Crow's Foot en MySQL Workbench | SC | MER Barker / Crow's Foot (MySQL Workbench). |
| 1.2.6 | Modelo relacional | Transformar el modelo a tablas, llaves y restricciones | JD | Modelo relacional Crow's Foot (MySQL Workbench). |
| 1.2.7 | Normalización | Justificar dependencias y descomposición hasta 3FN | JD | Sección de normalización del modelo relacional. |
| 1.2.8 | Diccionario | Documentar las 22 tablas, columnas, dominios y restricciones | SC | Diccionario de datos. |
| 1.2.9 | Álgebra relacional | Resolver y ejecutar 55 ejercicios y sus equivalentes SQL | JM | Documento de álgebra relacional, 55 ejercicios verificados. |
Table: Paquetes de análisis y modelado

## Fase 2 Implementación

| Código | Paquete | Descripción | Responsable | Entregable |
|---|---|---|---|---|
| 1.3.1 | Selección de SO | Comparar alternativas y justificar Oracle Linux 9 | JM | Análisis de selección de SO. |
| 1.3.2 | Máquina virtual | Configurar recursos, arranque y acceso del entorno Linux | JM | Implementación de la VM e instrucciones de reproducción. |
| 1.3.3 | Selección de RDBMS | Comparar opciones y justificar MySQL 8.4 LTS | JD | Análisis de selección de RDBMS. |
| 1.3.4 | Instalación de RDBMS | Preparar el servicio `mysqld`, la base `sigevet` y los usuarios `vetdb` y `vet_app` | JD | Implementación del RDBMS y evidencia de conexión. |
| 1.3.5 | Scripts y README | Crear objetos, insertar datos ficticios y ejecutar consultas y utilidades | JD | Scripts DDL, DML y varios con README y orden de ejecución. |
| 1.3.6 | Aplicación web | Conectar la aplicación Java (Servlets y JSP) por JDBC para consultas | JM | Conexión de la aplicación web de solo lectura. |
| 1.3.7 | Repositorio Git | Integrar aportes, estructura y documentación de reconstrucción | JD | Repositorio Git/GitHub con contribuciones del equipo. |
| 1.3.8 | Video | Validar audio y cámara del equipo en la demostración | JM | Video de demostración de máximo 15 minutos. |
Table: Paquetes de implementación

## Cierre

| Código | Paquete | Descripción | Responsable | Entregable |
|---|---|---|---|---|
| 1.4.1 | Sustentación y aceptación | Consolidar evidencias, sustentar y atender observaciones académicas | SC | Lista de entregables y registro de aceptación en el acta. |
| 1.4.2 | Cierre documental | Registrar balance, desviaciones, lecciones y espacios de firma | JD | Acta de cierre. |
Table: Paquetes de cierre

# Control y aceptación de paquetes

Un paquete se considera completo cuando su entregable está versionado, es legible y supera la revisión del responsable y de otro integrante. En modelos se verifica correspondencia con reglas y DDL; en álgebra, resultados de los 55 ejercicios; en plataforma, reproducción y conexión; en video, duración y participación. El docente valida Fase 1 en Semana 8, Fase 2 en Semana 13 y el cierre en Semana 14.

El seguimiento de 1.1.3 y el repositorio de 1.3.7 transcurren en paralelo con las fases; sus horas son esfuerzo de coordinación e integración, sin duplicar el trabajo de los demás paquetes. Los cambios se registran por código y se evalúan contra alcance, dependencias y presupuesto antes de aprobarse.
