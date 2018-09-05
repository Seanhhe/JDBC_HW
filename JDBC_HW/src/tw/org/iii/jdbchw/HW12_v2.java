package tw.org.iii.jdbchw;

import java.awt.BorderLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/*	20180901PM1
 * 	把HW13中匯入到mysql資料庫的農委會資料, 撈出後放入JTable視窗中
 * 	
 * 	
 */

public class HW12_v2 extends JFrame{

	
	// ----------------Scrollable and auto Update Table------------------
	// Creating a Table Model => 透過Model管控與呈現資料
	// 實作TableModel
	private JTable jTable;
	private int dataCount; // 資料有幾筆
	private String[] labels; // 欄位名稱
	private ResultSet rs;
	private Connection conn;
	
	public HW12_v2() {
		super("農委會推薦農村優良伴手禮 HW");
		
		setLayout(new BorderLayout());
		
		// 載入伺服器端資料
		initData();
		
		jTable = new JTable(new MyTableModel());
		JScrollPane jsp = new JScrollPane(jTable);
		add(jsp,BorderLayout.CENTER);
		
		setSize(800,600);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void initData() {
		
		String url = "jdbc:mysql://localhost:3306/jdbchw";
		
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		String query = "SELECT id as `編號`, name as `產品名稱`, place as `商家地址`, feature as `產品特色`, imgurl as `圖片連結` FROM `gifts`";
		
		try {
			// Connection 這裡不使用自動關閉 因為原先自動關閉是區域變數, 會造成myTable要抓資料時無法使用這個connection
			conn = DriverManager.getConnection(url, prop);
			// 第一道SQL Command => 資料的資料
			PreparedStatement pstmt1 = conn.prepareStatement("SELECT count(*) as count FROM `gifts`");
			rs = pstmt1.executeQuery();
			rs.next(); // 不要忘記寫這個
			dataCount = rs.getInt("count");
			
			
			//	第二道 SQL Command => 資料
			PreparedStatement pstmt2 = conn.prepareStatement(query, 
					ResultSet.TYPE_FORWARD_ONLY, 
					ResultSet.CONCUR_UPDATABLE);	// 讓資料可讀可修
			rs = pstmt2.executeQuery(); 	// ResultSet 可重複使用
			//	這時候的rs: 資料
			
			ResultSetMetaData metadata = rs.getMetaData();
			labels = new String [metadata.getColumnCount()];	// 欄位有幾個
			for (int i=0; i < labels.length; i++) {
				//	取得欄位名稱
				labels[i] = metadata.getColumnLabel(i+1);
				// columnlabel 從1 開始計算
			}
			
			System.out.println("OK");
		} catch (SQLException e) {
			System.out.println(e);
		}
		
	}
	
	private class MyTableModel extends DefaultTableModel {
		@Override
		public int getRowCount() {
			// how many rows
			return dataCount;
		}
		
		@Override
		public int getColumnCount() {
			// how many columns
			return labels.length;
		}
		
		@Override
		public Object getValueAt(int row, int column) {
			// 取得指定欄位資料 (先讓指標到那一列再選哪一欄)
			// 為甚麼不拋出: 如果只有一筆資料有問題 就要放棄全部也太可惜?XD
			try {
				rs.absolute(row+1);
				// model的row為0開始
				return rs.getString(labels[column]);
			} catch (SQLException e) {
				System.out.println(e);
				return "not found";
			}
		}
		
		@Override
		public String getColumnName(int column) {
			return labels[column];
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {
			// 資料可不可以被改
			//	若回傳回來的column欄位為id則false => 不可修改
			//	equals的字串要等於最後SQL撈回來的欄位名稱
			//	Table: column是從零開始算
			//	Table: row也是從零開始,但是零是標題列
			return labels[column].equals("編號")?false:true;
		}
		
		@Override
		public void setValueAt(Object aValue,int row, int column) {
			//	即時修改功能
			super.setValueAt(aValue, row, column);
			try {
				// 把指標移到要修改的列
				rs.absolute(row+1);
				// 給欄位與修改的值
				rs.updateString(labels[column], aValue.toString());
				//	執行更新
				rs.updateRow();
			} catch (SQLException e) {
				// 為什麼不可以拋 => override的方法 父類別沒拋你不能拋阿~
				System.out.println(e);
			}
		}
		
	}
	
	public static void main(String[] args) {
		new HW12_v2();
	}

}
