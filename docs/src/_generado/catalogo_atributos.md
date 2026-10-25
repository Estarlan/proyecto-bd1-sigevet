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
