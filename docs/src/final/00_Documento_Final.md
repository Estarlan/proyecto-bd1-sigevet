---
title: "Documento Final del Proyecto"
subtitle: "Documento Final del Proyecto"
fase: "Entrega final"
version: "1.0"
---

<!-- Created: [unknown] -->
<!-- Updated: 18:55 24-Oct-2026 -->
<!-- Updated: 18:55 24-Oct-2026 -->
<!-- Updated: 18:55 24-Oct-2026 -->

# Resumen del proyecto

SIGEVET (Sistema Integral de Gestión Veterinaria) es la base de datos relacional de la Red de Clínicas Veterinarias
Huella Sana S.A.S., un caso simulado de cinco sedes. El proyecto recorre el ciclo completo que pide la asignatura:

- **Análisis y modelado (Fase 1):** propuesta, supuestos y reglas de negocio, modelo entidad-relación en notación
  Peter Chen y extendido, modelos Barker y Crow's Foot y modelo relacional editables en Draw.io, normalización hasta la
  Tercera Forma Normal, diccionario de datos y 55 ejercicios de álgebra relacional.
- **Implementación (Fase 2):** máquina virtual con Oracle Linux 9, MySQL 8.4 LTS administrado con MySQL Workbench,
  scripts de creación, inserción y verificación, y una aplicación web en Java (Servlets y JSP) que consulta la base
  con JDBC, sin ORM, usando un usuario de solo lectura.
- **Gestión:** acta de constitución, interesados, EDT, cronograma de 14 semanas (385 horas), presupuesto de
  11.603.900 COP y acta de cierre.

| Indicador | Resultado |
|---|---|
| Modelo conceptual | 18 entidades y 26 relaciones (5 de 1:1, 18 de 1:N y 3 de N:M) |
| Modelo físico | 22 tablas en 3FN y 113 restricciones con nombre |
| Programación en la base | 6 vistas, 2 funciones, 3 procedimientos y 9 disparadores |
| Datos de prueba | 1.091 registros coherentes entre tablas |
| Reglas de negocio | 10 de 10 pruebas rechazan los datos inválidos con el error esperado |
| Álgebra relacional | 55 de 55 consultas dan en MySQL el mismo resultado que en la Fase 1 |
| Aplicación web | Todas las páginas responden contra la base real; el usuario de la app no puede modificar datos |

Table: Resultados principales

Este documento reúne, en orden, todos los entregables. Cada capítulo conserva el contenido del documento entregado
por separado.


# Parte I. Fase 1: análisis y modelado


## Documento de Propuesta del Proyecto



### Introducción

Este documento presenta la propuesta del proyecto final de la asignatura Bases de Datos 1: **SIGEVET — Sistema
Integral de Gestión Veterinaria**, una base de datos relacional para la Red de Clínicas Veterinarias *Huella Sana
S.A.S.* (caso simulado). La propuesta describe la organización, la problemática, los objetivos, el alcance y los
requerimientos del sistema, y servirá como guía para el modelado (Fase 1) y la implementación (Fase 2).

### Descripción de la organización

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

### Planteamiento del problema

#### Situación actual

Cada sede administra su información de forma independiente: las agendas se llevan en hojas de cálculo, las
historias clínicas se guardan en carpetas físicas, el inventario de medicamentos se cuenta a mano y la facturación se
hace en un programa de escritorio distinto en cada sede. No existe una fuente única de información para la red.

#### Síntomas, causas y consecuencias

| Síntoma observado | Causa | Consecuencia |
|---|---|---|
| Citas duplicadas o cruzadas para un mismo veterinario | Agendas en archivos separados sin validación | Clientes inconformes y tiempos muertos |
| Historia clínica incompleta cuando la mascota va a otra sede | Historias en papel, una carpeta por sede | Riesgo clínico: alergias y tratamientos previos desconocidos |
| Medicamentos agotados o con sobrestock | No hay control de stock mínimo por sede | Ventas perdidas y capital inmovilizado |
| Datos de clientes repetidos y contradictorios | Cada sede registra al cliente de nuevo | Redundancia, anomalías de actualización |
| La gerencia no conoce ingresos por sede ni por servicio | Facturación sin integración | Decisiones sin datos confiables |

Table: Análisis de la problemática

#### Pregunta problema

¿Cómo diseñar e implementar una base de datos relacional centralizada, normalizada hasta la Tercera Forma Normal, que
integre la información clínica, operativa y comercial de las cinco sedes de *Huella Sana* y permita consultarla de
forma confiable desde una aplicación web?

### Justificación

Una base de datos relacional centralizada elimina la redundancia (un cliente y una mascota se registran una sola
vez), garantiza la integridad de los datos mediante llaves primarias, foráneas y restricciones, y permite responder
preguntas de negocio con consultas declarativas (álgebra relacional y SQL). Para la asignatura, el caso es apropiado
porque exige todos los conceptos del curso: modelo entidad-relación con más de ocho entidades y relaciones 1:1, 1:N y
N:M, especialización (modelo extendido), normalización hasta 3FN, consultas con uniones, selecciones y proyecciones,
y una implementación real en un RDBMS sobre una máquina virtual Linux con acceso desde una aplicación web.

### Objetivos

#### Objetivo general

Diseñar e implementar la base de datos relacional SIGEVET para la red *Huella Sana*, desde el modelo conceptual hasta
el modelo físico en MySQL sobre una máquina virtual Oracle Linux, con una aplicación web de consulta.

#### Objetivos específicos

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

### Alcance

#### El proyecto incluye

- Gestión de la estructura de la red: departamentos, ciudades, sedes y su director.
- Gestión del personal: empleados con especialización en veterinarios y auxiliares; especialidades certificadas.
- Gestión de clientes (con varios teléfonos), mascotas, especies y razas.
- Historia clínica única por mascota, compartida por todas las sedes.
- Agenda de citas por veterinario, sede y servicio; registro de la consulta atendida.
- Fórmula médica (prescripción de medicamentos por consulta).
- Inventario de medicamentos por sede con stock mínimo.
- Facturación de servicios y medicamentos con detalle por línea.
- Consultas de lectura desde una aplicación web de demostración.

#### El proyecto no incluye

- Facturación electrónica ante la DIAN, nómina, contabilidad ni pasarela de pagos.
- Compras a proveedores, lotes y fechas de vencimiento de medicamentos.
- Autenticación de usuarios finales en la aplicación web (la app es de solo lectura y de demostración).
- Aplicación móvil, alta disponibilidad o réplicas de la base de datos.

### Actores del sistema

| Actor | Rol frente a la información |
|---|---|
| Gerencia de la red | Consulta indicadores de ingresos, citas y ocupación por sede |
| Director de sede | Supervisa agenda, personal e inventario de su sede |
| Recepcionista / administrativo | Registra clientes, mascotas, citas y facturas |
| Veterinario | Atiende citas, registra consultas y formula medicamentos |
| Auxiliar veterinario | Asiste consultas y registra signos vitales |
| Cliente (propietario) | Fuente de datos; recibe atención y facturas |

Table: Actores del sistema

### Requerimientos funcionales

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

### Requerimientos no funcionales

| Código | Requerimiento |
|---|---|
| RNF-01 | El esquema debe estar normalizado hasta Tercera Forma Normal (3FN). |
| RNF-02 | Toda regla de integridad expresable de forma declarativa se implementa con PK, FK, UNIQUE, CHECK y NOT NULL. |
| RNF-03 | El RDBMS debe ser distinto de SQL Server y ejecutarse en una máquina virtual Linux/Unix. |
| RNF-04 | La aplicación web se conecta con un usuario de solo lectura (principio de mínimo privilegio). |
| RNF-05 | Las restricciones siguen una convención de nombres (`PK_`, `FK_`, `UQ_`, `CK_`) para facilitar el mantenimiento. |
| RNF-06 | Todo el código y la documentación se versionan en un repositorio GitHub con aportes de los tres integrantes. |

Table: Requerimientos no funcionales

### Visión preliminar del modelo de datos

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

### Metodología y plan de trabajo

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

### Equipo de trabajo

| Integrante | Rol principal | Responsabilidades |
|---|---|---|
| Juan David Polo Herrera | Gerente del proyecto y DBA | Planeación, modelo relacional, normalización, scripts y control de versiones |
| Sebastián Castellanos Parra | Analista y modelador de datos | Requerimientos, supuestos, MER Chen/EER, diccionario de datos |
| Juan Miguel Ramos | Desarrollador web e infraestructura | Álgebra relacional y SQL, máquina virtual, aplicación web |

Table: Roles del equipo

### Herramientas

| Necesidad | Herramienta |
|---|---|
| Modelado | MySQL Workbench; Graphviz para diagramas de documentación |
| Sistema operativo (VM) | Oracle Linux 9 |
| RDBMS | MySQL 8.4 LTS (Community), administrado con MySQL Workbench |
| Aplicación web | Java 21 + Servlets/JSP (Tomcat 10.1) + JDBC (MySQL Connector/J) |
| Control de versiones | Git y GitHub |
| Documentación | Markdown + Pandoc → Microsoft Word |

Table: Herramientas previstas

### Riesgos iniciales

| Riesgo | Probabilidad | Impacto | Respuesta |
|---|---|---|---|
| Recursos limitados del equipo para la VM | Media | Alto | MySQL consume poca memoria; VM de 6 GB |
| Cambios de alcance tardíos | Media | Medio | Supuestos aprobados por el docente al inicio |
| Modelo con errores de cardinalidad | Baja | Alto | Validación con 55 consultas sobre datos de prueba |
| Aportes desiguales en GitHub | Media | Medio | Reparto de tareas por integrante y revisión semanal |

Table: Riesgos iniciales

### Conclusión

SIGEVET resuelve una necesidad real de integración de información en una red de clínicas veterinarias y permite
aplicar de forma completa los contenidos de Bases de Datos 1. El modelo propuesto tiene 18 entidades, todos los tipos
de relación, una especialización y un esquema que exige normalización hasta 3FN, por lo que el equipo considera que
la propuesta es viable, suficientemente compleja y alineada con los objetivos de la asignatura.

## Documento de Supuestos



### Propósito

Este documento registra los supuestos, reglas de negocio y restricciones que el equipo asumió para modelar SIGEVET.
Cada supuesto explica **por qué** el modelo tiene una cardinalidad, una participación, un atributo o una restricción
determinada. Si un supuesto cambia, el documento indica qué elemento del modelo se vería afectado.

Convenciones: **S** = supuesto de negocio, **RN** = regla de negocio que se traduce en una restricción del modelo,
**SD** = supuesto de datos, **ST** = supuesto técnico, **R** = restricción del proyecto.

### Supuestos de la organización

| ID | Supuesto | Elemento del modelo afectado |
|---|---|---|
| S-01 | *Huella Sana* es una sola empresa con varias sedes; toda la información se centraliza en una única base de datos. | Una sola instancia de MySQL y una sola base de datos (`sigevet`) |
| S-02 | Cada sede está en una sola ciudad y una ciudad puede tener varias sedes. | UBICADA_EN 1:N |
| S-03 | Solo se registran ciudades y departamentos donde hay sedes o clientes. | DEPARTAMENTO y CIUDAD como catálogos |
| S-04 | Cada sede tiene exactamente un director, que es un empleado de la red. Un empleado dirige a lo sumo una sede. | DIRIGE 1:1, `SEDE.id_director` UNIQUE |
| S-05 | El director puede ser veterinario o administrativo. | La FK del director apunta a EMPLEADO y no a un subtipo |
| S-06 | Cada empleado trabaja en una única sede; si cambia de sede se actualiza el registro (no se guarda historial de traslados). | LABORA_EN 1:N |
| S-07 | Una sede nueva se registra antes que su director; por eso el director puede quedar temporalmente vacío. | `id_director` admite nulos; FK agregada con ALTER TABLE |

Table: Supuestos de la organización

### Supuestos del personal

| ID | Supuesto | Elemento del modelo afectado |
|---|---|---|
| S-08 | Todo el personal comparte datos comunes (documento, nombres, contacto, salario, sede). | Supertipo EMPLEADO |
| S-09 | Un empleado es veterinario, auxiliar o administrativo, pero nunca dos a la vez. | Especialización **disyunta** (`tipo_empleado`) |
| S-10 | El personal administrativo no requiere atributos propios, por eso no tiene subtipo. | Especialización **parcial** |
| S-11 | Un veterinario puede tener varias especialidades certificadas y una especialidad puede tenerla ningún, uno o varios veterinarios. | CERTIFICADO_EN N:M con `fecha_certificacion` |
| S-12 | La tarjeta profesional del veterinario es única en el país. | `UQ_VETERINARIO_TARJETA_PROFESIONAL` |
| S-13 | Un empleado inactivo se conserva (no se borra) para no perder la trazabilidad de citas y facturas. | `EMPLEADO.estado` ACTIVO/INACTIVO |

Table: Supuestos del personal

### Supuestos de clientes y mascotas

| ID | Supuesto | Elemento del modelo afectado |
|---|---|---|
| S-14 | Un cliente se identifica por tipo y número de documento; no puede registrarse dos veces. | UNIQUE (tipo_documento, num_documento) |
| S-15 | Un cliente puede tener varios teléfonos (móvil, fijo, trabajo) y debe tener al menos uno. | Atributo multivaluado → tabla TELEFONO_CLIENTE |
| S-16 | Un cliente puede registrarse sin mascotas (por ejemplo, para comprar productos). | PROPIETARIO_DE parcial del lado CLIENTE |
| S-17 | Toda mascota tiene exactamente un propietario registrado. Si se vende o regala, se cambia el propietario. | PROPIETARIO_DE 1:N, FK NOT NULL |
| S-18 | La especie de una mascota se determina por su raza; los cruces sin raza definida se registran como raza "Criollo" de su especie. | MASCOTA → RAZA → ESPECIE (evita dependencia transitiva) |
| S-19 | La fecha de nacimiento puede ser desconocida (mascotas rescatadas). | `MASCOTA.fecha_nacimiento` admite nulos |
| S-20 | La edad no se almacena: se calcula a partir de la fecha de nacimiento. | Atributo derivado `edad` |
| S-21 | El microchip es opcional, pero si existe es único. | `microchip` UNIQUE y nulo |
| S-22 | Las mascotas fallecidas no se eliminan: se cambia su estado para conservar la historia clínica. | `MASCOTA.estado` |

Table: Supuestos de clientes y mascotas

### Supuestos clínicos

| ID | Supuesto | Elemento del modelo afectado |
|---|---|---|
| S-23 | Cada mascota tiene una sola historia clínica, compartida por todas las sedes, que se abre al registrarla. | TIENE 1:1 con participación total en ambos lados |
| S-24 | Una cita es para una sola mascota, con un solo veterinario, en una sede y para un servicio. | CITA con cuatro FK obligatorias |
| S-25 | Un veterinario no puede tener dos citas a la misma fecha y hora. | UNIQUE (id_veterinario, fecha_hora) |
| S-26 | El veterinario de la cita debe pertenecer a la sede de la cita. Esta regla depende de dos tablas y se controla en la capa de aplicación o con un disparador (Fase 2). | Regla RN-08 |
| S-27 | Las urgencias también se registran como una cita (con el servicio "Consulta de urgencias"); así toda consulta proviene de una cita. | GENERA 1:1, CONSULTA total |
| S-28 | Solo las citas atendidas generan consulta; una cita genera a lo sumo una consulta. | `CONSULTA.id_cita` UNIQUE |
| S-29 | Una consulta puede ser asistida por un auxiliar o por ninguno. | ASISTE 1:N parcial, FK nula |
| S-30 | En una consulta se pueden formular varios medicamentos y un medicamento aparece en muchas consultas; cada fórmula tiene dosis, frecuencia, duración y cantidad. | PRESCRIBE N:M con atributos |
| S-31 | Un mismo medicamento no se formula dos veces en la misma consulta (se ajusta la dosis). | PK (id_consulta, id_medicamento) |

Table: Supuestos clínicos

### Supuestos comerciales

| ID | Supuesto | Elemento del modelo afectado |
|---|---|---|
| S-32 | Cada sede maneja su propio inventario de cada medicamento con un stock mínimo de reorden. | ALMACENA N:M (tabla INVENTARIO) |
| S-33 | Una sede puede no manejar algunos medicamentos. | ALMACENA parcial |
| S-34 | Una factura pertenece a un cliente, la emite un empleado en una sede y tiene un número consecutivo único. | FACTURA con 3 FK; `numero_factura` UNIQUE |
| S-35 | Cada línea de factura cobra **un servicio o un medicamento, nunca ambos**. | Restricción `CK_DETALLE_FACTURA_ITEM` (arco exclusivo) |
| S-36 | La línea de factura depende de su factura y se identifica por su número de línea dentro de ella. | DETALLE_FACTURA entidad débil, PK (id_factura, num_linea) |
| S-37 | Los precios cambian en el tiempo; por eso la línea guarda el precio aplicado el día de la venta. | `precio_unitario` histórico (no es redundancia) |
| S-38 | Los precios incluyen IVA; el total de la factura se calcula (no se almacena). | Atributos derivados `total` y `subtotal` |
| S-39 | Una factura anulada no se borra; cambia de estado. | `FACTURA.estado` |
| S-40 | La red usa cuatro medios de pago: efectivo, tarjeta débito, tarjeta crédito y transferencia. | CHECK sobre `metodo_pago` |

Table: Supuestos comerciales

### Reglas de negocio y su implementación

| ID | Regla de negocio | Implementación declarativa |
|---|---|---|
| RN-01 | Los códigos DANE tienen 2 dígitos (departamento) y 5 dígitos (municipio). | `CHECK (REGEXP_LIKE(...))` |
| RN-02 | El salario, los precios y las cantidades son positivos. | `CHECK (> 0)` |
| RN-03 | El stock actual y el stock mínimo no pueden ser negativos. | `CHECK (>= 0)` |
| RN-04 | La temperatura registrada está entre 30 °C y 45 °C. | `CK_CONSULTA_TEMPERATURA_C` |
| RN-05 | El descuento de una línea está entre 0 % y 100 %. | `CK_DETALLE_FACTURA_DESCUENTO_PCT` |
| RN-06 | Los estados válidos de cita son PROGRAMADA, ATENDIDA, CANCELADA y NO_ASISTIO. | `CK_CITA_ESTADO` |
| RN-07 | Al eliminar una factura se eliminan sus líneas; al eliminar una consulta, su fórmula. | `ON DELETE CASCADE` |
| RN-08 | El veterinario de la cita labora en la sede de la cita. | Disparador o validación en la aplicación (Fase 2) |
| RN-09 | Un subtipo (VETERINARIO/AUXILIAR) solo existe para un empleado con el `tipo_empleado` correspondiente. | Disparador de validación (Fase 2) |

Table: Reglas de negocio

### Supuestos de datos

| ID | Supuesto |
|---|---|
| SD-01 | Los identificadores (`id_*`) son llaves sustitutas numéricas generadas por el sistema; los códigos de negocio (documento, número de factura, código DANE, microchip) se protegen con UNIQUE. |
| SD-02 | Los nombres se guardan separados en `nombres` y `apellidos` (atributo compuesto) para poder ordenar y buscar por apellido. |
| SD-03 | Las fechas con hora (citas, consultas, facturas) y las fechas de registro usan el tipo `DATETIME` de MySQL; las fechas sin hora usan `DATE`. |
| SD-04 | Los valores monetarios están en pesos colombianos con dos decimales (`DECIMAL(10,2)` o `DECIMAL(12,2)`). |
| SD-05 | Los datos de prueba son ficticios y se generan de forma reproducible (semilla fija); no contienen datos personales reales. |

Table: Supuestos de datos

### Supuestos técnicos y restricciones del proyecto

| ID | Supuesto o restricción |
|---|---|
| ST-01 | El RDBMS es MySQL 8.4 LTS sobre Oracle Linux 9 en una máquina virtual, administrado con MySQL Workbench. |
| ST-02 | La edición Free limita el uso a 2 hilos de CPU, 2 GB de RAM y 12 GB de datos, suficiente para el volumen académico. |
| ST-03 | La aplicación web solo lee datos y se conecta con un usuario de solo lectura. |
| R-01 | El proyecto se desarrolla en 14 semanas por un equipo de 3 estudiantes. |
| R-02 | No se permite SQL Server; el sistema operativo debe ser Linux o Unix. |
| R-03 | Todo el trabajo se versiona en GitHub. |

Table: Supuestos técnicos y restricciones

### Aprobación

| Nombre | Documento | Rol | Firma |
|---|---|---|---|
| Juan David Polo Herrera | C.C. 1117506677 | Gerente del proyecto y DBA | |
| Sebastián Castellanos Parra | C.C. 1031811214 | Analista y modelador de datos | |
| Juan Miguel Ramos | C.C. 1025528586 | Desarrollador web e infraestructura | |
| Ing., Esp., M.Ed. Christian Felipe Duarte Arévalo | | Docente / patrocinador | |

Table: Aprobación de los supuestos

## Modelo Entidad Relación con notación Peter Chen



### Introducción

El Modelo Entidad-Relación (MER) propuesto por Peter Chen (1976) representa el nivel **conceptual** de la base de
datos: qué cosas del mundo real interesan a la organización (entidades), qué se sabe de ellas (atributos) y cómo se
asocian (relaciones), sin decisiones de implementación. Este documento presenta el MER de SIGEVET construido a partir de
la propuesta y del documento de supuestos.

### Notación utilizada

| Símbolo | Significado |
|---|---|
| Rectángulo | Entidad fuerte |
| Rectángulo doble | Entidad débil (DETALLE_FACTURA) |
| Rombo | Relación |
| Rombo doble | Relación identificadora de la entidad débil (CONTIENE) |
| Elipse | Atributo simple |
| Elipse con nombre subrayado | Atributo clave (identificador) |
| Elipse con nombre en cursiva y subrayado discontinuo | Llave parcial de la entidad débil (`num_linea`) |
| Elipse unida a otras elipses | Atributo compuesto (`documento`, `nombre_completo`) |
| Elipse doble | Atributo multivaluado (`telefono` de CLIENTE) |
| Elipse punteada | Atributo derivado (`edad`, `antiguedad`, `total`, `subtotal`) |
| Línea sencilla / línea doble | Participación parcial / participación total |
| 1, N, M junto a la entidad | Razón de cardinalidad de la relación |

Table: Convenciones de la notación Chen

La cardinalidad se lee "a través" de la relación: en DEPARTAMENTO —1— PERTENECE_A —N— CIUDAD, un departamento se
relaciona con N ciudades y cada ciudad con un solo departamento. Las llaves foráneas **no** aparecen como atributos: en
el nivel conceptual la asociación se expresa con la relación.

### Diagrama completo

La vista global muestra la estructura de las 18 entidades y 26 relaciones, sin atributos. Los atributos completos se presentan por entidad en los tres catálogos gráficos de este capítulo. Las fuentes editables se entregan en Draw.io y las figuras en PNG, SVG y PDF.

![MER de SIGEVET en notación Peter Chen (completo)](diagramas/png/04_Chen_Global_191116-24Oct2026.png){width=100%}

### Vistas por área

Cada área se presenta en filas independientes, una por relación. Una misma entidad puede repetirse como referencia para evitar cruces de líneas; cada repetición representa la misma entidad del modelo. Los atributos se consultan en los catálogos gráficos siguientes.

#### Área Organización

Agrupa la ubicación geográfica, las sedes y el personal. Contiene la relación 1:1 **DIRIGE** (cada sede tiene un
director) y la relación N:M **CERTIFICADO_EN** (veterinarios y especialidades, con la fecha de certificación como
atributo de la relación). Los subtipos VETERINARIO y AUXILIAR se asocian a EMPLEADO mediante relaciones 1:1 (ES_UN);
en el modelo extendido esto se representa con una especialización.

![MER Chen — Área Organización](diagramas/png/01_Chen_Organizacion_191116-24Oct2026.png){width=100%}

#### Área Clínica

Contiene clientes, mascotas, historias clínicas, servicios, citas y consultas. Se destacan la relación 1:1 **TIENE**
(una historia clínica por mascota, total en ambos lados), la relación 1:1 **GENERA** (una consulta por cita atendida),
el atributo multivaluado `telefono` y el atributo derivado `edad`.

![MER Chen — Área Clínica](diagramas/png/02_Chen_Clinica_191116-24Oct2026.png){width=100%}

#### Área Comercial

Contiene medicamentos y facturación. La relación N:M **PRESCRIBE** une consultas y medicamentos con los atributos de
la fórmula; la relación N:M **ALMACENA** une sedes y medicamentos con el stock. DETALLE_FACTURA es una **entidad
débil**: no tiene identificador propio y se identifica por la factura que la contiene más su número de línea.

![MER Chen — Área Comercial](diagramas/png/03_Chen_Comercial_191116-24Oct2026.png){width=100%}

### Catálogos gráficos de atributos

Cada entidad tiene un identificador, sus atributos y sus convenciones Chen. Las repeticiones de entidad entre hojas son referencias al mismo objeto conceptual.

![Catálogo Chen de atributos — Organizacion](diagramas/png/11_Atributos_Organizacion_191116-24Oct2026.png){width=100%}

![Catálogo Chen de atributos — Clinica](diagramas/png/12_Atributos_Clinica_191116-24Oct2026.png){width=100%}

![Catálogo Chen de atributos — Comercial](diagramas/png/13_Atributos_Comercial_191116-24Oct2026.png){width=100%}

### Catálogo de entidades

<!-- Created: 22:45 09-Oct-2026 -->
<!-- Generado por docs/tools/gen_fragmentos.py: no editar a mano -->

| Entidad | Tipo | Área | Identificador | Descripción |
|---|---|---|---|---|
| **DEPARTAMENTO** | Catálogo | Organización | id_departamento | Departamentos de Colombia donde la red tiene sedes o clientes (codificación DANE). |
| **CIUDAD** | Catálogo | Organización | id_ciudad | Municipios donde se ubican sedes y residen clientes. |
| **SEDE** | Fuerte | Organización | id_sede | Clínica física de la red Huella Sana. Cada sede tiene exactamente un director. |
| **EMPLEADO** | Supertipo | Organización | id_empleado | Supertipo de todo el personal. El discriminador tipo_empleado indica el subtipo (VET, AUX) o ADM para personal administrativo que no pertenece a ningún subtipo. |
| **VETERINARIO** | Subtipo de EMPLEADO | Organización | id_empleado | Subtipo de EMPLEADO: médico veterinario habilitado para atender citas y prescribir. |
| **AUXILIAR** | Subtipo de EMPLEADO | Organización | id_empleado | Subtipo de EMPLEADO: auxiliar veterinario que asiste las consultas. |
| **ESPECIALIDAD** | Catálogo | Organización | id_especialidad | Áreas de especialización médica veterinaria. |
| **CLIENTE** | Fuerte | Clínica | id_cliente | Persona propietaria de una o más mascotas; es quien paga los servicios. |
| **ESPECIE** | Catálogo | Clínica | id_especie | Especie animal atendida por la red. |
| **RAZA** | Catálogo | Clínica | id_raza | Raza de una especie. Separarla de MASCOTA elimina la dependencia transitiva raza → especie (3FN). |
| **MASCOTA** | Fuerte | Clínica | id_mascota | Paciente de la red. La edad es un atributo derivado de fecha_nacimiento (no se almacena). |
| **HISTORIA_CLINICA** | Fuerte | Clínica | id_historia | Historia clínica única de cada mascota (relación 1:1 TIENE), compartida por todas las sedes. |
| **SERVICIO** | Catálogo | Clínica | id_servicio | Catálogo de servicios ofrecidos con su precio vigente y duración estimada. |
| **CITA** | Fuerte | Clínica | id_cita | Reserva de atención de una mascota con un veterinario, en una sede, para un servicio. |
| **CONSULTA** | Fuerte | Clínica | id_consulta | Registro clínico de una cita atendida (relación 1:1 GENERA con CITA). |
| **MEDICAMENTO** | Catálogo | Comercial | id_medicamento | Catálogo de medicamentos e insumos veterinarios con su precio de venta vigente. |
| **FACTURA** | Fuerte | Comercial | id_factura | Documento de venta emitido a un cliente en una sede. El total es derivado (vista V_FACTURA_TOTAL). |
| **DETALLE_FACTURA** | Débil | Comercial | num_linea | Entidad débil de FACTURA: cada línea cobra un servicio O un medicamento (arco exclusivo). |

Table: Catálogo de entidades del MER


### Clasificación de atributos

<!-- Created: 22:45 09-Oct-2026 -->
<!-- Generado por docs/tools/gen_fragmentos.py: no editar a mano -->

| Entidad | Llave | Simples | Compuestos | Multivaluados | Derivados |
|---|---|---|---|---|---|
| DEPARTAMENTO | id_departamento | codigo_dane, nombre | — | — | — |
| CIUDAD | id_ciudad | codigo_dane, nombre | — | — | — |
| SEDE | id_sede | nombre, direccion, telefono, email, fecha_apertura | — | — | — |
| EMPLEADO | id_empleado | fecha_nacimiento, telefono, email, tipo_empleado, fecha_ingreso, salario, estado | documento (tipo_documento, num_documento), nombre_completo (nombres, apellidos) | — | antiguedad |
| VETERINARIO | id_empleado | tarjeta_profesional, universidad, anio_graduacion | — | — | — |
| AUXILIAR | id_empleado | area, certificacion, turno | — | — | — |
| ESPECIALIDAD | id_especialidad | nombre, descripcion | — | — | — |
| CLIENTE | id_cliente | email, direccion, fecha_registro | documento (tipo_documento, num_documento), nombre_completo (nombres, apellidos) | telefono | — |
| ESPECIE | id_especie | nombre | — | — | — |
| RAZA | id_raza | nombre, tamano | — | — | — |
| MASCOTA | id_mascota | nombre, fecha_nacimiento, sexo, color, microchip, esterilizado, estado, fecha_registro | — | — | edad |
| HISTORIA_CLINICA | id_historia | numero_historia, fecha_apertura, grupo_sanguineo, alergias, enfermedades_cronicas, observaciones | — | — | — |
| SERVICIO | id_servicio | codigo, nombre, categoria, precio, duracion_min | — | — | — |
| CITA | id_cita | fecha_hora, motivo, estado | — | — | — |
| CONSULTA | id_consulta | fecha_hora_inicio, peso_kg, temperatura_c, frecuencia_cardiaca, sintomas, diagnostico, observaciones | — | — | — |
| MEDICAMENTO | id_medicamento | codigo, nombre, principio_activo, presentacion, laboratorio, precio_venta, requiere_formula | — | — | — |
| FACTURA | id_factura | numero_factura, fecha_emision, metodo_pago, estado | — | — | total |
| DETALLE_FACTURA | num_linea *(parcial)* | cantidad, precio_unitario, descuento_pct | — | — | subtotal |

Table: Clasificación de atributos por entidad


### Catálogo de relaciones

<!-- Created: 22:45 09-Oct-2026 -->
<!-- Generado por docs/tools/gen_fragmentos.py: no editar a mano -->

| # | Relación | Entidades | Tipo | Participación | Atributos propios |
|---|---|---|---|---|---|
| 1 | **PERTENECE_A** | DEPARTAMENTO (1) — CIUDAD (N) | 1:N | DEPARTAMENTO: parcial; CIUDAD: total | — |
| 2 | **UBICADA_EN** | CIUDAD (1) — SEDE (N) | 1:N | CIUDAD: parcial; SEDE: total | — |
| 3 | **RESIDE_EN** | CIUDAD (1) — CLIENTE (N) | 1:N | CIUDAD: parcial; CLIENTE: total | — |
| 4 | **LABORA_EN** | SEDE (1) — EMPLEADO (N) | 1:N | SEDE: total; EMPLEADO: total | — |
| 5 | **DIRIGE** | EMPLEADO (1) — SEDE (1) | 1:1 | EMPLEADO: parcial; SEDE: total | — |
| 6 | **ES_VETERINARIO** | EMPLEADO (1) — VETERINARIO (1) | 1:1 | EMPLEADO: parcial; VETERINARIO: total | — |
| 7 | **ES_AUXILIAR** | EMPLEADO (1) — AUXILIAR (1) | 1:1 | EMPLEADO: parcial; AUXILIAR: total | — |
| 8 | **CERTIFICADO_EN** | VETERINARIO (M) — ESPECIALIDAD (N) | N:M | VETERINARIO: parcial; ESPECIALIDAD: parcial | fecha_certificacion |
| 9 | **CLASIFICA** | ESPECIE (1) — RAZA (N) | 1:N | ESPECIE: total; RAZA: total | — |
| 10 | **ES_DE_RAZA** | RAZA (1) — MASCOTA (N) | 1:N | RAZA: parcial; MASCOTA: total | — |
| 11 | **PROPIETARIO_DE** | CLIENTE (1) — MASCOTA (N) | 1:N | CLIENTE: parcial; MASCOTA: total | — |
| 12 | **TIENE** | MASCOTA (1) — HISTORIA_CLINICA (1) | 1:1 | MASCOTA: total; HISTORIA_CLINICA: total | — |
| 13 | **SOLICITA** | MASCOTA (1) — CITA (N) | 1:N | MASCOTA: parcial; CITA: total | — |
| 14 | **ATIENDE** | VETERINARIO (1) — CITA (N) | 1:N | VETERINARIO: parcial; CITA: total | — |
| 15 | **SE_PROGRAMA_EN** | SEDE (1) — CITA (N) | 1:N | SEDE: parcial; CITA: total | — |
| 16 | **CORRESPONDE_A** | SERVICIO (1) — CITA (N) | 1:N | SERVICIO: parcial; CITA: total | — |
| 17 | **GENERA** | CITA (1) — CONSULTA (1) | 1:1 | CITA: parcial; CONSULTA: total | — |
| 18 | **ASISTE** | AUXILIAR (1) — CONSULTA (N) | 1:N | AUXILIAR: parcial; CONSULTA: parcial | — |
| 19 | **PRESCRIBE** | CONSULTA (M) — MEDICAMENTO (N) | N:M | CONSULTA: parcial; MEDICAMENTO: parcial | dosis, frecuencia_horas, duracion_dias, cantidad, indicaciones |
| 20 | **ALMACENA** | SEDE (M) — MEDICAMENTO (N) | N:M | SEDE: parcial; MEDICAMENTO: parcial | stock_actual, stock_minimo, fecha_actualizacion |
| 21 | **FACTURADA_A** | CLIENTE (1) — FACTURA (N) | 1:N | CLIENTE: parcial; FACTURA: total | — |
| 22 | **EMITIDA_EN** | SEDE (1) — FACTURA (N) | 1:N | SEDE: parcial; FACTURA: total | — |
| 23 | **EMITE** | EMPLEADO (1) — FACTURA (N) | 1:N | EMPLEADO: parcial; FACTURA: total | — |
| 24 | **CONTIENE** (identificadora) | FACTURA (1) — DETALLE_FACTURA (N) | 1:N | FACTURA: total; DETALLE_FACTURA: total | — |
| 25 | **INCLUYE_SERVICIO** | SERVICIO (1) — DETALLE_FACTURA (N) | 1:N | SERVICIO: parcial; DETALLE_FACTURA: parcial | — |
| 26 | **INCLUYE_MEDICAMENTO** | MEDICAMENTO (1) — DETALLE_FACTURA (N) | 1:N | MEDICAMENTO: parcial; DETALLE_FACTURA: parcial | — |

Table: Catálogo de relaciones del modelo


### Justificación de cardinalidades relevantes

- **DIRIGE (1:1):** una sede tiene exactamente un director (participación total de SEDE) y un empleado dirige como
  máximo una sede (parcial de EMPLEADO: la mayoría no son directores). Supuestos S-04 y S-05.
- **TIENE (1:1):** la historia clínica es única por mascota y se comparte entre sedes; ninguna existe sin la otra
  (total en ambos lados). Supuesto S-23.
- **GENERA (1:1):** solo una cita atendida produce consulta (parcial de CITA) y toda consulta proviene de una cita
  (total de CONSULTA). Supuestos S-27 y S-28.
- **PROPIETARIO_DE (1:N):** un cliente puede tener varias mascotas o ninguna (parcial); toda mascota tiene un dueño
  (total). Supuestos S-16 y S-17.
- **PRESCRIBE (N:M):** una consulta puede formular varios medicamentos y un medicamento aparece en muchas consultas;
  ambas participaciones son parciales (hay consultas sin fórmula y medicamentos nunca formulados). Supuesto S-30.
- **CONTIENE (1:N identificadora):** una factura tiene una o más líneas y cada línea pertenece a una sola factura,
  de la que depende su existencia. Supuesto S-36.

### Verificación de los requisitos del enunciado

| Requisito | Resultado |
|---|---|
| Al menos 8 entidades | 18 entidades |
| Relaciones 1:1 | 5 (DIRIGE, TIENE, GENERA, ES_VETERINARIO, ES_AUXILIAR) |
| Relaciones 1:N | 18 |
| Relaciones N:M | 3 (CERTIFICADO_EN, PRESCRIBE, ALMACENA) |
| Tipos de atributo | Clave, simple, compuesto, multivaluado, derivado y llave parcial |
| Entidad débil | DETALLE_FACTURA con relación identificadora CONTIENE |

Table: Verificación del MER frente al enunciado

## Modelo Entidad Relación Extendido



### Introducción

El Modelo Entidad-Relación Extendido (EER) añade al MER de Chen conceptos para representar jerarquías de entidades
(superclases y subclases), restricciones de especialización y restricciones estructurales más precisas. En SIGEVET el
EER **sí aplica**, porque el personal comparte datos comunes pero los veterinarios y los auxiliares tienen atributos y
relaciones propias.

### Conceptos aplicados

| Concepto | Aplicación en SIGEVET |
|---|---|
| Superclase | EMPLEADO: datos comunes de todo el personal |
| Subclases | VETERINARIO (tarjeta profesional, universidad, año de graduación) y AUXILIAR (área, certificación, turno) |
| Herencia | Las subclases heredan todos los atributos y relaciones de EMPLEADO (por ejemplo LABORA_EN y EMITE) |
| Relaciones propias de subclase | Solo VETERINARIO participa en ATIENDE y CERTIFICADO_EN; solo AUXILIAR participa en ASISTE |
| Restricción de disyunción (d) | Un empleado pertenece a lo sumo a una subclase |
| Restricción de completitud parcial | Hay empleados (administrativos) que no pertenecen a ninguna subclase |
| Atributo discriminador | `tipo_empleado` con dominio {VET, AUX, ADM} |
| Restricciones estructurales (min,max) | Cada extremo de relación indica el mínimo y el máximo de participaciones |
| Restricción de exclusión | Una línea de factura cobra un servicio **o** un medicamento ({XOR}) |

Table: Conceptos del modelo extendido utilizados

### Especialización de EMPLEADO

![Especialización disyunta y parcial de EMPLEADO](diagramas/png/10_EER_Especializacion_EMPLEADO_191116-24Oct2026.png){width=90%}

**Lectura del diagrama.** La línea sencilla entre EMPLEADO y el círculo indica participación **parcial**: no todo
empleado es veterinario o auxiliar (el personal administrativo permanece solo en la superclase). La letra **d** dentro
del círculo indica que la especialización es **disyunta**: un empleado no puede ser veterinario y auxiliar a la vez.
El símbolo de subconjunto (⊂) en cada línea hacia una subclase indica que el conjunto de veterinarios y el de
auxiliares son subconjuntos del conjunto de empleados.

**Por qué es disyunta y parcial.** El supuesto S-09 establece que un empleado tiene un único rol clínico, y el
supuesto S-10 que el personal administrativo no tiene atributos propios que justifiquen una tercera subclase. La
especialización está **definida por atributo**: el valor de `tipo_empleado` determina la subclase.

### Restricciones estructurales (min,max)

En el EER se reemplaza la razón de cardinalidad 1/N por la notación (min,max) de Elmasri y Navathe, que se lee
"mirando aquí": el par escrito junto a una entidad indica en cuántas instancias de la relación participa cada
ocurrencia de esa entidad. Un mínimo 0 equivale a participación parcial y un mínimo 1 a participación total.

<!-- Created: 22:45 09-Oct-2026 -->
<!-- Generado por docs/tools/gen_fragmentos.py: no editar a mano -->

| # | Relación | Entidad A (min,max) | Entidad B (min,max) | Tipo |
|---|---|---|---|---|
| 1 | **PERTENECE_A** | DEPARTAMENTO (0,N) | CIUDAD (1,1) | 1:N |
| 2 | **UBICADA_EN** | CIUDAD (0,N) | SEDE (1,1) | 1:N |
| 3 | **RESIDE_EN** | CIUDAD (0,N) | CLIENTE (1,1) | 1:N |
| 4 | **LABORA_EN** | SEDE (1,N) | EMPLEADO (1,1) | 1:N |
| 5 | **DIRIGE** | EMPLEADO (0,1) | SEDE (1,1) | 1:1 |
| 6 | **ES_VETERINARIO** | EMPLEADO (0,1) | VETERINARIO (1,1) | 1:1 |
| 7 | **ES_AUXILIAR** | EMPLEADO (0,1) | AUXILIAR (1,1) | 1:1 |
| 8 | **CERTIFICADO_EN** | VETERINARIO (0,N) | ESPECIALIDAD (0,N) | N:M |
| 9 | **CLASIFICA** | ESPECIE (1,N) | RAZA (1,1) | 1:N |
| 10 | **ES_DE_RAZA** | RAZA (0,N) | MASCOTA (1,1) | 1:N |
| 11 | **PROPIETARIO_DE** | CLIENTE (0,N) | MASCOTA (1,1) | 1:N |
| 12 | **TIENE** | MASCOTA (1,1) | HISTORIA_CLINICA (1,1) | 1:1 |
| 13 | **SOLICITA** | MASCOTA (0,N) | CITA (1,1) | 1:N |
| 14 | **ATIENDE** | VETERINARIO (0,N) | CITA (1,1) | 1:N |
| 15 | **SE_PROGRAMA_EN** | SEDE (0,N) | CITA (1,1) | 1:N |
| 16 | **CORRESPONDE_A** | SERVICIO (0,N) | CITA (1,1) | 1:N |
| 17 | **GENERA** | CITA (0,1) | CONSULTA (1,1) | 1:1 |
| 18 | **ASISTE** | AUXILIAR (0,N) | CONSULTA (0,1) | 1:N |
| 19 | **PRESCRIBE** | CONSULTA (0,N) | MEDICAMENTO (0,N) | N:M |
| 20 | **ALMACENA** | SEDE (0,N) | MEDICAMENTO (0,N) | N:M |
| 21 | **FACTURADA_A** | CLIENTE (0,N) | FACTURA (1,1) | 1:N |
| 22 | **EMITIDA_EN** | SEDE (0,N) | FACTURA (1,1) | 1:N |
| 23 | **EMITE** | EMPLEADO (0,N) | FACTURA (1,1) | 1:N |
| 24 | **CONTIENE** (identificadora) | FACTURA (1,N) | DETALLE_FACTURA (1,1) | 1:N |
| 25 | **INCLUYE_SERVICIO** | SERVICIO (0,N) | DETALLE_FACTURA (0,1) | 1:N |
| 26 | **INCLUYE_MEDICAMENTO** | MEDICAMENTO (0,N) | DETALLE_FACTURA (0,1) | 1:N |

Table: Restricciones estructurales (min,max) del modelo


Ejemplos de lectura:

- LABORA_EN: SEDE (1,N) — cada sede tiene entre 1 y N empleados; EMPLEADO (1,1) — cada empleado labora en
  exactamente una sede.
- DIRIGE: EMPLEADO (0,1) — un empleado dirige ninguna o una sede; SEDE (1,1) — cada sede tiene exactamente un director.
- PRESCRIBE: CONSULTA (0,N) y MEDICAMENTO (0,N) — puede haber consultas sin fórmula y medicamentos nunca formulados.

### Restricción de exclusión en el detalle de factura

Cada línea de DETALLE_FACTURA cobra exactamente un ítem, que puede ser un SERVICIO o un MEDICAMENTO. Las relaciones
INCLUYE_SERVICIO e INCLUYE_MEDICAMENTO son, por tanto, **mutuamente excluyentes** y una de las dos debe existir. En el
diagrama se anota con una nota {XOR}. En el modelo relacional se implementa con dos llaves foráneas que admiten nulos
y la restricción `CK_DETALLE_FACTURA_ITEM`:

```sql
CHECK ((id_servicio IS NOT NULL AND id_medicamento IS NULL)
    OR (id_servicio IS NULL AND id_medicamento IS NOT NULL))
```

Se evaluó modelar una categoría (tipo unión) ITEM_FACTURABLE = SERVICIO ∪ MEDICAMENTO. Se descartó porque obliga a
crear una tabla adicional con llave sustituta, mientras que el arco exclusivo cumple la misma regla con menos tablas y
consultas más simples.

### Diagrama EER completo

La vista EER muestra relaciones y especialización; los atributos se consultan en los catálogos del documento de Chen. Las entidades que se repiten entre filas son referencias a la misma entidad.

![MER Extendido de SIGEVET con notación (min,max) y especialización](diagramas/png/14_EER_Completo_191116-24Oct2026.png){width=100%}

### Transformación de la especialización al modelo relacional

Elmasri y Navathe proponen cuatro opciones para llevar una especialización a tablas:

| Opción | Descripción | Ventaja | Desventaja |
|---|---|---|---|
| 8A | Una tabla para la superclase y una por subclase (la PK de la subclase es FK a la superclase) | Sin nulos, funciona para cualquier tipo de especialización | Requiere JOIN para ver datos completos |
| 8B | Solo tablas para las subclases, con los atributos heredados | Sin JOIN | Solo sirve si la especialización es total; repite columnas |
| 8C | Una sola tabla con un atributo discriminador | Sin JOIN | Muchas columnas nulas; solo para disyuntas |
| 8D | Una sola tabla con banderas booleanas por subclase | Admite solapamiento | Muchas columnas nulas |

Table: Opciones de transformación de una especialización

**Opción elegida: 8A.** Como la especialización es parcial, la opción 8B no es posible: los administrativos se
perderían. Las opciones 8C y 8D dejarían nulas las columnas de veterinario en los auxiliares y viceversa, y además
VETERINARIO y AUXILIAR tienen relaciones propias (ATIENDE, CERTIFICADO_EN, ASISTE) cuyas llaves foráneas deben apuntar
exactamente a un veterinario o a un auxiliar. El resultado es:

- **EMPLEADO**([id_empleado]{.underline}, …, tipo_empleado, …, *id_sede*)
- **VETERINARIO**(*[id_empleado]{.underline}*, tarjeta_profesional, universidad, anio_graduacion)
- **AUXILIAR**(*[id_empleado]{.underline}*, area, certificacion, turno)

Se conserva además el discriminador `tipo_empleado` en EMPLEADO para identificar a los administrativos sin consultar
las subtablas. La coherencia entre el discriminador y la subtabla (regla RN-09) se garantizará con un disparador en
la Fase 2.

## Modelo Entidad Relación con notación Barker / Crow's Foot



### Introducción

Este documento presenta los modelos lógicos **Barker** y **Crow's Foot**, elaborados en **Draw.io** a partir del esquema de 22 tablas y 30 llaves foráneas. Las hojas lógicas omiten los tipos SQL; la vista física del documento 6 los incluye. Las relaciones N:M originan VETERINARIO_ESPECIALIDAD, PRESCRIPCION e INVENTARIO; el teléfono multivaluado origina TELEFONO_CLIENTE. Los archivos históricos de Workbench se conservan como respaldo; la versión vigente es la de Draw.io.

### Notación Barker

| Elemento | Representación |
|---|---|
| Entidad | Caja de esquinas redondeadas con el nombre en la parte superior |
| `#` | Atributo que forma parte del identificador único (UID) |
| `*` | Atributo obligatorio (NOT NULL) |
| `o` | Atributo opcional |
| `(FK)` | Atributo que viene de otra entidad por una relación |
| Línea continua (mitad de la relación) | La entidad de ese extremo **debe** participar ("must be") |
| Línea discontinua (mitad de la relación) | La entidad de ese extremo **puede** participar ("may be") |
| Pata de gallina en un extremo | Ese extremo es "uno o más" (muchos) |
| Extremo sin pata de gallina | Ese extremo es "uno y solo uno" |
| `#` y `[FK]` en un atributo | La FK forma parte del identificador; el UID se indica explícitamente en los atributos |
| Relación 1:1 identificadora con EMPLEADO | Subtipos VETERINARIO y AUXILIAR (especialización llevada a tablas) |

Table: Convenciones de la notación Barker

Cada relación se lee en ambos sentidos. CIUDAD debe pertenecer a un DEPARTAMENTO (mitad junto a CIUDAD continua; extremo DEPARTAMENTO sin pata). Un DEPARTAMENTO puede tener ciudades (mitad junto a DEPARTAMENTO discontinua; extremo CIUDAD con pata). La obligatoriedad de cada mitad corresponde a la entidad junto a esa mitad.

![Modelo de SIGEVET en notación Barker (Draw.io)](diagramas/png/15_Barker_Global_191116-24Oct2026.png){width=100%}

#### Observaciones del modelo Barker

- **Subtipos.** VETERINARIO y AUXILIAR aparecen como entidades unidas a EMPLEADO por relaciones 1:1
  identificadoras, que representa la especialización llevada a tablas (opción 8A del documento de
  modelo extendido). Las relaciones ATIENDE (con CITA), CERTIFICADO_EN y ASISTE se conectan al subtipo.
- **Relaciones identificadoras.** DETALLE_FACTURA (entidad débil) y las entidades de intersección marcan con `#` y `[FK]`
  las columnas del UID heredadas de la entidad padre.
- **Relaciones 1:1.** DIRIGE, TIENE y GENERA no tienen pata de gallina en ningún extremo.
- **Relación circular.** SEDE y EMPLEADO se relacionan dos veces: LABORA_EN (1:N) y DIRIGE (1:1).

### Notación Crow's Foot (pata de gallina)

| Símbolo en el extremo | Significado |
|---|---|
| Dos barras verticales | Uno y solo uno (mínimo 1, máximo 1) |
| Círculo y barra | Cero o uno |
| Barra y pata de gallina | Uno o muchos |
| Círculo y pata de gallina | Cero o muchos |

Table: Convenciones de la notación Crow's Foot

El símbolo más cercano a la entidad indica el **máximo** y el más alejado el **mínimo**. Por ejemplo, entre CLIENTE y
MASCOTA el extremo de CLIENTE tiene dos barras (toda mascota tiene exactamente un propietario) y el extremo de MASCOTA es `o<`
(un cliente puede tener cero o muchas mascotas).

![MER de SIGEVET en notación Crow's Foot (Draw.io)](diagramas/png/19_CrowsFoot_Global_191116-24Oct2026.png){width=100%}

Las marcas #, * y o indican identificador, atributo obligatorio y atributo opcional; PK/FK y UQn identifican llaves y grupos UNIQUE. Las líneas identificadoras son continuas y las demás, discontinuas. Las cardinalidades se derivan del DDL: el padre puede tener cero hijos y UNIQUE limita el máximo a uno. Las participaciones mínimas conceptuales requieren reglas adicionales.

### Entidades del modelo lógico

| Entidad lógica | Origen en el MER |
|---|---|
| DEPARTAMENTO, CIUDAD, SEDE, EMPLEADO, ESPECIALIDAD, CLIENTE, ESPECIE, RAZA, MASCOTA, HISTORIA_CLINICA, SERVICIO, CITA, CONSULTA, MEDICAMENTO, FACTURA | Entidades fuertes |
| VETERINARIO, AUXILIAR | Subtipos de EMPLEADO |
| DETALLE_FACTURA | Entidad débil de FACTURA |
| VETERINARIO_ESPECIALIDAD | Relación N:M CERTIFICADO_EN con atributo `fecha_certificacion` |
| PRESCRIPCION | Relación N:M PRESCRIBE con los atributos de la fórmula |
| INVENTARIO | Relación N:M ALMACENA con los atributos de stock |
| TELEFONO_CLIENTE | Atributo multivaluado `telefono` de CLIENTE |

Table: Correspondencia entre el MER y el modelo lógico

### Construcción y archivos editables en Draw.io

Se leyó el DDL sin modificarlo, se ubicaron las tablas por área y se dibujaron las relaciones desde nulabilidad, PK, UNIQUE y FK. Barker utiliza dos mitades con obligatoriedad independiente y pata sin barras; Crow's Foot utiliza símbolos de mínimo y máximo. Las referencias externas de cada área omiten atributos.

| Hojas Draw.io | Contenido |
|---|---|
| 15–18 | Barker global y áreas de organización, clínica y comercial |
| 19–22 | Crow's Foot global y las mismas áreas |
| 23–26 | Modelo relacional con tipos MySQL |

Las fuentes `.drawio` y sus exportaciones PNG, SVG y PDF son la entrega vigente; los `.mwb` y sus exportaciones anteriores quedan como material histórico de respaldo.

## Modelo Relacional con notación Crow's Foot y Normalización (3FN)



### Introducción

El modelo relacional traduce el MER a **relaciones (tablas)** con sus atributos, llaves primarias, llaves foráneas y
restricciones. Este documento presenta las reglas de transformación aplicadas, el esquema resultante de 22 tablas, el
diagrama en notación Crow's Foot y el proceso de normalización que demuestra que el esquema está en Tercera Forma
Normal (3FN).

### Transformación del MER al modelo relacional

Se aplicó el algoritmo de transformación de Elmasri y Navathe:

| Paso | Regla | Aplicación en SIGEVET |
|---|---|---|
| 1 | Cada entidad fuerte es una tabla con sus atributos simples; el identificador es la PK | 15 tablas (DEPARTAMENTO, CIUDAD, SEDE, EMPLEADO, …, FACTURA) |
| 2 | La entidad débil es una tabla cuya PK es la PK del propietario más la llave parcial | DETALLE_FACTURA(id_factura, num_linea) |
| 3 | Relación 1:1: FK con UNIQUE en el lado de participación total | SEDE.id_director (DIRIGE), HISTORIA_CLINICA.id_mascota (TIENE), CONSULTA.id_cita (GENERA) |
| 4 | Relación 1:N: FK en la tabla del lado N | 18 relaciones, por ejemplo MASCOTA.id_cliente (PROPIETARIO_DE) |
| 5 | Relación N:M: nueva tabla con las dos PK como PK compuesta y los atributos de la relación | VETERINARIO_ESPECIALIDAD, PRESCRIPCION, INVENTARIO |
| 6 | Atributo multivaluado: nueva tabla con la PK de la entidad y el valor | TELEFONO_CLIENTE(id_cliente, telefono) |
| 7 | Atributo compuesto: se guardan sus componentes | nombres, apellidos, tipo_documento, num_documento |
| 8 | Especialización: opción 8A (tabla por superclase y por subclase) | VETERINARIO y AUXILIAR con PK = FK a EMPLEADO |
| — | Atributos derivados: no se almacenan | edad, antigüedad, subtotal y total se calculan en consultas o vistas |

Table: Reglas de transformación aplicadas

#### Ejemplos gráficos de transformación

![CLIENTE y teléfonos multivaluados](diagramas/png/05_Transformacion_Multivaluados_CLIENTE_191116-24Oct2026.png){width=100%}

![Relación N:M PRESCRIBE](diagramas/png/06_Transformacion_NM_PRESCRIPCION_191116-24Oct2026.png){width=100%}

![Relación N:M ALMACENA](diagramas/png/07_Transformacion_NM_INVENTARIO_191116-24Oct2026.png){width=100%}

![Relación N:M CERTIFICADO_EN](diagramas/png/08_Transformacion_NM_ESPECIALIDAD_191116-24Oct2026.png){width=100%}

![Entidad débil DETALLE_FACTURA y llave parcial](diagramas/png/09_Entidad_Debil_DETALLE_FACTURA_191116-24Oct2026.png){width=100%}

**Relación circular SEDE–EMPLEADO.** EMPLEADO referencia a SEDE (LABORA_EN) y SEDE referencia a EMPLEADO (DIRIGE).
Para poder crear ambas tablas, la FK `FK_SEDE_DIRECTOR` se agrega con `ALTER TABLE` cuando EMPLEADO ya existe, y
`id_director` admite nulos para registrar la sede antes que su director (supuesto S-07).

### Esquema relacional

Notación: **TABLA**(llave primaria subrayada, columnas, *llaves foráneas en cursiva*).

<!-- Created: 22:45 09-Oct-2026 -->
<!-- Generado por docs/tools/gen_fragmentos.py: no editar a mano -->

- **DEPARTAMENTO** ([id_departamento]{.underline}, codigo_dane, nombre)
- **CIUDAD** ([id_ciudad]{.underline}, codigo_dane, nombre, *id_departamento*)
- **SEDE** ([id_sede]{.underline}, nombre, direccion, telefono, email, fecha_apertura, *id_ciudad*, *id_director*)
- **EMPLEADO** ([id_empleado]{.underline}, tipo_documento, num_documento, nombres, apellidos, fecha_nacimiento, telefono, email, tipo_empleado, fecha_ingreso, salario, estado, *id_sede*)
- **VETERINARIO** (*[id_empleado]{.underline}*, tarjeta_profesional, universidad, anio_graduacion)
- **AUXILIAR** (*[id_empleado]{.underline}*, area, certificacion, turno)
- **ESPECIALIDAD** ([id_especialidad]{.underline}, nombre, descripcion)
- **VETERINARIO_ESPECIALIDAD** (*[id_empleado]{.underline}*, *[id_especialidad]{.underline}*, fecha_certificacion)
- **CLIENTE** ([id_cliente]{.underline}, tipo_documento, num_documento, nombres, apellidos, email, direccion, *id_ciudad*, fecha_registro)
- **TELEFONO_CLIENTE** (*[id_cliente]{.underline}*, [telefono]{.underline}, tipo)
- **ESPECIE** ([id_especie]{.underline}, nombre)
- **RAZA** ([id_raza]{.underline}, nombre, tamano, *id_especie*)
- **MASCOTA** ([id_mascota]{.underline}, nombre, fecha_nacimiento, sexo, color, microchip, esterilizado, estado, *id_raza*, *id_cliente*, fecha_registro)
- **HISTORIA_CLINICA** ([id_historia]{.underline}, numero_historia, *id_mascota*, fecha_apertura, grupo_sanguineo, alergias, enfermedades_cronicas, observaciones)
- **SERVICIO** ([id_servicio]{.underline}, codigo, nombre, categoria, precio, duracion_min)
- **CITA** ([id_cita]{.underline}, fecha_hora, motivo, estado, *id_mascota*, *id_veterinario*, *id_sede*, *id_servicio*)
- **CONSULTA** ([id_consulta]{.underline}, *id_cita*, *id_auxiliar*, fecha_hora_inicio, peso_kg, temperatura_c, frecuencia_cardiaca, sintomas, diagnostico, observaciones)
- **MEDICAMENTO** ([id_medicamento]{.underline}, codigo, nombre, principio_activo, presentacion, laboratorio, precio_venta, requiere_formula)
- **PRESCRIPCION** (*[id_consulta]{.underline}*, *[id_medicamento]{.underline}*, dosis, frecuencia_horas, duracion_dias, cantidad, indicaciones)
- **INVENTARIO** (*[id_sede]{.underline}*, *[id_medicamento]{.underline}*, stock_actual, stock_minimo, fecha_actualizacion)
- **FACTURA** ([id_factura]{.underline}, numero_factura, fecha_emision, metodo_pago, estado, *id_cliente*, *id_sede*, *id_empleado*)
- **DETALLE_FACTURA** (*[id_factura]{.underline}*, [num_linea]{.underline}, *id_servicio*, *id_medicamento*, cantidad, precio_unitario, descuento_pct)


### Diagrama relacional (Draw.io)

El diagrama reproduce las 22 tablas, los tipos MySQL y las 30 llaves foráneas del DDL. # identifica la PK; * indica NOT NULL y o admite nulos; FK y UQn muestran llaves foráneas y grupos UNIQUE. Las relaciones identificadoras son continuas; las demás, discontinuas. La cardinalidad del padre es 1 u 0..1 según nulabilidad; la inversa es 0..N u 0..1 según UNIQUE. El DDL no impone por sí solo todas las participaciones mínimas del modelo conceptual.

![Modelo relacional completo de SIGEVET — Draw.io](diagramas/png/23_Relacional_Global_191116-24Oct2026.png){width=100%}

#### Vistas por área

Cada vista incluye las entidades de su área y referencias externas sin atributos.

![Modelo relacional — Organizacion](diagramas/png/24_Relacional_Organizacion_191116-24Oct2026.png){width=100%}

![Modelo relacional — Clinica](diagramas/png/25_Relacional_Clinica_191116-24Oct2026.png){width=100%}

![Modelo relacional — Comercial](diagramas/png/26_Relacional_Comercial_191116-24Oct2026.png){width=100%}

Los `.drawio` y sus exportaciones son la versión vigente; el modelo `SIGEVET.mwb` se conserva como respaldo histórico.

### Integridad referencial

<!-- Created: 22:45 09-Oct-2026 -->
<!-- Generado por docs/tools/gen_fragmentos.py: no editar a mano -->

| Tabla hija | Columna(s) | Tabla padre | Relación del MER | ON DELETE |
|---|---|---|---|---|
| CIUDAD | id_departamento | DEPARTAMENTO(id_departamento) | PERTENECE_A | NO ACTION |
| SEDE | id_ciudad | CIUDAD(id_ciudad) | UBICADA_EN | NO ACTION |
| SEDE | id_director | EMPLEADO(id_empleado) | DIRIGE | NO ACTION |
| EMPLEADO | id_sede | SEDE(id_sede) | LABORA_EN | NO ACTION |
| VETERINARIO | id_empleado | EMPLEADO(id_empleado) | ES_UN | CASCADE |
| AUXILIAR | id_empleado | EMPLEADO(id_empleado) | ES_UN | CASCADE |
| VETERINARIO_ESPECIALIDAD | id_empleado | VETERINARIO(id_empleado) | CERTIFICADO_EN | CASCADE |
| VETERINARIO_ESPECIALIDAD | id_especialidad | ESPECIALIDAD(id_especialidad) | CERTIFICADO_EN | NO ACTION |
| CLIENTE | id_ciudad | CIUDAD(id_ciudad) | RESIDE_EN | NO ACTION |
| TELEFONO_CLIENTE | id_cliente | CLIENTE(id_cliente) | TIENE_TELEFONO | CASCADE |
| RAZA | id_especie | ESPECIE(id_especie) | CLASIFICA | NO ACTION |
| MASCOTA | id_raza | RAZA(id_raza) | ES_DE_RAZA | NO ACTION |
| MASCOTA | id_cliente | CLIENTE(id_cliente) | PROPIETARIO_DE | NO ACTION |
| HISTORIA_CLINICA | id_mascota | MASCOTA(id_mascota) | TIENE | CASCADE |
| CITA | id_mascota | MASCOTA(id_mascota) | SOLICITA | NO ACTION |
| CITA | id_veterinario | VETERINARIO(id_empleado) | ATIENDE | NO ACTION |
| CITA | id_sede | SEDE(id_sede) | SE_PROGRAMA_EN | NO ACTION |
| CITA | id_servicio | SERVICIO(id_servicio) | CORRESPONDE_A | NO ACTION |
| CONSULTA | id_cita | CITA(id_cita) | GENERA | NO ACTION |
| CONSULTA | id_auxiliar | AUXILIAR(id_empleado) | ASISTE | NO ACTION |
| PRESCRIPCION | id_consulta | CONSULTA(id_consulta) | PRESCRIBE | CASCADE |
| PRESCRIPCION | id_medicamento | MEDICAMENTO(id_medicamento) | PRESCRIBE | NO ACTION |
| INVENTARIO | id_sede | SEDE(id_sede) | ALMACENA | NO ACTION |
| INVENTARIO | id_medicamento | MEDICAMENTO(id_medicamento) | ALMACENA | NO ACTION |
| FACTURA | id_cliente | CLIENTE(id_cliente) | FACTURADA_A | NO ACTION |
| FACTURA | id_sede | SEDE(id_sede) | EMITIDA_EN | NO ACTION |
| FACTURA | id_empleado | EMPLEADO(id_empleado) | EMITE | NO ACTION |
| DETALLE_FACTURA | id_factura | FACTURA(id_factura) | CONTIENE | CASCADE |
| DETALLE_FACTURA | id_servicio | SERVICIO(id_servicio) | INCLUYE_SERVICIO | NO ACTION |
| DETALLE_FACTURA | id_medicamento | MEDICAMENTO(id_medicamento) | INCLUYE_MEDICAMENTO | NO ACTION |

Table: Llaves foráneas del modelo relacional


<!-- Created: 22:45 09-Oct-2026 -->
<!-- Generado por docs/tools/gen_fragmentos.py: no editar a mano -->

| Tipo de restricción | Prefijo | Cantidad |
|---|---|---|
| Llave primaria | PK_ | 22 |
| Llave foránea | FK_ | 30 |
| Única | UQ_ | 25 |
| Verificación (CHECK) | CK_ | 36 |
| NOT NULL (columnas obligatorias de 138) | — | 118 |

Table: Resumen de restricciones de integridad


### Normalización

![Proceso de normalización de 0FN a 3FN](diagramas/png/27_Normalizacion_191116-24Oct2026.png){width=100%}

#### Conceptos

- **Dependencia funcional (DF):** $X \rightarrow Y$ si cada valor de X determina un único valor de Y.
- **1FN:** todos los atributos son atómicos; no hay grupos repetitivos ni atributos multivaluados.
- **2FN:** está en 1FN y ningún atributo no primo depende de **parte** de una llave candidata (sin dependencias parciales).
- **3FN:** está en 2FN y ningún atributo no primo depende **transitivamente** de una llave candidata
  ($X \rightarrow Y \rightarrow Z$ con Y no llave).

#### Punto de partida: el formato actual de "Orden de atención y factura"

Hoy cada sede registra en un solo formato la atención y el cobro. Llevado a una tabla sin normalizar (0FN), un
registro contiene grupos repetitivos (varias líneas cobradas) y un atributo multivaluado (varios teléfonos):

| num_factura | fecha | sede / ciudad / depto | cliente (doc, nombre, ciudad, depto) | teléfonos | mascota / raza / especie | veterinario | ítems (código, nombre, cant., precio) |
|---|---|---|---|---|---|---|---|
| FV-000041 | 2026-03-12 | Usaquén / Bogotá D.C. / Bogotá D.C. | CC 52987654, Andrés Martínez, Bogotá D.C., Bogotá D.C. | 3157894561, 6017451230 | Toby / Labrador / Canino | Laura Gómez | (SRV-CON01, Consulta general, 1, 65000); (MED-0001, Amoxivet, 14, 1800) |
| FV-000042 | 2026-03-12 | Chía / Chía / Cundinamarca | CC 1032456789, Paula Rojas, Chía, Cundinamarca | 3204567812 | Luna / Siamés / Felino | Diego Díaz | (SRV-VAC02, Vacuna triple felina, 1, 70000) |

Table: Formato de orden de atención y factura sin normalizar (0FN)

**Anomalías:** si cambia el teléfono de un cliente hay que cambiarlo en todas sus facturas (actualización); no se
puede registrar un medicamento que aún no se ha vendido (inserción); al borrar la única factura de un cliente se pierde
el cliente (eliminación).

#### Primera Forma Normal (1FN)

Se eliminan el grupo repetitivo y el atributo multivaluado: cada línea cobrada es una fila identificada por
(num_factura, num_linea) y los teléfonos pasan a una relación aparte.

- **ORDEN_1FN**([num_factura, num_linea]{.underline}, fecha, sede, ciudad_sede, depto_sede, doc_cliente,
  nombre_cliente, ciudad_cliente, depto_cliente, mascota, raza, especie, veterinario, codigo_item, nombre_item,
  cantidad, precio_unitario)
- **TELEFONO_1FN**([doc_cliente, telefono]{.underline})

Dependencias funcionales identificadas:

| # | Dependencia funcional | Tipo respecto a la PK (num_factura, num_linea) |
|---|---|---|
| DF1 | num_factura, num_linea → codigo_item, cantidad, precio_unitario | Completa |
| DF2 | num_factura → fecha, sede, doc_cliente, mascota, veterinario | **Parcial** (depende solo de num_factura) |
| DF3 | codigo_item → nombre_item, precio_vigente | **Parcial** a través de un atributo no primo |
| DF4 | sede → ciudad_sede → depto_sede | **Transitiva** |
| DF5 | doc_cliente → nombre_cliente, ciudad_cliente | **Transitiva** |
| DF6 | ciudad → departamento | **Transitiva** |
| DF7 | mascota → raza → especie | **Transitiva** |
| DF8 | mascota → doc_cliente (dueño) | **Transitiva** |

Table: Dependencias funcionales del formato en 1FN

#### Segunda Forma Normal (2FN)

Se eliminan las dependencias parciales (DF2 y DF3) separando lo que depende solo de la factura y lo que depende solo
del ítem:

- **FACTURA_2FN**([num_factura]{.underline}, fecha, sede, ciudad_sede, depto_sede, doc_cliente, nombre_cliente,
  ciudad_cliente, depto_cliente, mascota, raza, especie, veterinario)
- **DETALLE_2FN**([num_factura, num_linea]{.underline}, *codigo_item*, cantidad, precio_unitario)
- **ITEM_2FN**([codigo_item]{.underline}, nombre_item, precio_vigente)

El `precio_unitario` permanece en el detalle porque es el precio **histórico** del día de la venta, distinto del
precio vigente del catálogo (supuesto S-37); por eso no es redundante.

#### Tercera Forma Normal (3FN)

Se eliminan las dependencias transitivas (DF4 a DF8), creando una relación por cada determinante:

| Dependencia transitiva | Relación creada |
|---|---|
| sede → ciudad → departamento | SEDE(id_sede, …, *id_ciudad*), CIUDAD(id_ciudad, …, *id_departamento*), DEPARTAMENTO |
| doc_cliente → nombre, ciudad | CLIENTE(id_cliente, tipo_documento, num_documento, nombres, apellidos, …, *id_ciudad*) |
| mascota → raza → especie | MASCOTA(id_mascota, …, *id_raza*, *id_cliente*), RAZA(id_raza, …, *id_especie*), ESPECIE |
| factura → veterinario, mascota | La atención se separa del cobro: CITA y CONSULTA registran mascota y veterinario; FACTURA solo cliente, sede y empleado |
| codigo_item → servicio o medicamento | El ítem se especializa en SERVICIO y MEDICAMENTO; DETALLE_FACTURA referencia uno de los dos |

Table: Eliminación de dependencias transitivas

Al completar el análisis para todo el dominio (personal, inventario, fórmulas) se obtiene exactamente el esquema de
22 tablas presentado en la sección de esquema relacional.

#### Verificación de 3FN del esquema final

| Tabla | Determinantes | Dependencias parciales | Dependencias transitivas | Forma |
|---|---|---|---|---|
| DEPARTAMENTO, CIUDAD, ESPECIE, RAZA, ESPECIALIDAD, SERVICIO, MEDICAMENTO | PK y llaves únicas (código, nombre) | No (PK simple) | No | 3FN |
| SEDE, EMPLEADO, CLIENTE, MASCOTA, HISTORIA_CLINICA | PK y llaves únicas (documento, email, microchip) | No (PK simple) | No: ciudad, raza y sede se guardan solo como FK | 3FN |
| VETERINARIO, AUXILIAR | id_empleado | No | No | 3FN |
| CITA | id_cita; (id_veterinario, fecha_hora) | No | No: los datos de mascota, sede y servicio están en sus tablas | 3FN |
| CONSULTA, FACTURA | PK y llaves únicas (id_cita, numero_factura) | No | No: el total es derivado y no se almacena | 3FN |
| TELEFONO_CLIENTE | (id_cliente, telefono) | No | No | 3FN |
| VETERINARIO_ESPECIALIDAD | (id_empleado, id_especialidad) | No: fecha_certificacion depende de ambas | No | 3FN |
| PRESCRIPCION | (id_consulta, id_medicamento) | No: dosis, frecuencia y cantidad dependen de la combinación | No | 3FN |
| INVENTARIO | (id_sede, id_medicamento) | No: el stock depende de la sede y del medicamento | No | 3FN |
| DETALLE_FACTURA | (id_factura, num_linea) | No: cantidad y precio dependen de la línea concreta | No | 3FN |

Table: Verificación de 3FN por tabla

En todas las tablas los únicos determinantes son llaves candidatas (PK o UNIQUE); por lo tanto el esquema cumple
además la Forma Normal de Boyce-Codd (FNBC).

### Script DDL

El modelo físico correspondiente se encuentra en `database/01_ddl/01_crear_tablas.sql` (tablas y 113 restricciones
con nombre) y `database/01_ddl/02_comentarios.sql` (comentarios de tablas y columnas). Ambos se generan desde la misma
especificación del modelo, lo que garantiza que el diccionario de datos, los diagramas y el script coinciden.

## Diccionario de Datos



### Introducción

El diccionario de datos describe cada tabla y cada columna de la base de datos SIGEVET: tipo de dato de MySQL,
obligatoriedad, participación en llaves, valores por defecto, reglas de dominio (CHECK), significado y un valor de
ejemplo. Se entrega en este documento y en el libro de Excel `07_Diccionario_de_Datos.xlsx`, que permite filtrar y
buscar.

Las descripciones también quedan dentro de la base de datos: cada tabla y cada columna del script de creación
(`database/01_ddl/01_crear_tablas.sql`) lleva su `COMMENT`, así que se pueden consultar en MySQL Workbench (pestaña
*Columns* de cada tabla) o con `information_schema.TABLES` e `information_schema.COLUMNS`.

### Convenciones

| Elemento | Convención |
|---|---|
| Nombres de tablas | MAYÚSCULAS, singular, con guion bajo (`HISTORIA_CLINICA`) |
| Nombres de columnas | minúsculas con guion bajo (`fecha_nacimiento`) |
| Llave primaria | `id_<tabla>`, numérica; restricción `PK_<TABLA>` |
| Llave foránea | Mismo nombre de la PK referenciada, o un rol (`id_director`, `id_veterinario`, `id_auxiliar`); restricción `FK_<TABLA>_<REFERENCIA>` |
| Llave única | `UQ_<TABLA>_<COLUMNAS>` |
| Verificación | `CK_<TABLA>_<COLUMNA>` |
| Columna Llave | PK = llave primaria, FK = llave foránea, UK = forma parte de una llave única |
| Columna Nulo | "No" = NOT NULL (obligatoria); "Sí" = opcional |

Table: Convenciones del diccionario

| Tipo MySQL | Uso en el modelo |
|---|---|
| INT / BIGINT | Identificadores (con `AUTO_INCREMENT` en las llaves simples), cantidades, años |
| DECIMAL(p,s) | Valores monetarios, peso, temperatura y porcentajes |
| VARCHAR(n) | Textos de longitud variable |
| CHAR(n) | Códigos de longitud fija (DANE, discriminador `tipo_empleado`, banderas S/N, sexo) |
| DATE | Fechas sin hora (nacimiento, apertura, ingreso) |
| DATETIME | Fechas con hora (citas, consultas, facturas) y fechas de registro (`DEFAULT CURRENT_TIMESTAMP`) |

Table: Tipos de datos utilizados

<!-- Created: 22:45 09-Oct-2026 -->
<!-- Generado por docs/tools/gen_fragmentos.py: no editar a mano -->

| Tipo de restricción | Prefijo | Cantidad |
|---|---|---|
| Llave primaria | PK_ | 22 |
| Llave foránea | FK_ | 30 |
| Única | UQ_ | 25 |
| Verificación (CHECK) | CK_ | 36 |
| NOT NULL (columnas obligatorias de 138) | — | 118 |

Table: Resumen de restricciones de integridad


### Diccionario por tabla

<!-- Created: 22:45 09-Oct-2026 -->
<!-- Generado por docs/tools/gen_fragmentos.py: no editar a mano -->

#### DEPARTAMENTO

**Descripción:** Departamentos de Colombia donde la red tiene sedes o clientes (codificación DANE).  
**Tipo:** Catálogo · **Área:** Organización · **Llave primaria:** id_departamento

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_departamento | INT | No | PK | AUTO_INCREMENT | Identificador del departamento. | 1 |
| codigo_dane | CHAR(2) | No | UK | CHECK codigo_dane REGEXP '^[0-9]{2}$' | Código DANE de dos dígitos. | 11 |
| nombre | VARCHAR(60) | No | UK | — | Nombre oficial del departamento. | Bogotá D.C. |

Table: Diccionario de la tabla DEPARTAMENTO

#### CIUDAD

**Descripción:** Municipios donde se ubican sedes y residen clientes.  
**Tipo:** Catálogo · **Área:** Organización · **Llave primaria:** id_ciudad

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_ciudad | INT | No | PK | AUTO_INCREMENT | Identificador de la ciudad. | 1 |
| codigo_dane | CHAR(5) | No | UK | CHECK codigo_dane REGEXP '^[0-9]{5}$' | Código DANE del municipio (5 dígitos). | 11001 |
| nombre | VARCHAR(80) | No | UK | — | Nombre del municipio. | Bogotá D.C. |
| id_departamento | INT | No | FK, UK | — | Departamento al que pertenece la ciudad. | 1 |

Table: Diccionario de la tabla CIUDAD

**Integridad referencial y reglas:** id_departamento → DEPARTAMENTO(id_departamento).

#### SEDE

**Descripción:** Clínica física de la red Huella Sana. Cada sede tiene exactamente un director.  
**Tipo:** Fuerte · **Área:** Organización · **Llave primaria:** id_sede

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_sede | INT | No | PK | AUTO_INCREMENT | Identificador de la sede. | 1 |
| nombre | VARCHAR(80) | No | UK | — | Nombre comercial de la sede. | Huella Sana Usaquén |
| direccion | VARCHAR(120) | No | — | — | Dirección física. | Cl. 119 #6-24 |
| telefono | VARCHAR(20) | No | — | — | Teléfono principal de la sede. | 6016123456 |
| email | VARCHAR(100) | No | UK | — | Correo electrónico de la sede. | usaquen@huellasana.co |
| fecha_apertura | DATE | No | — | — | Fecha en que la sede inició operación. | 2019-03-01 |
| id_ciudad | INT | No | FK | — | Ciudad donde se ubica la sede. | 1 |
| id_director | INT | Sí | FK, UK | — | Empleado que dirige la sede (relación 1:1 DIRIGE). | 1 |

Table: Diccionario de la tabla SEDE

**Integridad referencial y reglas:** id_ciudad → CIUDAD(id_ciudad); id_director → EMPLEADO(id_empleado).

#### EMPLEADO

**Descripción:** Supertipo de todo el personal. El discriminador tipo_empleado indica el subtipo (VET, AUX) o ADM para personal administrativo que no pertenece a ningún subtipo.  
**Tipo:** Supertipo · **Área:** Organización · **Llave primaria:** id_empleado

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_empleado | INT | No | PK | AUTO_INCREMENT | Identificador del empleado. | 1 |
| tipo_documento | VARCHAR(3) | No | UK | CHECK tipo_documento IN ('CC','CE','PA','PPT') | Tipo de documento de identidad. | CC |
| num_documento | VARCHAR(15) | No | UK | — | Número del documento de identidad. | 1020345678 |
| nombres | VARCHAR(60) | No | — | — | Nombres del empleado (parte del atributo compuesto nombre_completo). | Laura Marcela |
| apellidos | VARCHAR(60) | No | — | — | Apellidos del empleado. | Gómez Rincón |
| fecha_nacimiento | DATE | No | — | — | Fecha de nacimiento. | 1990-05-14 |
| telefono | VARCHAR(20) | No | — | — | Teléfono de contacto. | 3104567890 |
| email | VARCHAR(100) | No | UK | — | Correo corporativo. | lgomez@huellasana.co |
| tipo_empleado | CHAR(3) | No | — | CHECK tipo_empleado IN ('VET','AUX','ADM') | Discriminador de la especialización: VET, AUX o ADM. | VET |
| fecha_ingreso | DATE | No | — | — | Fecha de vinculación a la red. | 2021-02-01 |
| salario | DECIMAL(12,2) | No | — | CHECK salario > 0 | Salario mensual en pesos colombianos. | 4800000 |
| estado | VARCHAR(10) | No | — | DEFAULT 'ACTIVO'; CHECK estado IN ('ACTIVO','INACTIVO') | Estado laboral. | ACTIVO |
| id_sede | INT | No | FK | — | Sede donde labora el empleado (relación LABORA_EN). | 1 |

Table: Diccionario de la tabla EMPLEADO

**Integridad referencial y reglas:** id_sede → SEDE(id_sede).

#### VETERINARIO

**Descripción:** Subtipo de EMPLEADO: médico veterinario habilitado para atender citas y prescribir.  
**Tipo:** Subtipo · **Área:** Organización · **Llave primaria:** id_empleado

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_empleado | INT | No | PK, FK | — | Identificador heredado del supertipo EMPLEADO. | 1 |
| tarjeta_profesional | VARCHAR(20) | No | UK | — | Número de tarjeta profesional (COMVEZCOL). | TP-25410 |
| universidad | VARCHAR(100) | No | — | — | Universidad que otorgó el título. | Universidad Nacional de Colombia |
| anio_graduacion | INT | No | — | CHECK anio_graduacion BETWEEN 1970 AND 2026 | Año de graduación. | 2014 |

Table: Diccionario de la tabla VETERINARIO

**Integridad referencial y reglas:** id_empleado → EMPLEADO(id_empleado).

#### AUXILIAR

**Descripción:** Subtipo de EMPLEADO: auxiliar veterinario que asiste las consultas.  
**Tipo:** Subtipo · **Área:** Organización · **Llave primaria:** id_empleado

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_empleado | INT | No | PK, FK | — | Identificador heredado del supertipo EMPLEADO. | 10 |
| area | VARCHAR(20) | No | — | CHECK area IN ('CLINICA','CIRUGIA','LABORATORIO','ESTETICA','HOSPITALIZACION') | Área de desempeño. | CLINICA |
| certificacion | VARCHAR(100) | Sí | — | — | Título técnico o tecnológico. | Técnico en asistencia veterinaria |
| turno | VARCHAR(10) | No | — | CHECK turno IN ('MANANA','TARDE','NOCHE') | Turno habitual. | MANANA |

Table: Diccionario de la tabla AUXILIAR

**Integridad referencial y reglas:** id_empleado → EMPLEADO(id_empleado).

#### ESPECIALIDAD

**Descripción:** Áreas de especialización médica veterinaria.  
**Tipo:** Catálogo · **Área:** Organización · **Llave primaria:** id_especialidad

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_especialidad | INT | No | PK | AUTO_INCREMENT | Identificador de la especialidad. | 1 |
| nombre | VARCHAR(60) | No | UK | — | Nombre de la especialidad. | Cirugía |
| descripcion | VARCHAR(200) | Sí | — | — | Descripción breve. | Procedimientos quirúrgicos de tejidos blandos y ortopedia |

Table: Diccionario de la tabla ESPECIALIDAD

#### VETERINARIO_ESPECIALIDAD

**Descripción:** Resuelve la relación N:M CERTIFICADO_EN entre VETERINARIO y ESPECIALIDAD.  
**Tipo:** Asociativa (N:M) · **Área:** Organización · **Llave primaria:** id_empleado, id_especialidad

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_empleado | INT | No | PK, FK | — | Veterinario certificado. | 1 |
| id_especialidad | INT | No | PK, FK | — | Especialidad certificada. | 2 |
| fecha_certificacion | DATE | No | — | — | Fecha en que obtuvo la certificación. | 2018-11-30 |

Table: Diccionario de la tabla VETERINARIO_ESPECIALIDAD

**Integridad referencial y reglas:** id_empleado → VETERINARIO(id_empleado); id_especialidad → ESPECIALIDAD(id_especialidad).

#### CLIENTE

**Descripción:** Persona propietaria de una o más mascotas; es quien paga los servicios.  
**Tipo:** Fuerte · **Área:** Clínica · **Llave primaria:** id_cliente

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_cliente | INT | No | PK | AUTO_INCREMENT | Identificador del cliente. | 1 |
| tipo_documento | VARCHAR(3) | No | UK | CHECK tipo_documento IN ('CC','CE','PA','PPT') | Tipo de documento de identidad. | CC |
| num_documento | VARCHAR(15) | No | UK | — | Número del documento de identidad. | 52987654 |
| nombres | VARCHAR(60) | No | — | — | Nombres del cliente. | Andrés Felipe |
| apellidos | VARCHAR(60) | No | — | — | Apellidos del cliente. | Martínez Ruiz |
| email | VARCHAR(100) | Sí | UK | — | Correo electrónico. | afmartinez@correo.com |
| direccion | VARCHAR(120) | No | — | — | Dirección de residencia. | Cra. 15 #93-60 Apto 402 |
| id_ciudad | INT | No | FK | — | Ciudad de residencia. | 1 |
| fecha_registro | DATETIME | No | — | DEFAULT CURRENT_TIMESTAMP | Fecha de registro en el sistema. | 2025-01-15 |

Table: Diccionario de la tabla CLIENTE

**Integridad referencial y reglas:** id_ciudad → CIUDAD(id_ciudad).

#### TELEFONO_CLIENTE

**Descripción:** Atributo multivaluado teléfono de CLIENTE llevado a tabla (Primera Forma Normal).  
**Tipo:** Multivaluado · **Área:** Clínica · **Llave primaria:** id_cliente, telefono

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_cliente | INT | No | PK, FK | — | Cliente dueño del teléfono. | 1 |
| telefono | VARCHAR(20) | No | PK | — | Número telefónico. | 3157894561 |
| tipo | VARCHAR(10) | No | — | CHECK tipo IN ('MOVIL','FIJO','TRABAJO') | Tipo de línea. | MOVIL |

Table: Diccionario de la tabla TELEFONO_CLIENTE

**Integridad referencial y reglas:** id_cliente → CLIENTE(id_cliente).

#### ESPECIE

**Descripción:** Especie animal atendida por la red.  
**Tipo:** Catálogo · **Área:** Clínica · **Llave primaria:** id_especie

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_especie | INT | No | PK | AUTO_INCREMENT | Identificador de la especie. | 1 |
| nombre | VARCHAR(40) | No | UK | — | Nombre común de la especie. | Canino |

Table: Diccionario de la tabla ESPECIE

#### RAZA

**Descripción:** Raza de una especie. Separarla de MASCOTA elimina la dependencia transitiva raza → especie (3FN).  
**Tipo:** Catálogo · **Área:** Clínica · **Llave primaria:** id_raza

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_raza | INT | No | PK | AUTO_INCREMENT | Identificador de la raza. | 1 |
| nombre | VARCHAR(60) | No | UK | — | Nombre de la raza. | Labrador Retriever |
| tamano | VARCHAR(10) | No | — | CHECK tamano IN ('PEQUENO','MEDIANO','GRANDE','NA') | Tamaño típico de la raza. | GRANDE |
| id_especie | INT | No | FK, UK | — | Especie a la que pertenece la raza. | 1 |

Table: Diccionario de la tabla RAZA

**Integridad referencial y reglas:** id_especie → ESPECIE(id_especie).

#### MASCOTA

**Descripción:** Paciente de la red. La edad es un atributo derivado de fecha_nacimiento (no se almacena).  
**Tipo:** Fuerte · **Área:** Clínica · **Llave primaria:** id_mascota

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_mascota | INT | No | PK | AUTO_INCREMENT | Identificador de la mascota. | 1 |
| nombre | VARCHAR(40) | No | — | — | Nombre de la mascota. | Toby |
| fecha_nacimiento | DATE | Sí | — | — | Fecha de nacimiento (aproximada si se desconoce). | 2020-06-10 |
| sexo | CHAR(1) | No | — | CHECK sexo IN ('M','H') | M = macho, H = hembra. | M |
| color | VARCHAR(40) | Sí | — | — | Color o patrón del pelaje/plumaje. | Dorado |
| microchip | VARCHAR(15) | Sí | UK | — | Código del microchip de identificación (15 dígitos ISO). | 985112003456789 |
| esterilizado | CHAR(1) | No | — | DEFAULT 'N'; CHECK esterilizado IN ('S','N') | S si está esterilizada, N si no. | S |
| estado | VARCHAR(10) | No | — | DEFAULT 'ACTIVA'; CHECK estado IN ('ACTIVA','FALLECIDA','TRASLADADA') | Estado del paciente. | ACTIVA |
| id_raza | INT | No | FK | — | Raza de la mascota (determina la especie). | 1 |
| id_cliente | INT | No | FK | — | Cliente propietario. | 1 |
| fecha_registro | DATETIME | No | — | DEFAULT CURRENT_TIMESTAMP | Fecha de registro en la red. | 2025-01-15 |

Table: Diccionario de la tabla MASCOTA

**Integridad referencial y reglas:** id_raza → RAZA(id_raza); id_cliente → CLIENTE(id_cliente).

#### HISTORIA_CLINICA

**Descripción:** Historia clínica única de cada mascota (relación 1:1 TIENE), compartida por todas las sedes.  
**Tipo:** Fuerte · **Área:** Clínica · **Llave primaria:** id_historia

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_historia | INT | No | PK | AUTO_INCREMENT | Identificador de la historia clínica. | 1 |
| numero_historia | VARCHAR(12) | No | UK | — | Número legible de la historia. | HC-000001 |
| id_mascota | INT | No | FK, UK | — | Mascota a la que pertenece (UNIQUE: garantiza 1:1). | 1 |
| fecha_apertura | DATE | No | — | — | Fecha de apertura de la historia. | 2025-01-15 |
| grupo_sanguineo | VARCHAR(10) | Sí | — | — | Grupo sanguíneo si se conoce. | DEA 1.1+ |
| alergias | VARCHAR(200) | Sí | — | — | Alergias conocidas. | Penicilina |
| enfermedades_cronicas | VARCHAR(200) | Sí | — | — | Enfermedades crónicas diagnosticadas. | Dermatitis atópica |
| observaciones | VARCHAR(400) | Sí | — | — | Notas generales. | Paciente nervioso, usar bozal |

Table: Diccionario de la tabla HISTORIA_CLINICA

**Integridad referencial y reglas:** id_mascota → MASCOTA(id_mascota).

#### SERVICIO

**Descripción:** Catálogo de servicios ofrecidos con su precio vigente y duración estimada.  
**Tipo:** Catálogo · **Área:** Clínica · **Llave primaria:** id_servicio

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_servicio | INT | No | PK | AUTO_INCREMENT | Identificador del servicio. | 1 |
| codigo | VARCHAR(10) | No | UK | — | Código interno del servicio. | SRV-CON01 |
| nombre | VARCHAR(80) | No | UK | — | Nombre del servicio. | Consulta general |
| categoria | VARCHAR(20) | No | — | CHECK categoria IN ('CONSULTA','VACUNACION','CIRUGIA','LABORATORIO','ESTETICA','IMAGENOLOGIA','HOSPITALIZACION') | Categoría del servicio. | CONSULTA |
| precio | DECIMAL(10,2) | No | — | CHECK precio > 0 | Precio vigente en pesos (IVA incluido). | 65000 |
| duracion_min | INT | No | — | CHECK duracion_min > 0 | Duración estimada en minutos. | 30 |

Table: Diccionario de la tabla SERVICIO

#### CITA

**Descripción:** Reserva de atención de una mascota con un veterinario, en una sede, para un servicio.  
**Tipo:** Fuerte · **Área:** Clínica · **Llave primaria:** id_cita

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_cita | BIGINT | No | PK | AUTO_INCREMENT | Identificador de la cita. | 1 |
| fecha_hora | DATETIME | No | UK | — | Fecha y hora programada de la cita. | 2026-03-12 09:30 |
| motivo | VARCHAR(200) | Sí | — | — | Motivo de la cita informado por el cliente. | Vómito desde ayer |
| estado | VARCHAR(12) | No | — | DEFAULT 'PROGRAMADA'; CHECK estado IN ('PROGRAMADA','ATENDIDA','CANCELADA','NO_ASISTIO') | Estado de la cita. | ATENDIDA |
| id_mascota | INT | No | FK | — | Mascota que será atendida. | 1 |
| id_veterinario | INT | No | FK, UK | — | Veterinario asignado. | 1 |
| id_sede | INT | No | FK | — | Sede donde se realiza la cita. | 1 |
| id_servicio | INT | No | FK | — | Servicio solicitado. | 1 |

Table: Diccionario de la tabla CITA

**Integridad referencial y reglas:** id_mascota → MASCOTA(id_mascota); id_veterinario → VETERINARIO(id_empleado); id_sede → SEDE(id_sede); id_servicio → SERVICIO(id_servicio).

#### CONSULTA

**Descripción:** Registro clínico de una cita atendida (relación 1:1 GENERA con CITA).  
**Tipo:** Fuerte · **Área:** Clínica · **Llave primaria:** id_consulta

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_consulta | BIGINT | No | PK | AUTO_INCREMENT | Identificador de la consulta. | 1 |
| id_cita | BIGINT | No | FK, UK | — | Cita que originó la consulta (UNIQUE: garantiza 1:1). | 1 |
| id_auxiliar | INT | Sí | FK | — | Auxiliar que asistió la consulta (opcional). | 10 |
| fecha_hora_inicio | DATETIME | No | — | — | Fecha y hora real de inicio de la atención. | 2026-03-12 09:35 |
| peso_kg | DECIMAL(5,2) | No | — | CHECK peso_kg > 0 | Peso registrado en kilogramos. | 28.40 |
| temperatura_c | DECIMAL(4,1) | Sí | — | CHECK temperatura_c BETWEEN 30 AND 45 | Temperatura corporal en °C. | 38.6 |
| frecuencia_cardiaca | INT | Sí | — | CHECK frecuencia_cardiaca BETWEEN 20 AND 300 | Latidos por minuto. | 96 |
| sintomas | VARCHAR(400) | Sí | — | — | Signos y síntomas observados. | Vómito, decaimiento |
| diagnostico | VARCHAR(400) | No | — | — | Diagnóstico del veterinario. | Gastroenteritis aguda |
| observaciones | VARCHAR(400) | Sí | — | — | Indicaciones y notas. | Dieta blanda 3 días |

Table: Diccionario de la tabla CONSULTA

**Integridad referencial y reglas:** id_cita → CITA(id_cita); id_auxiliar → AUXILIAR(id_empleado).

#### MEDICAMENTO

**Descripción:** Catálogo de medicamentos e insumos veterinarios con su precio de venta vigente.  
**Tipo:** Catálogo · **Área:** Comercial · **Llave primaria:** id_medicamento

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_medicamento | INT | No | PK | AUTO_INCREMENT | Identificador del medicamento. | 1 |
| codigo | VARCHAR(12) | No | UK | — | Código interno (SKU). | MED-0001 |
| nombre | VARCHAR(80) | No | UK | — | Nombre comercial. | Amoxicilina 500 |
| principio_activo | VARCHAR(80) | No | — | — | Principio activo. | Amoxicilina |
| presentacion | VARCHAR(40) | No | UK | — | Forma farmacéutica y concentración. | Tableta 500 mg |
| laboratorio | VARCHAR(60) | No | — | — | Laboratorio fabricante. | Lab. Andino Vet |
| precio_venta | DECIMAL(10,2) | No | — | CHECK precio_venta > 0 | Precio de venta unitario vigente (IVA incluido). | 1800 |
| requiere_formula | CHAR(1) | No | — | DEFAULT 'N'; CHECK requiere_formula IN ('S','N') | S si solo se vende con fórmula médica. | S |

Table: Diccionario de la tabla MEDICAMENTO

#### PRESCRIPCION

**Descripción:** Resuelve la relación N:M PRESCRIBE entre CONSULTA y MEDICAMENTO (fórmula médica).  
**Tipo:** Asociativa (N:M) · **Área:** Comercial · **Llave primaria:** id_consulta, id_medicamento

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_consulta | BIGINT | No | PK, FK | — | Consulta en la que se formula. | 1 |
| id_medicamento | INT | No | PK, FK | — | Medicamento formulado. | 1 |
| dosis | VARCHAR(60) | No | — | — | Dosis por toma. | 1 tableta |
| frecuencia_horas | INT | No | — | CHECK frecuencia_horas > 0 | Cada cuántas horas se administra. | 12 |
| duracion_dias | INT | No | — | CHECK duracion_dias > 0 | Días de tratamiento. | 7 |
| cantidad | INT | No | — | CHECK cantidad > 0 | Unidades a dispensar. | 14 |
| indicaciones | VARCHAR(200) | Sí | — | — | Indicaciones adicionales. | Administrar con comida |

Table: Diccionario de la tabla PRESCRIPCION

**Integridad referencial y reglas:** id_consulta → CONSULTA(id_consulta); id_medicamento → MEDICAMENTO(id_medicamento).

#### INVENTARIO

**Descripción:** Resuelve la relación N:M ALMACENA entre SEDE y MEDICAMENTO: existencias por sede.  
**Tipo:** Asociativa (N:M) · **Área:** Comercial · **Llave primaria:** id_sede, id_medicamento

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_sede | INT | No | PK, FK | — | Sede que almacena. | 1 |
| id_medicamento | INT | No | PK, FK | — | Medicamento almacenado. | 1 |
| stock_actual | INT | No | — | CHECK stock_actual >= 0 | Unidades disponibles. | 120 |
| stock_minimo | INT | No | — | CHECK stock_minimo >= 0 | Punto de reorden. | 30 |
| fecha_actualizacion | DATETIME | No | — | DEFAULT CURRENT_TIMESTAMP | Fecha del último movimiento o conteo. | 2026-09-30 |

Table: Diccionario de la tabla INVENTARIO

**Integridad referencial y reglas:** id_sede → SEDE(id_sede); id_medicamento → MEDICAMENTO(id_medicamento).

#### FACTURA

**Descripción:** Documento de venta emitido a un cliente en una sede. El total es derivado (vista V_FACTURA_TOTAL).  
**Tipo:** Fuerte · **Área:** Comercial · **Llave primaria:** id_factura

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_factura | BIGINT | No | PK | AUTO_INCREMENT | Identificador de la factura. | 1 |
| numero_factura | VARCHAR(15) | No | UK | — | Consecutivo legible de facturación. | FV-000001 |
| fecha_emision | DATETIME | No | — | — | Fecha y hora de emisión. | 2026-03-12 10:20 |
| metodo_pago | VARCHAR(16) | No | — | CHECK metodo_pago IN ('EFECTIVO','TARJETA_DEBITO','TARJETA_CREDITO','TRANSFERENCIA') | Medio de pago. | TARJETA_DEBITO |
| estado | VARCHAR(10) | No | — | DEFAULT 'PAGADA'; CHECK estado IN ('PAGADA','PENDIENTE','ANULADA') | Estado de la factura. | PAGADA |
| id_cliente | INT | No | FK | — | Cliente al que se factura. | 1 |
| id_sede | INT | No | FK | — | Sede que emite la factura. | 1 |
| id_empleado | INT | No | FK | — | Empleado que emite la factura. | 20 |

Table: Diccionario de la tabla FACTURA

**Integridad referencial y reglas:** id_cliente → CLIENTE(id_cliente); id_sede → SEDE(id_sede); id_empleado → EMPLEADO(id_empleado).

#### DETALLE_FACTURA

**Descripción:** Entidad débil de FACTURA: cada línea cobra un servicio O un medicamento (arco exclusivo).  
**Tipo:** Débil · **Área:** Comercial · **Llave primaria:** id_factura, num_linea

| Columna | Tipo de dato | Nulo | Llave | Restricción / Default | Descripción | Ejemplo |
|---|---|---|---|---|---|---|
| id_factura | BIGINT | No | PK, FK | — | Factura a la que pertenece la línea (parte de la llave). | 1 |
| num_linea | INT | No | PK | CHECK num_linea > 0 | Número de línea dentro de la factura (llave parcial). | 1 |
| id_servicio | INT | Sí | FK | — | Servicio cobrado (excluyente con id_medicamento). | 1 |
| id_medicamento | INT | Sí | FK | — | Medicamento vendido (excluyente con id_servicio). | — |
| cantidad | INT | No | — | CHECK cantidad > 0 | Cantidad facturada. | 1 |
| precio_unitario | DECIMAL(10,2) | No | — | CHECK precio_unitario >= 0 | Precio aplicado en la fecha de la venta (histórico, no derivado). | 65000 |
| descuento_pct | DECIMAL(5,2) | No | — | DEFAULT 0; CHECK descuento_pct BETWEEN 0 AND 100 | Porcentaje de descuento aplicado. | 0 |

Table: Diccionario de la tabla DETALLE_FACTURA

**Integridad referencial y reglas:** id_factura → FACTURA(id_factura); id_servicio → SERVICIO(id_servicio); id_medicamento → MEDICAMENTO(id_medicamento); CHECK (id_servicio IS NOT NULL AND id_medicamento IS NULL) OR (id_servicio IS NULL AND id_medicamento IS NOT NULL).

## Documento de Implementación — Sentencias de Álgebra Relacional



### Introducción

El álgebra relacional es el lenguaje formal y procedimental sobre el que se fundamentan los motores relacionales: cada
consulta SQL se traduce internamente a un árbol de operaciones del álgebra. Este documento presenta **55 ejercicios**
sobre la base de datos SIGEVET. Cada ejercicio incluye el enunciado en lenguaje natural, la expresión en álgebra
relacional, la sentencia SQL equivalente para MySQL y el **resultado real** obtenido al ejecutarla sobre el conjunto de
datos de prueba del proyecto.

### Operadores y notación

| Operador | Símbolo | Tipo | Descripción |
|---|---|---|---|
| Selección | $\sigma_{condición}(R)$ | Unario | Filtra las tuplas de R que cumplen la condición (equivale a WHERE) |
| Proyección | $\pi_{atributos}(R)$ | Unario | Conserva solo los atributos indicados y elimina duplicados (SELECT DISTINCT) |
| Renombramiento | $\rho_{S(a_1, \ldots, a_n)}(R)$ | Unario | Cambia el nombre de la relación o de sus atributos (alias) |
| Unión | $R \cup S$ | Binario de conjuntos | Tuplas que están en R, en S o en ambas (UNION) |
| Intersección | $R \cap S$ | Binario de conjuntos | Tuplas que están en R y en S (INTERSECT) |
| Diferencia | $R - S$ | Binario de conjuntos | Tuplas de R que no están en S (MINUS) |
| Producto cartesiano | $R \times S$ | Binario | Todas las combinaciones de tuplas (CROSS JOIN) |
| Reunión theta / equirreunión | $R \bowtie_{condición} S$ | Binario | Producto seguido de selección (JOIN … ON) |
| Reunión natural | $R \bowtie S$ | Binario | Reunión por igualdad de los atributos con el mismo nombre (NATURAL JOIN) |
| Reunión externa izquierda, derecha y completa | $⟕$, $⟖$, $⟗$ | Binario | Conserva las tuplas sin pareja completándolas con nulos (LEFT, RIGHT, FULL OUTER JOIN) |
| Semirreunión | $R \ltimes S$ | Binario | Tuplas de R que tienen pareja en S (EXISTS) |
| Antirreunión | $R \triangleright S$ | Binario | Tuplas de R que no tienen pareja en S (NOT EXISTS) |
| División | $R \div S$ | Binario | Valores de R asociados con **todas** las tuplas de S (doble NOT EXISTS) |
| Agregación | ${}_{G}\gamma_{F}(R)$ | Extendido | Agrupa por G y calcula las funciones F: COUNT, SUM, AVG, MIN, MAX (GROUP BY) |
| Asignación | $T \leftarrow E$ | Auxiliar | Guarda un resultado intermedio para usarlo en el paso siguiente |

Table: Operadores del álgebra relacional utilizados

Convenciones adicionales: ∧ (y), ∨ (o), ≠ (diferente), ≥ y ≤ (comparaciones), NULO (valor nulo). En la proyección
generalizada, la expresión `a · b → nombre` calcula un atributo nuevo. Para las fechas se usan literales con formato
ISO (`'2026-03-01'`), que en SQL se escriben `DATE '2026-03-01'`.

### Esquema de referencia

Las consultas se formulan sobre el esquema relacional de SIGEVET (llave primaria subrayada, *llaves foráneas en
cursiva*):

<!-- Created: 22:45 09-Oct-2026 -->
<!-- Generado por docs/tools/gen_fragmentos.py: no editar a mano -->

- **DEPARTAMENTO** ([id_departamento]{.underline}, codigo_dane, nombre)
- **CIUDAD** ([id_ciudad]{.underline}, codigo_dane, nombre, *id_departamento*)
- **SEDE** ([id_sede]{.underline}, nombre, direccion, telefono, email, fecha_apertura, *id_ciudad*, *id_director*)
- **EMPLEADO** ([id_empleado]{.underline}, tipo_documento, num_documento, nombres, apellidos, fecha_nacimiento, telefono, email, tipo_empleado, fecha_ingreso, salario, estado, *id_sede*)
- **VETERINARIO** (*[id_empleado]{.underline}*, tarjeta_profesional, universidad, anio_graduacion)
- **AUXILIAR** (*[id_empleado]{.underline}*, area, certificacion, turno)
- **ESPECIALIDAD** ([id_especialidad]{.underline}, nombre, descripcion)
- **VETERINARIO_ESPECIALIDAD** (*[id_empleado]{.underline}*, *[id_especialidad]{.underline}*, fecha_certificacion)
- **CLIENTE** ([id_cliente]{.underline}, tipo_documento, num_documento, nombres, apellidos, email, direccion, *id_ciudad*, fecha_registro)
- **TELEFONO_CLIENTE** (*[id_cliente]{.underline}*, [telefono]{.underline}, tipo)
- **ESPECIE** ([id_especie]{.underline}, nombre)
- **RAZA** ([id_raza]{.underline}, nombre, tamano, *id_especie*)
- **MASCOTA** ([id_mascota]{.underline}, nombre, fecha_nacimiento, sexo, color, microchip, esterilizado, estado, *id_raza*, *id_cliente*, fecha_registro)
- **HISTORIA_CLINICA** ([id_historia]{.underline}, numero_historia, *id_mascota*, fecha_apertura, grupo_sanguineo, alergias, enfermedades_cronicas, observaciones)
- **SERVICIO** ([id_servicio]{.underline}, codigo, nombre, categoria, precio, duracion_min)
- **CITA** ([id_cita]{.underline}, fecha_hora, motivo, estado, *id_mascota*, *id_veterinario*, *id_sede*, *id_servicio*)
- **CONSULTA** ([id_consulta]{.underline}, *id_cita*, *id_auxiliar*, fecha_hora_inicio, peso_kg, temperatura_c, frecuencia_cardiaca, sintomas, diagnostico, observaciones)
- **MEDICAMENTO** ([id_medicamento]{.underline}, codigo, nombre, principio_activo, presentacion, laboratorio, precio_venta, requiere_formula)
- **PRESCRIPCION** (*[id_consulta]{.underline}*, *[id_medicamento]{.underline}*, dosis, frecuencia_horas, duracion_dias, cantidad, indicaciones)
- **INVENTARIO** (*[id_sede]{.underline}*, *[id_medicamento]{.underline}*, stock_actual, stock_minimo, fecha_actualizacion)
- **FACTURA** ([id_factura]{.underline}, numero_factura, fecha_emision, metodo_pago, estado, *id_cliente*, *id_sede*, *id_empleado*)
- **DETALLE_FACTURA** (*[id_factura]{.underline}*, [num_linea]{.underline}, *id_servicio*, *id_medicamento*, cantidad, precio_unitario, descuento_pct)


### Metodología de verificación

1. Cada ejercicio se escribió primero en álgebra relacional y luego se tradujo a SQL de MySQL.
2. Las 55 sentencias SQL se reúnen en el script `database/03_varios/01_consultas_algebra_relacional.sql`.
   Están escritas para MySQL 8.4: la diferencia de conjuntos se escribe `EXCEPT`, el límite de filas `LIMIT` y, como
   MySQL no tiene `FULL OUTER JOIN`, la reunión externa completa del ejercicio 29 se arma como la unión (`UNION`) de la
   reunión externa izquierda y la derecha, que es justo su definición en álgebra relacional.
3. Para comprobar que cada expresión es correcta, las consultas se ejecutaron sobre los datos de prueba del proyecto
   (1.091 registros generados de forma reproducible). Los resultados de este documento son la salida real de esas
   ejecuciones; cuando el resultado tiene más de 8 filas se muestran las primeras 8 y se indica el total.

| Tabla | Registros | Tabla | Registros |
|---|---|---|---|
| DEPARTAMENTO | 6 | CIUDAD | 11 |
| SEDE | 5 | EMPLEADO | 27 |
| VETERINARIO | 12 | AUXILIAR | 8 |
| ESPECIALIDAD | 8 | VETERINARIO_ESPECIALIDAD | 12 |
| CLIENTE | 45 | TELEFONO_CLIENTE | 66 |
| ESPECIE | 5 | RAZA | 20 |
| MASCOTA | 59 | HISTORIA_CLINICA | 59 |
| SERVICIO | 14 | CITA | 144 |
| CONSULTA | 95 | MEDICAMENTO | 18 |
| PRESCRIPCION | 88 | INVENTARIO | 79 |
| FACTURA | 109 | DETALLE_FACTURA | 201 |

Table: Volumen de los datos de prueba

### Resumen de ejercicios

| Grupo | Ejercicios | Cantidad |
|---|---|---|
| Selección y proyección | 1 a 12 | 12 |
| Producto cartesiano | 13 y 14 | 2 |
| Renombramiento | 15 y 16 | 2 |
| Reunión natural y reunión theta | 17 a 25 | 9 |
| Reuniones externas | 26 a 29 | 4 |
| Semirreunión y antirreunión | 30 a 32 | 3 |
| Operaciones de conjuntos | 33 a 40 | 8 |
| División | 41 a 43 | 3 |
| Agregación y agrupamiento | 44 a 55 | 12 |
| **Total** | | **55** |

Table: Distribución de los ejercicios por tipo de operación

### Ejercicios

<!-- Created: 22:55 09-Oct-2026 -->
<!-- Generado por database/tools/gen_algebra.py -->

#### Selección (σ) y proyección (π)

##### Ejercicio 1. Servicios de vacunación

**Enunciado.** Listar el nombre y el precio de los servicios de la categoría VACUNACION.

**Álgebra relacional:**

$$\pi_{\text{nombre, precio}}(\sigma_{\text{categoria = 'VACUNACION'}}(\text{SERVICIO}))$$

**SQL (MySQL):**

```sql
SELECT nombre, precio
FROM SERVICIO
WHERE categoria = 'VACUNACION';
```

**Resultado con los datos de prueba** (3 fila(s)):

| nombre | precio |
|---|---|
| Vacuna múltiple canina | 75000 |
| Vacuna triple felina | 70000 |
| Vacuna antirrábica | 45000 |

Table: Resultado del ejercicio 1

##### Ejercicio 2. Hembras esterilizadas activas

**Enunciado.** Obtener el identificador, el nombre y el color de las mascotas hembra, esterilizadas y activas.

**Álgebra relacional:**

$$\pi_{\text{id\_mascota, nombre, color}}(\sigma_{\text{sexo = 'H' ∧ esterilizado = 'S' ∧ estado = 'ACTIVA'}}(\text{MASCOTA}))$$

**SQL (MySQL):**

```sql
SELECT id_mascota, nombre, color
FROM MASCOTA
WHERE sexo = 'H' AND esterilizado = 'S' AND estado = 'ACTIVA';
```

**Resultado con los datos de prueba** (17 fila(s); se muestran las primeras 8):

| id_mascota | nombre | color |
|---|---|---|
| 10 | Salem | Gris |
| 13 | Coco | Dorado |
| 15 | Oreo | Tricolor |
| 21 | Copito | Café |
| 25 | Simón | Gris |
| 26 | Rex | Café |
| 28 | Kiwi | Manchado |
| 29 | Coco | Crema |

Table: Resultado del ejercicio 2

##### Ejercicio 3. Empleados con salario alto

**Enunciado.** Mostrar nombres, apellidos y salario de los empleados que ganan más de $5.000.000.

**Álgebra relacional:**

$$\pi_{\text{nombres, apellidos, salario}}(\sigma_{\text{salario > 5000000}}(\text{EMPLEADO}))$$

**SQL (MySQL):**

```sql
SELECT nombres, apellidos, salario
FROM EMPLEADO
WHERE salario > 5000000;
```

**Resultado con los datos de prueba** (12 fila(s); se muestran las primeras 8):

| nombres | apellidos | salario |
|---|---|---|
| Ángela Sara | López Ramírez | 6006000 |
| Santiago Julián | Suárez Gómez | 5827000 |
| Alejandro David | Jiménez Restrepo | 6483000 |
| Felipe Andrés | Jiménez Ospina | 5200000 |
| Ricardo Esteban | Rojas Pérez | 6294000 |
| Andrés | Silva López | 6055000 |
| Paula | López Vargas | 5647000 |
| Andrea | López Ortiz | 6328000 |

Table: Resultado del ejercicio 3

##### Ejercicio 4. Medicamentos con fórmula de bajo costo

**Enunciado.** Listar código, nombre y precio de los medicamentos que requieren fórmula y cuestan menos de $10.000.

**Álgebra relacional:**

$$\pi_{\text{codigo, nombre, precio\_venta}}(\sigma_{\text{requiere\_formula = 'S' ∧ precio\_venta < 10000}}(\text{MEDICAMENTO}))$$

**SQL (MySQL):**

```sql
SELECT codigo, nombre, precio_venta
FROM MEDICAMENTO
WHERE requiere_formula = 'S' AND precio_venta < 10000;
```

**Resultado con los datos de prueba** (6 fila(s)):

| codigo | nombre | precio_venta |
|---|---|---|
| MED-0001 | Amoxivet | 1800 |
| MED-0005 | Gastrovet | 2500 |
| MED-0006 | Dermacalm | 1500 |
| MED-0010 | Metrovet | 1200 |
| MED-0012 | Cardiopet | 4200 |
| MED-0015 | Cefavet | 2100 |

Table: Resultado del ejercicio 4

##### Ejercicio 5. Citas perdidas

**Enunciado.** Listar las citas canceladas o a las que el cliente no asistió.

**Álgebra relacional:**

$$\pi_{\text{id\_cita, fecha\_hora, estado}}(\sigma_{\text{estado = 'CANCELADA' ∨ estado = 'NO\_ASISTIO'}}(\text{CITA}))$$

**SQL (MySQL):**

```sql
SELECT id_cita, fecha_hora, estado
FROM CITA
WHERE estado = 'CANCELADA' OR estado = 'NO_ASISTIO';
```

**Resultado con los datos de prueba** (31 fila(s); se muestran las primeras 8):

| id_cita | fecha_hora | estado |
|---|---|---|
| 1 | 2025-10-01 13:00:00 | NO_ASISTIO |
| 2 | 2025-10-06 11:30:00 | NO_ASISTIO |
| 3 | 2025-10-06 12:00:00 | NO_ASISTIO |
| 10 | 2025-10-23 14:00:00 | NO_ASISTIO |
| 19 | 2025-11-15 12:00:00 | NO_ASISTIO |
| 20 | 2025-11-15 15:30:00 | NO_ASISTIO |
| 22 | 2025-11-27 08:00:00 | CANCELADA |
| 25 | 2025-12-01 08:00:00 | NO_ASISTIO |

Table: Resultado del ejercicio 5

##### Ejercicio 6. Razas de tamaño grande

**Enunciado.** Obtener el nombre de las razas cuyo tamaño típico es GRANDE.

**Álgebra relacional:**

$$\pi_{\text{nombre}}(\sigma_{\text{tamano = 'GRANDE'}}(\text{RAZA}))$$

**SQL (MySQL):**

```sql
SELECT nombre
FROM RAZA
WHERE tamano = 'GRANDE';
```

**Resultado con los datos de prueba** (3 fila(s)):

| nombre |
|---|
| Labrador Retriever |
| Golden Retriever |
| Pastor Alemán |

Table: Resultado del ejercicio 6

##### Ejercicio 7. Teléfonos fijos

**Enunciado.** Listar el cliente y el número de los teléfonos de tipo FIJO.

**Álgebra relacional:**

$$\pi_{\text{id\_cliente, telefono}}(\sigma_{\text{tipo = 'FIJO'}}(\text{TELEFONO\_CLIENTE}))$$

**SQL (MySQL):**

```sql
SELECT id_cliente, telefono
FROM TELEFONO_CLIENTE
WHERE tipo = 'FIJO';
```

**Resultado con los datos de prueba** (19 fila(s); se muestran las primeras 8):

| id_cliente | telefono |
|---|---|
| 3 | 6061943814 |
| 6 | 6065446211 |
| 7 | 6039535601 |
| 9 | 6014013575 |
| 12 | 6022158212 |
| 14 | 6074571458 |
| 15 | 6046566204 |
| 18 | 6014688825 |

Table: Resultado del ejercicio 7

##### Ejercicio 8. Clientes sin correo electrónico

**Enunciado.** Obtener los clientes que no tienen correo electrónico registrado (selección sobre valores nulos).

**Álgebra relacional:**

$$\pi_{\text{id\_cliente, nombres, apellidos}}(\sigma_{\text{email = NULO}}(\text{CLIENTE}))$$

**SQL (MySQL):**

```sql
SELECT id_cliente, nombres, apellidos
FROM CLIENTE
WHERE email IS NULL;
```

**Resultado con los datos de prueba** (5 fila(s)):

| id_cliente | nombres | apellidos |
|---|---|---|
| 9 | Diana Paula | Restrepo Gómez |
| 18 | Juan Mateo | Torres García |
| 27 | Sara | Rincón Quintero |
| 36 | Carlos | Rincón Silva |
| 45 | Esteban Andrés | Pérez Restrepo |

Table: Resultado del ejercicio 8

##### Ejercicio 9. Consultas con fiebre

**Enunciado.** Listar las consultas en las que la temperatura fue igual o superior a 39,5 °C y su diagnóstico.

**Álgebra relacional:**

$$\pi_{\text{id\_consulta, temperatura\_c, diagnostico}}(\sigma_{\text{temperatura\_c ≥ 39.5}}(\text{CONSULTA}))$$

**SQL (MySQL):**

```sql
SELECT id_consulta, temperatura_c, diagnostico
FROM CONSULTA
WHERE temperatura_c >= 39.5;
```

**Resultado con los datos de prueba** (8 fila(s)):

| id_consulta | temperatura_c | diagnostico |
|---|---|---|
| 3 | 39.5 | Esguince leve |
| 7 | 39.5 | Paciente sano |
| 27 | 40.5 | Gastroenteritis aguda |
| 32 | 39.5 | Infestación por pulgas |
| 37 | 39.5 | Paciente sano |
| 59 | 39.5 | Gastroenteritis aguda |
| 72 | 39.6 | Esguince leve |
| 82 | 39.5 | Laceración contaminada |

Table: Resultado del ejercicio 9

##### Ejercicio 10. Categorías de servicio

**Enunciado.** Obtener las categorías de servicio existentes. La proyección del álgebra elimina duplicados; en SQL se requiere DISTINCT.

**Álgebra relacional:**

$$\pi_{\text{categoria}}(\text{SERVICIO})$$

**SQL (MySQL):**

```sql
SELECT DISTINCT categoria
FROM SERVICIO;
```

**Resultado con los datos de prueba** (7 fila(s)):

| categoria |
|---|
| CONSULTA |
| VACUNACION |
| CIRUGIA |
| LABORATORIO |
| ESTETICA |
| IMAGENOLOGIA |
| HOSPITALIZACION |

Table: Resultado del ejercicio 10

##### Ejercicio 11. Agenda futura

**Enunciado.** Listar las citas programadas desde el 9 de octubre de 2026 (selección por fecha).

**Álgebra relacional:**

$$\pi_{\text{id\_cita, fecha\_hora, id\_veterinario, id\_sede}}(\sigma_{\text{estado = 'PROGRAMADA' ∧ fecha\_hora ≥ '2026-10-09'}}(\text{CITA}))$$

**SQL (MySQL):**

```sql
SELECT id_cita, fecha_hora, id_veterinario, id_sede
FROM CITA
WHERE estado = 'PROGRAMADA' AND fecha_hora >= DATE '2026-10-09';
```

**Resultado con los datos de prueba** (18 fila(s); se muestran las primeras 8):

| id_cita | fecha_hora | id_veterinario | id_sede |
|---|---|---|---|
| 127 | 2026-10-10 09:00:00 | 1 | 1 |
| 128 | 2026-10-19 15:30:00 | 24 | 5 |
| 129 | 2026-10-19 17:30:00 | 8 | 2 |
| 130 | 2026-10-22 13:00:00 | 16 | 3 |
| 131 | 2026-10-26 17:30:00 | 15 | 3 |
| 132 | 2026-10-27 11:00:00 | 9 | 2 |
| 133 | 2026-10-28 10:00:00 | 19 | 4 |
| 134 | 2026-10-31 09:30:00 | 2 | 1 |

Table: Resultado del ejercicio 11

##### Ejercicio 12. Inventario bajo el mínimo

**Enunciado.** Encontrar los registros de inventario cuyo stock actual es menor que el stock mínimo (comparación entre dos atributos de la misma tupla).

**Álgebra relacional:**

$$\pi_{\text{id\_sede, id\_medicamento, stock\_actual, stock\_minimo}}(\sigma_{\text{stock\_actual < stock\_minimo}}(\text{INVENTARIO}))$$

**SQL (MySQL):**

```sql
SELECT id_sede, id_medicamento, stock_actual, stock_minimo
FROM INVENTARIO
WHERE stock_actual < stock_minimo;
```

**Resultado con los datos de prueba** (19 fila(s); se muestran las primeras 8):

| id_sede | id_medicamento | stock_actual | stock_minimo |
|---|---|---|---|
| 1 | 2 | 13 | 40 |
| 1 | 4 | 18 | 20 |
| 1 | 8 | 34 | 40 |
| 2 | 1 | 13 | 30 |
| 2 | 2 | 16 | 20 |
| 2 | 6 | 21 | 40 |
| 2 | 7 | 2 | 30 |
| 2 | 8 | 1 | 15 |

Table: Resultado del ejercicio 12

#### Producto cartesiano (×)

##### Ejercicio 13. Tabla de precios de vacunación por especie

**Enunciado.** Generar todas las combinaciones entre especies y servicios de vacunación para construir una tabla de precios (producto cartesiano).

**Álgebra relacional:**

$$\text{T1}\; \leftarrow \;\pi_{\text{id\_especie, nombre}}(\text{ESPECIE})$$

$$\text{T2}\; \leftarrow \;\pi_{\text{nombre, precio}}(\sigma_{\text{categoria = 'VACUNACION'}}(\text{SERVICIO}))$$

$$\text{T1}\; \times \;\text{T2}$$

**SQL (MySQL):**

```sql
SELECT e.id_especie, e.nombre AS especie, s.nombre AS servicio, s.precio
FROM ESPECIE e CROSS JOIN SERVICIO s
WHERE s.categoria = 'VACUNACION';
```

**Resultado con los datos de prueba** (15 fila(s); se muestran las primeras 8):

| id_especie | especie | servicio | precio |
|---|---|---|---|
| 1 | Canino | Vacuna antirrábica | 45000 |
| 1 | Canino | Vacuna múltiple canina | 75000 |
| 1 | Canino | Vacuna triple felina | 70000 |
| 2 | Felino | Vacuna antirrábica | 45000 |
| 2 | Felino | Vacuna múltiple canina | 75000 |
| 2 | Felino | Vacuna triple felina | 70000 |
| 3 | Ave | Vacuna antirrábica | 45000 |
| 3 | Ave | Vacuna múltiple canina | 75000 |

Table: Resultado del ejercicio 13

##### Ejercicio 14. Razas felinas mediante producto y selección

**Enunciado.** Obtener el nombre de cada raza felina junto al nombre de su especie, usando producto cartesiano seguido de selección (equivalente a una reunión).

**Álgebra relacional:**

$$\pi_{\text{RAZA.nombre, ESPECIE.nombre}}(\sigma_{\text{RAZA.id\_especie = ESPECIE.id\_especie ∧ ESPECIE.nombre = 'Felino'}}(\text{RAZA}\; \times \;\text{ESPECIE}))$$

**SQL (MySQL):**

```sql
SELECT r.nombre AS raza, e.nombre AS especie
FROM RAZA r, ESPECIE e
WHERE r.id_especie = e.id_especie AND e.nombre = 'Felino';
```

**Resultado con los datos de prueba** (5 fila(s)):

| raza | especie |
|---|---|
| Angora | Felino |
| Criollo | Felino |
| Maine Coon | Felino |
| Persa | Felino |
| Siamés | Felino |

Table: Resultado del ejercicio 14

#### Renombramiento (ρ)

##### Ejercicio 15. Veterinarios graduados el mismo año

**Enunciado.** Encontrar las parejas de veterinarios que se graduaron el mismo año. Se necesita renombrar la relación para compararla consigo misma.

**Álgebra relacional:**

$$\text{V1}\; \leftarrow \;\rho_{\text{V1}}(\text{VETERINARIO})$$

$$\text{V2}\; \leftarrow \;\rho_{\text{V2}}(\text{VETERINARIO})$$

$$\pi_{\text{V1.id\_empleado, V2.id\_empleado, V1.anio\_graduacion}}(\sigma_{\text{V1.anio\_graduacion = V2.anio\_graduacion ∧ V1.id\_empleado < V2.id\_empleado}}(\text{V1}\; \times \;\text{V2}))$$

**SQL (MySQL):**

```sql
SELECT v1.id_empleado AS veterinario_1, v2.id_empleado AS veterinario_2, v1.anio_graduacion
FROM VETERINARIO v1 JOIN VETERINARIO v2
  ON v1.anio_graduacion = v2.anio_graduacion AND v1.id_empleado < v2.id_empleado;
```

**Resultado con los datos de prueba** (6 fila(s)):

| veterinario_1 | veterinario_2 | anio_graduacion |
|---|---|---|
| 1 | 15 | 2006 |
| 2 | 25 | 2017 |
| 3 | 19 | 2015 |
| 9 | 10 | 2019 |
| 9 | 16 | 2019 |
| 10 | 16 | 2019 |

Table: Resultado del ejercicio 15

##### Ejercicio 16. Ciudades con su departamento

**Enunciado.** Listar cada ciudad con el nombre de su departamento. Ambas relaciones tienen un atributo «nombre», por lo que se renombran los atributos antes de la reunión natural.

**Álgebra relacional:**

$$\text{C}\; \leftarrow \;\rho_{\text{C(id\_ciudad, ciudad, id\_departamento)}}(\pi_{\text{id\_ciudad, nombre, id\_departamento}}(\text{CIUDAD}))$$

$$\text{D}\; \leftarrow \;\rho_{\text{D(id\_departamento, departamento)}}(\pi_{\text{id\_departamento, nombre}}(\text{DEPARTAMENTO}))$$

$$\pi_{\text{id\_ciudad, ciudad, departamento}}(\text{C}\; \bowtie \;\text{D})$$

**SQL (MySQL):**

```sql
SELECT c.id_ciudad, c.nombre AS ciudad, d.nombre AS departamento
FROM CIUDAD c JOIN DEPARTAMENTO d USING (id_departamento);
```

**Resultado con los datos de prueba** (11 fila(s); se muestran las primeras 8):

| id_ciudad | ciudad | departamento |
|---|---|---|
| 9 | Barranquilla | Atlántico |
| 1 | Bogotá D.C. | Bogotá D.C. |
| 10 | Bucaramanga | Santander |
| 7 | Cali | Valle del Cauca |
| 2 | Chía | Cundinamarca |
| 6 | Envigado | Antioquia |
| 11 | Floridablanca | Santander |
| 5 | Medellín | Antioquia |

Table: Resultado del ejercicio 16

#### Reunión natural y reunión theta (⋈)

##### Ejercicio 17. Datos completos de los veterinarios

**Enunciado.** Mostrar nombres, apellidos, tarjeta profesional y universidad de los veterinarios (reunión natural entre el subtipo y el supertipo por id_empleado).

**Álgebra relacional:**

$$\pi_{\text{nombres, apellidos, tarjeta\_profesional, universidad}}(\text{VETERINARIO}\; \bowtie \;\text{EMPLEADO})$$

**SQL (MySQL):**

```sql
SELECT nombres, apellidos, tarjeta_profesional, universidad
FROM VETERINARIO NATURAL JOIN EMPLEADO;
```

**Resultado con los datos de prueba** (12 fila(s); se muestran las primeras 8):

| nombres | apellidos | tarjeta_profesional | universidad |
|---|---|---|---|
| Ángela Sara | López Ramírez | TP-20037 | Universidad de La Salle |
| Santiago Julián | Suárez Gómez | TP-20074 | Universidad CES |
| Alejandro David | Jiménez Restrepo | TP-20111 | Universidad Antonio Nariño |
| Julián Esteban | Silva Vargas | TP-20296 | Universidad Nacional de Colombia |
| Ricardo Esteban | Rojas Pérez | TP-20333 | Universidad de Antioquia |
| Andrés | Silva López | TP-20370 | Universidad de La Salle |
| Paula | López Vargas | TP-20555 | Universidad Nacional de Colombia |
| Andrea | López Ortiz | TP-20592 | Universidad de La Salle |

Table: Resultado del ejercicio 17

##### Ejercicio 18. Existencias de la sede Usaquén

**Enunciado.** Listar los medicamentos que maneja la sede 1 (Usaquén) con su presentación y stock actual.

**Álgebra relacional:**

$$\pi_{\text{nombre, presentacion, stock\_actual}}(\sigma_{\text{id\_sede = 1}}(\text{INVENTARIO})\; \bowtie \;\text{MEDICAMENTO})$$

**SQL (MySQL):**

```sql
SELECT nombre, presentacion, stock_actual
FROM (SELECT * FROM INVENTARIO WHERE id_sede = 1) i NATURAL JOIN MEDICAMENTO;
```

**Resultado con los datos de prueba** (16 fila(s); se muestran las primeras 8):

| nombre | presentacion | stock_actual |
|---|---|---|
| Amoxivet | Tableta 500 mg | 88 |
| Meloxivet | Suspensión oral 15 ml | 13 |
| Dewormin Plus | Tableta | 215 |
| Pulguex | Tableta masticable 500 mg | 18 |
| Dermacalm | Tableta 5 mg | 18 |
| Otovet | Gotas óticas 15 ml | 89 |
| Suero Oral Pet | Solución 500 ml | 34 |
| Tramavet | Gotas 10 ml | 74 |

Table: Resultado del ejercicio 18

##### Ejercicio 19. Mascotas con alergias

**Enunciado.** Obtener el nombre de la mascota, el número de historia clínica y las alergias registradas.

**Álgebra relacional:**

$$\pi_{\text{nombre, numero\_historia, alergias}}(\sigma_{\text{alergias ≠ NULO}}(\text{HISTORIA\_CLINICA})\; \bowtie \;\text{MASCOTA})$$

**SQL (MySQL):**

```sql
SELECT m.nombre, h.numero_historia, h.alergias
FROM HISTORIA_CLINICA h JOIN MASCOTA m ON m.id_mascota = h.id_mascota
WHERE h.alergias IS NOT NULL;
```

**Resultado con los datos de prueba** (33 fila(s); se muestran las primeras 8):

| nombre | numero_historia | alergias |
|---|---|---|
| Lucas | HC-000001 | Picadura de pulga |
| Simón | HC-000003 | Penicilina |
| Max | HC-000004 | Pollo |
| Mía | HC-000005 | Pollo |
| Toby | HC-000007 | Pollo |
| Rocky | HC-000008 | Penicilina |
| Milo | HC-000009 | Penicilina |
| Salem | HC-000010 | Picadura de pulga |

Table: Resultado del ejercicio 19

##### Ejercicio 20. Gatos y sus propietarios

**Enunciado.** Listar las mascotas felinas con su raza y el nombre de su propietario (reunión de tres relaciones).

**Álgebra relacional:**

$$\text{T1}\; \leftarrow \;\text{MASCOTA}\;\bowtie_{\text{MASCOTA.id\_raza = RAZA.id\_raza}}\;\sigma_{\text{id\_especie = 2}}(\text{RAZA})$$

$$\pi_{\text{MASCOTA.nombre, RAZA.nombre, CLIENTE.nombres, CLIENTE.apellidos}}(\text{T1}\;\bowtie_{\text{MASCOTA.id\_cliente = CLIENTE.id\_cliente}}\;\text{CLIENTE})$$

**SQL (MySQL):**

```sql
SELECT m.nombre AS mascota, r.nombre AS raza, c.nombres, c.apellidos
FROM MASCOTA m
  JOIN RAZA r ON r.id_raza = m.id_raza
  JOIN CLIENTE c ON c.id_cliente = m.id_cliente
WHERE r.id_especie = 2;
```

**Resultado con los datos de prueba** (14 fila(s); se muestran las primeras 8):

| mascota | raza | nombres | apellidos |
|---|---|---|---|
| Rocky | Maine Coon | Camila Laura | Quintero Cárdenas |
| Oreo | Maine Coon | Alejandro Daniel | López Rincón |
| Chispa | Angora | Alejandro Daniel | López Rincón |
| Rex | Angora | Laura | Ramírez Suárez |
| Kiwi | Angora | Mauricio | Torres Castro |
| Coco | Criollo | Mateo Daniel | Torres Rodríguez |
| Copito | Angora | Ángela Natalia | Silva Torres |
| Coco | Criollo | Camilo | Rincón Quintero |

Table: Resultado del ejercicio 20

##### Ejercicio 21. Sedes, ubicación y director

**Enunciado.** Mostrar cada sede con su ciudad, su departamento y el nombre de su director (reunión por la llave foránea de rol id_director).

**Álgebra relacional:**

$$\text{T1}\; \leftarrow \;\text{SEDE}\;\bowtie_{\text{SEDE.id\_ciudad = CIUDAD.id\_ciudad}}\;\text{CIUDAD}\;\bowtie_{\text{CIUDAD.id\_departamento = DEPARTAMENTO.id\_departamento}}\;\text{DEPARTAMENTO}$$

$$\pi_{\text{SEDE.nombre, CIUDAD.nombre, DEPARTAMENTO.nombre, EMPLEADO.nombres, EMPLEADO.apellidos}}(\text{T1}\;\bowtie_{\text{SEDE.id\_director = EMPLEADO.id\_empleado}}\;\text{EMPLEADO})$$

**SQL (MySQL):**

```sql
SELECT s.nombre AS sede, c.nombre AS ciudad, d.nombre AS departamento, e.nombres, e.apellidos
FROM SEDE s
  JOIN CIUDAD c ON c.id_ciudad = s.id_ciudad
  JOIN DEPARTAMENTO d ON d.id_departamento = c.id_departamento
  JOIN EMPLEADO e ON e.id_empleado = s.id_director;
```

**Resultado con los datos de prueba** (5 fila(s)):

| sede | ciudad | departamento | nombres | apellidos |
|---|---|---|---|---|
| Huella Sana Usaquén | Bogotá D.C. | Bogotá D.C. | Felipe Andrés | Jiménez Ospina |
| Huella Sana Chapinero | Bogotá D.C. | Bogotá D.C. | Julián Esteban | Silva Vargas |
| Huella Sana Chía | Chía | Cundinamarca | Andrés Ricardo | Restrepo Pérez |
| Huella Sana Laureles | Medellín | Antioquia | Ángela | Hernández Vargas |
| Huella Sana San Fernando | Cali | Valle del Cauca | Mateo | Rojas Rodríguez |

Table: Resultado del ejercicio 21

##### Ejercicio 22. Especialidades de cada veterinario

**Enunciado.** Listar los veterinarios con cada especialidad certificada y su fecha (reunión a través de la tabla de la relación N:M).

**Álgebra relacional:**

$$\pi_{\text{nombres, apellidos, nombre, fecha\_certificacion}}(\text{EMPLEADO}\; \bowtie \;\text{VETERINARIO\_ESPECIALIDAD}\; \bowtie \;\text{ESPECIALIDAD})$$

**SQL (MySQL):**

```sql
SELECT e.nombres, e.apellidos, es.nombre AS especialidad, ve.fecha_certificacion
FROM EMPLEADO e
  JOIN VETERINARIO_ESPECIALIDAD ve ON ve.id_empleado = e.id_empleado
  JOIN ESPECIALIDAD es ON es.id_especialidad = ve.id_especialidad;
```

**Resultado con los datos de prueba** (12 fila(s); se muestran las primeras 8):

| nombres | apellidos | especialidad | fecha_certificacion |
|---|---|---|---|
| Ángela Sara | López Ramírez | Medicina interna | 2018-08-29 |
| Ángela Sara | López Ramírez | Medicina felina | 2021-05-06 |
| Santiago Julián | Suárez Gómez | Dermatología | 2025-02-18 |
| Alejandro David | Jiménez Restrepo | Medicina interna | 2024-02-28 |
| Ricardo Esteban | Rojas Pérez | Dermatología | 2016-06-05 |
| Andrés | Silva López | Animales exóticos | 2014-07-11 |
| Paula | López Vargas | Cirugía | 2014-10-02 |
| Paula | López Vargas | Cardiología | 2014-08-26 |

Table: Resultado del ejercicio 22

##### Ejercicio 23. Fórmula médica de una consulta

**Enunciado.** Mostrar los medicamentos formulados en la consulta 4 con dosis, frecuencia y duración.

**Álgebra relacional:**

$$\pi_{\text{nombre, presentacion, dosis, frecuencia\_horas, duracion\_dias}}(\sigma_{\text{id\_consulta = 4}}(\text{PRESCRIPCION})\; \bowtie \;\text{MEDICAMENTO})$$

**SQL (MySQL):**

```sql
SELECT m.nombre, m.presentacion, p.dosis, p.frecuencia_horas, p.duracion_dias
FROM PRESCRIPCION p JOIN MEDICAMENTO m ON m.id_medicamento = p.id_medicamento
WHERE p.id_consulta = 4;
```

**Resultado con los datos de prueba** (3 fila(s)):

| nombre | presentacion | dosis | frecuencia_horas | duracion_dias |
|---|---|---|---|---|
| Gastrovet | Cápsula 20 mg | 1 unidad | 8 | 5 |
| Suero Oral Pet | Solución 500 ml | Según peso | 24 | 7 |
| Antiemet | Tableta 16 mg | 1 unidad | 24 | 7 |

Table: Resultado del ejercicio 23

##### Ejercicio 24. Historia clínica de una mascota

**Enunciado.** Reconstruir la historia de atenciones de la mascota 17: fecha de la consulta, veterinario y diagnóstico, en cualquier sede.

**Álgebra relacional:**

$$\text{T1}\; \leftarrow \;\text{CONSULTA}\;\bowtie_{\text{CONSULTA.id\_cita = CITA.id\_cita}}\;\sigma_{\text{id\_mascota = 17}}(\text{CITA})$$

$$\pi_{\text{fecha\_hora\_inicio, id\_sede, nombres, apellidos, diagnostico}}(\text{T1}\;\bowtie_{\text{CITA.id\_veterinario = EMPLEADO.id\_empleado}}\;\text{EMPLEADO})$$

**SQL (MySQL):**

```sql
SELECT k.fecha_hora_inicio, ci.id_sede, e.nombres, e.apellidos, k.diagnostico
FROM CONSULTA k
  JOIN CITA ci ON ci.id_cita = k.id_cita
  JOIN EMPLEADO e ON e.id_empleado = ci.id_veterinario
WHERE ci.id_mascota = 17
ORDER BY k.fecha_hora_inicio;
```

**Resultado con los datos de prueba** (4 fila(s)):

| fecha_hora_inicio | id_sede | nombres | apellidos | diagnostico |
|---|---|---|---|---|
| 2026-02-24 14:41:00 | 2 | Julián Esteban | Silva Vargas | Paciente sano |
| 2026-04-18 16:42:00 | 2 | Ricardo Esteban | Rojas Pérez | Sarna demodécica |
| 2026-07-04 11:08:00 | 2 | Ricardo Esteban | Rojas Pérez | Traqueobronquitis infecciosa |
| 2026-07-06 15:43:00 | 2 | Julián Esteban | Silva Vargas | Esguince leve |

Table: Resultado del ejercicio 24

##### Ejercicio 25. Citas atendidas en la sede Chía

**Enunciado.** Listar las citas atendidas en la sede 3 (Chía) con el nombre de la mascota y el apellido del veterinario.

**Álgebra relacional:**

$$\text{T1}\; \leftarrow \;\sigma_{\text{id\_sede = 3 ∧ estado = 'ATENDIDA'}}(\text{CITA})\;\bowtie_{\text{CITA.id\_mascota = MASCOTA.id\_mascota}}\;\text{MASCOTA}$$

$$\pi_{\text{id\_cita, fecha\_hora, MASCOTA.nombre, EMPLEADO.apellidos}}(\text{T1}\;\bowtie_{\text{CITA.id\_veterinario = EMPLEADO.id\_empleado}}\;\text{EMPLEADO})$$

**SQL (MySQL):**

```sql
SELECT ci.id_cita, ci.fecha_hora, m.nombre AS mascota, e.apellidos AS veterinario
FROM CITA ci
  JOIN MASCOTA m ON m.id_mascota = ci.id_mascota
  JOIN EMPLEADO e ON e.id_empleado = ci.id_veterinario
WHERE ci.id_sede = 3 AND ci.estado = 'ATENDIDA'
ORDER BY ci.fecha_hora;
```

**Resultado con los datos de prueba** (16 fila(s); se muestran las primeras 8):

| id_cita | fecha_hora | mascota | veterinario |
|---|---|---|---|
| 7 | 2025-10-15 15:30:00 | Simón | López Ortiz |
| 14 | 2025-11-03 08:00:00 | Rex | López Vargas |
| 23 | 2025-11-27 11:00:00 | Simón | López Vargas |
| 31 | 2025-12-12 12:30:00 | Kiwi | López Ortiz |
| 34 | 2025-12-27 10:00:00 | Salem | López Vargas |
| 47 | 2026-02-05 09:30:00 | Kiwi | López Vargas |
| 50 | 2026-02-17 11:00:00 | Luna | López Ortiz |
| 75 | 2026-04-30 17:00:00 | Luna | López Ortiz |

Table: Resultado del ejercicio 25

#### Reuniones externas (⟕, ⟖, ⟗)

##### Ejercicio 26. Clientes sin mascotas

**Enunciado.** Encontrar los clientes que no tienen mascotas, usando una reunión externa izquierda y seleccionando las tuplas completadas con nulos.

**Álgebra relacional:**

$$\text{T1}\; \leftarrow \;\text{CLIENTE}\;\mathbin{⟕}_{\text{CLIENTE.id\_cliente = MASCOTA.id\_cliente}}\;\text{MASCOTA}$$

$$\pi_{\text{CLIENTE.id\_cliente, nombres, apellidos}}(\sigma_{\text{MASCOTA.id\_mascota = NULO}}(\text{T1}))$$

**SQL (MySQL):**

```sql
SELECT c.id_cliente, c.nombres, c.apellidos
FROM CLIENTE c LEFT OUTER JOIN MASCOTA m ON m.id_cliente = c.id_cliente
WHERE m.id_mascota IS NULL;
```

**Resultado con los datos de prueba** (2 fila(s)):

| id_cliente | nombres | apellidos |
|---|---|---|
| 43 | Óscar David | Ramírez Pérez |
| 45 | Esteban Andrés | Pérez Restrepo |

Table: Resultado del ejercicio 26

##### Ejercicio 27. Especialidades con o sin veterinarios

**Enunciado.** Listar todas las especialidades con los veterinarios certificados; las especialidades sin veterinarios deben aparecer con nulos.

**Álgebra relacional:**

$$\pi_{\text{ESPECIALIDAD.nombre, id\_empleado}}(\text{ESPECIALIDAD}\;\mathbin{⟕}_{\text{ESPECIALIDAD.id\_especialidad = VETERINARIO\_ESPECIALIDAD.id\_especialidad}}\;\text{VETERINARIO\_ESPECIALIDAD})$$

**SQL (MySQL):**

```sql
SELECT es.nombre AS especialidad, ve.id_empleado
FROM ESPECIALIDAD es
  LEFT OUTER JOIN VETERINARIO_ESPECIALIDAD ve ON ve.id_especialidad = es.id_especialidad
ORDER BY es.nombre;
```

**Resultado con los datos de prueba** (14 fila(s); se muestran las primeras 8):

| especialidad | id_empleado |
|---|---|
| Animales exóticos | 10 |
| Cardiología | 15 |
| Cardiología | 20 |
| Cirugía | 15 |
| Dermatología | 2 |
| Dermatología | 9 |
| Dermatología | 20 |
| Medicina felina | 1 |

Table: Resultado del ejercicio 27

##### Ejercicio 28. Todas las citas de una mascota, con o sin consulta

**Enunciado.** Para la mascota 17, listar todas sus citas y, cuando exista, la consulta que generaron (reunión externa derecha: se conservan todas las citas).

**Álgebra relacional:**

$$\pi_{\text{CITA.id\_cita, estado, id\_consulta, diagnostico}}(\text{CONSULTA}\;\mathbin{⟖}_{\text{CONSULTA.id\_cita = CITA.id\_cita}}\;\sigma_{\text{id\_mascota = 17}}(\text{CITA}))$$

**SQL (MySQL):**

```sql
SELECT ci.id_cita, ci.estado, k.id_consulta, k.diagnostico
FROM CONSULTA k
  RIGHT OUTER JOIN (SELECT * FROM CITA WHERE id_mascota = 17) ci ON ci.id_cita = k.id_cita
ORDER BY ci.id_cita;
```

**Resultado con los datos de prueba** (4 fila(s)):

| id_cita | estado | id_consulta | diagnostico |
|---|---|---|---|
| 55 | ATENDIDA | 38 | Paciente sano |
| 67 | ATENDIDA | 45 | Sarna demodécica |
| 98 | ATENDIDA | 70 | Traqueobronquitis infecciosa |
| 100 | ATENDIDA | 72 | Esguince leve |

Table: Resultado del ejercicio 28

##### Ejercicio 29. Comparación de inventarios entre sedes

**Enunciado.** Comparar el inventario de la sede 1 (Usaquén) y la sede 3 (Chía) por medicamento, mostrando también los medicamentos que solo maneja una de las dos (reunión externa completa).

**Álgebra relacional:**

$$\text{A}\; \leftarrow \;\rho_{\text{A(id\_medicamento, stock\_usaquen)}}(\pi_{\text{id\_medicamento, stock\_actual}}(\sigma_{\text{id\_sede = 1}}(\text{INVENTARIO})))$$

$$\text{B}\; \leftarrow \;\rho_{\text{B(id\_medicamento, stock\_chia)}}(\pi_{\text{id\_medicamento, stock\_actual}}(\sigma_{\text{id\_sede = 3}}(\text{INVENTARIO})))$$

$$\text{A}\;\mathbin{⟗}_{\text{A.id\_medicamento = B.id\_medicamento}}\;\text{B}$$

**SQL (MySQL):**

```sql
SELECT a.id_medicamento AS med_usaquen, a.stock_actual AS stock_usaquen,
       b.id_medicamento AS med_chia, b.stock_actual AS stock_chia
FROM (SELECT * FROM INVENTARIO WHERE id_sede = 1) a
  LEFT JOIN (SELECT * FROM INVENTARIO WHERE id_sede = 3) b ON a.id_medicamento = b.id_medicamento
UNION
SELECT a.id_medicamento, a.stock_actual, b.id_medicamento, b.stock_actual
FROM (SELECT * FROM INVENTARIO WHERE id_sede = 1) a
  RIGHT JOIN (SELECT * FROM INVENTARIO WHERE id_sede = 3) b ON a.id_medicamento = b.id_medicamento
ORDER BY COALESCE(med_usaquen, med_chia);
```

**Resultado con los datos de prueba** (18 fila(s); se muestran las primeras 8):

| med_usaquen | stock_usaquen | med_chia | stock_chia |
|---|---|---|---|
| 1 | 88 | NULL | NULL |
| 2 | 13 | 2 | 0 |
| 3 | 215 | 3 | 42 |
| 4 | 18 | 4 | 3 |
| NULL | NULL | 5 | 38 |
| 6 | 18 | 6 | 79 |
| 7 | 89 | 7 | 196 |
| 8 | 34 | 8 | 202 |

Table: Resultado del ejercicio 29

#### Semirreunión (⋉) y antirreunión (▷)

##### Ejercicio 30. Clientes con facturas anuladas

**Enunciado.** Obtener los clientes que tienen al menos una factura anulada. La semirreunión conserva solo los atributos de CLIENTE.

**Álgebra relacional:**

$$\pi_{\text{id\_cliente, nombres, apellidos}}(\text{CLIENTE}\; \ltimes \;\sigma_{\text{estado = 'ANULADA'}}(\text{FACTURA}))$$

**SQL (MySQL):**

```sql
SELECT c.id_cliente, c.nombres, c.apellidos
FROM CLIENTE c
WHERE EXISTS (SELECT 1 FROM FACTURA f WHERE f.id_cliente = c.id_cliente AND f.estado = 'ANULADA');
```

**Resultado con los datos de prueba** (3 fila(s)):

| id_cliente | nombres | apellidos |
|---|---|---|
| 7 | Camila Laura | Quintero Cárdenas |
| 31 | Lorena Paula | Hernández Ramírez |
| 35 | Natalia Camila | Rincón Ramírez |

Table: Resultado del ejercicio 30

##### Ejercicio 31. Mascotas sin atenciones

**Enunciado.** Listar las mascotas activas que nunca han tenido una cita atendida (antirreunión).

**Álgebra relacional:**

$$\pi_{\text{id\_mascota, nombre}}(\sigma_{\text{estado = 'ACTIVA'}}(\text{MASCOTA})\; \triangleright \;\sigma_{\text{estado = 'ATENDIDA'}}(\text{CITA}))$$

**SQL (MySQL):**

```sql
SELECT m.id_mascota, m.nombre
FROM MASCOTA m
WHERE m.estado = 'ACTIVA'
  AND NOT EXISTS (SELECT 1 FROM CITA ci WHERE ci.id_mascota = m.id_mascota AND ci.estado = 'ATENDIDA');
```

**Resultado con los datos de prueba** (9 fila(s); se muestran las primeras 8):

| id_mascota | nombre |
|---|---|
| 6 | Salem |
| 9 | Milo |
| 12 | Milo |
| 26 | Rex |
| 30 | Kira |
| 34 | Simón |
| 39 | Coco |
| 42 | Maya |

Table: Resultado del ejercicio 31

##### Ejercicio 32. Veterinarios que atendieron urgencias

**Enunciado.** Obtener los veterinarios que tienen al menos una cita del servicio de urgencias (servicio 3), sin repetir veterinarios ni mostrar atributos de la cita (semirreunión).

**Álgebra relacional:**

$$\pi_{\text{id\_empleado, nombres, apellidos}}(\text{EMPLEADO}\;\ltimes_{\text{EMPLEADO.id\_empleado = CITA.id\_veterinario}}\;\sigma_{\text{id\_servicio = 3}}(\text{CITA}))$$

**SQL (MySQL):**

```sql
SELECT e.id_empleado, e.nombres, e.apellidos
FROM EMPLEADO e
WHERE EXISTS (SELECT 1 FROM CITA ci WHERE ci.id_veterinario = e.id_empleado AND ci.id_servicio = 3);
```

**Resultado con los datos de prueba** (6 fila(s)):

| id_empleado | nombres | apellidos |
|---|---|---|
| 8 | Julián Esteban | Silva Vargas |
| 9 | Ricardo Esteban | Rojas Pérez |
| 19 | Ángela | Hernández Vargas |
| 20 | María José | Rubio Castro |
| 24 | Camilo Santiago | Torres Cárdenas |
| 25 | Juliana Lorena | Rodríguez López |

Table: Resultado del ejercicio 32

#### Operaciones de conjuntos (∪, ∩, −)

##### Ejercicio 33. Personas registradas en Chía

**Enunciado.** Obtener nombres y apellidos de las personas relacionadas con Chía: clientes que viven allí o empleados de la sede Chía (unión de relaciones compatibles).

**Álgebra relacional:**

$$\pi_{\text{nombres, apellidos}}(\sigma_{\text{id\_ciudad = 2}}(\text{CLIENTE}))\; \cup \;\pi_{\text{nombres, apellidos}}(\sigma_{\text{id\_sede = 3}}(\text{EMPLEADO}))$$

**SQL (MySQL):**

```sql
SELECT nombres, apellidos FROM CLIENTE WHERE id_ciudad = 2
UNION
SELECT nombres, apellidos FROM EMPLEADO WHERE id_sede = 3;
```

**Resultado con los datos de prueba** (9 fila(s); se muestran las primeras 8):

| nombres | apellidos |
|---|---|
| Andrea | López Ortiz |
| Andrés Ricardo | Restrepo Pérez |
| Laura | Ramírez Suárez |
| Mateo | Pérez Silva |
| Mateo Daniel | Torres Rodríguez |
| Mauricio | Torres Castro |
| Paula | López Vargas |
| Ricardo | Jiménez Salazar |

Table: Resultado del ejercicio 33

##### Ejercicio 34. Mascotas vacunadas u operadas

**Enunciado.** Listar las mascotas que han tenido cita de vacunación o de cirugía.

**Álgebra relacional:**

$$\text{V}\; \leftarrow \;\pi_{\text{id\_mascota}}(\text{CITA}\; \bowtie \;\sigma_{\text{categoria = 'VACUNACION'}}(\text{SERVICIO}))$$

$$\text{Q}\; \leftarrow \;\pi_{\text{id\_mascota}}(\text{CITA}\; \bowtie \;\sigma_{\text{categoria = 'CIRUGIA'}}(\text{SERVICIO}))$$

$$\pi_{\text{id\_mascota, nombre}}((\text{V}\; \cup \;\text{Q})\; \bowtie \;\text{MASCOTA})$$

**SQL (MySQL):**

```sql
SELECT m.id_mascota, m.nombre
FROM MASCOTA m
WHERE m.id_mascota IN (
  SELECT ci.id_mascota FROM CITA ci JOIN SERVICIO s ON s.id_servicio = ci.id_servicio WHERE s.categoria = 'VACUNACION'
  UNION
  SELECT ci.id_mascota FROM CITA ci JOIN SERVICIO s ON s.id_servicio = ci.id_servicio WHERE s.categoria = 'CIRUGIA')
ORDER BY m.id_mascota;
```

**Resultado con los datos de prueba** (27 fila(s); se muestran las primeras 8):

| id_mascota | nombre |
|---|---|
| 2 | Coco |
| 4 | Max |
| 5 | Mía |
| 8 | Rocky |
| 12 | Milo |
| 14 | Mía |
| 15 | Oreo |
| 18 | Rocky |

Table: Resultado del ejercicio 34

##### Ejercicio 35. Medicamentos comunes a dos sedes

**Enunciado.** Encontrar los medicamentos que maneja la sede 1 (Usaquén) y también la sede 4 (Laureles).

**Álgebra relacional:**

$$\text{T}\; \leftarrow \;\pi_{\text{id\_medicamento}}(\sigma_{\text{id\_sede = 1}}(\text{INVENTARIO}))\; \cap \;\pi_{\text{id\_medicamento}}(\sigma_{\text{id\_sede = 4}}(\text{INVENTARIO}))$$

$$\pi_{\text{id\_medicamento, nombre}}(\text{T}\; \bowtie \;\text{MEDICAMENTO})$$

**SQL (MySQL):**

```sql
SELECT m.id_medicamento, m.nombre
FROM MEDICAMENTO m
WHERE m.id_medicamento IN (
  SELECT id_medicamento FROM INVENTARIO WHERE id_sede = 1
  INTERSECT
  SELECT id_medicamento FROM INVENTARIO WHERE id_sede = 4)
ORDER BY m.id_medicamento;
```

**Resultado con los datos de prueba** (14 fila(s); se muestran las primeras 8):

| id_medicamento | nombre |
|---|---|
| 1 | Amoxivet |
| 2 | Meloxivet |
| 4 | Pulguex |
| 6 | Dermacalm |
| 7 | Otovet |
| 8 | Suero Oral Pet |
| 9 | Tramavet |
| 10 | Metrovet |

Table: Resultado del ejercicio 35

##### Ejercicio 36. Clientes con perro y gato

**Enunciado.** Obtener los clientes que tienen al menos un canino y al menos un felino.

**Álgebra relacional:**

$$\text{P}\; \leftarrow \;\pi_{\text{id\_cliente}}(\text{MASCOTA}\; \bowtie \;\sigma_{\text{id\_especie = 1}}(\text{RAZA}))$$

$$\text{G}\; \leftarrow \;\pi_{\text{id\_cliente}}(\text{MASCOTA}\; \bowtie \;\sigma_{\text{id\_especie = 2}}(\text{RAZA}))$$

$$\pi_{\text{id\_cliente, nombres, apellidos}}((\text{P}\; \cap \;\text{G})\; \bowtie \;\text{CLIENTE})$$

**SQL (MySQL):**

```sql
SELECT c.id_cliente, c.nombres, c.apellidos
FROM CLIENTE c
WHERE c.id_cliente IN (
  SELECT m.id_cliente FROM MASCOTA m JOIN RAZA r ON r.id_raza = m.id_raza WHERE r.id_especie = 1
  INTERSECT
  SELECT m.id_cliente FROM MASCOTA m JOIN RAZA r ON r.id_raza = m.id_raza WHERE r.id_especie = 2);
```

**Resultado con los datos de prueba** (4 fila(s)):

| id_cliente | nombres | apellidos |
|---|---|---|
| 20 | Laura | Ramírez Suárez |
| 28 | Camilo | Rincón Quintero |
| 40 | Catalina | Rubio López |
| 44 | María José | Restrepo Silva |

Table: Resultado del ejercicio 36

##### Ejercicio 37. Medicamentos nunca formulados

**Enunciado.** Listar los medicamentos del catálogo que nunca se han formulado (diferencia).

**Álgebra relacional:**

$$\text{T}\; \leftarrow \;\pi_{\text{id\_medicamento}}(\text{MEDICAMENTO})\; - \;\pi_{\text{id\_medicamento}}(\text{PRESCRIPCION})$$

$$\pi_{\text{id\_medicamento, nombre, presentacion}}(\text{T}\; \bowtie \;\text{MEDICAMENTO})$$

**SQL (MySQL):**

```sql
SELECT m.id_medicamento, m.nombre, m.presentacion
FROM MEDICAMENTO m
WHERE m.id_medicamento IN (
  SELECT id_medicamento FROM MEDICAMENTO
  EXCEPT
  SELECT id_medicamento FROM PRESCRIPCION);
```

**Resultado con los datos de prueba** (1 fila(s)):

| id_medicamento | nombre | presentacion |
|---|---|---|
| 11 | Felinvit | Pasta oral 100 g |

Table: Resultado del ejercicio 37

##### Ejercicio 38. Veterinarios sin especialidad

**Enunciado.** Obtener los veterinarios que no tienen ninguna especialidad certificada.

**Álgebra relacional:**

$$\text{T}\; \leftarrow \;\pi_{\text{id\_empleado}}(\text{VETERINARIO})\; - \;\pi_{\text{id\_empleado}}(\text{VETERINARIO\_ESPECIALIDAD})$$

$$\pi_{\text{id\_empleado, nombres, apellidos}}(\text{T}\; \bowtie \;\text{EMPLEADO})$$

**SQL (MySQL):**

```sql
SELECT e.id_empleado, e.nombres, e.apellidos
FROM EMPLEADO e
WHERE e.id_empleado IN (
  SELECT id_empleado FROM VETERINARIO
  EXCEPT
  SELECT id_empleado FROM VETERINARIO_ESPECIALIDAD);
```

**Resultado con los datos de prueba** (3 fila(s)):

| id_empleado | nombres | apellidos |
|---|---|---|
| 8 | Julián Esteban | Silva Vargas |
| 16 | Andrea | López Ortiz |
| 25 | Juliana Lorena | Rodríguez López |

Table: Resultado del ejercicio 38

##### Ejercicio 39. Medicamentos que no maneja la sede Chía

**Enunciado.** Listar los medicamentos del catálogo que no están en el inventario de la sede 3 (Chía).

**Álgebra relacional:**

$$\text{T}\; \leftarrow \;\pi_{\text{id\_medicamento}}(\text{MEDICAMENTO})\; - \;\pi_{\text{id\_medicamento}}(\sigma_{\text{id\_sede = 3}}(\text{INVENTARIO}))$$

$$\pi_{\text{id\_medicamento, nombre}}(\text{T}\; \bowtie \;\text{MEDICAMENTO})$$

**SQL (MySQL):**

```sql
SELECT id_medicamento, nombre
FROM MEDICAMENTO
WHERE id_medicamento NOT IN (
  SELECT id_medicamento FROM INVENTARIO WHERE id_sede = 3);
```

**Resultado con los datos de prueba** (1 fila(s)):

| id_medicamento | nombre |
|---|---|
| 1 | Amoxivet |

Table: Resultado del ejercicio 39

##### Ejercicio 40. Clientes de mostrador

**Enunciado.** Encontrar los clientes que tienen facturas pero no tienen mascotas registradas (compras de mostrador).

**Álgebra relacional:**

$$\text{T}\; \leftarrow \;\pi_{\text{id\_cliente}}(\text{FACTURA})\; - \;\pi_{\text{id\_cliente}}(\text{MASCOTA})$$

$$\pi_{\text{id\_cliente, nombres, apellidos}}(\text{T}\; \bowtie \;\text{CLIENTE})$$

**SQL (MySQL):**

```sql
SELECT c.id_cliente, c.nombres, c.apellidos
FROM CLIENTE c
WHERE c.id_cliente IN (
  SELECT id_cliente FROM FACTURA
  EXCEPT
  SELECT id_cliente FROM MASCOTA);
```

**Resultado con los datos de prueba** (1 fila(s)):

| id_cliente | nombres | apellidos |
|---|---|---|
| 45 | Esteban Andrés | Pérez Restrepo |

Table: Resultado del ejercicio 40

#### División (÷)

##### Ejercicio 41. Medicamentos disponibles en todas las sedes

**Enunciado.** Encontrar los medicamentos que están en el inventario de todas las sedes.

**Álgebra relacional:**

$$\text{T}\; \leftarrow \;\pi_{\text{id\_medicamento, id\_sede}}(\text{INVENTARIO})\; \div \;\pi_{\text{id\_sede}}(\text{SEDE})$$

$$\pi_{\text{id\_medicamento, nombre}}(\text{T}\; \bowtie \;\text{MEDICAMENTO})$$

**SQL (MySQL):**

```sql
SELECT m.id_medicamento, m.nombre
FROM MEDICAMENTO m
WHERE NOT EXISTS (
  SELECT s.id_sede FROM SEDE s
  WHERE NOT EXISTS (
    SELECT 1 FROM INVENTARIO i WHERE i.id_sede = s.id_sede AND i.id_medicamento = m.id_medicamento));
```

**Resultado con los datos de prueba** (8 fila(s)):

| id_medicamento | nombre |
|---|---|
| 14 | Antiemet |
| 16 | Calmapet |
| 15 | Cefavet |
| 17 | Ivermec |
| 2 | Meloxivet |
| 7 | Otovet |
| 13 | Shampoo Clorhex |
| 8 | Suero Oral Pet |

Table: Resultado del ejercicio 41

##### Ejercicio 42. Veterinarios con un conjunto de especialidades

**Enunciado.** Encontrar los veterinarios certificados en todas las especialidades del conjunto {Medicina interna, Medicina felina}.

**Álgebra relacional:**

$$\text{E}\; \leftarrow \;\pi_{\text{id\_especialidad}}(\sigma_{\text{nombre = 'Medicina interna' ∨ nombre = 'Medicina felina'}}(\text{ESPECIALIDAD}))$$

$$\text{T}\; \leftarrow \;\pi_{\text{id\_empleado, id\_especialidad}}(\text{VETERINARIO\_ESPECIALIDAD})\; \div \;\text{E}$$

$$\pi_{\text{id\_empleado, nombres, apellidos}}(\text{T}\; \bowtie \;\text{EMPLEADO})$$

**SQL (MySQL):**

```sql
SELECT e.id_empleado, e.nombres, e.apellidos
FROM EMPLEADO e
WHERE e.id_empleado IN (SELECT id_empleado FROM VETERINARIO)
  AND NOT EXISTS (
    SELECT es.id_especialidad FROM ESPECIALIDAD es
    WHERE es.nombre IN ('Medicina interna', 'Medicina felina')
      AND NOT EXISTS (
        SELECT 1 FROM VETERINARIO_ESPECIALIDAD ve
        WHERE ve.id_empleado = e.id_empleado AND ve.id_especialidad = es.id_especialidad));
```

**Resultado con los datos de prueba** (1 fila(s)):

| id_empleado | nombres | apellidos |
|---|---|---|
| 1 | Ángela Sara | López Ramírez |

Table: Resultado del ejercicio 42

##### Ejercicio 43. Clientes atendidos en todas las sedes de Bogotá

**Enunciado.** Encontrar los clientes que tienen facturas en todas las sedes ubicadas en Bogotá D.C.

**Álgebra relacional:**

$$\text{B}\; \leftarrow \;\pi_{\text{id\_sede}}(\sigma_{\text{id\_ciudad = 1}}(\text{SEDE}))$$

$$\text{T}\; \leftarrow \;\pi_{\text{id\_cliente, id\_sede}}(\text{FACTURA})\; \div \;\text{B}$$

$$\pi_{\text{id\_cliente, nombres, apellidos}}(\text{T}\; \bowtie \;\text{CLIENTE})$$

**SQL (MySQL):**

```sql
SELECT c.id_cliente, c.nombres, c.apellidos
FROM CLIENTE c
WHERE NOT EXISTS (
  SELECT s.id_sede FROM SEDE s WHERE s.id_ciudad = 1
  AND NOT EXISTS (
    SELECT 1 FROM FACTURA f WHERE f.id_cliente = c.id_cliente AND f.id_sede = s.id_sede));
```

**Resultado con los datos de prueba** (6 fila(s)):

| id_cliente | nombres | apellidos |
|---|---|---|
| 1 | Alejandro | Ramírez Rubio |
| 2 | Mauricio David | Silva García |
| 4 | Juan | Ramírez Ospina |
| 8 | Natalia | Torres Díaz |
| 17 | Nicolás Santiago | Ortiz Ospina |
| 44 | María José | Restrepo Silva |

Table: Resultado del ejercicio 43

#### Agregación y agrupamiento (γ)

##### Ejercicio 44. Mascotas por especie

**Enunciado.** Contar cuántas mascotas hay de cada especie.

**Álgebra relacional:**

$$\text{T}\; \leftarrow \;\text{MASCOTA}\;\bowtie_{\text{MASCOTA.id\_raza = RAZA.id\_raza}}\;\text{RAZA}\;\bowtie_{\text{RAZA.id\_especie = ESPECIE.id\_especie}}\;\text{ESPECIE}$$

$${}_{\text{ESPECIE.nombre}}\gamma_{\text{COUNT(id\_mascota) → total}}(\text{T})$$

**SQL (MySQL):**

```sql
SELECT e.nombre AS especie, COUNT(m.id_mascota) AS total
FROM MASCOTA m
  JOIN RAZA r ON r.id_raza = m.id_raza
  JOIN ESPECIE e ON e.id_especie = r.id_especie
GROUP BY e.nombre
ORDER BY total DESC;
```

**Resultado con los datos de prueba** (5 fila(s)):

| especie | total |
|---|---|
| Canino | 42 |
| Felino | 14 |
| Hámster | 1 |
| Conejo | 1 |
| Ave | 1 |

Table: Resultado del ejercicio 44

##### Ejercicio 45. Citas por estado

**Enunciado.** Obtener el número de citas en cada estado.

**Álgebra relacional:**

$${}_{\text{estado}}\gamma_{\text{COUNT(id\_cita) → cantidad}}(\text{CITA})$$

**SQL (MySQL):**

```sql
SELECT estado, COUNT(id_cita) AS cantidad
FROM CITA
GROUP BY estado
ORDER BY cantidad DESC;
```

**Resultado con los datos de prueba** (4 fila(s)):

| estado | cantidad |
|---|---|
| ATENDIDA | 95 |
| NO_ASISTIO | 19 |
| PROGRAMADA | 18 |
| CANCELADA | 12 |

Table: Resultado del ejercicio 45

##### Ejercicio 46. Ingresos por sede

**Enunciado.** Calcular los ingresos de cada sede a partir de las facturas pagadas. El valor de cada línea es un atributo calculado (proyección generalizada).

**Álgebra relacional:**

$$\text{L}\; \leftarrow \;\pi_{\text{id\_sede, cantidad · precio\_unitario · (1 − descuento\_pct / 100) → valor}}(\sigma_{\text{estado = 'PAGADA'}}(\text{FACTURA})\; \bowtie \;\text{DETALLE\_FACTURA})$$

$$\pi_{\text{nombre, ingresos}}({}_{\text{id\_sede}}\gamma_{\text{SUM(valor) → ingresos}}(\text{L})\; \bowtie \;\text{SEDE})$$

**SQL (MySQL):**

```sql
SELECT s.nombre AS sede,
       ROUND(SUM(d.cantidad * d.precio_unitario * (1 - d.descuento_pct / 100.0)), 2) AS ingresos
FROM FACTURA f
  JOIN DETALLE_FACTURA d ON d.id_factura = f.id_factura
  JOIN SEDE s ON s.id_sede = f.id_sede
WHERE f.estado = 'PAGADA'
GROUP BY s.nombre
ORDER BY ingresos DESC;
```

**Resultado con los datos de prueba** (5 fila(s)):

| sede | ingresos |
|---|---|
| Huella Sana Laureles | 3660850 |
| Huella Sana Chapinero | 3163750 |
| Huella Sana Usaquén | 2327250 |
| Huella Sana San Fernando | 1894900 |
| Huella Sana Chía | 1734300 |

Table: Resultado del ejercicio 46

##### Ejercicio 47. Salarios por tipo de empleado

**Enunciado.** Calcular el salario promedio, mínimo y máximo por tipo de empleado.

**Álgebra relacional:**

$${}_{\text{tipo\_empleado}}\gamma_{\text{AVG(salario) → promedio, MIN(salario) → minimo, MAX(salario) → maximo}}(\text{EMPLEADO})$$

**SQL (MySQL):**

```sql
SELECT tipo_empleado, ROUND(AVG(salario), 0) AS promedio, MIN(salario) AS minimo, MAX(salario) AS maximo
FROM EMPLEADO
GROUP BY tipo_empleado
ORDER BY tipo_empleado;
```

**Resultado con los datos de prueba** (3 fila(s)):

| tipo_empleado | promedio | minimo | maximo |
|---|---|---|---|
| ADM | 3869000 | 2423000 | 5200000 |
| AUX | 2220375 | 2069000 | 2389000 |
| VET | 5631500 | 4237000 | 6483000 |

Table: Resultado del ejercicio 47

##### Ejercicio 48. Veterinario con más citas atendidas

**Enunciado.** Encontrar el o los veterinarios con el mayor número de citas atendidas (agregación seguida de selección con el máximo).

**Álgebra relacional:**

$$\text{C}\; \leftarrow \;{}_{\text{id\_veterinario}}\gamma_{\text{COUNT(id\_cita) → atendidas}}(\sigma_{\text{estado = 'ATENDIDA'}}(\text{CITA}))$$

$$\text{M}\; \leftarrow \;\gamma_{\text{MAX(atendidas) → maximo}}(\text{C})$$

$$\pi_{\text{nombres, apellidos, atendidas}}(\sigma_{\text{atendidas = maximo}}(\text{C}\; \times \;\text{M})\;\bowtie_{\text{id\_veterinario = id\_empleado}}\;\text{EMPLEADO})$$

**SQL (MySQL):**

```sql
SELECT e.nombres, e.apellidos, COUNT(ci.id_cita) AS atendidas
FROM CITA ci JOIN EMPLEADO e ON e.id_empleado = ci.id_veterinario
WHERE ci.estado = 'ATENDIDA'
GROUP BY e.id_empleado, e.nombres, e.apellidos
HAVING COUNT(ci.id_cita) = (
  SELECT MAX(n) FROM (SELECT COUNT(*) AS n FROM CITA WHERE estado = 'ATENDIDA' GROUP BY id_veterinario) t);
```

**Resultado con los datos de prueba** (1 fila(s)):

| nombres | apellidos | atendidas |
|---|---|---|
| María José | Rubio Castro | 12 |

Table: Resultado del ejercicio 48

##### Ejercicio 49. Medicamentos más formulados

**Enunciado.** Obtener las unidades formuladas de cada medicamento, de mayor a menor (se muestran los 5 primeros).

**Álgebra relacional:**

$${}_{\text{nombre}}\gamma_{\text{SUM(cantidad) → unidades}}(\text{PRESCRIPCION}\; \bowtie \;\text{MEDICAMENTO})$$

**SQL (MySQL):**

```sql
SELECT m.nombre, SUM(p.cantidad) AS unidades
FROM PRESCRIPCION p JOIN MEDICAMENTO m ON m.id_medicamento = p.id_medicamento
GROUP BY m.nombre
ORDER BY unidades DESC
LIMIT 5;
```

**Resultado con los datos de prueba** (5 fila(s)):

| nombre | unidades |
|---|---|
| Gastrovet | 108 |
| Amoxivet | 91 |
| Dermacalm | 78 |
| Antiemet | 74 |
| Cefavet | 48 |

Table: Resultado del ejercicio 49

##### Ejercicio 50. Clientes con más de dos mascotas

**Enunciado.** Listar los clientes que tienen más de dos mascotas registradas (selección sobre un resultado agregado).

**Álgebra relacional:**

$$\text{T}\; \leftarrow \;{}_{\text{id\_cliente}}\gamma_{\text{COUNT(id\_mascota) → mascotas}}(\text{MASCOTA})$$

$$\pi_{\text{id\_cliente, nombres, apellidos, mascotas}}(\sigma_{\text{mascotas > 2}}(\text{T})\; \bowtie \;\text{CLIENTE})$$

**SQL (MySQL):**

```sql
SELECT c.id_cliente, c.nombres, c.apellidos, COUNT(m.id_mascota) AS mascotas
FROM CLIENTE c JOIN MASCOTA m ON m.id_cliente = c.id_cliente
GROUP BY c.id_cliente, c.nombres, c.apellidos
HAVING COUNT(m.id_mascota) > 2;
```

**Resultado con los datos de prueba** (5 fila(s)):

| id_cliente | nombres | apellidos | mascotas |
|---|---|---|---|
| 8 | Natalia | Torres Díaz | 3 |
| 16 | Mauricio | Cárdenas Suárez | 3 |
| 24 | Esteban Julián | Mejía Martínez | 3 |
| 32 | Óscar | Rojas Quintero | 3 |
| 40 | Catalina | Rubio López | 3 |

Table: Resultado del ejercicio 50

##### Ejercicio 51. Facturación mensual de 2026

**Enunciado.** Calcular el valor facturado (facturas pagadas) por mes durante 2026.

**Álgebra relacional:**

$$\text{L}\; \leftarrow \;\pi_{\text{mes(fecha\_emision) → mes, cantidad · precio\_unitario · (1 − descuento\_pct / 100) → valor}}(\sigma_{\text{estado = 'PAGADA' ∧ año(fecha\_emision) = 2026}}(\text{FACTURA})\; \bowtie \;\text{DETALLE\_FACTURA})$$

$${}_{\text{mes}}\gamma_{\text{SUM(valor) → facturado, COUNT(DISTINCT id\_factura) → facturas}}(\text{L})$$

**SQL (MySQL):**

```sql
SELECT DATE_FORMAT(f.fecha_emision, '%Y-%m') AS mes,
       ROUND(SUM(d.cantidad * d.precio_unitario * (1 - d.descuento_pct / 100.0)), 2) AS facturado,
       COUNT(DISTINCT f.id_factura) AS facturas
FROM FACTURA f JOIN DETALLE_FACTURA d ON d.id_factura = f.id_factura
WHERE f.estado = 'PAGADA' AND EXTRACT(YEAR FROM f.fecha_emision) = 2026
GROUP BY DATE_FORMAT(f.fecha_emision, '%Y-%m')
ORDER BY mes;
```

**Resultado con los datos de prueba** (10 fila(s); se muestran las primeras 8):

| mes | facturado | facturas |
|---|---|---|
| 2026-01 | 382000 | 4 |
| 2026-02 | 1106450 | 9 |
| 2026-03 | 973800 | 8 |
| 2026-04 | 1255750 | 8 |
| 2026-05 | 437500 | 3 |
| 2026-06 | 2311400 | 15 |
| 2026-07 | 705700 | 8 |
| 2026-08 | 932300 | 12 |

Table: Resultado del ejercicio 51

##### Ejercicio 52. Valor del inventario por sede

**Enunciado.** Calcular el valor del inventario de cada sede (existencias × precio de venta).

**Álgebra relacional:**

$$\text{L}\; \leftarrow \;\pi_{\text{id\_sede, stock\_actual · precio\_venta → valor}}(\text{INVENTARIO}\; \bowtie \;\text{MEDICAMENTO})$$

$$\pi_{\text{nombre, valor\_inventario}}({}_{\text{id\_sede}}\gamma_{\text{SUM(valor) → valor\_inventario}}(\text{L})\; \bowtie \;\text{SEDE})$$

**SQL (MySQL):**

```sql
SELECT s.nombre AS sede, SUM(i.stock_actual * m.precio_venta) AS valor_inventario
FROM INVENTARIO i
  JOIN MEDICAMENTO m ON m.id_medicamento = i.id_medicamento
  JOIN SEDE s ON s.id_sede = i.id_sede
GROUP BY s.nombre
ORDER BY valor_inventario DESC;
```

**Resultado con los datos de prueba** (5 fila(s)):

| sede | valor_inventario |
|---|---|
| Huella Sana Laureles | 39824000 |
| Huella Sana Chapinero | 28093800 |
| Huella Sana Chía | 25360100 |
| Huella Sana San Fernando | 23178300 |
| Huella Sana Usaquén | 21818700 |

Table: Resultado del ejercicio 52

##### Ejercicio 53. Edad promedio por especie

**Enunciado.** Calcular la edad promedio (en años) de las mascotas activas por especie al 9 de octubre de 2026. La edad es un atributo derivado de fecha_nacimiento.

**Álgebra relacional:**

$$\text{L}\; \leftarrow \;\pi_{\text{id\_especie, ('2026-10-09' − fecha\_nacimiento) / 365.25 → edad}}(\sigma_{\text{estado = 'ACTIVA' ∧ fecha\_nacimiento ≠ NULO}}(\text{MASCOTA})\; \bowtie \;\text{RAZA})$$

$$\pi_{\text{nombre, edad\_promedio}}({}_{\text{id\_especie}}\gamma_{\text{AVG(edad) → edad\_promedio}}(\text{L})\; \bowtie \;\text{ESPECIE})$$

**SQL (MySQL):**

```sql
SELECT e.nombre AS especie, ROUND(AVG(DATEDIFF(DATE '2026-10-09', m.fecha_nacimiento) / 365.25), 1) AS edad_promedio
FROM MASCOTA m
  JOIN RAZA r ON r.id_raza = m.id_raza
  JOIN ESPECIE e ON e.id_especie = r.id_especie
WHERE m.estado = 'ACTIVA' AND m.fecha_nacimiento IS NOT NULL
GROUP BY e.nombre
ORDER BY edad_promedio DESC;
```

**Resultado con los datos de prueba** (5 fila(s)):

| especie | edad_promedio |
|---|---|
| Ave | 9.9 |
| Canino | 9.4 |
| Felino | 9.2 |
| Hámster | 3.6 |
| Conejo | 2.8 |

Table: Resultado del ejercicio 53

##### Ejercicio 54. Ticket promedio por medio de pago

**Enunciado.** Calcular el valor promedio de las facturas pagadas según el medio de pago.

**Álgebra relacional:**

$$\text{T}\; \leftarrow \;{}_{\text{id\_factura, metodo\_pago}}\gamma_{\text{SUM(cantidad · precio\_unitario · (1 − descuento\_pct / 100)) → total}}(\sigma_{\text{estado = 'PAGADA'}}(\text{FACTURA})\; \bowtie \;\text{DETALLE\_FACTURA})$$

$${}_{\text{metodo\_pago}}\gamma_{\text{AVG(total) → ticket\_promedio, COUNT(id\_factura) → facturas}}(\text{T})$$

**SQL (MySQL):**

```sql
SELECT metodo_pago, ROUND(AVG(total), 0) AS ticket_promedio, COUNT(*) AS facturas
FROM (
  SELECT f.id_factura, f.metodo_pago,
         SUM(d.cantidad * d.precio_unitario * (1 - d.descuento_pct / 100.0)) AS total
  FROM FACTURA f JOIN DETALLE_FACTURA d ON d.id_factura = f.id_factura
  WHERE f.estado = 'PAGADA'
  GROUP BY f.id_factura, f.metodo_pago) t
GROUP BY metodo_pago
ORDER BY ticket_promedio DESC;
```

**Resultado con los datos de prueba** (4 fila(s)):

| metodo_pago | ticket_promedio | facturas |
|---|---|---|
| TARJETA_CREDITO | 161900 | 13 |
| EFECTIVO | 128061 | 40 |
| TARJETA_DEBITO | 118681 | 27 |
| TRANSFERENCIA | 106795 | 22 |

Table: Resultado del ejercicio 54

##### Ejercicio 55. Servicios que más ingresos generan

**Enunciado.** Obtener los 5 servicios con mayores ingresos en facturas pagadas, con el número de veces que se cobraron.

**Álgebra relacional:**

$$\text{L}\; \leftarrow \;\pi_{\text{id\_servicio, cantidad · precio\_unitario · (1 − descuento\_pct / 100) → valor}}(\sigma_{\text{estado = 'PAGADA'}}(\text{FACTURA})\; \bowtie \;\sigma_{\text{id\_servicio ≠ NULO}}(\text{DETALLE\_FACTURA}))$$

$$\pi_{\text{nombre, veces, ingresos}}({}_{\text{id\_servicio}}\gamma_{\text{COUNT(id\_servicio) → veces, SUM(valor) → ingresos}}(\text{L})\; \bowtie \;\text{SERVICIO})$$

**SQL (MySQL):**

```sql
SELECT s.nombre AS servicio, COUNT(*) AS veces,
       ROUND(SUM(d.cantidad * d.precio_unitario * (1 - d.descuento_pct / 100.0)), 2) AS ingresos
FROM FACTURA f
  JOIN DETALLE_FACTURA d ON d.id_factura = f.id_factura
  JOIN SERVICIO s ON s.id_servicio = d.id_servicio
WHERE f.estado = 'PAGADA'
GROUP BY s.nombre
ORDER BY ingresos DESC
LIMIT 5;
```

**Resultado con los datos de prueba** (5 fila(s)):

| servicio | veces | ingresos |
|---|---|---|
| Consulta general | 38 | 2421250 |
| Esterilización | 5 | 1805000 |
| Consulta especializada | 8 | 836000 |
| Consulta de urgencias | 5 | 686000 |
| Ecografía abdominal | 3 | 442500 |

Table: Resultado del ejercicio 55


### Conclusiones

- Las 55 expresiones del álgebra relacional tienen una sentencia SQL equivalente y todas producen resultados sobre los
  datos de prueba, lo que valida el modelo: las llaves foráneas permiten reconstruir cualquier relación del MER.
- La proyección del álgebra elimina duplicados, mientras que en SQL se necesita `DISTINCT` (ejercicio 10).
- Las reuniones externas y la antirreunión responden preguntas sobre "lo que falta" (clientes sin mascotas, mascotas
  sin atenciones) que la reunión interna no puede responder.
- La división no tiene un operador directo en SQL; se implementa con doble `NOT EXISTS` (ejercicios 41 a 43).
- Los atributos derivados del MER (edad, totales de factura) se obtienen con proyección generalizada y agregación, sin
  necesidad de almacenarlos, lo que mantiene el esquema en 3FN.

# Parte II. Fase 2: implementación


## Documento de Análisis de selección de Sistema Operativo



### Introducción y objetivo

Se selecciona **Oracle Linux 9 (x86_64)** como sistema operativo de la máquina virtual de SIGEVET. Ofrece MySQL 8.4 LTS en AppStream mediante el módulo `mysql:8.4`, no exige costo de licencia y coincide con la sugerencia de Bases de Datos 1 de la Universidad El Bosque. El objetivo del análisis es justificar la decisión mediante requisitos verificables y una comparación ponderada de seis alternativas Linux y Unix. [1]

SIGEVET es el Sistema Integral de Gestión Veterinaria de la organización ficticia Huella Sana S.A.S., una red de cinco clínicas en Usaquén, Chapinero, Chía, Laureles y San Fernando. Su modelo comprende 22 tablas para sedes, personal, propietarios, mascotas, atención clínica, inventarios y facturación. La aplicación web, desarrollada en Java con Servlets y JSP sobre Tomcat, será exclusivamente de consulta. El alcance es un laboratorio académico virtualizado con datos sintéticos.

### Requisitos y restricciones

#### Condiciones académicas y funcionales

La asignatura admite únicamente Linux o Unix para el sistema operativo invitado y sugiere expresamente Oracle Linux. Windows y macOS quedan fuera de la selección del invitado; su uso como anfitriones es independiente de esta restricción. Se debe alojar un RDBMS relacional, ejecutar los scripts de las 22 tablas y demostrar integridad referencial, consultas y administración de usuarios. SQL Server está excluido de la selección del RDBMS.

Se conserva Oracle Linux 9 x86_64 y se adopta **MySQL 8.4 LTS Community Edition**, con la base `sigevet`, el propietario `vetdb` y el usuario de consulta `vet_app`. La aplicación Java usa JDBC con MySQL Connector/J y la administración gráfica se realiza desde MySQL Workbench en el anfitrión. La primera instalación con Oracle Database Free quedó como respaldo. La selección detallada del motor corresponde al análisis de RDBMS de esta fase.

#### Hardware y presupuesto de recursos

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

#### Virtualización: equipo servidor y ambiente de simulación

La VM definitiva se ejecutará con **KVM/QEMU** (por medio de Lima) en el computador del equipo que funcionará como servidor durante la demostración, un Lenovo con Linux sobre x86_64; en un anfitrión con Windows la alternativa es **Oracle VirtualBox 7**. La simulación en macOS usa **Lima 2.2.1 y Apple Virtualization.framework**, con tipo de VM `vz` y arquitectura `x86_64`. Este backend exige coincidencia de arquitectura entre anfitrión e invitado; el MacBook Intel cumple esa condición. [2]

Lima permite conservar una configuración declarativa para la simulación disponible. En el equipo servidor con Linux se usa el hipervisor del propio kernel (KVM), sin instalar software de terceros. La elección del hipervisor depende del anfitrión y no cambia la selección de Linux como sistema invitado. [2, 3]

En el **equipo servidor** se creará una VM Linux de 64 bits con un medio oficial de Oracle Linux 9, 4 vCPU, 6 GiB de RAM y 40 GiB de disco. Se podrá usar NAT con reenvío de puertos o red puente para acceder desde Workbench. Se debe habilitar la virtualización del procesador y verificar la compatibilidad con el hipervisor activo en Windows. En ambos ambientes se conservan la versión de MySQL, la base `sigevet`, los usuarios y los scripts; la guía corresponde al documento de implementación de la VM. [3]

### Alternativas evaluadas

#### Oracle Linux 9

Distribución empresarial compatible a nivel de aplicaciones con RHEL, disponible sin costo de descarga y uso, con licencias por componente y soporte comercial opcional. Ofrece Unbreakable Enterprise Kernel (UEK) y Red Hat Compatible Kernel (RHCK), repositorios públicos y un ciclo empresarial de mantenimiento. La descarga gratuita no incluye soporte contratado. [4, 5]

**Compatibilidad y ventajas:** MySQL 8.4 se instala desde AppStream con `dnf module install mysql:8.4`, sin descargar RPM por separado. La evidencia del proyecto registra `mysql-server` de la rama 8.4 y `mysql-selinux`; este último aporta la política SELinux para el servicio. `systemd` y `firewalld` permiten gestionar su arranque y el puerto 3306. **Desventajas:** el equipo debe aprender administración RPM y revisar por separado el ciclo del sistema y el del módulo MySQL; no son necesariamente iguales. [1]

#### Rocky Linux 9

Distribución comunitaria empresarial gratuita y de código abierto, con licencias por componente y orientación a compatibilidad con RHEL. La rama mayor contempla diez años de soporte, entre soporte activo y mantenimiento de seguridad; debe utilizarse una revisión menor admitida. [6]

**Compatibilidad y ventajas:** comparte herramientas RPM, SELinux y administración similares a Oracle Linux; es una alternativa válida para MySQL y Java. **Desventajas:** cambiar a ella exigiría repetir la validación de los scripts y la VM, sin una mejora necesaria para esta carga. Su correspondencia con la sugerencia de la asignatura es menor. No se penaliza por una supuesta incompatibilidad con MySQL. [6]

#### AlmaLinux 9

Distribución empresarial comunitaria gratuita y de código abierto, con licencias por componente y orientación a compatibilidad con RHEL. La rama 9 tiene un ciclo de diez años; las revisiones menores deben actualizarse conforme a la política del proyecto. [7]

**Compatibilidad y ventajas:** proporciona un entorno RPM con SELinux y herramientas conocidas de administración, apropiado para MySQL y Java. **Desventajas:** al igual que Rocky, requeriría repetir la validación realizada sobre Oracle Linux y ofrece menor correspondencia con la recomendación del curso. No existe una incompatibilidad funcional que la descarte. [7]

#### Ubuntu Server 24.04 LTS

Distribución Linux gratuita con licencias por componente y servicios comerciales opcionales de Canonical. La versión LTS ofrece cinco años de mantenimiento estándar de seguridad para los paquetes de `Main`; las ampliaciones y la cobertura adicional dependen de Ubuntu Pro y sus condiciones. No se supone cobertura idéntica para todos los repositorios. [8]

**Compatibilidad y ventajas:** amplia documentación, administración con `apt`, integración con Java y mecanismos como AppArmor. MySQL dispone de un repositorio APT oficial para Ubuntu. **Desventajas:** habría que adaptar los comandos RPM del proyecto y comprobar el origen y la versión 8.4 de los paquetes. AppArmor es un control válido; su diferencia frente a SELinux no se considera una falla de seguridad. [8, 11]

#### Debian 12

Distribución comunitaria libre, gratuita y con licencias por componente. La rama tiene tres años de soporte completo y dos adicionales de LTS, con alcance de arquitecturas y paquetes definido por el proyecto. Debian 12 se encuentra en la etapa LTS; mantenerla exige comprobar esa cobertura y planear la actualización a una rama posterior. [9]

**Compatibilidad y ventajas:** instalación mínima de bajo consumo, estabilidad y documentación consolidada. MySQL ofrece un repositorio APT oficial para Debian y Java puede desplegarse en el sistema. **Desventajas:** se deben adaptar los scripts basados en `dnf` y seleccionar explícitamente MySQL 8.4; la rama 12 ofrece un horizonte de mantenimiento menor que las ramas empresariales evaluadas. [9, 11]

#### FreeBSD 14

Sistema de la familia BSD Unix, gratuito, con licencia predominantemente BSD permisiva y componentes sujetos a sus propias licencias. Es necesario seguir las revisiones menores admitidas y la política de mantenimiento de la rama 14. Se evalúa como opción Unix permitida por la asignatura. [10]

**Compatibilidad y ventajas:** sistema base coherente, documentación mediante el Handbook, administración con `pkg` y aislamiento mediante jails; puede alojar MySQL mediante sus paquetes o ports. **Desventajas:** requiere adaptar los scripts que usan `dnf`, `systemd`, SELinux y `firewalld`, y validar una VM distinta de la simulación Linux prevista. Su menor puntaje responde a este trabajo de adaptación, no a una imposibilidad de ejecutar MySQL. [10]

### Criterios y método de evaluación

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

### Resultado y justificación

Alternativa seleccionada: **OL9** — Oracle Linux 9 (x86_64).

Oracle Linux obtiene **4,90 sobre 5**, frente a 4,60 de Ubuntu y 4,50 de Rocky Linux y AlmaLinux. El total de OL9 es $(25\times5 + 10\times5 + 15\times5 + 10\times4 + 10\times5 + 10\times5 + 15\times5 + 5\times5)/100 = 4{,}90$.

Las seis alternativas tienen opciones de uso sin costo de licencia. MySQL funciona en los Linux evaluados, por lo que la compatibilidad tampoco decide por sí sola. Oracle Linux gana por su correspondencia con la asignatura, la disponibilidad directa de `mysql:8.4` en AppStream y la continuidad de los scripts y la VM ya comprobados. Ubuntu y Debian siguen siendo opciones válidas; Rocky y AlmaLinux reciben puntajes iguales por su ajuste semejante a este caso.

La evidencia local registra Oracle Linux 9.8, MySQL 8.4, el paquete `mysql-selinux`, el servicio `mysqld` activo y la apertura del puerto 3306. Los documentos de implementación presentan las pruebas de carga, reglas y acceso de consulta. Esta comprobación del entorno elegido no sustituye una comparación de rendimiento entre los seis sistemas.

### Riesgos y mitigaciones

| Riesgo | Mitigación propuesta |
|---|---|
| Presión de memoria en el anfitrión | Asignar 6 GiB a la VM (de 16 GB del anfitrión), cerrar cargas no necesarias y apagar la VM con `limactl stop` cuando no se use |
| Cambio del módulo o de los paquetes MySQL | Comprobar `dnf module list mysql`, seleccionar `mysql:8.4` y registrar las versiones instaladas; revisar el ciclo del módulo aparte del SO |
| Diferencias entre la simulación en macOS (vz) y el servidor con Linux (KVM) | Mantener arquitectura, recursos iniciales, versiones y scripts; comprobar `mysqld`, la aplicación y la conexión de Workbench en cada anfitrión |
| Interrupción del anfitrión o pérdida del disco virtual | Conservar DDL y DML en Git y exportaciones fuera de la VM; verificar restauración en lugar de depender solo de instantáneas |
| Acceso al puerto 3306 desde una red no prevista | Limitar el reenvío o las reglas de red al entorno de demostración y proteger las claves fuera de Git |
| SELinux o firewall bloquean servicios | Conservar `mysql-selinux`, revisar registros y contextos, y permitir solo los puertos necesarios sin desactivar globalmente los controles |
Table: Riesgos de la selección del SO y medidas de mitigación

### Conclusión

Se adopta Oracle Linux 9 x86_64 para el ambiente académico virtualizado de SIGEVET con MySQL 8.4 LTS Community Edition. Cumple la restricción Linux/Unix, coincide con la sugerencia de la asignatura y permite instalar el motor desde AppStream con su política SELinux. La VM definitiva se ejecutará en VirtualBox y la simulación en macOS se mantiene con Lima.

### Referencias

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

## Documento de Implementación de la Máquina Virtual



### Introducción

Este documento describe cómo se creó y configuró la máquina virtual (VM) que aloja la base de datos y la aplicación
web de SIGEVET. El sistema operativo es el que se eligió en el *Documento de análisis de selección de sistema
operativo*: **Oracle Linux 9 (x86_64)**.

El proyecto usa dos ambientes con el mismo sistema operativo y los mismos scripts:

- **Ambiente de simulación**: un MacBook Intel con Lima. Ahí se hicieron todas las pruebas que aparecen en este
  documento, y las salidas que se muestran son reales.
- **Equipo servidor**: un computador Lenovo con Linux, donde la VM queda funcionando para la demostración. La VM
  corre con KVM (el hipervisor del kernel de Linux) y se crea con un solo script, `vm/migrar_linux.sh`. Los scripts
  no dependen de la ruta ni del hipervisor, así que el procedimiento es el mismo.

### Ambientes

| Elemento | Simulación (MacBook) | Equipo servidor |
|---|---|---|
| Equipo anfitrión | MacBook Pro Intel Core i7-9750H, 16 GB RAM, macOS Sonoma 14.8 | Lenovo con Linux (datos en la tabla de abajo) |
| Hipervisor | Apple Virtualization.framework (`vmType: vz`) con Lima 2.2.1 | KVM/QEMU (`vmType: qemu`) con Lima 2.2.1 |
| Imagen del sistema | Plantilla KVM oficial `OL9U8_x86_64-kvm-b293.qcow2` | La misma plantilla KVM oficial de Oracle |
| Sistema operativo invitado | Oracle Linux Server 9.8, kernel UEK 6.12 | Oracle Linux Server 9.8 |
| Recursos de la VM | 4 vCPU, 6 GiB de RAM, disco de 40 GiB | La mitad de los núcleos (2 a 4) y 4 GiB de RAM (6 GiB si el equipo tiene 12 GB o más), disco de 40 GiB |
| Red | NAT con reenvío de 3306 (MySQL) y 5000 (web) | NAT con los puertos 3306 y 5000 abiertos a la red local |
| Proyecto dentro de la VM | Carpeta montada en `/proyecto` (virtiofs, solo lectura) | Copiado a `~/proyecto` por el script |

Table: Ambientes de virtualización

| Dato del equipo servidor | Valor |
|---|---|
| Procesador | Pendiente |
| Memoria RAM | Pendiente |
| Sistema operativo anfitrión | Pendiente |
| Dirección para entrar a la app | Pendiente |

Table: Equipo servidor (se completa con lo que imprime `vm/migrar_linux.sh` en el Lenovo)

**Por qué dos ambientes.** La VM se construyó y se probó primero en el MacBook de uno de los integrantes, y la
versión final queda en el Lenovo con Linux, que es el que se lleva a la sustentación. En macOS con procesador Intel, VirtualBox pide instalar extensiones
del kernel, y Homebrew ya no publica QEMU precompilado. Por eso la simulación se hizo con Lima, que usa el hipervisor
de macOS y guarda la definición de la VM en un archivo de texto (`vm/sigevet-ol9.yaml`).

### Simulación en macOS con Lima

#### Definición de la máquina virtual

La VM se declara en el archivo `vm/sigevet-ol9.yaml`, que forma parte del repositorio:

```yaml
minimumLimaVersion: 2.0.0
base:
- template:_images/oraclelinux-9   # Imagen oficial Oracle Linux 9 (KVM template de yum.oracle.com)
vmType: vz                         # Hipervisor nativo de macOS (Virtualization.framework)
cpus: 4
memory: 6GiB                       # MySQL y Tomcat usan poco; queda espacio de sobra para el SO
disk: 40GiB
mounts:
- location: "~/Developer/proyecto-bd1-sigevet"
  mountPoint: /proyecto
  writable: false
portForwards:                      # MySQL (Workbench) y aplicación web hacia el anfitrión
- guestPort: 3306
  hostPort: 3306
- guestPort: 5000
  hostPort: 5000
containerd:                        # el proyecto no usa contenedores
  system: false
  user: false
```

La plantilla `oraclelinux-9` de Lima descarga la imagen oficial de Oracle y verifica su integridad con la suma
SHA-256 `b12103391327abee8090686759c0d62dac9a7af2bf0f45fdf6b0d085a0fbb52b` antes de usarla. La configuración inicial
(usuario, llaves SSH, red) se hace con *cloud-init*.

#### Procedimiento de creación

1. **Instalar Lima** (sin permisos de administrador): se descargó `lima-2.2.1-Darwin-x86_64.tar.gz` de la página
   oficial de releases en GitHub y se descomprimió en `~/.local/lima`.
2. **Crear y arrancar la VM:**

   ```bash
   limactl start --name=sigevet --tty=false vm/sigevet-ol9.yaml
   ```

   Lima descarga la imagen, la convierte a disco de arranque, crea la VM y espera a que el sistema esté listo.
3. **Habilitar la carpeta compartida.** La plantilla KVM de Oracle no incluye el módulo `virtiofs` en el kernel
   básico; se instaló el paquete `kernel-uek-modules-extra` correspondiente al kernel en ejecución, se cargó el módulo
   y se montó la carpeta con la etiqueta que expone el hipervisor. Para que el montaje sea permanente se agregó a
   `/etc/fstab` y a `/etc/modules-load.d`:

   ```bash
   sudo dnf install -y kernel-uek-modules-extra-$(uname -r)
   sudo modprobe virtiofs
   echo virtiofs | sudo tee /etc/modules-load.d/virtiofs.conf
   echo "<etiqueta> /proyecto virtiofs ro,nofail 0 0" | sudo tee -a /etc/fstab
   sudo mount /proyecto
   ```

4. **Entrar a la VM:** `limactl shell sigevet` abre una sesión SSH con el usuario del anfitrión.
5. **Operación diaria:** `limactl stop sigevet` apaga la VM y `limactl start sigevet` la vuelve a encender. La base de
   datos y la aplicación web arrancan solas porque quedan como servicios del sistema.

### Evidencias

<!-- Created: 18:15 19-Oct-2026 -->

**Estado de la máquina virtual** (salida real, archivo `vm/logs/03_evidencia_vm.log`):

```text

$ cat /etc/oracle-release
Oracle Linux Server release 9.8

$ uname -srm
Linux 6.12.0-204.92.4.2.el9uek.x86_64 x86_64

$ hostnamectl | grep -E 'Static hostname|Operating System|Kernel|Architecture|Virtualization'
 Static hostname: lima-sigevet
  Virtualization: apple
Operating System: Oracle Linux Server 9.8
          Kernel: Linux 6.12.0-204.92.4.2.el9uek.x86_64
    Architecture: x86-64
  Hardware Model: Apple Virtualization Generic Platform

$ lscpu | grep -E '^CPU\(s\)|Model name|Hypervisor vendor|Virtualization type'
CPU(s):                                  4
Model name:                              Intel(R) Core(TM) i7-9750H CPU @ 2.60GHz

$ free -h
               total        used        free      shared  buff/cache   available
Mem:           5.8Gi       1.2Gi       3.8Gi        16Mi       1.0Gi       4.6Gi
Swap:          4.0Gi          0B       4.0Gi

$ df -h /
Filesystem                   Size  Used Avail Use% Mounted on
/dev/mapper/vg_main-lv_root   32G   13G   20G  39% /

$ ip -brief address
lo               UNKNOWN        127.0.0.1/8 ::1/128
eth0             UP             192.168.5.15/24 fe80::5055:55ff:fece:8c7e/64

$ getenforce
Enforcing

$ systemctl is-active sshd
active
```


### Problemas encontrados y solución

| Problema | Causa | Solución |
|---|---|---|
| Homebrew intentaba compilar QEMU y sus dependencias desde el código fuente (horas) | Homebrew ya no publica binarios para macOS Intel | Se usó Lima con el hipervisor nativo `vz`, que no necesita QEMU |
| El primer arranque quedó esperando la instalación de `nerdctl` | La plantilla instala contenedores por defecto | Se desactivó `containerd` en la definición de la VM |
| La carpeta del proyecto no aparecía en `/proyecto` | El kernel de la plantilla no trae el módulo `virtiofs` | Se instaló `kernel-uek-modules-extra` y se montó con la etiqueta real del dispositivo |

Table: Problemas y soluciones durante la implementación de la VM

### Instalación en el equipo servidor (Lenovo con Linux)

En el servidor no se copia el disco de la VM del MacBook: ese disco usa el hipervisor de Apple y además pesaba 13 GB
porque todavía tenía Oracle. Como toda la base se construye con scripts, la VM se crea de nuevo y queda idéntica. Todo
lo hace un solo script, `vm/migrar_linux.sh`, que se ejecuta como usuario normal (con sudo) desde la carpeta del
proyecto:

```bash
bash vm/migrar_linux.sh
```

| Paso | Qué hace |
|---|---|
| 1 | Revisa el equipo: que exista `/dev/kvm` (virtualización activada en la BIOS) y cuánta RAM y cuántos núcleos tiene |
| 2 | Instala QEMU y el firmware UEFI (OVMF) con el gestor de paquetes de la distribución (apt, dnf, pacman o zypper) |
| 3 | Descarga Lima 2.2.1 de su página oficial y verifica la suma SHA-256 |
| 4 | Crea `vm/claves.env` con claves aleatorias si no existe |
| 5 | Crea la VM `sigevet` desde la misma definición `vm/sigevet-ol9.yaml`, con `vmType: qemu`, los recursos según el equipo y los puertos 3306 y 5000 abiertos a la red local |
| 6 | Copia el proyecto a la VM y ejecuta `instalar_mysql.sh`, `cargar_bd.sh` e `instalar_app.sh` |
| 7 | Comprueba que la aplicación responda y muestra las direcciones para el navegador y para MySQL Workbench |

Table: Pasos de vm/migrar_linux.sh

El script se probó completo creando una VM nueva desde cero en el MacBook (allí usa el hipervisor `vz` en lugar de
QEMU). La VM nueva quedó con MySQL 8.4, la base cargada, las 10 pruebas de reglas en OK y la aplicación web
respondiendo, tanto en `127.0.0.1` como desde la dirección IP del equipo en la red local.

Después de reiniciar el Lenovo, la VM se enciende con `limactl start sigevet`; MySQL y la aplicación arrancan solos
dentro de ella.

#### Alternativa: VirtualBox

En un anfitrión con Windows se puede usar VirtualBox 7: crear una VM *Linux / Oracle (64-bit)* con 4 GB de RAM y 40 GB
de disco, instalar Oracle Linux 9 desde la ISO oficial con la opción *Server*, reenviar los puertos 3306 y 5000, copiar
el proyecto a la VM y ejecutar los mismos tres scripts (`instalar_mysql.sh`, `cargar_bd.sh` e `instalar_app.sh`). El
paso a paso está en `vm/README.md`.

## Documento de Análisis de selección de RDBMS



### Introducción y objetivo

Se selecciona **MySQL 8.4 LTS Community Edition** para la base de datos de SIGEVET, proyecto de Bases de Datos 1 de la Universidad El Bosque. La decisión considera las funciones del modelo, la instalación en Oracle Linux 9, el uso de MySQL Workbench desde el anfitrión y la conexión de la aplicación Java. Primero se probó Oracle Database Free; esa versión quedó como respaldo y MySQL es el motor de la implementación principal.

SIGEVET centraliza la información de Huella Sana S.A.S., organización ficticia con cinco clínicas. El modelo tiene 22 tablas para sedes, empleados, clientes, mascotas, citas, historias clínicas, medicamentos, inventario y facturación. La aplicación usa Servlets y JSP en Tomcat y consulta los datos con una cuenta de solo lectura. Los scripts del equipo crean el esquema y cargan los datos sintéticos.

### Requisitos y restricciones

#### Alcance académico y relacional

El enunciado exige un RDBMS, prohíbe SQL Server y acepta «Data Modeler o Workbench» para el modelado. Se necesitan claves primarias y foráneas, unicidad, obligatoriedad, restricciones de dominio, transacciones y consultas de álgebra relacional. También se requieren vistas, funciones, procedimientos y disparadores para las reglas de negocio. La evaluación compara MySQL, PostgreSQL, Oracle Database Free y MariaDB; ninguna alternativa se presenta como plenamente conforme con todo el estándar SQL.

La base se llama `sigevet`. El usuario `vetdb` administra sus objetos y `vet_app` consulta mediante el rol `rol_sigevet_lectura`. MySQL Workbench Community permite modelar diagramas EER en notación Crow's Foot, administrar la base y ejecutar consultas desde el anfitrión. La aplicación Java se conecta mediante el controlador JDBC oficial **MySQL Connector/J**, con consultas parametrizadas y sin usar la cuenta dueña del esquema. [1, 2, 3]

#### Infraestructura y operación

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

### Alternativas evaluadas

#### MySQL 8.4 LTS Community Edition

Con InnoDB ofrece transacciones e integridad referencial mediante claves foráneas. Aplica restricciones `CHECK` desde 8.0.16 e incorpora `EXCEPT` e `INTERSECT` desde 8.0.31, funciones disponibles en 8.4. También admite vistas, funciones, procedimientos y disparadores con `SIGNAL` para rechazar operaciones que incumplan las reglas. [5, 6, 7, 8]

**Ventajas:** licencia gratuita sin límites de edición equivalentes a Oracle Free; paquete oficial en AppStream; Workbench Community gratuito para modelado, administración y consultas; Connector/J oficial; familiaridad del equipo y una comunidad amplia. La base cargada ocupa 1,08 MB, lo que muestra una necesidad baja de almacenamiento para este conjunto académico. Ese tamaño no mide el consumo de RAM ni demuestra por sí solo mayor rendimiento que otros motores.

**Desventajas:** carece de `FULL OUTER JOIN`; cada disparador atiende un solo evento; y, con la configuración habitual de Linux, los nombres de tabla distinguen mayúsculas. El analizador de Workbench usado en el proyecto no reconoce valores por defecto con expresión como `DEFAULT (CURRENT_DATE)`, aunque el servidor los admite. Las adaptaciones se explican en los riesgos y están reflejadas en los scripts. [8, 9, 10, 11]

#### PostgreSQL 17

Motor gratuito de código abierto con licencia PostgreSQL permisiva. Ofrece transacciones, integridad referencial, restricciones y una amplia cobertura de SQL, incluido `FULL OUTER JOIN`. Puede ejecutarse en Oracle Linux y conectarse desde Java mediante JDBC; es una alternativa sólida para el modelo. [12]

**Ventajas:** expresividad SQL, controles de integridad, documentación y comunidad amplias, sin límites de edición comparables a Oracle Free. **Desventajas para este equipo:** exige aprender y validar otro flujo de modelado y administración; Workbench está orientado a MySQL. No se penaliza su capacidad relacional ni se afirma que tenga peor rendimiento: la diferencia está en las herramientas y la experiencia disponibles.

#### Oracle Database Free

Fue el motor de la primera prueba y su implementación quedó como respaldo. Ofrece transacciones, restricciones, vistas, rutinas, disparadores y herramientas gratuitas como SQL Developer Data Modeler, además de un controlador JDBC oficial.

**Ventajas:** funciones relacionales suficientes y una instalación que el equipo ya probó. **Desventajas:** licencia propietaria de uso gratuito, límites de **2 CPU, 2 GB de RAM y 12 GB de datos de usuario**, y una instalación que requirió preinstalador y RPM separado. El límite de memoria corresponde al motor, no a toda la VM. Estas restricciones no impiden alojar los datos académicos, pero reducen el margen y añaden pasos frente al módulo MySQL de AppStream. [13]

#### MariaDB 11.4 LTS

Motor gratuito de código abierto bajo GPLv2, con InnoDB, transacciones, claves foráneas, restricciones y conectividad Java. Es viable para SIGEVET y tiene una comunidad propia. [14]

**Ventajas:** licencia libre, recursos configurables y funciones relacionales suficientes. **Desventajas para el proyecto:** MariaDB y MySQL han divergido; habría que validar dialecto, conector y herramientas. Workbench se diseña para MySQL y no se presupone compatibilidad completa con MariaDB. La familiaridad del equipo con MySQL no sustituye esa comprobación.

### Criterios y método de evaluación

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

### Resultado y justificación

Alternativa seleccionada: **MySQL** — MySQL 8.4 LTS Community Edition.

MySQL obtiene **4,75 sobre 5**. El cálculo es $(15\times4 + 15\times5 + 15\times5 + 10\times5 + 15\times5 + 10\times4 + 5\times5 + 10\times5 + 5\times5)/100 = 4{,}75$. Le siguen PostgreSQL con 4,40, Oracle con 4,20 y MariaDB con 4,05.

La ventaja resulta de la instalación directa en AppStream, Workbench como herramienta admitida y conocida, la licencia sin límites de edición equivalentes a Oracle Free y el ajuste a Java con Connector/J. La base de 1,08 MB y la carga completa en 1–2 s respaldan la viabilidad de la implementación académica; no constituyen una comparación de rendimiento entre motores. Las evidencias están en los registros de instalación, carga y verificación de `vm/logs/`.

La elección depende del contexto. Si se trasladan 10 puntos de modelado y 5 de familiaridad al criterio SQL, MySQL y PostgreSQL empatan en 4,60. PostgreSQL sería especialmente competitivo si se priorizara expresividad SQL. MySQL gana con los pesos adoptados por sus ventajas prácticas para este equipo, aceptando adaptaciones concretas del dialecto.

### Riesgos y mitigaciones

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

### Referencias

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

## Documento de Implementación del RDBMS



### Introducción

Este documento describe la instalación de **MySQL 8.4 LTS Community Edition** en Oracle Linux 9, la creación de la base `sigevet` y sus usuarios, y las pruebas de funcionamiento. La versión instalada es **8.4.11**. MySQL Workbench se ejecuta en el equipo anfitrión para administrar y consultar la base.

La VM definitiva corre con KVM en el computador servidor (un Lenovo con Linux) y se crea con `vm/migrar_linux.sh`; la simulación en macOS utiliza Lima. Primero se probó Oracle Database Free y esa versión quedó como respaldo. Las evidencias incluidas corresponden a la implementación actual con MySQL.

### Arquitectura de la instalación

| Elemento | Valor |
|---|---|
| Motor | MySQL 8.4.11 LTS Community Edition; tablas InnoDB |
| Paquetes | `mysql-server` y `mysql`, del módulo oficial `mysql:8.4` de AppStream |
| Base de datos | `sigevet` |
| Servicio | `mysqld`, habilitado para arrancar con la VM |
| Conexión | TCP, puerto `3306`; reenvío al anfitrión o IP de la VM en modo puente |
| Configuración del proyecto | `/etc/my.cnf.d/sigevet.cnf` |
| Dueño y usuario de la aplicación | `vetdb` y `vet_app`, respectivamente |
| Juego de caracteres y cotejamiento | `utf8mb4` / `utf8mb4_0900_ai_ci` |

Table: Arquitectura de la instalación

### Procedimiento de instalación

Desde la carpeta del proyecto, dentro de la VM, se ejecuta el instalador como administrador. El script lee la clave de `root` del archivo local `vm/claves.env`, que no se versiona:

```bash
sudo bash vm/scripts/instalar_mysql.sh
```

| Paso | Comando | Qué hace |
|---|---|---|
| 1 | `dnf -y -q module enable mysql:8.4` y `dnf -y -q install mysql-server mysql` | Instala el servidor y el cliente desde AppStream, sin descargar RPM externos; incluye la política `mysql-selinux` |
| 2 | Escribir `/etc/my.cnf.d/sigevet.cnf` | Define la escucha de red, los caracteres, la zona horaria y la creación de funciones |
| 3 | `systemctl enable --now mysqld` y `ALTER USER 'root'@'localhost' ...` | Inicia el servicio, habilita su arranque automático y establece la clave inicial de `root` si aún no tiene una |
| 4 | `firewall-cmd --permanent --add-port=3306/tcp` y `firewall-cmd --reload` | Abre el puerto de MySQL cuando `firewalld` está activo |

Table: Pasos de instalación del RDBMS

<!-- Created: 18:15 19-Oct-2026 -->

**Instalación y configuración de MySQL 8.4** (salida real, archivo `vm/logs/02_instalacion_mysql.log`):

```text
== Sistema: Oracle Linux Server release 9.8 | kernel 6.12.0-204.92.4.2.el9uek.x86_64 | 4 CPU | 5.8Gi RAM
== 1/4 Paquetes de MySQL 8.4 (repositorio AppStream de Oracle Linux)

Installed:
  mysql-8.4.11-1.module+el9.8.0+90998+76ddfd45.x86_64
  mysql-common-8.4.11-1.module+el9.8.0+90998+76ddfd45.noarch
  mysql-errmsg-8.4.11-1.module+el9.8.0+90998+76ddfd45.noarch
  mysql-selinux-1.0.14-1.el9_6.noarch
  mysql-server-8.4.11-1.module+el9.8.0+90998+76ddfd45.x86_64

mysql-server-8.4.11-1.module+el9.8.0+90998+76ddfd45.x86_64
== 2/4 Configuración (/etc/my.cnf.d/sigevet.cnf)
== 3/4 Servicio mysqld con arranque automático
Created symlink /etc/systemd/system/multi-user.target.wants/mysqld.service → /usr/lib/systemd/system/mysqld.service.
active
Clave de root configurada
== 4/4 Firewall (puerto 3306)
success
success
mysql  Ver 8.4.11 for Linux on x86_64 (Source distribution)
== MySQL instalado
EXIT 0
```


### Configuración

| Parámetro | Valor | Motivo |
|---|---|---|
| `bind-address` | `0.0.0.0` | Permite entrar desde Workbench en el anfitrión a través de las interfaces de la VM |
| `character-set-server` | `utf8mb4` | Conserva tildes, ñ y el resto de caracteres Unicode de los datos |
| `collation-server` | `utf8mb4_0900_ai_ci` | Define el cotejamiento de texto, sin distinguir mayúsculas ni acentos en sus comparaciones |
| `default-time-zone` | `'-05:00'` | Usa la zona horaria de Colombia para las fechas y horas de registro |
| `log_bin_trust_function_creators` | `1` | Con el registro binario activo, permite crear funciones y disparadores sin conceder el privilegio global `SUPER` a `vetdb` |

Table: Configuración de MySQL para SIGEVET

El script `vm/scripts/evidencias.sh rdbms` consulta los paquetes, el estado de `mysqld`, la escucha en `3306`, el tamaño y los permisos. La salida guardada confirma el servicio habilitado y activo:

<!-- Created: 18:15 19-Oct-2026 -->

**Servicio, configuración y base sigevet en MySQL** (salida real, archivo `vm/logs/05_evidencia_rdbms.log`):

```text

$ rpm -q mysql-server mysql
mysql-server-8.4.11-1.module+el9.8.0+90998+76ddfd45.x86_64
mysql-8.4.11-1.module+el9.8.0+90998+76ddfd45.x86_64

$ systemctl is-enabled mysqld
enabled

$ systemctl is-active mysqld
active

$ cat /etc/my.cnf.d/sigevet.cnf
[mysqld]
# escuchar en todas las interfaces para poder entrar desde Workbench en el equipo anfitrión
bind-address = 0.0.0.0
character-set-server = utf8mb4
collation-server = utf8mb4_0900_ai_ci
default-time-zone = '-05:00'
# permite que vetdb cree funciones y disparadores sin privilegio SUPER (el binlog está activo)
log_bin_trust_function_creators = 1

$ ss -ltn | grep 3306
LISTEN 0      151          0.0.0.0:3306       0.0.0.0:*
LISTEN 0      70                 *:33060            *:*

$ mysql -u vetdb -p -t  (versión, base, tamaño y juego de caracteres)
+---------+---------+---------+
| version | base    | usuario |
+---------+---------+---------+
| 8.4.11  | sigevet | vetdb@% |
+---------+---------+---------+
+-----------------+
| mb_base_sigevet |
+-----------------+
|            1.08 |
+-----------------+
+---------+--------------------+
| charset | collation          |
+---------+--------------------+
| utf8mb4 | utf8mb4_0900_ai_ci |
+---------+--------------------+
+----------------------------------------------------+
| Grants for vetdb@%                                 |
+----------------------------------------------------+
| GRANT USAGE ON *.* TO `vetdb`@`%`                  |
| GRANT ALL PRIVILEGES ON `sigevet`.* TO `vetdb`@`%` |
+----------------------------------------------------+
```


### Usuarios, rol y seguridad

`database/01_ddl/00_crear_usuarios.sql` se ejecuta como `root` mediante `vm/scripts/cargar_bd.sh`. Crea la base y separa la administración de los objetos del acceso de la aplicación:

| Usuario / rol | Privilegios | Uso |
|---|---|---|
| `root@localhost` | Administración del servidor | Instalación y creación inicial de usuarios |
| `vetdb@%` | `ALL PRIVILEGES ON sigevet.*` | Dueño de los objetos; sus permisos se limitan a la base `sigevet` |
| `rol_sigevet_lectura@%` | `SELECT, SHOW VIEW ON sigevet.*` | Agrupa los permisos para consultar tablas y vistas |
| `vet_app@%` | `rol_sigevet_lectura` como rol por defecto | Conexión de la aplicación web, solo lectura |

Table: Usuarios y privilegios

Los usuarios `vetdb` y `vet_app` aceptan conexiones desde otros equipos mediante `%`. El rol se declara sin host, lo que equivale a `%`. Esto permite usar las cuentas desde Workbench; `root` permanece en `localhost`. Las claves están en `vm/claves.env`, excluido del repositorio.

### Prueba de conexión y de privilegios

La evidencia de `vm/scripts/evidencias.sh vet_app` muestra que `vet_app` puede leer los datos y tiene activo el rol de lectura. Los intentos de `DELETE`, `UPDATE`, `INSERT` y `DROP` son rechazados con el **error 1142**:

<!-- Created: 18:15 19-Oct-2026 -->

**Conexión y privilegios del usuario vet_app** (salida real, archivo `vm/logs/07_prueba_conexion.log`):

```text
$ mysql -u vet_app -p -t --force < vm/scripts/prueba_vet_app.sql
+-----------+---------------------------+
| usuario   | rol                       |
+-----------+---------------------------+
| vet_app@% | `rol_sigevet_lectura`@`%` |
+-----------+---------------------------+
+----------+-------+----------+
| mascotas | citas | facturas |
+----------+-------+----------+
|       59 |   144 |      109 |
+----------+-------+----------+
+--------------+
| nombre       |
+--------------+
| Bogotá D.C.  |
| Chía         |
+--------------+
+---------------------------------------------------------+
| Grants for vet_app@%                                    |
+---------------------------------------------------------+
| GRANT USAGE ON *.* TO `vet_app`@`%`                     |
| GRANT SELECT, SHOW VIEW ON `sigevet`.* TO `vet_app`@`%` |
| GRANT `rol_sigevet_lectura`@`%` TO `vet_app`@`%`        |
+---------------------------------------------------------+
ERROR 1142 (42000) at line 10: DELETE command denied to user 'vet_app'@'localhost' for table 'MASCOTA'
$ UPDATE sigevet.SERVICIO SET precio = 1
ERROR 1142 (42000) at line 1: UPDATE command denied to user 'vet_app'@'localhost' for table 'SERVICIO'
$ INSERT INTO sigevet.ESPECIE VALUES (99, 'Prueba')
ERROR 1142 (42000) at line 1: INSERT command denied to user 'vet_app'@'localhost' for table 'ESPECIE'
$ DROP TABLE sigevet.CITA
ERROR 1142 (42000) at line 1: DROP command denied to user 'vet_app'@'localhost' for table 'CITA'
```


### Conexión con MySQL Workbench

Se utiliza **MySQL Workbench 8.0.44** en el equipo anfitrión. Se crea una conexión nueva de tipo **Standard (TCP/IP)** con `Hostname: 127.0.0.1`, `Port: 3306`, `Username: vetdb` y `Default Schema: sigevet`. Se introduce la clave local de `vetdb` al conectar.

`127.0.0.1:3306` requiere el reenvío del puerto de la VM al anfitrión. Desde otro equipo de la red se utiliza la IP del computador servidor, porque los puertos 3306 y 5000 quedan abiertos a la red local. Desde Workbench se pueden ejecutar los scripts SQL, revisar el esquema y abrir el modelo EER `SIGEVET.mwb` de la Fase 1, con notación Crow's Foot.

| Herramienta | Cadena o parámetros de conexión |
|---|---|
| Cliente `mysql` | `mysql --default-character-set=utf8mb4 -h 127.0.0.1 -P 3306 -u vetdb -p sigevet` |
| MySQL Workbench | Host `127.0.0.1`, puerto `3306`, usuario `vetdb`, base por defecto `sigevet` |
| Aplicación Java con Connector/J (Servlets y JSP en Tomcat) | `jdbc:mysql://localhost:3306/sigevet`; usuario `vet_app` y clave por separado |

Table: Cadenas de conexión

La URL JDBC con `localhost` corresponde a Tomcat dentro de la VM. Si el cliente está en otro equipo sin reenvío, se reemplaza el host por la IP de la VM.

### Operación

| Acción | Comando (dentro de la VM) |
|---|---|
| Estado del motor | `systemctl status mysqld` |
| Reiniciar el motor | `sudo systemctl restart mysqld` |
| Crear usuarios y cargar el esquema | `bash vm/scripts/cargar_bd.sh` |
| Borrar la base y cargarla de nuevo | `bash vm/scripts/cargar_bd.sh --reiniciar` |
| Consultar versión, objetos e ingresos por sede | `bash vm/scripts/resumen_bd.sh` |
| Estado de la aplicación web | `systemctl status sigevet-web` |

Table: Comandos de operación

La opción `--reiniciar` elimina la base antes de reconstruirla; se utiliza para repetir la carga de prueba.

### Resultados

La carga completa tarda aproximadamente **1–2 s**; `vm/logs/04_carga_bd.log` registra **1 s**, finalización correcta y los siguientes objetos:

| Resultado | Cantidad |
|---|---|
| Tablas | 22 |
| Vistas | 6 |
| Funciones | 2 |
| Procedimientos | 3 |
| Disparadores | 9 |
| Índices | 71 |
| Pruebas de reglas de negocio | 10 de 10 OK |

Table: Resultados de la carga y verificación

Las pruebas comprueban claves foráneas, restricciones `CHECK` y `UNIQUE`, y reglas implementadas con disparadores. La base con sus datos e índices ocupa **1,08 MB**, según la suma de `DATA_LENGTH + INDEX_LENGTH` en `information_schema.TABLES`, mostrada en la evidencia del RDBMS.

### Consideraciones y soluciones

| Situación | Solución |
|---|---|
| Codificación de los datos | NLS ya no aplica. La base usa `utf8mb4` y la carga se ejecuta con `--default-character-set=utf8mb4`; la prueba conserva Bogotá D.C. y Chía |
| Claves durante la carga y las consultas de evidencia | `cargar_bd.sh`, `evidencias.sh` y `resumen_bd.sh` usan un archivo de opciones temporal con `--defaults-extra-file`, para que la clave de conexión no aparezca en la línea de comandos |
| Nombres de tablas en Linux | Se respeta la escritura de los nombres, porque MySQL distingue mayúsculas y minúsculas en este entorno |
| Expresiones en valores por defecto | El analizador de Workbench no reconoce `DEFAULT (CURRENT_DATE)`; las fechas de registro utilizan `DATETIME DEFAULT CURRENT_TIMESTAMP` |
| Un disparador cubre un solo evento | Las reglas compartidas se definen en procedimientos llamados desde los disparadores de `INSERT` y `UPDATE` |
| Ausencia de `FULL OUTER JOIN` | El ejercicio 29 utiliza `LEFT JOIN UNION RIGHT JOIN` |

Table: Consideraciones de la implementación

## Scripts de Implementación del Modelo Lógico al Modelo Físico



### Introducción

Este documento acompaña los scripts SQL que transforman el modelo lógico de SIGEVET en el modelo físico de **MySQL 8.4**.
Los scripts están en la carpeta `database/` del repositorio, junto con su `README.md`. Se agrupan en los tres tipos
que pide el enunciado: **creación de objetos**, **inserción de registros** y **scripts varios**. Se pueden ejecutar con el
cliente `mysql` dentro de la VM (así lo hace `vm/scripts/cargar_bd.sh`) o abriéndolos uno por uno en MySQL Workbench.

### Scripts de creación de objetos

| Script | Objetos | Cantidad |
|---|---|---|
| `01_ddl/00_crear_usuarios.sql` | Base de datos, usuarios y rol (se ejecuta como root) | Base `sigevet`, 2 usuarios (`vetdb`, `vet_app`) y 1 rol (`rol_sigevet_lectura`) |
| `01_ddl/01_crear_tablas.sql` | Tablas y restricciones con nombre, con el comentario de cada tabla y columna | 22 tablas; 22 PK, 30 FK, 25 UNIQUE, 36 CHECK; 138 columnas comentadas |
| `01_ddl/02_indices.sql` | Índices de búsqueda | 5 índices (InnoDB crea solo los de PK, UNIQUE y FK) |
| `01_ddl/03_vistas.sql` | Vistas | 6 vistas |
| `01_ddl/04_programas.sql` | Funciones, procedimientos y disparadores | 2 funciones, 3 procedimientos y 9 disparadores |

Table: Scripts de creación de objetos

#### Decisiones del modelo físico

- **Motor InnoDB y utf8mb4.** InnoDB es el motor de MySQL que maneja llaves foráneas y transacciones. La base usa
  `utf8mb4` para guardar bien las tildes y la ñ.
- **Restricciones con nombre.** Todas siguen la convención `PK_`, `FK_`, `UQ_` y `CK_` + tabla + columna. Así, un error
  como `Check constraint 'CK_MASCOTA_SEXO' is violated` dice exactamente qué regla se rompió.
- **Relación circular SEDE–EMPLEADO.** `FK_SEDE_DIRECTOR` se crea con `ALTER TABLE` después de las dos tablas.
- **AUTO_INCREMENT.** Las tablas con llave numérica propia generan sus identificadores con `AUTO_INCREMENT`. Como los
  datos de prueba se insertan con su id, MySQL continúa después del último identificador cargado.
- **Comentarios en la misma tabla.** El diccionario de datos queda dentro de la base con `COMMENT` en cada tabla y en
  cada columna, y se ve en la pestaña *Columns* de Workbench.
- **Vistas para atributos derivados.** El total de la factura (`V_FACTURA_TOTAL`) y la edad de la mascota
  (`V_MASCOTA_DETALLE`) se calculan y no se guardan, para mantener la Tercera Forma Normal.
- **Disparadores para reglas que no caben en un CHECK.** Un CHECK de MySQL no puede consultar otra tabla, así que las
  reglas RN-08 y RN-09 del documento de supuestos se hicieron con disparadores. En MySQL un disparador cubre un solo
  evento (INSERT o UPDATE), por eso cada regla vive en un procedimiento que se llama desde los dos disparadores. Los
  errores se lanzan con `SIGNAL SQLSTATE '45000'` y un código propio (20001 a 20006):

| Disparador | Procedimiento / regla |
|---|---|
| `TRG_VETERINARIO_TIPO_INS` y `_UPD` | `PR_VALIDAR_TIPO_EMPLEADO`: solo un empleado con `tipo_empleado = 'VET'` puede ser veterinario (RN-09, error 20001) |
| `TRG_AUXILIAR_TIPO_INS` y `_UPD` | `PR_VALIDAR_TIPO_EMPLEADO`: solo un empleado con `tipo_empleado = 'AUX'` puede ser auxiliar (RN-09, error 20002) |
| `TRG_CITA_VETERINARIO_SEDE_INS` y `TRG_CITA_UPD` | `PR_VALIDAR_SEDE_VETERINARIO`: el veterinario de la cita debe trabajar en la sede de la cita (RN-08, error 20003) |
| `TRG_CONSULTA_CITA_ATENDIDA_INS` y `_UPD` | `PR_VALIDAR_CITA_ATENDIDA`: solo una cita ATENDIDA puede generar consulta (supuesto S-28, error 20004) |
| `TRG_EMPLEADO_TIPO` | No se puede cambiar el tipo de un empleado que ya tiene fila de subtipo (error 20005) |
| `TRG_CITA_UPD` | Además, una cita que ya generó consulta no puede dejar de estar ATENDIDA (error 20006) |

Table: Disparadores de reglas de negocio

### Scripts de inserción de registros

La carpeta `02_dml/` contiene 23 archivos (uno por tabla, más la asignación de directores) con **1.091 registros** de
prueba ficticios. El orden respeta las dependencias de las llaves foráneas:

```text
01_departamento → 02_ciudad → 03_sede (sin director) → 04_empleado → 05_sede_director (UPDATE)
→ 06_veterinario → 07_auxiliar → 08_especialidad → 09_veterinario_especialidad → 10_cliente
→ 11_telefono_cliente → 12_especie → 13_raza → 14_mascota → 15_historia_clinica → 16_servicio
→ 17_cita → 18_consulta → 19_medicamento → 20_prescripcion → 21_inventario → 22_factura → 23_detalle_factura
```

Las fechas se escriben en formato ISO (`'2025-10-08 16:13:00'`), que MySQL entiende sin importar la configuración
regional. Cada archivo empieza con `USE sigevet;` y agrupa sus INSERT en una transacción (`START TRANSACTION` …
`COMMIT`).

### Scripts varios

| Script | Propósito |
|---|---|
| `03_varios/01_consultas_algebra_relacional.sql` | Las 55 consultas SQL equivalentes a los ejercicios de álgebra relacional de la Fase 1 |
| `03_varios/02_verificacion.sql` | Objetos, restricciones y registros por tabla, y prueba de que las reglas de negocio rechazan datos inválidos |
| `03_varios/03_reiniciar_base.sql` | Borra la base `sigevet` y la crea vacía para reinstalar (los usuarios se conservan) |
| `instalar_esquema.sql` | Script maestro: ejecuta todo en orden con `SOURCE` y termina con la verificación |
| `vm/scripts/cargar_bd.sh` | Crea la base y los usuarios (si no existen) y ejecuta el script maestro dentro de la VM |

Table: Scripts varios

### Ejecución y resultados

<!-- Created: 18:15 19-Oct-2026 -->

**Creación de la base y carga del esquema** (salida real, archivo `vm/logs/04_carga_bd.log`):

```text
== Borrando la base sigevet
resultado
Base sigevet vacía
== Instalando el esquema SIGEVET
+-------------------------------------------+
| paso                                      |
+-------------------------------------------+
| == 1/6 Tablas y restricciones (22 tablas) |
+-------------------------------------------+
+------------------------------+
| paso                         |
+------------------------------+
| == 2/6 Índices de búsqueda   |
+------------------------------+
+----------------------------------------------------------------------+
| paso                                                                 |
+----------------------------------------------------------------------+
| == 3/6 Funciones, procedimientos y disparadores (antes de los datos) |
+----------------------------------------------------------------------+
+------------------------------------------+
| paso                                     |
+------------------------------------------+
| == 4/6 Datos de prueba (1.091 registros) |
+------------------------------------------+
+---------------+
| paso          |
+---------------+
| == 5/6 Vistas |
+---------------+
+----------------------+
| paso                 |
+----------------------+
| == 6/6 Verificación  |
+----------------------+
+-------------------------------+
| seccion                       |
+-------------------------------+
| == Objetos de la base sigevet |
+-------------------------------+
+------------+----------+
| objeto     | cantidad |
+------------+----------+
| TABLA      |       22 |
| VISTA      |        6 |
| FUNCTION   |        2 |
| PROCEDURE  |        3 |
| DISPARADOR |        9 |
| INDICE     |       71 |
+------------+----------+
+---------------------------+
| seccion                   |
+---------------------------+
| == Restricciones por tipo |
+---------------------------+
+-------------+----------+
| tipo        | cantidad |
+-------------+----------+
| CHECK       |       36 |
| FOREIGN KEY |       30 |
| PRIMARY KEY |       22 |
| UNIQUE      |       25 |
+-------------+----------+
+------------------------+
| seccion                |
+------------------------+
| == Registros por tabla |
+------------------------+
+--------------------------+-----------+
| tabla                    | registros |
+--------------------------+-----------+
| DEPARTAMENTO             |         6 |
| CIUDAD                   |        11 |
| SEDE                     |         5 |
| EMPLEADO                 |        27 |
| VETERINARIO              |        12 |
| AUXILIAR                 |         8 |
| ESPECIALIDAD             |         8 |
| VETERINARIO_ESPECIALIDAD |        12 |
| CLIENTE                  |        45 |
| TELEFONO_CLIENTE         |        66 |
| ESPECIE                  |         5 |
| RAZA                     |        20 |
| MASCOTA                  |        59 |
| HISTORIA_CLINICA         |        59 |
| SERVICIO                 |        14 |
| CITA                     |       144 |
| CONSULTA                 |        95 |
| MEDICAMENTO              |        18 |
| PRESCRIPCION             |        88 |
| INVENTARIO               |        79 |
| FACTURA                  |       109 |
| DETALLE_FACTURA          |       201 |
+--------------------------+-----------+
+------------------------------------------------------------------------------------------+
| seccion                                                                                  |
+------------------------------------------------------------------------------------------+
| == Prueba de reglas de negocio (cada intento debe ser RECHAZADO con el código esperado)  |
+------------------------------------------------------------------------------------------+
+-----------+-------------------------------+--------------------------------------------------------------------------------------------------+
| resultado | prueba                        | error                                                                                            |
+-----------+-------------------------------+--------------------------------------------------------------------------------------------------+
| OK        | FK a departamento inexistente | 1452: Cannot add or update a child row: a foreign key constraint fails (`sigevet`.`CIUDAD`, CONS |
+-----------+-------------------------------+--------------------------------------------------------------------------------------------------+
+-----------+-----------------------+-------------------------------------------------------+
| resultado | prueba                | error                                                 |
+-----------+-----------------------+-------------------------------------------------------+
| OK        | CHECK sexo de mascota | 3819: Check constraint 'CK_MASCOTA_SEXO' is violated. |
+-----------+-----------------------+-------------------------------------------------------+
+-----------+-----------------------------+-----------------------------------------------------------------------------------------------+
| resultado | prueba                      | error                                                                                         |
+-----------+-----------------------------+-----------------------------------------------------------------------------------------------+
| OK        | UNIQUE documento de cliente | 1062: Duplicate entry 'CC-52209458' for key 'CLIENTE.UQ_CLIENTE_TIPO_DOCUMENTO_NUM_DOCUMENTO' |
+-----------+-----------------------------+-----------------------------------------------------------------------------------------------+
+-----------+----------------------------+---------------------------------------------------------------+
| resultado | prueba                     | error                                                         |
+-----------+----------------------------+---------------------------------------------------------------+
| OK        | Arco exclusivo del detalle | 3819: Check constraint 'CK_DETALLE_FACTURA_ITEM' is violated. |
+-----------+----------------------------+---------------------------------------------------------------+
+-----------+-------------------+-----------------------------------------+
| resultado | prueba            | error                                   |
+-----------+-------------------+-----------------------------------------+
| OK        | RN-09 subtipo VET | 20001: El empleado 6 no es de tipo VET. |
+-----------+-------------------+-----------------------------------------+
+-----------+--------------------------------+---------------------------------------------------------------+
| resultado | prueba                         | error                                                         |
+-----------+--------------------------------+---------------------------------------------------------------+
| OK        | RN-08 veterinario de otra sede | 20003: El veterinario 1 labora en la sede 1, no en la sede 5. |
+-----------+--------------------------------+---------------------------------------------------------------+
+-----------+------------------------------+-----------------------------------------------------------------------------------------+
| resultado | prueba                       | error                                                                                   |
+-----------+------------------------------+-----------------------------------------------------------------------------------------+
| OK        | Consulta de cita no atendida | 20004: La cita 22 está en estado CANCELADA; solo las citas ATENDIDAS generan consulta.  |
+-----------+------------------------------+-----------------------------------------------------------------------------------------+
+-----------+---------------------------------+-----------------------------------------------------------------------------------------+
| resultado | prueba                          | error                                                                                   |
+-----------+---------------------------------+-----------------------------------------------------------------------------------------+
| OK        | Mover consulta a cita cancelada | 20004: La cita 22 está en estado CANCELADA; solo las citas ATENDIDAS generan consulta.  |
+-----------+---------------------------------+-----------------------------------------------------------------------------------------+
+-----------+----------------------------+--------------------------------------------------------------------------------------------+
| resultado | prueba                     | error                                                                                      |
+-----------+----------------------------+--------------------------------------------------------------------------------------------+
| OK        | Cambio de tipo con subtipo | 20005: El empleado 1 tiene registro de subtipo; elimínelo antes de cambiar tipo_empleado.  |
+-----------+----------------------------+--------------------------------------------------------------------------------------------+
+-----------+---------------------------------+------------------------------------------------------------------------------+
| resultado | prueba                          | error                                                                        |
+-----------+---------------------------------+------------------------------------------------------------------------------+
| OK        | Cita con consulta deja ATENDIDA | 20006: La cita 4 ya tiene consulta registrada; no puede cambiar a CANCELADA. |
+-----------+---------------------------------+------------------------------------------------------------------------------+
+------------------------------------+
| seccion                            |
+------------------------------------+
| == Atributos derivados (funciones) |
+------------------------------------+
+----------------+-----------+
| numero_factura | total     |
+----------------+-----------+
| FV-000001      | 364000.00 |
| FV-000002      |  80500.00 |
| FV-000003      | 400000.00 |
+----------------+-----------+
+---------------------------+
| paso                      |
+---------------------------+
| == Instalación terminada  |
+---------------------------+
Tiempo: 2 s
EXIT 0
```


### Ejecución de las consultas de álgebra relacional en MySQL

Las 55 sentencias del script `03_varios/01_consultas_algebra_relacional.sql` se ejecutaron en MySQL (usuario vetdb) y su resultado se comparó fila por fila con el publicado en el documento de álgebra relacional de la Fase 1. **55 de 55 consultas producen exactamente el mismo resultado en MySQL y en la Fase 1.**

| Ejercicio | Filas en SQLite (Fase 1) | Filas en MySQL | Resultado idéntico |
|---|---|---|---|
| 1. Servicios de vacunación | 3 | 3 | Sí |
| 2. Hembras esterilizadas activas | 17 | 17 | Sí |
| 3. Empleados con salario alto | 12 | 12 | Sí |
| 4. Medicamentos con fórmula de bajo costo | 6 | 6 | Sí |
| 5. Citas perdidas | 31 | 31 | Sí |
| 6. Razas de tamaño grande | 3 | 3 | Sí |
| 7. Teléfonos fijos | 19 | 19 | Sí |
| 8. Clientes sin correo electrónico | 5 | 5 | Sí |
| 9. Consultas con fiebre | 8 | 8 | Sí |
| 10. Categorías de servicio | 7 | 7 | Sí |
| 11. Agenda futura | 18 | 18 | Sí |
| 12. Inventario bajo el mínimo | 19 | 19 | Sí |
| 13. Tabla de precios de vacunación por especie | 15 | 15 | Sí |
| 14. Razas felinas mediante producto y selección | 5 | 5 | Sí |
| 15. Veterinarios graduados el mismo año | 6 | 6 | Sí |
| 16. Ciudades con su departamento | 11 | 11 | Sí |
| 17. Datos completos de los veterinarios | 12 | 12 | Sí |
| 18. Existencias de la sede Usaquén | 16 | 16 | Sí |
| 19. Mascotas con alergias | 33 | 33 | Sí |
| 20. Gatos y sus propietarios | 14 | 14 | Sí |
| 21. Sedes, ubicación y director | 5 | 5 | Sí |
| 22. Especialidades de cada veterinario | 12 | 12 | Sí |
| 23. Fórmula médica de una consulta | 3 | 3 | Sí |
| 24. Historia clínica de una mascota | 4 | 4 | Sí |
| 25. Citas atendidas en la sede Chía | 16 | 16 | Sí |
| 26. Clientes sin mascotas | 2 | 2 | Sí |
| 27. Especialidades con o sin veterinarios | 14 | 14 | Sí |
| 28. Todas las citas de una mascota, con o sin consulta | 4 | 4 | Sí |
| 29. Comparación de inventarios entre sedes | 18 | 18 | Sí |
| 30. Clientes con facturas anuladas | 3 | 3 | Sí |
| 31. Mascotas sin atenciones | 9 | 9 | Sí |
| 32. Veterinarios que atendieron urgencias | 6 | 6 | Sí |
| 33. Personas registradas en Chía | 9 | 9 | Sí |
| 34. Mascotas vacunadas u operadas | 27 | 27 | Sí |
| 35. Medicamentos comunes a dos sedes | 14 | 14 | Sí |
| 36. Clientes con perro y gato | 4 | 4 | Sí |
| 37. Medicamentos nunca formulados | 1 | 1 | Sí |
| 38. Veterinarios sin especialidad | 3 | 3 | Sí |
| 39. Medicamentos que no maneja la sede Chía | 1 | 1 | Sí |
| 40. Clientes de mostrador | 1 | 1 | Sí |
| 41. Medicamentos disponibles en todas las sedes | 8 | 8 | Sí |
| 42. Veterinarios con un conjunto de especialidades | 1 | 1 | Sí |
| 43. Clientes atendidos en todas las sedes de Bogotá | 6 | 6 | Sí |
| 44. Mascotas por especie | 5 | 5 | Sí |
| 45. Citas por estado | 4 | 4 | Sí |
| 46. Ingresos por sede | 5 | 5 | Sí |
| 47. Salarios por tipo de empleado | 3 | 3 | Sí |
| 48. Veterinario con más citas atendidas | 1 | 1 | Sí |
| 49. Medicamentos más formulados | 5 | 5 | Sí |
| 50. Clientes con más de dos mascotas | 5 | 5 | Sí |
| 51. Facturación mensual de 2026 | 10 | 10 | Sí |
| 52. Valor del inventario por sede | 5 | 5 | Sí |
| 53. Edad promedio por especie | 5 | 5 | Sí |
| 54. Ticket promedio por medio de pago | 4 | 4 | Sí |
| 55. Servicios que más ingresos generan | 5 | 5 | Sí |

Table: Comparación de resultados de las consultas de álgebra relacional


### README

El archivo `database/README.md` del repositorio describe la estructura de carpetas, el orden de ejecución, cómo
ejecutar los scripts dentro de la VM con el cliente `mysql` o desde MySQL Workbench, el manejo de credenciales y el
origen de los datos de prueba.

## Conexión de la Aplicación Web a la Base de Datos Relacional



### Introducción

SIGEVET incluye una aplicación web de consulta que se conecta a la base de datos MySQL de la máquina virtual y
muestra, en tiempo real, la información de la red de clínicas: clientes, mascotas e historias clínicas, agenda,
inventario, facturas, personal y los 55 ejercicios de álgebra relacional. Su propósito es demostrar la conectividad y
la lectura de datos que pide el enunciado.

### Arquitectura

La aplicación está hecha en **Java 21 con Servlets y JSP**, sin frameworks, y se despliega como `ROOT.war` en
**Apache Tomcat 10.1**. Tomcat corre **dentro de la máquina virtual**, junto a MySQL, como un servicio del sistema
(`sigevet-web`) en el puerto 5000. Desde el equipo anfitrión solo se abre el navegador.

```text
Navegador ──HTTP 5000──▶ Tomcat ──▶ Servlets ──▶ SQL escrito a mano (Consultas.java)
                                       │              │ JDBC: DriverManager + PreparedStatement (Connector/J)
                                       ▼              ▼
                                 Páginas JSP    MySQL 8.4 · base sigevet (usuario vet_app)
```

| Archivo (`app/src/main/`) | Qué hace |
|---|---|
| `java/.../Conexion.java` | Abre la conexión con `DriverManager` usando las variables de entorno |
| `java/.../BaseDatos.java` | Ejecuta el SQL con `PreparedStatement` y `ResultSet` y devuelve las filas |
| `java/.../Consultas.java` | Las consultas SQL de cada página, escritas a mano como texto |
| `java/.../*Servlet.java` | Un servlet por página: inicio, clientes, cliente, mascota, agenda, inventario, facturas, factura, personal y álgebra |
| `java/.../Parametros.java` | Lee y valida los parámetros de la URL (números, fechas y página) |
| `java/.../Algebra.java` | Lee los 55 ejercicios del script `01_consultas_algebra_relacional.sql` |
| `java/.../Formato.java` y `webapp/WEB-INF/sigevet.tld` | Formato de fechas (dd/MM/yyyy) y de dinero ($ 364.000) para usar en las JSP |
| `webapp/WEB-INF/jsp/*.jsp` | Páginas HTML con JSP y JSTL |
| `webapp/WEB-INF/web.xml` | Páginas de error 404 y 500, y codificación UTF-8 |

Table: Archivos de la aplicación

**Sin frameworks ni ORM.** La aplicación no usa Spring, JPA, Hibernate ni ninguna herramienta que genere SQL de
forma automática. Cada servlet recibe la petición, ejecuta su consulta SQL con JDBC y le pasa las filas a una página
JSP. Así se ve la relación directa entre las consultas de la aplicación, el modelo relacional y el álgebra relacional
de la Fase 1.

### Conexión a MySQL

La conexión usa **MySQL Connector/J** (`mysql-connector-j`), el controlador JDBC oficial de MySQL. Cada consulta abre una
conexión con `DriverManager` y la cierra al terminar (`Conexion.java`):

```java
Class.forName("com.mysql.cj.jdbc.Driver");
return DriverManager.getConnection(url, usuario, clave);
```

La URL ya incluye la base de datos (`jdbc:mysql://localhost:3306/sigevet`), así que las consultas usan los nombres de
las tablas sin prefijo.

Ejemplo de una consulta (`Consultas.java`). Los `?` son parámetros que se llenan con `PreparedStatement`:

```sql
SELECT c.id_cliente, c.tipo_documento, c.num_documento, c.nombres, c.apellidos, c.email, ci.nombre AS ciudad
FROM CLIENTE c JOIN CIUDAD ci ON ci.id_ciudad = c.id_ciudad
WHERE UPPER(c.num_documento) LIKE ? OR UPPER(c.apellidos) LIKE ?
ORDER BY c.apellidos, c.nombres, c.id_cliente
OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
```

Así se ejecuta (`BaseDatos.java`, resumido):

```java
try (Connection conexion = Conexion.obtener();
     PreparedStatement sentencia = conexion.prepareStatement(sql)) {
    for (int i = 0; i < parametros.length; i++) {
        sentencia.setObject(i + 1, parametros[i]);
    }
    try (ResultSet resultado = sentencia.executeQuery()) {
        while (resultado.next()) {
            // cada fila se guarda como un Map columna -> valor
        }
    }
}
```

Y un servlet completo (`ClienteServlet.java`, resumido):

```java
@WebServlet("/clientes/*")
public class ClienteServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) ... {
        Long id = Parametros.id(request.getPathInfo());
        var cliente = BaseDatos.consultarUno(Consultas.SQL_CLIENTE, id);
        if (cliente == null) { response.sendError(404); return; }
        ...
        request.setAttribute("datos", datos);
        request.getRequestDispatcher("/WEB-INF/jsp/cliente.jsp").forward(request, response);
    }
}
```

| Dato | Valor usado en el proyecto | Variable de entorno |
|---|---|---|
| Usuario | `vet_app` (solo lectura) | `SIGEVET_DB_USER` |
| Clave | No se escribe en el código | `SIGEVET_DB_PASSWORD` |
| URL JDBC | `jdbc:mysql://localhost:3306/sigevet` (la app corre en la misma VM) | `SIGEVET_DB_URL` |

Table: Configuración de la conexión

En la VM, `vm/scripts/instalar_app.sh` guarda estos valores en `/opt/sigevet/app.env` (con permisos solo para el
dueño), y el servicio de Tomcat los lee de ahí.

### Seguridad

- **Mínimo privilegio:** la aplicación usa `vet_app`, que solo tiene `SELECT` mediante el rol `rol_sigevet_lectura`.
  Aunque la aplicación tuviera un error, no podría modificar ni borrar datos.
- **Consultas parametrizadas:** los filtros del usuario (búsqueda, fechas, sede, estado) se pasan como parámetros
  `?` del `PreparedStatement` y nunca se pegan al texto del SQL. Así se evita la inyección de SQL.
- **Escape en las páginas:** los textos que vienen de la base se muestran con `<c:out>` de JSTL, que escapa el HTML.
- **Álgebra relacional:** la página solo ejecuta las 55 consultas del script del proyecto. La URL elige un número de
  ejercicio, no envía SQL.

### Ejecución

En la máquina virtual (forma recomendada), desde la raíz del proyecto:

```bash
sudo bash vm/scripts/instalar_app.sh     # instala Java 21 y Tomcat 10.1, compila ROOT.war y deja el servicio activo
systemctl status sigevet-web
```

En cualquier equipo con Java 21 y Tomcat 10.1:

```bash
cd app
./mvnw package                            # genera target/ROOT.war
cp target/ROOT.war <tomcat>/webapps/
export SIGEVET_DB_PASSWORD='<clave de vet_app>'
<tomcat>/bin/catalina.sh run              # http://localhost:8080
```

### Evidencias de funcionamiento

La aplicación Java (Servlets + JSP en Tomcat) se ejecutó como servicio dentro de la VM, conectada a MySQL con el usuario `vet_app`, y se abrió
desde el navegador del equipo anfitrión. Estas son las páginas probadas contra la base de datos real:

| Ruta | Respuesta | Qué muestra |
|---|---|---|
| `/` | HTTP 200 | Inicio: conteo de tablas, ingresos por sede, citas por estado, stock bajo y próximas citas |
| `/clientes` | HTTP 200 | Lista de los 45 clientes con búsqueda por documento o apellido y paginación |
| `/clientes/8` | HTTP 200 | Cliente con sus teléfonos, 3 mascotas y facturas |
| `/mascotas/17` | HTTP 200 | Historia clínica de Bruno con sus consultas y fórmulas |
| `/agenda?sede=3` | HTTP 200 | Agenda de la sede Chía con filtros |
| `/inventario` | HTTP 200 | Inventario por sede con alertas de stock mínimo |
| `/facturas/1` | HTTP 200 | Factura FV-000001 con líneas, descuentos y total ($ 364.000) |
| `/personal` | HTTP 200 | Personal por sede, director y especialidades |
| `/algebra/1` a `/algebra/55` | HTTP 200 | Los 55 ejercicios de álgebra ejecutados en MySQL |
| `/algebra/99` | HTTP 404 | Ejercicio que no existe: se rechaza sin ejecutar nada |
| `/clientes/99999` | HTTP 404 | Cliente que no existe |

Table: Páginas de la aplicación probadas contra MySQL

![Página de inicio con indicadores de la red](diagramas/png/app_inicio_121329-21Oct2026.png){width=95%}

![Detalle de un cliente con teléfonos, mascotas y facturas](diagramas/png/app_cliente_121329-21Oct2026.png){width=95%}

![Historia clínica de una mascota (consultas y prescripciones)](diagramas/png/app_mascota_121329-21Oct2026.png){width=95%}

![Agenda de citas filtrada por sede](diagramas/png/app_agenda_121329-21Oct2026.png){width=95%}

![Inventario con alertas de stock por debajo del mínimo](diagramas/png/app_inventario_121329-21Oct2026.png){width=95%}

![Detalle de la factura FV-000001](diagramas/png/app_factura_121329-21Oct2026.png){width=95%}

![Personal por sede con directores y especialidades](diagramas/png/app_personal_121329-21Oct2026.png){width=95%}

![Ejercicio 41 de álgebra relacional (división) ejecutado en MySQL](diagramas/png/app_algebra_121329-21Oct2026.png){width=95%}

## Repositorio en Git con la Aplicación Web y los Scripts de Base de Datos



### Introducción

El código y la documentación de SIGEVET se manejan con **Git** y se publican en **GitHub**. El repositorio tiene la
aplicación web, los scripts de base de datos, los scripts de la máquina virtual y los documentos entregados. Con él,
cualquier integrante (o el docente) puede montar el proyecto completo desde cero.

### Estructura

| Carpeta | Contenido |
|---|---|
| `database/` | Scripts SQL de creación de objetos, inserción de registros y varios, con su `README.md` |
| `app/` | Aplicación web en Java (Servlets + JSP) de solo lectura, con su `README.md`. Todo el SQL está escrito a mano y se ejecuta con JDBC, sin ORM |
| `vm/` | Scripts para instalar MySQL, cargar la base e instalar la app en la VM, la definición de la VM de simulación (Lima) y las evidencias |
| `documentos/` | Documentos entregados en la Fase 1, la Fase 2 y los transversales, los modelos de MySQL Workbench (`.mwb`) y los diagramas |

Table: Estructura del repositorio

### Lo que no se versiona

| Elemento | Motivo |
|---|---|
| `vm/claves.env` | Tiene las claves; cada integrante crea el suyo a partir de `vm/claves.ejemplo` |
| `vm/cache/` | Archivos temporales y descargas de la VM |
| `app/target/` | Archivos compilados por Maven (el `ROOT.war` se genera con `./mvnw package`) |

Table: Exclusiones del archivo .gitignore

### Flujo de trabajo del equipo

1. Cada integrante clona el repositorio y trabaja en su parte (modelado, scripts, infraestructura, aplicación).
2. Antes de subir cambios a la base de datos, prueba los scripts con `bash vm/scripts/cargar_bd.sh --reiniciar` en su
   VM.
3. Los mensajes de commit siguen el formato `tipo: descripción` (`feat`, `fix`, `docs`, `chore`).

| Integrante | Aportes principales en el repositorio |
|---|---|
| Juan David Polo Herrera | Especificación del modelo, scripts DDL y DML, carga en MySQL, verificación |
| Sebastián Castellanos Parra | Supuestos, modelos Chen/EER, modelos en MySQL Workbench, diccionario de datos |
| Juan Miguel Ramos | Máquina virtual, instalación del RDBMS, aplicación web |

Table: Aportes por integrante

### Evidencias

Historial de commits del repositorio (`git log --reverse --format='%h | %an | %s'`):

| Commit | Autor | Descripción |
|---|---|---|
| fb00ade | Juan David Polo Herrera | chore: estructura inicial del proyecto y README |
| c445b59 | Juan David Polo Herrera | docs: entregables de la fase 1 (propuesta, supuestos, modelos, diccionario y algebra) |
| ed5428c | Juan David Polo Herrera | feat: scripts de creacion de usuarios, tablas, vistas, funciones y disparadores |
| 8944cc4 | Juan David Polo Herrera | feat: scripts de insercion de registros de prueba |
| 333f716 | Juan David Polo Herrera | feat: consultas de algebra, verificacion, permisos y script maestro |
| 8e501a7 | Juan David Polo Herrera | feat: scripts de la maquina virtual para instalar oracle y cargar la base |
| 025d856 | Juan David Polo Herrera | docs: evidencias de la instalacion en la VM |
| a319b1c | Juan David Polo Herrera | feat: aplicacion web en flask conectada a oracle (solo lectura) |
| 7b708a6 | Juan David Polo Herrera | docs: documentos de la fase 2 y transversales |
| 39dca6a | Juan David Polo Herrera | docs: actualizar documentos con el historial del repositorio |
| aa17468 | Juan David Polo Herrera | fix: disparador de consulta tambien valida UPDATE, ajustes de scripts y textos |
| 26d306c | Juan David Polo Herrera | feat: migrar la aplicacion web a java con spring boot y jdbc (sin orm) |
| adb6f4f | Juan David Polo Herrera | feat: instalar la app java como servicio en la VM y prueba del usuario VET_APP |
| 3ef9058 | Juan David Polo Herrera | docs: actualizar documentos a la aplicacion en java |
| 3f5c09f | Juan David Polo Herrera | docs: historial de commits actualizado |
| 8c04fe4 | Juan David Polo Herrera | refactor: app web con servlets y jsp sobre tomcat, sin spring boot (jdbc puro) |
| ed152b7 | Juan David Polo Herrera | feat: instalar tomcat y desplegar ROOT.war como servicio en la VM |
| 39a1e6b | Juan David Polo Herrera | docs: documentos actualizados a servlets y jsp |
| 2830233 | Juan David Polo Herrera | docs: historial de commits actualizado |
| b5c91d1 | Juan David Polo Herrera | feat: pasar la base de datos de oracle a mysql 8.4 (tablas, datos, vistas, disparadores y algebra) |
| 560fd5f | Juan David Polo Herrera | feat: instalar mysql 8.4 en la VM, carga de la base y acceso para mysql workbench |
| b3ec407 | Juan David Polo Herrera | feat: la app web se conecta a mysql con connector/j |
| 8ff6933 | Juan David Polo Herrera | docs: README principal con mysql y workbench |
| dd98c21 | Juan David Polo Herrera | docs: documentos actualizados a mysql y modelos en mysql workbench |
| adb73a3 | Juan David Polo Herrera | docs: historial de commits actualizado |
| 2b87fed | Juan David Polo Herrera | feat: script para crear la VM completa en el computador servidor con linux |
| 6225086 | Juan David Polo Herrera | docs: documento final, presentacion de sustentacion y procedimiento del servidor |

Table: Historial de commits

El repositorio se publicará en GitHub cuando los tres integrantes hayan registrado sus aportes; la dirección pública se agregará a este documento en ese momento.

## Guion del Video de Demostración (máximo 15 minutos)



### Propósito y requisitos

El enunciado pide un video de **máximo 15 minutos** que muestre el proceso de desarrollo, la manipulación de la base
de datos y la aplicación final, con la participación de **todos** los integrantes **con audio y con cámara**. Este
guion reparte el contenido en bloques cronometrados (total previsto: 13 min 30 s, con margen).

| Requisito | Cómo se cumple |
|---|---|
| Duración ≤ 15 min | Bloques que suman 13:30 |
| Todos los integrantes | Cada integrante presenta al menos dos bloques y aparece en la introducción y el cierre |
| Audio y cámara | Grabar con cámara activa en recuadro (por ejemplo, Zoom, Teams u OBS con cámara superpuesta) |
| Proceso de desarrollo | Bloques 2 y 3 (modelado y decisiones) |
| Manipulación de la base de datos | Bloques 4 a 6 (VM, scripts, consultas) |
| Resultado final | Bloques 7 y 8 (aplicación web y repositorio) |

Table: Requisitos del video

### Preparación (antes de grabar)

1. Encender la VM en el computador servidor (`limactl start sigevet`) y esperar a que arranquen MySQL y la aplicación web
   (`systemctl status mysqld sigevet-web`).
2. Abrir: una terminal SSH dentro de la VM, MySQL Workbench conectado a `127.0.0.1:3306` (usuario `vetdb`),
   la aplicación web en `http://127.0.0.1:5000`, el modelo `SIGEVET.mwb` en otra pestaña de Workbench y el
   repositorio en GitHub.
3. Configurar la grabación con pantalla compartida y cámara visible de quien habla.

### Guion

| # | Tiempo | Integrante | Qué se muestra | Qué se dice (idea principal) |
|---|---|---|---|---|
| 1 | 0:00–1:00 | Los tres | Cámaras de los tres; portada del proyecto | Presentación del grupo, la red de clínicas Huella Sana y el problema que resuelve SIGEVET |
| 2 | 1:00–3:00 | Sebastián Castellanos Parra | Diagrama Chen completo, EER y documento de supuestos | Entidades, relaciones 1:1, 1:N y N:M, especialización de EMPLEADO, supuestos clave |
| 3 | 3:00–4:30 | Sebastián Castellanos Parra | Workbench: modelo EER (Barker y Crow's Foot) y modelo relacional `SIGEVET.mwb`; diccionario de datos | Paso del MER al modelo relacional, normalización hasta 3FN (teléfonos, raza→especie, ciudad→departamento) |
| 4 | 4:30–6:00 | Juan Miguel Ramos | Terminal en el servidor: `limactl list`, `limactl shell sigevet`, `cat /etc/oracle-release`, `free -h`, `systemctl status mysqld` | Por qué Oracle Linux; recursos y red de la VM; simulación previa en Mac con Lima |
| 5 | 6:00–8:00 | Juan David Polo Herrera | Terminal: `bash vm/scripts/cargar_bd.sh --reiniciar` y la salida de la verificación | Orden de los scripts, 22 tablas, 113 restricciones, 1.091 registros, disparadores que rechazan datos inválidos |
| 6 | 8:00–10:00 | Juan David Polo Herrera | MySQL Workbench: 3 o 4 consultas del script de álgebra (una reunión, una división, una agregación) | Relación entre la expresión de álgebra relacional y el SQL; resultado de cada consulta |
| 7 | 10:00–12:30 | Juan Miguel Ramos | Aplicación web: inicio, clientes, historia clínica de una mascota, agenda, inventario, facturas y página de álgebra | La app corre dentro de la VM como servicio; es Java con Servlets y JSP sobre Tomcat y se conecta por JDBC al usuario de solo lectura VET_APP; todo el SQL está escrito a mano (sin ORM) |
| 8 | 12:30–13:30 | Los tres | GitHub: estructura del repositorio y commits de cada integrante | Trabajo colaborativo, lecciones aprendidas y cierre |

Table: Guion por bloques

### Consultas sugeridas para el bloque 6

- Ejercicio 22: sedes con su ciudad, departamento y director (reunión de cuatro tablas).
- Ejercicio 41: medicamentos disponibles en todas las sedes (división).
- Ejercicio 46: ingresos por sede (agregación con atributo calculado).
- Ejercicio 26: clientes sin mascotas (reunión externa).

### Recomendaciones de grabación

- Resolución 1920×1080, letra del terminal y del editor SQL en tamaño grande (16 pt o más).
- Ensayar una vez completo con cronómetro; si se pasa de 14 minutos, recortar el bloque 6 a dos consultas.
- Exportar en MP4 (H.264) y subir a YouTube como *no listado* o a OneDrive/Drive con enlace para el docente.

# Parte III. Gestión del proyecto


## Acta de constitución del proyecto



### Autorización y justificación

Se constituye SIGEVET, Sistema Integral de Gestión Veterinaria, como proyecto académico de Bases de Datos 1 de la Universidad El Bosque. Su propósito es integrar la información de las cinco sedes de la organización ficticia Huella Sana S.A.S.: Usaquén, Chapinero, Chía, Laureles y San Fernando.

Las agendas aisladas, las historias clínicas físicas y la facturación sin integración producen duplicidad de clientes, cruces de citas y consultas incompletas entre sedes. Se autoriza construir una base relacional centralizada y una aplicación web de consulta para demostrar integridad y trazabilidad clínica, operativa y comercial.

### Objetivos y criterios de éxito

#### Objetivo general

Diseñar e implementar SIGEVET en MySQL sobre una máquina virtual Oracle Linux, desde el modelo conceptual hasta la consulta web, para integrar la información de Huella Sana.

#### Objetivos específicos

- Documentar requerimientos, supuestos y reglas de negocio de las cinco sedes.
- Elaborar MER Chen, MER Extendido y modelos lógico y relacional en MySQL Workbench.
- Normalizar las 22 tablas del esquema a tercera forma normal y documentar tipos, dominios y restricciones.
- Verificar 55 ejercicios de álgebra relacional mediante sus consultas equivalentes y datos de prueba.
- Implementar VM, RDBMS y scripts reproducibles, y conectar la aplicación Java (Servlets y JSP) con el usuario de consulta `vet_app`.
- Mantener aportes verificables en Git/GitHub y presentar una demostración de máximo 15 minutos con audio y cámara de todos los integrantes.

Se considera satisfactorio el proyecto cuando el docente valida los entregables oficiales, los scripts reconstruyen la base `sigevet`, las consultas producen los resultados esperados y la aplicación acredita acceso de solo lectura. La validación de Fase 1 se ubica en Semana 8, la de Fase 2 en Semana 13 y el cierre en Semana 14.

### Alcance autorizado

#### Incluye

Estructura geográfica y sedes con director; empleados, veterinarios, auxiliares y especialidades; clientes con teléfonos; mascotas, razas y especies; historia clínica única por mascota; citas y consultas; prescripciones; inventario por sede y stock mínimo; facturas y sus detalles. Incluye modelado, normalización, implementación, datos ficticios y consultas web de demostración. Las operaciones de registro se demuestran mediante SQL; la aplicación web permite únicamente lectura.

#### No incluye

Facturación electrónica ante la DIAN, nómina, contabilidad, pagos en línea, compras a proveedores, lotes o vencimientos de medicamentos, autenticación de usuarios finales, aplicación móvil, alta disponibilidad ni réplicas. No se compromete una puesta en producción en clínicas reales.

### Entregables por fase

| Fase | Entregables oficiales |
|---|---|
| Fase 1 | Propuesta del proyecto; Documento de supuestos; MER notación Peter Chen; MER Extendido; MER Barker / Crow's Foot (MySQL Workbench); Modelo relacional Crow's Foot (MySQL Workbench); Diccionario de datos; Documento de álgebra relacional (55 ejercicios). |
| Fase 2 | Análisis de selección de SO; Implementación de la VM; Análisis de selección de RDBMS; Implementación del RDBMS; Scripts de creación de objetos, inserción de registros y varios con README; Conexión de la aplicación web; Repositorio Git; Video de demostración de máximo 15 minutos, con todos los integrantes con audio y cámara. |
| Transversal | Acta de constitución; Identificación de interesados; EDT/WBS; Cronograma; Presupuesto; Acta de cierre. |
Table: Productos sujetos a aceptación académica

La normalización forma parte del modelo relacional y tiene paquete propio en la EDT; no sustituye ningún entregable oficial.

### Hitos de control

| Orden | Hito | Referencia temporal |
|---|---|---|
| 1 | Inicio y constitución del proyecto | Semana 1 |
| 2 | Envío de la propuesta y la Fase 1 para validación del docente | Semana 8 |
| 3 | Entrega de la Fase 2 | Semana 13 |
| 4 | Sustentación, aceptación y cierre | Semana 14 |
Table: Secuencia de hitos autorizada

### Supuestos y restricciones

Se supone disponibilidad de los tres integrantes y de sus portátiles, acceso a Internet y retroalimentación del docente en las revisiones. Los actores de Huella Sana son perfiles simulados; el equipo valida reglas del caso con el docente, sin atribuir entrevistas a una empresa real.

El horizonte es de 14 semanas y 385 horas de esfuerzo distribuido, sin dedicación exclusiva. Se utilizarán Oracle Linux 9, MySQL 8.4 LTS con MySQL Workbench, una VM con KVM en el computador servidor (con simulación previa en Lima en macOS) y Java con Servlets/JSP sobre Tomcat, dentro de la VM de 6 GB prevista en la propuesta. Los datos serán ficticios y el esquema de consulta tendrá privilegios mínimos. El medio de virtualización se adecúa al equipo anfitrión sin cambiar el alcance funcional.

### Riesgos de alto nivel

| Riesgo | Probabilidad / impacto | Respuesta y responsable |
|---|---|---|
| Recursos insuficientes o incompatibilidad de virtualización | Media / Alto | Validar arquitectura, memoria y arranque antes de instalar MySQL; Juan Miguel Ramos. |
| Cambios tardíos de alcance | Media / Medio | Registrar solicitud, estimar horas e impacto y obtener validación del docente; Juan David Polo Herrera. |
| Cardinalidades o restricciones incorrectas | Baja / Alto | Revisar modelos, normalización y resultados de 55 consultas; Sebastián Castellanos Parra. |
| Aportes desiguales al repositorio | Media / Medio | Asignar paquetes, revisar avances semanalmente y verificar contribuciones; Juan David Polo Herrera. |
| Divergencia entre DDL, diccionario y diagramas | Media / Alto | Regenerar desde la especificación común y revisar diferencias; Juan David Polo Herrera. |
Table: Riesgos y respuestas iniciales

### Presupuesto resumido

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

### Dirección y responsabilidades

| Nombre e identificación | Rol | Responsabilidad y autoridad |
|---|---|---|
| Juan David Polo Herrera (1117506677) | Gerente del proyecto y DBA | Coordinar líneas base, modelo relacional, normalización, scripts y Git. |
| Sebastián Castellanos Parra (1031811214) | Analista y modelador de datos | Requerimientos, interesados, supuestos, Chen/EER, modelo lógico y diccionario; verificar trazabilidad. |
| Juan Miguel Ramos (1025528586) | Desarrollador web e infraestructura | Álgebra relacional y SQL, análisis de SO, VM, aplicación web y coordinación del video. |
| Ing., Esp., M.Ed. Christian Felipe Duarte Arévalo | Docente / patrocinador | Orientar, validar alcance y criterios de aceptación, evaluar fases y autorizar cierre académico; sin financiación monetaria asumida. |
Table: Equipo autorizado y patrocinio académico

### Aprobación

La firma formaliza la aceptación del alcance y de las líneas base. Los espacios se dejan para sus titulares.

| Nombre | Documento | Rol | Firma |
|---|---|---|---|
| Juan David Polo Herrera | C.C. 1117506677 | Gerente del proyecto y DBA | |
| Sebastián Castellanos Parra | C.C. 1031811214 | Analista y modelador de datos | |
| Juan Miguel Ramos | C.C. 1025528586 | Desarrollador web e infraestructura | |
| Ing., Esp., M.Ed. Christian Felipe Duarte Arévalo | | Docente / patrocinador | |

Table: Aprobación del acta de constitución

## Identificación de interesados



### Propósito y criterios del registro

Se identifican quienes definen, construyen, evalúan o representan necesidades de información de SIGEVET. El registro orienta las revisiones de reglas de negocio y la aceptación académica. Huella Sana S.A.S. y sus actores son ficticios; sus expectativas se utilizan para validar el caso con el docente y no corresponden a entrevistas realizadas.

El poder es la autoridad para aprobar alcance o aceptar productos; la influencia es la capacidad de orientar decisiones técnicas u operativas. Se califica alta, media o baja. Para la matriz, el poder medio se agrupa con poder bajo porque no implica autoridad de aprobación. El interés refleja el impacto esperado de los resultados.

### Registro de interesados

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

### Matriz de poder e interés

| Poder / interés | Interés alto | Interés bajo |
|---|---|---|
| Poder alto | Gestionar de cerca: I01, I02, I05 | Mantener satisfecho: I11 |
| Poder bajo o medio | Mantener informados: I03, I04, I06, I07, I08, I09, I10 | Monitorear: sin interesados identificados en este cuadrante |
Table: Matriz de clasificación dos por dos

El poder de I05 representa su función dentro del caso simulado; la aprobación académica corresponde a I01. El equipo no atribuye a I05 una autorización real sobre los recursos de la Universidad.

### Estrategia de participación

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

## EDT y diccionario de paquetes de trabajo



### Estructura orientada a entregables

La estructura de desglose del trabajo organiza el alcance completo de SIGEVET bajo la regla del cien por ciento: cada resultado se asigna a un único paquete de tercer nivel y cada paquete tiene un responsable de integración. El cronograma utiliza los mismos códigos; H1, H2 y H3 son hitos de control y no paquetes adicionales.

![EDT/WBS del proyecto SIGEVET](diagramas/png/edt_wbs.png){width=100%}

### Jerarquía y límites

| Código | Componente | Contenido consolidado |
|---|---|---|
| 1 | SIGEVET | Proyecto académico de base de datos y consulta web para Huella Sana. |
| 1.1 | Gestión del proyecto | Constitución, interesados y seguimiento de las líneas base. |
| 1.2 | Fase 1 Análisis y modelado | Propuesta, supuestos, modelos, normalización, diccionario y álgebra. |
| 1.3 | Fase 2 Implementación | Análisis de plataforma, VM, RDBMS, scripts, aplicación, repositorio y video. |
| 1.4 | Cierre | Sustentación, aceptación, balance y acta de cierre. |
Table: Niveles primero y segundo de la EDT

### Diccionario de la EDT

Para facilitar lectura de la tabla, JD corresponde a Juan David Polo Herrera, Gerente del proyecto y DBA; SC a Sebastián Castellanos Parra, Analista y modelador de datos; JM a Juan Miguel Ramos, Desarrollador web e infraestructura. La responsabilidad identifica al integrador del paquete; el equipo participa en las revisiones y en la sustentación.

#### Gestión del proyecto

| Código | Paquete | Descripción | Responsable | Entregable |
|---|---|---|---|---|
| 1.1.1 | Constitución | Definir objetivos, alcance, riesgos y autorización académica | JD | Acta de constitución. |
| 1.1.2 | Interesados | Registrar expectativas, poder, interés y participación | SC | Registro, matriz y estrategia de interesados. |
| 1.1.3 | Seguimiento | Integrar EDT, cronograma y presupuesto; revisar avance y cambios semanalmente | JD | EDT y diccionario; cronograma Excel y Gantt; presupuesto Excel; decisiones de revisión. |
Table: Paquetes de gestión

#### Fase 1 Análisis y modelado

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

#### Fase 2 Implementación

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

#### Cierre

| Código | Paquete | Descripción | Responsable | Entregable |
|---|---|---|---|---|
| 1.4.1 | Sustentación y aceptación | Consolidar evidencias, sustentar y atender observaciones académicas | SC | Lista de entregables y registro de aceptación en el acta. |
| 1.4.2 | Cierre documental | Registrar balance, desviaciones, lecciones y espacios de firma | JD | Acta de cierre. |
Table: Paquetes de cierre

### Control y aceptación de paquetes

Un paquete se considera completo cuando su entregable está versionado, es legible y supera la revisión del responsable y de otro integrante. En modelos se verifica correspondencia con reglas y DDL; en álgebra, resultados de los 55 ejercicios; en plataforma, reproducción y conexión; en video, duración y participación. El docente valida Fase 1 en Semana 8, Fase 2 en Semana 13 y el cierre en Semana 14.

El seguimiento de 1.1.3 y el repositorio de 1.3.7 transcurren en paralelo con las fases; sus horas son esfuerzo de coordinación e integración, sin duplicar el trabajo de los demás paquetes. Los cambios se registran por código y se evalúan contra alcance, dependencias y presupuesto antes de aprobarse.

## Cronograma del proyecto



### Línea base y criterios de programación

El cronograma distribuye SIGEVET entre Semana 1 y Semana 14, con 22 paquetes EDT y tres hitos de duración nula. El esfuerzo total es de 385 horas; una semana de duración indica una ventana de ejecución y no una dedicación de tiempo completo. La gestión y el repositorio se desarrollan en paralelo para apoyar las dos fases.

Las dependencias son de fin a inicio entre paquetes: el sucesor inicia en una semana posterior al fin de su predecesora. Los hitos se verifican al final de la semana de sus productos; por ello pueden compartir semana con las actividades que los habilitan. Los paquetes 1.4.1 y 1.4.2 se integran durante Semana 14 y habilitan el hito de cierre al terminarla.

![Cronograma Gantt del proyecto SIGEVET](diagramas/png/cronograma_gantt.png){width=100%}

### Actividades y responsables

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

### Resumen semanal de resultados

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

### Carga de trabajo y control

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

## Presupuesto del proyecto



### Base de estimación y resultado

El presupuesto académico de SIGEVET es **11.603.900 COP**, incluido un 10 % de imprevistos sobre los costos directos. Se valora el esfuerzo de 385 horas del cronograma, la utilización de tres portátiles durante 14 semanas y los servicios compartidos que permiten construir y demostrar el proyecto.

Los valores son supuestos internos de planeación, expresados en pesos colombianos; no son cotizaciones de mercado ni una liquidación laboral. El trabajo estudiantil, los portátiles y la conectividad son aportes en especie del equipo. No se asumen salarios pagados, compras de equipos ni desembolsos certificados. El costo del patrocinio docente no se imputa al equipo.

### Talento humano

Las tarifas se justifican como valoración de perfiles junior o estudiantes: 25.000 COP/h para gestión y administración de bases de datos por responsabilidad de integración; 22.000 COP/h para análisis y modelado; 24.000 COP/h para desarrollo e infraestructura por configuración de VM y conexión a MySQL. Son valores de referencia acordados para este ejercicio, sin prestaciones o tributos adicionales simulados.

| Integrante y rol | Horas | Tarifa COP/h | Subtotal COP |
|---|---:|---:|---:|
| Juan David Polo Herrera — Gerente del proyecto y DBA | 153 | 25.000 | 3.825.000 |
| Sebastián Castellanos Parra — Analista y modelador de datos | 136 | 22.000 | 2.992.000 |
| Juan Miguel Ramos — Desarrollador web e infraestructura | 96 | 24.000 | 2.304.000 |
| Total talento humano | 385 | — | 9.121.000 |
Table: Valoración del esfuerzo de los tres integrantes

Las horas provienen de las actividades del cronograma; los hitos tienen cero horas y no incrementan el costo. El seguimiento y el repositorio tienen esfuerzo propio de coordinación y no duplican la construcción de productos.

### Hardware y depreciación

Se utiliza depreciación lineal de uso, con valor de referencia de 3.120.000 COP por portátil, valor residual de cero y vida útil de 156 semanas. La imputación por portátil es $(3.120.000-0) / 156 \times 14 = 280.000$ COP. Se considera el uso durante todo el proyecto, independientemente de la dedicación parcial; es una imputación académica del recurso disponible, sin compra de hardware.

| Concepto | Cantidad | Valor unitario imputado COP | Subtotal COP |
|---|---:|---:|---:|
| Depreciación de portátil durante 14 semanas | 3 | 280.000 | 840.000 |
Table: Imputación de hardware por depreciación lineal

### Software y licencias

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

### Servicios

| Concepto | Semanas | Valor semanal COP | Subtotal COP | Criterio |
|---|---:|---:|---:|---|
| Internet compartido del equipo | 14 | 30.000 | 420.000 | Imputación conjunta por conectividad, sincronización y demostración. |
| Energía del equipo y de la VM | 14 | 12.000 | 168.000 | Estimación agregada para los tres portátiles; no se multiplica otra vez por integrante. |
| Total servicios | — | — | 588.000 | Aportes del equipo, sin factura atribuida al proyecto. |
Table: Estimación de servicios durante el horizonte del proyecto

### Consolidado y reserva

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

### Libro de cálculo y seguimiento

El archivo `entregables/Transversales/05_Presupuesto_SIGEVET.xlsx` contiene Resumen y Detalle. Detalle conserva cantidades y valores unitarios numéricos, y calcula cada subtotal como cantidad por valor unitario. El valor unitario de depreciación referencia el bloque de origen, residual, vida útil y semanas de uso. Resumen suma rubros, calcula el costo directo y la reserva, y obtiene el total con fórmulas reales.

Las horas salen de la misma lista de actividades del cronograma; un cambio de esfuerzo exige actualizar ambos libros y actualizar las tablas documentales y el acta de constitución. El gerente registra por paquete EDT cualquier uso propuesto de la reserva y su efecto en horas y costo, para revisión del equipo y validación del docente si afecta el alcance.

## Acta de cierre del proyecto



### Resumen del cierre académico

SIGEVET integra el caso simulado de Huella Sana S.A.S. mediante un esquema relacional de 22 tablas, modelos conceptuales y lógicos, scripts, datos de prueba y una aplicación de consulta. Este acta documenta el balance del proyecto de Bases de Datos 1 y la aceptación de los productos dentro de un horizonte de 14 semanas.

Los estados de entrega corresponden al registro académico del equipo; la aceptación formal del docente se expresa mediante su firma. No se atribuye aceptación a una clínica real ni se declara una puesta en producción.

### Cumplimiento de objetivos

| Objetivo | Resultado de cierre | Evidencia de aceptación |
|---|---|---|
| Analizar requerimientos y reglas del caso | Documentados para cinco sedes y sus procesos clínicos y comerciales | Propuesta y documento de supuestos. |
| Diseñar modelos conceptual, extendido, lógico y relacional | Modelos consistentes y esquema normalizado a 3FN | Diagramas Chen/EER, modelos de MySQL Workbench (`.mwb`) y modelo relacional. |
| Documentar estructura y restricciones | Diccionario coherente con el esquema de 22 tablas | Diccionario de datos y comentarios del catálogo (`02_comentarios.sql`). |
| Validar consultas | 55 ejercicios ejecutados sobre datos de prueba | Documento de álgebra, SQL equivalente y resultados. |
| Implementar plataforma y acceso web | Entorno Oracle Linux, MySQL 8.4 y conexión de consulta | VM, scripts, README y demostración de lectura con `vet_app`. |
| Colaborar y comunicar resultados | Productos versionados y demostración del equipo | Repositorio Git y video de máximo 15 minutos con audio y cámara de los tres integrantes. |
Table: Balance de objetivos y evidencia documental

### Registro de entregables

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

### Desviaciones y balance de recursos

| Aspecto | Ajuste de esfuerzo | Tratamiento | Impacto sobre hitos |
|---|---|---|---|
| Herramientas de macOS y virtualización | Redistribución de 4 horas dentro del paquete de VM | Uso de micromamba y Lima; se absorbió en las 16 horas de VM | 0 semanas. |
| Dependencia circular SEDE–EMPLEADO | Redistribución de 2 horas dentro de scripts | Creación posterior de la FK del director mediante `ALTER TABLE` | 0 semanas. |
| Cambio de RDBMS de Oracle Database Free a MySQL 8.4 | Redistribución de horas dentro de la Fase 2 | Scripts, modelos de Workbench, aplicación y documentos ajustados; la versión en Oracle queda como respaldo | 0 semanas. |
Table: Balance académico de ajustes menores dentro de la línea base

Las redistribuciones anteriores son estimaciones de esfuerzo para este balance académico; no constituyen registros de tiempos medidos. No incrementan las 385 horas presupuestadas. Los hitos se conservan en Semana 8, Semana 13 y Semana 14. La valoración base permanece en **11.603.900 COP**, incluida la reserva de 1.054.900 COP; no se certifican desembolsos ni consumos de reserva sin soportes contables.

### Lecciones aprendidas

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

### Transferencia y aceptación

Se entrega al docente el repositorio con fuentes, scripts, modelos y documentación, así como los archivos finales y el video. El equipo conserva las instrucciones de reconstrucción y conexión; la revisión de la entrega exige comprobar correspondencia entre fuentes y productos generados, legibilidad y participación de todos los integrantes.

No se transfieren servicios productivos ni obligaciones de soporte a Huella Sana, por tratarse de una organización ficticia. Las exclusiones de la propuesta siguen vigentes. El gerente consolida observaciones de sustentación y el patrocinador determina su aceptación académica. Los espacios de firma no representan aprobaciones ya otorgadas.

| Nombre | Documento | Rol | Firma |
|---|---|---|---|
| Juan David Polo Herrera | C.C. 1117506677 | Gerente del proyecto y DBA | |
| Sebastián Castellanos Parra | C.C. 1031811214 | Analista y modelador de datos | |
| Juan Miguel Ramos | C.C. 1025528586 | Desarrollador web e infraestructura | |
| Ing., Esp., M.Ed. Christian Felipe Duarte Arévalo | | Docente / patrocinador | |

Table: Aceptación y cierre académico

# Conclusiones

1. **El modelo bien hecho se nota en la implementación.** Los supuestos y la normalización hasta 3FN se tradujeron
   directamente en llaves y restricciones con nombre; casi ninguna regla tuvo que vivir en la aplicación.
2. **El álgebra relacional explica el SQL.** Cada consulta de la aplicación y de los 55 ejercicios se lee como
   selecciones, proyecciones, reuniones, diferencias o divisiones; por eso fue posible comprobarlas en otro motor.
3. **Tener todo en scripts permitió cambiar de motor.** El proyecto empezó en Oracle Database Free y se pasó a MySQL
   8.4 sin perder datos ni resultados, porque la base completa se reconstruye con los scripts de `database/`. La
   versión en Oracle quedó como respaldo.
4. **Probar contra datos reales encuentra huecos.** Las pruebas de reglas mostraron, por ejemplo, que una consulta se
   podía mover a una cita cancelada con un UPDATE; se corrigió extendiendo el disparador.
5. **La máquina virtual hace el proyecto portable.** La VM se crea con un solo script (`vm/migrar_linux.sh`), que
   sirve para el MacBook donde se hicieron las pruebas y para el computador servidor con Linux, y queda con MySQL y la
   aplicación arrancando solos.

