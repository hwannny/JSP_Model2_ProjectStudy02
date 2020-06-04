<%@page import="board.BoardDBBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="setting.jsp" %>
<script src="<%=project%>script.js"></script>

<h2> <%=page_modify%> </h2>
<%
	request.setCharacterEncoding( "utf-8" );
%>
<%
	String pageNum = request.getParameter( "pageNum" );
%>
<jsp:useBean id="boardDto" class="board.BoardDataBean"/>
	<jsp:setProperty name="boardDto" property="*"/>
	<!-- num email subject content passwd -->
<%
	BoardDBBean boardDao = BoardDBBean.getInstance();
	int result = boardDao.modifyArticle( boardDto );
	if( result == 0) {
		%>
		<script type="text/javascript">
			<!--
			alert( modifyerror );
			//->
		</script>
		<meta http-equiv="refresh" content="0; url='list.jsp?pageNum=<%=pageNum%>'">
		<%
	} else {
		response.sendRedirect( "list.jsp?pageNum=" + pageNum );
	}
%>
















