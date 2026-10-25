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
