---
title: "Propuesta del Proyecto"
subtitle: "Documento de Propuesta del Proyecto"
fase: "Fase 1"
version: "1.0"
---
<!-- Created: 22:45 09-Oct-2026 -->
<!-- Updated: 13:03 10-Oct-2026 -->
<!-- Updated: 13:25 10-Oct-2026 -->
<!-- Updated: 18:55 24-Oct-2026 -->
<!-- Updated: 18:55 24-Oct-2026 -->

# Introducción

Este documento presenta la propuesta del proyecto final de la asignatura Bases de Datos 1: **SIGEVET — Sistema
Integral de Gestión Veterinaria**, una base de datos relacional para la Red de Clínicas Veterinarias *Huella Sana
S.A.S.* (caso simulado). La propuesta describe la organización, la problemática, los objetivos, el alcance y los
requerimientos del sistema, y servirá como guía para el modelado (Fase 1) y la implementación (Fase 2).

# Descripción de la organización

*Huella Sana S.A.S.* es una red de clínicas veterinarias de pequeños animales fundada en 2018 en Bogotá. Hoy opera
**cinco sedes**: Usaquén y Chapinero (Bogotá D.C.), Chía (Cundinamarca), Laureles (Medellín) y San Fernando (Cali).
Atiende principalmente perros y gatos, y en menor proporción aves, conejos y hámsteres.

| Aspecto | Situación actual |
|---|---|
| Sedes | 5 clínicas en 4 ciudades, cada una con un director |
| Personal | 27 empleados: veterinarios, auxiliares veterinarios y personal administrativo |
| Clientes | Propietarios de mascotas que pueden ser atendidos en cualquier sede |
| Servicios | Consulta general, especializada y de urgencias, vacunación, cirugía, laboratorio, imagenología, estética y hospitalización |
| Productos | Medicamentos e insumos veterinarios vendidos con o sin fórmula médica |

Table: Caracterización general de la organización

# Planteamiento del problema

## Situación actual

Cada sede administra su información de forma independiente: las agendas se llevan en hojas de cálculo, las
historias clínicas se guardan en carpetas físicas, el inventario de medicamentos se cuenta a mano y la facturación se
hace en un programa de escritorio distinto en cada sede. No existe una fuente única de información para la red.

## Síntomas, causas y consecuencias

| Síntoma observado | Causa | Consecuencia |
|---|---|---|
| Citas duplicadas o cruzadas para un mismo veterinario | Agendas en archivos separados sin validación | Clientes inconformes y tiempos muertos |
| Historia clínica incompleta cuando la mascota va a otra sede | Historias en papel, una carpeta por sede | Riesgo clínico: alergias y tratamientos previos desconocidos |
| Medicamentos agotados o con sobrestock | No hay control de stock mínimo por sede | Ventas perdidas y capital inmovilizado |
| Datos de clientes repetidos y contradictorios | Cada sede registra al cliente de nuevo | Redundancia, anomalías de actualización |
| La gerencia no conoce ingresos por sede ni por servicio | Facturación sin integración | Decisiones sin datos confiables |

Table: Análisis de la problemática

## Pregunta problema

¿Cómo diseñar e implementar una base de datos relacional centralizada, normalizada hasta la Tercera Forma Normal, que
integre la información clínica, operativa y comercial de las cinco sedes de *Huella Sana* y permita consultarla de
forma confiable desde una aplicación web?

# Justificación

Una base de datos relacional centralizada elimina la redundancia (un cliente y una mascota se registran una sola
vez), garantiza la integridad de los datos mediante llaves primarias, foráneas y restricciones, y permite responder
preguntas de negocio con consultas declarativas (álgebra relacional y SQL). Para la asignatura, el caso es apropiado
porque exige todos los conceptos del curso: modelo entidad-relación con más de ocho entidades y relaciones 1:1, 1:N y
N:M, especialización (modelo extendido), normalización hasta 3FN, consultas con uniones, selecciones y proyecciones,
y una implementación real en un RDBMS sobre una máquina virtual Linux con acceso desde una aplicación web.

# Objetivos

## Objetivo general

Diseñar e implementar la base de datos relacional SIGEVET para la red *Huella Sana*, desde el modelo conceptual hasta
el modelo físico en MySQL sobre una máquina virtual Oracle Linux, con una aplicación web de consulta.

## Objetivos específicos

1. Levantar los requerimientos de información de la red y documentar los supuestos y reglas de negocio.
2. Construir el modelo entidad-relación en notación Peter Chen y su versión extendida (especialización).
3. Construir el modelo lógico en notación Barker / Crow's Foot y el modelo relacional en Crow's Foot con MySQL
   Workbench.
4. Normalizar el esquema hasta la Tercera Forma Normal y documentar el proceso.
5. Elaborar el diccionario de datos con tipos, dominios y restricciones de cada columna.
6. Formular al menos 50 consultas en álgebra relacional con su equivalente en SQL.
7. Seleccionar e instalar el sistema operativo y el RDBMS en una máquina virtual (Fase 2).
8. Implementar los scripts de creación, inserción y consulta, y conectar una aplicación web de lectura (Fase 2).
9. Gestionar el proyecto con Git y GitHub con aportes de todos los integrantes.

# Alcance

## El proyecto incluye

- Gestión de la estructura de la red: departamentos, ciudades, sedes y su director.
- Gestión del personal: empleados con especialización en veterinarios y auxiliares; especialidades certificadas.
- Gestión de clientes (con varios teléfonos), mascotas, especies y razas.
- Historia clínica única por mascota, compartida por todas las sedes.
- Agenda de citas por veterinario, sede y servicio; registro de la consulta atendida.
- Fórmula médica (prescripción de medicamentos por consulta).
- Inventario de medicamentos por sede con stock mínimo.
- Facturación de servicios y medicamentos con detalle por línea.
- Consultas de lectura desde una aplicación web de demostración.

## El proyecto no incluye

- Facturación electrónica ante la DIAN, nómina, contabilidad ni pasarela de pagos.
- Compras a proveedores, lotes y fechas de vencimiento de medicamentos.
- Autenticación de usuarios finales en la aplicación web (la app es de solo lectura y de demostración).
- Aplicación móvil, alta disponibilidad o réplicas de la base de datos.

# Actores del sistema

| Actor | Rol frente a la información |
|---|---|
| Gerencia de la red | Consulta indicadores de ingresos, citas y ocupación por sede |
| Director de sede | Supervisa agenda, personal e inventario de su sede |
| Recepcionista / administrativo | Registra clientes, mascotas, citas y facturas |
| Veterinario | Atiende citas, registra consultas y formula medicamentos |
| Auxiliar veterinario | Asiste consultas y registra signos vitales |
| Cliente (propietario) | Fuente de datos; recibe atención y facturas |

Table: Actores del sistema

# Requerimientos funcionales

| Código | Requerimiento | Prioridad |
|---|---|---|
| RF-01 | Registrar departamentos y ciudades con su código DANE | Media |
| RF-02 | Registrar sedes con su ciudad y su director (un director por sede) | Alta |
| RF-03 | Registrar empleados indicando su sede y su tipo (veterinario, auxiliar o administrativo) | Alta |
| RF-04 | Registrar los datos profesionales de veterinarios y las especialidades certificadas con su fecha | Media |
| RF-05 | Registrar los datos de auxiliares (área, certificación y turno) | Media |
| RF-06 | Registrar clientes con documento único y uno o varios teléfonos | Alta |
| RF-07 | Registrar mascotas con raza, especie (derivada de la raza), propietario y microchip opcional | Alta |
| RF-08 | Abrir automáticamente una única historia clínica por mascota | Alta |
| RF-09 | Mantener el catálogo de servicios con precio y duración | Media |
| RF-10 | Agendar citas validando que un veterinario no tenga dos citas a la misma hora | Alta |
| RF-11 | Cambiar el estado de la cita (programada, atendida, cancelada, no asistió) | Alta |
| RF-12 | Registrar la consulta de una cita atendida con peso, temperatura, diagnóstico y auxiliar | Alta |
| RF-13 | Registrar la fórmula médica: medicamentos, dosis, frecuencia, duración y cantidad | Alta |
| RF-14 | Controlar el inventario de cada medicamento por sede con stock mínimo | Alta |
| RF-15 | Emitir facturas con número consecutivo único, cliente, sede, empleado y medio de pago | Alta |
| RF-16 | Registrar líneas de factura que cobren un servicio o un medicamento (nunca ambos) | Alta |
| RF-17 | Consultar la historia clínica completa de una mascota en cualquier sede | Alta |
| RF-18 | Consultar la agenda de un veterinario o de una sede por fecha | Alta |
| RF-19 | Consultar medicamentos con existencias por debajo del mínimo por sede | Media |
| RF-20 | Consultar ingresos por sede, por servicio y por periodo | Media |

Table: Requerimientos funcionales

# Requerimientos no funcionales

| Código | Requerimiento |
|---|---|
| RNF-01 | El esquema debe estar normalizado hasta Tercera Forma Normal (3FN). |
| RNF-02 | Toda regla de integridad expresable de forma declarativa se implementa con PK, FK, UNIQUE, CHECK y NOT NULL. |
| RNF-03 | El RDBMS debe ser distinto de SQL Server y ejecutarse en una máquina virtual Linux/Unix. |
| RNF-04 | La aplicación web se conecta con un usuario de solo lectura (principio de mínimo privilegio). |
| RNF-05 | Las restricciones siguen una convención de nombres (`PK_`, `FK_`, `UQ_`, `CK_`) para facilitar el mantenimiento. |
| RNF-06 | Todo el código y la documentación se versionan en un repositorio GitHub con aportes de los tres integrantes. |

Table: Requerimientos no funcionales

# Visión preliminar del modelo de datos

El análisis identificó **18 entidades** y **26 relaciones**, que cumplen y superan los mínimos del enunciado:

| Criterio del enunciado | Cumplimiento en SIGEVET |
|---|---|
| Al menos 8 entidades | 18 entidades (17 fuertes y 1 débil), implementadas en 22 tablas |
| Relaciones uno a uno | 5: DIRIGE (empleado–sede), TIENE (mascota–historia clínica), GENERA (cita–consulta) y las 2 de la especialización |
| Relaciones uno a muchos | 18, por ejemplo PROPIETARIO_DE (cliente–mascota) y CONTIENE (factura–detalle) |
| Relaciones muchos a muchos | 3: CERTIFICADO_EN (veterinario–especialidad), PRESCRIBE (consulta–medicamento) y ALMACENA (sede–medicamento) |
| Modelo extendido | Especialización disyunta y parcial de EMPLEADO en VETERINARIO y AUXILIAR |
| Normalización a 3FN | Atributo multivaluado (teléfonos) → 1FN; detalle de factura → 2FN; raza → especie y ciudad → departamento → 3FN |
| Consultas complejas | 55 ejercicios de álgebra relacional con su SQL y resultado verificado |

Table: Cumplimiento de los criterios del enunciado

![Vista global del modelo (entidades y relaciones, notación Chen)](diagramas/png/04_Chen_Global_191116-24Oct2026.png){width=100%}

# Metodología y plan de trabajo

El proyecto sigue un ciclo de diseño de bases de datos en cascada con revisiones al final de cada fase:

1. **Análisis:** levantamiento de requerimientos, supuestos y reglas de negocio.
2. **Diseño conceptual:** MER en notación Chen y EER.
3. **Diseño lógico:** modelo lógico Barker / Crow's Foot, modelo relacional, normalización y diccionario.
4. **Validación:** 55 consultas de álgebra relacional verificadas con datos de prueba.
5. **Diseño físico e implementación (Fase 2):** selección de SO y RDBMS, VM, scripts DDL/DML y aplicación web.

| Orden | Hito |
|---|---|
| 1 | Inicio del proyecto y acta de constitución |
| 2 | Envío de la propuesta y la Fase 1 para validación del docente |
| 3 | Entrega Fase 2 (VM, RDBMS, scripts, app, repositorio, video) |
| 4 | Sustentación y cierre |

Table: Hitos del proyecto

# Equipo de trabajo

| Integrante | Rol principal | Responsabilidades |
|---|---|---|
| Juan David Polo Herrera | Gerente del proyecto y DBA | Planeación, modelo relacional, normalización, scripts y control de versiones |
| Sebastián Castellanos Parra | Analista y modelador de datos | Requerimientos, supuestos, MER Chen/EER, diccionario de datos |
| Juan Miguel Ramos | Desarrollador web e infraestructura | Álgebra relacional y SQL, máquina virtual, aplicación web |

Table: Roles del equipo

# Herramientas

| Necesidad | Herramienta |
|---|---|
| Modelado | Draw.io para los modelos de datos; MySQL Workbench para administrar y consultar el esquema |
| Sistema operativo (VM) | Oracle Linux 9 |
| RDBMS | MySQL 8.4 LTS (Community), administrado con MySQL Workbench |
| Aplicación web | Java 21 + Servlets/JSP (Tomcat 10.1) + JDBC (MySQL Connector/J) |
| Control de versiones | Git y GitHub |
| Documentación | Markdown + Pandoc → Microsoft Word |

Table: Herramientas previstas

# Riesgos iniciales

| Riesgo | Probabilidad | Impacto | Respuesta |
|---|---|---|---|
| Recursos limitados del equipo para la VM | Media | Alto | MySQL consume poca memoria; VM de 6 GB |
| Cambios de alcance tardíos | Media | Medio | Supuestos aprobados por el docente al inicio |
| Modelo con errores de cardinalidad | Baja | Alto | Validación con 55 consultas sobre datos de prueba |
| Aportes desiguales en GitHub | Media | Medio | Reparto de tareas por integrante y revisión semanal |

Table: Riesgos iniciales

# Conclusión

SIGEVET resuelve una necesidad real de integración de información en una red de clínicas veterinarias y permite
aplicar de forma completa los contenidos de Bases de Datos 1. El modelo propuesto tiene 18 entidades, todos los tipos
de relación, una especialización y un esquema que exige normalización hasta 3FN, por lo que el equipo considera que
la propuesta es viable, suficientemente compleja y alineada con los objetivos de la asignatura.
