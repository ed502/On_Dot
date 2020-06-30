package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
	// private Statement stmt2 = null;
	private ResultSet rs = null;
	// private ResultSet rs2 = null;
	private PreparedStatement ps = null;

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
		// ���ÿ��� ���� �� �� �и�
		String dot = request.getParameter("dot");

		String[] arr = new String[20];
		int num = dot.length() / 6;
		String[] data = new String[num];
		int[] type = new int[num];

		String[] arr2 = new String[10];
		//int num2 = dot.length() / 12;
		String[] data2 = new String[num-1];
		int[] type2 = new int[num-1];

		for (int i = 0; i < num + 1; i++) {
			if (i != num) {
				arr[i] = dot.substring(0 + (6 * i), 6 * (i + 1));
			} else {
				arr[i] = dot.substring(0 + (6 * i), dot.length());
			}
		}

		for (int i = 0; i < num; i++) {
			if (i != num-1) {
				arr2[i] = dot.substring(0 + (6 * i), 12 + (6 * i));
			} else {
				arr2[i] = dot.substring(0 + (6 * i), dot.length());
			}
		}

		// rlagkrwls.translate(dot);
		String result = "";
		String result2 = "";
		String query = "";
		
		try {
			conn = DBConnection.getConnection();
			stmt = conn.createStatement();

			for (int i = 0; i < num; i++) {
				query = "select * from initial_dots where dot = '" + arr[i] + "'" + "and id !=14 order by type desc, id desc  ";
				rs = stmt.executeQuery(query);
				while (rs.next()) {
					data[i] = rs.getString("word");
					type[i] = rs.getInt("type");

				}
			}
			for (int i = 0; i < num-1; i++) {
				query = "select * from initial_dots where dot = '" + arr2[i] + "'" + "and id !=14 order by type desc, id desc  ";

				ps = conn.prepareStatement(query);

				rs = ps.executeQuery();
				while (rs.next()) {
					data2[i] = rs.getString("word");
					type2[i] = rs.getInt("type");
					System.out.println("약어 발견  " + data2[i] + i);
					
				}
			}
			for(int i=0;i<num-1;i++) {
				if(data2[i]!=null) {
					data[i]=data2[i];
					type[i]=type2[i];
					data[i+1]="-";
					type[i+1]=-1;
				}
				if(type[i]==1&&type[i+1]==7) {
					System.out.println("정보 "+data[i+1]+"  "+data[i]+"  "+type[i+1]+"  "+type[i]);
					combineData.kim(data[i+1],data[i]);
					data[i]=combineData.getData();
					System.out.println("조합하니 이게나옴!! :  " + combineData.getData());
					type[i]=1;
					data[i+1]=null;
					type[i+1]=0;
				}
			}

			combineData.hak(data, type);
			result = combineData.getData();
			combineData = new CombineData();
			combineData.hak(data2, type2);
			result2 = combineData.getData();

			if (num == 1) {
				result = data[0];
			}
			if (num-1 == 1) {
				result2 = data2[0];
			}

			sql.translate(result);

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			// 6. ����� Statement ����
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

			// 7. Ŀ�ؼ� ����
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

			;
			dotCombineXML.append("</dot>");
			dotCombineXML.append(result2);
				

			dotCombineXML.append("</entry>");

			dotCombineXML.append("</Dots>");
			System.out.println(dotCombineXML.toString());

			// ����
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/xml; charset=utf-8");
			response.getWriter().println(dotCombineXML.toString());
		}

	}

}
