-- =====================================================================
-- SIGEVET - Funciones y disparadores (PL/SQL)
-- Ejecutar conectado como VETDB en FREEPDB1, ANTES de cargar los datos (los disparadores validan las inserciones).
-- Implementan las reglas de negocio que no se pueden expresar con PK/FK/UNIQUE/CHECK (documento de supuestos).
-- =====================================================================

-- Edad en años cumplidos a una fecha (atributo derivado "edad" del MER).
CREATE OR REPLACE FUNCTION FN_EDAD_MASCOTA (p_fecha_nacimiento IN DATE, p_al IN DATE DEFAULT SYSDATE)
RETURN NUMBER IS
BEGIN
  IF p_fecha_nacimiento IS NULL THEN
    RETURN NULL;
  END IF;
  RETURN TRUNC(MONTHS_BETWEEN(p_al, p_fecha_nacimiento) / 12);
END FN_EDAD_MASCOTA;
/

-- Total de una factura (atributo derivado "total").
CREATE OR REPLACE FUNCTION FN_TOTAL_FACTURA (p_id_factura IN NUMBER)
RETURN NUMBER IS
  v_total NUMBER(14,2);
BEGIN
  SELECT NVL(ROUND(SUM(cantidad * precio_unitario * (1 - descuento_pct / 100)), 2), 0)
    INTO v_total
    FROM DETALLE_FACTURA
   WHERE id_factura = p_id_factura;
  RETURN v_total;
END FN_TOTAL_FACTURA;
/

-- RN-09: solo un empleado de tipo VET puede registrarse como VETERINARIO.
CREATE OR REPLACE TRIGGER TRG_VETERINARIO_TIPO
BEFORE INSERT OR UPDATE OF id_empleado ON VETERINARIO
FOR EACH ROW
DECLARE
  v_tipo EMPLEADO.tipo_empleado%TYPE;
BEGIN
  SELECT tipo_empleado INTO v_tipo FROM EMPLEADO WHERE id_empleado = :NEW.id_empleado;
  IF v_tipo <> 'VET' THEN
    RAISE_APPLICATION_ERROR(-20001, 'El empleado ' || :NEW.id_empleado || ' no es de tipo VET.');
  END IF;
END;
/

-- RN-09: solo un empleado de tipo AUX puede registrarse como AUXILIAR.
CREATE OR REPLACE TRIGGER TRG_AUXILIAR_TIPO
BEFORE INSERT OR UPDATE OF id_empleado ON AUXILIAR
FOR EACH ROW
DECLARE
  v_tipo EMPLEADO.tipo_empleado%TYPE;
BEGIN
  SELECT tipo_empleado INTO v_tipo FROM EMPLEADO WHERE id_empleado = :NEW.id_empleado;
  IF v_tipo <> 'AUX' THEN
    RAISE_APPLICATION_ERROR(-20002, 'El empleado ' || :NEW.id_empleado || ' no es de tipo AUX.');
  END IF;
END;
/

-- RN-08: el veterinario de una cita debe laborar en la sede de la cita.
CREATE OR REPLACE TRIGGER TRG_CITA_VETERINARIO_SEDE
BEFORE INSERT OR UPDATE OF id_veterinario, id_sede ON CITA
FOR EACH ROW
DECLARE
  v_sede EMPLEADO.id_sede%TYPE;
BEGIN
  SELECT id_sede INTO v_sede FROM EMPLEADO WHERE id_empleado = :NEW.id_veterinario;
  IF v_sede <> :NEW.id_sede THEN
    RAISE_APPLICATION_ERROR(-20003, 'El veterinario ' || :NEW.id_veterinario
      || ' labora en la sede ' || v_sede || ', no en la sede ' || :NEW.id_sede || '.');
  END IF;
END;
/

-- Solo se registra una consulta para citas en estado ATENDIDA.
CREATE OR REPLACE TRIGGER TRG_CONSULTA_CITA_ATENDIDA
BEFORE INSERT ON CONSULTA
FOR EACH ROW
DECLARE
  v_estado CITA.estado%TYPE;
BEGIN
  SELECT estado INTO v_estado FROM CITA WHERE id_cita = :NEW.id_cita;
  IF v_estado <> 'ATENDIDA' THEN
    RAISE_APPLICATION_ERROR(-20004, 'La cita ' || :NEW.id_cita || ' está en estado ' || v_estado
      || '; solo las citas ATENDIDAS generan consulta.');
  END IF;
END;
/

-- Revisión cruzada (Codex): impide eludir RN-09 cambiando el tipo de un empleado que ya tiene fila de subtipo.
CREATE OR REPLACE TRIGGER TRG_EMPLEADO_TIPO
BEFORE UPDATE OF tipo_empleado ON EMPLEADO
FOR EACH ROW
WHEN (OLD.tipo_empleado <> NEW.tipo_empleado)
DECLARE
  v_filas PLS_INTEGER;
BEGIN
  SELECT (SELECT COUNT(*) FROM VETERINARIO WHERE id_empleado = :OLD.id_empleado)
       + (SELECT COUNT(*) FROM AUXILIAR WHERE id_empleado = :OLD.id_empleado)
    INTO v_filas FROM dual;
  IF v_filas > 0 THEN
    RAISE_APPLICATION_ERROR(-20005, 'El empleado ' || :OLD.id_empleado
      || ' tiene registro de subtipo; elimínelo antes de cambiar tipo_empleado.');
  END IF;
END;
/

-- Revisión cruzada (Codex): una cita que ya generó consulta no puede dejar de estar ATENDIDA.
CREATE OR REPLACE TRIGGER TRG_CITA_ESTADO_CONSULTA
BEFORE UPDATE OF estado ON CITA
FOR EACH ROW
WHEN (OLD.estado = 'ATENDIDA' AND NEW.estado <> 'ATENDIDA')
DECLARE
  v_consultas PLS_INTEGER;
BEGIN
  SELECT COUNT(*) INTO v_consultas FROM CONSULTA WHERE id_cita = :OLD.id_cita;
  IF v_consultas > 0 THEN
    RAISE_APPLICATION_ERROR(-20006, 'La cita ' || :OLD.id_cita
      || ' ya tiene consulta registrada; no puede cambiar a ' || :NEW.estado || '.');
  END IF;
END;
/
