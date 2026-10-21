-- SIGEVET - Datos de SEDE (5 registros)
-- MySQL 8.4, base de datos sigevet
USE sigevet;

START TRANSACTION;
INSERT INTO SEDE (id_sede, nombre, direccion, telefono, email, fecha_apertura, id_ciudad) VALUES (1, 'Huella Sana Usaquén', 'Cl. 119 #6-24', '6016123456', 'usaquen@huellasana.co', '2018-03-01', 1);
INSERT INTO SEDE (id_sede, nombre, direccion, telefono, email, fecha_apertura, id_ciudad) VALUES (2, 'Huella Sana Chapinero', 'Cra. 13 #63-41', '6017456123', 'chapinero@huellasana.co', '2019-08-15', 1);
INSERT INTO SEDE (id_sede, nombre, direccion, telefono, email, fecha_apertura, id_ciudad) VALUES (3, 'Huella Sana Chía', 'Av. Pradilla #5-30', '6018621450', 'chia@huellasana.co', '2021-02-01', 2);
INSERT INTO SEDE (id_sede, nombre, direccion, telefono, email, fecha_apertura, id_ciudad) VALUES (4, 'Huella Sana Laureles', 'Cra. 76 #33A-12', '6044112233', 'laureles@huellasana.co', '2022-05-10', 5);
INSERT INTO SEDE (id_sede, nombre, direccion, telefono, email, fecha_apertura, id_ciudad) VALUES (5, 'Huella Sana San Fernando', 'Cl. 5 #38-25', '6025567788', 'sanfernando@huellasana.co', '2023-09-04', 7);
COMMIT;
