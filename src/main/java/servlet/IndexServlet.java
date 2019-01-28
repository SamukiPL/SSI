package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.codec.binary.StringUtils;

import launch.Main;


@WebServlet("/IndexServlet")
public class IndexServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public IndexServlet() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		HttpSession session = request.getSession(false);
		if (session != null && request.isRequestedSessionIdValid()) {
			String test = "<a href=\"panel.jsp\" id=\"anotherSite\" class=\"menu-button\">Panel Administratora</a>";
			request.setAttribute("admin", test); 
			request.setAttribute("loginOrLogout", "<a href=\"LogoutServlet\" id=\"anotherSite\" class=\"menu-button\">Wyloguj</a>");
		}  else {
			request.setAttribute("message", "");
			request.setAttribute("loginOrLogout", "<a href=\"login.jsp\" id=\"anotherSite\" class=\"menu-button\">Zaloguj</a>");
		}
		int pageNumber = 1;
		int activePage = 1;
		if (request.getParameter("page") != null && isNumeric(request.getParameter("page"))) {
			activePage = Integer.parseInt(request.getParameter("page"));
		}
		
		Connection connection;
		try {
			connection = Main.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS page_number FROM articles");
			if (rs.next()) {
				int number = rs.getInt("page_number");
				pageNumber = number/3;
				if (number % 3 != 0) {
					pageNumber++;
				}
			}
			getArticles(request, statement, (activePage - 1) * 3);
			setCategories(request, statement);
			statement.close();
			connection.close();
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		request.setAttribute("numberOfPages", pageNumber);
		request.setAttribute("activePage", activePage);
        
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/main.jsp");
		rd.include(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void getArticles(HttpServletRequest request, Statement statement, int offset) throws SQLException {
		ResultSet rs = statement.executeQuery("SELECT * FROM articles ORDER BY article_id DESC LIMIT 3 OFFSET " + offset);

		int drawOn = 1;
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ArrayList<String> titles = new ArrayList<String>();
		ArrayList<String> articles = new ArrayList<String>();
		ArrayList<ArrayList<Integer>> categoriesIds = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<String>> categoriesArray = new ArrayList<ArrayList<String>>();
		
		while (rs.next()) {
			ids.add(rs.getInt("article_id"));
			titles.add(rs.getString("title"));
			articles.add(rs.getString("article_text"));
		}
		ArrayList<ArrayList<Integer>> categories = new ArrayList<ArrayList<Integer>>();
		for (Integer id : ids) {
			ResultSet connections = statement.executeQuery("SELECT * FROM categorie_connection WHERE article_id = " + id);
			ArrayList<Integer> category = new ArrayList<Integer>();
			while (connections.next()) {
				int categoryId = connections.getInt("category_id");
				category.add(categoryId);
			}
			categories.add(category);
		}
		for (ArrayList<Integer> list : categories) {
			ArrayList<Integer> categoryIds = new ArrayList<Integer>();
			ArrayList<String> categoryNames = new ArrayList<String>();
			for (int categoryId : list) {
				ResultSet category = statement.executeQuery("SELECT * FROM categories WHERE category_id = " + categoryId);
				while (category.next()) {
					categoryIds.add(category.getInt("category_id"));
					categoryNames.add(category.getString("name"));
				}
			}
			categoriesIds.add(categoryIds);
			categoriesArray.add(categoryNames);
		}
		
		for (int i = 0; i < ids.size(); i++) {
			drawArticle(request, ids.get(i), titles.get(i), categoriesIds.get(i), categoriesArray.get(i), articles.get(i), drawOn);
			drawOn++;
		}
		
	}
	
	private void drawArticle(HttpServletRequest request, int id, String title, ArrayList<Integer> categoriesIds, ArrayList<String> categories, String text, int drawOn) {
		String articleHtml = "<div class=\"article\">\r\n" + 
				"		<h1 class=\"article-title\"><a class=\"article-title\" href=\"ArticleServlet?id=" + id + "\">" + title + "</a></h1>\r\n" + 
				"		<h6 class=\"article-categories\">\r\n" + 
				"			Kategorie: \r\n";
		for (int i = 0; i < categories.size(); i++) {
			articleHtml += "<a class=\"article-category\" href=\"CategoryServlet?id=" + categoriesIds.get(i) +  "\" style=\"" + 
					"text-decoration: none; color: #00b5f8;\">" + categories.get(i) + "</a>\r\n";
		}
		
		
		articleHtml +=		"		</h6>\r\n" + 
				"		<p class=\"article-content\">" + text + "</p>\r\n" + 
				"	</div>";
		
		if (drawOn == 1) {
			request.setAttribute("article1", articleHtml);
		} else if (drawOn == 2) {
			request.setAttribute("article2", articleHtml);
		} else if (drawOn == 3) {
			request.setAttribute("article3", articleHtml);
			
		}
	}
	
	private void setCategories(HttpServletRequest request, Statement statement) throws SQLException {
		ResultSet rs = statement.executeQuery("SELECT * FROM categories");
		String categories = "<h6 class=\"article-categories\">";
		while (rs.next()) {
			categories += "<a class=\"article-category\" href=\"CategoryServlet?id=" + rs.getInt("category_id") + "\" style=\"" + 
					"text-decoration: none; color: #00b5f8;\">" + rs.getString("name") + "</a>\r\n";
		}
		categories += "</h6>";
		request.setAttribute("categories", categories);
	}
	
	public boolean isNumeric(String s) {  
	    return s != null && s.matches("[-+]?\\d*\\.?\\d+");  
	}  

}
