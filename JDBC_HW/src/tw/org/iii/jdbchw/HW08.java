package tw.org.iii.jdbchw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


/*	20180826PM1 SQL Command Query 01:09:00
 * 	
 * 	查詢後回傳的資料如何處理!
 * 	=> 透過pointer指標把每一列的資料讀進來
 * 	=> 欄位標題 可以更換也可以計算有幾個欄位
 * 	
 */

public class HW08 {

	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/mysqlhw";
		
		// 利用Properties儲存屬性
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		
		// SQL Command
		// => 回傳的會依據指令要求的欄位順序
		// => 也同樣可以使用SQL的函數呼叫計算欄位 select count(*) from customer;
		// String query = "SELECT * FROM customer";
		String query = "SELECT name as FNAME, uid, tel, birthday FROM `customer`";
		
		
		try (Connection conn = DriverManager.getConnection(url, prop);) {
			Statement stmt = conn.createStatement();
			
			// 執行查詢 executeQuery(String sql) => 回傳 ResultSet
			/*	ResultSet: https://docs.oracle.com/javase/7/docs/api/java/sql/ResultSet.html
			 * 	=> 裡面的方法也可用來處理更新資料庫資料
			 * 	=> getString(int columnIndex) [拿資料, index 1]
			 * 	=> next() 指標指向一筆一筆的資料 (return false if there are no more rows)
			 */
			ResultSet rs = stmt.executeQuery(query);
			
			
			// 回傳的資料
			while(rs.next()) {
				// 4 culomns (column index)
				// 用getString 因為是通過通訊協定 所以基本上資料都是字串型別
				// 除非是要運算在使用int等其他型別
//				String c1 = rs.getString(1);
//				String c2 = rs.getString(2);
//				String c3 = rs.getString(3);
//				String c4 = rs.getString(4);
				
				// 改用 column label 維護性較高
				// 這裡的Label是搭配查詢出來的結果: 如果SQL Command有另設Label則要與它相同
				String c1 = rs.getString("uid");
				String c2 = rs.getString("FNAME"); // 原name=> FNAME
				String c3 = rs.getString("tel");
				String c4 = rs.getString("birthday");
				
				System.out.println(c1 + " : " + c2 +" : "+ c3 +" : "+ c4);
			}
			
			
			
			System.out.println("Connection OK");
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

}
