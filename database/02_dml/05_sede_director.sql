-- SIGEVET - Asignación del director de cada sede (relación circular SEDE–EMPLEADO)
-- Ejecutar conectado como VETDB en FREEPDB1.
SET DEFINE OFF
SET FEEDBACK OFF

UPDATE SEDE SET id_director = 6 WHERE id_sede = 1;
UPDATE SEDE SET id_director = 8 WHERE id_sede = 2;
UPDATE SEDE SET id_director = 18 WHERE id_sede = 3;
UPDATE SEDE SET id_director = 19 WHERE id_sede = 4;
UPDATE SEDE SET id_director = 27 WHERE id_sede = 5;

COMMIT;
