<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Álgebra relacional · SIGEVET"/>
<%@ include file="encabezado.jspf" %>
<main id="contenido" class="container pagina" tabindex="-1">

<h2>Álgebra relacional</h2>
<p>Los 55 ejercicios del proyecto. Abre uno para ver el enunciado, el SQL y los resultados.</p>
<c:forEach var="grupo" items="${grupos}">
<section class="card"><h3><c:out value="${grupo.key}"/></h3><ol class="exercise-list">
<c:forEach var="ejercicio" items="${grupo.value}">
  <li value="${fn:escapeXml(ejercicio.numero)}"><a href="<c:url value='/algebra/${ejercicio.numero}'/>"><c:out value="${ejercicio.titulo}"/></a></li>
</c:forEach>
</ol></section>
</c:forEach>

</main>
<%@ include file="pie.jspf" %>
