<!-- Created: [unknown] -->
<!-- Updated: 11:05 02-Nov-2026 -->
<!-- Updated: 12:40 02-Nov-2026 -->
<!-- Updated: 15:43 02-Nov-2026 -->
<!-- Updated: 18:10 02-Nov-2026 -->
<!-- Updated: 19:45 02-Nov-2026 -->
# SIGEVET — guion del video por persona

> **Versión para grabar la voz:** el equipo tiene un guion por persona con los tiempos exactos de cada clip.
> Ahí el bloque 6 se muestra en la aplicación, no en Workbench.

Video de demostración de máximo 15 minutos (dura unos 12:30). Todos hablan, con cámara y audio. Cada persona
lee solo su parte; el texto está pensado para decirse con sus palabras, no para leerlo de corrido. Los tiempos son
aproximados: ensayen y midan.

| # | Tiempo | Quién | Qué se muestra |
|---|---|---|---|
| 1 | 0:00–1:00 | Los cuatro | Cámaras y portada de la presentación |
| 2 | 1:00–2:15 | Sebastián | Chen, entidad débil, EER y supuestos |
| 3 | 2:15–3:10 | Sebastián | Barker, Crow's Foot, relacional, normalización |
| 4 | 3:10–4:40 | Juan David | Servidor Fedora (su portátil) y VM Oracle Linux 9 |
| 5 | 4:40–6:35 | Sergio | Cargar la base y verificarla |
| 6 | 6:35–8:35 | Sergio y Juan David | Consultas de álgebra en la aplicación (Sergio 21 y 41; Juan David 46 y 26) |
| 7 | 8:35–11:20 | Juan Miguel | Aplicación web |
| 8 | 11:20–12:20 | Juan David, Juan Miguel y Sergio | GitHub y cierre |

Antes de grabar: resolución 1920×1080, letra de terminal y editor en 16 pt o más, cámara visible de quien habla,
las ventanas ya abiertas en el orden del guion. Exportar en MP4. Si se pasan de 14 minutos, recorten el bloque 6 a
dos consultas.

---

## Bloque 1 — Apertura (0:00–1:00) · los cuatro

Se muestra la portada de la presentación con las cuatro cámaras. Empieza Juan David y cada uno se presenta en una
frase (nombre y qué hizo).

**Juan David (15 s):**
> "Hola, somos el grupo de SIGEVET, de Bases de Datos 1 en la Universidad El Bosque. Yo soy Juan David Polo, fui el
> gerente del proyecto y el encargado de la base de datos. Trabajamos con una red inventada de clínicas veterinarias,
> Huella Sana, con cinco sedes. El problema era que cada sede llevaba sus propios datos: citas cruzadas, historias
> clínicas incompletas y nadie sabía cuánto inventario tenía cada una. SIGEVET lo resuelve con una sola base de datos
> en MySQL y una aplicación web para consultarla."

**Sebastián (10 s):** "Soy Sebastián Castellanos, analista y modelador de datos. Hice los modelos entidad-relación y
la normalización."

**Juan Miguel (10 s):** "Soy Juan Miguel Ramos. Me encargué del servidor Linux y de la aplicación web."

**Sergio (10 s):** "Soy Sergio Alejandro Parra Rivera. Me encargué de la carga y verificación de la base de datos
y de la integración final del proyecto."

---

## Bloques 2 y 3 — Sebastián (1:00–5:00)

**Prepara abiertas:** `diagramas/drawio/04_Chen_Global.drawio` (o su PNG), `14_EER_Completo`, `15_Barker_Global`,
`19_CrowsFoot_Global`, `23_Relacional_Global`, `27_Normalizacion`, y el Word `02_Documento_de_Supuestos`.

**Bloque 2 (2 min) — Chen, EER y supuestos**
1. Muestra el Chen global. Di:
   > "Este es el modelo conceptual en notación de Peter Chen. Tiene 18 entidades y 26 relaciones: 5 son uno a uno,
   > 18 son uno a muchos y 3 son muchos a muchos. DETALLE_FACTURA es una entidad débil, porque no existe sin su
   > factura."
2. Muestra el EER.
   > "EMPLEADO se especializa en VETERINARIO y AUXILIAR. La especialización es disyunta, un empleado no es las dos
   > cosas a la vez, y parcial, porque los administrativos se quedan solo en EMPLEADO."
3. Abre el documento de supuestos y lee dos o tres supuestos clave (elige los que prefieras del documento).

**Bloque 3 (1:30) — Barker, Crow's Foot, relacional y normalización**
1. Muestra Barker y luego Crow's Foot. Di:
   > "El mismo esquema en otras dos notaciones. En Barker la línea continua es participación obligatoria y la
   > discontinua, opcional. En Crow's Foot los símbolos de cada extremo indican el mínimo y el máximo."
2. Muestra el relacional.
   > "Al pasar al modelo relacional salen 22 tablas y 30 llaves foráneas. Cada relación muchos a muchos se convierte
   > en una tabla, y el teléfono, que es multivaluado, también tiene su propia tabla."
3. Muestra la normalización y el diccionario de datos.
   > "Llegamos hasta la tercera forma normal: los teléfonos pasan a su propia tabla, la factura se separa de sus
   > líneas, y separamos ciudad de departamento y raza de especie."

---

## Bloque 4 — Juan David: servidor (5:00–6:30)

Juan David lo presenta desde su portátil Fedora. El servidor es un portátil con Fedora que aloja una
máquina virtual Lima llamada `sigevet` (Oracle Linux 9), y MySQL corre dentro de la VM, no en Fedora. Los datos
reales están en `INFORME_SERVIDOR.md` (Sergio lo tiene); usa esos valores al hablar. La VM debe estar encendida
antes de grabar.

**Comandos en este orden (uno por uno, dejando ver la salida):**
```bash
cat /etc/fedora-release
nproc
free -h
df -h /
limactl list
limactl shell sigevet cat /etc/oracle-release
limactl shell sigevet systemctl status mysqld --no-pager
```
**Di:**
> "El servidor es un portátil con Fedora, con [N] núcleos, [X] GB de memoria y [Y] GB de disco libre, en la red local
> con la dirección [IP]. Sobre Fedora corre una máquina virtual con Oracle Linux 9, creada con Lima, y dentro de ella
> está MySQL 8.4. Usamos una VM porque así se puede borrar y volver a montar todo con un solo script, sin tocar el
> sistema del portátil. Aquí se ve que la VM está encendida y que MySQL está activo."

---

## Bloques 5 y 6 — Sergio y Juan David: la base de datos (6:30–10:20)

**Prepara:** una terminal SSH al portátil del servidor y, desde ahí, entrar a la VM. `cargar_bd.sh` usa el cliente
`mysql` de la VM, así que se corre **dentro de la VM**, no en Fedora. El proyecto está en `~/proyecto` dentro de la
VM (es la copia que deja `vm/migrar_linux.sh`) y ahí mismo debe estar `vm/claves.env` (archivo que NO se sube a
GitHub; Juan David te lo pasa por un canal privado, hay un modelo en `vm/claves.ejemplo`). Antes de grabar, confirma
que esa copia tiene la última versión de `database/`. El puerto 3306 está abierto en el firewall del Fedora.

**Bloque 5 (2 min) — Carga y verificación**
```bash
limactl shell --workdir / sigevet
cd ~/proyecto
bash vm/scripts/cargar_bd.sh --reiniciar
```
**Di:**
> "Este script borra la base y la vuelve a crear desde cero, en orden: primero las tablas con sus restricciones, luego
> los índices, después las funciones, los procedimientos y los disparadores, y solo entonces los datos. Al final van
> las vistas y la verificación. Verificamos: 22 tablas, 113 restricciones con nombre y 1.091 registros. También hay 9
> disparadores, que rechazan datos que un CHECK no alcanza a validar, por ejemplo un veterinario asignado a una cita
> de una sede donde no trabaja."

Muestra la salida de la verificación. **Ojo:** esa salida no imprime los totales 113 ni 1.091. Muestra las
restricciones por tipo (22 llaves primarias, 25 únicas, 36 CHECK y 30 foráneas, que suman 113) y los registros por
tabla (que suman 1.091), así que di el total de palabra mientras señalas esas tablas. Al final la prueba de reglas
imprime 10 filas, una por cada regla que debe rechazar un dato.

Otros ejemplos reales de disparadores, por si prefieres cambiar el de arriba: una consulta de una cita que no está
ATENDIDA (error 20004) o un empleado que no es de tipo VET registrado como veterinario (error 20001).

**Bloque 6 (2 min) — Consultas de álgebra en la aplicación**
En la página Álgebra de la aplicación se abren estos ejercicios, que se ejecutan en vivo contra MySQL (Sergio el 21 y el 41; Juan David el 46 y el 26):
- **Ejercicio 21:** sedes con su ciudad, departamento y director (reunión de cuatro tablas).
- **Ejercicio 41:** medicamentos que están en las 5 sedes (división). Resultado: 8 medicamentos.
- **Ejercicio 46:** ingresos por sede (agregación).
- **Ejercicio 26:** clientes sin mascotas (reunión externa).

**Di, para cada una:**
> "Esta es la expresión de álgebra relacional y esta es su consulta en SQL. La división no existe en SQL, así que se
> escribe con un doble NOT EXISTS. Son 55 ejercicios y los 55 dan el mismo resultado que en la Fase 1."

Nota: las 55 consultas son idénticas a las de `documentos/fase1/08_Consultas_SQL_Algebra_Relacional.sql` (se comparó
sin comentarios). Que den el mismo resultado lo declara el registro `vm/logs/06_algebra_mysql.log`, que lo compara
contra una base SQLite, y en la aplicación los 55 se ejecutan sin error contra MySQL.

---

## Bloque 7 — Juan Miguel: aplicación web (10:20–13:00)

Abre `http://192.168.2.40:5000` (o `http://127.0.0.1:5000` en el portátil). Recorre en este orden (unos 20 s cada paso):
0. **Inicio de sesión**: primero un documento equivocado (sale el error en rojo) y luego el correo y el documento
   correctos de un empleado activo; arriba aparece su nombre y el botón Salir.
1. **Inicio**: los indicadores de la red.
2. **Clientes**: busca uno y entra a su detalle (teléfonos, mascotas y facturas).
3. **Crear un cliente** con el formulario y guardarlo: aparece en la lista (CRUD).
4. **Dato inválido**: crea otro cliente con **el mismo tipo y número de documento**. La base lo rechaza y la página
   muestra "Ya existe un cliente con ese tipo y número de documento" (restricción UNIQUE de MySQL, no de Java).
5. **Inventario** de una sede: mira las existencias de un medicamento.
6. **Nueva factura** en esa sede con una línea de ese medicamento: al guardarla, vuelve a **Inventario** y muestra que
   las existencias bajaron. Abre la factura creada.
7. **Personal**: el polimorfismo (cada tipo de empleado describe su cargo distinto).
8. **Álgebra**: abre un ejercicio y muestra el resultado en vivo.

**Di:**
> "Solo entra el personal activo: el correo y el documento se comprueban contra la tabla EMPLEADO. La interfaz está hecha en Angular y le pide los datos a una API REST en Java 21 con Spring Boot. Todo el SQL está
> escrito a mano con JDBC y PreparedStatement; no usamos ORM. La aplicación se conecta con el usuario vet_app, que lee
> toda la base y solo puede escribir en las tablas que usa, sin borrar tablas ni cambiar catálogos. Las reglas viven en
> la base: cuando metemos un documento repetido, es MySQL el que lo rechaza y la aplicación muestra la restricción. La
> factura se guarda en una transacción: el encabezado, las líneas y el descuento del inventario se confirman juntos con
> COMMIT, y si algo falla se deshace todo con ROLLBACK. En la página de personal hay herencia y polimorfismo: Empleado
> tiene dos subclases, Veterinario y Auxiliar, y la misma lista muestra descripciones distintas."

---

## Bloque 8 — Cierre en GitHub (13:00–14:00) · los cuatro

Muestra https://github.com/Estarlan/proyecto-bd1-sigevet: las carpetas y la pestaña de commits. Cada uno en 15 s:

**Juan David:**
> "El repositorio tiene cuatro carpetas: documentos, con los entregables; database, con los scripts de la base; app,
> con la aplicación; y vm, con los scripts del servidor. Aquí están mis commits, la base y los diagramas. Lo que más
> aprendí fue que el orden importa: primero se diseña, luego se normaliza y solo después se crean las tablas. Y que
> un script que monta todo desde cero ahorra mucho tiempo cuando algo falla."

**Sebastián:** muestra sus commits y di una lección sobre el modelado (por ejemplo, qué cambió al normalizar).

**Juan Miguel:** muestra sus commits y di una lección sobre el servidor o la aplicación.

**Sergio:** muestra sus commits y di una lección sobre la integración o la carga de datos. Ejemplos: "Aprendí que
un script que carga todo en orden evita errores que a mano pasan sin que uno los vea", o "Aprendí que integrar el trabajo
de cuatro personas exige acordar quién edita cada archivo antes de empezar". Cierra el video:
"Gracias por ver nuestro proyecto."

**Nota:** si alguien no tiene commits propios, muestra en su lugar la parte del repositorio que trabajó (sus ramas,
pull requests o archivos).
