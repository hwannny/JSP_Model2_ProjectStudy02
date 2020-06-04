<%@page import="board.BoardDBBean"%>
<%@page import="java.sql.Timestamp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="setting.jsp" %>
<script src="<%=project%>script.js"></script>

<h2> <%=page_write%> </h2>

	<%
	int result = (Integer)request.getAttribute("result");
	%>
	
	<%
	if( result == 0 ) {
		// 작성 실패
		%>
		<script type="text/javascript">
			<!--
			alert( inserterror );
			//-->
		</script>
		<meta http-equiv="refresh" content="0; url='list.do'">
		<%
	} else {
		// 작성 성공
		response.sendRedirect( "list.do" );
	}
%>
	

















