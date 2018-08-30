package tw.org.iii.jdbchw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


/*	20180826PM1 建立連線
 * 	
 * 	有些伺服器會保持持續性的連接, 所以要抓到伺服器的特性.
 * 	
 * 	建立連線=> 取得Connection的物件實體
 * 	
 * 	注意import的package => java.sql.Connection
 */

public class HW02 {

	public static void main(String[] args) {
		// 預設port 可省略 (:3306) / iii (資料庫名)
		// port號要注意是否正確
		// https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-url-format.html
		// url格式各資料庫大同小異 注意差異的部分即可
		// mysql://localhost:3306/mysqlhw?user=root&
		String url = "jdbc:mysql://localhost:3306/mysqlhw?user=root&password=root";
		
		// SQL Command
		String sqlcommand = "insert into `customer` (name, tel, birthday)" +
							"values ('Jay', '5556666', '2003-02-28')";
		
		// 使用的是 java.sql.Connection
		try {
			// 建立連線 (透過字串)
			Connection conn = DriverManager.getConnection(url);
			// 產生statement ==> 回傳Statement物件實體
			Statement stmt = conn.createStatement();
			
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
