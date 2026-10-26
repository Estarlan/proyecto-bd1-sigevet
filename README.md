<!-- Created: [unknown] -->
<!-- Updated: 18:55 24-Oct-2026 -->
<!-- Updated: 18:55 24-Oct-2026 -->
# SIGEVET - Sistema Integral de Gestión Veterinaria

Proyecto final de **Bases de Datos 1**, Universidad El Bosque, Ingeniería de Sistemas.
Docente: Ing., Esp., M.Ed. Christian Felipe Duarte Arévalo.

Hicimos la base de datos de una red de clínicas veterinarias que nos inventamos, *Huella Sana S.A.S.*, con 5 sedes.
Cubre clientes, mascotas con su historia clínica, citas, consultas y fórmulas, inventario de medicamentos por sede y
facturación. La base corre en **MySQL 8.4** sobre una máquina virtual con **Oracle Linux 9** y se administra con
**MySQL Workbench**. Encima hay
una aplicación web sencilla en **Java con Spring Boot** que solo lee datos, sin ORM (SQL escrito a mano con JDBC).

| Integrante | Rol |
|---|---|
| Juan David Polo Herrera | Gerente del proyecto y DBA |
| Sebastián Castellanos Parra | Analista y modelador de datos |
| Juan Miguel Ramos | Desarrollador web e infraestructura |

## Qué hay en el repositorio

```text
database/      scripts SQL: creación de objetos, inserción de registros y varios (ver database/README.md)
app/           aplicación web en Java (Spring Boot); el SQL está escrito a mano con JDBC, sin ORM (ver app/README.md)
vm/            scripts de la máquina virtual: instalar MySQL, cargar la base, instalar la app (ver vm/README.md)
documentos/    documentos de la Fase 1, la Fase 2 y los transversales, los modelos de MySQL Workbench y los diagramas
```

## Algunos números

- **Modelo conceptual:** 18 entidades y 26 relaciones (5 de 1:1, 18 de 1:N y 3 de N:M), con especialización de
  EMPLEADO en VETERINARIO y AUXILIAR.
- **Modelo físico:** 22 tablas en 3FN, 113 restricciones con nombre, 6 vistas, 2 funciones, 6 disparadores y 1.091
  registros de prueba.
- **Álgebra relacional:** 55 ejercicios. Los probamos en MySQL y dan el mismo resultado que en la Fase 1.

## Modelos editables en Draw.io

La versión vigente de los modelos contiene **28 láminas**: Chen y catálogos completos de atributos, EER,
transformación de multivaluados y relaciones N:M, entidad débil, Barker, Crow's Foot, modelo relacional y normalización.
Las vistas globales se complementan con ampliaciones por área para facilitar la lectura.

- [Abrir o descargar el archivo completo de Draw.io](documentos/fase1/diagramas_drawio/SIGEVET_Modelos_drawio_Completo.drawio).
- [Índice de láminas, notación y reproducción](diagramas/README.md).
- [Documentos de la Fase 1](documentos/fase1/) e [informe final actualizado](documentos/final/Documento_Final_SIGEVET.docx).

Los `.drawio` contienen formas y conexiones editables. Las figuras PNG, SVG y PDF se exportan con el motor de
Draw.io; los modelos anteriores de Workbench permanecen como respaldo. El esquema físico refleja las 22 tablas,
138 columnas y 30 FK del SQL actual, incluida la nulabilidad del director y las claves compuestas.

## Cómo montarlo

En un computador con Linux todo se hace con un solo comando desde la carpeta del proyecto (crea la VM, instala MySQL,
carga la base y deja la app funcionando):

```bash
bash vm/migrar_linux.sh
```

O paso a paso, ya dentro de una VM con Oracle Linux 9:

```bash
cp vm/claves.ejemplo vm/claves.env         # y poner las claves
sudo bash vm/scripts/instalar_mysql.sh     # MySQL 8.4
bash vm/scripts/cargar_bd.sh               # base, usuarios, tablas, datos y verificación
sudo bash vm/scripts/instalar_app.sh       # app web en el puerto 5000
```

Luego se abre `http://127.0.0.1:5000` en el equipo anfitrión, o `http://<ip-de-la-vm>:5000` si la VM está en modo
puente. MySQL Workbench se conecta a `127.0.0.1:3306` (ver `vm/README.md`).
