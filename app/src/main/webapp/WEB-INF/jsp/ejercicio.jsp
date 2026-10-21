<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Ejercicio ${ejercicio.numero} · SIGEVET"/>
<%@ include file="encabezado.jspf" %>
<main id="contenido" class="container pagina" tabindex="-1">

<p><a href="<c:url value='/algebra'/>">Volver a álgebra relacional</a></p>
<h2>Ejercicio <c:out value="${ejercicio.numero}"/>. <c:out value="${ejercicio.titulo}"/></h2>
<p><c:out value="${ejercicio.grupo}"/></p>
<section class="card"><h3>Enunciado</h3><p><c:out value="${ejercicio.enunciado}"/></p>
<h3>Consulta SQL</h3><pre><code><c:out value="${ejercicio.sql}"/></code></pre></section>
<section class="card"><h3>Resultados</h3><p><c:out value="${fn:length(filas)}"/> filas obtenidas.</p>
<c:if test="${not empty filas}"><div class="table-wrap"><table>
  <thead><tr><c:forEach var="columna" items="${columnas}"><th><c:out value="${columna}"/></th></c:forEach></tr></thead>
  <tbody><c:forEach var="fila" items="${filas}"><tr><c:forEach var="columna" items="${columnas}">
    <td><c:choose>
      <c:when test="${columnasPesos.contains(columna)}"><c:out value="${f:pesos(fila[columna])}"/></c:when>
      <c:otherwise><c:out value="${fila[columna]}"/></c:otherwise>
    </c:choose></td>
  </c:forEach></tr></c:forEach></tbody>
</table></div></c:if><c:if test="${empty filas}"><p>La consulta no devolvió registros.</p></c:if>
</section>

</main>
<%@ include file="pie.jspf" %>
