<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="database.DBConnection" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>

	<%
	//post 12개씩 자름
	 	request.setCharacterEncoding("euc-kr");
		String dot = request.getParameter("dot");
		String[] arr = new String[10];
		//out.println(dot.substring(0,9));
		out.println(dot.length());
		String store;
		int num = dot.length() / 12;
		for (int i = 0; i < num ; i++) {
			if (i != num) {
				arr[i] = dot.substring(0 + (12 * i), 12 * (i + 1));
			}
			else{
				arr[i] = dot.substring(0 + (12 * i), dot.length());
			}
		}
		for (int i = 0; i < num ; i++) {
			store = arr[i];
			out.println(store);
		} 
	%>
	
	
</body>
</html>