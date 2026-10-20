-- SIGEVET - Datos de RAZA (20 registros)
-- Ejecutar conectado como VETDB en FREEPDB1.
SET DEFINE OFF
SET FEEDBACK OFF

INSERT INTO RAZA (id_raza, nombre, tamano, id_especie) VALUES (1, 'Labrador Retriever', 'GRANDE', 1);
INSERT INTO RAZA (id_raza, nombre, tamano, id_especie) VALUES (2, 'Golden Retriever', 'GRANDE', 1);
INSERT INTO RAZA (id_raza, nombre, tamano, id_especie) VALUES (3, 'Pastor Alemán', 'GRANDE', 1);
INSERT INTO RAZA (id_raza, nombre, tamano, id_especie) VALUES (4, 'Bulldog Francés', 'PEQUENO', 1);
INSERT INTO RAZA (id_raza, nombre, tamano, id_especie) VALUES (5, 'Poodle', 'PEQUENO', 1);
INSERT INTO RAZA (id_raza, nombre, tamano, id_especie) VALUES (6, 'Schnauzer', 'MEDIANO', 1);
INSERT INTO RAZA (id_raza, nombre, tamano, id_especie) VALUES (7, 'Beagle', 'MEDIANO', 1);
INSERT INTO RAZA (id_raza, nombre, tamano, id_especie) VALUES (8, 'Criollo', 'MEDIANO', 1);
INSERT INTO RAZA (id_raza, nombre, tamano, id_especie) VALUES (9, 'Shih Tzu', 'PEQUENO', 1);
INSERT INTO RAZA (id_raza, nombre, tamano, id_especie) VALUES (10, 'Border Collie', 'MEDIANO', 1);
INSERT INTO RAZA (id_raza, nombre, tamano, id_especie) VALUES (11, 'Siamés', 'NA', 2);
INSERT INTO RAZA (id_raza, nombre, tamano, id_especie) VALUES (12, 'Persa', 'NA', 2);
INSERT INTO RAZA (id_raza, nombre, tamano, id_especie) VALUES (13, 'Angora', 'NA', 2);
INSERT INTO RAZA (id_raza, nombre, tamano, id_especie) VALUES (14, 'Criollo', 'NA', 2);
INSERT INTO RAZA (id_raza, nombre, tamano, id_especie) VALUES (15, 'Maine Coon', 'NA', 2);
INSERT INTO RAZA (id_raza, nombre, tamano, id_especie) VALUES (16, 'Periquito australiano', 'NA', 3);
INSERT INTO RAZA (id_raza, nombre, tamano, id_especie) VALUES (17, 'Cacatúa ninfa', 'NA', 3);
INSERT INTO RAZA (id_raza, nombre, tamano, id_especie) VALUES (18, 'Belier', 'NA', 4);
INSERT INTO RAZA (id_raza, nombre, tamano, id_especie) VALUES (19, 'Cabeza de león', 'NA', 4);
INSERT INTO RAZA (id_raza, nombre, tamano, id_especie) VALUES (20, 'Sirio', 'NA', 5);

COMMIT;
