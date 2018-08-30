package tw.org.iii.jdbchw;

import java.lang.reflect.Method;

/*	20180826AM2 JDBC 基本設定
 * 	載入 mySQL Driver
 * 	Class物件
 * 
 */

public class HW01 {

	public static void main(String[] args) {
		// 指定載入特定的類別名稱 [JDBC載入驅動程式的方式之一]
		// Class.forName(className); 
		
		// Class類別物件方法介紹
//		String str1 = new String();
//		String str2 = "";
//		Class class1 = str1.getClass();
//		System.out.println(class1.getName());
//		
//		// 取得class1的父類別物件 (Object)
//		Class class2 = class1.getSuperclass();
//		System.out.println(class2.getName());
//		
//		// 使用class.getModifiers方法
//		int modify = class1.getModifiers();
//		System.out.println(modify);
//		
//		
//		// .getDeclaredMethods()
//		Method[] methods = class2.getDeclaredMethods();
//		for (Method method : methods) {
//			System.out.println(method.getName());
//		}
		
		// 載入JDBC Driver
		// https://dev.mysql.com/doc/connector-j/5.1/en/connector-j-usagenotes-connect-drivermanager.html
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("OK: Driver Loaded");
			// reference設定完後, 顯示OK代表專案已經自動載入Driver
		} catch (ClassNotFoundException e) {
			System.out.println("Driver Not Found");
		}
	}

}
