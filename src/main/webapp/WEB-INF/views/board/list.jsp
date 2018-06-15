<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="/Spring_Server/webjars/jquery/3.2.1/dist/jquery.min.js"></script>
<style>
ul {
list-style: none;
}
</style>
<script>
	$(function(){
		$.ajax({type:"post",url:"bbld"})
		.done(function(data){
			var d= JSON.parse(data);
			console.log(d);
			var list = d.list;
			$("ul").empty();
			for(var i=0;i<list.length;i++){
				var html = "<li>"+ list[i].boardNo + "/<a href='/Spring_Server/bSelect?boardNo="+list[i].boardNo+"'>" + list[i].boardTitle +"</a></li>";
				$("ul").append(html);
			}
		});
	});
</script>
</head>
<body>
	<h1>리스트</h1>
	<ul></ul>
</body>
</html>