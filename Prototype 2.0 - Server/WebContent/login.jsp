<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <% request.setCharacterEncoding("UTF-8"); %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>login.jsp</title>
</head>
<body>
	<%
	String userid = request.getParameter("userid");
	String pw = request.getParameter("pw");
	out.println("[" + userid + "]");
	
	if(userid!=null&& userid.equals("admin")){
		if(pw!=null&& pw.equals("qnghkf1324")){
		session.setAttribute("userid",userid);
		session.setAttribute("pw",pw);
		
		response.sendRedirect("wrongRanking_admin.jsp");
		}
	}
	else
		response.sendRedirect("login.html");
	
	%>
</body>
</html>