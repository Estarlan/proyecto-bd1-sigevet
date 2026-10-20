import re
from pathlib import Path


def cargar_ejercicios():
    archivo = Path(__file__).resolve().parent.parent / "database/03_varios/01_consultas_algebra_relacional.sql"
    texto = archivo.read_text(encoding="utf-8")
    partes = re.split(r"^--\s*Ejercicio\s+(\d+)\.\s*(.+)$", texto, flags=re.MULTILINE)
    ejercicios = []
    grupo = "General"

    for posicion in range(1, len(partes), 3):
        grupos = re.findall(r"^-- =+\n-- (.+)\n-- =+", partes[posicion - 1], re.MULTILINE)
        if grupos:
            grupo = grupos[-1].strip()
        numero = int(partes[posicion])
        titulo = partes[posicion + 1].strip()
        lineas = partes[posicion + 2].splitlines()
        enunciado = []
        for linea in lineas:
            if linea.lstrip().upper().startswith("SELECT"):
                break
            if linea.lstrip().startswith("--"):
                enunciado.append(linea.lstrip()[2:].strip())
        sql = "\n".join(linea for linea in lineas if not linea.lstrip().startswith("--")).strip()
        if not sql.endswith(";"):
            raise ValueError("Falta el punto y coma del ejercicio " + str(numero))
        sql = sql[:-1].rstrip()

        # la página solo debe ejecutar consultas, nunca cambios
        if not sql.upper().startswith("SELECT"):
            raise ValueError("El ejercicio " + str(numero) + " debe ser solo SELECT")
        ejercicios.append({"numero": numero, "titulo": titulo, "grupo": grupo,
                           "enunciado": " ".join(enunciado), "sql": sql})

    return ejercicios
