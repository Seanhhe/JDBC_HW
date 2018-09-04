package tw.org.iii.jdbchw;

import java.awt.BorderLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
	
	// 原始資料 也可以用Property
	private LinkedList<HashMap<String, String>> data; 
	
	
	public HW12_v2() {
		super("JDBCHW12 JTable 練習2");
		
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
		// 模擬資料傳入
		data = new LinkedList<>();
		
		
		String url = "jdbc:mysql://localhost:3306/mysqlhw";
		
		Properties prop = new Properties();
		prop.setProperty("user", "root");
		prop.setProperty("password", "root");
		
		String query = "SELECT * FROM `customer`";
		
		try (Connection conn = DriverManager.getConnection(url, prop);) {
			PreparedStatement pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				String c1 = rs.getString("uid");
				String c2 = rs.getString("name");
				String c3 = rs.getString("tel");
				String c4 = rs.getString("birthday");
				
				HashMap<String, String> row = new HashMap<>();
				row.put("id",c1);
				row.put("name", c2);
				row.put("tel", c3);
				row.put("birthday", c4);
				data.add(row);
			}
			System.out.println("OK");
		} catch (SQLException e) {
			System.out.println(e);
		}
		
	}
	
	private class MyTableModel extends DefaultTableModel {
		/*	To create a concrete TableModel as a subclass of AbstractTableModel 
		 * 	you need only provide implementations for the following three methods:
		 * 		public int getRowCount();
		 * 		public int getColumnCount();
		 * 		public Object getValueAt(int row, int column);
		 * 
		 * 	Model => 整合資料 透過調變器 處理資料
		 */
		@Override
		public int getRowCount() {
			// how many rows
			return data.size();
		}
		
		@Override
		public int getColumnCount() {
			// how many columns
			return 4;
		}
		
		@Override
		public Object getValueAt(int row, int column) {
			// 取得指定欄位資料
			String read = "";
			switch(column) {
			case 0: 
				read = data.get(row).get("id");
				break;
			case 1: 
				read = data.get(row).get("name");
				break;
			case 2: 
				read = data.get(row).get("tel");
				break;
			case 3: 
				read = data.get(row).get("birthday");
				break;
			}
			return read;
		}
		
		@Override
		public String getColumnName(int column) {
			// 改寫: 回傳欄位名稱
			String read = "";
			switch(column) {
			case 0: 
				read = "帳號";
				break;
			case 1: 
				read = "姓名";
				break;
			case 2: 
				read = "電話";
				break;
			case 3: 
				read = "生日";
				break;
			}
			return read;
		}
		
		
	}
	
	public static void main(String[] args) {
		new HW12_v2();
	}

}
