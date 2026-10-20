-- =====================================================================
-- SIGEVET - Usuarios y rol de la base de datos
-- Ejecutar como SYSTEM conectado a la PDB FREEPDB1:
--   sqlplus system/<clave>@localhost:1521/FREEPDB1 @00_crear_usuarios.sql <clave_vetdb> <clave_vet_app>
--
--   VETDB      dueño del esquema: crea tablas, vistas, secuencias, funciones y disparadores.
--   VET_APP    usuario de la aplicación web: solo puede leer (rol ROL_SIGEVET_LECTURA).
-- =====================================================================
SET VERIFY OFF
WHENEVER SQLERROR EXIT SQL.SQLCODE

CREATE USER VETDB IDENTIFIED BY "&1"
  DEFAULT TABLESPACE USERS
  TEMPORARY TABLESPACE TEMP
  QUOTA UNLIMITED ON USERS;

GRANT CREATE SESSION, CREATE TABLE, CREATE VIEW, CREATE SEQUENCE,
      CREATE PROCEDURE, CREATE TRIGGER TO VETDB;

CREATE ROLE ROL_SIGEVET_LECTURA;

CREATE USER VET_APP IDENTIFIED BY "&2"
  DEFAULT TABLESPACE USERS
  TEMPORARY TABLESPACE TEMP;

GRANT CREATE SESSION TO VET_APP;
GRANT ROL_SIGEVET_LECTURA TO VET_APP;

-- VETDB otorga SELECT sobre sus objetos al rol con el script 03_varios/03_permisos_lectura.sql.

PROMPT Usuarios VETDB y VET_APP y rol ROL_SIGEVET_LECTURA creados.
EXIT
