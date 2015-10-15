<!DOCTYPE html>


<html>
<head>
	<meta charset="UTF-8">
	<title>Antons Skafferi - Meny</title>
	<link rel="stylesheet" media="screen and (orientation:portrait)" href="css/mobile.css">
	<link rel="stylesheet" media="screen and (orientation:landscape)" href="css/style.css">
	<script src="js/jquery-1.11.3.min.js"></script>
	<script src="https://maps.googleapis.com/maps/api/js"></script>
</head>

<body>
	<%@include file="sidebar.jsp" %>
	<div id="content">
		<div id="media-holder" style="height: 700px">
			
		</div>
		<script>
			$(document).ready(function(){
				function initialize() {
			        var mapCanvas = document.getElementById('media-holder');
			        var mapOptions = {
			        	center: new google.maps.LatLng(62.394099, 17.283968),
			        	zoom: 15,
			        	mapTypeId: google.maps.MapTypeId.ROADMAP
			        }
			        var map = new google.maps.Map(mapCanvas, mapOptions);
			        var styles = [{"stylers":[{"hue":"#ff1a00"},{"invert_lightness":true},{"saturation":-80},{"lightness":50},{"gamma":0.5}]},{"featureType":"water","elementType":"geometry","stylers":[{"color":"#2D333C"}]}];

					map.setOptions({styles: styles});
		    	}
		    	google.maps.event.addDomListener(window, 'load', initialize);
		    	$("#media-holder").hover(function(){
		    		$("#text-holder").animate({"top": "0px"}, 600);
		    	}, function(){
		    		$("#text-holder").animate({"top": "-475px"}, 600);
		    	});
			});
	    </script>
		<div id="text-holder" style="position: relative; top: -475px; min-height: 475px">
			 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc ornare eros in nibh tempus, non semper nibh condimentum. Etiam ultrices sit amet odio cursus tincidunt. Vivamus lobortis vehicula orci, ac elementum est commodo ut. Vivamus ut libero eu massa viverra finibus. Curabitur eu sagittis mi, sed pulvinar ante. Nullam venenatis mauris arcu, ac placerat arcu pulvinar eget. In tellus ante, mattis a blandit ut, consequat ac mauris.

In porta imperdiet libero, sit amet condimentum metus porta vitae. Duis efficitur sit amet lacus at auctor. Etiam scelerisque lobortis vestibulum. Donec fringilla efficitur erat, vel tincidunt sapien ultrices sit amet. Proin risus lectus, convallis id convallis ac, mollis in libero. Maecenas viverra metus et lacinia dignissim. Duis maximus faucibus sagittis. Proin interdum mattis felis porttitor semper. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Praesent ullamcorper quam non semper elementum. Vestibulum id pulvinar ligula. Ut id erat a risus mattis aliquet. Ut vel tristique turpis. Ut auctor sit amet sapien vel pulvinar.

Mauris eget dapibus ante. Morbi id lorem sed erat venenatis maximus. Sed mauris nulla, mattis nec est eget, eleifend convallis justo. Fusce vitae eros eget lorem posuere pharetra. Nunc a arcu quis neque luctus tempus. Donec egestas lorem ipsum, quis porttitor diam suscipit nec. Nam quis orci neque. Sed fermentum ante nisi, id hendrerit nulla lobortis a. Nam ac risus arcu.

Nulla interdum blandit lorem, sed gravida mauris. Aliquam sem odio, posuere id luctus vel, interdum vulputate turpis. Praesent quis venenatis nisi, vel finibus ex. Interdum et malesuada fames ac ante ipsum primis in faucibus. Mauris a nisi ut nisi viverra auctor. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Fusce suscipit erat non ligula porttitor condimentum. Praesent commodo, metus pretium iaculis porta, metus ex placerat sapien, nec feugiat arcu metus ut lectus. Pellentesque commodo congue metus, volutpat ullamcorper elit pharetra ut. Pellentesque dui justo, ornare sed nulla ut, pharetra condimentum erat. Nulla volutpat aliquam rutrum. 
		</div>
	</div>
</body>
