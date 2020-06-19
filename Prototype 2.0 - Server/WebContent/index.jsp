<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
<%-- 0428주석 --%>


	메인페이지
	<form method="post" action="dbtest.jsp">

		<p>
			<input type="submit" value="DB 연결 됐냐?">
	</form>

	<form method="post" action="typetest1.jsp">

		<p>
			<input type="submit" value="초성">
	</form>
		<form method="post" action="typetest2.jsp">

		<p>
			<input type="submit" value="모음">
	</form>
		<form method="post" action="typetest3.jsp">

		<p>
			<input type="submit" value="종성">
	</form>
	
	</form>
		<form method="post" action="typetest4.jsp">

		<p>
			<input type="submit" value="특수기호/숫자">
	</form>
	
	</form>
		<form method="post" action="typetest5.jsp">

		<p>
			<input type="submit" value="약어">
	</form>
	
	<form method="post" action="wordtest.jsp">

		<p>
			<input type="submit" value="단어 넣은것들">
	</form>
	
	<form method="post" action="wordRankTest.jsp">

		<p>
			<input type="submit" value="오답 랭킹">
	</form>
	
	<form method="post" action="translateRankTest.jsp">

		<p>
			<input type="submit" value="번역 랭킹">
	</form>
	
	<form method="post" action="braile.jsp">

		<p>
			<input type="submit" value="웹사이트">
	</form>
</body>
</html>