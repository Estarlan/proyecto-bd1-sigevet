-- SIGEVET - Script maestro: crea y carga toda la base sigevet
-- Se ejecuta como vetdb desde la carpeta database/ con el cliente mysql:
--   mysql -u vetdb -p < instalar_esquema.sql
-- (En MySQL Workbench se abren y ejecutan los mismos archivos en este orden.)
-- Si una sentencia falla, el cliente mysql se detiene ahí.

SELECT '== 1/6 Tablas y restricciones (22 tablas)' AS paso;
SOURCE 01_ddl/01_crear_tablas.sql
SELECT '== 2/6 Índices de búsqueda' AS paso;
SOURCE 01_ddl/02_indices.sql
SELECT '== 3/6 Funciones, procedimientos y disparadores (antes de los datos)' AS paso;
SOURCE 01_ddl/04_programas.sql
SELECT '== 4/6 Datos de prueba (1.091 registros)' AS paso;
SOURCE 02_dml/01_departamento.sql
SOURCE 02_dml/02_ciudad.sql
SOURCE 02_dml/03_sede.sql
SOURCE 02_dml/04_empleado.sql
SOURCE 02_dml/05_sede_director.sql
SOURCE 02_dml/06_veterinario.sql
SOURCE 02_dml/07_auxiliar.sql
SOURCE 02_dml/08_especialidad.sql
SOURCE 02_dml/09_veterinario_especialidad.sql
SOURCE 02_dml/10_cliente.sql
SOURCE 02_dml/11_telefono_cliente.sql
SOURCE 02_dml/12_especie.sql
SOURCE 02_dml/13_raza.sql
SOURCE 02_dml/14_mascota.sql
SOURCE 02_dml/15_historia_clinica.sql
SOURCE 02_dml/16_servicio.sql
SOURCE 02_dml/17_cita.sql
SOURCE 02_dml/18_consulta.sql
SOURCE 02_dml/19_medicamento.sql
SOURCE 02_dml/20_prescripcion.sql
SOURCE 02_dml/21_inventario.sql
SOURCE 02_dml/22_factura.sql
SOURCE 02_dml/23_detalle_factura.sql
SELECT '== 5/6 Vistas' AS paso;
SOURCE 01_ddl/03_vistas.sql
SELECT '== 6/6 Verificación' AS paso;
SOURCE 03_varios/02_verificacion.sql
SELECT '== Instalación terminada' AS paso;
