<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="notice.NoticeDAO"%>
<!-- noticedao의 클래스 가져옴 -->
<%@ page import="java.io.PrintWriter"%>
<!-- 자바 클래스 사용 -->
<%
	request.setCharacterEncoding("UTF-8");
	response.setContentType("text/html; charset=UTF-8"); //set으로쓰는습관들이세오.
%>
<!-- 한명의 회원정보를 담는 user클래스를 자바 빈즈로 사용 / scope:페이지 현재의 페이지에서만 사용-->
<jsp:useBean id="notice" class="notice.Notice" scope="page" />
<!-- // notice notice = new notice(); -->
<jsp:setProperty name="notice" property="noticeTitle" /><!-- notice.setnoticeTitle(request) -->
<jsp:setProperty name="notice" property="noticeContent" />
<%
	System.out.println(notice);
%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>게시판 프로젝트</title>
</head>
<body>
	<%
		String userID = null;
	if(session.getAttribute("userID") != null){//유저 아이디 이름으로 세션이 존재하는 회원들은
		userID = (String)session.getAttribute("userID");//유저아이디에 해당 세션값을 넣어준다.
	}
	if(userID == null){
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('로그인을 하세요.')");
		script.println("location.href = 'login.jsp'");
		script.println("</script>");
	}else{
		if(notice.getNoticeTitle() == null || notice.getNoticeContent() == null){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('입력이 안된 사항이 있습니다.')");
			script.println("history.back()");
			script.println("</script>");
		}else{
			NoticeDAO NoticeDAO = new NoticeDAO();
			int result = NoticeDAO.write(notice.getNoticeTitle(), userID, notice.getNoticeContent());
			if(result == -1){
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('글쓰기에 실패했습니다')");
                script.println("history.back()");
                script.println("</script>");
			}else{
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("location.href='notice.jsp'");
				script.println("</script>");
			}
		}
	}
	%>
</body>
</html>