 <%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>Logowanie</title>
	<link rel="stylesheet" type="text/css" href="main.css">
	<link href='https://fonts.googleapis.com/css?family=Karla' rel='stylesheet'>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>

<div class="login-box">
	<p class="error-message">${data}</p>
	<form action=LoginServlet method="post">  
		<input type="text" name="username" class="login-input" placeholder="Login"/><br/><br/>  
		<input type="password" name="userpass" class="login-input" placeholder="Hasło"/><br/><br/>  
      	<input type="submit" value="Zaloguj">
	</form> 
</div> 
 
</body>
</html>