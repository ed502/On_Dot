
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
ȸ�� ����Ʈ
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
		//JDBC ���α׷� ����
		//01�ܰ� :����̹� �ε� ����
		Class.forName("com.mysql.jdbc.Driver");
		//01�ܰ� :����̹� �ε� ��
		try {
			//02�ܰ� :DB����(Connection)����
			conn = DBConnection.getConnection();
			//02�ܰ� :DB����(Connection)��
			
			// DB ������ ���� �Ǿ����� �ȵǾ����� �Ǵ��϶�
			if (conn != null) {
				out.println("01 DB���� ����");
			} else {
				out.println("02 DB���� ����");
			}
			//03�ܰ� :Query������ ���� statement �Ǵ� prepareStatement��ü���� ����
			stmt = conn.createStatement();
			//04�ܰ� :Query���� ����
			String query = "select * from initial_dots where type =1";
			rs = stmt.executeQuery(query);
			//04�ܰ� :Query���� ��
			//05�ܰ� :Query������ ���
			// �ѹ� ȣ��Ǹ� ������ ��������. ��ü ����Ʈ�� �����ٶ��� �ּ�ó�� �ؾ� ��ü ����Ʈ�� ���´�.
			//             System.out.println(rs.next() + "<-- rs.next() m_list.jsp");
			//---   select���� ���ؼ� ��� ȸ�� ��� �����ͼ� ���پ� (���ڵ�(record) or �ο�(row))�����ش� ���� 
			while (rs.next()) {
	%>

	<tr>
		<td><%=rs.getString("id")%></td>
		<td><%=rs.getString("word")%></td>
		<td><%=rs.getString("dot")%></td>
		<td><%=rs.getString("type")%></td>


	</tr>

	<%
		/* out.println(rs.getString("m_id") + "<-- m_id �ʵ�=�÷� �� in tb_member���̺� <br>");
				    out.println(rs.getString("m_pw") + "<-- m_pw �ʵ�=�÷� �� in tb_member���̺� <br>");
				    out.println(rs.getString("m_level") + "<-- m_level �ʵ�=�÷� �� in tb_member���̺� <br>");
				    out.println(rs.getString("m_name") + "<-- m_name �ʵ�=�÷� �� in tb_member���̺� <br>");
				    out.println(rs.getString("m_email") + "<-- m_email �ʵ�=�÷� �� in tb_member���̺� <br><br>"); */
			}
			//---   select���� ���ؼ� ��� ȸ�� ��� �����ͼ� ���پ� (���ڵ�(record) or �ο�(row))�����ش� ��

		} catch (SQLException ex) {
			out.println(ex.getMessage());
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
	%>
       <form method="post" action="index.jsp">

		<p>
			<input type="submit" value="Home">
	</form>
</table>