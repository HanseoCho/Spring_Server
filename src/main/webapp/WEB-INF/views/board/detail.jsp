<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>글작성</title>
<script src="/Spring_Server/webjars/jquery/3.2.1/dist/jquery.min.js"></script>
<script>
	$(function(){
		var boardNo = "${param.boardNo}";
		if(boardNo == ""){
			alert("비정상적인 접근입니다.");
			$(location).attr("href","/Spring_Server");
		}
		else{
		console.log(boardNo);
		$.ajax({type:"post",url:"http://localhost/Spring_Server/bld",data:{"boardNo":boardNo}})
		.done(function(data) {
			var d = JSON.parse(data);
			console.log(d);
			var boardData = d.boardData;
			var fileData = d.fileData;
			boardHTML(boardData)
			filesHTML(fileData)
			})
		}
	});
	
	function boardHTML(data) {
		console.log("boardHTML",data);
		var title = data.boardTitle;
		var contents = data.boardContents;
		$("#title").text(title);
		$("#contents").text(contents);
	}
	function filesHTML(data){
		console.log("fileHTML",data);
		$("#files").empty();
		for(var i=0;i<data.length;i++){
			var fileURL = data[i].fileURL;
			var html = "<img src='"+fileURL+"'><br>";
			$("#files").append(html);
		}
	}
	
</script>
</head>
<body>
	<h1>목록</h1>
	<h1 id="title"></h1>
	<p id="contents"></p>
	<div id="files"></div>
	<div>
		<a href="http://localhost/Spring_Server/bLits">목록으로</a>
		<a href="bUpdate?boardNo=${param.boardNo}">수정</a>
		<a href="/Spring_Server/dlb?boardNo=${param.boardNo}">삭제</a>
	</div>
</body>
</html>