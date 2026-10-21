<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="tituloPagina" value="Mascota · ${datos.datos.nombre} · SIGEVET"/>
<%@ include file="encabezado.jspf" %>
<main id="contenido" class="container pagina" tabindex="-1">


<div class="pagina-heading"><p><a href="<c:url value='/clientes/${datos.datos.id_cliente}'/>">← Cliente: <c:out value="${datos.datos.cliente}"/></a></p><p class="eyebrow">Mascota <c:out value="${datos.datos.id_mascota}"/></p><h2><c:out value="${datos.datos.nombre}"/></h2></div>
<div class="grid">
  <section class="card"><h3>Datos de la mascota</h3><dl>
    <dt>Especie</dt><dd><c:out value="${datos.datos.especie}"/></dd><dt>Raza</dt><dd><c:out value="${datos.datos.raza}"/></dd>
    <dt>Sexo</dt><dd><c:out value="${datos.datos.sexo}"/></dd><dt>Fecha de nacimiento</dt><dd><c:out value="${f:fecha(datos.datos.fecha_nacimiento)}"/></dd>
    <dt>Color</dt><dd><c:out value="${datos.datos.color}"/></dd><dt>Microchip</dt><dd><c:out value="${datos.datos.microchip}"/></dd>
    <dt>Esterilizado</dt><dd><c:out value="${datos.datos.esterilizado == 'S' ? 'Sí' : 'No'}"/></dd><dt>Estado</dt><dd><span class="badge"><c:out value="${datos.datos.estado}"/></span></dd>
  </dl></section>
  <section class="card"><h3>Historia clínica</h3>
    <c:if test="${datos.historia != null}"><dl>
      <dt>Número de historia</dt><dd><c:out value="${datos.historia.numero_historia}"/></dd><dt>Fecha de apertura</dt><dd><c:out value="${f:fecha(datos.historia.fecha_apertura)}"/></dd>
      <dt>Grupo sanguíneo</dt><dd><c:out value="${datos.historia.grupo_sanguineo}"/></dd><dt>Alergias</dt><dd><c:out value="${datos.historia.alergias}"/></dd>
      <dt>Enfermedades crónicas</dt><dd><c:out value="${datos.historia.enfermedades_cronicas}"/></dd><dt>Observaciones</dt><dd><c:out value="${datos.historia.observaciones}"/></dd>
    </dl></c:if><c:if test="${!(datos.historia != null)}"><p class="empty-state">No hay historia clínica registrada.</p></c:if>
  </section>
</div>
<h3>Consultas</h3>
<c:forEach var="consulta" items="${datos.consultas}">
<article class="card">
  <p class="eyebrow">Consulta <c:out value="${consulta.id_consulta}"/></p><h3><c:out value="${f:fecha(consulta.fecha_hora_inicio)}"/></h3>
  <dl>
    <dt>Veterinario</dt><dd><c:out value="${consulta.veterinario}"/></dd><dt>Sede</dt><dd><c:out value="${consulta.sede}"/></dd>
    <dt>Peso</dt><dd><c:out value="${consulta.peso_kg}"/> kg</dd><dt>Temperatura</dt><dd><c:out value="${consulta.temperatura_c}"/> °C</dd>
    <dt>Frecuencia cardíaca</dt><dd><c:out value="${consulta.frecuencia_cardiaca}"/> lpm</dd><dt>Síntomas</dt><dd><c:out value="${consulta.sintomas}"/></dd>
    <dt>Diagnóstico</dt><dd><c:out value="${consulta.diagnostico}"/></dd><dt>Observaciones</dt><dd><c:out value="${consulta.observaciones}"/></dd>
  </dl>
  <h3>Prescripciones</h3><c:if test="${not empty consulta.prescripciones}"><div class="table-wrap"><table>
<thead><tr><th>Medicamento</th><th>Dosis</th><th>Frecuencia (horas)</th><th>Duración (días)</th><th>Cantidad</th><th>Indicaciones</th></tr></thead>
<tbody><c:forEach var="fila" items="${consulta.prescripciones}"><tr>
<td><c:out value="${fila.medicamento}"/></td>
<td><c:out value="${fila.dosis}"/></td>
<td><c:out value="${fila.frecuencia_horas}"/></td>
<td><c:out value="${fila.duracion_dias}"/></td>
<td><c:out value="${fila.cantidad}"/></td>
<td><c:out value="${fila.indicaciones}"/></td>
</tr></c:forEach></tbody></table></div></c:if>
<c:if test="${empty consulta.prescripciones}"><p>No hay registros para mostrar.</p></c:if>
</article>
</c:forEach><c:if test="${empty datos.consultas}"><div class="empty-state">No hay consultas registradas para esta mascota.</div></c:if>

</main>
<%@ include file="pie.jspf" %>
