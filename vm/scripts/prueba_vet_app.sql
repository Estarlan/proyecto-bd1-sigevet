-- Prueba del usuario de la aplicación (vet_app): puede leer pero no modificar.
-- mysql -u vet_app -p -t < vm/scripts/prueba_vet_app.sql
SELECT CURRENT_USER() AS usuario, CURRENT_ROLE() AS rol;
SELECT (SELECT COUNT(*) FROM sigevet.MASCOTA) AS mascotas,
       (SELECT COUNT(*) FROM sigevet.CITA) AS citas,
       (SELECT COUNT(*) FROM sigevet.FACTURA) AS facturas;
SELECT nombre FROM sigevet.CIUDAD WHERE nombre IN ('Bogotá D.C.', 'Chía') ORDER BY nombre;
SHOW GRANTS;
-- Cada una de estas debe fallar con "command denied" (error 1142)
DELETE FROM sigevet.MASCOTA;
