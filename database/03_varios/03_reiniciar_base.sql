-- SIGEVET - Borra la base sigevet con todo su contenido y la vuelve a crear vacía.
-- Se ejecuta como vetdb antes de reinstalar desde cero. Los usuarios y el rol no se borran.
DROP DATABASE IF EXISTS sigevet;
CREATE DATABASE sigevet CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
SELECT 'Base sigevet vacía' AS resultado;
