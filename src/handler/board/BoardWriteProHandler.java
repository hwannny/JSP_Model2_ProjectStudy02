package handler.board;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.BoardDBBean;
import board.BoardDataBean;
import handler.CommandHandler;

public class BoardWriteProHandler implements CommandHandler {
	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		 request.setCharacterEncoding( "utf-8" );
		 BoardDataBean boardDto = new BoardDataBean();
		 
		 boardDto.setWriter( request.getParameter( "writer" ) );
		 boardDto.setEmail( request.getParameter( "email" ) );
		 boardDto.setSubject( request.getParameter( "subject" ) );
		 boardDto.setContent( request.getParameter( "content" ) );
		 boardDto.setPasswd( request.getParameter( "passwd" ) );
		 
		 boardDto.setNum( Integer.parseInt( request.getParameter( "num" ) ) );
		 boardDto.setRef( Integer.parseInt( request.getParameter( "ref" ) ) );
		 boardDto.setRe_step( Integer.parseInt( request.getParameter( "re_step" ) ) );
		 boardDto.setRe_level( Integer.parseInt( request.getParameter( "re_level" ) ) );
		 
		 boardDto.setReg_date( new Timestamp( System.currentTimeMillis() ) );

		 boardDto.setIp( request.getRemoteAddr() );
		 
		 BoardDBBean boardDao = BoardDBBean.getInstance();
		 int result = boardDao.insertArticle( boardDto );
	
		 request.setAttribute( "result", result );
		 
		 return "/board/writePro.jsp";
	}
}
