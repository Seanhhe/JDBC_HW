package tw.org.iii.jdbchw;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/*	20180901AM1 JDBC
 * 	將農委會OpenData資料下載至本機端的資料庫
 * 	http://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvAgriculturalProduce.aspx
 * 
 * 	設計流程:
 * 	1.	寫出程式基本架構
 * 	2.	分割問題
 * 	
 * 	**下載完成後資料庫顯示亂碼 => 字元編碼問題
 * 	MAMP\conf\mysql => 修改mysql組態檔
 * 	https://dev.mysql.com/doc/refman/5.7/en/charset-applications.html
 * 	
 */

public class HW13 {

	public static void main(String[] args) {
		//	Step1: Download the JSON format data from web;
		String source = fetchOpendata();
		// System.out.println(source); //	測試資料是否OK
		if (source != null) {
			toMyDB(source);
		} else {
			System.out.println("Data not found!!");
		}
	}
	
	private static String fetchOpendata() {
		// 撈資料
		String result = null;
		String link = "http://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvAgriculturalProduce.aspx";
		
		try {
			//	要有遠端的URL; 但她只是個字串, 要呼叫其他的動作/物件去啟動 (就像執行緒也要start)
			URL url = new URL(link);
			
			//	建立連線
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.connect();	//	啟動連線
			
			//	接收資料 (串流是以byte為單位, 目前的資料是文字=>BufferReader)
			//	InputStream-->InputStreamReader-->BufferReader
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));
			
			//	讀資料; 是文字資料, 需要一列一列接上
			StringBuffer buf = new StringBuffer(); 	// 一列一列接上
			String line; // 暫存讀進來的那一列
			while ((line = reader.readLine()) != null) {
				// 為什麼寫while迴圈?
				// 來源資料(頁面原始碼)有換列格式(農委會)
				// 一般來說 如果為單列無換列格式, 可不用寫while迴圈, 一個readline就結束.
				// 複習: 串流下載的是檔案(原始碼)
				buf.append(line);
				// line 一列一列的append到buf裡
			}
			reader.close();
			result = buf.toString(); // 把裝完的東西buf轉回String輸出
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
		
		
		return result;
	}
	
	private static void toMyDB(String json) {
		// 把資料擺到DB
		
		//	1.	先解析JSON格式=>解析成功才能入到資料庫
		JSONArray root = new JSONArray(json);
		//System.out.println(root.length());	//	確認是否有解析成功
		
		//	3.	建立資料庫連線
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		try (Connection conn = 
				DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbchw", prop);) {
			
			//	4. 建立SQL Command(Prepared Statement)把資料放入資料庫
			PreparedStatement pstmt = conn.prepareStatement(
					"INSERT INTO `gifts` (name, feature, place, imgurl) VALUES (?,?,?,?)");
			
			
			//	2. 開始解析, 用for迴圈尋訪陣列中每個物件
			for (int i = 0; i < root.length(); i++) {
				try {
					// 把每個陣列內的資料轉成JSON物件
					JSONObject row = root.getJSONObject(i);
					// 調資料
					String name = row.getString("Name");
					String feature = row.getString("Feature");
					String place = row.getString("SalePlace");
					String imgurl = row.getString("Column1");
					// System.out.println(name +":"+ feature +":" + place+ ":" + imgurl); // 測試資料是否有撈取成功
					
					//	5. 將資料對應PreparedStatement的問號
					pstmt.setString(1, name);
					pstmt.setString(2, feature);
					pstmt.setString(3, place);
					pstmt.setString(4, imgurl);
					
					//	6. 記得執行!
					pstmt.execute();
				} catch (JSONException e) {
					// 若某筆資料有問題的不算 continue!
					System.out.println(e);
				}
			}
			System.out.println("Load OK");	// 確認跑完
		} catch (Exception e) {
			//	這裡會捕捉SQL Exception & JSON EXception
			//	=> 確保資料庫連線與JSON解析有問題時會拋出
			System.out.println(e);
		}
		
	}
}
