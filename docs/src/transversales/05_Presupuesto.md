---
title: "Presupuesto del proyecto"
subtitle: "Presupuesto del proyecto"
fase: "Transversal"
version: "1.0"
---
<!-- Created: 18:15 19-Oct-2026 -->

# Base de estimación y resultado

El presupuesto académico de SIGEVET es **11.603.900 COP**, incluido un 10 % de imprevistos sobre los costos directos. Se valora el esfuerzo de 385 horas del cronograma, la utilización de tres portátiles durante 14 semanas y los servicios compartidos que permiten construir y demostrar el proyecto.

Los valores son supuestos internos de planeación, expresados en pesos colombianos; no son cotizaciones de mercado ni una liquidación laboral. El trabajo estudiantil, los portátiles y la conectividad son aportes en especie del equipo. No se asumen salarios pagados, compras de equipos ni desembolsos certificados. El costo del patrocinio docente no se imputa al equipo.

# Talento humano

Las tarifas se justifican como valoración de perfiles junior o estudiantes: 25.000 COP/h para gestión y administración de bases de datos por responsabilidad de integración; 22.000 COP/h para análisis y modelado; 24.000 COP/h para desarrollo e infraestructura por configuración de VM y conexión a MySQL. Son valores de referencia acordados para este ejercicio, sin prestaciones o tributos adicionales simulados.

| Integrante y rol | Horas | Tarifa COP/h | Subtotal COP |
|---|---:|---:|---:|
| Juan David Polo Herrera — Gerente del proyecto y DBA | 153 | 25.000 | 3.825.000 |
| Sebastián Castellanos Parra — Analista y modelador de datos | 136 | 22.000 | 2.992.000 |
| Juan Miguel Ramos — Desarrollador web e infraestructura | 96 | 24.000 | 2.304.000 |
| Total talento humano | 385 | — | 9.121.000 |
Table: Valoración del esfuerzo de los tres integrantes

Las horas provienen de las actividades del cronograma; los hitos tienen cero horas y no incrementan el costo. El seguimiento y el repositorio tienen esfuerzo propio de coordinación y no duplican la construcción de productos.

# Hardware y depreciación

Se utiliza depreciación lineal de uso, con valor de referencia de 3.120.000 COP por portátil, valor residual de cero y vida útil de 156 semanas. La imputación por portátil es $(3.120.000-0) / 156 \times 14 = 280.000$ COP. Se considera el uso durante todo el proyecto, independientemente de la dedicación parcial; es una imputación académica del recurso disponible, sin compra de hardware.

| Concepto | Cantidad | Valor unitario imputado COP | Subtotal COP |
|---|---:|---:|---:|
| Depreciación de portátil durante 14 semanas | 3 | 280.000 | 840.000 |
Table: Imputación de hardware por depreciación lineal

# Software y licencias

| Herramienta | Cantidad de referencias | Costo unitario COP | Subtotal COP | Condición de uso |
|---|---:|---:|---:|---|
| Oracle Linux | 1 | 0 | 0 | Distribución de uso gratuito; no se contrata soporte comercial. |
| MySQL 8.4 Community | 1 | 0 | 0 | Edición Community bajo licencia GPLv2, sin costo y sin límites de uso. |
| Lima | 1 | 0 | 0 | Proyecto de código abierto, licencia Apache 2.0. |
| Java/Tomcat | 1 | 0 | 0 | OpenJDK bajo GPL con excepción Classpath y Apache Tomcat bajo Apache 2.0; dependencias gratuitas del proyecto. |
| Git/GitHub Free | 1 | 0 | 0 | Git bajo GPLv2 y alojamiento en plan GitHub Free sujeto a sus límites. |
| MySQL Workbench Community | 1 | 0 | 0 | Herramienta gráfica oficial de MySQL, gratuita bajo licencia GPLv2. |
| Total software | — | — | 0 | Se conserva el uso académico dentro de los términos aplicables. |
Table: Herramientas y licenciamiento contemplados

La cantidad representa una referencia presupuestal por herramienta, no una restricción al número de instalaciones permitidas por su licencia. Graphviz, Pandoc y openpyxl son utilidades gratuitas de documentación y tampoco requieren compra de licencia.

# Servicios

| Concepto | Semanas | Valor semanal COP | Subtotal COP | Criterio |
|---|---:|---:|---:|---|
| Internet compartido del equipo | 14 | 30.000 | 420.000 | Imputación conjunta por conectividad, sincronización y demostración. |
| Energía del equipo y de la VM | 14 | 12.000 | 168.000 | Estimación agregada para los tres portátiles; no se multiplica otra vez por integrante. |
| Total servicios | — | — | 588.000 | Aportes del equipo, sin factura atribuida al proyecto. |
Table: Estimación de servicios durante el horizonte del proyecto

# Consolidado y reserva

| Rubro | Valor COP |
|---|---:|
| Talento humano | 9.121.000 |
| Hardware | 840.000 |
| Software | 0 |
| Servicios | 588.000 |
| Costo directo | 10.549.000 |
| Imprevistos, 10 % del costo directo | 1.054.900 |
| Total del proyecto | 11.603.900 |
Table: Consolidación económica de SIGEVET

El gran total es **11.603.900 COP**. La reserva equivale a $10.549.000 \times 0,10 = 1.054.900$ COP; se aplica una sola vez a la suma de costos directos, no sobre sí misma. Es una provisión para ajustes menores de horas o recursos, no un gasto ejecutado.

# Libro de cálculo y seguimiento

El archivo `entregables/Transversales/05_Presupuesto_SIGEVET.xlsx` contiene Resumen y Detalle. Detalle conserva cantidades y valores unitarios numéricos, y calcula cada subtotal como cantidad por valor unitario. El valor unitario de depreciación referencia el bloque de origen, residual, vida útil y semanas de uso. Resumen suma rubros, calcula el costo directo y la reserva, y obtiene el total con fórmulas reales.

Las horas salen de la misma lista de actividades del cronograma; un cambio de esfuerzo exige actualizar ambos libros y actualizar las tablas documentales y el acta de constitución. El gerente registra por paquete EDT cualquier uso propuesto de la reserva y su efecto en horas y costo, para revisión del equipo y validación del docente si afecta el alcance.
