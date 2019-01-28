package servlet;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import launch.Main;


@WebServlet("/ArticleServlet")
public class ArticleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	int id = 0;
       
    
    public ArticleServlet() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		
		if (request.getParameter("id") != null && isNumeric(request.getParameter("id"))) {
			id = Integer.parseInt(request.getParameter("id"));
		}
		request.setAttribute("articleId", id);
		
		Connection connection;
		try {
			connection = Main.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM articles WHERE article_id = " + id);

			int drawOn = 1;
			String title = "";
			String article = "";
			ArrayList<String> categoriesArray = new ArrayList<String>();
			
			while (rs.next()) {
				title = rs.getString("title");
				article = rs.getString("article_text");
			}
			ArrayList<Integer> categories = new ArrayList<Integer>();
			
			ResultSet connections = statement.executeQuery("SELECT * FROM categorie_connection WHERE article_id = " + id);
			while (connections.next()) {
				int categoryId = connections.getInt("category_id");
				categories.add(categoryId);
			}
				
			
			for (int categoryId : categories) {
				ResultSet category = statement.executeQuery("SELECT * FROM categories WHERE category_id = " + categoryId);
				while (category.next()) {
					categoriesArray.add(category.getString("name"));
				}
			}
			
			drawArticle(request, title, categoriesArray, article, drawOn);
			setComments(request, statement, id);
			setCategories(request, statement);
			
			statement.close();
			connection.close();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RequestDispatcher rd = request.getRequestDispatcher("/article.jsp");
		rd.forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");

		String author = request.getParameter("author");
		String comment = request.getParameter("comment");
		id = 0;
		if (request.getParameter("article") != null && isNumeric(request.getParameter("article"))) {
			id = Integer.parseInt(request.getParameter("article"));
		}
		
		try {
			Connection connection = Main.getConnection();
			Statement statement = connection.createStatement();
			statement.executeUpdate("INSERT INTO comments (article_id, autor, comment_text) VALUES (" + id + ", \'" + author +"\', \'" + comment + "\')");
			
			statement.close();
			connection.close();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		doGet(request, response);
	}
	

	
	private void drawArticle(HttpServletRequest request, String title, ArrayList<String> categories, String text, int drawOn) {
		String articleHtml = "<div class=\"article\">\r\n" + 
				"		<h1 class=\"article-title\">" + title + "</h1>\r\n" + 
				"		<h6 class=\"article-categories\">\r\n" + 
				"			Kategorie: \r\n";
		System.out.println(categories);
		for (int i = 0; i < categories.size(); i++) {
			articleHtml += "<a class=\"article-category\">" + categories.get(i) + "</a>\r\n";
		}
		
		
		articleHtml +=		"		</h6>\r\n" + 
				"		<p class=\"article-content\">" + text + "</p>\r\n" + 
				"	</div>";
		
		if (drawOn == 1) {
			request.setAttribute("article1", articleHtml);
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
	
	private void setComments(HttpServletRequest request, Statement statement, int articleId) throws SQLException {
		ResultSet rs = statement.executeQuery("SELECT * FROM comments WHERE article_id = " + articleId);
		String comments = "";
		while (rs.next()) {
			comments += "<div id=\"" + rs.getInt("comment_id") + "\" class=\"comment-container\">\r\n" + 
					"					<div class=\"comment-content\">\r\n" + 
					"						<h5 class=\"comment-author\">" + rs.getString("autor") + "</h5>\r\n" + 
					"						" + rs.getString("comment_text") +
					"					</div>\r\n" + 
					"					<img src=\"default_profile_pic.png\" class=\"comment-profile-picture\" />\r\n" + 
					"				</div>\r\n";
		}
		request.setAttribute("comments", comments);
	}
	
	public boolean isNumeric(String s) {  
	    return s != null && s.matches("[-+]?\\d*\\.?\\d+");  
	}  

}
