# Created: 18:55 24-Oct-2026
# Updated: 18:55 24-Oct-2026
# Updated: 18:55 24-Oct-2026
# Updated: 18:55 24-Oct-2026
# Updated: 18:55 24-Oct-2026
# Updated: 18:55 24-Oct-2026
# Updated: 18:55 24-Oct-2026
# Updated: 18:55 24-Oct-2026
"""Chen and EER views with explicit cardinality and uncluttered detail sheets."""
from __future__ import annotations

import math

from conceptual_model import AREAS, ENTIDADES, RELACIONES, atributos
from primitives import Diagram


def title(d: Diagram, text: str, subtitle: str) -> None:
    width = int(d.element.find("mxGraphModel").get("pageWidth")) - 90
    d.text(text, 45, 20, min(2200, width), 42, 24)
    d.text(subtitle, 45, 65, min(2250, width), 55, 14)


def participation(d: Diagram, source: str, target: str, card: str, total: bool,
                  other_card: str, eer: bool = False, style: str = "") -> None:
    label = f"({1 if total else 0},{'N' if other_card != '1' else '1'})" if eer else f"({card}) {'total' if total else 'parcial'}"
    d.edge(source, target, label, ("shape=link;" if total and not eer else "") + style)


def relation_sheet(name: str, relations: list, eer: bool = False) -> Diagram:
    """Use aligned lanes so relationship details never cross unrelated nodes."""
    d = Diagram(name, 1550, 160 + len(relations) * 210)
    title(d, "SIGEVET · " + name, "Cada fila amplía una relación. Las entidades repetidas son referencias a la misma entidad del modelo global.")
    for index, r in enumerate(relations):
        y = 150 + index * 210
        a = d.entity(r.a, 80, y + 45, r.a == "DETALLE_FACTURA")
        b = d.entity(r.b, 1160, y + 45, r.b == "DETALLE_FACTURA")
        rel = d.relation(r.nombre, 630, y + 35, r.identificadora)
        participation(d, a, rel, r.card_a, r.total_a, r.card_b, eer)
        participation(d, rel, b, r.card_b, r.total_b, r.card_a, eer)
        for entity, cid, x in ((r.a, a, 100), (r.b, b, 1180)):
            keys = [a for a in atributos(entity) if a.tipo in {"llave", "parcial"}]
            for key in keys:
                attr = d.attribute(key.nombre, x, y - 15, key.tipo, 170)
                d.edge(attr, cid)
        for j, attr in enumerate(r.atributos):
            x = 330 + j * 215
            aid = d.attribute(attr, x, y + 138, w=195)
            d.edge(rel, aid)
    return d


def attributes_sheet(name: str, entities: list[str]) -> Diagram:
    d = Diagram(name, 2360, 140 + math.ceil(len(entities) / 2) * 850)
    title(d, "SIGEVET · " + name, "Atributos conceptuales completos. No se muestran FK; las asociaciones están en las vistas Chen y en el modelo global.")
    for index, entity in enumerate(entities):
        ox, oy = 60 + (index % 2) * 1160, 140 + (index // 2) * 850
        center = d.entity(entity, ox + 420, oy + 240, entity == "DETALLE_FACTURA")
        attrs = [a for a in atributos(entity) if not a.componentes]
        for j, attr in enumerate(attrs):
            angle = 2 * math.pi * j / len(attrs) - math.pi / 2
            x = ox + 445 + 360 * math.cos(angle)
            y = oy + 245 + 225 * math.sin(angle)
            aid = d.attribute(attr.nombre, round(x), round(y), attr.tipo, 185)
            d.edge(center, aid)
        compounds = [a for a in atributos(entity) if a.componentes]
        for j, attr in enumerate(compounds):
            x, y = ox + 170 + j * 570, oy + 560
            aid = d.attribute(attr.nombre, x, y, attr.tipo, 185)
            d.edge(center, aid)
            for k, component in enumerate(attr.componentes):
                comp = d.attribute(component, x + (k - .5) * 195, y + 90, w=180)
                d.edge(aid, comp)
        d.text(entity, ox + 330, oy + 740, 480, 30, 15)
    return d


def overview(name: str, eer: bool = False) -> Diagram:
    """Route every global relationship around entities and unrelated diamonds."""
    from routing import Router, pairwise

    d = Diagram(name, 4480, 3340)
    title(d, "SIGEVET · " + name, "18 entidades · " + ("24 relaciones y especialización disyunta parcial" if eer else "26 relaciones: 5 de 1:1, 18 de 1:N y 3 de N:M") + " · Atributos completos en las hojas 11–13.")
    positions = {
        "DEPARTAMENTO": (100, 170), "CIUDAD": (770, 170), "CLIENTE": (1500, 170),
        "SEDE": (100, 670), "EMPLEADO": (770, 670), "FACTURA": (2200, 170),
        "ESPECIALIDAD": (100, 1180), "VETERINARIO": (770, 1180), "MASCOTA": (1500, 670),
        "AUXILIAR": (100, 1740), "CONSULTA": (1500, 1740), "CITA": (1500, 1180),
        "ESPECIE": (2200, 670), "RAZA": (2640, 670), "HISTORIA_CLINICA": (2200, 1180),
        "SERVICIO": (2200, 1740), "MEDICAMENTO": (770, 1740), "DETALLE_FACTURA": (2640, 170),
    }
    positions = {key: (x * 1.5, y * 1.5) for key, (x, y) in positions.items()}
    ids = {e: d.entity(e, *positions[e], e == "DETALLE_FACTURA") for e in ENTIDADES}
    # Separate both SEDE–EMPLEADO relationships and the long cross-area routes.
    overrides = {
        "DIRIGE": (435, 500), "LABORA_EN": (435, 685), "UBICADA_EN": (100, 410),
        "ES_AUXILIAR": (435, 920), "ES_VETERINARIO": (785, 920),
        "RESIDE_EN": (1110, 180), "SE_PROGRAMA_EN": (435, 1000),
        "EMITIDA_EN": (1090, 340), "EMITE": (1710, 430),
        "ASISTE": (1090, 2010), "ALMACENA": (435, 1450),
        "INCLUYE_MEDICAMENTO": (2040, 2020), "INCLUYE_SERVICIO": (2640, 1450),
        "PRESCRIBE": (1110, 1750), "FACTURADA_A": (1850, 180),
        "CERTIFICADO_EN": (435, 1190), "ATIENDE": (1110, 1190),
        "TIENE": (1850, 1000), "CLASIFICA": (2440, 680),
        "ES_DE_RAZA": (2470, 940), "CONTIENE": (2440, 180),
    }
    pending = []
    for r in RELACIONES:
        if eer and r.nombre in {"ES_VETERINARIO", "ES_AUXILIAR"}:
            continue
        ax, ay = positions[r.a]
        bx, by = positions[r.b]
        default = ((ax + bx) / 2 + 15, (ay + by) / 2 + 20)
        rx, ry = tuple(value * 1.5 for value in overrides[r.nombre]) if r.nombre in overrides else default
        rel = d.relation(r.nombre, rx, ry, r.identificadora)
        for source, target, card, total, other_card in (
                (ids[r.a], rel, r.card_a, r.total_a, r.card_b),
                (rel, ids[r.b], r.card_b, r.total_b, r.card_a)):
            label = f"({1 if total else 0},{'N' if other_card != '1' else '1'})" if eer else f"({card}) {'total' if total else 'parcial'}"
            pending.append((source, target, label, total and not eer))
    if eer:
        circle = d.node("d", 1260, 1425, 60, 60, "ellipse;fontStyle=1;fontSize=22;")
        pending += [(ids["EMPLEADO"], circle, "parcial", False),
                    (circle, ids["VETERINARIO"], "⊂", False),
                    (circle, ids["AUXILIAR"], "⊂", False)]
    shape_ids = {cell.get("id") for cell in d.root if cell.get("vertex") == "1"
                 and "text;" not in cell.get("style", "")}
    router = Router({key: box for key, box in d.bounds.items() if key in shape_ids}, 4480, 3340)
    d.routed_paths = []
    for source, target, label, total in pending:
        points, start, finish = router.route(source, target)
        style = (f"exitX={start[0]};exitY={start[1]};entryX={finish[0]};entryY={finish[1]};"
                 "exitPerimeter=0;entryPerimeter=0;" + ("shape=link;" if total else ""))
        cid = d.edge(source, target, label, style, points[1:-1])
        geometry = next(cell for cell in d.root if cell.get("id") == cid).find("mxGeometry")
        # Center labels on the longest segment, away from all endpoint boxes.
        lengths = [abs(b[0] - a[0]) + abs(b[1] - a[1]) for a, b in pairwise(points)]
        longest = max(range(len(lengths)), key=lengths.__getitem__)
        geometry.set("x", str(2 * (sum(lengths[:longest]) + lengths[longest] / 2) / sum(lengths) - 1))
        geometry.set("y", "-12")
        d.routed_paths.append((source, target, points))
    edges = [cell for cell in d.root if cell.get("edge") == "1"]
    for edge in edges:
        d.root.remove(edge)
    for index, edge in enumerate(edges, 2):
        d.root.insert(index, edge)
    d.text("DETALLE_FACTURA: exactamente un SERVICIO o un MEDICAMENTO {XOR}. Clave: factura + num_linea.", 80, 3260, 4200, 40)
    return d


def specialization() -> Diagram:
    d = Diagram("10 EER Especialización EMPLEADO", 1500, 850)
    title(d, "SIGEVET · Especialización de EMPLEADO", "Disyunta y parcial · opción 8A: una tabla por supertipo y por subtipo.")
    emp = d.entity("EMPLEADO", 645, 180)
    discriminator = d.attribute("tipo_empleado", 1000, 180, w=190)
    d.edge(emp, discriminator)
    circle = d.node("d", 720, 340, 60, 60, "ellipse;fontSize=24;fontStyle=1;")
    d.edge(emp, circle, "parcial")
    vet = d.entity("VETERINARIO", 260, 530)
    aux = d.entity("AUXILIAR", 1030, 530)
    d.edge(circle, vet, "⊂")
    d.edge(circle, aux, "⊂")
    d.text("VET: tarjeta_profesional, universidad, anio_graduacion", 120, 640, 620, 60)
    d.text("AUX: area, certificacion, turno", 870, 640, 560, 60)
    d.text("d: un empleado no pertenece a ambos subtipos. ADM permanece solo en EMPLEADO. PK del subtipo = FK a EMPLEADO.", 100, 750, 1300, 60)
    return d


def build_areas() -> list[Diagram]:
    groups = [set(AREAS["Organización"]), set(AREAS["Clínica"]), set(AREAS["Comercial"])]
    assigned: set[str] = set()
    result = []
    for i, group in enumerate(groups):
        # Commercial relationships belong to Commercial even when one endpoint is clinical.
        rels = [r for r in RELACIONES if (r.a in group or r.b in group)
                and (i == 2 or not ({r.a, r.b} & groups[2]))
                and (i != 0 or not ({r.a, r.b} & groups[1])) and r.nombre not in assigned]
        assigned.update(r.nombre for r in rels)
        result.append(relation_sheet(f"0{i+1} Chen {list(AREAS)[i]}", rels))
    if len(assigned) != 26:
        raise ValueError(f"Missing relationships: { {r.nombre for r in RELACIONES} - assigned}")
    return result
