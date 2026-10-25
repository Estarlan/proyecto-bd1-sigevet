---
title: "Cronograma del proyecto"
subtitle: "Cronograma del proyecto"
fase: "Transversal"
version: "1.0"
---
<!-- Created: 18:15 19-Oct-2026 -->
<!-- Updated: 18:17 19-Oct-2026 -->
<!-- Updated: 18:17 19-Oct-2026 -->

# Línea base y criterios de programación

El cronograma distribuye SIGEVET entre Semana 1 y Semana 14, con 22 paquetes EDT y tres hitos de duración nula. El esfuerzo total es de 385 horas; una semana de duración indica una ventana de ejecución y no una dedicación de tiempo completo. La gestión y el repositorio se desarrollan en paralelo para apoyar las dos fases.

Las dependencias son de fin a inicio entre paquetes: el sucesor inicia en una semana posterior al fin de su predecesora. Los hitos se verifican al final de la semana de sus productos; por ello pueden compartir semana con las actividades que los habilitan. Los paquetes 1.4.1 y 1.4.2 se integran durante Semana 14 y habilitan el hito de cierre al terminarla.

![Cronograma Gantt del proyecto SIGEVET](diagramas/png/cronograma_gantt.png){width=100%}

# Actividades y responsables

JD: Juan David Polo Herrera, Gerente del proyecto y DBA. SC: Sebastián Castellanos Parra, Analista y modelador de datos. JM: Juan Miguel Ramos, Desarrollador web e infraestructura. El responsable integra el resultado; los tres participan en sustentación y video dentro de la coordinación de sus respectivos paquetes.

| Rama EDT | Paquetes y responsables | Ventana de trabajo | Horas |
|---|---|---|---:|
| 1.1 Gestión | Acta y control (JD); interesados (SC) | Semana 1 a Semana 14 | 52 |
| 1.2 Fase 1 | Análisis y modelos (SC); relacional y 3FN (JD); diccionario (SC); álgebra (JM) | Semana 1 a Semana 8 | 196 |
| 1.3 Fase 2 | SO y VM (JM); RDBMS y scripts (JD); aplicación y video (JM); repositorio (JD) | Semana 9 a Semana 13; repositorio desde Semana 1 | 121 |
| 1.4 Cierre | Sustentación y aceptación (SC); acta de cierre (JD) | Semana 14 | 16 |
| Total | Tres integrantes, 22 paquetes y tres hitos sin esfuerzo adicional | 14 semanas | 385 |
Table: Resumen del cronograma por rama de la EDT

La hoja Tareas del Excel contiene el detalle de las 25 actividades e hitos: ID, actividad, responsable, semana de inicio, semana de fin, duración calculada, horas y predecesoras. La secuencia conduce a H1 en Semana 8, H2 en Semana 13 y H3 en Semana 14. El trabajo de repositorio empieza en Semana 1; la implementación de la plataforma comienza en Semana 9, después de H1.

# Resumen semanal de resultados

| Semana | Resultado principal |
|---|---|
| Semana 1 | Constitución, propuesta e inicio del registro de interesados, seguimiento y repositorio. |
| Semana 2 | Supuestos y registro de interesados completos. |
| Semana 3 | MER Chen y cardinalidades revisadas. |
| Semana 4 | MER Extendido y especialización de empleados. |
| Semana 5 | MER Barker / Crow's Foot en MySQL Workbench. |
| Semana 6 | Modelo relacional con llaves y restricciones. |
| Semana 7 | Normalización e inicio de verificación del álgebra. |
| Semana 8 | Diccionario, 55 ejercicios y fin de Fase 1. |
| Semana 9 | Análisis de selección de SO y RDBMS. |
| Semana 10 | Máquina virtual implementada. |
| Semana 11 | RDBMS configurado. |
| Semana 12 | Scripts y README reproducibles. |
| Semana 13 | Aplicación conectada, repositorio consolidado, video y fin de Fase 2. |
| Semana 14 | Sustentación, aceptación y acta de cierre. |
Table: Cobertura del horizonte de catorce semanas

# Carga de trabajo y control

| Integrante | Horas estimadas | Criterio de distribución |
|---|---:|---|
| Juan David Polo Herrera | 153 | Gestión, diseño relacional, normalización, RDBMS y scripts. |
| Sebastián Castellanos Parra | 136 | Interesados, análisis, modelos conceptuales y lógico, diccionario y aceptación. |
| Juan Miguel Ramos | 96 | Álgebra, SO, VM, conexión web y video. |
| Total | 385 | Esfuerzo imputado al presupuesto, sin sumar horas de hitos. |
Table: Carga acumulada por integrante

Para estimar carga semanal, las horas se distribuyen uniformemente dentro de la duración de cada paquete. Con ese criterio la mayor carga es de 35 horas en una semana para JD; las ventanas admiten revisiones sin modificar los hitos. Una semana de duración no equivale a 40 horas presupuestadas.

El libro `entregables/Transversales/04_Cronograma_SIGEVET.xlsx` contiene las hojas Tareas y Gantt. Tareas calcula duración como fin menos inicio más uno; para hitos utiliza cero. Gantt referencia Tareas y señala semanas activas mediante fórmulas y formato condicional. Las fórmulas se recalculan al abrir en Excel o una aplicación compatible. La imagen se genera con la misma lista de actividades.

Cada semana el gerente revisa avance, esfuerzo pendiente y predecesoras. Si un cambio compromete Semana 8, Semana 13 o Semana 14, el equipo plantea redistribución de horas y solicita validación al docente antes de modificar la línea base.
