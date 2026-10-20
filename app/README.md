# Aplicación de SIGEVET

Hicimos estas páginas para consultar la base de datos de las clínicas Huella Sana.
No se usa ORM y todas las consultas están escritas a mano en SQL, con variables de enlace.

Desde la raíz del proyecto:

```bash
python3 -m venv .venv
source .venv/bin/activate
pip install -r app/requirements.txt
cd app
cp config.ejemplo.ini config.ini
```

En `config.ini` ponemos los datos de Oracle en la sección `[oracle]`:
`usuario`, `clave`, `host`, `puerto` y `servicio`. Hay que cambiar la clave de ejemplo.
El archivo no se sube a Git. Las variables `SIGEVET_DB_USER`, `SIGEVET_DB_PASSWORD`
y `SIGEVET_DB_DSN` tienen prioridad sobre sus valores en el archivo.
El usuario necesita permiso de lectura sobre las tablas de `VETDB`.
Usamos python-oracledb en modo thin, sin instalar Oracle Client.

Con Oracle encendido, podemos probar y arrancar así:

```bash
python3 probar_conexion.py
python3 app.py
```

En la máquina virtual no hace falta nada de esto: `sudo bash vm/scripts/instalar_app.sh` la deja
instalada como servicio (`sigevet-web`) que arranca sola con la VM.

Flask escucha en `0.0.0.0:5000`. Para abrirlo en el mismo equipo usamos
`http://127.0.0.1:5000`.

Hay inicio con los conteos y próximas citas, clientes con búsqueda y páginas,
detalle de cliente, mascotas con historia clínica, agenda, inventario,
facturas con su detalle y personal. En álgebra relacional se pueden ver los
55 ejercicios del archivo de `database/03_varios/`; la consulta se ejecuta
cuando abrimos un ejercicio. La aplicación solo consulta datos.
