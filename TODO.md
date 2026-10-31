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

- [ ] Nombre completo de **Juan Miguel**, como aparece en su cédula → Juan David
- [ ] Nombre completo, cédula y rol de **Sergio** → Juan David
- [ ] Archivo del **diagrama de clases** (no está en el repositorio) → de Juan David a Sebastián

## Etapa 1 — Cada uno hace lo suyo (en paralelo)

### Juan David — solo el servidor
- [ ] Autorizar a Antigravity en el portátil: encender la VM (`limactl start sigevet`) y dar la clave de MySQL
- [ ] Aplicar los 3 arreglos del servidor: no suspender al cerrar la tapa, firewall del 3306 solo para la red local,
      arranque automático de la VM
- [ ] Verificar la base en la VM: 22 tablas, 113 restricciones, 6 vistas, 2 funciones, 3 procedimientos,
      9 disparadores, 1.091 registros
- [ ] Mandar `INFORME_SERVIDOR.md` actualizado a Sergio (sin claves)
- [ ] Pasar `vm/claves.env` a Sergio por un canal privado
- [ ] Mandar el diagrama de clases a Sebastián
- [ ] Mandar los nombres de la lista "Datos que faltan"

### Sebastián — modelos y presentación
- [ ] Ordenar las flechas del diagrama de clases en draw.io y guardarlo como `diagramas/drawio/28_Clases.drawio`
      con su PNG en `diagramas/png/`
- [ ] Revisar `01_Propuesta_del_Proyecto.docx`
- [ ] Revisar `06_Modelo_Relacional.docx`
- [ ] Preparar la explicación de los ejercicios 11 y 53 (fecha fija 2026-10-09); opcional: una línea en
      `08_Algebra_Relacional.docx`
- [ ] **Presentación completa** (única persona que la edita): revisar las 15 diapositivas en PowerPoint, corregir texto
      cortado e imágenes, y aplicar las observaciones de Juan Miguel y Sergio
- [ ] Repartir quién habla en cada diapositiva
- [ ] Hacer sus commits con su usuario de GitHub

### Juan Miguel — servidor y aplicación
- [ ] Mandar a Juan David su nombre completo (NO editar portadas ni firmas)
- [ ] Actualizar la tabla de commits en `07_Repositorio_GitHub.docx` (comparar con `git log`)
- [ ] Revisar `04_Implementacion_RDBMS.docx`
- [ ] Revisar `02_Identificacion_de_Interesados.docx`
- [ ] Revisar `06_Acta_de_Cierre.docx`
- [ ] Mandar a Sebastián sus observaciones de las diapositivas 10 a 13
- [ ] Ensayar el bloque 4 (servidor) y el bloque 7 (aplicación web)
- [ ] Hacer sus commits con su usuario de GitHub

### Sergio — base de datos y documentos de servidor
- [ ] Mandar a Juan David su nombre completo, cédula y rol
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
