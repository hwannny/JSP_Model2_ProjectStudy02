package control;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import handler.CommandHandler;
import handler.DefaultHandler;

public class Controller extends HttpServlet {
	private HashMap<String, CommandHandler> handlerMap = new HashMap<String, CommandHandler>();
	
	public void init( ServletConfig config) throws ServletException {
		
		//파일 경로 찾기
		String configFile = config.getInitParameter("configFile"); 
		String fileName = config.getServletContext().getRealPath("/") + configFile;
		
		//파일생성
		FileInputStream fis = null;
		Properties prop = new Properties();
		try {
			fis = new FileInputStream( fileName );
			//읽은 파일로 맵 생성
			prop.load( fis ); 
			
			//객체로 맵핑하기위해 반복자로 만들기
			Iterator<Object> e =  prop.keySet().iterator();
			
			//반복자 사용해서 키값에 의해 객체 생성하고 넣기
			while( e.hasNext() ) {
				//커멘드 읽어오기
				String command = (String)e.next();
				
				//커멘드에 대응하는 값 가져오기
				String handlerName = prop.getProperty( command );
				
				//클래스 객체 생성
				Class<?> hanlderClass =  Class.forName( handlerName ); 
				
				//핸들러 객체 생성
				CommandHandler handler = (CommandHandler)hanlderClass.newInstance();
				
				//해쉬 맵에 넣기
				handlerMap.put(command, handler);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}//init
	
	
	public void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//URL 짜르기
		String command = request.getRequestURI();
		
		//방어_우리 프로젝트의 URL형식인지 확인
		if ( command.indexOf(request.getContextPath())==0 ) { //0이라는건 시작부분이 맞는지
			
			//URL 짤라서 커멘드 생성
			command = command.substring( request.getContextPath().length() );
			
			//커멘드에 대응하는 객체 꺼내오기
			CommandHandler handler = handlerMap.get(command);
			
			//방어_대응하는 핸들러가 없으면 기본객체 넣기
			if(handler==null) {
				handler = new DefaultHandler();
			}
			
			//포워드로 보낼 뷰페이지 생성
			String viewPage = null;
			
			try {
				//작업을 시키고 객체들을 가지고 + 뷰페이지 경로를 리턴 받아서 뷰페이지에 저장
				viewPage = handler.process(request, response);
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			//뷰페이지에 해당하는 것을 골라서 가져온다
			RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
			
			//뷰페이지 경로로 보내준다
			dispatcher.forward(request, response);
		}
		
	}//doProcess
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess( request, response );
	}//doGet
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess( request, response );
	}//doPost
	
}//Class - Controller
