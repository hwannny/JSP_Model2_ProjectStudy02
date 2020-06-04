<%@page import="board.BoardDataBean"%>
<%@page import="board.BoardDBBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="setting.jsp" %>
<script src="<%=project%>script.js"></script>

<h2> <%=page_delete%> </h2>
	<%
	String pageNum = request.getParameter( "pageNum" );
	int num = Integer.parseInt( request.getParameter( "num" ) );
	String passwd = request.getParameter( "passwd" );
	int number = Integer.parseInt( request.getParameter( "number" ) );
	%>
	
	<%
	BoardDBBean boardDao = BoardDBBean.getInstance();
	BoardDataBean boardDto = boardDao.getArticle( num );
	if( passwd.equals( boardDto.getPasswd() ) ) {
		// 비밀번호가 같다
		int result = boardDao.deleteArticle( num );
		if( result == 0 ) {
			%>
			<script type="text/javascript">
				<!--
				alert( deleteerror );
				//-->
			</script>
			<meta http-equiv="refresh" content="0; url='list.jsp?pageNum=<%=pageNum%>">
			<%	
		} else if( result == -1 ) {
			%>
			<script type="text/javascript">
				<!--
				alert( replyerror );
				//-->
			</script>
			<meta http-equiv="refresh" content="0; url='list.jsp?pageNum=<%=pageNum%>'">
			<%	
		} else {
			if(number==1){
				int temp = Integer.parseInt(pageNum) - 1;
				pageNum = Integer.toString(temp);
			}
			response.sendRedirect( "list.jsp?pageNum=" + pageNum );
		}		
	} else {
		// 비밀번호가 다르다
		%>
		<script type="text/javascript">
			<!--
			erroralert( repasswderror );
			//-->
		</script>
		<%		
	}
	%>

	












    