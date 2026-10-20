import configparser
import os
from pathlib import Path

import oracledb


def obtener_conexion():
    config = configparser.ConfigParser(interpolation=None)
    config.read(Path(__file__).with_name("config.ini"), encoding="utf-8")
    usuario = os.getenv("SIGEVET_DB_USER", config.get("oracle", "usuario", fallback=""))
    clave = os.getenv("SIGEVET_DB_PASSWORD", config.get("oracle", "clave", fallback=""))
    dsn = os.getenv("SIGEVET_DB_DSN")
    if dsn is None:
        host = config.get("oracle", "host", fallback="127.0.0.1")
        puerto = config.get("oracle", "puerto", fallback="1521")
        servicio = config.get("oracle", "servicio", fallback="FREEPDB1")
        dsn = f"{host}:{puerto}/{servicio}"

    bd = oracledb.connect(user=usuario, password=clave, dsn=dsn)
    # VET_APP no es dueño de las tablas, así que apuntamos al esquema VETDB
    cursor = bd.cursor()
    cursor.execute("ALTER SESSION SET CURRENT_SCHEMA = VETDB")
    cursor.close()
    return bd


def consultar(bd, sql, params=None):
    with bd.cursor() as cursor:
        cursor.execute(sql, params or {})
        columnas = [columna[0].lower() for columna in cursor.description]
        return [dict(zip(columnas, fila)) for fila in cursor.fetchall()]


def consultar_uno(bd, sql, params=None):
    filas = consultar(bd, sql, params)
    return filas[0] if filas else None
