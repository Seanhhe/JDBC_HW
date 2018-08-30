package tw.org.iii.jdbchw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


/*	20180826PM1 SQL Command 刪除語法 00:56:00
 * 	
 * 	
 */

public class HW06 {

	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/mysqlhw";
		
		// 利用Properties儲存屬性
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		
		// SQL Command
		//String del = "delete from customer where uid = 3";
		String del = "delete from customer where name = 'Mary'";
		
		
		
		try (Connection conn = DriverManager.getConnection(url, prop);) {
			Statement stmt = conn.createStatement();
			
			stmt.execute(del);
			
			System.out.println("Connection OK");
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

}
