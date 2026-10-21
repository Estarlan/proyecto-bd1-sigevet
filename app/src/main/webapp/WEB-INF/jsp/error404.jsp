<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Página no encontrada · SIGEVET"/>
<%@ include file="encabezado.jspf" %>
<main id="contenido" class="container pagina" tabindex="-1">
  <section class="card">
    <h2>Página no encontrada</h2>
    <p>No existe ese registro o ejercicio.</p>
    <a href="<c:url value='/'/>">Volver al inicio</a>
  </section>
</main>
<%@ include file="pie.jspf" %>
