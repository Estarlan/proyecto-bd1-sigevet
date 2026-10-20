-- =====================================================================
-- SIGEVET - Verificación de la instalación (ejecutar como VETDB)
-- Muestra objetos creados, restricciones por tipo, registros por tabla, objetos inválidos
-- y prueba que las reglas de negocio de los disparadores rechazan datos incorrectos.
-- =====================================================================
SET PAGESIZE 100
SET LINESIZE 160
SET FEEDBACK OFF
SET SERVEROUTPUT ON
COLUMN object_type FORMAT A20
COLUMN tabla FORMAT A28
COLUMN tipo FORMAT A30

PROMPT
PROMPT == Objetos del esquema VETDB
SELECT object_type, COUNT(*) AS cantidad FROM user_objects GROUP BY object_type ORDER BY object_type;

PROMPT
PROMPT == Restricciones por tipo (P = llave primaria, R = llave foránea, U = única, C = CHECK y NOT NULL)
SELECT DECODE(constraint_type, 'P', 'P - Llave primaria', 'R', 'R - Llave foránea', 'U', 'U - Única',
              'C', 'C - CHECK / NOT NULL', constraint_type) AS tipo, COUNT(*) AS cantidad
FROM user_constraints WHERE table_name NOT LIKE 'BIN$%'
GROUP BY constraint_type ORDER BY constraint_type;

PROMPT
PROMPT == Objetos inválidos (debe salir vacío)
SELECT object_name, object_type FROM user_objects WHERE status <> 'VALID';

PROMPT
PROMPT == Registros por tabla
SELECT 'DEPARTAMENTO' tabla, COUNT(*) registros FROM DEPARTAMENTO UNION ALL
SELECT 'CIUDAD', COUNT(*) FROM CIUDAD UNION ALL
SELECT 'SEDE', COUNT(*) FROM SEDE UNION ALL
SELECT 'EMPLEADO', COUNT(*) FROM EMPLEADO UNION ALL
SELECT 'VETERINARIO', COUNT(*) FROM VETERINARIO UNION ALL
SELECT 'AUXILIAR', COUNT(*) FROM AUXILIAR UNION ALL
SELECT 'ESPECIALIDAD', COUNT(*) FROM ESPECIALIDAD UNION ALL
SELECT 'VETERINARIO_ESPECIALIDAD', COUNT(*) FROM VETERINARIO_ESPECIALIDAD UNION ALL
SELECT 'CLIENTE', COUNT(*) FROM CLIENTE UNION ALL
SELECT 'TELEFONO_CLIENTE', COUNT(*) FROM TELEFONO_CLIENTE UNION ALL
SELECT 'ESPECIE', COUNT(*) FROM ESPECIE UNION ALL
SELECT 'RAZA', COUNT(*) FROM RAZA UNION ALL
SELECT 'MASCOTA', COUNT(*) FROM MASCOTA UNION ALL
SELECT 'HISTORIA_CLINICA', COUNT(*) FROM HISTORIA_CLINICA UNION ALL
SELECT 'SERVICIO', COUNT(*) FROM SERVICIO UNION ALL
SELECT 'CITA', COUNT(*) FROM CITA UNION ALL
SELECT 'CONSULTA', COUNT(*) FROM CONSULTA UNION ALL
SELECT 'MEDICAMENTO', COUNT(*) FROM MEDICAMENTO UNION ALL
SELECT 'PRESCRIPCION', COUNT(*) FROM PRESCRIPCION UNION ALL
SELECT 'INVENTARIO', COUNT(*) FROM INVENTARIO UNION ALL
SELECT 'FACTURA', COUNT(*) FROM FACTURA UNION ALL
SELECT 'DETALLE_FACTURA', COUNT(*) FROM DETALLE_FACTURA;

PROMPT
PROMPT == Prueba de reglas de negocio (cada intento debe ser RECHAZADO con el código de error esperado)
DECLARE
  PROCEDURE probar(p_nombre VARCHAR2, p_sql VARCHAR2, p_codigo PLS_INTEGER) IS
  BEGIN
    EXECUTE IMMEDIATE p_sql;
    DBMS_OUTPUT.PUT_LINE('FALLA  ' || p_nombre || ': la base de datos aceptó un dato inválido');
    ROLLBACK;
  EXCEPTION WHEN OTHERS THEN
    IF SQLCODE = p_codigo THEN
      DBMS_OUTPUT.PUT_LINE('OK     ' || RPAD(p_nombre, 34) || SUBSTR(SQLERRM, 1, 90));
    ELSE
      DBMS_OUTPUT.PUT_LINE('FALLA  ' || p_nombre || ': error distinto al esperado (' || p_codigo || ') -> ' || SQLERRM);
    END IF;
    ROLLBACK;
  END;
BEGIN
  probar('FK a departamento inexistente', 'INSERT INTO CIUDAD VALUES (999, ''99999'', ''Ciudad X'', 99)', -2291);
  probar('CHECK sexo de mascota', 'UPDATE MASCOTA SET sexo = ''X'' WHERE id_mascota = 1', -2290);
  probar('UNIQUE documento de cliente', 'UPDATE CLIENTE SET (tipo_documento, num_documento) = (SELECT tipo_documento, num_documento FROM CLIENTE WHERE id_cliente = 2) WHERE id_cliente = 1', -1);
  probar('Arco exclusivo del detalle', 'UPDATE DETALLE_FACTURA SET id_medicamento = 1 WHERE id_servicio IS NOT NULL AND ROWNUM = 1', -2290);
  probar('RN-09 subtipo VET', 'INSERT INTO VETERINARIO VALUES ((SELECT MIN(id_empleado) FROM EMPLEADO WHERE tipo_empleado = ''ADM''), ''TP-X'', ''U'', 2020)', -20001);
  probar('RN-08 veterinario de otra sede', 'UPDATE CITA SET id_sede = CASE id_sede WHEN 1 THEN 5 ELSE 1 END WHERE id_cita = 1', -20003);
  probar('Consulta de cita no atendida', 'INSERT INTO CONSULTA (id_consulta, id_cita, fecha_hora_inicio, peso_kg, diagnostico) SELECT 9999, MIN(id_cita), SYSDATE, 5, ''X'' FROM CITA WHERE estado = ''CANCELADA''', -20004);
  probar('Cambio de tipo con subtipo', 'UPDATE EMPLEADO SET tipo_empleado = ''ADM'' WHERE id_empleado = (SELECT MIN(id_empleado) FROM VETERINARIO)', -20005);
  probar('Cita con consulta deja ATENDIDA', 'UPDATE CITA SET estado = ''CANCELADA'' WHERE id_cita = (SELECT MIN(id_cita) FROM CONSULTA)', -20006);
END;
/

PROMPT
PROMPT == Atributos derivados (funciones)
SELECT numero_factura, FN_TOTAL_FACTURA(id_factura) AS total FROM FACTURA WHERE id_factura <= 3 ORDER BY id_factura;
