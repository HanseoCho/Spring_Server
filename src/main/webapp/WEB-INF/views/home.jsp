<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>  
	<h1>Hello App Server</h1>
	${sessionScope.user}<br>
	<c:choose>
		<c:when test="${sessionScope.user.status == 1}">
			<br>${sessionScope.user.userName}님 환영합니다
			<a href="logout">로그아웃</a>
		</c:when>
		<c:when test="${sessionScope.user.status == 0}">
			<a href="login">로그인</a>
			<a href="user">회원가입</a>		
			<br>아이디와 비밀번호를 확인해주세요
		</c:when>
		<c:otherwise>
			<a href="login">로그인</a>
			<a href="user">회원가입</a>
		</c:otherwise>
	</c:choose><br>
		<a href="insert">글쓰기</a>
		<a href="bLits">글목록</a>	
<%-- 	<c:if test="${sessionScope.user.status != 1}">	
		<a href="login">로그인</a>
		<a href="user">회원가입</a>
	</c:if> --%>


<%-- 	<c:if test="${sessionScope.user.status == 1}">
			<br>${sessionScope.user.userName}님 환영합니다
			<a href="logout">로그아웃</a>
	</c:if>
	<c:if test="${sessionScope.user.status == 0}">	
		<br>아이디와 비밀번호를 확인해주세요
	</c:if> --%>	
</body>
</html>
