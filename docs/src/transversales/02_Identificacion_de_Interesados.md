---
title: "Identificación de interesados"
subtitle: "Identificación de interesados"
fase: "Transversal"
version: "1.0"
---
<!-- Created: 18:15 19-Oct-2026 -->

# Propósito y criterios del registro

Se identifican quienes definen, construyen, evalúan o representan necesidades de información de SIGEVET. El registro orienta las revisiones de reglas de negocio y la aceptación académica. Huella Sana S.A.S. y sus actores son ficticios; sus expectativas se utilizan para validar el caso con el docente y no corresponden a entrevistas realizadas.

El poder es la autoridad para aprobar alcance o aceptar productos; la influencia es la capacidad de orientar decisiones técnicas u operativas. Se califica alta, media o baja. Para la matriz, el poder medio se agrupa con poder bajo porque no implica autoridad de aprobación. El interés refleja el impacto esperado de los resultados.

# Registro de interesados

El registro se presenta en dos tablas vinculadas por ID para conservar la legibilidad y la trazabilidad de cada expectativa y requisito.

| ID | Nombre o rol | Organización | Interés / poder / influencia |
|---|---|---|---|
| I01 | Ing., Esp., M.Ed. Christian Felipe Duarte Arévalo; docente / patrocinador | Universidad El Bosque | Alto / Alto / Alta |
| I02 | Juan David Polo Herrera (1117506677); Gerente del proyecto y DBA | Equipo SIGEVET | Alto / Alto / Alta |
| I03 | Sebastián Castellanos Parra (1031811214); Analista y modelador de datos | Equipo SIGEVET | Alto / Medio / Alta |
| I04 | Juan Miguel Ramos (1025528586); Desarrollador web e infraestructura | Equipo SIGEVET | Alto / Medio / Alta |
| I05 | Gerente general ficticio de Huella Sana | Huella Sana S.A.S. | Alto / Alto / Alta |
| I06 | Administradores de sede | Cinco sedes de Huella Sana | Alto / Medio / Media |
| I07 | Veterinarios | Huella Sana S.A.S. | Alto / Bajo / Media |
| I08 | Auxiliares veterinarios | Huella Sana S.A.S. | Alto / Bajo / Media |
| I09 | Personal de recepción y facturación | Huella Sana S.A.S. | Alto / Bajo / Media |
| I10 | Clientes, dueños de mascotas | Usuarios del caso Huella Sana | Alto / Bajo / Baja |
| I11 | Coordinación del programa de Ingeniería de Sistemas | Universidad El Bosque | Bajo / Alto / Media |
Table: Identidad y valoración de interesados

| ID | Expectativas | Requisitos verificables |
|---|---|---|
| I01 | Evidenciar dominio de diseño e implementación de bases de datos | Ocho entregables por fase, seis transversales, 55 ejercicios y video de máximo 15 minutos con los tres integrantes. |
| I02 | Mantener coherencia y cumplir hitos sin ampliar el alcance | Esquema de 22 tablas, restricciones nombradas, scripts reproducibles y trazabilidad de horas y costos. |
| I03 | Representar reglas y cardinalidades sin contradicciones | Chen, EER, Barker/Crow's Foot, normalización y diccionario sincronizados con el DDL. |
| I04 | Disponer de un entorno reproducible y consultas comprobables | VM Oracle Linux, MySQL 8.4, conexión de la aplicación Java con `vet_app` y resultados de 55 ejercicios. |
| I05 | Comparar ingresos y actividad entre las cinco sedes | Consultas por sede y servicio; integridad entre facturas, clientes y atención. |
| I06 | Consultar agenda, personal y existencias de su sede | Director por sede, asignación de empleados y consulta de inventario bajo mínimo. |
| I07 | Recuperar antecedentes y prescripciones completos | Historia única por mascota, consultas vinculadas a citas y medicamentos con dosis y frecuencia. |
| I08 | Conservar datos de apoyo a la atención | Asociación opcional del auxiliar a la consulta y registro de signos vitales. |
| I09 | Evitar duplicidad y facturas inconsistentes | Documento de cliente único, teléfonos múltiples y detalle con servicio o medicamento, nunca ambos. |
| I10 | Mantener información coherente de sus mascotas entre sedes | Mascota asociada a propietario e historia única; uso exclusivo de datos ficticios en la demostración. |
| I11 | Garantizar calidad y evidencia del proyecto formativo | Entregables con identificación institucional, roles y registro de aceptación académica. |
Table: Expectativas y requisitos vinculados por interesado

Los requisitos operativos de los actores de Huella Sana se modelan y se prueban mediante SQL. No amplían la aplicación a captura de datos ni a autenticación de usuarios finales, excluidas en la propuesta.

# Matriz de poder e interés

| Poder / interés | Interés alto | Interés bajo |
|---|---|---|
| Poder alto | Gestionar de cerca: I01, I02, I05 | Mantener satisfecho: I11 |
| Poder bajo o medio | Mantener informados: I03, I04, I06, I07, I08, I09, I10 | Monitorear: sin interesados identificados en este cuadrante |
Table: Matriz de clasificación dos por dos

El poder de I05 representa su función dentro del caso simulado; la aprobación académica corresponde a I01. El equipo no atribuye a I05 una autorización real sobre los recursos de la Universidad.

# Estrategia de participación

| ID | Estrategia y mecanismo | Frecuencia o punto de control | Responsable |
|---|---|---|---|
| I01 | Presentar paquetes verificables, resolver observaciones y registrar decisiones de alcance | Revisión semanal; aceptación en Semana 8, Semana 13 y Semana 14 | Juan David Polo Herrera |
| I02 | Consolidar avance, riesgos, horas y cambios; revisar restricciones y scripts | Cada semana, Semana 1 a Semana 14 | Juan David Polo Herrera |
| I03 | Revisar modelos contra supuestos y diccionario mediante cambios de Git | Cada semana de modelado; revisión final en Semana 8 | Juan David Polo Herrera |
| I04 | Revisar resultados de consultas, instalación y conexión de solo lectura | Semana 7 a Semana 13 | Juan David Polo Herrera |
| I05 | Validar escenarios simulados de indicadores y alcance con el docente | Semana 2 y Semana 13 | Sebastián Castellanos Parra |
| I06 | Revisar casos de dirección, agenda e inventario por sede | Semana 2, Semana 5 y Semana 13 | Sebastián Castellanos Parra |
| I07 | Contrastar historia clínica y prescripción con consultas de prueba | Semana 3, Semana 7 y Semana 8 | Sebastián Castellanos Parra |
| I08 | Revisar relación opcional de asistencia y datos de consulta | Semana 3 y Semana 8 | Sebastián Castellanos Parra |
| I09 | Probar unicidad, detalle de factura y consistencia de clientes | Semana 6, Semana 8 y Semana 12 | Juan David Polo Herrera |
| I10 | Revisar escenarios de propietario y mascota con datos ficticios | Semana 2 y Semana 8 | Sebastián Castellanos Parra |
| I11 | Conservar evidencia de productos y participación; atender solicitudes por el canal del docente | Semana 14 o cuando sea requerido | Juan David Polo Herrera |
Table: Estrategias de participación y seguimiento

El gerente actualiza la clasificación cuando cambian expectativas o capacidad de decisión. Cada observación se vincula al ID del interesado, al paquete EDT y a su resolución en Git; si afecta alcance, presupuesto o hitos, se presenta al docente antes de incorporarla.
