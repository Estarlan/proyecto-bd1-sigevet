<!-- Created: 18:55 24-Oct-2026 -->
<!-- Updated: 18:55 24-Oct-2026 -->
# Modelos de SIGEVET en Draw.io

La fuente editable vigente es [SIGEVET_Modelos_drawio_Completo.drawio](drawio/SIGEVET_Modelos_drawio_Completo.drawio).
Puede abrirse desde **Archivo → Abrir desde → Dispositivo** en [Draw.io](https://app.diagrams.net/).
Cada entidad, atributo, relación y conector es un objeto editable, no una imagen pegada.

| Hojas | Contenido |
|---|---|
| 00 | Leyenda y reglas de lectura |
| 01–03 | Chen por área; cada fila amplía una relación |
| 04 | Estructura global Chen: 18 entidades y 26 relaciones |
| 05 | Transformación del teléfono multivaluado de CLIENTE |
| 06–08 | Transformaciones N:M: PRESCRIPCION, INVENTARIO y ESPECIALIDAD |
| 09 | Entidad débil DETALLE_FACTURA, identificación y llave parcial |
| 10 | Especialización disyunta y parcial de EMPLEADO |
| 11–13 | Los 100 atributos conceptuales, con compuestos y sus componentes |
| 14 | EER global, cardinalidades min/max, especialización y exclusión |
| 15–18 | Barker global y tres áreas |
| 19–22 | Crow's Foot global y tres áreas |
| 23–26 | Relacional MySQL global y tres áreas, con tipos, PK, FK y UNIQUE |
| 27 | Normalización de 0FN a 3FN |

También hay un `.drawio` independiente por hoja en [drawio/](drawio/), con copias para la entrega
en [documentos/fase1/diagramas_drawio/](../documentos/fase1/diagramas_drawio/).
Las exportaciones vigentes terminan en `_191116-24Oct2026` y están en [png/](png/), [svg/](svg/) y [pdf/](pdf/).
Los PDF y SVG conservan el dibujo vectorial; conviene ampliarlos para revisar los modelos globales.

## Reglas del modelo

El conceptual conserva las 18 entidades y 26 relaciones del proyecto: 5 de 1:1, 18 de 1:N y 3 de N:M.
El físico proviene de `database/01_ddl/01_crear_tablas.sql`: 22 tablas, 138 columnas y 30 FK.
Las cuatro tablas adicionales proceden de los teléfonos y de las tres relaciones N:M.

Una FK NOT NULL exige un padre; una FK nullable permite omitirlo. La inversa admite cero hijos y su máximo
es uno cuando hay UNIQUE, o muchos en otro caso. Estas restricciones físicas no imponen todas las participaciones
mínimas del negocio: por ejemplo, `SEDE.id_director` admite nulos durante la carga. El modelo conceptual mantiene
la regla de director obligatorio. El CHECK de DETALLE_FACTURA exige servicio o medicamento, de forma excluyente.

En Barker, las dos mitades de una relación indican su obligatoriedad por separado. En Crow's Foot, la línea
continua indica una FK que pertenece a la PK; el círculo y las barras indican mínimos y máximos.
Las tablas asociativas no se clasifican automáticamente como entidades débiles. En Chen solo DETALLE_FACTURA
se representa como débil; `num_linea` es su llave parcial y CONTIENE su relación identificadora.

Las referencias originales `1B.SEDE.drawio` y `2b.CUADRILLA.drawio` se conservan sin cambios en `referencias/`.
Se tomó su notación visual, corrigiendo las inconsistencias de identificación y las conexiones incompletas.

## Regenerar

Desde la raíz del repositorio, con Python 3.10 o posterior:

```bash
python3 diagramas/reconstruir_drawio.py --copy-to documentos/fase1/diagramas_drawio
```

El generador lee el SQL y el catálogo conceptual en `drawio_tools/`, verifica las cantidades esperadas y crea
29 archivos: 28 hojas individuales y el documento de 28 páginas. No requiere Graphviz ni los generadores antiguos.
`generar_drawio_sigevet.py` y los `.dot` se conservan como antecedentes; el comando anterior es el vigente.

Para exportar se necesita Node.js, Playwright con Chromium y acceso a `app.diagrams.net`:

```bash
npm install --no-save playwright
npx playwright install chromium
node diagramas/exportar_drawio.mjs
```

Se pueden configurar `PLAYWRIGHT_MODULE`, `DRAWIO_CHROMIUM_PATH` y `DRAWIO_STAMP`.
`export_manifest.json` registra las dimensiones, las celdas renderizadas y las rutas de cada exportación.
Los capítulos 03–06 de `docs/src/fase1/` y el documento final utilizan estas figuras.
