package notice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import notice.Notice;

public class NoticeDAO {// 데이터 베이스 접근 객체의 약자
	
	private Connection conn;// connection db에 접근하게 해주는 객체
	private ResultSet rs;
	public NoticeDAO() {
		try {
			String driverName = "oracle.jdbc.driver.OracleDriver";
			String dbURL = "jdbc:oracle:thin:@localhost:1521:system";
			String dbID = "yeon";
			String dbPassword = "1234";
			Class.forName(driverName);
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//현재의 시간을 가져오는 함수
	public String getDate() {
		//String SQL = "Select GETDATE()";
		//
		String SQL = "SELECT SYSDATE FROM NOTICE";
		// Select GETDATE();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";// 데이터베이스 오류
	}
//noticeID 게시글 번호 가져오는 함수
	public int getNext() {
		//String SQL = "SELECT noticeID FROM dbo.[notice] ORDER BY noticeID DESC";
		String SQL = "SELECT noticeID FROM NOTICE ORDER BY noticeID DESC";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1; // 첫번째 게시물인경우
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;// 데이터베이스 오류
	}
//글 작성하는 함수
	public int write(String noticeTitle, String userID, String noticeContent) {
		String SQL = "INSERT INTO NOTICE VALUES(?,?,?,?,?,?)";
	//String SQL = "insert into dbo.[notice](noticeID, noticeTitle, userID, noticeDate, noticeContent, noticeAvailable) values(?,?,?,?,?,?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, noticeTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, noticeContent);
			pstmt.setInt(6, 1);
			/*rs = pstmt.executeQuery();
			System.out.println(SQL);*/
			return pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	
	public ArrayList<Notice> getList(int pageNumber) {
		
		String SQL = "SELECT * FROM (SELECT * FROM NOTICE WHERE noticeID <? and noticeAvailable=1 ORDER BY noticeID DESC) WHERE ROWNUM<=10";
		
		//notice에서 나오는 걸 보관할수 있는 인스턴스를 생성
		ArrayList<Notice> list = new ArrayList<Notice>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			//게시글의 개수에 따라 값으로 나오게 할려고 그래서 6보다 작게 할려고 모든 글자가 다 나오게 할려고 이다. 그래서 일부러 함수를 만든것이다.
			rs = pstmt.executeQuery();
			//System.out.println("여기 에러요~");
			while (rs.next()) {
				Notice notice = new Notice();
				notice.setNoticeID(rs.getInt(1));
				notice.setNoticeTitle(rs.getString(2)); 
				notice.setUserID(rs.getString(3));
				notice.setNoticeDate(rs.getTimestamp(4));
				notice.setNoticeContent(rs.getString(5));
				notice.setNoticeAvailable(rs.getInt(6));
				list.add(notice);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean nextPage(int pageNumber) {
		  String SQL = "select * from (select * from notice where noticeid <? and noticeAvailable=1 order by noticeID desc) where rownum<=10";
		ArrayList<Notice> list = new ArrayList<Notice>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Notice getNotice(int noticeID) {
		//String SQL = "SELCET * FROM dbo.[notice] WHERE noticeID = ?";
		String SQL = "SELECT * FROM NOTICE WHERE noticeID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, noticeID);
			//System.out.println("여기 에러입니까??");
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				Notice notice = new Notice();
				notice.setNoticeID(rs.getInt(1));
				notice.setNoticeTitle(rs.getString(2));
				notice.setUserID(rs.getString(3));
				//notice.setnoticeTitle(rs.getString(4));
				notice.setNoticeDate(rs.getTimestamp(4));
				notice.setNoticeContent(rs.getString(5));
				notice.setNoticeAvailable(rs.getInt(6));

				return notice;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 수정 함수
		public int update(int noticeID, String noticeTitle, String noticeContent) {
			String SQL = "UPDATE NOTICE SET noticeTitle = ?, noticeContent = ? WHERE noticeID = ?";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				pstmt.setString(1, noticeTitle);
				pstmt.setString(2, noticeContent);
				pstmt.setInt(3, noticeID);
				return pstmt.executeUpdate();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1; // 데이터베이스 오류
		}
		
		// 삭제 함수
		public int delete(int noticeID) {
			String SQL = "UPDATE NOTICE SET noticeAvailable = 0 WHERE noticeID = ?";
			try {
				PreparedStatement pstmt = conn.prepareStatement(SQL);
				pstmt.setInt(1, noticeID);
				return pstmt.executeUpdate();

			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1; // 데이터베이스 오류
		}
	
}
