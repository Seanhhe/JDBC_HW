package tw.org.iii.jdbchw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.json.JSONStringer;
import org.json.JSONWriter;

/*	20180901AM2 JDBC
 * 	
 * 	從資料庫讀取資料並以JSON格式輸出
 * 	=> JSONStringer ()
 * 	=> JSONWriter (I/O)
 * 
 * 	設計程式觀念:
 * 	=> 使用JSON前先思考為何要JSON?
 * 		團隊開發、網際網路、程式需求
 */

public class HW15 {

	public static void main(String[] args) {
		Properties info = new Properties();
		info.setProperty("user", "root");
		info.setProperty("password", "root");
		
		try (Connection conn = 
				DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/jdbchw", info);) {
			String sql = "SELECT * FROM `gifts` ORDER BY id desc"; //  LIMIT 0, 10
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			
			//	如何輸出JSON? 利用org.json
			//	JSON 最後輸出也是字串=> 利用JSONStringer
//			JSONStringer js = new JSONStringer();	//源頭: 代表整個JSON格式 
//			JSONWriter jw = js.array();	// 如果最外圈的格式是陣列 => 由array產生, 傳回JSONWriter => writer就可以拿來輸出的動作
//			jw.endArray(); // 處理完畢要結束array
			
//			JSONWriter jw = js.array();
//			jw.object();
//				jw.key("key1").value("value1");
//				jw.key("key2").value("value2");
//			jw.endObject();
//			jw.object();
//				jw.key("key1").value("value1");
//				jw.key("key2").value("value2");
//			jw.endObject();
//			jw.endArray(); // 結束array
//			System.out.println(jw);
			
			//	產生JSON字串
			JSONStringer js = new JSONStringer();
			JSONWriter jw = js.array();
			
			while (rs.next()) {
				String id = rs.getString("id");
				String name = rs.getString("name");
				//String feature = rs.getString("feature");
				String place = rs.getString("place");
				//String imgurl = rs.getString("imgurl");
				
				jw.object();
					jw.key("編號").value(id);
					jw.key("名稱").value(name);
					// jw.key("特色").value(feature);
					jw.key("地址").value(place);
					// jw.key("圖片").value(imgurl);
				jw.endObject();
			}
			js.endArray();
			System.out.println(js);
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}

}
