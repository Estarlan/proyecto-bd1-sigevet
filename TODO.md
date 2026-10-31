# SIGEVET — lista de pendientes (TO-DO)

Marca `[x]` cuando algo esté hecho. Detalle de cada tarea en `ENCARGO_COMPANERO.md`; textos del video en
`GUION_VIDEO.md`. Hay cuatro etapas, en este orden.

## Reglas para no pisarse (leer primero)

- **Antes de empezar cada día:** `git pull origin main`. Cada uno trabaja en su rama y avisa antes de unirla.
- **Un Word o la presentación los edita una sola persona.** Los `.docx` y el `.pptx` no se pueden combinar si dos
  personas los cambian a la vez.
- **Nadie cambia nombres ni portadas.** Los nombres de los integrantes (portada de los 24 Word, firmas, diapositivas 1
  y 15, README) se cambian todos juntos en la etapa 3, con Claude, para que no haya choques. Solo manden los datos
  (ver "Datos que faltan").
- **La presentación (`.pptx`) la edita solo Sebastián.** Juan Miguel y Sergio le mandan sus observaciones.
- Nunca subir claves ni `vm/claves.env`. Nunca force push.

## Datos que faltan (necesarios para la etapa 3)

- [x] **Juan Miguel:** Juan Miguel Ramos Sevilla, C.C. 1025528586
- [x] **Sergio:** Sergio Alejandro Parra Rivera, C.C. 1031653350 (rol: carga y verificación de la base e integración final)
- [x] **Diagrama de clases:** hecho por Claude (`documentos/fase1/diagramas_drawio/28_Clases.drawio` y PNG); Sebastián lo revisa

## Etapa 1 — Cada uno hace lo suyo (en paralelo)

### Juan David — solo el servidor
- [ ] En el portátil Fedora, con el repositorio clonado y `vm/claves.env` puesto (modelo en `vm/claves.ejemplo`),
      correr `bash vm/scripts/preparar_servidor_fedora.sh --aplicar`. Revisa el equipo, enciende la VM, aplica los
      arreglos (pide sudo y confirma cada uno) y escribe `~/INFORME_SERVIDOR.md` sin claves
- [ ] Mandar `INFORME_SERVIDOR.md` y `vm/claves.env` a Sergio por un canal privado
- [ ] Ya no hace falta la regla de firewall del 3306: la VM publica ese puerto solo en 127.0.0.1

### Sebastián — modelos y presentación
- [ ] Revisar el diagrama de clases `documentos/fase1/diagramas_drawio/28_Clases.drawio` (ya hecho; arreglar lo que haga falta)
- [ ] Revisar `01_Propuesta_del_Proyecto.docx`
- [ ] Revisar `06_Modelo_Relacional.docx`
- [ ] Preparar la explicación de los ejercicios 11 y 53 (fecha fija 2026-10-09); opcional: una línea en
      `08_Algebra_Relacional.docx`
- [ ] **Presentación completa** (única persona que la edita): revisar las 15 diapositivas en PowerPoint, corregir texto
      cortado e imágenes, y aplicar las observaciones de Juan Miguel y Sergio
- [ ] Repartir quién habla en cada diapositiva
- [ ] Hacer sus commits con su usuario de GitHub

### Juan Miguel — servidor y aplicación
- [ ] Actualizar la tabla de commits en `07_Repositorio_GitHub.docx` (comparar con `git log`)
- [ ] Revisar `04_Implementacion_RDBMS.docx`
- [ ] Revisar `02_Identificacion_de_Interesados.docx`
- [ ] Revisar `06_Acta_de_Cierre.docx`
- [ ] Mandar a Sebastián sus observaciones de las diapositivas 10 a 13
- [ ] Ensayar el bloque 4 (servidor) y el bloque 7 (aplicación web)
- [ ] Hacer sus commits con su usuario de GitHub

### Sergio — base de datos y documentos de servidor
- [ ] Recibir `INFORME_SERVIDOR.md` y `vm/claves.env`
- [ ] Actualizar `documentos/fase2/01_Analisis_Seleccion_SO.docx` y `02_Implementacion_VM.docx` con los datos
      reales (Fedora 44 como anfitrión, VM Lima con Oracle Linux 9 y MySQL 8.4 dentro de la VM)
- [ ] Actualizar `documentos/fase2/08_Guion_Video.docx` con el reparto de cuatro personas (el vigente está en
      `GUION_VIDEO.md`)
- [ ] Correr `bash vm/scripts/cargar_bd.sh --reiniciar` y confirmar las cifras
- [ ] Mandar a Sebastián sus observaciones de la diapositiva 11
- [ ] Ensayar el bloque 5 y el bloque 6
- [ ] Hacer sus commits con su usuario de GitHub

## Etapa 2 — Unir el trabajo

- [ ] Cada uno sube su rama y avisa
- [ ] Sergio une todas las ramas a `main` (sin force push) y revisa que no se hayan roto los Word
      (`pandoc archivo.docx -t plain`)

## Etapa 3 — Integración final (Juan David con Claude)

- [ ] Cambiar el nombre de Juan Miguel y agregar a Sergio en la portada de los 24 Word, con script
- [ ] Agregar a Sergio en las tablas de firmas (Supuestos, Acta de Constitución, Acta de Cierre)
- [ ] Agregar a Sergio en las diapositivas 1 y 15 y en el README
- [ ] Revisar de nuevo cifras, enlaces y que no queden fechas de calendario
- [ ] Rehacer los zips (`docs/tools/zips_nuevos.py`) y comprobar su contenido
- [ ] Subir los cambios a `main`

## Etapa 4 — Video y entrega

- [ ] Ensayar el video completo con cronómetro (13:30; máximo 15 min)
- [ ] Grabar los 8 bloques (los cuatro hablan, con cámara y audio)
- [ ] Subir el video y poner el enlace donde lo pida el curso
- [ ] Firmar las tres actas (los cuatro): Supuestos, Constitución y Cierre
- [ ] Entregar `Entrega_Final_SIGEVET.zip`

## Para preguntar y verificar (Juan David)

- [ ] Subir los commits a GitHub: `git push origin HEAD:main` desde la carpeta del proyecto
- [ ] Responder a Sergio las preguntas de su Claude (la VM es Lima con Oracle Linux 9 sobre Fedora; los datos del servidor)
- [ ] En el portátil: correr el script `preparar_servidor_fedora.sh --aplicar` (ver arriba)
- [ ] Comprobar que los compañeros ya hicieron `git pull` y trabajan en su propia rama
- [ ] Revisar que nadie haya editado nombres, portadas ni firmas antes de la integración final
