# Scripts de base de datos - SIGEVET

Scripts que llevan el modelo lógico de SIGEVET (Red de Clínicas Veterinarias *Huella Sana*) al modelo físico en
**MySQL 8.4 LTS** (base de datos `sigevet`), sobre la máquina virtual **Oracle Linux 9**. La base se administra con
**MySQL Workbench**.

## Estructura

| Carpeta / archivo | Contenido |
|---|---|
| `01_ddl/00_crear_usuarios.sql` | Base `sigevet`, usuarios `vetdb` (dueño) y `vet_app` (solo lectura) y rol `rol_sigevet_lectura`. Se ejecuta como root. |
| `01_ddl/01_crear_tablas.sql` | **Creación de objetos:** 22 tablas con 113 restricciones con nombre (PK, FK, UNIQUE, CHECK), la FK circular SEDE→EMPLEADO y el comentario de cada tabla y columna (diccionario de datos dentro de la base). |
| `01_ddl/02_indices.sql` | Índices de búsqueda (InnoDB ya indexa las PK, UNIQUE y FK). |
| `01_ddl/03_vistas.sql` | 6 vistas: total de factura, detalle de mascota con edad, historial clínico, agenda, stock bajo e ingresos por sede y mes. |
| `01_ddl/04_programas.sql` | Funciones `FN_EDAD_MASCOTA` y `FN_TOTAL_FACTURA`, 3 procedimientos y 9 disparadores con las reglas de negocio (RN-08, RN-09, consulta solo de citas atendidas). |
| `02_dml/01_…sql` a `23_…sql` | **Inserción de registros:** 1.091 registros de prueba, un archivo por tabla en orden de dependencias. |
| `03_varios/01_consultas_algebra_relacional.sql` | **Scripts varios:** las 55 consultas SQL equivalentes a los ejercicios de álgebra relacional. |
| `03_varios/02_verificacion.sql` | Conteo de objetos, restricciones y registros, y prueba de que las reglas rechazan datos inválidos. |
| `03_varios/03_reiniciar_base.sql` | Borra la base `sigevet` y la crea vacía para reinstalar desde cero. |
| `instalar_esquema.sql` | Script maestro: ejecuta todo en el orden correcto y termina con la verificación. |

## Orden de ejecución

```text
1. 01_ddl/00_crear_usuarios.sql       (como root)
2. 01_ddl/01_crear_tablas.sql         (como vetdb, igual que todos los siguientes)
3. 01_ddl/02_indices.sql
4. 01_ddl/04_programas.sql            antes de los datos: los disparadores validan cada INSERT
5. 02_dml/01_departamento.sql … 23_detalle_factura.sql
6. 01_ddl/03_vistas.sql
7. 03_varios/02_verificacion.sql
```

La FK `FK_SEDE_DIRECTOR` es circular (EMPLEADO referencia a SEDE y SEDE a EMPLEADO): las sedes se insertan sin
director (`02_dml/03_sede.sql`) y el director se asigna con `UPDATE` después de cargar los empleados
(`02_dml/05_sede_director.sql`).

## Requisito del servidor

`vetdb` crea funciones y disparadores sin ser administrador. Con el registro binario activo (MySQL 8.x lo trae así por
defecto) eso exige `log_bin_trust_function_creators = 1` en la configuración del servidor. `vm/scripts/instalar_mysql.sh`
ya lo escribe en `/etc/my.cnf.d/sigevet.cnf`; en cualquier otro MySQL hay que agregarlo, porque si no el script
`01_ddl/04_programas.sql` falla con el error 1419.

## Cómo ejecutar

Dentro de la máquina virtual, desde la carpeta del proyecto:

```bash
bash vm/scripts/cargar_bd.sh               # crea la base y los usuarios y carga todo
bash vm/scripts/cargar_bd.sh --reiniciar   # borra la base y la vuelve a cargar
```

A mano con el cliente `mysql`, desde la carpeta `database/`:

```bash
mysql -u root -p -e "SET @clave_vetdb='...'; SET @clave_vet_app='...'; SOURCE 01_ddl/00_crear_usuarios.sql;"
mysql -u vetdb -p --default-character-set=utf8mb4 < instalar_esquema.sql
```

Con **MySQL Workbench**: conexión a `127.0.0.1:3306` con el usuario `vetdb` (o con root para el primer script),
y se abre cada archivo con *File → Open SQL Script* y se ejecuta con el rayo, en el orden de arriba. Workbench no
entiende el comando `SOURCE`, por eso ahí se abren los archivos uno por uno en vez de `instalar_esquema.sql`.

## Credenciales

Las claves no están en el repositorio. Cada uno copia `vm/claves.ejemplo` como `vm/claves.env` (está en el
`.gitignore`) y pone ahí `MYSQL_ROOT_PWD`, `VETDB_PWD` y `VET_APP_PWD`.

## Datos de prueba

Los 1.091 registros son ficticios: nombres, documentos, teléfonos y correos inventados. Los generamos con un script
auxiliar para que fueran coherentes entre tablas (citas solo en sedes donde trabaja el veterinario, consultas solo de
citas atendidas, facturas que cuadran con sus detalles) y de ahí sacamos los INSERT de `02_dml/`.

## Versión anterior en Oracle

El proyecto empezó en Oracle Database Free. Esa versión de los scripts sigue en el historial de Git: el último commit
con Oracle es `2830233`, justo antes del cambio a MySQL.
