-- =====================================================================
-- SIGEVET - Elimina todos los objetos del esquema VETDB (para reinstalar desde cero)
-- Ejecutar como VETDB. ¡Borra los datos! Úsese solo en el ambiente académico.
-- =====================================================================
SET SERVEROUTPUT ON
BEGIN
  FOR o IN (SELECT object_name, object_type FROM user_objects
             WHERE object_type IN ('VIEW', 'TRIGGER', 'FUNCTION', 'PROCEDURE', 'SEQUENCE')) LOOP
    EXECUTE IMMEDIATE 'DROP ' || o.object_type || ' ' || o.object_name;
  END LOOP;
  FOR t IN (SELECT table_name FROM user_tables) LOOP
    EXECUTE IMMEDIATE 'DROP TABLE ' || t.table_name || ' CASCADE CONSTRAINTS PURGE';
  END LOOP;
  DBMS_OUTPUT.PUT_LINE('Esquema VETDB vacío.');
END;
/
