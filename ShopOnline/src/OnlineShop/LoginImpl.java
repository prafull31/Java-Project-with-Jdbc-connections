package OnlineShop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Scanner;

public class LoginImpl implements AutoCloseable {
	static Scanner scanner = new Scanner(System.in);
//	static Connect connect = new Connect();

	public static void select() throws Exception {
		System.out.println("Enter 1 for register");
		System.out.println("Enter 2 for login");
		System.out.println("Enter 9 for admin login");
	//System.out.println("");
		for (;;) {
			int select = scanner.nextInt();
			switch (select) {
			case 1:
				register();
				break;
			case 2:
				loginUser();
				break;
			case 9:
				loginAdmin();
				break;
			default:
				System.out.println("Invalid input please try again");
			}
		}
	}

	public static void register() throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = Connect.getConnect();
			ps = connection
					.prepareStatement("insert into user_details(userName, userEmail, userPassword) " + "values(?,?,?)");

			System.out.println("Enter Name");
			String name = scanner.next();
			System.out.println("Enter Email");
			String email = scanner.next();
			System.out.println("Set Password");
			String password = scanner.next();

			ps.setString(1, name);
			ps.setString(2, email);
			ps.setString(3, password);

			ps.executeUpdate();
			System.out.println("Successfully registered ");
			System.out.println("");
			loginUser();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	static String enterEmail;
	static int userId;

	public static void loginUser() throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = Connect.getConnect();
			ps = connection.prepareStatement("select userEmail, userPassword from user_details");
			ResultSet rs = ps.executeQuery();
			HashMap<String, String> hashMap = new HashMap<String, String>();
			while (rs.next()) {
				String email = rs.getString(1);
				String password = rs.getString(2);
				hashMap.put(email, password);
			}
			System.out.println("Enter Email");
			enterEmail = scanner.next();
			System.out.println("Enter Password");
			String enterPassword = scanner.next();

			if (hashMap.containsKey(enterEmail) && enterPassword.equals(hashMap.get(enterEmail))) {

				ps = connection.prepareStatement("select userId from user_details where userEmail =?");
				ps.setString(1, enterEmail);
				ResultSet r = ps.executeQuery();
				while (r.next()) {
					userId = r.getInt(1);
				}
				System.out.println("**************************************************");
				System.out.println("logged in succesfully ");
				System.out.println("**************************************************");
				Product.product();
			} else {
				throw new LoginException("Invalid login credentials try again");
			}
		} catch (LoginException e) {
			System.out.println(e.getLocalizedMessage());
			System.out.println("");
			select();
		}
	}

	public static void loginAdmin() throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = Connect.getConnect();
			ps = connection.prepareStatement("select adminId, userPassword from admin_details");
			ResultSet rs = ps.executeQuery();
			HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
			while (rs.next()) {
				int id = rs.getInt(1);
				String password = rs.getString(2);
				hashMap.put(id, password);
			}

			System.out.println("Enter ID");
			int enterId = scanner.nextInt();
			System.out.println("Enter Password");
			String enterPassword = scanner.next();

			if (hashMap.containsKey(enterId) && enterPassword.equals(hashMap.get(enterId))) {
				System.out.println("logged in succesfully ");
				Admin.select();

			} else {
				throw new LoginException("Invalid login credentils try again");
			}
		} catch (LoginException e) {
			System.out.println(e.getLocalizedMessage());
			System.out.println("");
			select();
		}
	}

	@Override
	public void close() throws Exception {
	}
}
