package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import launch.Main;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LoginServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");

		String login = request.getParameter("username");
		String pass = request.getParameter("userpass");
		
		try {
			Connection connection = Main.getConnection();
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM users WHERE login LIKE \'" + login + "\' AND password LIKE \'" + pass + "\'");
			System.out.println("chyba git");
			if (rs.next()) {
				HttpSession session = request.getSession();
				session.setAttribute("login", login);
				ServletContext context = getServletContext();
				RequestDispatcher rd = context.getRequestDispatcher("/IndexServlet");
				rd.forward(request, response);
			} else {
				Object data = "Login lub has³o s¹ nieprawid³owe";
				request.setAttribute("data", data);
				RequestDispatcher rd = request.getRequestDispatcher("/login.jsp");
				rd.forward(request, response);
			}
			statement.close();
			connection.close();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
