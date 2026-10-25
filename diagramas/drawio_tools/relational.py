# Created: 18:55 24-Oct-2026
# Updated: 18:55 24-Oct-2026
# Updated: 18:55 24-Oct-2026
# Updated: 18:55 24-Oct-2026
# Updated: 18:55 24-Oct-2026
# Updated: 18:55 24-Oct-2026
"""Editable logical and physical models with SQL-derived multiplicities."""
from __future__ import annotations

from html import escape

from primitives import Diagram

AREAS = {
    "organizacion": ("DEPARTAMENTO", "ESPECIALIDAD", "CIUDAD", "VETERINARIO_ESPECIALIDAD",
                     "SEDE", "VETERINARIO", "EMPLEADO", "AUXILIAR"),
    "clinica": ("CLIENTE", "TELEFONO_CLIENTE", "MASCOTA", "RAZA", "HISTORIA_CLINICA",
                "ESPECIE", "CITA", "SERVICIO", "CONSULTA"),
    "comercial": ("MEDICAMENTO", "INVENTARIO", "PRESCRIPCION", "FACTURA", "DETALLE_FACTURA"),
}
COLORS = {"organizacion": "#e2efff", "clinica": "#e4f4eb", "comercial": "#fff1dd"}
TITLES = {"barker": "MER lógico · Barker", "crowsfoot": "MER lógico · Crow's Foot",
          "physical": "Modelo relacional · MySQL 8.4"}


def _label(name: str, table: dict, notation: str, external: bool) -> str:
    """Show composite unique groups consistently without conflating them with PKs."""
    heading = f"<b>{escape(name)}</b><hr>"
    if external:
        return heading + "<i>Referencia externa · atributos omitidos</i>"
    foreign = {column for fk in table["fks"] for column in fk["columns"]}
    rows = []
    for column in table["columns"]:
        col = column["name"]
        keys = (["PK"] if col in table["pk"] else []) + (["FK"] if col in foreign else [])
        keys += [f"UQ{i + 1}" for i, group in enumerate(table["unique"]) if col in group]
        marker = "#" if col in table["pk"] else ("o" if column["nullable"] else "*")
        text = f"{marker} {escape(col)}"
        if notation == "physical":
            text += f" : {escape(column['type'])}"
        if keys:
            text += f" <small>[{', '.join(keys)}]</small>"
        rows.append(text)
    return heading + '<div style="text-align:left">' + "<br>".join(rows) + "</div>"


def _multiplicity(table: dict, fk: dict) -> tuple[bool, bool]:
    """Return optional parent and unique referencing row under SQL MATCH SIMPLE."""
    cols = set(fk["columns"])
    optional = any(c["nullable"] for c in table["columns"] if c["name"] in cols)
    unique = any(set(group) <= cols for group in [table["pk"], *table["unique"]] if group)
    return optional, unique


def _split_path(path: list[tuple[float, float]]) -> tuple[list, list, tuple]:
    """Split an orthogonal route at half its distance for Barker's two strokes."""
    segments = [(point, path[index + 1]) for index, point in enumerate(path[:-1])]
    length = sum(abs(b[0] - a[0]) + abs(b[1] - a[1]) for a, b in segments)
    remaining = length / 2
    for i, (a, b) in enumerate(segments):
        distance = abs(b[0] - a[0]) + abs(b[1] - a[1])
        if distance and remaining <= distance:
            ratio = remaining / distance
            mid = (a[0] + (b[0] - a[0]) * ratio, a[1] + (b[1] - a[1]) * ratio)
            return path[1:i + 1], path[i + 1:-1], mid
        remaining -= distance
    raise ValueError("Relationship requires distinct endpoints")


def build_relational(schema: dict, notation: str, area: str | None = None) -> Diagram:
    """Build the complete schema or a readable area with adjoining external stubs."""
    if notation not in TITLES or (area is not None and area not in AREAS):
        raise ValueError(f"Unknown relational diagram: {notation}, {area}")
    primary = set(AREAS[area]) if area else set(schema)
    visible = set(primary)
    for child, table in schema.items():
        for fk in table["fks"]:
            if child in primary or fk["table"] in primary:
                visible.update((child, fk["table"]))
    external = sorted(visible - primary)
    width, height = (2700, 3350) if area else (4040, 3350)
    diagram = Diagram(TITLES[notation] + (f" · {area}" if area else " · completo"), width, height)
    diagram.text("SIGEVET — " + TITLES[notation], 50, 30, width - 100, 45, 26)
    diagram.text("# PK / identificador · * obligatorio · o opcional · FK foránea · UQn grupo único",
                 50, 90, width - 100, 30, 16)
    legend = ("Barker: pata sin barras = muchos; sin pata = uno; mitad continua = obligatoria; "
              "mitad discontinua = opcional.") if notation == "barker" else (
              "Extremos: || = 1 · o| = 0..1 · o< = 0..N. Línea continua: FK incluida en PK; "
              "discontinua: relación no identificadora.")
    diagram.text(legend, 50, 135, width - 100, 45, 16)
    diagram.text("Cardinalidades del DDL: un padre admite 0 hijos; UNIQUE reduce N a 1. "
                 "Las participaciones mínimas del MER conceptual requieren reglas adicionales.",
                 50, 195, width - 100, 45, 16)
    nodes, boxes, ports = {}, {}, {}
    selected_areas = [area] if area else list(AREAS)
    for group_index, group in enumerate(selected_areas):
        base = 80 + (0 if area else group_index * 1300)
        diagram.text(group.upper(), base, 260, 1100, 35, 20)
        for index, name in enumerate(AREAS[group]):
            if name not in schema:
                continue
            x, y = base + (index % 2) * 650, 440 + (index // 2) * 550
            boxes[name] = (x, y, 500, 50 + len(schema[name]["columns"]) * 23)
    if external:
        diagram.text("REFERENCIAS EXTERNAS", 1400, 260, 1100, 35, 20)
    for index, name in enumerate(external):
        boxes[name] = (1400 + index % 2 * 650, 440 + index // 2 * 550, 430, 84)
    for name, (x, y, w, h) in boxes.items():
        group = next(group for group, names in AREAS.items() if name in names)
        rounded = "1" if notation == "barker" else "0"
        color = "#f2f3f5" if name in external else COLORS[group]
        style = (f"rounded={rounded};arcSize=12;fillColor={color};verticalAlign=top;"
                 "spacingTop=12;spacingLeft=14;spacingRight=12;fontSize=16;align=left;")
        nodes[name] = diagram.node(_label(name, schema[name], notation, name in external),
                                   x, y, w, h, style)
        ports[name] = 0
    index = 0
    for child, table in schema.items():
        for fk in table["fks"]:
            parent = fk["table"]
            if child not in nodes or parent not in nodes or not ({child, parent} & primary):
                continue
            sx, sy, sw, sh = boxes[child]
            tx, ty, tw, th = boxes[parent]
            sp, tp = 0.30 + ports[child] % 9 * 0.065, 0.30 + ports[parent] % 9 * 0.065
            ports[child] += 1
            ports[parent] += 1
            lane = 24 + index % 7 * 11
            bus = min(sy, ty) - 60 - index % 8 * 12
            points = [(sx + sw + lane, sy + sh * sp), (sx + sw + lane, bus),
                      (tx + tw + lane, bus), (tx + tw + lane, ty + th * tp)]
            optional, unique = _multiplicity(table, fk)
            label = ", ".join(fk["columns"])
            anchors = f"exitX=1;exitY={sp};entryX=1;entryY={tp};fontSize=11;"
            if notation == "barker":
                path = [(sx + sw, sy + sh * sp), *points, (tx + tw, ty + th * tp)]
                left, right, mid = _split_path(path)
                pivot = diagram.node("", *mid, 0, 0, "opacity=0;strokeOpacity=0;fillOpacity=0;")
                arrow = "none" if unique else "ERmany"
                diagram.edge(nodes[child], pivot, label,
                             f"exitX=1;exitY={sp};entryPerimeter=0;dashed={int(optional)};"
                             f"startArrow={arrow};startSize=16;fontSize=11;", left)
                diagram.edge(pivot, nodes[parent], "",
                             f"exitPerimeter=0;entryX=1;entryY={tp};dashed=1;", right)
            else:
                start = "ERzeroToOne" if unique else "ERzeroToMany"
                end = "ERzeroToOne" if optional else "ERone"
                identifying = set(fk["columns"]) <= set(table["pk"])
                diagram.edge(nodes[child], nodes[parent], label,
                             anchors + f"startArrow={start};endArrow={end};startSize=16;"
                             f"endSize=16;dashed={int(not identifying)};", points)
            index += 1
    diagram.text("UQn agrupa atributos de una misma restricción UNIQUE; PK compuesta comparte #. "
                 "CHECK, disparadores y reglas de negocio se documentan en el DDL y el informe.",
                 50, height - 110, width - 100, 50, 16)
    return diagram
