-- SIGEVET - Datos de ESPECIE (5 registros)
-- Ejecutar conectado como VETDB en FREEPDB1.
SET DEFINE OFF
SET FEEDBACK OFF

INSERT INTO ESPECIE (id_especie, nombre) VALUES (1, 'Canino');
INSERT INTO ESPECIE (id_especie, nombre) VALUES (2, 'Felino');
INSERT INTO ESPECIE (id_especie, nombre) VALUES (3, 'Ave');
INSERT INTO ESPECIE (id_especie, nombre) VALUES (4, 'Conejo');
INSERT INTO ESPECIE (id_especie, nombre) VALUES (5, 'Hámster');

COMMIT;
