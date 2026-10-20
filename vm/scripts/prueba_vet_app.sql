-- Prueba del usuario de la aplicación (VET_APP): puede leer pero no modificar.
-- sqlplus VET_APP/<clave>@localhost:1521/FREEPDB1 @vm/scripts/prueba_vet_app.sql
SET FEEDBACK OFF
SET SERVEROUTPUT ON
SET LINESIZE 100
COLUMN usuario FORMAT A10
COLUMN pdb FORMAT A10
COLUMN nombre FORMAT A20
SELECT USER AS usuario, SYS_CONTEXT('USERENV', 'CON_NAME') AS pdb FROM dual;
SELECT (SELECT COUNT(*) FROM VETDB.MASCOTA) AS mascotas,
       (SELECT COUNT(*) FROM VETDB.CITA) AS citas,
       (SELECT COUNT(*) FROM VETDB.FACTURA) AS facturas FROM dual;
SELECT nombre FROM VETDB.CIUDAD WHERE nombre IN ('Bogotá D.C.', 'Chía') ORDER BY nombre;

DECLARE
  PROCEDURE intentar(p_sql VARCHAR2) IS
  BEGIN
    EXECUTE IMMEDIATE p_sql;
    DBMS_OUTPUT.PUT_LINE('PERMITIDO (no debería): ' || p_sql);
    ROLLBACK;
  EXCEPTION
    WHEN OTHERS THEN
      DBMS_OUTPUT.PUT_LINE('RECHAZADO: ' || p_sql || ' -> ' || SQLERRM);
  END;
BEGIN
  intentar('DELETE FROM VETDB.MASCOTA');
  intentar('UPDATE VETDB.SERVICIO SET precio = 1');
  intentar('INSERT INTO VETDB.ESPECIE VALUES (99, ''Prueba'')');
  intentar('DROP TABLE VETDB.CITA');
END;
/
EXIT
