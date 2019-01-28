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


@WebServlet("/CategoryServlet")
public class CategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	int id = 0;
       
    
    public CategoryServlet() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		
		if (request.getParameter("id") != null && isNumeric(request.getParameter("id"))) {
			id = Integer.parseInt(request.getParameter("id"));
		}
		
		Connection connection;
		try {
			connection = Main.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM categorie_connection WHERE category_id = " + id);
			ArrayList<Integer> articleIds = new ArrayList<Integer>();
			while (rs.next()) {
				articleIds.add(rs.getInt("article_id"));
			}

			setArticles(request, statement, articleIds);
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
		
		RequestDispatcher rd = request.getRequestDispatcher("/category.jsp");
		rd.forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
		doGet(request, response);
	}
	
	private void setArticles(HttpServletRequest request, Statement statement, ArrayList<Integer> articleIds) throws SQLException {
		String allArticles = "";
		for (int id : articleIds) {
			ResultSet rs = statement.executeQuery("SELECT * FROM articles WHERE article_id = " + id);
			if (rs.next()) {
				String articleHtml = "<div class=\"article\">\r\n" + 
						"		<h1 class=\"article-title\"><a class=\"article-title\" href=\"ArticleServlet?id=" + id + "\">" + rs.getString("title") + "</a></h1>\r\n";
				
				String text = rs.getString("article_text");
				if (text.length() > 128) {
					text = text.substring(0, 127);
				}
				
				articleHtml +=		"		<p class=\"article-content\">" + text + "</p>\r\n" + 
						"	</div>";
				allArticles += articleHtml;
			}
		}
		request.setAttribute("articles", allArticles);
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
