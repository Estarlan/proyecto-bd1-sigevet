---
title: "Acta de constitución del proyecto"
subtitle: "Acta de constitución del proyecto"
fase: "Transversal"
version: "1.0"
---
<!-- Created: 18:15 19-Oct-2026 -->
<!-- Updated: 18:17 19-Oct-2026 -->

# Autorización y justificación

Se constituye SIGEVET, Sistema Integral de Gestión Veterinaria, como proyecto académico de Bases de Datos 1 de la Universidad El Bosque. Su propósito es integrar la información de las cinco sedes de la organización ficticia Huella Sana S.A.S.: Usaquén, Chapinero, Chía, Laureles y San Fernando.

Las agendas aisladas, las historias clínicas físicas y la facturación sin integración producen duplicidad de clientes, cruces de citas y consultas incompletas entre sedes. Se autoriza construir una base relacional centralizada y una aplicación web de consulta para demostrar integridad y trazabilidad clínica, operativa y comercial.

# Objetivos y criterios de éxito

## Objetivo general

Diseñar e implementar SIGEVET en MySQL sobre una máquina virtual Oracle Linux, desde el modelo conceptual hasta la consulta web, para integrar la información de Huella Sana.

## Objetivos específicos

- Documentar requerimientos, supuestos y reglas de negocio de las cinco sedes.
- Elaborar MER Chen, MER Extendido y modelos lógico y relacional en MySQL Workbench.
- Normalizar las 22 tablas del esquema a tercera forma normal y documentar tipos, dominios y restricciones.
- Verificar 55 ejercicios de álgebra relacional mediante sus consultas equivalentes y datos de prueba.
- Implementar VM, RDBMS y scripts reproducibles, y conectar la aplicación Java (Servlets y JSP) con el usuario de consulta `vet_app`.
- Mantener aportes verificables en Git/GitHub y presentar una demostración de máximo 15 minutos con audio y cámara de todos los integrantes.

Se considera satisfactorio el proyecto cuando el docente valida los entregables oficiales, los scripts reconstruyen la base `sigevet`, las consultas producen los resultados esperados y la aplicación acredita acceso de solo lectura. La validación de Fase 1 se ubica en Semana 8, la de Fase 2 en Semana 13 y el cierre en Semana 14.

# Alcance autorizado

## Incluye

Estructura geográfica y sedes con director; empleados, veterinarios, auxiliares y especialidades; clientes con teléfonos; mascotas, razas y especies; historia clínica única por mascota; citas y consultas; prescripciones; inventario por sede y stock mínimo; facturas y sus detalles. Incluye modelado, normalización, implementación, datos ficticios y consultas web de demostración. Las operaciones de registro se demuestran mediante SQL; la aplicación web permite únicamente lectura.

## No incluye

Facturación electrónica ante la DIAN, nómina, contabilidad, pagos en línea, compras a proveedores, lotes o vencimientos de medicamentos, autenticación de usuarios finales, aplicación móvil, alta disponibilidad ni réplicas. No se compromete una puesta en producción en clínicas reales.

# Entregables por fase

| Fase | Entregables oficiales |
|---|---|
| Fase 1 | Propuesta del proyecto; Documento de supuestos; MER notación Peter Chen; MER Extendido; MER Barker / Crow's Foot (MySQL Workbench); Modelo relacional Crow's Foot (MySQL Workbench); Diccionario de datos; Documento de álgebra relacional (55 ejercicios). |
| Fase 2 | Análisis de selección de SO; Implementación de la VM; Análisis de selección de RDBMS; Implementación del RDBMS; Scripts de creación de objetos, inserción de registros y varios con README; Conexión de la aplicación web; Repositorio Git; Video de demostración de máximo 15 minutos, con todos los integrantes con audio y cámara. |
| Transversal | Acta de constitución; Identificación de interesados; EDT/WBS; Cronograma; Presupuesto; Acta de cierre. |
Table: Productos sujetos a aceptación académica

La normalización forma parte del modelo relacional y tiene paquete propio en la EDT; no sustituye ningún entregable oficial.

# Hitos de control

| Orden | Hito | Referencia temporal |
|---|---|---|
| 1 | Inicio y constitución del proyecto | Semana 1 |
| 2 | Envío de la propuesta y la Fase 1 para validación del docente | Semana 8 |
| 3 | Entrega de la Fase 2 | Semana 13 |
| 4 | Sustentación, aceptación y cierre | Semana 14 |
Table: Secuencia de hitos autorizada

# Supuestos y restricciones

Se supone disponibilidad de los tres integrantes y de sus portátiles, acceso a Internet y retroalimentación del docente en las revisiones. Los actores de Huella Sana son perfiles simulados; el equipo valida reglas del caso con el docente, sin atribuir entrevistas a una empresa real.

El horizonte es de 14 semanas y 385 horas de esfuerzo distribuido, sin dedicación exclusiva. Se utilizarán Oracle Linux 9, MySQL 8.4 LTS con MySQL Workbench, una VM con KVM en el computador servidor (con simulación previa en Lima en macOS) y Java con Servlets/JSP sobre Tomcat, dentro de la VM de 6 GB prevista en la propuesta. Los datos serán ficticios y el esquema de consulta tendrá privilegios mínimos. El medio de virtualización se adecúa al equipo anfitrión sin cambiar el alcance funcional.

# Riesgos de alto nivel

| Riesgo | Probabilidad / impacto | Respuesta y responsable |
|---|---|---|
| Recursos insuficientes o incompatibilidad de virtualización | Media / Alto | Validar arquitectura, memoria y arranque antes de instalar MySQL; Juan Miguel Ramos. |
| Cambios tardíos de alcance | Media / Medio | Registrar solicitud, estimar horas e impacto y obtener validación del docente; Juan David Polo Herrera. |
| Cardinalidades o restricciones incorrectas | Baja / Alto | Revisar modelos, normalización y resultados de 55 consultas; Sebastián Castellanos Parra. |
| Aportes desiguales al repositorio | Media / Medio | Asignar paquetes, revisar avances semanalmente y verificar contribuciones; Juan David Polo Herrera. |
| Divergencia entre DDL, diccionario y diagramas | Media / Alto | Regenerar desde la especificación común y revisar diferencias; Juan David Polo Herrera. |
Table: Riesgos y respuestas iniciales

# Presupuesto resumido

| Rubro | Valor COP |
|---|---:|
| Talento humano, 385 horas | 9.121.000 |
| Depreciación de tres portátiles | 840.000 |
| Software de uso gratuito | 0 |
| Internet y energía | 588.000 |
| Costo directo | 10.549.000 |
| Imprevistos, 10 % del costo directo | 1.054.900 |
| Total autorizado | 11.603.900 |
Table: Línea base económica del proyecto

El presupuesto total es **11.603.900 COP** y coincide con el presupuesto detallado y el Excel. Valora aportes académicos. El gerente registra el uso de reserva y solicita validación del docente si altera el alcance.

# Dirección y responsabilidades

| Nombre e identificación | Rol | Responsabilidad y autoridad |
|---|---|---|
| Juan David Polo Herrera (1117506677) | Gerente del proyecto y DBA | Coordinar líneas base, modelo relacional, normalización, scripts y Git. |
| Sebastián Castellanos Parra (1031811214) | Analista y modelador de datos | Requerimientos, interesados, supuestos, Chen/EER, modelo lógico y diccionario; verificar trazabilidad. |
| Juan Miguel Ramos (1025528586) | Desarrollador web e infraestructura | Álgebra relacional y SQL, análisis de SO, VM, aplicación web y coordinación del video. |
| Ing., Esp., M.Ed. Christian Felipe Duarte Arévalo | Docente / patrocinador | Orientar, validar alcance y criterios de aceptación, evaluar fases y autorizar cierre académico; sin financiación monetaria asumida. |
Table: Equipo autorizado y patrocinio académico

# Aprobación

La firma formaliza la aceptación del alcance y de las líneas base. Los espacios se dejan para sus titulares.

| Nombre | Documento | Rol | Firma |
|---|---|---|---|
| Juan David Polo Herrera | C.C. 1117506677 | Gerente del proyecto y DBA | |
| Sebastián Castellanos Parra | C.C. 1031811214 | Analista y modelador de datos | |
| Juan Miguel Ramos | C.C. 1025528586 | Desarrollador web e infraestructura | |
| Ing., Esp., M.Ed. Christian Felipe Duarte Arévalo | | Docente / patrocinador | |

Table: Aprobación del acta de constitución
