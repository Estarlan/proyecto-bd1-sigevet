# Created: 18:55 24-Oct-2026
# Updated: 18:55 24-Oct-2026
"""Read the project's MySQL DDL without treating COMMENT text as constraints."""

import re
from pathlib import Path

_IDENTIFIER = r"`?([A-Za-z_][A-Za-z_0-9]*)`?"


def _without_comments(sql: str) -> str:
    """Preserve quoted literals while removing SQL line and block comments."""
    pattern = r"'(?:''|\\.|[^'\\])*'|\"(?:\"\"|\\.|[^\"\\])*\"|--[^\n]*|/\*[\s\S]*?\*/"
    return re.sub(pattern, lambda m: " " if m[0].startswith(("--", "/*")) else m[0], sql)


def _split(sql: str, separator: str) -> list[str]:
    """Split only outside parentheses and quoted SQL strings/identifiers."""
    parts, start, depth, quote, index = [], 0, 0, None, 0
    while index < len(sql):
        char = sql[index]
        if quote:
            if char == "\\":
                index += 2
                continue
            if char == quote:
                if index + 1 < len(sql) and sql[index + 1] == quote:
                    index += 2
                    continue
                quote = None
        elif char in "'\"`":
            quote = char
        elif char == "(":
            depth += 1
        elif char == ")":
            depth -= 1
        elif char == separator and depth == 0:
            parts.append(sql[start:index].strip())
            start = index + 1
        index += 1
    if quote or depth:
        raise ValueError("Unbalanced SQL parentheses or quotes")
    parts.append(sql[start:].strip())
    return [part for part in parts if part]


def _names(value: str) -> list[str]:
    return [name.strip().strip("`") for name in value.split(",")]


def _body(definition: str) -> str:
    """Return the first parenthesized expression, including nested CHECKs."""
    start = definition.index("(")
    depth, quote, index = 0, None, start
    while index < len(definition):
        char = definition[index]
        if quote:
            if char == "\\":
                index += 2
                continue
            if char == quote:
                if index + 1 < len(definition) and definition[index + 1] == quote:
                    index += 2
                    continue
                quote = None
        elif char in "'\"`":
            quote = char
        elif char == "(":
            depth += 1
        elif char == ")":
            depth -= 1
            if depth == 0:
                return definition[start + 1:index]
        index += 1
    raise ValueError("Unclosed SQL expression")


def _constraint(table: dict, definition: str) -> None:
    match = re.match(rf"CONSTRAINT\s+{_IDENTIFIER}\s+", definition, re.IGNORECASE)
    name = match[1] if match else ""
    clause = definition[match.end():] if match else definition
    if re.match(r"PRIMARY\s+KEY\b", clause, re.IGNORECASE):
        table["pk"] = _names(_body(clause))
    elif re.match(r"UNIQUE\b", clause, re.IGNORECASE):
        table["unique"].append(_names(_body(clause)))
    elif re.match(r"CHECK\b", clause, re.IGNORECASE):
        table["checks"].append(_body(clause).strip())
    elif re.match(r"FOREIGN\s+KEY\b", clause, re.IGNORECASE):
        reference = re.search(rf"\bREFERENCES\s+{_IDENTIFIER}\s*\(", clause, re.IGNORECASE)
        if not reference:
            raise ValueError(f"Missing REFERENCES in {definition}")
        table["fks"].append({
            "columns": _names(_body(clause)),
            "table": reference[1],
            "references": _names(_body(clause[reference.start():])),
            "name": name,
        })
    else:
        raise ValueError(f"Unsupported table constraint: {definition}")


def load_schema(path: Path) -> dict[str, dict]:
    """Extract columns, PKs, UNIQUEs, CHECKs and CREATE/ALTER foreign keys."""
    schema = {}
    for statement in _split(_without_comments(path.read_text(encoding="utf-8")), ";"):
        create = re.match(rf"CREATE\s+TABLE\s+{_IDENTIFIER}\s*\(", statement, re.IGNORECASE)
        alter = re.match(rf"ALTER\s+TABLE\s+{_IDENTIFIER}\s+ADD\s+", statement, re.IGNORECASE)
        if alter:
            _constraint(schema[alter[1]], statement[alter.end():])
            continue
        if not create:
            continue
        name = create[1]
        if name in schema:
            raise ValueError(f"Duplicate table: {name}")
        table = {"columns": [], "pk": [], "unique": [], "fks": [], "checks": []}
        schema[name] = table
        for definition in _split(_body(statement), ","):
            if re.match(r"(?:CONSTRAINT|PRIMARY|UNIQUE|FOREIGN|CHECK)\b", definition, re.IGNORECASE):
                _constraint(table, definition)
                continue
            column = re.match(rf"{_IDENTIFIER}\s+([A-Za-z]+(?:\s*\([^)]*\))?)", definition)
            if not column:
                raise ValueError(f"Unsupported column in {name}: {definition}")
            # Only the SQL prefix contains constraints; COMMENT prose does not.
            prefix = re.split(r"\s+COMMENT\s+", definition, maxsplit=1, flags=re.IGNORECASE)[0]
            table["columns"].append({
                "name": column[1], "type": re.sub(r"\s+", "", column[2]).upper(),
                "nullable": not bool(re.search(r"\bNOT\s+NULL\b", prefix, re.IGNORECASE)),
            })
        for column in table["columns"]:
            if column["name"] in table["pk"]:
                column["nullable"] = False
    if not schema:
        raise ValueError(f"No CREATE TABLE statements in {path}")
    return schema
