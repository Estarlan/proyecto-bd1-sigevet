-- SIGEVET - Datos de MEDICAMENTO (18 registros)
-- MySQL 8.4, base de datos sigevet
USE sigevet;

START TRANSACTION;
INSERT INTO MEDICAMENTO (id_medicamento, codigo, nombre, principio_activo, presentacion, laboratorio, precio_venta, requiere_formula) VALUES (1, 'MED-0001', 'Amoxivet', 'Amoxicilina', 'Tableta 500 mg', 'Lab. Andino Vet', 1800, 'S');
INSERT INTO MEDICAMENTO (id_medicamento, codigo, nombre, principio_activo, presentacion, laboratorio, precio_venta, requiere_formula) VALUES (2, 'MED-0002', 'Meloxivet', 'Meloxicam', 'Suspensión oral 15 ml', 'Farmavet Colombia', 32000, 'S');
INSERT INTO MEDICAMENTO (id_medicamento, codigo, nombre, principio_activo, presentacion, laboratorio, precio_venta, requiere_formula) VALUES (3, 'MED-0003', 'Dewormin Plus', 'Praziquantel + Pirantel', 'Tableta', 'Lab. Andino Vet', 9500, 'N');
INSERT INTO MEDICAMENTO (id_medicamento, codigo, nombre, principio_activo, presentacion, laboratorio, precio_venta, requiere_formula) VALUES (4, 'MED-0004', 'Pulguex', 'Fluralaner', 'Tableta masticable 500 mg', 'BioPet Labs', 98000, 'N');
INSERT INTO MEDICAMENTO (id_medicamento, codigo, nombre, principio_activo, presentacion, laboratorio, precio_venta, requiere_formula) VALUES (5, 'MED-0005', 'Gastrovet', 'Omeprazol', 'Cápsula 20 mg', 'Farmavet Colombia', 2500, 'S');
INSERT INTO MEDICAMENTO (id_medicamento, codigo, nombre, principio_activo, presentacion, laboratorio, precio_venta, requiere_formula) VALUES (6, 'MED-0006', 'Dermacalm', 'Prednisolona', 'Tableta 5 mg', 'BioPet Labs', 1500, 'S');
INSERT INTO MEDICAMENTO (id_medicamento, codigo, nombre, principio_activo, presentacion, laboratorio, precio_venta, requiere_formula) VALUES (7, 'MED-0007', 'Otovet', 'Gentamicina + Betametasona', 'Gotas óticas 15 ml', 'Lab. Andino Vet', 28000, 'S');
INSERT INTO MEDICAMENTO (id_medicamento, codigo, nombre, principio_activo, presentacion, laboratorio, precio_venta, requiere_formula) VALUES (8, 'MED-0008', 'Suero Oral Pet', 'Electrolitos', 'Solución 500 ml', 'Farmavet Colombia', 15000, 'N');
INSERT INTO MEDICAMENTO (id_medicamento, codigo, nombre, principio_activo, presentacion, laboratorio, precio_venta, requiere_formula) VALUES (9, 'MED-0009', 'Tramavet', 'Tramadol', 'Gotas 10 ml', 'BioPet Labs', 26000, 'S');
INSERT INTO MEDICAMENTO (id_medicamento, codigo, nombre, principio_activo, presentacion, laboratorio, precio_venta, requiere_formula) VALUES (10, 'MED-0010', 'Metrovet', 'Metronidazol', 'Tableta 250 mg', 'Lab. Andino Vet', 1200, 'S');
INSERT INTO MEDICAMENTO (id_medicamento, codigo, nombre, principio_activo, presentacion, laboratorio, precio_venta, requiere_formula) VALUES (11, 'MED-0011', 'Felinvit', 'Multivitamínico', 'Pasta oral 100 g', 'NutriAnimal', 34000, 'N');
INSERT INTO MEDICAMENTO (id_medicamento, codigo, nombre, principio_activo, presentacion, laboratorio, precio_venta, requiere_formula) VALUES (12, 'MED-0012', 'Cardiopet', 'Pimobendan', 'Tableta 5 mg', 'BioPet Labs', 4200, 'S');
INSERT INTO MEDICAMENTO (id_medicamento, codigo, nombre, principio_activo, presentacion, laboratorio, precio_venta, requiere_formula) VALUES (13, 'MED-0013', 'Shampoo Clorhex', 'Clorhexidina 4%', 'Frasco 250 ml', 'NutriAnimal', 38000, 'N');
INSERT INTO MEDICAMENTO (id_medicamento, codigo, nombre, principio_activo, presentacion, laboratorio, precio_venta, requiere_formula) VALUES (14, 'MED-0014', 'Antiemet', 'Maropitant', 'Tableta 16 mg', 'Farmavet Colombia', 12500, 'S');
INSERT INTO MEDICAMENTO (id_medicamento, codigo, nombre, principio_activo, presentacion, laboratorio, precio_venta, requiere_formula) VALUES (15, 'MED-0015', 'Cefavet', 'Cefalexina', 'Cápsula 500 mg', 'Lab. Andino Vet', 2100, 'S');
INSERT INTO MEDICAMENTO (id_medicamento, codigo, nombre, principio_activo, presentacion, laboratorio, precio_venta, requiere_formula) VALUES (16, 'MED-0016', 'Calmapet', 'Extracto de valeriana', 'Gotas 30 ml', 'NutriAnimal', 22000, 'N');
INSERT INTO MEDICAMENTO (id_medicamento, codigo, nombre, principio_activo, presentacion, laboratorio, precio_venta, requiere_formula) VALUES (17, 'MED-0017', 'Ivermec', 'Ivermectina', 'Solución inyectable 50 ml', 'BioPet Labs', 45000, 'S');
INSERT INTO MEDICAMENTO (id_medicamento, codigo, nombre, principio_activo, presentacion, laboratorio, precio_venta, requiere_formula) VALUES (18, 'MED-0018', 'Hepatovet', 'Silimarina', 'Tableta 100 mg', 'Farmavet Colombia', 1900, 'N');
COMMIT;
