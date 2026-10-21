-- SIGEVET - Datos de DEPARTAMENTO (6 registros)
-- MySQL 8.4, base de datos sigevet
USE sigevet;

START TRANSACTION;
INSERT INTO DEPARTAMENTO (id_departamento, codigo_dane, nombre) VALUES (1, '11', 'Bogotá D.C.');
INSERT INTO DEPARTAMENTO (id_departamento, codigo_dane, nombre) VALUES (2, '25', 'Cundinamarca');
INSERT INTO DEPARTAMENTO (id_departamento, codigo_dane, nombre) VALUES (3, '05', 'Antioquia');
INSERT INTO DEPARTAMENTO (id_departamento, codigo_dane, nombre) VALUES (4, '76', 'Valle del Cauca');
INSERT INTO DEPARTAMENTO (id_departamento, codigo_dane, nombre) VALUES (5, '08', 'Atlántico');
INSERT INTO DEPARTAMENTO (id_departamento, codigo_dane, nombre) VALUES (6, '68', 'Santander');
COMMIT;
