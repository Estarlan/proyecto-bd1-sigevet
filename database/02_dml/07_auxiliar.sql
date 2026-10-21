-- SIGEVET - Datos de AUXILIAR (8 registros)
-- MySQL 8.4, base de datos sigevet
USE sigevet;

START TRANSACTION;
INSERT INTO AUXILIAR (id_empleado, area, certificacion, turno) VALUES (4, 'HOSPITALIZACION', NULL, 'TARDE');
INSERT INTO AUXILIAR (id_empleado, area, certificacion, turno) VALUES (5, 'CLINICA', 'Tecnólogo en producción animal', 'MANANA');
INSERT INTO AUXILIAR (id_empleado, area, certificacion, turno) VALUES (11, 'CIRUGIA', 'Técnico en asistencia veterinaria', 'MANANA');
INSERT INTO AUXILIAR (id_empleado, area, certificacion, turno) VALUES (12, 'LABORATORIO', 'Tecnólogo en producción animal', 'TARDE');
INSERT INTO AUXILIAR (id_empleado, area, certificacion, turno) VALUES (17, 'LABORATORIO', 'Técnico en asistencia veterinaria', 'MANANA');
INSERT INTO AUXILIAR (id_empleado, area, certificacion, turno) VALUES (21, 'CIRUGIA', 'Tecnólogo en producción animal', 'MANANA');
INSERT INTO AUXILIAR (id_empleado, area, certificacion, turno) VALUES (22, 'LABORATORIO', 'Tecnólogo en producción animal', 'MANANA');
INSERT INTO AUXILIAR (id_empleado, area, certificacion, turno) VALUES (26, 'CIRUGIA', 'Técnico en asistencia veterinaria', 'TARDE');
COMMIT;
