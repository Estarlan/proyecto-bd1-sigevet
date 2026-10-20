-- SIGEVET - Datos de VETERINARIO_ESPECIALIDAD (12 registros)
-- Ejecutar conectado como VETDB en FREEPDB1.
SET DEFINE OFF
SET FEEDBACK OFF

INSERT INTO VETERINARIO_ESPECIALIDAD (id_empleado, id_especialidad, fecha_certificacion) VALUES (1, 1, TO_DATE('2018-08-29', 'YYYY-MM-DD'));
INSERT INTO VETERINARIO_ESPECIALIDAD (id_empleado, id_especialidad, fecha_certificacion) VALUES (1, 6, TO_DATE('2021-05-06', 'YYYY-MM-DD'));
INSERT INTO VETERINARIO_ESPECIALIDAD (id_empleado, id_especialidad, fecha_certificacion) VALUES (2, 3, TO_DATE('2025-02-18', 'YYYY-MM-DD'));
INSERT INTO VETERINARIO_ESPECIALIDAD (id_empleado, id_especialidad, fecha_certificacion) VALUES (3, 1, TO_DATE('2024-02-28', 'YYYY-MM-DD'));
INSERT INTO VETERINARIO_ESPECIALIDAD (id_empleado, id_especialidad, fecha_certificacion) VALUES (9, 3, TO_DATE('2016-06-05', 'YYYY-MM-DD'));
INSERT INTO VETERINARIO_ESPECIALIDAD (id_empleado, id_especialidad, fecha_certificacion) VALUES (10, 7, TO_DATE('2014-07-11', 'YYYY-MM-DD'));
INSERT INTO VETERINARIO_ESPECIALIDAD (id_empleado, id_especialidad, fecha_certificacion) VALUES (15, 2, TO_DATE('2014-10-02', 'YYYY-MM-DD'));
INSERT INTO VETERINARIO_ESPECIALIDAD (id_empleado, id_especialidad, fecha_certificacion) VALUES (15, 5, TO_DATE('2014-08-26', 'YYYY-MM-DD'));
INSERT INTO VETERINARIO_ESPECIALIDAD (id_empleado, id_especialidad, fecha_certificacion) VALUES (19, 1, TO_DATE('2014-10-26', 'YYYY-MM-DD'));
INSERT INTO VETERINARIO_ESPECIALIDAD (id_empleado, id_especialidad, fecha_certificacion) VALUES (20, 3, TO_DATE('2025-05-22', 'YYYY-MM-DD'));
INSERT INTO VETERINARIO_ESPECIALIDAD (id_empleado, id_especialidad, fecha_certificacion) VALUES (20, 5, TO_DATE('2018-10-11', 'YYYY-MM-DD'));
INSERT INTO VETERINARIO_ESPECIALIDAD (id_empleado, id_especialidad, fecha_certificacion) VALUES (24, 1, TO_DATE('2021-07-11', 'YYYY-MM-DD'));

COMMIT;
