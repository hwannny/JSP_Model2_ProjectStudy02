<%@page import="java.text.SimpleDateFormat"%>
<%@page import="board.BoardDataBean"%>
<%@page import="board.BoardDBBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="setting.jsp" %>
<link href="<%=project%>style.css" type="text/css" rel="stylesheet">
<script src="<%=project%>script.js"></script>

	<h2> <%=page_content%> </h2>
	
	<%
	String pageNum = request.getParameter( "pageNum" );
	int num = Integer.parseInt( request.getParameter( "num" ) );
	int number = Integer.parseInt( request.getParameter( "number" ) );
	%>
	<%
	BoardDBBean boardDao = BoardDBBean.getInstance();
	BoardDataBean boardDto = boardDao.getArticle( num );
		
	if( ! boardDto.getIp().equals( request.getRemoteAddr() ) ){
		boardDao.addCount( num );
	}
		
	%>
	
	<table>
		<tr>
			<th> <%=str_number%> </th>
			<td align="center"> <%=number%> </td>
			<th> <%=str_readcount%> </th>
			<td align="center"> <%=boardDto.getReadcount()%> </td>		
		</tr>
		<tr>
			<th> <%=str_writer%> </th>
			<td align="center"> <%=boardDto.getWriter()%> </td>
			<th> <%=str_reg_date%> </th>
			<td align="center">
				<%
					SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
				%>
				<%=sdf.format( boardDto.getReg_date() )%>
			</td>
		</tr>
		<tr>
			<th> <%=str_subject%> </th>
			<td colspan="3"> <%=boardDto.getSubject()%> </td>		
		</tr>
		<tr>
			<th> <%=str_content%> </th>
			<td colspan="3"> <pre><%=boardDto.getContent()%></pre> </td>
		</tr>
		<tr>
			<th colspan="4">
				<input class="inputbutton" type="button" value="<%=btn_modify%>" onclick="location='modifyForm.jsp?num=<%=boardDto.getNum()%>&pageNum=<%=pageNum%>'">
				<input class="inputbutton" type="button" value="<%=btn_delete%>" onclick="location='deleteForm.jsp?num=<%=boardDto.getNum()%>&pageNum=<%=pageNum%>&number=<%=number%>'">
				<input class="inputbutton" type="button" value="<%=btn_reply%>"
					onclick="location='writeForm.jsp?num=<%=boardDto.getNum()%>&ref=<%=boardDto.getRef()%>&re_step=<%=boardDto.getRe_step()%>&re_level=<%=boardDto.getRe_level()%>'">
				<input class="inputbutton" type="button" value="<%=btn_list%>" onclick="location='list.jsp?pageNum=<%=pageNum%>'">
			</th>
		</tr>
	</table>

















