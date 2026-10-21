<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Cliente · ${datos.datos.nombres} ${datos.datos.apellidos} · SIGEVET"/>
<%@ include file="encabezado.jspf" %>
<main id="contenido" class="container pagina" tabindex="-1">


<div class="pagina-heading"><p><a href="<c:url value='/clientes'/>">← Clientes</a></p><p class="eyebrow">Cliente <c:out value="${datos.datos.id_cliente}"/></p><h2><c:out value="${datos.datos.nombres}"/> <c:out value="${datos.datos.apellidos}"/></h2></div>
<div class="grid">
  <section class="card"><h3>Datos del cliente</h3><dl>
    <dt>Documento</dt><dd><c:out value="${datos.datos.tipo_documento}"/> <c:out value="${datos.datos.num_documento}"/></dd>
    <dt>Correo electrónico</dt><dd><c:out value="${datos.datos.email}"/></dd>
    <dt>Dirección</dt><dd><c:out value="${datos.datos.direccion}"/></dd>
    <dt>Ciudad</dt><dd><c:out value="${datos.datos.ciudad}"/></dd>
    <dt>Departamento</dt><dd><c:out value="${datos.datos.departamento}"/></dd>
    <dt>Fecha de registro</dt><dd><c:out value="${f:fecha(datos.datos.fecha_registro)}"/></dd>
  </dl></section>
  <section class="card"><h3>Teléfonos</h3><c:if test="${not empty datos.telefonos}"><div class="table-wrap"><table>
<thead><tr><th>Teléfono</th><th>Tipo</th></tr></thead>
<tbody><c:forEach var="fila" items="${datos.telefonos}"><tr>
<td><c:out value="${fila.telefono}"/></td>
<td><c:out value="${fila.tipo}"/></td>
</tr></c:forEach></tbody></table></div></c:if>
<c:if test="${empty datos.telefonos}"><p>No hay registros para mostrar.</p></c:if></section>
</div>
<section class="card"><h3>Mascotas</h3><c:if test="${not empty datos.mascotas}"><div class="table-wrap"><table>
<thead><tr><th>Mascota</th><th>Raza</th><th>Especie</th><th>Estado</th></tr></thead>
<tbody><c:forEach var="fila" items="${datos.mascotas}"><tr>
<td><a href="<c:url value='/mascotas/${fila.id_mascota}'/>"><c:out value="${fila.nombre}"/></a></td>
<td><c:out value="${fila.raza}"/></td>
<td><c:out value="${fila.especie}"/></td>
<td><c:out value="${fila.estado}"/></td>
</tr></c:forEach></tbody></table></div></c:if>
<c:if test="${empty datos.mascotas}"><p>No hay registros para mostrar.</p></c:if></section>
<section class="card"><h3>Facturas</h3><c:if test="${not empty datos.facturas}"><div class="table-wrap"><table>
<thead><tr><th>Número</th><th>Fecha de emisión</th><th>Sede</th><th>Estado</th><th>Total</th></tr></thead>
<tbody><c:forEach var="fila" items="${datos.facturas}"><tr>
<td><a href="<c:url value='/facturas/${fila.id_factura}'/>"><c:out value="${fila.numero_factura}"/></a></td>
<td><c:out value="${f:fecha(fila.fecha_emision)}"/></td>
<td><c:out value="${fila.sede}"/></td>
<td><c:out value="${fila.estado}"/></td>
<td><c:out value="${f:pesos(fila.total)}"/></td>
</tr></c:forEach></tbody></table></div></c:if>
<c:if test="${empty datos.facturas}"><p>No hay registros para mostrar.</p></c:if></section>

</main>
<%@ include file="pie.jspf" %>
