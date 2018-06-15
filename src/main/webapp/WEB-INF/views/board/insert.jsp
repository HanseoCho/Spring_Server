<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="/Spring_Server/webjars/jquery/3.2.1/dist/jquery.min.js"></script>
<script>
	$(function(){
		$("form").on("submit",function(e){
			e.preventDefault();
			console.log("1");	
			$.ajax({
				type:"post",
				url:"http://FileServer/FileUpload/jhs",
				data:new FormData($(this)[0]),
				cache: false,
				contentType: false,
				processData: false})
			.done(function(data){
				console.log("2");
				var d = JSON.parse(data);
				//jQuery.ajaxSettings.traditional = true;

				var jd = JSON.stringify(d.upload)
				jQuery.ajaxSettings.traditional = true;				
				$.ajax({
					url:"/Spring_Server/bid",
					data:{"boardTitle":$("form input:text").eq(0).val(),"boardContents":$("form input:text").eq(1).val(),"data":jd},
					type:"post"					
				}).done(function(data){
					var d = JSON.parse(data);
					alert(d.msg)
					if(d.status==1){
						location.href="/Spring_Server/bSelect?boardNo="+d.boardNo;
					}
					
				});
			});
		});
	});
</script>
</head>
<body>
	<h1>글작성!</h1>
	<form action="" enctype="muiltpart/form-data" method="post">
		<input type="text" name="boardTitle" placeholder="제목을 입력해주세요."><br>
		<input type="text" name="boardContents" placeholder="내용을 입력하세요."><br>
		<input type="file" name="file" multiple="multiple"><br>
		<input type="submit" value="작성">
	</form>
</body>
</html>