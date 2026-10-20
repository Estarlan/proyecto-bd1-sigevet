-- SIGEVET - Datos de ESPECIALIDAD (8 registros)
-- Ejecutar conectado como VETDB en FREEPDB1.
SET DEFINE OFF
SET FEEDBACK OFF

INSERT INTO ESPECIALIDAD (id_especialidad, nombre, descripcion) VALUES (1, 'Medicina interna', 'Diagnóstico y tratamiento de enfermedades sistémicas');
INSERT INTO ESPECIALIDAD (id_especialidad, nombre, descripcion) VALUES (2, 'Cirugía', 'Procedimientos quirúrgicos de tejidos blandos y ortopedia');
INSERT INTO ESPECIALIDAD (id_especialidad, nombre, descripcion) VALUES (3, 'Dermatología', 'Enfermedades de piel, pelo y oídos');
INSERT INTO ESPECIALIDAD (id_especialidad, nombre, descripcion) VALUES (4, 'Odontología', 'Profilaxis y tratamiento dental');
INSERT INTO ESPECIALIDAD (id_especialidad, nombre, descripcion) VALUES (5, 'Cardiología', 'Diagnóstico y manejo de enfermedades cardíacas');
INSERT INTO ESPECIALIDAD (id_especialidad, nombre, descripcion) VALUES (6, 'Medicina felina', 'Atención especializada en gatos');
INSERT INTO ESPECIALIDAD (id_especialidad, nombre, descripcion) VALUES (7, 'Animales exóticos', 'Aves, reptiles y pequeños mamíferos');
INSERT INTO ESPECIALIDAD (id_especialidad, nombre, descripcion) VALUES (8, 'Oncología', 'Diagnóstico y tratamiento del cáncer');

COMMIT;
