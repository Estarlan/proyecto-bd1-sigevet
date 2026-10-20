-- SIGEVET - Datos de SERVICIO (14 registros)
-- Ejecutar conectado como VETDB en FREEPDB1.
SET DEFINE OFF
SET FEEDBACK OFF

INSERT INTO SERVICIO (id_servicio, codigo, nombre, categoria, precio, duracion_min) VALUES (1, 'SRV-CON01', 'Consulta general', 'CONSULTA', 65000, 30);
INSERT INTO SERVICIO (id_servicio, codigo, nombre, categoria, precio, duracion_min) VALUES (2, 'SRV-CON02', 'Consulta especializada', 'CONSULTA', 110000, 45);
INSERT INTO SERVICIO (id_servicio, codigo, nombre, categoria, precio, duracion_min) VALUES (3, 'SRV-CON03', 'Consulta de urgencias', 'CONSULTA', 140000, 40);
INSERT INTO SERVICIO (id_servicio, codigo, nombre, categoria, precio, duracion_min) VALUES (4, 'SRV-VAC01', 'Vacuna múltiple canina', 'VACUNACION', 75000, 20);
INSERT INTO SERVICIO (id_servicio, codigo, nombre, categoria, precio, duracion_min) VALUES (5, 'SRV-VAC02', 'Vacuna triple felina', 'VACUNACION', 70000, 20);
INSERT INTO SERVICIO (id_servicio, codigo, nombre, categoria, precio, duracion_min) VALUES (6, 'SRV-VAC03', 'Vacuna antirrábica', 'VACUNACION', 45000, 15);
INSERT INTO SERVICIO (id_servicio, codigo, nombre, categoria, precio, duracion_min) VALUES (7, 'SRV-CIR01', 'Esterilización', 'CIRUGIA', 380000, 120);
INSERT INTO SERVICIO (id_servicio, codigo, nombre, categoria, precio, duracion_min) VALUES (8, 'SRV-CIR02', 'Cirugía de tejidos blandos', 'CIRUGIA', 650000, 150);
INSERT INTO SERVICIO (id_servicio, codigo, nombre, categoria, precio, duracion_min) VALUES (9, 'SRV-LAB01', 'Hemograma completo', 'LABORATORIO', 58000, 15);
INSERT INTO SERVICIO (id_servicio, codigo, nombre, categoria, precio, duracion_min) VALUES (10, 'SRV-LAB02', 'Perfil bioquímico', 'LABORATORIO', 95000, 15);
INSERT INTO SERVICIO (id_servicio, codigo, nombre, categoria, precio, duracion_min) VALUES (11, 'SRV-EST01', 'Baño y peluquería', 'ESTETICA', 55000, 60);
INSERT INTO SERVICIO (id_servicio, codigo, nombre, categoria, precio, duracion_min) VALUES (12, 'SRV-IMG01', 'Radiografía', 'IMAGENOLOGIA', 120000, 30);
INSERT INTO SERVICIO (id_servicio, codigo, nombre, categoria, precio, duracion_min) VALUES (13, 'SRV-IMG02', 'Ecografía abdominal', 'IMAGENOLOGIA', 150000, 40);
INSERT INTO SERVICIO (id_servicio, codigo, nombre, categoria, precio, duracion_min) VALUES (14, 'SRV-HOS01', 'Hospitalización por día', 'HOSPITALIZACION', 210000, 1440);

COMMIT;
