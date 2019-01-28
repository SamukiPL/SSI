 <%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>Strona główna</title>
	<link rel="stylesheet" type="text/css" href="main.css">
	<link href='https://fonts.googleapis.com/css?family=Karla' rel='stylesheet'>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>

<div class="wrapper">
	<div class="baner-container">
		<img src="baner.png" id="baner"/>
	</div>

	<div class="content-container">
		<div class="menu">
			<a href="index.jsp" id="mainSite" class="menu-button">Strona główna</a>
		</div>
		<div class="articles">
			${articles}
		</div>
		<div class="sidebar">
			<div class="sidebar-item">
				<h3 class="sidebar-item-header">Rekomendowane</h3>
				<div class="sidebar-item-category">
					${categories}
				</div>
			</div>
		</div>
	</div>

	<div class="footer-container">
		<img src="footer.png" id="footer"/>
	</div>
</div>
</body>
</html>