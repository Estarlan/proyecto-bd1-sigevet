<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Personal · SIGEVET"/>
<%@ include file="encabezado.jspf" %>
<main id="contenido" class="container pagina" tabindex="-1">

<div class="pagina-heading"><p class="eyebrow">Equipo Huella Sana</p><h2>Personal</h2><p>Directores de sede, empleados y especialidades veterinarias.</p></div>
<form method="get" class="filters">
  <label for="sede">Sede<select id="sede" name="sede"><option value="">Todas las sedes</option><c:forEach var="sede" items="${datos.sedes}"><option value="${fn:escapeXml(sede.id_sede)}" ${sede.id_sede.toString() == filtros['sede'] ? 'selected' : ''}><c:out value="${sede.nombre}"/></option></c:forEach></select></label>
  <button type="submit">Filtrar</button><a href="<c:url value='/personal'/>">Limpiar</a>
</form>
<section class="card"><h3>Sedes y directores</h3><c:if test="${not empty datos.sedes}"><div class="table-wrap"><table>
<thead><tr><th>Sede</th><th>Director</th></tr></thead>
<tbody><c:forEach var="fila" items="${datos.sedes}"><tr>
<td><c:out value="${fila.nombre}"/></td>
<td><c:out value="${fila.director}"/></td>
</tr></c:forEach></tbody></table></div></c:if>
<c:if test="${empty datos.sedes}"><p>No hay registros para mostrar.</p></c:if></section>
<section class="card"><h3>Empleados por sede</h3><p class="muted">VET: veterinario · AUX: auxiliar · ADM: administrativo.</p>
<c:if test="${not empty datos.empleados}"><div class="table-wrap" role="region" aria-label="Personal de las sedes" tabindex="0"><table>
  <thead><tr><th scope="col">Nombres</th><th scope="col">Apellidos</th><th scope="col">Sede</th><th scope="col">Tipo</th><th scope="col">Especialidades</th><th scope="col">Estado</th><th scope="col">Teléfono</th><th scope="col">Correo electrónico</th></tr></thead>
  <tbody><c:forEach var="fila" items="${datos.empleados}"><tr>
    <td><c:out value="${fila.nombres}"/></td><td><c:out value="${fila.apellidos}"/></td><td><c:out value="${fila.sede}"/></td><td><span class="badge badge-${fn:escapeXml(fn:toLowerCase(fila.tipo_empleado))}"><c:out value="${fila.tipo_empleado}"/></span></td>
    <td><c:if test="${fila.tipo_empleado == 'VET'}"><c:if test="${empty fila.especialidades}">Sin especialidades registradas</c:if><c:forEach var="especialidad" items="${fila.especialidades}" varStatus="paso"><c:if test="${!paso.first}">, </c:if><c:out value="${especialidad}"/></c:forEach></c:if><c:if test="${!(fila.tipo_empleado == 'VET')}">—</c:if></td>
    <td><span class="badge"><c:out value="${fila.estado}"/></span></td><td><c:out value="${fila.telefono}"/></td><td><c:out value="${fila.email}"/></td>
  </tr></c:forEach></tbody>
</table></div></c:if><c:if test="${empty datos.empleados}"><div class="empty-state">No hay empleados registrados para esta sede.</div></c:if>
</section>

</main>
<%@ include file="pie.jspf" %>
