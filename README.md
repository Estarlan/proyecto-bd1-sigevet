# SIGEVET - Sistema Integral de Gestión Veterinaria

Proyecto final de **Bases de Datos 1**, Universidad El Bosque, Ingeniería de Sistemas.
Docente: Ing., Esp., M.Ed. Christian Felipe Duarte Arévalo.

Hicimos la base de datos de una red de clínicas veterinarias que nos inventamos, *Huella Sana S.A.S.*, con 5 sedes.
Cubre clientes, mascotas con su historia clínica, citas, consultas y fórmulas, inventario de medicamentos por sede y
facturación. La base corre en **Oracle Database Free** sobre una máquina virtual con **Oracle Linux 9**. Encima hay
una aplicación web sencilla en **Python + Flask** que solo lee datos.

| Integrante | Rol |
|---|---|
| Juan David Polo Herrera | Gerente del proyecto y DBA |
| Sebastián Castellanos Parra | Analista y modelador de datos |
| Juan Miguel Ramos | Desarrollador web e infraestructura |

## Qué hay en el repositorio

```text
database/      scripts SQL: creación de objetos, inserción de registros y varios (ver database/README.md)
app/           aplicación web Flask; el SQL está escrito a mano, sin ORM (ver app/README.md)
vm/            scripts de la máquina virtual: instalar Oracle, cargar la base, instalar la app (ver vm/README.md)
documentos/    documentos de la Fase 1, la Fase 2 y los transversales, el modelo de Data Modeler y los diagramas
```

## Algunos números

- **Modelo conceptual:** 18 entidades y 26 relaciones (5 de 1:1, 18 de 1:N y 3 de N:M), con especialización de
  EMPLEADO en VETERINARIO y AUXILIAR.
- **Modelo físico:** 22 tablas en 3FN, 113 restricciones con nombre, 6 vistas, 2 funciones, 6 disparadores y 1.091
  registros de prueba.
- **Álgebra relacional:** 55 ejercicios. Los probamos en Oracle y dan el mismo resultado que en la Fase 1.

## Cómo montarlo

Dentro de una VM con Oracle Linux 9 y desde la carpeta del proyecto (el paso a paso completo está en `vm/README.md`):

```bash
cp vm/claves.ejemplo vm/claves.env         # y poner las claves
sudo bash vm/scripts/instalar_oracle.sh    # Oracle Database Free
bash vm/scripts/cargar_bd.sh               # usuarios, tablas, datos y verificación
sudo bash vm/scripts/instalar_app.sh       # app web en el puerto 5000
```

Luego se abre `http://127.0.0.1:5000` en el equipo anfitrión, o `http://<ip-de-la-vm>:5000` si la VM está en modo
puente.
