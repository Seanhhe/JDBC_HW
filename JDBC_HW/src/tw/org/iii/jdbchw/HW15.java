package tw.org.iii.jdbchw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/*	20180901AM2 JDBC
 * 	
 * 	從資料庫讀取資料並以JSON格式輸出
 * 
 */

public class HW15 {

	public static void main(String[] args) {
		Properties info = new Properties();
		info.setProperty("user", "root");
		info.setProperty("password", "root");
		
		try (Connection conn = 
				DriverManager.getConnection(
						"jdbc:mysq://localhost:3306/jdbchw", info);) {
			String sql = "";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
		} catch (SQLException e) {
			System.out.println(e);
		}
		
	}

}
