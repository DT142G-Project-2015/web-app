<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div id="sidebar">
	<div id="logo-holder"></div>
	<div id="nav">
		<a class='nav-item ${fn:contains(pageContext.request.requestURI, "meny") ? "active" : ""}' href="meny">Meny</a>
		<a class='nav-item ${fn:contains(pageContext.request.requestURI, "om-oss") ? "active" : ""}' href="om-oss">Om oss</a>
		<a class='nav-item ${fn:contains(pageContext.request.requestURI, "boka-bord") ? "active" : ""}' href="boka-bord">Boka bord</a>
		<a class='nav-item ${fn:contains(pageContext.request.requestURI, "hitta-hit") ? "active" : ""}' href="hitta-hit">Hitta hit</a>
	</div>
    <div id="sidebar-content">
        <div class="sidebar-content-item">
            <h3>Öppettider</h3>
            <strong>Vardagar</strong><br>
            11.00-13.00 Lunch<br>
            17.00-21.00 A la carté<br>
            <strong>Lördagar</strong><br>
            16.00-00.00 A la carté<br>
            <strong>Söndagar</strong><br>
            Stängt
        </div>
        <div class="sidebar-content-item">
            <h3>Dagens lunch</h3>

            <c:forEach items="${lunchGroups}" var="g">
                <strong>${g.name}</strong><br>
                <c:forEach items="${g.items}" var="i">
                    ${i.name}<br>
                </c:forEach>
            </c:forEach>

        </div>
    </div>
</div>

