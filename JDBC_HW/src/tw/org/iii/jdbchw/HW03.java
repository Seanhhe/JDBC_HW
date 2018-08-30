package tw.org.iii.jdbchw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


/*	20180826PM1 getConnection V2 00:38:22
 * 	
 * 	建立連線 (透過字串) 第二招  
 * 	=> getConnection(String url, String user, String password)
 * 	
 * 	建立連線=> 取得Connection的物件實體
 * 	
 * 	注意import的package => java.sql.Connection
 */

public class HW03 {

	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/mysqlhw";
		
		// 將帳號與密碼獨立寫出
		String user = "root";
		String password = "root";
		
		// SQL Command
		String sqlcommand = "insert into `customer` (name, tel, birthday)" +
							"values ('Jolin', '7777888', '2015-03-28')";
		
		// 使用的是 java.sql.Connection
		try {
			// 建立連線 (透過字串)
			Connection conn = DriverManager.getConnection(url, user,password);
			// 產生statement ==> 回傳Statement物件實體
			Statement stmt = conn.createStatement();
			
//			stmt.executeQuery(insert);	// 這個是查詢語法 不可用insert
			stmt.execute(sqlcommand);
			
			conn.close(); 
			// 用戶端的關閉 (不同的伺服器特性不同, 例如mysql session操作結束後會自動關閉)
			// 有些為了效能, 當有連續多次的連線時,會使用相同connection => 保持持續性的連接
			// mysql 要下指令他就會保持持續性的連接
			// 所以要注意伺服器的特性
			// 關閉也可使用自動關閉語法(像串流)
			System.out.println("Connection OK");
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

}
