-- SIGEVET - Datos de ESPECIE (5 registros)
-- MySQL 8.4, base de datos sigevet
USE sigevet;

START TRANSACTION;
INSERT INTO ESPECIE (id_especie, nombre) VALUES (1, 'Canino');
INSERT INTO ESPECIE (id_especie, nombre) VALUES (2, 'Felino');
INSERT INTO ESPECIE (id_especie, nombre) VALUES (3, 'Ave');
INSERT INTO ESPECIE (id_especie, nombre) VALUES (4, 'Conejo');
INSERT INTO ESPECIE (id_especie, nombre) VALUES (5, 'Hámster');
COMMIT;
