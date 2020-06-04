package handler.board;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.BoardDBBean;
import board.BoardDataBean;
import handler.CommandHandler;

public class BoardListHandler implements CommandHandler {
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		int pageSize = 5;					
		int pageBlock = 3;					
		
		int cnt = 0;
		String pageNum = null;			
		int currentPage = 0;				
		int start = 0;						
		int end = 0;							
		int number = 0;					
		
		int startPage = 0;
		int endPage = 0;
		int pageCount = 0;

		BoardDBBean boardDao = BoardDBBean.getInstance();
		cnt = boardDao.getCount();
		
		pageNum = request.getParameter( "pageNum" );
		if( pageNum == null ) {
			pageNum = "1";
		}
		currentPage = Integer.parseInt( pageNum );
		start = ( currentPage -1 ) * pageSize + 1;		
		if( end > cnt ) end = cnt;
		end = start + pageSize - 1;						
		
		number = cnt - ( currentPage -1 ) * pageSize;
		
		pageCount = cnt / pageSize + ( cnt % pageSize > 0 ? 1 : 0 );
		startPage = ( currentPage / pageBlock ) * pageBlock + 1;
					
		if( currentPage % pageBlock == 0 ) startPage -= pageBlock;				
		endPage = startPage + pageBlock - 1;

		if( endPage > pageCount ) endPage = pageCount;			
		
		request.setAttribute("pageSize", pageSize);	
		request.setAttribute("pageBlock", pageBlock);
		request.setAttribute("cnt", cnt);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("number", number);
		request.setAttribute("startPage", startPage);
		request.setAttribute("endPage", endPage);
		request.setAttribute("pageCount", pageCount);
		
		//총 글이 1개 이상인 경우
		if( cnt>0 ) {
			ArrayList<BoardDataBean> boardDtos = boardDao.getArticles( start, end );
			request.setAttribute("boardDtos", boardDtos);
		}
	
		return "/board/list.jsp";
	}
}
