from datetime import date, datetime, timedelta

from flask import Flask, abort, render_template, request

import conexion
import consultas as sql
from algebra import cargar_ejercicios

app = Flask(__name__)
ejercicios = cargar_ejercicios()

@app.template_filter('fecha')
def fecha(valor):
    if valor is None:
        return '-'
    if isinstance(valor, datetime):
        if (valor.hour, valor.minute, valor.second) != (0, 0, 0):
            return valor.strftime('%d/%m/%Y %H:%M')
    if isinstance(valor, date):
        return valor.strftime('%d/%m/%Y')
    return str(valor)

@app.template_filter('pesos')
def pesos(valor):
    return '$ {:,.0f}'.format(valor or 0).replace(',', '.')

def filtro_numero(nombre):
    try:
        numero = int(request.args.get(nombre, '') or 0)
        return numero if numero > 0 else None
    except ValueError:
        return None

def filtro_fecha(nombre, dia_siguiente=False):
    try:
        valor = datetime.strptime(request.args.get(nombre, ''), '%Y-%m-%d')
        return valor + timedelta(days=1) if dia_siguiente else valor
    except (ValueError, OverflowError):
        return None

@app.route('/')
def inicio():
    with conexion.obtener_conexion() as bd:
        datos = {'conteos': conexion.consultar(bd, sql.SQL_CONTEOS),
                 'ingresos': conexion.consultar(bd, sql.SQL_INGRESOS),
                 'estados_citas': conexion.consultar(bd, sql.SQL_ESTADOS_CITAS),
                 'stock_bajo': conexion.consultar(bd, sql.SQL_STOCK_BAJO),
                 'proximas': conexion.consultar(bd, sql.SQL_PROXIMAS_CITAS)}
    return render_template('inicio.html', datos=datos)

@app.route('/clientes')
def clientes():
    buscar = request.args.get('buscar', '').strip()[:100]
    try:
        pagina = max(1, int(request.args.get('pagina', '1')))
    except ValueError:
        pagina = 1
    # paginación sencilla, 20 por página
    parametros = {'q': '%' + buscar.upper() + '%'}
    with conexion.obtener_conexion() as bd:
        total = conexion.consultar_uno(bd, sql.SQL_TOTAL_CLIENTES, parametros)['total']
        total_paginas = max(1, (total + 19) // 20)
        pagina = min(pagina, total_paginas)
        parametros.update(offset=(pagina - 1) * 20, per_page=20)
        filas = conexion.consultar(bd, sql.SQL_CLIENTES, parametros)
    return render_template('clientes.html', datos={'filas': filas, 'total': total},
                           buscar=buscar, pagina=pagina, total_paginas=total_paginas)

@app.route('/clientes/<int:id>')
def cliente(id):
    parametros = {'id_cliente': id}
    with conexion.obtener_conexion() as bd:
        datos = {'datos': conexion.consultar_uno(bd, sql.SQL_CLIENTE, parametros)}
        if datos['datos'] is None:
            abort(404)
        datos['telefonos'] = conexion.consultar(bd, sql.SQL_TELEFONOS, parametros)
        datos['mascotas'] = conexion.consultar(bd, sql.SQL_MASCOTAS_CLIENTE, parametros)
        datos['facturas'] = conexion.consultar(bd, sql.SQL_FACTURAS_CLIENTE, parametros)
    return render_template('cliente.html', datos=datos)

@app.route('/mascotas/<int:id>')
def mascota(id):
    parametros = {'id_mascota': id}
    with conexion.obtener_conexion() as bd:
        datos = {'datos': conexion.consultar_uno(bd, sql.SQL_MASCOTA, parametros)}
        if datos['datos'] is None:
            abort(404)
        datos['historia'] = conexion.consultar_uno(bd, sql.SQL_HISTORIA, parametros)
        consultas = conexion.consultar(bd, sql.SQL_CONSULTAS, parametros)
        prescripciones = conexion.consultar(bd, sql.SQL_PRESCRIPCIONES, parametros)
        for consulta in consultas:
            consulta['prescripciones'] = [p for p in prescripciones
                                         if p['id_consulta'] == consulta['id_consulta']]
        datos['consultas'] = consultas
    return render_template('mascota.html', datos=datos)

@app.route('/agenda')
def agenda():
    parametros = {'sede': filtro_numero('sede'), 'veterinario': filtro_numero('veterinario'),
                  'estado': request.args.get('estado') or None,
                  'desde': filtro_fecha('desde'), 'hasta': filtro_fecha('hasta', True)}
    with conexion.obtener_conexion() as bd:
        filas = conexion.consultar(bd, sql.SQL_AGENDA, parametros)
        opciones = {'sedes': conexion.consultar(bd, sql.SQL_SEDES),
                    'veterinarios': conexion.consultar(bd, sql.SQL_VETERINARIOS)}
    return render_template('agenda.html', filas=filas, opciones=opciones,
                           estados=['PROGRAMADA', 'ATENDIDA', 'CANCELADA', 'NO_ASISTIO'])

@app.route('/inventario')
def inventario():
    with conexion.obtener_conexion() as bd:
        filas = conexion.consultar(bd, sql.SQL_INVENTARIO, {'sede': filtro_numero('sede')})
        opciones = {'sedes': conexion.consultar(bd, sql.SQL_SEDES)}
    return render_template('inventario.html', filas=filas, opciones=opciones)

@app.route('/facturas')
def facturas():
    parametros = {'sede': filtro_numero('sede'), 'estado': request.args.get('estado') or None}
    with conexion.obtener_conexion() as bd:
        filas = conexion.consultar(bd, sql.SQL_FACTURAS, parametros)
        opciones = {'sedes': conexion.consultar(bd, sql.SQL_SEDES)}
    return render_template('facturas.html', filas=filas, opciones=opciones,
                           estados=['PAGADA', 'PENDIENTE', 'ANULADA'])

@app.route('/facturas/<int:id>')
def factura(id):
    parametros = {'id_factura': id}
    with conexion.obtener_conexion() as bd:
        datos = {'datos': conexion.consultar_uno(bd, sql.SQL_FACTURA, parametros)}
        if datos['datos'] is None:
            abort(404)
        datos['lineas'] = conexion.consultar(bd, sql.SQL_LINEAS, parametros)
        datos['total'] = datos['datos']['total']
    return render_template('factura.html', datos=datos)

@app.route('/personal')
def personal():
    parametros = {'sede': filtro_numero('sede')}
    with conexion.obtener_conexion() as bd:
        datos = {'sedes': conexion.consultar(bd, sql.SQL_DIRECTORES),
                 'empleados': conexion.consultar(bd, sql.SQL_EMPLEADOS, parametros)}
        especialidades = conexion.consultar(bd, sql.SQL_ESPECIALIDADES, parametros)
        for empleado in datos['empleados']:
            empleado['especialidades'] = [e['nombre'] for e in especialidades
                                         if e['id_empleado'] == empleado['id_empleado']]
    return render_template('personal.html', datos=datos)

@app.route('/algebra')
def algebra():
    grupos = {}
    for ejercicio in ejercicios:
        grupos.setdefault(ejercicio['grupo'], []).append(ejercicio)
    return render_template('algebra.html', grupos=grupos)

@app.route('/algebra/<int:numero>')
def ejercicio(numero):
    elegido = next((e for e in ejercicios if e['numero'] == numero), None)
    if elegido is None:
        abort(404)
    with conexion.obtener_conexion() as bd:
        filas = conexion.consultar(bd, elegido['sql'])
    columnas = list(filas[0]) if filas else []
    return render_template('ejercicio.html', ejercicio=elegido, filas=filas, columnas=columnas)

@app.errorhandler(404)
def no_encontrado(error):
    return render_template('error.html', titulo='Página no encontrada',
                           mensaje='No existe ese registro o ejercicio.'), 404

@app.errorhandler(500)
def error_servidor(error):
    return render_template('error.html', titulo='No se pudieron consultar los datos',
                           mensaje='Revisa la conexión a Oracle y el archivo config.ini.'), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=False)
