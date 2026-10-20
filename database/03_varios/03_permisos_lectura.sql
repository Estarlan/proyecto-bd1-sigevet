-- =====================================================================
-- SIGEVET - Permisos de solo lectura para la aplicación web
-- Ejecutar como VETDB. Otorga SELECT sobre todas las tablas y vistas al rol ROL_SIGEVET_LECTURA,
-- que tiene el usuario VET_APP (principio de mínimo privilegio: la app no puede modificar datos).
-- =====================================================================
SET SERVEROUTPUT ON
DECLARE
  v_total PLS_INTEGER := 0;
BEGIN
  FOR o IN (SELECT object_name FROM user_objects
             WHERE object_type IN ('TABLE', 'VIEW') ORDER BY object_type, object_name) LOOP
    EXECUTE IMMEDIATE 'GRANT SELECT ON ' || o.object_name || ' TO ROL_SIGEVET_LECTURA';
    v_total := v_total + 1;
  END LOOP;
  DBMS_OUTPUT.PUT_LINE('Permisos SELECT otorgados a ROL_SIGEVET_LECTURA sobre ' || v_total || ' objetos.');
END;
/
