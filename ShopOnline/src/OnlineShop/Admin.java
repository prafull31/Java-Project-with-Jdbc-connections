package OnlineShop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Admin implements AutoCloseable {
	static Scanner s = new Scanner(System.in);

	static void select() throws Exception {
		System.out.println("****************************************************");
		System.out.println("Enter 1 to get user details \n" + "enter 2 to get inventory details \n"
				+ "enter 3 to get user cart details \n" + "enter 4 to exit");
		int select = s.nextInt();
		switch (select) {
		case 1:
			getUserDetails();
			break;
		case 2:
			getQuantityDetails();
			break;
		case 3:
			getUserOrders();
			break;
		case 4:
			System.exit(2);
		default:
			System.out.println("invalid selection");
		}
	}

	static void getUserDetails() throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = Connect.getConnect();
			ps = connection.prepareStatement("select userId, userName, userEmail from user_details");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println("Id " + rs.getInt(1));
				System.out.println("Name " + rs.getString(2));
				System.out.println("Email " + rs.getString(3));
				System.out.println("");
			}
			select();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	static void getQuantityDetails() throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = Connect.getConnect();
			ps = connection.prepareStatement("select productID, productName, productQuantity from product_table");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println("Id " + rs.getInt(1) + " Name " + rs.getString(2) + " Quantity " + rs.getInt(3));
			}
			select();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	static void getUserOrders() {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = Connect.getConnect();
			ps = connection.prepareStatement(
					"select userId, productID, productName, productQuantity, productPrice, totalPrice from cart where userId = ?");
			System.out.println("Enter the User Id to get order history for user");
			int num = s.nextInt();
			ps.setInt(1, num);
			ResultSet rs = ps.executeQuery();
			System.out
					.println("userId \t" + "ProductId \t" + "productName \t" + "Quantity \t" + "price \t" + "Total \t");
			while (rs.next()) {
				System.out.println(rs.getInt(1) + "\t" + rs.getInt(2) + "\t" + rs.getString(3) + "\t" + rs.getInt(4)
						+ "\t\t" + rs.getInt(5) + "\t" + rs.getInt(6));
			}
			select();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void close() throws Exception {

	}

}
