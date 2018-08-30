package tw.org.iii.jdbchw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


/*	20180826PM1 getConnection V4 00:52:28
 * 	
 * 	第三招再加上自動關閉
 * 	=> 為甚麼可以使用autoclose => 因Connection有實作autoclosebale介面
 * 	=> autoclose可以寫兩段用分號區隔
 * 	
 * 	
 * 	建立連線 (透過字串) 第三招  [推薦!!]
 * 	getConnection(String url, Properties info)
 * 		=> properties 
 * 			=> https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-reference-configuration-properties.html
 * 			=> 看一下 Properties and Descriptions (可設定的key)
 * 		=> 彈性更大, 自訂屬性設定
 * 	Properties
 * 	=> https://docs.oracle.com/javase/7/docs/api/java/util/Properties.html
 * 	=> 實作Map介面 (key, value)
 * 	=> 都是字串
 * 	=> setProperty : 設定
 * 	=> getProperty : 取值
 * 	
 * 	注意import的package => java.sql.Connection
 */

public class HW05 {

	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/mysqlhw";
		
		// 利用Properties儲存屬性
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		
		// SQL Command
		String sqlcommand = "insert into `customer` (name, tel, birthday)" +
							"values ('Obama', '66666666', '1970-11-28')";
		
		// 使用的是 java.sql.Connection
		
		
		try (Connection conn = DriverManager.getConnection(url, prop);) {
			// 產生statement ==> 回傳Statement物件實體
			Statement stmt = conn.createStatement();
			
//			stmt.executeQuery(insert);	// 這個是查詢語法 不可用insert
			stmt.execute(sqlcommand);
			
			// conn.close(); 
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
