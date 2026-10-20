-- =====================================================================
-- SIGEVET - Script maestro: crea y carga todo el esquema VETDB
-- Ejecutar conectado como VETDB en FREEPDB1 desde la carpeta database/:
--   sqlplus VETDB/<clave>@localhost:1521/FREEPDB1 @instalar_esquema.sql
-- Orden: tablas → comentarios → secuencias e índices → funciones y disparadores → datos → vistas → permisos → verificación
-- =====================================================================
SET ECHO OFF
SET TERMOUT ON
SET FEEDBACK OFF
SET SERVEROUTPUT ON
WHENEVER SQLERROR EXIT SQL.SQLCODE ROLLBACK
TIMING START instalacion

PROMPT == 1/8 Tablas y restricciones (22 tablas, 113 restricciones con nombre)
@@01_ddl/01_crear_tablas.sql
PROMPT == 2/8 Comentarios de tablas y columnas (diccionario de datos en el catálogo)
@@01_ddl/02_comentarios.sql
PROMPT == 3/8 Secuencias e índices
@@01_ddl/03_secuencias_indices.sql
PROMPT == 4/8 Funciones y disparadores
@@01_ddl/05_programas.sql
-- Un disparador con errores de compilación no detiene SQL*Plus por sí solo: se verifica explícitamente.
DECLARE
  v_errores PLS_INTEGER;
BEGIN
  SELECT COUNT(*) INTO v_errores FROM user_errors;
  IF v_errores > 0 THEN
    RAISE_APPLICATION_ERROR(-20099, v_errores || ' errores de compilación en funciones o disparadores (ver USER_ERRORS).');
  END IF;
END;
/
PROMPT == 5/8 Datos de prueba (1.091 registros)
@@02_dml/01_departamento.sql
@@02_dml/02_ciudad.sql
@@02_dml/03_sede.sql
@@02_dml/04_empleado.sql
@@02_dml/05_sede_director.sql
@@02_dml/06_veterinario.sql
@@02_dml/07_auxiliar.sql
@@02_dml/08_especialidad.sql
@@02_dml/09_veterinario_especialidad.sql
@@02_dml/10_cliente.sql
@@02_dml/11_telefono_cliente.sql
@@02_dml/12_especie.sql
@@02_dml/13_raza.sql
@@02_dml/14_mascota.sql
@@02_dml/15_historia_clinica.sql
@@02_dml/16_servicio.sql
@@02_dml/17_cita.sql
@@02_dml/18_consulta.sql
@@02_dml/19_medicamento.sql
@@02_dml/20_prescripcion.sql
@@02_dml/21_inventario.sql
@@02_dml/22_factura.sql
@@02_dml/23_detalle_factura.sql
PROMPT == 6/8 Vistas
@@01_ddl/04_vistas.sql
PROMPT == 7/8 Permisos de solo lectura para VET_APP
@@03_varios/03_permisos_lectura.sql
PROMPT == 8/8 Verificación
WHENEVER SQLERROR CONTINUE
@@03_varios/02_verificacion.sql
TIMING STOP
PROMPT == Instalación del esquema terminada
EXIT
