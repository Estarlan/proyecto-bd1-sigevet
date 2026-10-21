<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Clientes · SIGEVET"/>
<%@ include file="encabezado.jspf" %>
<main id="contenido" class="container pagina" tabindex="-1">

<div class="pagina-heading"><p class="eyebrow">Personas y mascotas</p><h2>Clientes</h2><p>Consulta los datos de contacto, las mascotas y la facturación de cada cliente.</p></div>
<form method="get" class="filters"><label for="buscar">Documento o apellido<input id="buscar" name="buscar" type="search" placeholder="Buscar cliente" value="${fn:escapeXml(buscar)}"></label><button type="submit">Buscar</button><a href="<c:url value='/clientes'/>">Limpiar</a></form>
<section class="card">
  <p class="muted"><c:out value="${datos.total}"/> cliente<c:if test="${datos.total != 1}">s</c:if> encontrado<c:if test="${datos.total != 1}">s</c:if>.</p>
  <c:if test="${not empty datos.filas}"><div class="table-wrap"><table>
<thead><tr><th>Código</th><th>Nombres</th><th>Apellidos</th><th>Documento</th><th>Correo electrónico</th></tr></thead>
<tbody><c:forEach var="fila" items="${datos.filas}"><tr>
<td><a href="<c:url value='/clientes/${fila.id_cliente}'/>"><c:out value="${fila.id_cliente}"/></a></td>
<td><c:out value="${fila.nombres}"/></td>
<td><c:out value="${fila.apellidos}"/></td>
<td><c:out value="${fila.num_documento}"/></td>
<td><c:out value="${fila.email}"/></td>
</tr></c:forEach></tbody></table></div></c:if>
<c:if test="${empty datos.filas}"><p>No hay registros para mostrar.</p></c:if>
  <nav class="pagination" aria-label="Páginas de clientes">
    <c:if test="${pagina > 1}"><c:url var="anterior" value="/clientes"><c:param name="buscar" value="${buscar}"/><c:param name="pagina" value="${pagina - 1}"/></c:url><a href="${fn:escapeXml(anterior)}">← Anterior</a></c:if>
    <span>Página <c:out value="${pagina}"/> de <c:out value="${totalPaginas}"/></span>
    <c:if test="${pagina < totalPaginas}"><c:url var="siguiente" value="/clientes"><c:param name="buscar" value="${buscar}"/><c:param name="pagina" value="${pagina + 1}"/></c:url><a href="${fn:escapeXml(siguiente)}">Siguiente →</a></c:if>
  </nav>
</section>

</main>
<%@ include file="pie.jspf" %>
