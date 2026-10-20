# Scripts de base de datos - SIGEVET

Scripts que llevan el modelo lógico de SIGEVET (Red de Clínicas Veterinarias *Huella Sana*) al modelo físico en
**Oracle AI Database 26ai Free** (PDB `FREEPDB1`), sobre la máquina virtual **Oracle Linux 9**.

## Estructura

| Carpeta / archivo | Contenido |
|---|---|
| `01_ddl/00_crear_usuarios.sql` | Usuarios `VETDB` (dueño del esquema) y `VET_APP` (solo lectura) y rol `ROL_SIGEVET_LECTURA`. Se ejecuta como SYSTEM. |
| `01_ddl/01_crear_tablas.sql` | **Creación de objetos:** 22 tablas con 113 restricciones con nombre (PK, FK, UNIQUE, CHECK) y la FK circular SEDE→EMPLEADO. |
| `01_ddl/02_comentarios.sql` | Comentarios de tablas y columnas (diccionario de datos dentro del catálogo de Oracle). |
| `01_ddl/03_secuencias_indices.sql` | Secuencias para las llaves nuevas e índices sobre llaves foráneas y columnas de búsqueda. |
| `01_ddl/04_vistas.sql` | 6 vistas: total de factura, detalle de mascota con edad, historial clínico, agenda, stock bajo e ingresos por sede y mes. |
| `01_ddl/05_programas.sql` | Funciones `FN_EDAD_MASCOTA` y `FN_TOTAL_FACTURA` y 6 disparadores con las reglas de negocio (RN-08, RN-09, consulta solo de citas atendidas). |
| `02_dml/01_…sql` a `23_…sql` | **Inserción de registros:** 1.091 registros de prueba, un archivo por tabla en orden de dependencias. |
| `03_varios/01_consultas_algebra_relacional.sql` | **Scripts varios:** las 55 consultas SQL equivalentes a los ejercicios de álgebra relacional. |
| `03_varios/02_verificacion.sql` | Conteo de objetos, restricciones y registros, y prueba de que las reglas rechazan datos inválidos. |
| `03_varios/03_permisos_lectura.sql` | Otorga SELECT sobre tablas y vistas al rol de solo lectura de la aplicación web. |
| `03_varios/04_eliminar_objetos.sql` | Borra todos los objetos del esquema para reinstalar desde cero. |
| `instalar_esquema.sql` | Script maestro: ejecuta todo en el orden correcto y termina con la verificación. |

## Orden de ejecución

```text
1. 01_ddl/00_crear_usuarios.sql       (como SYSTEM)
2. 01_ddl/01_crear_tablas.sql         (como VETDB, igual que todos los siguientes)
3. 01_ddl/02_comentarios.sql
4. 01_ddl/03_secuencias_indices.sql
5. 01_ddl/05_programas.sql            antes de los datos: los disparadores validan cada INSERT
6. 02_dml/01_departamento.sql … 23_detalle_factura.sql
7. 01_ddl/04_vistas.sql
8. 03_varios/03_permisos_lectura.sql
9. 03_varios/02_verificacion.sql
```

La FK `FK_SEDE_DIRECTOR` es circular (EMPLEADO referencia a SEDE y SEDE a EMPLEADO): las sedes se insertan sin
director (`02_dml/03_sede.sql`) y el director se asigna con `UPDATE` después de cargar los empleados
(`02_dml/05_sede_director.sql`).

## Cómo ejecutar

Dentro de la máquina virtual, desde la carpeta del proyecto:

```bash
bash vm/scripts/cargar_bd.sh               # crea usuarios y carga todo
bash vm/scripts/cargar_bd.sh --reiniciar   # borra el esquema y vuelve a cargar
```

O a mano con SQL*Plus desde la carpeta `database/`:

```bash
export NLS_LANG=AMERICAN_AMERICA.AL32UTF8
sqlplus system/<clave>@localhost:1521/FREEPDB1 @01_ddl/00_crear_usuarios.sql <clave_vetdb> <clave_vet_app>
sqlplus VETDB/<clave_vetdb>@localhost:1521/FREEPDB1 @instalar_esquema.sql
```

`NLS_LANG` con `AL32UTF8` hace falta para que las tildes y la ñ lleguen bien a la base de datos.

## Credenciales

Las claves no están en el repositorio. Cada uno copia `vm/claves.ejemplo` como `vm/claves.env` (está en el
`.gitignore`) y pone ahí `ORACLE_PWD`, `VETDB_PWD` y `VET_APP_PWD`.

## Datos de prueba

Los 1.091 registros son ficticios: nombres, documentos, teléfonos y correos inventados. Los generamos con un script
auxiliar para que fueran coherentes entre tablas (citas solo en sedes donde trabaja el veterinario, consultas solo de
citas atendidas, facturas que cuadran con sus detalles) y de ahí sacamos los INSERT de `02_dml/`.
