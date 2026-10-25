<!-- Created: 22:55 09-Oct-2026 -->
<!-- Generado por database/tools/gen_algebra.py -->

## Selección (σ) y proyección (π)

### Ejercicio 1. Servicios de vacunación

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

### Ejercicio 2. Hembras esterilizadas activas

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

### Ejercicio 3. Empleados con salario alto

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

### Ejercicio 4. Medicamentos con fórmula de bajo costo

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

### Ejercicio 5. Citas perdidas

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

### Ejercicio 6. Razas de tamaño grande

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

### Ejercicio 7. Teléfonos fijos

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

### Ejercicio 8. Clientes sin correo electrónico

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

### Ejercicio 9. Consultas con fiebre

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

### Ejercicio 10. Categorías de servicio

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

### Ejercicio 11. Agenda futura

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

### Ejercicio 12. Inventario bajo el mínimo

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

## Producto cartesiano (×)

### Ejercicio 13. Tabla de precios de vacunación por especie

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

### Ejercicio 14. Razas felinas mediante producto y selección

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

## Renombramiento (ρ)

### Ejercicio 15. Veterinarios graduados el mismo año

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

### Ejercicio 16. Ciudades con su departamento

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

## Reunión natural y reunión theta (⋈)

### Ejercicio 17. Datos completos de los veterinarios

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

### Ejercicio 18. Existencias de la sede Usaquén

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

### Ejercicio 19. Mascotas con alergias

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

### Ejercicio 20. Gatos y sus propietarios

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

### Ejercicio 21. Sedes, ubicación y director

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

### Ejercicio 22. Especialidades de cada veterinario

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

### Ejercicio 23. Fórmula médica de una consulta

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

### Ejercicio 24. Historia clínica de una mascota

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

### Ejercicio 25. Citas atendidas en la sede Chía

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

## Reuniones externas (⟕, ⟖, ⟗)

### Ejercicio 26. Clientes sin mascotas

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

### Ejercicio 27. Especialidades con o sin veterinarios

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

### Ejercicio 28. Todas las citas de una mascota, con o sin consulta

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

### Ejercicio 29. Comparación de inventarios entre sedes

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

## Semirreunión (⋉) y antirreunión (▷)

### Ejercicio 30. Clientes con facturas anuladas

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

### Ejercicio 31. Mascotas sin atenciones

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

### Ejercicio 32. Veterinarios que atendieron urgencias

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

## Operaciones de conjuntos (∪, ∩, −)

### Ejercicio 33. Personas registradas en Chía

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

### Ejercicio 34. Mascotas vacunadas u operadas

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

### Ejercicio 35. Medicamentos comunes a dos sedes

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

### Ejercicio 36. Clientes con perro y gato

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

### Ejercicio 37. Medicamentos nunca formulados

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

### Ejercicio 38. Veterinarios sin especialidad

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

### Ejercicio 39. Medicamentos que no maneja la sede Chía

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

### Ejercicio 40. Clientes de mostrador

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

## División (÷)

### Ejercicio 41. Medicamentos disponibles en todas las sedes

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

### Ejercicio 42. Veterinarios con un conjunto de especialidades

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

### Ejercicio 43. Clientes atendidos en todas las sedes de Bogotá

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

## Agregación y agrupamiento (γ)

### Ejercicio 44. Mascotas por especie

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

### Ejercicio 45. Citas por estado

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

### Ejercicio 46. Ingresos por sede

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

### Ejercicio 47. Salarios por tipo de empleado

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

### Ejercicio 48. Veterinario con más citas atendidas

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

### Ejercicio 49. Medicamentos más formulados

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

### Ejercicio 50. Clientes con más de dos mascotas

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

### Ejercicio 51. Facturación mensual de 2026

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

### Ejercicio 52. Valor del inventario por sede

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

### Ejercicio 53. Edad promedio por especie

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

### Ejercicio 54. Ticket promedio por medio de pago

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

### Ejercicio 55. Servicios que más ingresos generan

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
