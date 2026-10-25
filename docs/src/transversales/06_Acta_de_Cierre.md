---
title: "Acta de cierre del proyecto"
subtitle: "Acta de cierre del proyecto"
fase: "Transversal"
version: "1.0"
---
<!-- Created: 18:15 19-Oct-2026 -->

# Resumen del cierre académico

SIGEVET integra el caso simulado de Huella Sana S.A.S. mediante un esquema relacional de 22 tablas, modelos conceptuales y lógicos, scripts, datos de prueba y una aplicación de consulta. Este acta documenta el balance del proyecto de Bases de Datos 1 y la aceptación de los productos dentro de un horizonte de 14 semanas.

Los estados de entrega corresponden al registro académico del equipo; la aceptación formal del docente se expresa mediante su firma. No se atribuye aceptación a una clínica real ni se declara una puesta en producción.

# Cumplimiento de objetivos

| Objetivo | Resultado de cierre | Evidencia de aceptación |
|---|---|---|
| Analizar requerimientos y reglas del caso | Documentados para cinco sedes y sus procesos clínicos y comerciales | Propuesta y documento de supuestos. |
| Diseñar modelos conceptual, extendido, lógico y relacional | Modelos consistentes y esquema normalizado a 3FN | Diagramas Chen/EER, modelos de MySQL Workbench (`.mwb`) y modelo relacional. |
| Documentar estructura y restricciones | Diccionario coherente con el esquema de 22 tablas | Diccionario de datos y comentarios del catálogo (`02_comentarios.sql`). |
| Validar consultas | 55 ejercicios ejecutados sobre datos de prueba | Documento de álgebra, SQL equivalente y resultados. |
| Implementar plataforma y acceso web | Entorno Oracle Linux, MySQL 8.4 y conexión de consulta | VM, scripts, README y demostración de lectura con `vet_app`. |
| Colaborar y comunicar resultados | Productos versionados y demostración del equipo | Repositorio Git y video de máximo 15 minutos con audio y cámara de los tres integrantes. |
Table: Balance de objetivos y evidencia documental

# Registro de entregables

| Fase | Entregable | Estado |
|---|---|---|
| Fase 1 | Propuesta del proyecto | Entregado |
| Fase 1 | Documento de supuestos | Entregado |
| Fase 1 | MER notación Peter Chen | Entregado |
| Fase 1 | MER Extendido | Entregado |
| Fase 1 | MER Barker / Crow's Foot (MySQL Workbench) | Entregado |
| Fase 1 | Modelo relacional Crow's Foot (MySQL Workbench), con normalización | Entregado |
| Fase 1 | Diccionario de datos | Entregado |
| Fase 1 | Documento de álgebra relacional, 55 ejercicios | Entregado |
| Fase 2 | Análisis de selección de SO | Entregado |
| Fase 2 | Implementación de la VM | Entregado |
| Fase 2 | Análisis de selección de RDBMS | Entregado |
| Fase 2 | Implementación del RDBMS | Entregado |
| Fase 2 | Scripts de creación de objetos, inserción de registros y varios, con README | Entregado |
| Fase 2 | Conexión de la aplicación web | Entregado |
| Fase 2 | Repositorio Git | Entregado |
| Fase 2 | Video de demostración, máximo 15 minutos, todos con audio y cámara | Entregado |
| Transversal | Acta de constitución del proyecto | Entregado |
| Transversal | Identificación de interesados | Entregado |
| Transversal | EDT/WBS y diccionario de paquetes | Entregado |
| Transversal | Cronograma, Excel y Gantt | Entregado |
| Transversal | Presupuesto y Excel | Entregado |
| Transversal | Acta de cierre | Entregado |
Table: Relación de productos presentados para aceptación

# Desviaciones y balance de recursos

| Aspecto | Ajuste de esfuerzo | Tratamiento | Impacto sobre hitos |
|---|---|---|---|
| Herramientas de macOS y virtualización | Redistribución de 4 horas dentro del paquete de VM | Uso de micromamba y Lima; se absorbió en las 16 horas de VM | 0 semanas. |
| Dependencia circular SEDE–EMPLEADO | Redistribución de 2 horas dentro de scripts | Creación posterior de la FK del director mediante `ALTER TABLE` | 0 semanas. |
| Cambio de RDBMS de Oracle Database Free a MySQL 8.4 | Redistribución de horas dentro de la Fase 2 | Scripts, modelos de Workbench, aplicación y documentos ajustados; la versión en Oracle queda como respaldo | 0 semanas. |
Table: Balance académico de ajustes menores dentro de la línea base

Las redistribuciones anteriores son estimaciones de esfuerzo para este balance académico; no constituyen registros de tiempos medidos. No incrementan las 385 horas presupuestadas. Los hitos se conservan en Semana 8, Semana 13 y Semana 14. La valoración base permanece en **11.603.900 COP**, incluida la reserva de 1.054.900 COP; no se certifican desembolsos ni consumos de reserva sin soportes contables.

# Lecciones aprendidas

| Lección del proyecto | Aplicación posterior |
|---|---|
| Homebrew ya no publica binarios para macOS Intel en el entorno empleado; se utilizaron micromamba y Lima con Apple Virtualization.framework | Verificar arquitectura y disponibilidad de paquetes antes de fijar instrucciones de instalación. |
| La relación circular SEDE–EMPLEADO impedía crear ambas referencias en una sola secuencia simple | Crear las tablas primero y agregar con `ALTER TABLE` la FK del director; documentar el orden en README. |
| Mantener modelos, diccionario y scripts alineados | Cada cambio del modelo se aplica a la vez en MySQL Workbench, el diccionario y los scripts SQL, y se valida con `cargar_bd.sh --reiniciar`. |
| Los 55 ejercicios de álgebra se verificaron ejecutándolos sobre datos de prueba | Contrastar expresión, SQL y resultado; no aceptar consultas únicamente por inspección visual. |
| MySQL Workbench no reconoce valores por defecto escritos como expresión (`DEFAULT (CURRENT_DATE)`) | Probar el script en la herramienta de modelado antes de cerrar el diseño físico; se usó `DATETIME DEFAULT CURRENT_TIMESTAMP`. |
| Git/GitHub permitió trabajo colaborativo y revisión de aportes | Dividir por paquetes, revisar diferencias antes de integrar y conservar README reproducibles. |
| El usuario `vet_app` limita el acceso de la demostración a consultas | Mantener separados el esquema dueño y la cuenta de aplicación para evitar cambios accidentales. |
Table: Lecciones técnicas y de colaboración del equipo

# Transferencia y aceptación

Se entrega al docente el repositorio con fuentes, scripts, modelos y documentación, así como los archivos finales y el video. El equipo conserva las instrucciones de reconstrucción y conexión; la revisión de la entrega exige comprobar correspondencia entre fuentes y productos generados, legibilidad y participación de todos los integrantes.

No se transfieren servicios productivos ni obligaciones de soporte a Huella Sana, por tratarse de una organización ficticia. Las exclusiones de la propuesta siguen vigentes. El gerente consolida observaciones de sustentación y el patrocinador determina su aceptación académica. Los espacios de firma no representan aprobaciones ya otorgadas.

| Nombre | Documento | Rol | Firma |
|---|---|---|---|
| Juan David Polo Herrera | C.C. 1117506677 | Gerente del proyecto y DBA | |
| Sebastián Castellanos Parra | C.C. 1031811214 | Analista y modelador de datos | |
| Juan Miguel Ramos | C.C. 1025528586 | Desarrollador web e infraestructura | |
| Ing., Esp., M.Ed. Christian Felipe Duarte Arévalo | | Docente / patrocinador | |

Table: Aceptación y cierre académico
