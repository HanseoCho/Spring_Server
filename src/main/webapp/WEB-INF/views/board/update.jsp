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
		var delData = [];		
		var boardNo = "${param.boardNo}";
		if(boardNo == ""){
			alert("누구세요?");
			$(location).attr("href","/Spring_Server");			
		}
		else{
			console.log(boardNo);
			$.ajax({type:"post",url:"bld",data:{"boardNo":boardNo}})
			.done(function(data) {
				var d = JSON.parse(data);
				console.log(d);
				var boardData = d.boardData;
				var fileData = d.fileData;
				boardHTML(boardData)
				filesHTML(fileData)
				})
			}		
		
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
				console.log("3")
				update(d);
			});
		});
	
		function update(d){
			console.log("d.upload : "+d.upload);
			$.ajax({
				  type : "post",
				  url : "bud",
				  data : {
					  "boardNo" : boardNo,
					  "boardTitle" : $("form input").eq(0).val(),
					  "boardContents" : $("form input").eq(1).val(),
					  "data" : JSON.stringify(d.upload),
					  "delData" : JSON.stringify(delData)
				  }
			  }).done(function(data){
				  var d = JSON.parse(data)
				  alert(d.msg);
				  if(d.status == 1){
					location.href = "bSelect?boardNo=" + d.boardNo;
				  }
			  });
		}
		
		function boardHTML(data) {
			console.log("boardHTML",data);
			var title = data.boardTitle;
			var contents = data.boardContents;
			$("form input:text").eq(0).val(title);
			$("form input:text").eq(1).val(contents);
			
		}
		function filesHTML(data){
			console.log("fileHTML",data);
			$("#files").empty();
			for(var i=0;i<data.length;i++){
				var fileURL = data[i].fileURL;
				var html = "<img src='"+fileURL+"' width='50' height='50'><br>";
				$("#files").append(html);				
			}
			$("img").on("click", function(){
				var index = $("img").index(this);
				var fileNo = data[index].fileNo;
				delData.push({"fileNo" : fileNo});
				data.splice(index, 1);
				$("img").eq(index).remove();
			});			
		}
	});
	
	
</script>
</head>
<body>
	<h1>글수정!</h1>
	<form action="" enctype="muiltpart/form-data" method="post">
		<input type="text" name="boardTitle" placeholder="제목을 입력해주세요."><br>
		<input type="text" name="boardContents" placeholder="내용을 입력하세요."><br>
		<input type="file" name="file" multiple="multiple"><br>
		<input type="submit" value="수정">
		<div id="files"></div>
	</form>
</body>
</html>