package servlet;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import launch.Main;


@WebServlet("/AddArticleServlet")
public class AddArticleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public AddArticleServlet() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");

		String title = request.getParameter("title");
		String categories = request.getParameter("categories");
		String categoriesArray[] = categories.replaceAll(" ", "").toLowerCase().split(",");
		String fullText = request.getParameter("full-text");
		
		Connection connection;
		try {
			connection = Main.getConnection();
			Statement statement = connection.createStatement();
			ResultSet articleRs = statement.executeQuery("INSERT INTO articles (title, article_text) VALUES (\'" + title +"\', \'" + fullText + "\')  RETURNING article_id");
			int articleId = 0;
			if (articleRs.next())
				articleId = articleRs.getInt(1); 
					
			for (int i = 0; i < categoriesArray.length; i++) {
				ResultSet rs = statement.executeQuery("SELECT * FROM categories WHERE name LIKE \'" + categoriesArray[i] + "\'");
				int categoryId = 0;
				if (rs.next()) {
					categoryId = rs.getInt("category_id");
				} else {
					ResultSet categoryRs = statement.executeQuery("INSERT INTO categories (name) VALUES (\'" + categoriesArray[i] + "\')  RETURNING category_id");
					if (categoryRs.next())
						categoryId = categoryRs.getInt(1);
				}
				statement.executeUpdate("INSERT INTO categorie_connection (article_id, category_id) VALUES (" + articleId + ", " + categoryId + ")");
			}
			statement.close();
			connection.close();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RequestDispatcher rd = request.getRequestDispatcher("/IndexServlet");
		rd.forward(request, response);
	}

}
