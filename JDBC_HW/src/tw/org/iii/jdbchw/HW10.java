package tw.org.iii.jdbchw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


/*	20180826PM2 SQL Command Query 00:14:55
 * 	PreparedStatement = DELETE
 * 	
 * 	Interface PreparedStatement => 可用來對付SQL Injection
 * 	https://docs.oracle.com/javase/7/docs/api/java/sql/PreparedStatement.html
 * 	
 * 	
 */

public class HW10 {

	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/mysqlhw";
		
		// 利用Properties儲存屬性
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		
		// SQL Command
		String delete = "delete from `customer` where uid = ?";
		
		
		try (Connection conn = DriverManager.getConnection(url, prop);) {
			PreparedStatement pstmt = conn.prepareStatement(delete);
			
			pstmt.setString(1, "1");
			
			// PreparedStatement.execute()/ .executeQuery()/ .executeUpdate()
			// .executeUpdate() 會回傳剛才改變的資料筆數 (沒動到會回傳0)
			int result = pstmt.executeUpdate();
			
			
			System.out.println("Data update:" + result);
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

}
