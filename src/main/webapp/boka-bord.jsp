<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>


<html>
<head>
	<meta charset="UTF-8">
	<title>Antons Skafferi - Meny</title>
	<link rel="stylesheet" media="screen and (orientation:portrait)" href="css/mobile.css">
	<link rel="stylesheet" media="screen and (orientation:landscape)" href="css/style.css">
	<script src="js/jquery-1.11.3.min.js"></script>

	<link rel="stylesheet" href="js/jquery-ui-1.11.4.custom//jquery-ui.css">
    <script src="js/jquery-ui-1.11.4.custom/jquery-ui.js"></script>
    <script src="js/datepicker_sv.js"></script>

	<script src="js/book_table.js"></script>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
</head>

<body>
	<%@include file="sidebar.jsp" %>
	<div id="content">
		<div id="media-holder">
			<img src="img/boka-bord.jpg">
		</div>
		<div id="text-holder">
			<div id="book_holder">

				<div class="section" id="section_1">
					<i class="fa fa-users" style="font-size: 60px; display: block"></i>
					<h1>ANTAL PERSONER</h1>
					<div>
						<div class="button_persons" id="p_1">1</div>
						<div class="button_persons" id="p_2">2</div>
						<div class="button_persons" id="p_3">3</div>
						<div class="button_persons" id="p_4">4</div>
					</div>
					<div>
						<div class="button_persons" id="p_5">5</div>
						<div class="button_persons" id="p_6">6</div>
						<div class="button_persons" id="p_7">7</div>
						<div class="button_persons" id="p_8">8</div>
					</div>
				</div>

				<div class="section" id="section_2">
					<i class="fa fa-caret-down arrow"></i>
					<div class="s_border"></div>
					<i class="fa fa-calendar-check-o" style="font-size: 60px; display: block"></i>
					<h1>DATUM</h1>
					<input placeholder="Välj ett datum" id="book_date" type="text" name="book_date">
				</div>

				<div class="section" id="section_3">
					<i class="fa fa-caret-down arrow"></i>
					<div class="s_border"></div>
					<i class="fa fa-clock-o" style="font-size: 60px; display: block"></i>
					<h1>TID</h1>
					<div class="week_day">
						<div>
							<div class="button_time">17:00</div>
							<div class="button_time">17:30</div>
							<div class="button_time">18:00</div>
							<div class="button_time">18:30</div>
						</div>
						<div>
							<div class="button_time">19:00</div>
							<div class="button_time">19:30</div>
							<div class="button_time">20:00</div>
							<div class="button_time">20:30</div>
						</div>
					</div>
				</div>

				<div class="section" id="section_4">
					<i class="fa fa-caret-down arrow"></i>
					<div class="s_border"></div>
					<i class="fa fa-star" style="font-size: 60px; display: block"></i>
					<h1>KONTAKTUPPGIFTER</h1>
					<input placeholder="Namn" type="text" name="name" id="name"><br>
					<input placeholder="Tel" type="text" name="tel" id="tel"><br>
					<input placeholder="Email" type="text" name="email" id="email"><br>
					<div id="book_done">KLAR</div>
				</div>

			</div>
		</div>
	</div>
</body>
