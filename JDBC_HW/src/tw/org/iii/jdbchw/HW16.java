package tw.org.iii.jdbchw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/*	20180901AM2
 * 	
 * 	帳號密碼登入_用程式端驗證帳號是否重複
 * 	
 * 	1.	建立程式架構
 * 		連線資料庫-->
 * 		如果輸入的帳號資料沒有重複-->新增一筆資料
 *		如果有重複-->告知資料重複
 *		==> 判斷是否重複獨立成一個方法
 *	2.	先將連線與判斷架構完成
 *	3.	處理判別是否有重複的方法
 *		a.	外面主程式已經開啟一個連線, 該方法就繼續用那個連線==>傳入參數connection
 *		b.	方法內的例外要不要拋出? 
 *			==> 要throws Exception 而非在該程式內catch 
 *			==>	因為在裡面處理的話, 那這個程式的true/false不就沒有辦法判斷了
 *			==> 所以有例外應該拋出傳回主程式, 讓主程式處理
 *	
 *	注意! 一般密碼設定不會使用明碼!! =>不安全!!
 */


public class HW16 {

	public static void main(String[] args) {
		//	待會登入成功後要新增的資料
		String account = "john456", password = "987654321", realname = "John Dillen";
				
		Properties info = new Properties();
		info.setProperty("user", "root");
		info.setProperty("password", "root");
		
		try (Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/jdbchw", info);) {
			if (!isDataRepeat(account, conn)) {
				String sql = "INSERT INTO `accounts` (account, password, realname) VALUES (?,?,?)";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, account);
				pstmt.setString(2, password);
				pstmt.setString(3, realname);
				pstmt.execute();
				
				System.out.println("UPDATE SUCCESS!");
			} else {
				System.out.println("Data Repeat");
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}
	
	static boolean isDataRepeat(String account, Connection conn) throws SQLException {
		// 傳入account & conn 讓方法可以存取
		// 為什麼拋出例外?	=> 如果這裡出現例外, 然後處理完, 那這個方法的回傳值true/false就沒用
		//	拋出讓上面的程式去接, 才會達到我們要的效果 (拋出-->有狀況)
		String sql2 = "SELECT count(*) as count FROM `accounts` where account = ?";
		//	如果有重複資料筆數就會大於零
		PreparedStatement pstmt = conn.prepareStatement(sql2);
		pstmt.setString(1, account); // 別忘了處理前面sql2的問號啊
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		int count = rs.getInt("count"); // 取得資料比數
		
		return count != 0 ;
	}
	
}
