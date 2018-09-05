package tw.org.iii.jdbchw;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;


/*	20180901PM2 00:38:00
 * 	
 * 	存取資料庫上的Object&Image
 * 	=> BLOB&LONGBLOB格式
 * 	=>	BinaryStream
 * 	
 * 	alt+shift+z => try catch shortcut
 * 
 * 	**補充 MDB
 * 	Access=>
 */

public class HW20 {

	public static void main(String[] args) {
		Properties info = new Properties();
		info.setProperty("user", "root");
		info.setProperty("password", "root");
		
		try (
				Connection conn = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/jdbchw", info);
				FileOutputStream fout = new FileOutputStream("dir2/hw20.png");
				) {
			String sql = "SELECT * FROM  `accounts` WHERE id =?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "31");
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			
			InputStream in = rs.getBinaryStream("img"); // 把資料庫的圖片讀進來
			
			byte[] buf = new byte[4096]; //每讀一次暫存到這裡
			int len; // 讀進來的長度
			while ( (len = in.read(buf)) != -1) {
				fout.write(buf, 0, len);
			}
			fout.flush();
			in.close(); // 關閉in串流
			
			//	學生物件不能直接getObject轉型, 因為資料庫欄位是使用BLOB格式
			// Student obj = (Student)rs.getObject("student");
			// System.out.println(obj.sumScore());
			
			//	透過串接把物件取出
			//	通常物件儲存在資料庫無法用SQL查詢, 但可以保有物件特性,讀入程式中使用
			InputStream in2 = rs.getBinaryStream("student"); 
			ObjectInputStream oin = new ObjectInputStream(in2);
			Student s2 = (Student)oin.readObject();
			System.out.println(s2.sumScore());
			
			System.out.println("Export OK");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
