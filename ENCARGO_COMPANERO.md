# SIGEVET — encargo para el equipo (para usar con Claude Code)

Proyecto final de Bases de Datos 1, Universidad El Bosque. Equipo: Juan David Polo Herrera, Sebastián Castellanos
Parra, Juan Miguel Ramos y Sergio. Este archivo reparte lo que falta para cerrar el proyecto. Juan David solo
mantiene y verifica el portátil Fedora que hace de servidor; el resto de pendientes es de los otros tres.

> **Avisos importantes (cambios de último momento):**
> - La lista de pendientes con casillas está en `TODO.md`. Úsala para marcar lo que vayas terminando.
> - Nadie edita nombres de integrantes, portadas ni firmas. Eso se hace todo junto al final, con Claude, para que no
>   haya choques en los Word. Solo manda tu nombre completo a Juan David.
> - La presentación `.pptx` la edita solo Sebastián. Juan Miguel y Sergio le mandan observaciones.
> - Sergio es el cuarto integrante; donde el texto diga "los tres", son los cuatro.

## 1. Cómo empezar

```bash
git clone https://github.com/Estarlan/proyecto-bd1-sigevet.git
cd proyecto-bd1-sigevet
claude
```

- Juan David te agrega como colaborador en GitHub (Settings → Collaborators). Acepta la invitación que llega al correo.
- Cada persona trabaja en su propia rama (`git checkout -b sebastian-pendientes`, `juanmiguel-pendientes`,
  `sergio-pendientes`) y avisa al equipo antes de unirla a `main`.
- Mensajes de commit en español, formato `tipo: descripción` (feat, fix, docs, chore). Sin líneas de coautoría.
- Haz los commits con TU usuario de GitHub. El video muestra al final los commits de cada integrante (bloque 8), así
  que cada uno tiene que tener los suyos reales. No inventes autorías.

## 2. Qué hay en el repositorio

| Carpeta | Contenido |
|---|---|
| `documentos/fase1`, `fase2`, `transversales`, `final` | Los 24 Word entregables y la presentación `.pptx` |
| `database/` | Scripts MySQL 8.4: `01_ddl`, `02_dml`, `03_varios` (álgebra relacional, verificación) |
| `app/` | Aplicación Spring Boot 3.5 / Java 21, sin ORM, JDBC con PreparedStatement |
| `vm/` | Scripts del servidor Linux (`migrar_linux.sh`, `scripts/`) |
| `documentos/fase1/diagramas_drawio/` | Diagramas editables `.drawio` y sus PNG |

Cifras del proyecto (están en los documentos, no las cambies): 22 tablas, 138 columnas, 30 FK, 113 restricciones,
6 vistas, 2 funciones, 3 procedimientos, 9 disparadores, 1.091 registros, 55 ejercicios de álgebra.

## 3. Reglas que no se rompen

- Los `.docx` de `documentos/` son la versión buena. Edítalos directamente (python-docx o su XML) y conserva estilos,
  tablas e imágenes. No existe otra fuente: no los regeneres desde ningún otro lado.
- Después de editar un Word, conviértelo con `pandoc archivo.docx -t plain` para comprobar que no se dañó.
- Si dos personas necesitan el mismo Word, no lo editen a la vez: avísense, o una persona cambia y sube primero.
- Sin ORM en la app. El código debe seguir pareciendo de estudiante: variables explícitas, POO, herencia, polimorfismo.
- Sin fechas de entrega ni de calendario en los documentos.
- Las relaciones del modelo están en infinitivo (PERTENECER, TENER, POSEER...). Los diagramas no llevan relleno.
- Nunca subas claves ni `vm/claves.env` (está en `.gitignore`; el modelo está en `vm/claves.ejemplo`). La clave de la
  base va solo en la variable de entorno `SIGEVET_DB_PASSWORD`. Quien necesite las claves reales las pide a Juan David
  por un canal privado, no por el repositorio ni por el chat del grupo.
- No borres ni reescribas historial de `main` (no force push).

## 4. Reparto

### Sebastián — modelos y diagramas
1. **Diagrama de clases:** ya está hecho: `documentos/fase1/diagramas_drawio/28_Clases.drawio` y su PNG en
   `documentos/fase1/diagramas_drawio/png/28_Clases.png` (sale de las clases Java del paquete `model`). Ábrelo en
   draw.io y revisa que las clases, atributos y flechas sean correctos; si quieres mover algo, hazlo en draw.io y
   reexporta el PNG. Decide con el equipo si se incluye en un Word o en la presentación.
2. **Revisión de documentos:** lee completos `01_Propuesta_del_Proyecto` y `06_Modelo_Relacional` y corrige lo que
   suene raro o genérico. No los alargues más de un 5 % ni quites datos, tablas o imágenes.
3. **Ejercicios 11 y 53** (`database/03_varios/01_consultas_algebra_relacional.sql`): usan la fecha fija
   `2026-10-09`, que es la fecha de corte de los datos de prueba. Con `CURDATE()` el resultado cambiaría cada día y
   dejaría de coincidir con el Word de Fase 1. No los cambies; prepárate para explicarlo. Opcional: una línea en
   `documentos/fase1/08_Algebra_Relacional.docx` que lo diga.
4. **Presentación completa (15 diapositivas):** eres la única persona que edita el `.pptx`. Ábrela en PowerPoint y
   corrige texto cortado, imágenes y orden. Aplica las observaciones de Juan Miguel (diapositivas 10 a 13) y de Sergio
   (diapositiva 11). No cambies nombres de integrantes.
5. **Video:** bloques 2 y 3.

### Juan Miguel — servidor y aplicación
1. **Nombre completo:** mándaselo a Juan David, tal como aparece en tu cédula. No edites las portadas ni las tablas
   de firmas: Juan David y Claude cambian tu nombre y agregan a Sergio en los 24 Word de una sola vez, al final.
2. **Historial de commits:** `documentos/fase2/07_Repositorio_GitHub.docx` tiene una tabla. Compárala con
   `git log --reverse --format='%h | %an | %s'` y agrega las filas que falten (Spring Boot, diagramas nuevos en
   draw.io, documentos más compactos, revisión completa, presentación nueva y los commits de este reparto). La tabla
   cuenta cómo cambió el proyecto, por eso todavía menciona Servlets y Tomcat en commits viejos: es correcto.
3. **Revisión de documentos:** `04_Implementacion_RDBMS`, `02_Identificacion_de_Interesados` y `06_Acta_de_Cierre`.
   Revisa que las menciones de MySQL Workbench sean coherentes: se usa para administrar y consultar la base, y los
   modelos vigentes están en Draw.io (los de Workbench quedan de respaldo).
4. **Presentación, diapositivas 10 a 13** (MySQL, infraestructura, app web, herencia y polimorfismo): revísalas y
   mándale tus observaciones a Sebastián, que es el único que edita el `.pptx`.
5. **Video:** bloque 4 (servidor) y bloque 7 (app web: inicio, clientes, historia clínica, agenda, inventario,
   facturas, álgebra). El servidor es el portátil de Juan David: o él graba el bloque 4, o tú entras por SSH.

### Sergio — base de datos, servidor en los documentos e integración final
1. **Datos del servidor Fedora:** Juan David te pasa el archivo `INFORME_SERVIDOR.md` con sistema operativo,
   recursos, red y estado de MySQL. Con eso:
   - Actualiza `documentos/fase2/01_Analisis_Seleccion_SO.docx` y `02_Implementacion_VM.docx`. El servidor real es
     el portátil de Juan David con **Fedora 44** (Ryzen 3 3250U, 17 GB de RAM, 475 GB de disco), que aloja la VM
     Lima `sigevet` con Oracle Linux 9 y MySQL 8.4. MySQL NO está instalado en Fedora, sino dentro de la VM. Los
     documentos deben decir Fedora como anfitrión y Oracle Linux 9 como sistema de la VM; revisa que no se contradigan.
   - Manda tus observaciones de la diapositiva 11 a Sebastián y ajusta el bloque 4 de `documentos/fase2/08_Guion_Video.docx` (y la sección "Preparación").
2. **Verificación de la base:** `bash vm/scripts/cargar_bd.sh --reiniciar` contra el servidor. Debe dar 22 tablas,
   113 restricciones y 1.091 registros.
3. **Video:** bloque 5 (cargar y verificar la base) y bloque 6 (Workbench: 3 o 4 consultas de álgebra: reunión,
   división, agregación).
4. **Integración final:** cuando los demás terminen, une las ramas a `main` (sin force push), rehace los zips con
   `docs/tools/zips_nuevos.py` (copia `documentos/` a `entregables/` y arma los paquetes; ese script está en la
   carpeta privada `docs/` de Juan David, pídeselo) y entrega `Entrega_Final_SIGEVET.zip`.

### Juan David — solo el servidor
- Verifica que el portátil Fedora funcione como servidor (ver el prompt de la sección 6) y le entrega
  `INFORME_SERVIDOR.md` a Sergio.
- Aparece en el video y firma las actas (ver abajo).

### Los cuatro
- **Firmas:** firmar a mano o con firma digital la columna "Firma" de las tablas de
  `documentos/fase1/02_Documento_de_Supuestos.docx`, `documentos/transversales/01_Acta_de_Constitucion.docx` y
  `documentos/transversales/06_Acta_de_Cierre.docx`. Claude no firma por nadie. Sergio se agrega a la lista de
  integrantes y a las tablas de firmas en la integración final (Juan David con Claude); no lo hagas tú.
- **Video (máximo 15 min):** el guion por persona, con los comandos y el texto de cada bloque, está en
  `GUION_VIDEO.md` (raíz del repositorio). El Word `documentos/fase2/08_Guion_Video.docx` es la versión formal y
  todavía tiene el reparto antiguo. Todos hablan,
  con audio y cámara. Bloque 1 (0:00–1:00, presentación) y bloque 8 (12:30–13:30, GitHub y lecciones aprendidas) los
  hacen los cuatro. Se muestra el desarrollo, la manipulación de la base y el resultado final.

| # | Tiempo | Quién | Qué se muestra |
|---|---|---|---|
| 1 | 0:00–1:00 | Todos | Cámaras y portada; presentación del grupo y del problema |
| 2 | 1:00–3:00 | Sebastián | Chen completo, EER, documento de supuestos |
| 3 | 3:00–4:30 | Sebastián | Barker y Crow's Foot, modelo relacional, diccionario, normalización 3FN |
| 4 | 4:30–6:00 | Juan Miguel | Servidor Linux: sistema operativo, recursos, red, estado de MySQL |
| 5 | 6:00–8:00 | Sergio | Cargar la base y verificarla (22 tablas, 113 restricciones, 1.091 registros) |
| 6 | 8:00–10:00 | Sergio | Workbench: consultas de álgebra |
| 7 | 10:00–12:30 | Juan Miguel | App web |
| 8 | 12:30–13:30 | Todos | GitHub: estructura y commits de cada integrante; lecciones aprendidas |

  El guion del Word todavía tiene el reparto de tres personas: hay que actualizarlo con este (lo hace Sergio junto
  con los datos del servidor). Claude Code puede escribir el texto de cada bloque, preparar el paso a paso de
  comandos y ventanas, y medir tiempos; no puede grabar. El video se sube donde pida el curso y su enlace va en el
  documento que lo pida.
- **Presentación** (`documentos/final/Presentacion_Sustentacion_SIGEVET.pptx`, 15 diapositivas: 1 portada, 2 problema,
  3 alcance, 4 Chen, 5 EER, 6 Barker, 7 Crow's Foot, 8 relacional, 9 normalización, 10 MySQL, 11 infraestructura,
  12 aplicación web, 13 herencia y polimorfismo, 14 álgebra, 15 equipo y cierre): Sergio se agrega en las
  diapositivas 1 y 15 en la integración final (no lo hagas tú). Repartan quién habla en cada una y ensayen.

## 5. Al terminar
- Sube tu rama y avisa al equipo. Sergio une todo a `main` y rehace los zips (se arman desde los Word de `documentos/`,
  por eso los cambios tienen que estar ahí).
- Entrega final: `Entrega_Final_SIGEVET.zip` (documentos, presentación y repositorio).

## 6. Prompt para pegar en Claude Code (cada uno)

```
Lee ENCARGO_COMPANERO.md en la raíz de este repositorio. Yo soy [TU NOMBRE]. Haz solo las tareas que el archivo
asigna a mi nombre, en orden, más lo que dice para "Los cuatro". Antes de editar cualquier Word, dime qué vas a
cambiar. Si necesitas un dato que no tienes (por ejemplo un nombre o una clave), pídemelo; no lo
adivines. Después de cada edición de un .docx compruébalo con pandoc. Trabaja en la rama [tunombre]-pendientes,
haz commits pequeños en español con el formato tipo: descripción y no los subas hasta que yo te diga. Al final
dame un resumen en español sencillo y una lista corta de lo que me toca a mí.
```
