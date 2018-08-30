package tw.org.iii.jdbchw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


/*	20180826PM2 SQL Command Query 00:03:00
 * 	Interface PreparedStatement => 可用來對付SQL Injection
 * 	https://docs.oracle.com/javase/7/docs/api/java/sql/PreparedStatement.html
 * 	
 * 	
 */

public class HW09 {

	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/mysqlhw";
		
		// 利用Properties儲存屬性
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		
		// SQL Command
		String query = "INSERT INTO `customer` (name, tel, birthday) VALUES (?,?,?)";
		
		
		try (Connection conn = DriverManager.getConnection(url, prop);) {
			PreparedStatement pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, "Mary"+(int)(Math.random()*50));
			pstmt.setString(2, "0800080"+(int)(Math.random()*100));
			pstmt.setString(3, "2008-08-"+(int)(Math.random()*10));
			
			// PreparedStatement.execute()/ .executeQuery()/ .executeUpdate()
			// .executeUpdate() 會回傳剛才改變的資料筆數
			int result = pstmt.executeUpdate();
			
			
			System.out.println("Data update:" + result);
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

}
