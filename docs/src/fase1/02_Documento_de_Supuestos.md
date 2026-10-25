---
title: "Documento de Supuestos"
subtitle: "Documento de Supuestos"
fase: "Fase 1"
version: "1.0"
---
<!-- Created: 22:45 09-Oct-2026 -->
<!-- Updated: 13:03 10-Oct-2026 -->

# Propósito

Este documento registra los supuestos, reglas de negocio y restricciones que el equipo asumió para modelar SIGEVET.
Cada supuesto explica **por qué** el modelo tiene una cardinalidad, una participación, un atributo o una restricción
determinada. Si un supuesto cambia, el documento indica qué elemento del modelo se vería afectado.

Convenciones: **S** = supuesto de negocio, **RN** = regla de negocio que se traduce en una restricción del modelo,
**SD** = supuesto de datos, **ST** = supuesto técnico, **R** = restricción del proyecto.

# Supuestos de la organización

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

# Supuestos del personal

| ID | Supuesto | Elemento del modelo afectado |
|---|---|---|
| S-08 | Todo el personal comparte datos comunes (documento, nombres, contacto, salario, sede). | Supertipo EMPLEADO |
| S-09 | Un empleado es veterinario, auxiliar o administrativo, pero nunca dos a la vez. | Especialización **disyunta** (`tipo_empleado`) |
| S-10 | El personal administrativo no requiere atributos propios, por eso no tiene subtipo. | Especialización **parcial** |
| S-11 | Un veterinario puede tener varias especialidades certificadas y una especialidad puede tenerla ningún, uno o varios veterinarios. | CERTIFICADO_EN N:M con `fecha_certificacion` |
| S-12 | La tarjeta profesional del veterinario es única en el país. | `UQ_VETERINARIO_TARJETA_PROFESIONAL` |
| S-13 | Un empleado inactivo se conserva (no se borra) para no perder la trazabilidad de citas y facturas. | `EMPLEADO.estado` ACTIVO/INACTIVO |

Table: Supuestos del personal

# Supuestos de clientes y mascotas

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

# Supuestos clínicos

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

# Supuestos comerciales

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

# Reglas de negocio y su implementación

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

# Supuestos de datos

| ID | Supuesto |
|---|---|
| SD-01 | Los identificadores (`id_*`) son llaves sustitutas numéricas generadas por el sistema; los códigos de negocio (documento, número de factura, código DANE, microchip) se protegen con UNIQUE. |
| SD-02 | Los nombres se guardan separados en `nombres` y `apellidos` (atributo compuesto) para poder ordenar y buscar por apellido. |
| SD-03 | Las fechas con hora (citas, consultas, facturas) y las fechas de registro usan el tipo `DATETIME` de MySQL; las fechas sin hora usan `DATE`. |
| SD-04 | Los valores monetarios están en pesos colombianos con dos decimales (`DECIMAL(10,2)` o `DECIMAL(12,2)`). |
| SD-05 | Los datos de prueba son ficticios y se generan de forma reproducible (semilla fija); no contienen datos personales reales. |

Table: Supuestos de datos

# Supuestos técnicos y restricciones del proyecto

| ID | Supuesto o restricción |
|---|---|
| ST-01 | El RDBMS es MySQL 8.4 LTS sobre Oracle Linux 9 en una máquina virtual, administrado con MySQL Workbench. |
| ST-02 | La edición Free limita el uso a 2 hilos de CPU, 2 GB de RAM y 12 GB de datos, suficiente para el volumen académico. |
| ST-03 | La aplicación web solo lee datos y se conecta con un usuario de solo lectura. |
| R-01 | El proyecto se desarrolla en 14 semanas por un equipo de 3 estudiantes. |
| R-02 | No se permite SQL Server; el sistema operativo debe ser Linux o Unix. |
| R-03 | Todo el trabajo se versiona en GitHub. |

Table: Supuestos técnicos y restricciones

# Aprobación

| Nombre | Documento | Rol | Firma |
|---|---|---|---|
| Juan David Polo Herrera | C.C. 1117506677 | Gerente del proyecto y DBA | |
| Sebastián Castellanos Parra | C.C. 1031811214 | Analista y modelador de datos | |
| Juan Miguel Ramos | C.C. 1025528586 | Desarrollador web e infraestructura | |
| Ing., Esp., M.Ed. Christian Felipe Duarte Arévalo | | Docente / patrocinador | |

Table: Aprobación de los supuestos
