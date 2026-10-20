-- SIGEVET - Datos de CIUDAD (11 registros)
-- Ejecutar conectado como VETDB en FREEPDB1.
SET DEFINE OFF
SET FEEDBACK OFF

INSERT INTO CIUDAD (id_ciudad, codigo_dane, nombre, id_departamento) VALUES (1, '11001', 'Bogotá D.C.', 1);
INSERT INTO CIUDAD (id_ciudad, codigo_dane, nombre, id_departamento) VALUES (2, '25175', 'Chía', 2);
INSERT INTO CIUDAD (id_ciudad, codigo_dane, nombre, id_departamento) VALUES (3, '25899', 'Zipaquirá', 2);
INSERT INTO CIUDAD (id_ciudad, codigo_dane, nombre, id_departamento) VALUES (4, '25754', 'Soacha', 2);
INSERT INTO CIUDAD (id_ciudad, codigo_dane, nombre, id_departamento) VALUES (5, '05001', 'Medellín', 3);
INSERT INTO CIUDAD (id_ciudad, codigo_dane, nombre, id_departamento) VALUES (6, '05266', 'Envigado', 3);
INSERT INTO CIUDAD (id_ciudad, codigo_dane, nombre, id_departamento) VALUES (7, '76001', 'Cali', 4);
INSERT INTO CIUDAD (id_ciudad, codigo_dane, nombre, id_departamento) VALUES (8, '76520', 'Palmira', 4);
INSERT INTO CIUDAD (id_ciudad, codigo_dane, nombre, id_departamento) VALUES (9, '08001', 'Barranquilla', 5);
INSERT INTO CIUDAD (id_ciudad, codigo_dane, nombre, id_departamento) VALUES (10, '68001', 'Bucaramanga', 6);
INSERT INTO CIUDAD (id_ciudad, codigo_dane, nombre, id_departamento) VALUES (11, '68276', 'Floridablanca', 6);

COMMIT;
