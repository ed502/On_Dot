<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
	<%@ page import="java.sql.DriverManager"%>
	<%@ page import="java.sql.Connection"%>
	<%@ page import="java.sql.PreparedStatement"%>
	<%@ page import="java.sql.SQLException"%>
	<%@ page import="database.DBConnection"%>
	<%
      request.setCharacterEncoding("euc-kr");

      Connection conn = null;

      PreparedStatement pstmt = null;

      

      String id = request.getParameter("id");
      Class.forName("com.mysql.jdbc.Driver");

       
      try {

         //02 ܰ  :DB    (Connection)    
         conn = DBConnection.getConnection();
         //02 ܰ  :DB    (Connection)  
         //03 ܰ  :Query            statement  Ǵ  prepareStatement  ü         
         pstmt = conn.prepareStatement(
               "update initial_dots set count = count +1 where id = "+id);
         //update into tb_member values(id001,pw001,      ,ȫ01,email01);


         //03 ܰ  :Query            statement  Ǵ  prepareStatement  ü       
          pstmt.executeUpdate();

         //04 ܰ  :Query         


         //04 ܰ  :Query       

         //05 ܰ  :Query           (update               ɴܰ )

      } finally {

         //06 ܰ  :statement  Ǵ  prepareStatement  ü     (close())

         if (pstmt != null)
            try {
               pstmt.close();
            } catch (SQLException ex) {
            }

         //07 ܰ  :Connection   ü     (close())

         if (conn != null)
            try {
               conn.close();
            } catch (SQLException ex) {
            }

      }
   %>

</body>
</html>