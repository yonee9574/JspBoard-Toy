<%@ page import="javax.security.auth.callback.ConfirmationCallback"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="notice.NoticeDAO"%>
<%@ page import="notice.Notice"%>
<%@ page import="java.util.ArrayList"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- 뷰포트 -->
<meta name="viewport" content="width=device-width" initial-scale="1">
<!-- 스타일시트 참조 -->
<link rel="stylesheet" href="css/bootstrap.css">
<title>게시판 프로젝트</title>
<style type="text/css">
a, a:hover {
	color: #000000;
	text-decoration: none;
}
</style>
</head>
<body>
	<%
		//로긴 한사람이면 userID라는 변수에 해당 아이디가 담기고 그렇지 않으면 null값
		String userID = null;
		if (session.getAttribute("userID") != null) {
			userID = (String) session.getAttribute("userID");
		}
		int pageNumber = 1; //기본 페이지 넘버

		//페이지 넘버값이 있을때
		if (request.getParameter("pageNumber") != null) {
			pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
		}
	%>
	<!-- 네비게이션 -->
	<nav class="navbar navbar-default">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
				aria-expaned="false">
				<span class="icon-bar"></span><span class="icon-bar"></span><span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="main.jsp">게시판 프로젝트</a>
			</div>
			<div class="collaspe navbar-collapse"
				id="#bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<li><a href="main.jsp">메인</a></li>
					<li class="active"><a href="notice.jsp">공지사항</a></li>
					<li><a href="bbs.jsp">게시판</a></li>
				</ul>

				<%
					//로그인 안된경우
					if (userID == null) {
				%>
				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">접속하기<span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="login.jsp">로그인</a></li>
							<li><a href="join.jsp">회원가입</a></li>
						</ul></li>
				</ul>
				<%
					//로그인 된경우
					} else {
				%>
				<ul class="nav navbar-nav navbar-right">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">회원관리<span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li><a href="logoutAction.jsp">로그아웃</a></li>
						</ul></li>
				</ul>
				<%
					}
				%>
			</div>
	
	</nav>
	<!-- 게시판 -->
	<div class="container">
		<div class="row">
			<table class="table table-striped"
				style="text-align: center; border: 1ps solid #dddddd">
				<thead>
					<tr>
						<th style="background-color: #eeeeee; text-align: center;">번호</th>
						<th style="background-color: #eeeeee; text-align: center;">제목</th>
						<th style="background-color: #eeeeee; text-align: center;">작성자</th>
						<th style="background-color: #eeeeee; text-align: center;">작성일</th>
					</tr>
				</thead>
				<tbody>
					<%
						NoticeDAO noticeDAO = new NoticeDAO();
						
						ArrayList<Notice> list = noticeDAO.getList(pageNumber);
						for (int i = 0; i < list.size(); i++) {
					%>
					<tr>
						<td><%=list.get(i).getNoticeID()%></td>
						<td><a href="Nview.jsp?noticeID=<%=list.get(i).getNoticeID()%>">
						<%=list.get(i).getNoticeTitle()%></a></td>
						<td><%=list.get(i).getUserID()%></td>
						<td><%=list.get(i).getNoticeDate() %></td>
						</tr>
					<%
						}
					%>
				</tbody>
			</table>
			<!-- 페이지 넘기기 -->
			<%
				if (pageNumber != 1) {
			%>
			<a href="notice.jsp?pageNumber=<%=pageNumber - 1%>"
				class="btn btn-success btn-arrow-left">이전</a>
			<%
				}
				if (noticeDAO.nextPage(pageNumber)) {
			%>
			<a href="notice.jsp?pageNumber=<%=pageNumber + 1%>"
				class="btn btn-success btn-arrow-left">다음</a>
			<%
				}
			%>
			<!-- 회원만 넘어가도록 -->
			<%
				//if logined userID라는 변수에 해당 아이다가 담기고 if not null
				if (session.getAttribute("userID") != null) {
			%>
			<a href="Nwrite.jsp" class="btn btn-primary pull-right" id="notice_write">글쓰기</a>
			<%
				} else {
			%>
			<button class="btn btn-primary pull-right"
				onclick="if(confirm('로그인 하세요'))location.href='login.jsp';"
				type="button">글쓰기</button>
			<%
				}
			%>

		</div>
	</div>
	<!--  애니매이션 담당 JQUERY -->
	<script src="http://code.jquery.com/jquery-3.1.1.min.js"></script>
	<!--  부트스트랩 JS -->
	<script src="js/bootstrap.js"></script>
	
	<script type="text/javascript">
		$(document).ready(function(){
			$('#notice_write').css('color', 'red');
		})
			
			/* if session.getAttribute("auth")==="0"{
				$("#notice_write").css("display", "block");
				
			} */
		function login_check(){
			if
		}
		
		
	</script>
</body>
</html>