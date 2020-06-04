<%@page import="board.BoardDBBean"%>
<%@page import="java.sql.Timestamp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="setting.jsp" %>
<script src="<%=project%>script.js"></script>

<h2> <%=page_write%> </h2>

<%
	request.setCharacterEncoding( "utf-8" );
%>
<jsp:useBean id="boardDto" class="board.BoardDataBean"/>
	<jsp:setProperty name="boardDto" property="*"/>
	<!-- writer email subject content ip -->
	<!-- num ref re_step re_level -->
<%
	// reg_date 작성일
	boardDto.setReg_date( new Timestamp( System.currentTimeMillis() ) );
%>	
<%
	// readcount 조회수
%>
<%
	// ip
	boardDto.setIp( request.getRemoteAddr() );
%>
<%
	BoardDBBean boardDao = BoardDBBean.getInstance();
	int result = boardDao.insertArticle( boardDto );
	if( result == 0 ) {
		// 작성 실패
		%>
		<script type="text/javascript">
			<!--
			alert( inserterror );
			//-->
		</script>
		<meta http-equiv="refresh" content="0; url='list.jsp'">
		<%
	} else {
		// 작성 성공
		response.sendRedirect( "list.jsp" );
	}
%>
	

















