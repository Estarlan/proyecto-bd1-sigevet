<!-- Created: 18:55 24-Oct-2026 -->
# Verificación de los modelos Draw.io

La entrega contiene 28 láminas editables, disponibles por separado y en un documento de 28 páginas.
Se generaron con objetos mxGraph nativos y se renderizaron con el motor de Draw.io, sin imágenes de diagramas
pegadas dentro de los archivos editables.

| Comprobación | Resultado |
|---|---|
| Entidades conceptuales | 18 |
| Relaciones Chen | 26: cinco 1:1, dieciocho 1:N y tres N:M |
| Atributos conceptuales | 100, más los componentes de atributos compuestos |
| Tablas del SQL MySQL | 22 |
| Columnas del SQL MySQL | 138 |
| Claves foráneas | 30 |
| Hojas individuales y documento maestro | 29 archivos Draw.io |
| Exportaciones nativas | 28 PNG, 28 SVG y 28 PDF |
| Figuras de modelos en fuentes documentales | 22 apariciones, incluida la propuesta |
| Word actualizados | Propuesta, cuatro capítulos de modelos e informe final |

Se comprobaron identificadores únicos, extremos de conexiones válidos, geometrías dentro de página,
subrayados sin doble escape HTML y sincronización exacta de las copias de entrega. Las 52 rutas de Chen global
y las 51 de EER global se calcularon evitando las cajas ajenas; los nombres quedan despejados.

Los modelos lógicos y físicos se contrastaron con el DDL vigente, no con los tipos Oracle históricos.
Se verificaron PK y FK compuestas, restricciones UNIQUE y nulabilidad, en particular el director de SEDE,
HISTORIA_CLINICA, CONSULTA, los subtipos y los dos tipos de ítem de DETALLE_FACTURA.

La revisión detectó y corrigió errores de los modelos anteriores: geometrías `as_` inválidas,
claves doblemente escapadas, la clave equivocada de ESPECIE, atributos omitidos, relaciones sustituidas
por líneas sin rombo y una especialización sin círculo de disyunción.

Las referencias originales SEDE y CUADRILLA se conservaron byte por byte.
El nuevo generador y el verificador funcionan sin las herramientas históricas de Graphviz ni los modelos Oracle.
Ruff y la comprobación de sintaxis JavaScript pasan.

Los Word se renderizaron para revisar imágenes y paginación. Las figuras globales se complementan con las áreas
y catálogos; los PDF y SVG separados permiten ampliarlas. La tabla de contenido de los Word es un campo dinámico
que se actualiza al abrir el documento en Microsoft Word.

Para repetir las comprobaciones:

```bash
python3 diagramas/verificar_drawio.py
```
