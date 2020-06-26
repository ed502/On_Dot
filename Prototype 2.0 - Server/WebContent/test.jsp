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
<%@ page import="database.DBConnection" %>
	<%
		request.setCharacterEncoding("euc-kr");

		Connection conn = null;

		PreparedStatement pstmt = null;

		//화면에서 입력자료 받아서 콘솔창에 출력 시작

		String id = request.getParameter("id");
		Class.forName("com.mysql.jdbc.Driver");

		//01단계 :드라이버 로딩 끝
// 사용하는거고 이게 오답 count 하는거
		try {

			//02단계 :DB연결(Connection)시작
			conn = DBConnection.getConnection();
			//02단계 :DB연결(Connection)끝
			//03단계 :Query실행을 위한 statement 또는 prepareStatement객체생성 시작
			pstmt = conn.prepareStatement(
					"update initial_dots set count = count +1 where id = "+id);
			//update into tb_member values(id001,pw001,관리자,홍01,email01);


			//03단계 :Query실행을 위한 statement 또는 prepareStatement객체생성 끝
 			pstmt.executeUpdate();

			//04단계 :Query실행 시작


			//04단계 :Query실행 끝

			//05단계 :Query실행결과 사용 (update의 경우 생략 가능단계)

		} finally {

			//06단계 :statement 또는 prepareStatement객체 종료(close())

			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException ex) {
				}

			//07단계 :Connection 객체 종료(close())

			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				}

		}
	%>

</body>
</html>