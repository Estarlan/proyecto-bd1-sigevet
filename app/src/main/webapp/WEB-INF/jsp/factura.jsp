<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Factura · ${datos.datos.numero_factura} · SIGEVET"/>
<%@ include file="encabezado.jspf" %>
<main id="contenido" class="container pagina" tabindex="-1">


<div class="pagina-heading"><p><a href="<c:url value='/facturas'/>">← Facturas</a></p><p class="eyebrow">Detalle de facturación</p><h2>Factura <c:out value="${datos.datos.numero_factura}"/></h2></div>
<section class="card"><h3>Datos de la factura</h3><dl>
  <dt>Cliente</dt><dd><a href="<c:url value='/clientes/${datos.datos.id_cliente}'/>"><c:out value="${datos.datos.cliente}"/></a></dd><dt>Sede</dt><dd><c:out value="${datos.datos.sede}"/></dd>
  <dt>Fecha de emisión</dt><dd><c:out value="${f:fecha(datos.datos.fecha_emision)}"/></dd><dt>Método de pago</dt><dd><c:out value="${datos.datos.metodo_pago}"/></dd>
  <dt>Estado</dt><dd><span class="badge badge-${fn:escapeXml(fn:toLowerCase(datos.datos.estado))}"><c:out value="${datos.datos.estado}"/></span></dd>
</dl></section>
<section class="card"><h3>Conceptos facturados</h3>
<c:if test="${not empty datos.lineas}"><div class="table-wrap" role="region" aria-label="Detalle de la factura" tabindex="0"><table>
  <thead><tr><th scope="col">Línea</th><th scope="col">Servicio o medicamento</th><th scope="col">Cantidad</th><th scope="col">Precio unitario</th><th scope="col">Descuento</th><th scope="col">Subtotal</th></tr></thead>
  <tbody><c:forEach var="fila" items="${datos.lineas}"><tr><td><c:out value="${fila.num_linea}"/></td><td><c:out value="${fila.concepto}"/></td><td><c:out value="${fila.cantidad}"/></td><td><c:out value="${f:pesos(fila.precio_unitario)}"/></td><td><c:out value="${fila.descuento_pct}"/> %</td><td><c:out value="${f:pesos(fila.subtotal)}"/></td></tr></c:forEach></tbody>
</table></div></c:if><c:if test="${empty datos.lineas}"><div class="empty-state">Esta factura no tiene conceptos registrados.</div></c:if>
<p class="muted">Subtotal = cantidad × precio unitario × (1 − descuento / 100).</p><p>Total de la factura <strong class="stat-value"><c:out value="${f:pesos(datos.total)}"/></strong></p>
</section>

</main>
<%@ include file="pie.jspf" %>
