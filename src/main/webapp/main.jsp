 <%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>Strona główna</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/main.css">
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
			<a href="#" id="mainSite" class="menu-button menu-button-active">Strona główna</a>
			${loginOrLogout}
			${admin}
		</div>
		<div class="articles">
		${article1}
		${article2}
		${article3}

			<div class="articles-pages" id="articles-pages">
				<p class="articles-pages-desc" id="articles-pages-desc">Strona 1 z 5</p>
			</div>
		</div>
		<div class="sidebar">
			<div class="sidebar-item">
				<h3 class="sidebar-item-header">Kategorie</h3>
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
<script type="text/javascript">
	var numberOfPages = ${numberOfPages};
	var pageActive = ${activePage};

	function prepareArticlesPages(numberOfPages, pageActive) {
		var startingPage = 1;
		var endingPage = 5;
		if (pageActive <= 3) {
			startingPage = 1;
			endingPage = 5;
		} else if (pageActive > 3 && pageActive < numberOfPages - 4) {
			startingPage = pageActive - 2;
			endingPage = pageActive + 2;
		} else if (pageActive >= numberOfPages - 4) {
			startingPage = numberOfPages - 4;
			endingPage = numberOfPages;
		}
		if (endingPage > numberOfPages)
			endingPage = numberOfPages;

		var articlePages = document.getElementById('articles-pages');

		if (startingPage != 1) {
				articlePages.innerHTML += "<a class=\"article-page\" href=\"IndexServlet?page=1\">Pierwsza</a>";
				articlePages.innerHTML += "<span class=\"article-page-dots\">...</span>";
		}

		for (i = startingPage; i <= endingPage; i++) {
			if (i == pageActive)
				articlePages.innerHTML += "<a class=\"article-page article-page-active\" href=\"IndexServlet?page=" + i + "\">" + i + "</a>";
			else 
				articlePages.innerHTML += "<a class=\"article-page\" href=\"IndexServlet?page=" + i + "\">" + i + "</a>";
		}

		if (endingPage != numberOfPages) {
				articlePages.innerHTML += "<span class=\"article-page-dots\">...</span>";
				articlePages.innerHTML += "<a class=\"article-page\" href=\"IndexServlet?page=" + i + "\">Ostatnia</a>";
		}

		var articlePagesDesc = document.getElementById('articles-pages-desc');
		articlePagesDesc.innerHTML = "Strona " + pageActive + " z " + numberOfPages;
	}
	prepareArticlesPages(numberOfPages, pageActive);

</script>
</body>
</html>