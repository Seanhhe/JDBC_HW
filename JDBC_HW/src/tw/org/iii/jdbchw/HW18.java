package tw.org.iii.jdbchw;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/*	20180901PM1 00:18:30
 * 	
 * 	搜尋到該筆資料後修改ResultSet後即時更新資料庫內的資料
 * 	=>	不透過傳統取其ID後下SQL更新語法的方式 (避免不停地傳送SQL Command給資料庫)
 * 	=>	ResultSet.TYPE_FORWARD_ONLY
 * 		ResultSet.CONCUR_UPDATABLE
 * 	<java.sql Interface ResultSet>
 * 	
 * 	注意: Java有提供ResultSet這個方法, 但是該Connection不見得有支援, 所以要先取得DB的metadata確認
 */	

public class HW18 {

	public static void main(String[] args) {
		Properties info = new Properties();
		info.setProperty("user", "root");
		info.setProperty("password", "root");
		
		try (Connection conn = 
				DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/jdbchw", info);) {
			DatabaseMetaData metadata = conn.getMetaData();	// 管理資料庫的資料 (資料的資料, 就像網頁HTML的meta)
			//	詢問資料庫是否支援ResultSet的同步
			//	(若未支援則需查詢官方文件是否有參數可調整)
			//	https://docs.oracle.com/javase/7/docs/api/java/sql/ResultSet.html
			boolean isOK = metadata.supportsResultSetConcurrency(
					ResultSet.TYPE_FORWARD_ONLY, 
					ResultSet.CONCUR_UPDATABLE);
			System.out.println(isOK);
			//	statement 也要指定參數
			Statement stmt = conn.createStatement(
					ResultSet.TYPE_FORWARD_ONLY, 
					ResultSet.CONCUR_UPDATABLE); 
			ResultSet rs = stmt.executeQuery("SELECT * FROM `accounts`WHERE id = 1");
			rs.next();
			System.out.println(rs.getString("realname"));
//			rs.updateString("realname", "Tsai Yi Lin");
//			rs.updateRow();
			System.out.println("OK");
			
//			//--------同時修改所有帳號的密碼(不用重複下SQL 語法)----------
			String sql2 = "SELECT * FROM `accounts`";
			PreparedStatement pstmt = conn.prepareStatement(sql2,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				rs.updateString("password", "1234567890");
				rs.updateRow();
			}
//			
			//------------直接新增資料(不用SQL 語法)------------
			rs.moveToInsertRow(); // 指標移到新增欄位的空白位置(表最後一筆的下面)
			rs.updateString("account", "hello14");
			rs.updateString("realname", "ABC");
			rs.updateString("password", "1111111");
			rs.insertRow();		// 執行新增 (事實上是暫存??)
			
			//rs.previous();		// 回到上一筆
			//rs.deleteRow();		// 新增後不能馬上執行刪除 ==> 這裡砍的是新增前的最後一筆
			
//			//-----------目前ResultSet的指標位置-----------
			System.out.println(rs.getRow()+ "-指標目前的位置-"); // 目前在最下面
//			//	rs.previous(); // 回上一列
//			//	System.out.println(rs.getRow());
			rs.beforeFirst(); // 移到上面再重撈資料 ((標頭下的第一個欄位Moves the cursor to the front of this ResultSet object, just before the first row.
			while (rs.next()) {
				String id = rs.getString("id");
				String name = rs.getString("account");
				System.out.println(id + " : " + name); // 顯示目前資料庫內的東西
				//rs.deleteRow(); // 全砍 (最新新增的不能砍, 試驗過好像還是可以砍?
			}	// 離開迴圈時是在沒有下一筆的位置
			
			//rs.deleteRow();	//	刪掉最新的那一筆
//			rs.first();	// 第一筆資料
//			System.out.println(rs.getString("account"));
			rs.last(); // 最後一筆資料
			System.out.println(rs.getString("id"));
			rs.deleteRow(); 	//	砍掉指標所在的資料 這裡是砍掉最後一筆(被新增的那一筆)
//			rs.absolute(2);
//			System.out.println(rs.getString("id"));
			// rs.afterLast();	// 最後一筆後的那個空欄位
			// System.out.println(rs.getString("id"));
			// rs.beforeFirst();	// 標頭
			// System.out.println(rs.getString("id"));
			
			
			/*	ResultSet 是同步資料庫目前的狀況
			 *	 	
			 */

			
			
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
