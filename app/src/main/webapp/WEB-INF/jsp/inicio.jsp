<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Inicio · SIGEVET"/>
<%@ include file="encabezado.jspf" %>
<main id="contenido" class="container pagina" tabindex="-1">

<div class="pagina-heading"><p class="eyebrow">Huella Sana</p><h2>Inicio</h2><p>Resumen de citas, ingresos e inventario.</p></div>
<section class="card"><h3>Próximas 10 citas programadas</h3><c:if test="${not empty datos.proximas}"><div class="table-wrap"><table>
<thead><tr><th>Fecha y hora</th><th>Mascota</th><th>Servicio</th><th>Veterinario</th><th>Sede</th><th>Estado</th></tr></thead>
<tbody><c:forEach var="fila" items="${datos.proximas}"><tr>
<td><c:out value="${f:fecha(fila.fecha_hora)}"/></td>
<td><c:out value="${fila.mascota}"/></td>
<td><c:out value="${fila.servicio}"/></td>
<td><c:out value="${fila.veterinario}"/></td>
<td><c:out value="${fila.sede}"/></td>
<td><c:out value="${fila.estado}"/></td>
</tr></c:forEach></tbody></table></div></c:if>
<c:if test="${empty datos.proximas}"><p>No hay registros para mostrar.</p></c:if></section>
<div class="grid">
  <section class="card"><h3>Ingresos por sede</h3><p class="muted">Total de facturas pagadas, incluidos los descuentos.</p><c:if test="${not empty datos.ingresos}"><div class="table-wrap"><table>
<thead><tr><th>Sede</th><th>Ingresos</th></tr></thead>
<tbody><c:forEach var="fila" items="${datos.ingresos}"><tr>
<td><c:out value="${fila.sede}"/></td>
<td><c:out value="${f:pesos(fila.total)}"/></td>
</tr></c:forEach></tbody></table></div></c:if>
<c:if test="${empty datos.ingresos}"><p>No hay registros para mostrar.</p></c:if></section>
  <section class="card"><h3>Citas por estado</h3><c:if test="${not empty datos.estados_citas}"><div class="table-wrap"><table>
<thead><tr><th>Estado</th><th>Citas</th></tr></thead>
<tbody><c:forEach var="fila" items="${datos.estados_citas}"><tr>
<td><c:out value="${fila.estado}"/></td>
<td><c:out value="${fila.total}"/></td>
</tr></c:forEach></tbody></table></div></c:if>
<c:if test="${empty datos.estados_citas}"><p>No hay registros para mostrar.</p></c:if></section>
</div>
<section class="card"><h3>Medicamentos por debajo del mínimo</h3><c:if test="${not empty datos.stock_bajo}"><div class="table-wrap"><table>
<thead><tr><th>Sede</th><th>Medicamento</th><th>Stock actual</th><th>Stock mínimo</th></tr></thead>
<tbody><c:forEach var="fila" items="${datos.stock_bajo}"><tr>
<td><c:out value="${fila.sede}"/></td>
<td><c:out value="${fila.medicamento}"/></td>
<td><c:out value="${fila.stock_actual}"/></td>
<td><c:out value="${fila.stock_minimo}"/></td>
</tr></c:forEach></tbody></table></div></c:if>
<c:if test="${empty datos.stock_bajo}"><p>No hay registros para mostrar.</p></c:if></section>
<section class="card"><h3>Registros de las 22 tablas</h3><c:if test="${not empty datos.conteos}"><div class="table-wrap"><table>
<thead><tr><th>Tabla</th><th>Registros</th></tr></thead>
<tbody><c:forEach var="fila" items="${datos.conteos}"><tr>
<td><c:out value="${fila.tabla}"/></td>
<td><c:out value="${fila.total}"/></td>
</tr></c:forEach></tbody></table></div></c:if>
<c:if test="${empty datos.conteos}"><p>No hay registros para mostrar.</p></c:if></section>

</main>
<%@ include file="pie.jspf" %>
