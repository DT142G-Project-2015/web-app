<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<div id="sidebar_holder">
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
                <i style="font-size: 0.9em">11.00-13.00 Lunch</i><br>
                <i style="font-size: 0.9em">17.00-21.00 A la carté</i><br>
                <strong>Lördagar</strong><br>
                <i style="font-size: 0.9em">16.00-00.00 A la carté</i><br>
                <strong>Söndagar</strong><br>
                <i style="font-size: 0.9em">Stängt</i>
            </div>
            <div class="sidebar-content-item">

                <h3>Dagens lunch</h3>

                <c:forEach items="${lunchGroups}" var="g">
                    <c:if test="${!fn:contains(g.name, 'Mat')}">
                        <br><h3>${g.name}</h3>
                    </c:if>
                    <ul>
                        <c:forEach items="${g.items}" var="i">
                            <c:if test="${!fn:contains(i.name, 'inkl dryck och kaffe')}">
                                <li style = "margin: 5px 5px 5px 20px; font-size: 0.9em" ><i>${i.name}</i></li>
                            </c:if>
                        </c:forEach>
                    </ul>
                </c:forEach>

            </div>
    </div>
    <div id="end_sidebar">
        <div id="left_end"></div>
        <div id="right_end"></div>
    </div>
    </div>
</div>

