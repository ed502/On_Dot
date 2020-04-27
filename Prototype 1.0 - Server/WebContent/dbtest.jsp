<%@ page language="java" contentType="text/html; charset=UTF-8"
       pageEncoding="UTF-8" import="java.sql.*"%>
       <%@ page import ="database.DBConnection" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>DB Connection Test</title>
</head>
<body>

       <%
             
              Connection conn;
              Statement stmt;
              PreparedStatement ps;
              ResultSet rs;
              try {
                     Class.forName("com.mysql.jdbc.Driver");
                     conn = DBConnection.getConnection();
                     stmt = conn.createStatement();
                        
                    /* SQL 처리 코드 추가 부분 */

                     conn.close();
                     out.println("On_dot 김문송이 화이팅 0428");
              } catch (Exception e) {
                     out.println(e.getMessage());
              }
       %>
       <form method="post" action="index.jsp">

		<p>
			<input type="submit" value="Home">
	</form>
</body>
</html>