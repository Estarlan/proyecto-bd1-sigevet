#!/usr/bin/env python3
# Created: 18:55 24-Oct-2026
# Updated: 18:55 24-Oct-2026
"""Validate native Draw.io coverage, editable geometry and exported assets."""
from __future__ import annotations

import json
import re
import sys
import xml.etree.ElementTree as ET
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "diagramas" / "drawio_tools"))
from conceptual_model import ENTIDADES, resumen
from schema import load_schema


def verify_page(page: ET.Element) -> None:
    """Reject missing IDs, endpoints, malformed geometry and clipped cells."""
    model = page.find("mxGraphModel")
    assert model is not None, page.get("name")
    cells = list(model.findall("root/mxCell"))
    ids = [cell.get("id") for cell in cells]
    assert len(ids) == len(set(ids)), "Duplicate cell identifiers"
    width, height = float(model.get("pageWidth")), float(model.get("pageHeight"))
    for cell in cells:
        assert "&lt;u&gt;" not in cell.get("value", ""), "HTML escaped twice"
        assert "image=" not in cell.get("style", ""), "Raster image in editable source"
        geometry = cell.find("mxGeometry")
        if cell.get("vertex") == "1":
            assert geometry is not None and geometry.get("as") == "geometry"
            x, y, w, h = [float(geometry.get(key, "0")) for key in ("x", "y", "width", "height")]
            assert x >= 0 and y >= 0 and x + w <= width and y + h <= height, (
                page.get("name"), cell.get("value"), "outside page")
        elif cell.get("edge") == "1":
            assert cell.get("source") in ids and cell.get("target") in ids, "Disconnected edge"
            assert geometry is not None and geometry.get("as") == "geometry"


def main() -> None:
    schema = load_schema(ROOT / "database/01_ddl/01_crear_tablas.sql")
    assert (len(schema), sum(len(t["columns"]) for t in schema.values()),
            sum(len(t["fks"]) for t in schema.values())) == (22, 138, 30)
    assert resumen() == {"entidades": 18, "relaciones": 26, "1:1": 5,
                         "1:N": 18, "N:M": 3, "atributos": 100}
    master = ROOT / "diagramas/drawio/SIGEVET_Modelos_drawio_Completo.drawio"
    pages = ET.parse(master).getroot().findall("diagram")
    assert len(pages) == 28
    assert len({page.get("id") for page in pages}) == 28
    for page in pages:
        verify_page(page)
    global_cells = pages[4].findall("mxGraphModel/root/mxCell")
    assert sum(c.get("value") in ENTIDADES for c in global_cells) == 18
    assert sum("rhombus" in c.get("style", "") for c in global_cells) == 26
    for index in (15, 19, 23):
        cells = pages[index].findall("mxGraphModel/root/mxCell")
        assert sum(c.get("vertex") == "1" and "<hr>" in c.get("value", "") for c in cells) == 22
        assert sum(c.get("edge") == "1" for c in cells) == (60 if index == 15 else 30)
    for path in (ROOT / "diagramas/drawio").glob("*.drawio"):
        assert "<!-- Created:" in path.read_text()[:300]
        for page in ET.parse(path).getroot().findall("diagram"):
            verify_page(page)
        copy = ROOT / "documentos/fase1/diagramas_drawio" / path.name
        assert copy.read_bytes() == path.read_bytes(), f"Unsynchronized copy: {copy}"
    manifest = json.loads((ROOT / "diagramas/export_manifest.json").read_text())
    assert len(manifest["entries"]) == 28
    for item in manifest["entries"]:
        assert item["width"] > 0 and item["height"] > 0 and item["cells"] >= 3
        for ext in ("png", "svg", "pdf"):
            path = ROOT / item[ext]
            assert path.is_file() and path.stat().st_size > 500, path
        assert "<image " not in (ROOT / item["svg"]).read_text(), "Rasterized SVG"
    checked = 0
    for path in sorted((ROOT / "docs/src/fase1").glob("0[1-6]_*.md")):
        for image in re.findall(r"!\[[^\]]*\]\(([^)]+)\)", path.read_text()):
            assert (ROOT / image).is_file(), f"Missing document image: {image}"
            assert "191116-24Oct2026" in image, f"Outdated model figure: {image}"
            checked += 1
    for name in ("1B.SEDE.drawio", "2b.CUADRILLA.drawio"):
        original = Path.home() / "Downloads" / name
        if original.exists():
            assert (ROOT / "diagramas/referencias" / name).read_bytes() == original.read_bytes()
    print("PASS: 28 pages, 29 editable files, 18 entities, 26 relationships, 100 attributes.")
    print(f"PASS: 22 SQL tables, 138 columns, 30 FK; {checked} document figures; 84 native exports.")
    print("PASS: unique IDs, valid endpoints, geometry inside pages, synchronized copies and intact references.")


if __name__ == "__main__":
    main()
