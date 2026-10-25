#!/usr/bin/env python3
# Created: 18:55 24-Oct-2026
"""Build the complete native Draw.io collection from the project's models."""
from __future__ import annotations

import argparse
import copy
import shutil
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "diagramas" / "drawio_tools"))
from conceptual import AREAS, attributes_sheet, build_areas, overview, specialization
from primitives import write_drawio
from relational import build_relational
from schema import load_schema
from transformations import legend, normalization, transform


def build(destination: Path) -> list[Path]:
    """Generate independent pages and a master document with the same content."""
    schema = load_schema(ROOT / "database" / "01_ddl" / "01_crear_tablas.sql")
    if (len(schema), sum(len(t["columns"]) for t in schema.values()),
            sum(len(t["fks"]) for t in schema.values())) != (22, 138, 30):
        raise ValueError("The DDL changed; review diagram coverage before rebuilding")
    areas = build_areas()
    pages = [("00_Leyenda", legend()), ("01_Chen_Organizacion", areas[0]),
             ("02_Chen_Clinica", areas[1]), ("03_Chen_Comercial", areas[2]),
             ("04_Chen_Global", overview("04 Chen Global"))]
    transforms = [
        ("05_Transformacion_Multivaluados_CLIENTE", None, "TELEFONO_CLIENTE"),
        ("06_Transformacion_NM_PRESCRIPCION", "PRESCRIBE", "PRESCRIPCION"),
        ("07_Transformacion_NM_INVENTARIO", "ALMACENA", "INVENTARIO"),
        ("08_Transformacion_NM_ESPECIALIDAD", "CERTIFICADO_EN", "VETERINARIO_ESPECIALIDAD"),
        ("09_Entidad_Debil_DETALLE_FACTURA", None, "DETALLE_FACTURA"),
    ]
    for number, (stem, relation, association) in enumerate(transforms, 5):
        pages.append((stem, transform(schema, number, relation, association)))
    pages.append(("10_EER_Especializacion_EMPLEADO", specialization()))
    for number, (name, entities) in enumerate(AREAS.items(), 11):
        stem = f"{number:02d}_Atributos_" + ("Organizacion" if number == 11 else "Clinica" if number == 12 else "Comercial")
        pages.append((stem, attributes_sheet(f"{number:02d} Atributos {name}", entities)))
    pages.append(("14_EER_Completo", overview("14 EER Completo", eer=True)))
    for start, notation, prefix in [(15, "barker", "Barker"),
                                     (19, "crowsfoot", "CrowsFoot"),
                                     (23, "physical", "Relacional")]:
        for offset, area in enumerate([None, "organizacion", "clinica", "comercial"]):
            stem = f"{start + offset:02d}_{prefix}_" + (area.capitalize() if area else "Global")
            pages.append((stem, build_relational(schema, notation, area)))
    pages.append(("27_Normalizacion", normalization()))
    written = []
    for index, (stem, page) in enumerate(pages, 1):
        path = destination / (stem + ".drawio")
        write_drawio(path, [copy.deepcopy(page)])
        written.append(path)
        print(f"Section {index}/{len(pages)} complete: {path.name}")
    master = destination / "SIGEVET_Modelos_drawio_Completo.drawio"
    write_drawio(master, [p for _, p in pages])
    written.append(master)
    print(f"Generated {len(written)} Draw.io files, 28 master pages; lifecycle headers in every file.")
    return written


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--output", type=Path, default=ROOT / "diagramas" / "drawio")
    parser.add_argument("--copy-to", action="append", type=Path, default=[])
    args = parser.parse_args()
    paths = build(args.output)
    for destination in args.copy_to:
        destination.mkdir(parents=True, exist_ok=True)
        for path in paths:
            shutil.copy2(path, destination / path.name)
        print(f"Synchronized {len(paths)}/{len(paths)} files: {destination}")


if __name__ == "__main__":
    main()
