# Created: 18:55 24-Oct-2026
# Updated: 18:55 24-Oct-2026
# Adapted from database/tools/modelo_conceptual.py; attributes use the current MySQL DDL.
"""Modelo conceptual (MER) de SIGEVET: entidades, atributos y relaciones con cardinalidad y participación.

Los atributos simples se toman del modelo relacional (sin llaves foráneas: en el MER no existen);
aquí solo se declaran los atributos especiales (compuestos, multivaluados, derivados, llave parcial).
"""
from __future__ import annotations

from dataclasses import dataclass
from pathlib import Path

from schema import load_schema

SCHEMA = load_schema(Path(__file__).resolve().parents[2] / "database/01_ddl/01_crear_tablas.sql")

AREAS = {"Organización": ["DEPARTAMENTO", "CIUDAD", "SEDE", "EMPLEADO", "VETERINARIO", "AUXILIAR", "ESPECIALIDAD"],
         "Clínica": ["CLIENTE", "ESPECIE", "RAZA", "MASCOTA", "HISTORIA_CLINICA", "SERVICIO", "CITA", "CONSULTA"],
         "Comercial": ["MEDICAMENTO", "FACTURA", "DETALLE_FACTURA"]}
ENTIDADES = [e for lista in AREAS.values() for e in lista]
DEBILES = {"DETALLE_FACTURA"}
SUBTIPOS = {"VETERINARIO": "EMPLEADO", "AUXILIAR": "EMPLEADO"}
COMPUESTOS = {  # entidad -> {atributo compuesto: [componentes]}
    "EMPLEADO": {"documento": ["tipo_documento", "num_documento"], "nombre_completo": ["nombres", "apellidos"]},
    "CLIENTE": {"documento": ["tipo_documento", "num_documento"], "nombre_completo": ["nombres", "apellidos"]},
}
MULTIVALUADOS = {"CLIENTE": ["telefono"]}
DERIVADOS = {"MASCOTA": ["edad"], "FACTURA": ["total"], "DETALLE_FACTURA": ["subtotal"], "EMPLEADO": ["antiguedad"]}
LLAVE_PARCIAL = {"DETALLE_FACTURA": "num_linea"}


@dataclass(frozen=True)
class Atributo:
    nombre: str
    tipo: str = "simple"          # simple | llave | parcial | compuesto | multivaluado | derivado
    componentes: tuple[str, ...] = ()


@dataclass(frozen=True)
class Relacion:
    nombre: str
    a: str
    card_a: str      # cardinalidad que se escribe junto a la entidad A: "1", "N" o "M"
    total_a: bool    # participación total de A
    b: str
    card_b: str
    total_b: bool
    atributos: tuple[str, ...] = ()
    identificadora: bool = False
    tabla: str | None = None   # tabla relacional que la implementa (N:M)

    @property
    def tipo(self) -> str:
        cards = {self.card_a, self.card_b}
        if cards == {"1"}:
            return "1:1"
        return "N:M" if "1" not in cards else "1:N"


RELACIONES: list[Relacion] = [
    Relacion("PERTENECE_A", "DEPARTAMENTO", "1", False, "CIUDAD", "N", True),
    Relacion("UBICADA_EN", "CIUDAD", "1", False, "SEDE", "N", True),
    Relacion("RESIDE_EN", "CIUDAD", "1", False, "CLIENTE", "N", True),
    Relacion("LABORA_EN", "SEDE", "1", True, "EMPLEADO", "N", True),
    Relacion("DIRIGE", "EMPLEADO", "1", False, "SEDE", "1", True),
    Relacion("ES_VETERINARIO", "EMPLEADO", "1", False, "VETERINARIO", "1", True),
    Relacion("ES_AUXILIAR", "EMPLEADO", "1", False, "AUXILIAR", "1", True),
    Relacion("CERTIFICADO_EN", "VETERINARIO", "M", False, "ESPECIALIDAD", "N", False,
             ("fecha_certificacion",), tabla="VETERINARIO_ESPECIALIDAD"),
    Relacion("CLASIFICA", "ESPECIE", "1", True, "RAZA", "N", True),
    Relacion("ES_DE_RAZA", "RAZA", "1", False, "MASCOTA", "N", True),
    Relacion("PROPIETARIO_DE", "CLIENTE", "1", False, "MASCOTA", "N", True),
    Relacion("TIENE", "MASCOTA", "1", True, "HISTORIA_CLINICA", "1", True),
    Relacion("SOLICITA", "MASCOTA", "1", False, "CITA", "N", True),
    Relacion("ATIENDE", "VETERINARIO", "1", False, "CITA", "N", True),
    Relacion("SE_PROGRAMA_EN", "SEDE", "1", False, "CITA", "N", True),
    Relacion("CORRESPONDE_A", "SERVICIO", "1", False, "CITA", "N", True),
    Relacion("GENERA", "CITA", "1", False, "CONSULTA", "1", True),
    Relacion("ASISTE", "AUXILIAR", "1", False, "CONSULTA", "N", False),
    Relacion("PRESCRIBE", "CONSULTA", "M", False, "MEDICAMENTO", "N", False,
             ("dosis", "frecuencia_horas", "duracion_dias", "cantidad", "indicaciones"), tabla="PRESCRIPCION"),
    Relacion("ALMACENA", "SEDE", "M", False, "MEDICAMENTO", "N", False,
             ("stock_actual", "stock_minimo", "fecha_actualizacion"), tabla="INVENTARIO"),
    Relacion("FACTURADA_A", "CLIENTE", "1", False, "FACTURA", "N", True),
    Relacion("EMITIDA_EN", "SEDE", "1", False, "FACTURA", "N", True),
    Relacion("EMITE", "EMPLEADO", "1", False, "FACTURA", "N", True),
    Relacion("CONTIENE", "FACTURA", "1", True, "DETALLE_FACTURA", "N", True, identificadora=True),
    Relacion("INCLUYE_SERVICIO", "SERVICIO", "1", False, "DETALLE_FACTURA", "N", False),
    Relacion("INCLUYE_MEDICAMENTO", "MEDICAMENTO", "1", False, "DETALLE_FACTURA", "N", False),
]


def atributos(entidad: str) -> list[Atributo]:
    """Atributos conceptuales de una entidad (sin llaves foráneas), con su clasificación."""
    tabla = SCHEMA[entidad]
    compuestos = COMPUESTOS.get(entidad, {})
    componentes = {c for lista in compuestos.values() for c in lista}
    resultado, ya_compuestos = [], set()
    for col in tabla["columns"]:
        es_fk = any(col["name"] in f["columns"] for f in tabla["fks"])
        if es_fk and not (entidad in SUBTIPOS and col["name"] in tabla["pk"]):
            continue
        if col["name"] in componentes:
            padre = next(n for n, lista in compuestos.items() if col["name"] in lista)
            if padre not in ya_compuestos:
                ya_compuestos.add(padre)
                resultado.append(Atributo(padre, "compuesto", tuple(compuestos[padre])))
            continue
        if LLAVE_PARCIAL.get(entidad) == col["name"]:
            resultado.append(Atributo(col["name"], "parcial"))
        elif col["name"] in tabla["pk"]:
            resultado.append(Atributo(col["name"], "llave"))
        else:
            resultado.append(Atributo(col["name"]))
    resultado += [Atributo(m, "multivaluado") for m in MULTIVALUADOS.get(entidad, [])]
    resultado += [Atributo(d, "derivado") for d in DERIVADOS.get(entidad, [])]
    return resultado


def area_de(entidad: str) -> str:
    return next(a for a, lista in AREAS.items() if entidad in lista)


def resumen() -> dict[str, int]:
    tipos = [r.tipo for r in RELACIONES]
    return {"entidades": len(ENTIDADES), "relaciones": len(RELACIONES), "1:1": tipos.count("1:1"),
            "1:N": tipos.count("1:N"), "N:M": tipos.count("N:M"),
            "atributos": sum(len(atributos(e)) for e in ENTIDADES)}


if __name__ == "__main__":
    print(resumen())
