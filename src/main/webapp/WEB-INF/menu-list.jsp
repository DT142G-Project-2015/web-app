<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <h1>Menus</h1>
    <c:forEach items="${menus}" var="menu">
        <div><a href="menu/${menu['id']}">${menu['name']}</a></div>
    </c:forEach>
</html>
