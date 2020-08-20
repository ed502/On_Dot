package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
		// ÿ и
		String dot = request.getParameter("dot");
		System.out.println("Dot : " + dot);
		BrailleToHangul bth = new BrailleToHangul();
		bth.setResult(dot);
		bth.cutString();
		// String result = bth.getResult();

		/*
		 * if (result != null) { StringBuffer dotCombineXML = new StringBuffer(2048);
		 * dotCombineXML.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		 * dotCombineXML.append("<Dots>"); dotCombineXML.append("<entry>");
		 * dotCombineXML.append("<dot>"); dotCombineXML.append(result);
		 * dotCombineXML.append("</dot>"); dotCombineXML.append("</entry>");
		 * dotCombineXML.append("</Dots>");
		 * 
		 * System.out.println(dotCombineXML.toString());
		 * 
		 * response.setCharacterEncoding("utf-8");
		 * response.setContentType("text/xml; charset=utf-8");
		 * response.getWriter().println(dotCombineXML.toString()); }
		 */

		/*
		 * for (int i = 0; i < cnt; i++) { int lastindex = (i + 1) * 6;
		 * system.out.println("lastindex : " + lastindex); if (i != cnt-1) { sixdot[i] =
		 * dot.substring(6 * i, lastindex); } else { sixdot[i] = dot.substring(6 * i); }
		 * } for(string s : sixdot) { system.out.println("sixdot : " + s); }
		 * 
		 * string[] twelvedot = new string[10]; // 120 길이 / 12 = 10세트 , 마지막에 6개가 오는 경우가
		 * 존재 for(int i=0; i<sixdot.length; i++) { if(i+1 != sixdot.length && sixdot[i]
		 * != null) { twelvedot[i] = sixdot[i] + sixdot[i+1]; } } for(string s :
		 * twelvedot) { system.out.println("twelvedot : " + s); }
		 * 
		 * 
		 * Hot's 코드 String[] arr = new String[20]; int num = dot.length() / 6; String[]
		 * data = new String[num]; int[] type = new int[num];
		 * 
		 * String[] arr2 = new String[10]; // int num2 = dot.length() / 12; String[]
		 * data2 = new String[10]; int[] type2 = new int[10];
		 * 
		 * for (int i = 0; i < num; i++) { if (i != num - 1) { arr2[i] = dot.substring(0
		 * + (6 * i), 12 + (6 * i)); } else { arr2[i] = dot.substring(0 + (6 * i),
		 * dot.length()); } }
		 * 
		 * for (String s : arr2) { System.out.println("arr2 : " + s); }
		 * 
		 * // rlagkrwls.translate(dot); String[] resultDot = new String[10]; Boolean[]
		 * stateDot = new Boolean[10]; for (int i = 0; i < stateDot.length; i++) {
		 * stateDot[i] = false; } String result = ""; String result2 = ""; String query
		 * = "";
		 * 
		 * try { conn = DBConnection.getConnection(); stmt = conn.createStatement();
		 * System.out.println("data2 len : " + data2.length);
		 * System.out.println("type2 len : " + type2.length);
		 * 
		 * for (int i = 0; i < arr2.length - 1; i++) { if (arr2[i] != null &&
		 * arr2[i].length() == 12) { query = "select * from initial_dots where dot = '"
		 * + arr2[i] + "' and (type = 5 or type = 3)"; ps =
		 * conn.prepareStatement(query); rs = stmt.executeQuery(query); while
		 * (rs.next()) { data2[i] = rs.getString("word"); type2[i] = rs.getInt("type");
		 * resultDot[i] = data2[i]; if(type2[i] == 3) { stateDot[i] = false; stateDot[i
		 * + 1] = true; }else { stateDot[i] = true; stateDot[i + 1] = true; }
		 * 
		 * System.out.println("data2[" + i + "] : " + data2[i] + " type2[" + i + "] : "
		 * + type2[i]); } } }
		 * 
		 * for (int i = 0; i < resultDot.length; i++) { System.out.println("resultDot["
		 * + i + "] : " + resultDot[i] + " stateDot[" + i + "] : " + stateDot[i]); }
		 * 
		 * for (int i = 0; i < num; i++) { int lastindex = (i + 1) * 6;
		 * 
		 * if (stateDot[i] == true || resultDot[i] != null) { continue; }
		 * 
		 * if (i != num - 1) { arr[i] = dot.substring(6 * i, lastindex); } else { arr[i]
		 * = dot.substring(6 * i); } }
		 * 
		 * for (String s : arr) { System.out.println("arr : " + s); }
		 * 
		 * for (int i = 0; i < arr.length; i++) { if (arr[i] != null) { query =
		 * "select * from initial_dots where dot = '" + arr[i] + "'"; rs =
		 * stmt.executeQuery(query); while (rs.next()) { if (rs.getInt("type") != 5 &&
		 * rs.getInt("type") != 4) { data[i] = rs.getString("word"); type[i] =
		 * rs.getInt("type"); resultDot[i] = data[i]; if(type[i] == 7) { stateDot[i] =
		 * true; }
		 * 
		 * System.out.println("data[" + i + "] : " + data[i] + " type[" + i + "] : " +
		 * type[i]); }
		 * 
		 * } } // 약어가 아닌 것들이 남았으니 초성인지 모음인지 종성인지 찾아서 배열에 넣어 그리고 합쳐 }
		 * 
		 * for (int i = 0; i < resultDot.length; i++) { System.out.println("resultDot["
		 * + i + "] : " + resultDot[i] + " stateDot[" + i + "] : " + stateDot[i]); }
		 * 
		 * //***************************************************************************
		 * ************ // 여기까지가 resultDot[] 넣는 부분
		 * 
		 * int[] typeDot = new int[10];
		 * 
		 * for (int i = 0; i < data.length; i++) {
		 * 
		 * if(data[i] != null) { typeDot[i] = type[i]; System.out.println("data[" + i +
		 * "] : " + data[i] + " type[" + i + "] : " + type[i]); }
		 * 
		 * 
		 * 
		 * } for(int i=0; i<data2.length; i++) { if(data2[i] != null) { typeDot[i] =
		 * type2[i]; System.out.println("data2[" + i + "] : " + data2[i] + " type2[" + i
		 * + "] : " + type2[i]);
		 * 
		 * }
		 * 
		 * 
		 * }
		 * 
		 * for(int i=0;i<typeDot.length;i++) { System.out.println("typeDot[" + i +
		 * "] : " +typeDot[i]); }
		 * 
		 * 
		 * 
		 * 
		 * 
		 * // for (int i = 0; i < num; i++) { // query =
		 * "select * from initial_dots where dot = '" + arr[i] + "'" // +
		 * "and id !=14 order by type desc, id desc  "; // rs =
		 * stmt.executeQuery(query); // while (rs.next()) { // data[i] =
		 * rs.getString("word"); // type[i] = rs.getInt("type"); //
		 * System.out.println("data["+ i + "] : " + data[i] + "type["+ i + "] : "+
		 * type[i]); // } // } // for (int i = 0; i < num - 1; i++) { //
		 * System.out.println("여기 되나?"); // query =
		 * "select * from initial_dots where dot = '" + arr2[i] + "'" // +
		 * "and id !=14 order by type desc, id desc  "; // ps =
		 * conn.prepareStatement(query); // rs = ps.executeQuery(); // while (rs.next())
		 * { // System.out.println("여기 되나?"); // data2[i] = rs.getString("word"); //
		 * type2[i] = rs.getInt("type"); // System.out.println("data2["+ i + "] : " +
		 * data2[i] + "type2["+ i + "] : "+ type2[i]); // System.out.println("약어 발견  " +
		 * data2[i] + i); // } // } for (int i = 0; i < num - 1; i++) { if (data2[i] !=
		 * null) { // 약어가 없으면 data[i] = data2[i]; type[i] = type2[i]; data[i + 1] = "-";
		 * type[i + 1] = -1; } if (type[i] == 1 && type[i + 1] == 7) {
		 * System.out.println("정보 " + data[i + 1] + "  " + data[i] + "  " + type[i + 1]
		 * + "  " + type[i]); combineData.kim(data[i + 1], data[i]); data[i] =
		 * combineData.getData(); System.out.println("조합하니 이게나옴!! :  " +
		 * combineData.getData()); type[i] = 1; data[i + 1] = null; type[i + 1] = 0; } }
		 * 
		 * // combineData.hak(data, type); // result = combineData.getData();
		 * combineData.hak(resultDot, typeDot, stateDot); result =
		 * combineData.getData();
		 * 
		 * // combineData = new CombineData(); // combineData.hak(data2, type2); //
		 * result2 = combineData.getData();
		 * 
		 * 
		 * if (num == 1) { result = data[0]; }
		 * 
		 * if (num - 1 == 1) { result2 = data2[0]; }
		 * 
		 * sql.translate(result);
		 * 
		 * } catch (SQLException ex) { ex.printStackTrace(); } finally { // 6. Statement
		 * if (rs != null) try { rs.close(); } catch (SQLException ex) { } if (stmt !=
		 * null) try { stmt.close(); } catch (SQLException ex) { }
		 * 
		 * // 7. Ŀ ؼ if (conn != null) try { conn.close(); } catch (SQLException ex) { }
		 * }
		 */

	}

}