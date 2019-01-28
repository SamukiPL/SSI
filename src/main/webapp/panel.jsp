 <%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>Dodaj artykuł</title>
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
			<a href="IndexServlet" id="mainSite" class="menu-button">Strona główna</a>
			<a href="#" id="anotherSite" class="menu-button menu-button-active">Przykładowy artykuł</a>
		</div>
		<div class="articles">
			<div class="article">
				<form action=AddArticleServlet method="post">
					<input type="text" name="title" class="login-input" placeholder="Tytuł"/><br/><br/>  
					<input type="text" name="categories" class="login-input" placeholder="Kategoria, Kategoria"/><br/><br/>  
					<textarea name="full-text" class="login-input" placeholder="Treść" rows="20"></textarea>
					<input type="submit" value="Dodaj" />
				</form>
			</div>
		</div>
	</div>

	<div class="footer-container">
		<img src="footer.png" id="footer"/>
	</div>
</div>
</body>
</html>=