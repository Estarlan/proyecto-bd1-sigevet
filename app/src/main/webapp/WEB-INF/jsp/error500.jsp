<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="No se pudieron consultar los datos · SIGEVET"/>
<%@ include file="encabezado.jspf" %>
<main id="contenido" class="container pagina" tabindex="-1">
  <section class="card">
    <h2>No se pudieron consultar los datos</h2>
    <p>Revisa la conexión a Oracle y las variables de conexión.</p>
    <a href="<c:url value='/'/>">Volver al inicio</a>
  </section>
</main>
<%@ include file="pie.jspf" %>
