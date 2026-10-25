# Created: 22:40 21-Oct-2026
# Updated: 18:55 24-Oct-2026
"""Une los 22 documentos (Fase 1, Fase 2 y transversales) en el documento final del proyecto.

Escribe docs/src/final/00_Documento_Final.md; luego build_docs.py lo convierte en Word.
Uso: .venv/bin/python docs/tools/gen_documento_final.py
"""
import re
import sys
from pathlib import Path

REPO = Path(__file__).resolve().parents[2]
SRC = REPO / "docs" / "src"
DESTINO = SRC / "final" / "00_Documento_Final.md"
PARTES = [
    ("Parte I. Fase 1: análisis y modelado", "fase1"),
    ("Parte II. Fase 2: implementación", "fase2"),
    ("Parte III. Gestión del proyecto", "transversales"),
]
ENCABEZADO = """---
title: "Documento Final del Proyecto"
subtitle: "Documento Final del Proyecto"
fase: "Entrega final"
version: "1.0"
---

# Resumen del proyecto

SIGEVET (Sistema Integral de Gestión Veterinaria) es la base de datos relacional de la Red de Clínicas Veterinarias
Huella Sana S.A.S., un caso simulado de cinco sedes. El proyecto recorre el ciclo completo que pide la asignatura:

- **Análisis y modelado (Fase 1):** propuesta, supuestos y reglas de negocio, modelo entidad-relación en notación
  Peter Chen y extendido, modelos Barker y Crow's Foot y modelo relacional editables en Draw.io, normalización hasta la
  Tercera Forma Normal, diccionario de datos y 55 ejercicios de álgebra relacional.
- **Implementación (Fase 2):** máquina virtual con Oracle Linux 9, MySQL 8.4 LTS administrado con MySQL Workbench,
  scripts de creación, inserción y verificación, y una aplicación web en Java (Servlets y JSP) que consulta la base
  con JDBC, sin ORM, usando un usuario de solo lectura.
- **Gestión:** acta de constitución, interesados, EDT, cronograma de 14 semanas (385 horas), presupuesto de
  11.603.900 COP y acta de cierre.

| Indicador | Resultado |
|---|---|
| Modelo conceptual | 18 entidades y 26 relaciones (5 de 1:1, 18 de 1:N y 3 de N:M) |
| Modelo físico | 22 tablas en 3FN y 113 restricciones con nombre |
| Programación en la base | 6 vistas, 2 funciones, 3 procedimientos y 9 disparadores |
| Datos de prueba | 1.091 registros coherentes entre tablas |
| Reglas de negocio | 10 de 10 pruebas rechazan los datos inválidos con el error esperado |
| Álgebra relacional | 55 de 55 consultas dan en MySQL el mismo resultado que en la Fase 1 |
| Aplicación web | Todas las páginas responden contra la base real; el usuario de la app no puede modificar datos |

Table: Resultados principales

Este documento reúne, en orden, todos los entregables. Cada capítulo conserva el contenido del documento entregado
por separado.
"""
CONCLUSIONES = """
# Conclusiones

1. **El modelo bien hecho se nota en la implementación.** Los supuestos y la normalización hasta 3FN se tradujeron
   directamente en llaves y restricciones con nombre; casi ninguna regla tuvo que vivir en la aplicación.
2. **El álgebra relacional explica el SQL.** Cada consulta de la aplicación y de los 55 ejercicios se lee como
   selecciones, proyecciones, reuniones, diferencias o divisiones; por eso fue posible comprobarlas en otro motor.
3. **Tener todo en scripts permitió cambiar de motor.** El proyecto empezó en Oracle Database Free y se pasó a MySQL
   8.4 sin perder datos ni resultados, porque la base completa se reconstruye con los scripts de `database/`. La
   versión en Oracle quedó como respaldo.
4. **Probar contra datos reales encuentra huecos.** Las pruebas de reglas mostraron, por ejemplo, que una consulta se
   podía mover a una cita cancelada con un UPDATE; se corrigió extendiendo el disparador.
5. **La máquina virtual hace el proyecto portable.** La VM se crea con un solo script (`vm/migrar_linux.sh`), que
   sirve para el MacBook donde se hicieron las pruebas y para el computador servidor con Linux, y queda con MySQL y la
   aplicación arrancando solos.
"""


def sin_encabezado(texto: str) -> tuple:
    if texto.startswith("---"):
        _, yaml, cuerpo = texto.split("---", 2)
        sub = re.search(r'subtitle:\s*"([^"]*)"', yaml)
        return (sub.group(1) if sub else ""), cuerpo
    return "", texto


def expandir(cuerpo: str) -> str:
    salida = []
    for linea in cuerpo.splitlines():
        marca = linea.strip()
        if marca.startswith("<!-- INCLUDE:") and marca.endswith("-->"):
            salida.append((SRC / marca[len("<!-- INCLUDE:"):-3].strip()).read_text(encoding="utf-8"))
        elif re.match(r"^\s*<!--\s*(Created|Updated|Generado)", linea):
            continue
        else:
            salida.append(linea)
    return "\n".join(salida)


def bajar_titulos(cuerpo: str, niveles: int) -> str:
    """Baja cada título de Markdown `niveles` niveles, sin tocar los bloques de código."""
    salida, en_codigo = [], False
    for linea in cuerpo.splitlines():
        if linea.lstrip().startswith("```"):
            en_codigo = not en_codigo
        if not en_codigo and re.match(r"^#{1,4} ", linea):
            linea = "#" * niveles + linea
        salida.append(linea)
    return "\n".join(salida)


def main() -> int:
    partes = [ENCABEZADO]
    total = 0
    for titulo, carpeta in PARTES:
        partes.append(f"\n# {titulo}\n")
        for md in sorted((SRC / carpeta).glob("[0-9]*.md")):
            subtitulo, cuerpo = sin_encabezado(md.read_text(encoding="utf-8"))
            partes.append(f"\n## {subtitulo}\n")
            partes.append(bajar_titulos(expandir(cuerpo), 2))
            total += 1
    partes.append(CONCLUSIONES)
    DESTINO.parent.mkdir(parents=True, exist_ok=True)
    texto = "\n".join(partes) + "\n"
    created = "[unknown]"
    updates = []
    if DESTINO.exists():
        previous = DESTINO.read_text(encoding="utf-8").split("---", 2)[-1]
        header = previous.split("# Resumen", 1)[0]
        match = re.search(r"<!-- Created: (.*?) -->", header)
        created = match.group(1) if match else "[unknown]"
        updates = re.findall(r"<!-- Updated: (.*?) -->", header)
    history = f"<!-- Created: {created} -->\n" + "".join(
        f"<!-- Updated: {value} -->\n" for value in (updates + ["18:55 24-Oct-2026"])[-20:])
    end = texto.index("\n---", 4) + 5
    texto = texto[:end] + "\n" + history + texto[end:]
    DESTINO.write_text(texto, encoding="utf-8")
    print(f"OK {DESTINO.relative_to(REPO)}: {total} documentos unidos")
    return 0


if __name__ == "__main__":
    sys.exit(main())
