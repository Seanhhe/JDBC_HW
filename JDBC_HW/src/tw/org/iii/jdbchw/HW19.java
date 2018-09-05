package tw.org.iii.jdbchw;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/*	20180901PM2
 * 	
 * 	BLOB 
 * 	=>	65535 bytes
 * 	LONGBLOB 
 * 	=>	4294967295 bytes (4Gib) 
 * 	=>	沒有真的要放很多的話, 不建議使用, 因為多少會影響到資料庫搜尋的速度
 */

public class HW19 {

	public static void main(String[] args) {
		Properties info = new Properties();
		info.setProperty("user", "root");
		info.setProperty("password", "root");
		
		Student s1 = new Student(90, 87, 100);
		
		try (	// 放進來讓他autoclose
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbchw", info);
				FileInputStream fin = new FileInputStream("dir1/card9.png");
				) {
			
			String sql = "UPDATE `accounts` SET img = ?, student = ? WHERE id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			// 讀圖片進來 (二進制串流)
			pstmt.setBinaryStream(1, fin);
			// 讀進物件
			pstmt.setObject(2, s1);
			pstmt.setString(3, "31");
			pstmt.executeUpdate();
			
			System.out.println("Save Success");
		} catch (SQLException e) {
			System.out.println(e);
		} catch (FileNotFoundException e1) {
			System.out.println(e1);
		} catch (IOException e1) {
			System.out.println(e1);
		} 
	}

}

class Student implements Serializable {
	int ch, eng, math;
	public Student(int a, int b, int c) {
		this.ch = a;
		this.eng = b;
		this.math = c;
	}
	
	public int sumScore() {
		return ch+eng+math;
	}
}
