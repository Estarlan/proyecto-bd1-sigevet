-- SIGEVET - Base de datos, usuarios y rol
-- Se ejecuta como root. Antes hay que definir las claves en las variables @clave_vetdb y @clave_vet_app, por ejemplo:
--   SET @clave_vetdb = '...';  SET @clave_vet_app = '...';
-- (vm/scripts/cargar_bd.sh lo hace solo con las claves de vm/claves.env)
--
--   vetdb    dueño de la base sigevet: crea tablas, vistas, funciones, procedimientos y disparadores.
--   vet_app  usuario de la aplicación web: solo puede leer (rol rol_sigevet_lectura).
-- Los dos usuarios pueden conectarse desde otro equipo ('%'), por ejemplo desde MySQL Workbench.

CREATE DATABASE IF NOT EXISTS sigevet CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

SET @sql = CONCAT("CREATE USER IF NOT EXISTS 'vetdb'@'%' IDENTIFIED BY '", @clave_vetdb, "'");
PREPARE sentencia FROM @sql;
EXECUTE sentencia;
DEALLOCATE PREPARE sentencia;
GRANT ALL PRIVILEGES ON sigevet.* TO 'vetdb'@'%';

CREATE ROLE IF NOT EXISTS 'rol_sigevet_lectura';
GRANT SELECT, SHOW VIEW ON sigevet.* TO 'rol_sigevet_lectura';

SET @sql = CONCAT("CREATE USER IF NOT EXISTS 'vet_app'@'%' IDENTIFIED BY '", @clave_vet_app, "'");
PREPARE sentencia FROM @sql;
EXECUTE sentencia;
DEALLOCATE PREPARE sentencia;
GRANT 'rol_sigevet_lectura' TO 'vet_app'@'%';
SET DEFAULT ROLE 'rol_sigevet_lectura' TO 'vet_app'@'%';

SET @sql = NULL;
SELECT 'Base sigevet, usuarios vetdb y vet_app y rol rol_sigevet_lectura creados' AS resultado;
