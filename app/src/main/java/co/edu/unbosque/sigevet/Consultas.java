package co.edu.unbosque.sigevet;

public class Consultas {
    static final String SQL_TOTAL_CLIENTES = """
        SELECT COUNT(*) AS total FROM CLIENTE c
        WHERE UPPER(c.num_documento) LIKE ? OR UPPER(c.apellidos) LIKE ?
        """;

    static final String SQL_CLIENTES = """
        SELECT c.id_cliente, c.tipo_documento, c.num_documento, c.nombres, c.apellidos, c.email, ci.nombre AS ciudad
        FROM CLIENTE c JOIN CIUDAD ci ON ci.id_ciudad = c.id_ciudad
        WHERE UPPER(c.num_documento) LIKE ? OR UPPER(c.apellidos) LIKE ?
        ORDER BY c.apellidos, c.nombres, c.id_cliente
        LIMIT ?, ?
        """;

    static final String SQL_CLIENTE = """
        SELECT c.*, ci.nombre AS ciudad, d.nombre AS departamento
        FROM CLIENTE c JOIN CIUDAD ci ON ci.id_ciudad = c.id_ciudad
        JOIN DEPARTAMENTO d ON d.id_departamento = ci.id_departamento
        WHERE c.id_cliente = ?
        """;

    static final String SQL_TELEFONOS = """
        SELECT telefono, tipo FROM TELEFONO_CLIENTE
        WHERE id_cliente = ? ORDER BY tipo, telefono
        """;

    static final String SQL_MASCOTAS_CLIENTE = """
        SELECT m.id_mascota, m.nombre, m.estado, m.sexo, r.nombre AS raza, e.nombre AS especie
        FROM MASCOTA m JOIN RAZA r ON r.id_raza = m.id_raza
        JOIN ESPECIE e ON e.id_especie = r.id_especie
        WHERE m.id_cliente = ? ORDER BY m.nombre, m.id_mascota
        """;

    static final String SQL_FACTURAS_CLIENTE = """
        SELECT f.id_factura, f.numero_factura, f.fecha_emision, f.estado, s.nombre AS sede, IFNULL(SUM(df.cantidad * df.precio_unitario * (1 - df.descuento_pct / 100)), 0) AS total
        FROM FACTURA f JOIN SEDE s ON s.id_sede = f.id_sede
        LEFT JOIN DETALLE_FACTURA df ON df.id_factura = f.id_factura
        WHERE f.id_cliente = ?
        GROUP BY f.id_factura, f.numero_factura, f.fecha_emision, f.estado, s.nombre
        ORDER BY f.fecha_emision DESC, f.id_factura DESC
        """;

    static final String SQL_MASCOTA = """
        SELECT m.*, r.nombre AS raza, e.nombre AS especie, CONCAT(c.nombres, ' ', c.apellidos) AS cliente
        FROM MASCOTA m JOIN RAZA r ON r.id_raza = m.id_raza
        JOIN ESPECIE e ON e.id_especie = r.id_especie
        JOIN CLIENTE c ON c.id_cliente = m.id_cliente
        WHERE m.id_mascota = ?
        """;

    static final String SQL_HISTORIA = """
        SELECT * FROM HISTORIA_CLINICA WHERE id_mascota = ?
        """;

    static final String SQL_CONSULTAS = """
        SELECT co.*, ci.fecha_hora, CONCAT(e.nombres, ' ', e.apellidos) AS veterinario, s.nombre AS sede
        FROM CONSULTA co JOIN CITA ci ON ci.id_cita = co.id_cita
        JOIN EMPLEADO e ON e.id_empleado = ci.id_veterinario
        JOIN SEDE s ON s.id_sede = ci.id_sede
        WHERE ci.id_mascota = ?
        ORDER BY co.fecha_hora_inicio DESC, co.id_consulta DESC
        """;

    static final String SQL_PRESCRIPCIONES = """
        SELECT p.*, m.nombre AS medicamento, m.presentacion
        FROM PRESCRIPCION p
        JOIN MEDICAMENTO m ON m.id_medicamento = p.id_medicamento
        JOIN CONSULTA co ON co.id_consulta = p.id_consulta
        JOIN CITA ci ON ci.id_cita = co.id_cita
        WHERE ci.id_mascota = ?
        ORDER BY p.id_consulta, m.nombre, p.id_medicamento
        """;

    static final String SQL_SEDES = """
        SELECT id_sede, nombre FROM SEDE ORDER BY nombre
        """;

    static final String SQL_VETERINARIOS = """
        SELECT e.id_empleado, e.id_empleado AS id_veterinario, CONCAT(e.nombres, ' ', e.apellidos) AS nombre, e.id_sede
        FROM EMPLEADO e JOIN VETERINARIO v ON v.id_empleado = e.id_empleado
        ORDER BY e.apellidos, e.nombres, e.id_empleado
        """;

    static final String SQL_AGENDA = """
        SELECT c.id_cita, c.fecha_hora, c.estado, c.id_mascota, c.motivo, m.nombre AS mascota, sv.nombre AS servicio, s.nombre AS sede, CONCAT(e.nombres, ' ', e.apellidos) AS veterinario
        FROM CITA c JOIN MASCOTA m ON m.id_mascota = c.id_mascota
        JOIN SERVICIO sv ON sv.id_servicio = c.id_servicio
        JOIN EMPLEADO e ON e.id_empleado = c.id_veterinario
        JOIN SEDE s ON s.id_sede = c.id_sede
        WHERE (? IS NULL OR c.id_sede = ?)
        AND (? IS NULL OR c.id_veterinario = ?)
        AND (? IS NULL OR c.estado = ?)
        AND (? IS NULL OR c.fecha_hora >= ?)
        AND (? IS NULL OR c.fecha_hora < ?)
        ORDER BY c.fecha_hora, c.id_cita
        """;

    static final String SQL_INVENTARIO = """
        SELECT s.nombre AS sede, m.nombre AS medicamento, m.presentacion, i.stock_actual, i.stock_minimo, i.fecha_actualizacion
        FROM INVENTARIO i JOIN SEDE s ON s.id_sede = i.id_sede
        JOIN MEDICAMENTO m ON m.id_medicamento = i.id_medicamento
        WHERE (? IS NULL OR i.id_sede = ?)
        ORDER BY s.nombre, m.nombre, m.presentacion
        """;

    static final String SQL_FACTURAS = """
        SELECT f.id_factura, f.numero_factura, f.fecha_emision, f.estado, CONCAT(c.nombres, ' ', c.apellidos) AS cliente, s.nombre AS sede, IFNULL(t.total, 0) AS total
        FROM FACTURA f JOIN CLIENTE c ON c.id_cliente = f.id_cliente
        JOIN SEDE s ON s.id_sede = f.id_sede
        LEFT JOIN (
        SELECT id_factura, SUM(cantidad * precio_unitario * (1 - descuento_pct / 100)) AS total
        FROM DETALLE_FACTURA GROUP BY id_factura ) t ON t.id_factura = f.id_factura
        WHERE (? IS NULL OR f.id_sede = ?)
        AND (? IS NULL OR f.estado = ?)
        ORDER BY f.fecha_emision DESC, f.id_factura DESC
        """;

    static final String SQL_FACTURA = """
        SELECT f.*, CONCAT(c.nombres, ' ', c.apellidos) AS cliente, s.nombre AS sede, IFNULL(t.total, 0) AS total
        FROM FACTURA f JOIN CLIENTE c ON c.id_cliente = f.id_cliente
        JOIN SEDE s ON s.id_sede = f.id_sede
        LEFT JOIN (
        SELECT id_factura, SUM(cantidad * precio_unitario * (1 - descuento_pct / 100)) AS total
        FROM DETALLE_FACTURA GROUP BY id_factura ) t ON t.id_factura = f.id_factura
        WHERE f.id_factura = ?
        """;

    static final String SQL_LINEAS = """
        SELECT d.num_linea, COALESCE(s.nombre, m.nombre) AS concepto, d.cantidad, d.precio_unitario, d.descuento_pct, d.cantidad * d.precio_unitario * (1 - d.descuento_pct / 100) AS subtotal
        FROM DETALLE_FACTURA d
        LEFT JOIN SERVICIO s ON s.id_servicio = d.id_servicio
        LEFT JOIN MEDICAMENTO m ON m.id_medicamento = d.id_medicamento
        WHERE d.id_factura = ? ORDER BY d.num_linea
        """;

    static final String SQL_DIRECTORES = """
        SELECT s.id_sede, s.nombre, s.direccion, s.telefono, CONCAT(e.nombres, ' ', e.apellidos) AS director
        FROM SEDE s LEFT JOIN EMPLEADO e ON e.id_empleado = s.id_director
        ORDER BY s.nombre
        """;

    static final String SQL_EMPLEADOS = """
        SELECT e.*, s.nombre AS sede
        FROM EMPLEADO e JOIN SEDE s ON s.id_sede = e.id_sede
        WHERE (? IS NULL OR e.id_sede = ?)
        ORDER BY s.nombre, e.apellidos, e.nombres, e.id_empleado
        """;

    static final String SQL_ESPECIALIDADES = """
        SELECT ve.id_empleado, es.nombre
        FROM VETERINARIO_ESPECIALIDAD ve
        JOIN ESPECIALIDAD es ON es.id_especialidad = ve.id_especialidad
        JOIN EMPLEADO e ON e.id_empleado = ve.id_empleado
        WHERE (? IS NULL OR e.id_sede = ?)
        ORDER BY ve.id_empleado, es.nombre
        """;

    static final String SQL_INGRESOS = """
        SELECT s.nombre AS sede, IFNULL(SUM(d.cantidad*d.precio_unitario*(1-d.descuento_pct/100)),0) AS total
        FROM SEDE s LEFT JOIN FACTURA f ON f.id_sede=s.id_sede AND f.estado='PAGADA'
        LEFT JOIN DETALLE_FACTURA d ON d.id_factura=f.id_factura
        GROUP BY s.id_sede,s.nombre ORDER BY s.nombre
        """;

    static final String SQL_ESTADOS_CITAS = """
        SELECT estado,COUNT(*) AS total FROM CITA GROUP BY estado ORDER BY estado
        """;

    static final String SQL_STOCK_BAJO = """
        SELECT s.nombre AS sede,m.nombre AS medicamento,i.stock_actual,i.stock_minimo
        FROM INVENTARIO i JOIN SEDE s ON s.id_sede=i.id_sede
        JOIN MEDICAMENTO m ON m.id_medicamento=i.id_medicamento
        WHERE i.stock_actual<i.stock_minimo ORDER BY s.nombre,m.nombre
        """;

    static final String SQL_PROXIMAS_CITAS = """
        SELECT c.id_cita,c.fecha_hora,c.estado,m.id_mascota,m.nombre AS mascota, sv.nombre AS servicio,CONCAT(e.nombres, ' ', e.apellidos) AS veterinario,s.nombre AS sede
        FROM CITA c JOIN MASCOTA m ON m.id_mascota=c.id_mascota
        JOIN SERVICIO sv ON sv.id_servicio=c.id_servicio
        JOIN EMPLEADO e ON e.id_empleado=c.id_veterinario
        JOIN SEDE s ON s.id_sede=c.id_sede
        WHERE c.estado='PROGRAMADA' AND c.fecha_hora>=NOW()
        ORDER BY c.fecha_hora,c.id_cita LIMIT 10
        """;

    static final String SQL_CONTEOS = """
        SELECT 'DEPARTAMENTO' AS tabla, COUNT(*) AS total FROM DEPARTAMENTO
        UNION ALL SELECT 'CIUDAD', COUNT(*) FROM CIUDAD
        UNION ALL SELECT 'SEDE', COUNT(*) FROM SEDE
        UNION ALL SELECT 'EMPLEADO', COUNT(*) FROM EMPLEADO
        UNION ALL SELECT 'VETERINARIO', COUNT(*) FROM VETERINARIO
        UNION ALL SELECT 'AUXILIAR', COUNT(*) FROM AUXILIAR
        UNION ALL SELECT 'ESPECIALIDAD', COUNT(*) FROM ESPECIALIDAD
        UNION ALL SELECT 'VETERINARIO_ESPECIALIDAD', COUNT(*) FROM VETERINARIO_ESPECIALIDAD
        UNION ALL SELECT 'CLIENTE', COUNT(*) FROM CLIENTE
        UNION ALL SELECT 'TELEFONO_CLIENTE', COUNT(*) FROM TELEFONO_CLIENTE
        UNION ALL SELECT 'ESPECIE', COUNT(*) FROM ESPECIE
        UNION ALL SELECT 'RAZA', COUNT(*) FROM RAZA
        UNION ALL SELECT 'MASCOTA', COUNT(*) FROM MASCOTA
        UNION ALL SELECT 'HISTORIA_CLINICA', COUNT(*) FROM HISTORIA_CLINICA
        UNION ALL SELECT 'SERVICIO', COUNT(*) FROM SERVICIO
        UNION ALL SELECT 'CITA', COUNT(*) FROM CITA
        UNION ALL SELECT 'CONSULTA', COUNT(*) FROM CONSULTA
        UNION ALL SELECT 'MEDICAMENTO', COUNT(*) FROM MEDICAMENTO
        UNION ALL SELECT 'PRESCRIPCION', COUNT(*) FROM PRESCRIPCION
        UNION ALL SELECT 'INVENTARIO', COUNT(*) FROM INVENTARIO
        UNION ALL SELECT 'FACTURA', COUNT(*) FROM FACTURA
        UNION ALL SELECT 'DETALLE_FACTURA', COUNT(*) FROM DETALLE_FACTURA
        """;

}
