package tw.org.iii.jdbchw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


/*	20180904AM2 存取資料庫資料
 * 
 */

public class HW14 {

	public static void main(String[] args) {
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbchw", prop);) {
			Statement stmt = conn.createStatement();
			//	執行Query得到ResultSet-->一張表的觀念
			ResultSet rs = stmt.executeQuery("SELECT count(*) as count FROM `gifts`");
			rs.next();	//	移動指標到那一列
			
			//	取回傳值
			int nums = rs.getInt("count");	// 因為知道欄位值一定是整數, 所以後面可以處理
			String number = rs.getString("count");	// 直接拿整數, 反正後面也是要做字串處理
			System.out.println(nums);
			//	System.out.println(rs.getString("count"));
			//	System.out.println(rs.getInt(1));	//	用column index(從1開始)
			
			//	資料分頁處理
			int rpp = 20;	// 每頁看幾筆
			int page = 2;	// 第幾頁
			int start = (page-1)*rpp;	// 起始位置
			
			rs = stmt.executeQuery("SELECT id, name FROM `gifts` LIMIT " + start + "," + rpp);
			while (rs.next()) {
				String id = rs.getString("id");
				String name = rs.getString("name");
				System.out.println(id + " : " + name);
			}
		} catch (SQLException e) {
			System.out.println(e);
		}

	}

}
