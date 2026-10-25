# Created: 18:55 24-Oct-2026
# Updated: 18:55 24-Oct-2026
"""Editable mxGraph cells shared by the SIGEVET Draw.io builders."""
from __future__ import annotations

import xml.etree.ElementTree as ET
from pathlib import Path

STAMP = "18:55 24-Oct-2026"
BASE = "html=1;whiteSpace=wrap;fontFamily=Arial;fontSize=14;strokeColor=#263238;fillColor=#ffffff;"


class Diagram:
    """A native Draw.io page with stable identifiers and explicit geometry."""

    def __init__(self, name: str, width: int = 2400, height: int = 1800):
        self.name = name
        self.element = ET.Element("diagram", name=name, id=name.split(" ")[0])
        model = ET.SubElement(self.element, "mxGraphModel", grid="1", gridSize="10",
                              page="1", pageWidth=str(width), pageHeight=str(height),
                              guides="1", connect="1", arrows="1", math="0", shadow="0",
                              background="#ffffff")
        self.root = ET.SubElement(model, "root")
        ET.SubElement(self.root, "mxCell", id="0")
        ET.SubElement(self.root, "mxCell", id="1", parent="0")
        self.counter = 0
        self.bounds: dict[str, tuple[float, float, float, float]] = {}

    def next_id(self) -> str:
        self.counter += 1
        return f"c{self.counter}"

    def node(self, label: str, x: float, y: float, w: float, h: float,
             style: str = "") -> str:
        cid = self.next_id()
        cell = ET.SubElement(self.root, "mxCell", id=cid, parent="1", vertex="1",
                             value=label, style=BASE + style)
        ET.SubElement(cell, "mxGeometry", x=str(x), y=str(y), width=str(w),
                      height=str(h), **{"as": "geometry"})
        self.bounds[cid] = (x, y, w, h)
        return cid

    def edge(self, source: str, target: str, label: str = "", style: str = "",
             points: list[tuple[float, float]] | None = None) -> str:
        cid = self.next_id()
        cell = ET.SubElement(self.root, "mxCell", id=cid, parent="1", edge="1",
                             source=source, target=target, value=label,
                             style="html=1;endArrow=none;startArrow=none;fontFamily=Arial;"
                             "fontSize=13;labelBackgroundColor=#ffffff;strokeColor=#52616b;"
                             "rounded=0;jumpStyle=arc;" + style)
        geom = ET.SubElement(cell, "mxGeometry", relative="1", **{"as": "geometry"})
        if points:
            arr = ET.SubElement(geom, "Array", **{"as": "points"})
            for x, y in points:
                ET.SubElement(arr, "mxPoint", x=str(x), y=str(y))
        return cid

    def text(self, label: str, x: float, y: float, w: float = 900,
             h: float = 30, size: int = 16) -> str:
        return self.node(label, x, y, w, h,
                         f"text;strokeColor=none;fillColor=none;align=left;"
                         f"verticalAlign=middle;fontSize={size};")

    def entity(self, name: str, x: float, y: float, weak: bool = False,
               external: bool = False) -> str:
        return self.node(name, x, y, 210, 58,
                         "rounded=0;fontStyle=1;" + ("shape=ext;double=1;" if weak else "")
                         + ("fillColor=#f1f4f5;" if external else ""))

    def relation(self, name: str, x: float, y: float, identifying: bool = False) -> str:
        return self.node(name, x, y, 180, 78,
                         "rhombus;fontSize=12;fillColor=#f2f7fa;"
                         + ("double=1;" if identifying else ""))

    def attribute(self, name: str, x: float, y: float, kind: str = "simple",
                  w: float = 160) -> str:
        label = f"<u>{name}</u>" if kind == "llave" else name
        if kind == "parcial":
            label = f'<span style="text-decoration:underline dashed">{name}</span>'
        style = "ellipse;fontSize=12;"
        if kind == "multivaluado":
            style += "shape=doubleEllipse;"
        if kind == "derivado":
            style += "dashed=1;"
        return self.node(label, x, y, w, 44, style)


def write_drawio(path: Path, diagrams: list[Diagram]) -> None:
    """Write editable XML; retain an append-only lifecycle header."""
    path.parent.mkdir(parents=True, exist_ok=True)
    history = f"<!-- Created: {STAMP} -->\n"
    if path.exists():
        import re
        previous = path.read_text(encoding="utf-8")
        created = re.search(r"<!-- Created: (.*?) -->", previous)
        updates = re.findall(r"<!-- Updated: (.*?) -->", previous)
        history = f"<!-- Created: {created.group(1) if created else '[unknown]'} -->\n"
        history += "".join(f"<!-- Updated: {s} -->\n" for s in (updates + [STAMP])[-20:])
    root = ET.Element("mxfile", host="app.diagrams.net", agent="Codex", version="29.0.0")
    for index, diagram in enumerate(diagrams):
        diagram.element.set("id", f"sigevet-page-{index:02d}")
        root.append(diagram.element)
    path.write_text(history + ET.tostring(root, encoding="unicode") + "\n", encoding="utf-8")
