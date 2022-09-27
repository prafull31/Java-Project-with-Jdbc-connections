package OnlineShop;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connect {
	public static Connection getConnect() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/online_shop", "root", "root");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return connection;
	}

}
