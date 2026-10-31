# SIGEVET — guion del video por persona

Video de demostración de máximo 15 minutos (este guion dura 13:30). Todos hablan, con cámara y audio. Cada persona
lee solo su parte; el texto está pensado para decirse con sus palabras, no para leerlo de corrido. Los tiempos son
aproximados: ensayen y midan.

| # | Tiempo | Quién | Qué se muestra |
|---|---|---|---|
| 1 | 0:00–1:00 | Los cuatro | Cámaras y portada de la presentación |
| 2 | 1:00–3:00 | Sebastián | Chen, EER, documento de supuestos |
| 3 | 3:00–4:30 | Sebastián | Barker, Crow's Foot, relacional, normalización |
| 4 | 4:30–6:00 | Juan Miguel | Servidor Fedora y VM Oracle Linux 9 |
| 5 | 6:00–8:00 | Sergio | Cargar la base y verificarla |
| 6 | 8:00–10:00 | Sergio | Consultas de álgebra en Workbench |
| 7 | 10:00–12:30 | Juan Miguel | Aplicación web |
| 8 | 12:30–13:30 | Los cuatro | GitHub y cierre |

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

## Bloques 2 y 3 — Sebastián (1:00–4:30)

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

## Bloque 4 — Juan Miguel: servidor (4:30–6:00)

Conéctate al portátil Fedora de Juan David (SSH o en persona). El servidor es un portátil con Fedora que aloja una
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

## Bloques 5 y 6 — Sergio: la base de datos (6:00–10:00)

**Prepara:** una terminal en la raíz del repositorio del servidor, con las claves ya definidas en `vm/claves.env`
(archivo que NO se sube a GitHub; Juan David te lo pasa por un canal privado, hay un modelo en `vm/claves.ejemplo`),
y MySQL Workbench conectado a la base `sigevet`.

**Bloque 5 (2 min) — Carga y verificación**
```bash
bash vm/scripts/cargar_bd.sh --reiniciar
```
**Di:**
> "Este script borra la base y la vuelve a crear desde cero, en orden: primero las tablas, luego los índices, las
> vistas y los programas, y al final los datos. Al terminar verificamos: 22 tablas, 113 restricciones con nombre y
> 1.091 registros. También hay 9 disparadores, que rechazan datos inválidos que un CHECK no alcanza a validar, por
> ejemplo una cita en una fecha imposible."

Muestra la salida de la verificación con esas cifras.

**Bloque 6 (2 min) — Consultas de álgebra en Workbench**
Abre `database/03_varios/01_consultas_algebra_relacional.sql` y ejecuta tres o cuatro de estas:
- **Ejercicio 21:** sedes con su ciudad, departamento y director (reunión de cuatro tablas).
- **Ejercicio 41:** medicamentos que están en las 5 sedes (división). Resultado: 8 medicamentos.
- **Ejercicio 46:** ingresos por sede (agregación).
- **Ejercicio 26:** clientes sin mascotas (reunión externa).

**Di, para cada una:**
> "Esta es la expresión de álgebra relacional y esta es su consulta en SQL. La división no existe en SQL, así que se
> escribe con un doble NOT EXISTS. Son 55 ejercicios y los 55 dan el mismo resultado que en la Fase 1."

---

## Bloque 7 — Juan Miguel: aplicación web (10:00–12:30)

Abre la aplicación en el puerto 5000 del servidor. Recorre en este orden:
1. **Inicio** (`/`).
2. **Clientes** (`/clientes`) y entra a uno.
3. **Historia clínica** de una mascota (`/mascotas/{id}`).
4. **Agenda** (`/agenda`).
5. **Inventario** (`/inventario`).
6. **Facturas** (`/facturas`) y abre una.
7. **Personal** (`/personal`): aquí se ve el polimorfismo (cada tipo de empleado describe su cargo distinto).
8. **Álgebra** (`/algebra`) y abre un ejercicio.

**Di:**
> "La aplicación está hecha en Java 21 con Spring Boot y Thymeleaf. Todo el SQL está escrito a mano con JDBC y
> PreparedStatement; no usamos ORM. Se conecta con el usuario vet_app, que solo puede leer, así que la aplicación no
> puede modificar nada. En la página de personal hay herencia y polimorfismo: Empleado tiene dos subclases,
> Veterinario y Auxiliar, y la misma lista muestra tres descripciones distintas."

---

## Bloque 8 — Cierre en GitHub (12:30–13:30) · los cuatro

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

**Importante:** antes de grabar, cada uno debe tener sus propios commits reales en el repositorio, hechos con su
usuario de GitHub.
