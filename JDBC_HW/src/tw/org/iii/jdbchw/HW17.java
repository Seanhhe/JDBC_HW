package tw.org.iii.jdbchw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/*	20180901PM1
 * 	帳號密碼驗證通過後建立該帳號的物件實體
 * 	
 * 	基本流程
 * 	1.	連線資料庫
 * 	2.	檢查帳號密碼
 * 	3.	驗證通過後印出並產生物件實體
 * 	
 * 	注意程式設計的邏輯
 */


public class HW17 {

	public static void main(String[] args) {
		String account = "john";
		String pwd = "123456789";
		
		Properties info = new Properties();
		info.setProperty("user", "root");
		info.setProperty("password", "root");
		
		try (Connection conn = 
				DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/jdbchw", info);) {
			Member member = null;
			if ((member = checkMember(account,pwd,conn)) != null) {
				System.out.println("歡迎登入 " + member.realname + "!");
			} else {
				System.out.println("登入失敗!");
			}
		} catch (Exception e) {
			System.out.println("伺服器忙線中");
		}
		
	}
	
	static Member checkMember(String account, String pwd, Connection conn) throws SQLException {
		String sql = "SELECT * FROM `accounts` WHERE account = ? and password = ?"; 
		// 基本上目前沒有人這樣寫 連密碼都比對, 而是找到該帳號後再撈出密碼(非明碼)比對 
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, account);
		pstmt.setString(2, pwd);
		ResultSet rs = pstmt.executeQuery();
		if (rs.next()) {
			return new Member(rs.getString("account"),rs.getString("realname"));
		} else {
			return null;
		}
	}
}

class Member{
	String account, realname;
	public Member(String account, String realname) {
		this.account = account;
		this.realname = realname;
	}
	
}


