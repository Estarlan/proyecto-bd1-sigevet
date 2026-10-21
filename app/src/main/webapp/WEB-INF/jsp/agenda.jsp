<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Agenda · SIGEVET"/>
<%@ include file="encabezado.jspf" %>
<main id="contenido" class="container pagina" tabindex="-1">

<div class="pagina-heading"><p class="eyebrow">Atención veterinaria</p><h2>Agenda de citas</h2><p>Consulta las citas por sede, veterinario, estado y fechas.</p></div>
<form method="get" class="filters">
  <label for="sede">Sede<select id="sede" name="sede"><option value="">Todas las sedes</option><c:forEach var="sede" items="${opciones.sedes}"><option value="${fn:escapeXml(sede.id_sede)}" ${sede.id_sede.toString() == filtros['sede'] ? 'selected' : ''}><c:out value="${sede.nombre}"/></option></c:forEach></select></label>
  <label for="veterinario">Veterinario<select id="veterinario" name="veterinario"><option value="">Todos los veterinarios</option><c:forEach var="vet" items="${opciones.veterinarios}"><option value="${fn:escapeXml(vet.id_empleado)}" ${vet.id_empleado.toString() == filtros['veterinario'] ? 'selected' : ''}><c:out value="${vet.nombre}"/></option></c:forEach></select></label>
  <label for="estado">Estado<select id="estado" name="estado"><option value="">Todos los estados</option><c:forEach var="estado" items="${estados}"><option value="${fn:escapeXml(estado)}" ${estado == filtros['estado'] ? 'selected' : ''}><c:out value="${estado}"/></option></c:forEach></select></label>
  <label for="desde">Desde<input id="desde" name="desde" type="date" value="${fn:escapeXml(filtros['desde'])}"></label>
  <label for="hasta">Hasta (incluido)<input id="hasta" name="hasta" type="date" value="${fn:escapeXml(filtros['hasta'])}"></label>
  <button type="submit">Filtrar</button><a href="<c:url value='/agenda'/>">Limpiar</a>
</form>
<section class="card"><h3>Citas encontradas</h3><c:if test="${not empty filas}"><div class="table-wrap"><table>
<thead><tr><th>Fecha y hora</th><th>Mascota</th><th>Servicio</th><th>Veterinario</th><th>Sede</th><th>Estado</th><th>Motivo</th></tr></thead>
<tbody><c:forEach var="fila" items="${filas}"><tr>
<td><c:out value="${f:fecha(fila.fecha_hora)}"/></td>
<td><c:out value="${fila.mascota}"/></td>
<td><c:out value="${fila.servicio}"/></td>
<td><c:out value="${fila.veterinario}"/></td>
<td><c:out value="${fila.sede}"/></td>
<td><c:out value="${fila.estado}"/></td>
<td><c:out value="${fila.motivo}"/></td>
</tr></c:forEach></tbody></table></div></c:if>
<c:if test="${empty filas}"><p>No hay registros para mostrar.</p></c:if></section>

</main>
<%@ include file="pie.jspf" %>
