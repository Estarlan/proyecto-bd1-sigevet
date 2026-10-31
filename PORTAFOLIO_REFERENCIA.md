# SIGEVET — referencia para el portafolio (solo bases de datos)

Resumen del proyecto final de Bases de Datos 1, para actualizar el portafolio de Juan David Polo Herrera.
Solo se habla de bases de datos. No incluye claves ni fechas de entrega.

## Datos generales

- **Proyecto:** SIGEVET, Sistema Integral de Gestión Veterinaria.
- **Curso:** Bases de Datos 1, Ingeniería de Sistemas, Universidad El Bosque.
- **Caso:** red inventada de clínicas veterinarias, Huella Sana S.A.S., con 5 sedes. Maneja clientes, mascotas con
  historia clínica, citas, consultas, fórmulas, inventario de medicamentos por sede y facturación.
- **Repositorio:** https://github.com/Estarlan/proyecto-bd1-sigevet
- **Equipo:** Juan David Polo Herrera (gerente del proyecto y DBA), Sebastián Castellanos Parra (analista y modelador
  de datos), Juan Miguel Ramos.
- **Rol de Juan David:** coordinación, diseño y construcción de la base de datos, diagramas y documentación.

## Texto para la tarjeta del portafolio (2–3 frases)

Diseñé con mi equipo la base de datos de una red de clínicas veterinarias: modelo entidad-relación en varias
notaciones, modelo relacional en tercera forma normal y una base en MySQL 8.4 con 22 tablas, vistas, funciones y
disparadores. Resolví 55 ejercicios de álgebra relacional y los comprobé en SQL.

## Lo que se hizo

**Modelado**
- Modelo conceptual: 18 entidades y 26 relaciones (5 de 1:1, 18 de 1:N, 3 de N:M) y una especialización de EMPLEADO en
  VETERINARIO y AUXILIAR.
- Notaciones: Peter Chen, ER extendido (EER), Barker y Crow's Foot.
- Transformación al modelo relacional: atributos multivaluados, relaciones N:M, entidad débil y especialización.
- Normalización de 0FN a 3FN, con dependencias funcionales.
- Diagramas editables en Draw.io, sin relleno y con las relaciones en infinitivo (PERTENECER, TENER, POSEER...).

**Base de datos (MySQL 8.4)**
- 22 tablas en 3FN, 138 columnas, 30 llaves foráneas y 113 restricciones con nombre.
- 6 vistas, 2 funciones, 3 procedimientos, 9 disparadores y 1.091 registros de prueba.
- Scripts ordenados: creación de tablas, índices, vistas y programas (`database/01_ddl`), inserción de datos
  (`02_dml`) y consultas, verificación y reinicio (`03_varios`).
- Usuarios de base de datos con permisos mínimos.
- Diccionario de datos completo (columna, tipo, nulabilidad, llaves, restricciones, descripción).

**Álgebra relacional y SQL**
- 55 ejercicios de álgebra relacional, cada uno con su expresión, su consulta SQL y su resultado verificado en MySQL.

## Herramientas (para etiquetas o iconos)

MySQL 8.4, MySQL Workbench, Oracle SQL Developer Data Modeler, Draw.io, SQL, álgebra relacional, modelo
entidad-relación, normalización.

## Imágenes disponibles

- PNG de los diagramas en `documentos/fase1/diagramas_drawio/`: Chen, EER, Barker, Crow's Foot, modelo relacional y
  normalización.

## Lo que no se debe publicar

- Claves, tokens, el archivo `vm/claves.env` ni la carpeta `vm/cache`.
- Datos personales reales: todos los registros de prueba son inventados.
- Fechas de entrega o de calendario.
