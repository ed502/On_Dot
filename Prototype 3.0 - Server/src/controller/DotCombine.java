package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class DotCombine
 */
@WebServlet("/DotCombine")
public class DotCombine extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DotCombine() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// doGet(request, response);

		String dot = request.getParameter("dot");
		System.out.println("Dot : " + dot);
		BrailleToHangul bth = new BrailleToHangul();
		bth.setResult(dot);
		bth.cutString();
		String result = bth.getResult();
		
		
		if (result != null) {
			StringBuffer dotCombineXML = new StringBuffer(2048);
			dotCombineXML.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
			dotCombineXML.append("<Dots>");
			dotCombineXML.append("<entry>");
			dotCombineXML.append("<dot>");
			dotCombineXML.append(result);
			dotCombineXML.append("</dot>");
			dotCombineXML.append("</entry>");
			dotCombineXML.append("</Dots>");

			System.out.println(dotCombineXML.toString());

			response.setCharacterEncoding("utf-8");
			response.setContentType("text/xml; charset=utf-8");
			response.getWriter().println(dotCombineXML.toString());
		}
		

	}

}