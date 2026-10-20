-- Evidencias del RDBMS (ejecutar como VETDB en FREEPDB1).
SET PAGESIZE 50
SET LINESIZE 120
SET FEEDBACK OFF
COLUMN banner_full FORMAT A90
COLUMN pdb FORMAT A12
COLUMN usuario FORMAT A10
COLUMN charset FORMAT A12
SELECT banner_full FROM v$version;
SELECT SYS_CONTEXT('USERENV', 'CON_NAME') AS pdb, USER AS usuario FROM dual;
SELECT ROUND(SUM(bytes) / 1024 / 1024, 2) AS mb_esquema_vetdb FROM user_segments;
SELECT value AS charset FROM nls_database_parameters WHERE parameter = 'NLS_CHARACTERSET';
EXIT
