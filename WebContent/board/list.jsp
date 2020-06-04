<%@page import="java.text.SimpleDateFormat"%>
<%@page import="board.BoardDataBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="board.BoardDBBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="setting.jsp" %>
<link href="<%=project%>style.css" type="text/css" rel="stylesheet">

	<%
	int pageSize = (Integer)request.getAttribute("pageSize");	
	int pageBlock =  (Integer)request.getAttribute("pageBlock");
	int cnt =  (Integer)request.getAttribute("cnt");
	String pageNum = (String)request.getAttribute("pageNum");
	int currentPage = (Integer)request.getAttribute("currentPage");
	int number =  (Integer)request.getAttribute("number");
	int startPage =  (Integer)request.getAttribute("startPage");
	int endPage =  (Integer)request.getAttribute("endPage");
	int pageCount =  (Integer)request.getAttribute("pageCount");
	
	%>

	<h2> <%=page_list%> ( <%=str_cnt%> : <%=cnt%> ) </h2>

	<table>
		<tr>
			<td colspan="6" align="right">
				<a href="writeForm.do"><%=str_write%></a>&nbsp;&nbsp;&nbsp;
			</td>
		</tr>
		<tr>
			<th style="width: 8%"> <%=str_number%> </th>
			<th style="width: 40%"> <%=str_subject%> </th>
			<th style="width: 10%"> <%=str_writer%> </th>
			<th style="width: 20%"> <%=str_reg_date%> </th>
			<th style="width: 9%"> <%=str_readcount%> </th>
			<th style="width: 15%"> <%=str_ip%> </th>
		</tr>
		<%
			if( cnt == 0 ) {
				// 글이 없는 경우
				%>
				<tr>
					<td colspan="6" align="center">
						<%=msg_list_x%>
					</td>
				</tr>
				<%
			} else {
				// 글이 있는 경우
				ArrayList <BoardDataBean> boardDtos = (ArrayList<BoardDataBean>)request.getAttribute("boardDtos"); //경고무시
				for( int i=0; i<boardDtos.size(); i++ ) {
					BoardDataBean boardDto = boardDtos.get( i );
					%>
					<tr>
						<td align="center">
							<%=number--%>
						</td>
						<td>
							&nbsp;
							<%
								int level = boardDto.getRe_level();
								if( level > 1 ) {
									int wid = ( level - 1 ) * 10;
									%>
									<img src="<%=project%>images/level.gif" border="0" width="<%=wid%>" height="15">
									<%	
								}						
								if( level > 0 ) {
									%>
									<img src="<%=project%>images/re.gif" border="0" width="20" height="15">
									<%
								}
							%>					
							<a href="content.do?pageNum=<%=pageNum%>&num=<%=boardDto.getNum()%>&number=<%=number+1%> ">
								<%=boardDto.getSubject()%>
							</a>
						</td>
						<td align="center">
							<%=boardDto.getWriter()%>
						</td>
						<td align="center">
							<%
							SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm" );
							%>
							<%=sdf.format( boardDto.getReg_date() )%>
						</td>
						<td align="center">
							<%=boardDto.getReadcount()%>
						</td>
						<td align="center">
							<%=boardDto.getIp()%>
						</td>
					</tr>
					<%
				}
				
			}
		%>			
	</table>
	<br>
	<%
	if( cnt > 0 ) {
		if( startPage > pageBlock ) {
			%>
			<a href="list.do">[◀◀]</a>
			<a href="list.do?pageNum=<%=startPage-pageBlock%>">[◀]</a>
			<%	
		}		
		for( int i=startPage; i<=endPage; i++ ) {
			if( i == currentPage ) {
				%>	
				<b>[<%=i%>]</b>				
				<%
			} else {
				%>	
				<a href="list.do?pageNum=<%=i%>">[<%=i%>]</a>				
				<%
			}
		}		
		if( pageCount > endPage ) {
			%>
			<a href="list.do?pageNum=<%=startPage+pageBlock%>">[▶]</a>
			<a href="list.do?pageNum=<%=pageCount%>">[▶▶]</a>
			<%
		}	 
	}
	%>
















