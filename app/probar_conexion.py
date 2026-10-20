from conexion import obtener_conexion


def probar():
    with obtener_conexion() as bd:
        print('Versión de Oracle:', bd.version)
        with bd.cursor() as cursor:
            cursor.execute("""SELECT 'Clientes', COUNT(*) FROM CLIENTE
                              UNION ALL SELECT 'Mascotas', COUNT(*) FROM MASCOTA
                              UNION ALL SELECT 'Citas', COUNT(*) FROM CITA""")
            for nombre, total in cursor:
                print(nombre + ':', total)
    print('La conexión quedó cerrada.')


if __name__ == '__main__':
    probar()
