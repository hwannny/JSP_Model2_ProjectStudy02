package board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class BoardDBBean {
	private static BoardDBBean instance = new BoardDBBean();
	public static BoardDBBean getInstance() {
		return instance;
	}
	public Connection getConnection() throws NamingException, SQLException {
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup( "java:comp/env" );
		DataSource ds = (DataSource) envCtx.lookup( "jdbc/encore" );
		return ds.getConnection();
	}
	
	public int getCount() {
		int count = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "select count(*) from board";
			pstmt = con.prepareStatement( sql );
			rs = pstmt.executeQuery();
			if( rs.next() ) {
				count = rs.getInt( 1 );
			}			
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if( rs != null ) rs.close();
				if( pstmt !=null ) pstmt.close();
				if( con != null ) con.close();
			} catch( SQLException e ) {
				e.printStackTrace();
			}
		}
		return count;
	}
	
	public int insertArticle( BoardDataBean boardDto ) {
		int result = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			
			/*						ref			re_step		re_level
			 * 10 제목글			9			0				0	
			 * 09 ㄴ 답글			9			1				1
			 * 08    ㄴ 재답글		9			2				2
			 */
			
			/*						ref			re_step		re_level
			 * 10 제목글			9			0				0	
			 * 09 ㄴ 답글			9			2				1
			 * 08    ㄴ 재답글		9			3				2
			 * 07 ㄴ 나중 답글		9			1				1		
			 * 									자기보다 큰 값 +1
			 */
			
			/*						ref			re_step		re_level
			 * 10 제목글			9			0				0	
			 * 07 ㄴ 나중 답글		9			1				1
			 * 09 ㄴ 답글			9			2				1
			 * 08    ㄴ 재답글		9			3				2		
			 */
			
			int num = boardDto.getNum();
			int ref = boardDto.getRef();
			int re_step = boardDto.getRe_step();
			int re_level = boardDto.getRe_level();
			String sql = null;
			if( num == 0 ) {
				// 제목글인 경우
				sql = "select max(num) from board";
				pstmt = con.prepareStatement( sql );
				rs = pstmt.executeQuery();
				if( rs.next() ) {
					// 글이 있는 경우
					ref = rs.getInt( 1 ) + 1;		// 그룹화아이디 = 글번호최대값 + 1
				} else {
					// 글이 없는 경우
					ref = 1;
				}				
				re_step = 0;
				re_level = 0;
			} else {
				// 답글인 경우
				sql = "update board set re_step=re_step+1 "
					+ "where ref=? and re_step>?";
				pstmt = con.prepareStatement( sql );
				pstmt.setInt( 1, ref );
				pstmt.setInt( 2, re_step );
				pstmt.executeUpdate();
				re_step ++;
				re_level ++;
			}		
			
			if( pstmt != null ) pstmt.close();
			sql = "insert into board( num, writer, email, subject, passwd, "
				+ "reg_date, ref, re_step, re_level, content, ip ) "
				+ "values( board_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";
			pstmt = con.prepareStatement( sql );
			pstmt.setString( 1, boardDto.getWriter() );
			pstmt.setString( 2, boardDto.getEmail() );
			pstmt.setString( 3, boardDto.getSubject() );
			pstmt.setString( 4, boardDto.getPasswd() );
			pstmt.setTimestamp( 5, boardDto.getReg_date() );
			pstmt.setInt( 6, ref );
			pstmt.setInt( 7, re_step );
			pstmt.setInt( 8, re_level );
			pstmt.setString( 9, boardDto.getContent() );
			pstmt.setString( 10, boardDto.getIp() );
			
			result = pstmt.executeUpdate();
			
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if( rs != null ) rs.close();
				if( pstmt !=null ) pstmt.close();
				if( con != null ) con.close();
			} catch( SQLException e ) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public ArrayList<BoardDataBean> getArticles( int start, int end ) {
		ArrayList <BoardDataBean> articles = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "select num,writer,email,subject,passwd,";
				sql+= "reg_date,ref,re_step,re_level,content,ip,readcount,r ";
				sql+= "from (select num,writer,email,subject,passwd,reg_date,ref,re_step";
				sql+= ",re_level,content,ip,readcount,rownum r from ";
				sql+= "(select num,writer,email,subject,passwd,reg_date,ref,re_step,re_level ";
				sql+= ",content,ip,readcount from board order by ref desc, re_step asc) ";
				sql+= "order by ref desc, re_step asc ) where r >= ? and r <= ?";
			pstmt = con.prepareStatement( sql );
			pstmt.setInt( 1, start );
			pstmt.setInt( 2, end );
			rs = pstmt.executeQuery();
			if( rs.next() ) {
				articles = new ArrayList<BoardDataBean> ( end - start + 1 );
				do {
					BoardDataBean article = new BoardDataBean();
					article.setNum( rs.getInt( "num" ) );
					article.setWriter( rs.getString( "writer" ) );
					article.setEmail( rs.getString( "email" ) );
					article.setSubject( rs.getString( "subject" ) );
					article.setPasswd( rs.getString( "passwd" ) );
					article.setReg_date( rs.getTimestamp( "reg_date" ) );
					article.setReadcount( rs.getInt( "readcount" ) );
					article.setRef( rs.getInt( "ref" ) );
					article.setRe_step( rs.getInt( "re_step" ) );
					article.setRe_level( rs.getInt( "re_level" ) );
					article.setContent( rs.getString( "content" ) );
					article.setIp( rs.getString( "ip" ) );			
					
					articles.add( article );
					
				} while( rs.next() );			
			}			
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if( rs != null ) rs.close();
				if( pstmt !=null ) pstmt.close();
				if( con != null ) con.close();
			} catch( SQLException e ) {
				e.printStackTrace();
			}
		}
		return articles;
	}
	
	public BoardDataBean getArticle( int num ) {
		BoardDataBean article = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			String sql = "select * from board where num=?";
			pstmt = con.prepareStatement( sql );
			pstmt.setInt( 1, num );
			rs = pstmt.executeQuery();
			if( rs.next() ) {
				article = new BoardDataBean();
				article.setNum( rs.getInt( "num" ) );
				article.setWriter( rs.getString( "writer" ) );
				article.setEmail( rs.getString( "email" ) );
				article.setSubject( rs.getString( "subject" ) );
				article.setPasswd( rs.getString( "passwd" ) );
				article.setReg_date( rs.getTimestamp( "reg_date" ) );
				article.setReadcount( rs.getInt( "readcount" ) );
				article.setRef( rs.getInt( "ref" ) );
				article.setRe_step( rs.getInt( "re_step" ) );
				article.setRe_level( rs.getInt( "re_level" ) );
				article.setContent( rs.getString( "content" ) );
				article.setIp( rs.getString( "ip" ) );					
			}
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if( rs != null ) rs.close();
				if( pstmt !=null ) pstmt.close();
				if( con != null ) con.close();
			} catch( SQLException e ) {
				e.printStackTrace();
			}
		}		
		return article;
	}
	
	public void addCount( int num ) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "update board set readcount=readcount+1 where num=?";
			pstmt = con.prepareStatement( sql );
			pstmt.setInt( 1, num );
			pstmt.executeUpdate();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {		
				if( pstmt !=null ) pstmt.close();
				if( con != null ) con.close();
			} catch( SQLException e ) {
				e.printStackTrace();
			}
		}			
	}
	
	public int deleteArticle( int num ) {
		int result = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			BoardDataBean boardDto = getArticle( num );
			int ref = boardDto.getRef();
			int re_step = boardDto.getRe_step();
			int re_level = boardDto.getRe_level();
			String sql = "select * from board "
						+ "where ref=? and re_step=?+1 and re_level>?";
			pstmt = con.prepareStatement( sql );
			pstmt.setInt( 1, ref );
			pstmt.setInt( 2, re_step );
			pstmt.setInt( 3, re_level );
			rs = pstmt.executeQuery();
			
			if( rs.next() ) {
				// 답글이 있다
				result = -1;
			} else {
				// 답글이 없다
				sql = "update board set re_step=re_step-1 "
					+ "where ref=? and re_step>?";
				if( pstmt != null ) pstmt.close();
				pstmt = con.prepareStatement( sql );
				pstmt.setInt( 1, ref );
				pstmt.setInt( 2, re_step );
				pstmt.executeUpdate();
				
				sql = "delete from board where num=?";
				if( pstmt != null ) pstmt.close();
				pstmt = con.prepareStatement( sql );
				pstmt.setInt( 1, num );
				result = pstmt.executeUpdate();						
			}			
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if( rs != null ) rs.close();
				if( pstmt !=null ) pstmt.close();
				if( con != null ) con.close();
			} catch( SQLException e ) {
				e.printStackTrace();
			}
		}				
		return result;
	}

	public int modifyArticle( BoardDataBean boardDto ) {
		int result = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = getConnection();
			String sql = "update board set email=?, subject=?, content=?, passwd=? "
						+ "where num=?";
			pstmt = con.prepareStatement( sql );
			pstmt.setString( 1, boardDto.getEmail() );
			pstmt.setString( 2, boardDto.getSubject() );
			pstmt.setString( 3, boardDto.getContent() );
			pstmt.setString( 4, boardDto.getPasswd() );
			pstmt.setInt( 5, boardDto.getNum() );
			result = pstmt.executeUpdate();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if( pstmt !=null ) pstmt.close();
				if( con != null ) con.close();
			} catch( SQLException e ) {
				e.printStackTrace();
			}
		}		
		return result;
	}
	
} // class

















