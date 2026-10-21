<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Facturas · SIGEVET"/>
<%@ include file="encabezado.jspf" %>
<main id="contenido" class="container pagina" tabindex="-1">

<div class="pagina-heading"><p class="eyebrow">Facturación de la red</p><h2>Facturas</h2><p>Los totales incluyen la cantidad, el precio y el descuento de cada concepto.</p></div>
<form method="get" class="filters">
  <label for="sede">Sede<select id="sede" name="sede"><option value="">Todas las sedes</option><c:forEach var="sede" items="${opciones.sedes}"><option value="${fn:escapeXml(sede.id_sede)}" ${sede.id_sede.toString() == filtros['sede'] ? 'selected' : ''}><c:out value="${sede.nombre}"/></option></c:forEach></select></label>
  <label for="estado">Estado<select id="estado" name="estado"><option value="">Todos los estados</option><c:forEach var="estado" items="${estados}"><option value="${fn:escapeXml(estado)}" ${estado == filtros['estado'] ? 'selected' : ''}><c:out value="${estado}"/></option></c:forEach></select></label>
  <button type="submit">Filtrar</button><a href="<c:url value='/facturas'/>">Limpiar</a>
</form>
<section class="card"><h3>Facturas encontradas</h3>
<c:if test="${not empty filas}"><div class="table-wrap" role="region" aria-label="Facturas" tabindex="0"><table>
  <thead><tr><th scope="col">Número</th><th scope="col">Fecha de emisión</th><th scope="col">Cliente</th><th scope="col">Sede</th><th scope="col">Estado</th><th scope="col">Total</th></tr></thead>
  <tbody><c:forEach var="fila" items="${filas}"><tr><td><a href="<c:url value='/facturas/${fila.id_factura}'/>"><c:out value="${fila.numero_factura}"/></a></td><td><c:out value="${f:fecha(fila.fecha_emision)}"/></td><td><c:out value="${fila.cliente}"/></td><td><c:out value="${fila.sede}"/></td><td><span class="badge badge-${fn:escapeXml(fn:toLowerCase(fila.estado))}"><c:out value="${fila.estado}"/></span></td><td><c:out value="${f:pesos(fila.total)}"/></td></tr></c:forEach></tbody>
</table></div></c:if><c:if test="${empty filas}"><div class="empty-state">No hay facturas que coincidan con los filtros.</div></c:if>
</section>

</main>
<%@ include file="pie.jspf" %>
