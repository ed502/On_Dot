
<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import="database.DBConnection" %>
<%=request.getRequestURI()%>
<br>
회원 리스트
<br>
<table width="100%" border="1">
	<tr>
		<td>id</td>
		<td>word</td>
		<td>dot</td>
		<td>type</td>
	</tr>

	<%
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		//JDBC 프로그램 순서
		//01단계 :드라이버 로딩 시작
		Class.forName("com.mysql.jdbc.Driver");
		//01단계 :드라이버 로딩 끝
		try {
			//02단계 :DB연결(Connection)시작
			conn = DBConnection.getConnection();
			//02단계 :DB연결(Connection)끝
			
			// DB 연결이 성공 되었는지 안되었는지 판단하라
			if (conn != null) {
				out.println("01 DB연결 성공");
			} else {
				out.println("02 DB연결 실패");
			}
			//03단계 :Query실행을 위한 statement 또는 prepareStatement객체생성 시작
			stmt = conn.createStatement();
			//04단계 :Query실행 시작
			String query = "select * from initial_dots where type =1";
			rs = stmt.executeQuery(query);
			//04단계 :Query실행 끝
			//05단계 :Query실행결과 사용
			// 한번 호출되면 밑으로 내려간다. 전체 리스트를 보여줄때는 주석처리 해야 전체 리스트가 나온다.
			//             System.out.println(rs.next() + "<-- rs.next() m_list.jsp");
			//---   select문장 통해서 모든 회원 목록 가져와서 한줄씩 (레코드(record) or 로우(row))보여준다 시작 
			while (rs.next()) {
	%>

	<tr>
		<td><%=rs.getString("id")%></td>
		<td><%=rs.getString("word")%></td>
		<td><%=rs.getString("dot")%></td>
		<td><%=rs.getString("type")%></td>


	</tr>

	<%
		/* out.println(rs.getString("m_id") + "<-- m_id 필드=컬럼 값 in tb_member테이블 <br>");
				    out.println(rs.getString("m_pw") + "<-- m_pw 필드=컬럼 값 in tb_member테이블 <br>");
				    out.println(rs.getString("m_level") + "<-- m_level 필드=컬럼 값 in tb_member테이블 <br>");
				    out.println(rs.getString("m_name") + "<-- m_name 필드=컬럼 값 in tb_member테이블 <br>");
				    out.println(rs.getString("m_email") + "<-- m_email 필드=컬럼 값 in tb_member테이블 <br><br>"); */
			}
			//---   select문장 통해서 모든 회원 목록 가져와서 한줄씩 (레코드(record) or 로우(row))보여준다 끝

		} catch (SQLException ex) {
			out.println(ex.getMessage());
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
	%>
       <form method="post" action="index.jsp">

		<p>
			<input type="submit" value="Home">
	</form>
</table>