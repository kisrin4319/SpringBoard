<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>글 보기 : ${board.subject }</title>
<link href="<%=request.getContextPath()%>/css/board.css" rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
	function errCodeCheck(){
		var errCode = <%=request.getParameter("errCode")%>;
		if(errCode != null || errCode != ""){
			switch (errCode) {
			case 1:
				alert("잘못된 접근 경로입니다!");
				break;
			case 2:
				alert("댓글이 있어 글을 삭제하실 수 없습니다!");
				break;
			}
		}		
	}
	
	function commentDelete(commentIdx, boardIdx){
		if(confirm("선택하신 댓글을 삭제하시겠습니까?")){
			location.href = "commentDelete.do?idx=" + commentIdx + "&boardidx=" + boardIdx;
		}		
	}
	
	function moveAction(where){
		switch (where) {
		case 1:
			if(confirm("글을 삭제하시겠습니까?")){
				location.href ="delete.do?idx=${board.idx}";
			}
			break;

		case 2:
			if(confirm("글을 수정하시겠습니까?")){
				location.href = "modify.do?idx=${board.idx}";
			}
			break;
			
		case 3:
			location.href = "list.do";			
			break;
		
		case 4:
			if(confirm("글을 추천하시겠습니까?")){
				location.href = "recommend.do?idx=${board.idx}";
			}
			break;
		case 5:
			if(confirm("첨부파일을 받으시겠습니까?")){
				location.href = "download?fileName=${board.fileName}";
			}
			break;
	}
	}
</script>
</head>
<body onload="errCodeCheck()">
<div class = "wrapper">
	<table class = "boardView">
		<tr>
			<td colspan="4"><h3>${board.subject }</h3></td>
		</tr>
		<tr>
			<th>작성자</th>
			<th>조회수</th>
			<th>추천수</th>
			<th>작성일</th>
		</tr>
		<tr>
			<td>${board.writer }</td>
			<td>${board.hitcount }</td>
			<td>${board.recommendcount }</td>
			<td>${board.writeDate }</td>
		</tr>
		
		<tr>
			<th colspan="4">내용</th>
		</tr>
		<c:if test="${board.fileName!=null }">
			<tr>
				<td colspan="4" align="left">
					<span class="date">첨부파일:&nbsp;
						<a href="download.do?fileName=${board.fileName}" target ="_blank">${board.fileName}</a>
					</span>
				</td>
			</tr>
		</c:if>
		
		<tr>
			<td colspan="4" align="left"><p>${board.content }</p><br /><br /></td>
		</tr>
	</table>
	<table class = "commentView">
		<tr>
			<th colspan="2">댓글</th>
		</tr>
		<c:forEach var = "comment" items = "${commentList}">
			<tr>
				<td class = "writer">
					<p>${comment.writer }
						<c:if test="${comment.writerId == member_id }">
							<br /><a onclick="commentDelete(${comment.idx},${board.idx})"><small>댓글 삭제</small></a>
						</c:if>					
					</p>				
				</td>
				<td class = "content" align="left">
					<span class = "date">${comment.writeDate }</span>
					<p>${comment.content }</p>
				</td>
			</tr>		
		</c:forEach>
		<tr>
			<td class = "write"><strong>댓글 쓰기</strong></td>
			<td class = "content">
				<form action="commentWrite.do" method="post">
					<input type = "hidden" id = "writer" name = "writer" value = "${member_name }" />
					<input type = "hidden" id = "writerId" name = "writerId" value = "${member_id }" />
					<input type = "hidden" id = "boardidx" name = "boardidx" value = "${board.idx}" />
					<textarea id = "content" name = "content" class = "commentForm"></textarea>
					<input type="submit" value ="확인" class = "commentBt" />
				</form>
			</td>
		</tr>
	</table>
	<br />
	<c:choose>
		<c:when test="${board.writerId == member_id }">
			<input type ="button" value ="삭제" class = "writeBt" onclick="moveAction(1)" />
			<input type ="button" value ="수정" class = "writeBt" onclick="moveAction(2)" />
			<input type ="button" value ="목록" class = "writeBt" onclick="moveAction(3)" />
		</c:when>
		<c:otherwise>
			<input type ="button" value ="추천" class = "writeBt" onclick="moveAction(4)" />
			<input type ="button" value ="목록" class = "writeBt" onclick="moveAction(3)" />		
		</c:otherwise>	
	</c:choose>
</div>
</body>
</html>