# Created: 18:55 24-Oct-2026
"""Chen-to-relational worked transformations and notation legend."""
from __future__ import annotations

from conceptual import RELACIONES, title
from primitives import Diagram


def table(d: Diagram, schema: dict, name: str, x: int, y: int) -> str:
    t = schema[name]
    fk = {c for f in t["fks"] for c in f["columns"]}
    rows = []
    for c in t["columns"]:
        tags = (["PK"] if c["name"] in t["pk"] else []) + (["FK"] if c["name"] in fk else [])
        rows.append(("<b>" if "PK" in tags else "") + c["name"]
                    + ("</b>" if "PK" in tags else "") + (" [" + ", ".join(tags) + "]" if tags else ""))
    return d.node("<b>" + name + "</b><hr>" + "<br>".join(rows), x, y, 350,
                  65 + 24 * len(rows), "align=left;verticalAlign=top;spacing=12;")


def transform(schema: dict, number: int, relation: str | None, association: str) -> Diagram:
    d = Diagram(f"{number:02d} Transformación {association}", 1650, 1150)
    title(d, "SIGEVET · Transformación " + association, "Arriba: modelo conceptual Chen. Abajo: tablas con PK y FK, verificadas contra el DDL MySQL.")
    if relation:
        r = next(r for r in RELACIONES if r.nombre == relation)
        a, b = d.entity(r.a, 100, 200), d.entity(r.b, 1300, 200)
        diamond = d.relation(r.nombre, 720, 190, r.identificadora)
        d.edge(a, diamond, f"({r.card_a}) {'total' if r.total_a else 'parcial'}",
               "shape=link;" if r.total_a else "")
        d.edge(diamond, b, f"({r.card_b}) {'total' if r.total_b else 'parcial'}",
               "shape=link;" if r.total_b else "")
        for i, attr in enumerate(r.atributos):
            aid = d.attribute(attr, 260 + i * 240, 350, w=215)
            d.edge(diamond, aid)
        left, right = table(d, schema, r.a, 80, 570), table(d, schema, r.b, 1210, 570)
        middle = table(d, schema, association, 650, 570)
        d.edge(left, middle, "1 : N", "edgeStyle=orthogonalEdgeStyle;")
        d.edge(middle, right, "N : 1", "edgeStyle=orthogonalEdgeStyle;")
        d.text("PK compuesta de " + association + ": " + ", ".join(schema[association]["pk"])
               + ". Sus columnas también referencian las entidades de origen.", 70, 1010, 1500, 65)
    elif association == "TELEFONO_CLIENTE":
        client = d.entity("CLIENTE", 300, 200)
        phone = d.attribute("telefono", 1040, 200, "multivaluado", 190)
        d.edge(client, phone)
        left = table(d, schema, "CLIENTE", 200, 480)
        right = table(d, schema, association, 1020, 480)
        d.edge(left, right, "1 : N", "edgeStyle=orthogonalEdgeStyle;")
        d.text("PK(id_cliente, telefono). El teléfono deja de ser multivaluado; cada número ocupa una fila.", 80, 1030, 1450, 60)
    else:
        invoice = d.entity("FACTURA", 200, 200)
        detail = d.entity("DETALLE_FACTURA", 1210, 200, True)
        rel = d.relation("CONTIENE", 710, 190, True)
        d.edge(invoice, rel, "(1) total", "shape=link;")
        d.edge(rel, detail, "(N) total", "shape=link;")
        partial = d.attribute("num_linea", 1230, 350, "parcial", 180)
        d.edge(detail, partial)
        left = table(d, schema, "FACTURA", 180, 530)
        right = table(d, schema, association, 1060, 530)
        d.edge(left, right, "FK identificadora", "edgeStyle=orthogonalEdgeStyle;")
        d.text("PK(id_factura, num_linea). num_linea es llave parcial: solo identifica una línea dentro de su factura.", 80, 1030, 1450, 60)
    return d


def legend() -> Diagram:
    d = Diagram("00 Leyenda y lectura", 1550, 1200)
    title(d, "SIGEVET · Notación y organización de los modelos", "Fuentes: supuestos del proyecto, modelo conceptual y DDL MySQL. Las referencias SEDE/CUADRILLA orientan la notación visual.")
    items = [
        ("Entidad fuerte", "Rectángulo simple: identificador propio."),
        ("Entidad débil", "Rectángulo doble: DETALLE_FACTURA depende de FACTURA y num_linea."),
        ("Relación", "Rombo simple; doble en la identificadora CONTIENE."),
        ("llave", "Atributo clave subrayado."),
        ("parcial", "Llave parcial: subrayado discontinuo, no contorno discontinuo."),
        ("multivaluado", "Elipse doble: telefono de CLIENTE, antes de su transformación."),
        ("derivado", "Elipse discontinua: edad, antiguedad, subtotal y total."),
    ]
    for i, (kind, explanation) in enumerate(items):
        y = 140 + i * 100
        if i < 2:
            d.entity(kind.upper(), 75, y, i == 1)
        elif i == 2:
            d.relation("RELACIÓN", 90, y - 10)
        else:
            d.attribute(kind, 90, y, kind, 190)
        d.text(explanation, 370, y, 1070, 60)
    d.text("Chen: (1), (N), (M) expresan la razón de cardinalidad. Línea simple: participación parcial; doble: total.", 75, 890, 1400, 65)
    d.text("EER: (min,max) se lee en la entidad adyacente. Círculo d: especialización disyunta; línea simple desde EMPLEADO: parcial.", 75, 970, 1400, 65)
    d.text("Los modelos lógicos y físicos muestran las 22 tablas. Una tabla asociativa no se clasifica como entidad débil solo por proceder de N:M.", 75, 1050, 1400, 65)
    return d


def normalization() -> Diagram:
    d = Diagram("27 Normalización hasta 3FN", 1800, 1120)
    title(d, "SIGEVET · Normalización de la orden de atención y factura", "El desarrollo de dependencias funcionales se conserva en el capítulo de modelo relacional del informe.")
    stages = [
        ("0FN", "Orden de atención<br>Grupos repetitivos de ítems<br>Varios teléfonos por cliente"),
        ("1FN", "ORDEN_1FN(num_factura, num_linea, ...)<br>Una fila por ítem<br>TELEFONO_1FN(doc_cliente, telefono)"),
        ("2FN", "FACTURA_2FN · DETALLE_2FN · ITEM_2FN<br>Separar dependencias parciales<br>Precio histórico permanece en el detalle"),
        ("3FN", "22 tablas finales del DDL<br>Separar dependencias transitivas:<br>CIUDAD → DEPARTAMENTO · RAZA → ESPECIE"),
    ]
    nodes = []
    for i, (name, body) in enumerate(stages):
        x, y = 100 + (i % 2) * 880, 170 + (i // 2) * 410
        nodes.append(d.node(f"<b>{name}</b><hr>{body}", x, y, 650, 240, "rounded=0;fontSize=20;spacing=20;"))
    d.edge(nodes[0], nodes[1], "Atomicidad")
    d.edge(nodes[1], nodes[2], "Eliminar DF parciales", "edgeStyle=orthogonalEdgeStyle;")
    d.edge(nodes[2], nodes[3], "Eliminar DF transitivas")
    d.text("DETALLE_FACTURA conserva precio_unitario de la venta; SERVICIO y MEDICAMENTO mantienen el precio vigente. No son el mismo atributo.", 100, 960, 1600, 80)
    return d
