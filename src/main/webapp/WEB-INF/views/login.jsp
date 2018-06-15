<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>로그인</h1>
	<form action="/Spring_Server/userSelect" method="post"> 
		<input type="email" name="userEmail" placeholder="이메일을 입력해주세요" maxlength="100" required="required"><br>
		<input type="password" name="userPassword" placeholder="비밀번호를 입력해주세요" maxlength="10" required="required" min="8"><br>
		<input type="submit" value="로그인">
	</form>
</body>
</html>