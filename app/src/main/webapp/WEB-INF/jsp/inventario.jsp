<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Inventario · SIGEVET"/>
<%@ include file="encabezado.jspf" %>
<main id="contenido" class="container pagina" tabindex="-1">

<div class="pagina-heading"><p class="eyebrow">Disponibilidad de medicamentos</p><h2>Inventario por sede</h2><p>Las existencias inferiores al mínimo aparecen con la indicación «Stock bajo».</p></div>
<form method="get" class="filters">
  <label for="sede">Sede<select id="sede" name="sede"><option value="">Todas las sedes</option><c:forEach var="sede" items="${opciones.sedes}"><option value="${fn:escapeXml(sede.id_sede)}" ${sede.id_sede.toString() == filtros['sede'] ? 'selected' : ''}><c:out value="${sede.nombre}"/></option></c:forEach></select></label>
  <button type="submit">Filtrar</button><a href="<c:url value='/inventario'/>">Limpiar</a>
</form>
<section class="card"><h3>Medicamentos y existencias</h3>
<c:if test="${not empty filas}"><div class="table-wrap" role="region" aria-label="Inventario de medicamentos" tabindex="0"><table>
  <thead><tr><th scope="col">Sede</th><th scope="col">Medicamento</th><th scope="col">Presentación</th><th scope="col">Stock actual</th><th scope="col">Stock mínimo</th><th scope="col">Disponibilidad</th><th scope="col">Actualización</th></tr></thead>
  <tbody><c:forEach var="fila" items="${filas}"><tr class="${fila.stock_actual < fila.stock_minimo ? 'low-stock' : ''}">
    <td><c:out value="${fila.sede}"/></td><td><c:out value="${fila.medicamento}"/></td><td><c:out value="${fila.presentacion}"/></td><td><c:out value="${fila.stock_actual}"/></td><td><c:out value="${fila.stock_minimo}"/></td><td><span class="badge"><c:out value="${fila.stock_actual < fila.stock_minimo ? 'Stock bajo' : 'Stock suficiente'}"/></span></td><td><c:out value="${f:fecha(fila.fecha_actualizacion)}"/></td>
  </tr></c:forEach></tbody>
</table></div></c:if><c:if test="${empty filas}"><div class="empty-state">No hay medicamentos registrados para esta sede.</div></c:if>
</section>

</main>
<%@ include file="pie.jspf" %>
