package user;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDAO {
	//dao:데이터베이스 접근 객체의 약자로
	//실질적으로 db에서 회원정보 불러오거나 db에 회원정보를 넣을때
	private Connection conn; //connection db에 접근하게 해주는 객체
	private PreparedStatement pstmt;
	private ResultSet rs;
	//Oracle에 접속 해주는 부분
	public UserDAO() {//생성자 실행될때마다 자동으로 db연결이 이루어질수 있도록함
		try {
			String driverName = "oracle.jdbc.driver.OracleDriver";
			String dbURL = "jdbc:oracle:thin:@localhost:1521:system";
			String dbID = "yeon";
			String dbPassword = "1234";
			
			Class.forName(driverName);
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			
			System.out.println("DB에 연결 되었습니다.\n");
			
		}catch(ClassNotFoundException e) {
			System.out.println("DB 드라이버 로딩 실패 :" +e.toString());
		}catch(SQLException sqle) {
			System.out.println("DB 접속실패 :"+sqle.toString());
		}catch(Exception e) {
			System.out.println("Unkonwn error");
			e.printStackTrace();
		}
		
			
		
	}
	 public int login(String userID, String userPassword) {
		 String SQL = "SELECT userPassword FROM USER1 WHERE userID = ?";
		 
		 try {
			 //pstmt: prepared statement 정해진 sql문장을 db에 삽입하는 형식으로 인스턴스가져옴
			 pstmt = conn.prepareStatement(SQL);
			 //sql인젝션 같은 해킹기법을 방해하는것 pstmt를 이용해 하나의 문장을 미리 준비해서 (물음표사용)
			 //물음표에 해당하는 내용을 유저 아이디로, 매개변수로 이용 1)존재하는지 2)비번 무엇인지
			 pstmt.setString(1, userID);
			 //rs:result set에 결과보관
			 rs = pstmt.executeQuery();
			 //결과가 존재한다면 실행
			 if(rs.next()) {
				 //패스워드 일치한다면 실행
				 if(rs.getString(1).equals(userPassword)) {
					 return 1;//로긴성공
				 }else
					 return 0;//비번 불일치
			 }return -1;//아이디 없음
			 
		 }catch(Exception e) {
			 e.printStackTrace();
		 }return -2;//데이터베이스 오류를 의미
	 }
	 
	 public int join(User user) {
		 String SQL = "INSERT INTO USER1 VALUES (?,?,?,?,?)";
		 try {
			 pstmt = conn.prepareStatement(SQL);
			 pstmt.setString(1, user.getUserID());
			 pstmt.setString(2, user.getUserPassword());
			 pstmt.setString(3, user.getUserName());
			 pstmt.setString(4, user.getUserGender());
			 pstmt.setString(5, user.getUserEmail());
			 return pstmt.executeUpdate();
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
		 return -1;//DB오류
	 }
 }