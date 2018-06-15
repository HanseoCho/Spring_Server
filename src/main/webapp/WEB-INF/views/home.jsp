<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello Server!  
	<h1>Hello App Server</h1>
	<a href="login">로그인</a>
	<a href="user">회원가입</a>
	<a href="insert">글쓰기</a>
	<a href="bLits">글목록</a>	
</h1>
	<c:if test="${data.status == 1}">
		<br>${data.userName}님 환영합니다
	</c:if>
	<c:if test="${data.status == 0}">
		<br>아이디와 비밀번호를 확인해주세요
	</c:if>	
</body>
</html>
