package tw.org.iii.jdbchw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


/*	20180826PM1 SQL Command Update 00:56:00
 */

public class HW07 {

	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/mysqlhw";
		
		// 利用Properties儲存屬性
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		
		// SQL Command
		//String del = "delete from customer where uid = 3";
		//String del = "delete from customer where name = 'Mary'";
		String update = "UPDATE `mysqlhw`.`customer` SET `name`='Tsai', `tel`='0228225252', `birthday`='1993-04-03' where `uid` = 7";
		
		
		try (Connection conn = DriverManager.getConnection(url, prop);) {
			Statement stmt = conn.createStatement();
			
			stmt.execute(update);
			
			System.out.println("Connection OK");
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

}
