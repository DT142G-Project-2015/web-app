<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>


<html>
<head>
	<meta charset="UTF-8">
	<title>Antons Skafferi - Meny</title>
	<link rel="stylesheet" media="screen and (orientation:portrait)" href="css/mobile.css">
	<link rel="stylesheet" media="screen and (orientation:landscape)" href="css/style.css">
	<script src="js/jquery-1.11.3.min.js"></script>
</head>

<body>
	<%@include file="sidebar.jsp" %>
	<div id="content">
		<div id="media-holder">
			<img src="img/meny.jpg">
		</div>
		<div id="text-holder">
		<div id="menu_holder">
			<c:forEach items="${dinnerGroups}" var="g">
				<h1 class="menu_h1">${g.name}</h1><br>
				<c:forEach items="${g.items}" var="i">
					<br><b>${i.name}, ${i.price}kr</b>
					<c:if test = "${i.description != ''}">
						<br><i style="font-size: 0.8em">${i.description}</i><br>
					</c:if>
				</c:forEach>
			</c:forEach>
        </div>
		</div>
	</div>
</body>
