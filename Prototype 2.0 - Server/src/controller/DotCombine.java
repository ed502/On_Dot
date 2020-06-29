package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DBConnection;
import model.DotVO;

/**
 * Servlet implementation class DotCombine
 */
@WebServlet("/DotCombine")
public class DotCombine extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private CombineData combineData = null;
	private Sql sql = null;
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;

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

		combineData = new CombineData();
		sql = new Sql();
		// 어플에서 받은 값 및 분리
		String dot = request.getParameter("dot");

		String[] arr = new String[10];
		int num = dot.length() / 12;
		String[] data = new String[num];
		int[] type = new int[num];
		for (int i = 0; i < num + 1; i++) {
			if (i != num) {
				arr[i] = dot.substring(0 + (12 * i), 12 * (i + 1));
			} else {
				arr[i] = dot.substring(0 + (12 * i), dot.length());
			}
		}

		String result = "";
		try {
			conn = DBConnection.getConnection();
			stmt = conn.createStatement();
			for (int i = 0; i < num; i++) {
				String query = "select * from translate_data where dot = '" + arr[i] + "'";
				rs = stmt.executeQuery(query);
				while (rs.next()) {
					data[i] = rs.getString("word");
					type[i] = rs.getInt("type");
				}

			}

			combineData.hak(data, type);

			result = combineData.getData();

			if (num == 1) {
				result = data[0];
			}
			System.out.println("result 넘겨");
			sql.translate(result);
			

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			// 6. 사용한 Statement 종료
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException ex) {
				}

			// 7. 커넥션 종료
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}
		}

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

			// 응답
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/xml; charset=utf-8");
			response.getWriter().println(dotCombineXML.toString());
		}

	}

}
