package OnlineShop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Scanner;

public class Product implements AutoCloseable {

	static Scanner s = new Scanner(System.in);
	static HashMap<Integer, String> name = new HashMap<Integer, String>();
	static HashMap<Integer, Integer> price = new HashMap<Integer, Integer>();

	static {
		System.out.println("Welcome " + LoginImpl.enterEmail + " to online_shop");
		System.out.println("*********************************************************");
	}

	static void product() throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = Connect.getConnect();

			for (;;) {
				System.out.println("Enter 1 to sort by id or 2 to sort by name or 3 to sort by price");
				int sort = s.nextInt();
				if (sort == 1) {
					ps = connection.prepareStatement("select * from product_table order by productID asc");

					break;
				} else if (sort == 2) {
					ps = connection.prepareStatement("select * from product_table order by productName asc");
					break;
				} else if (sort == 3) {
					ps = connection.prepareStatement("select * from product_table order by productPrice asc");
					break;
				} else {
					System.out.println("Invalid input");
				}

			}
			System.out.println("*********************************************************");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				System.out.println("Id- " + rs.getInt(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t"
						+ "Price-" + "\t" + rs.getInt(5));

			}
			Cart c = new Cart();
			c.addToCart();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			connection.close();
			ps.close();
		}

	}

	static public HashMap<Integer, String> getProductName() throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = Connect.getConnect();
			ps = connection.prepareStatement("select productID,productName from product_table");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				String productName = rs.getString(2);
				name.put(id, productName);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			connection.close();
			ps.close();
		}
		return name;
	}

	static public HashMap<Integer, Integer> getProductPrice() throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = Connect.getConnect();
			ps = connection.prepareStatement("select productID,productPrice from product_table");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int id = rs.getInt(1);
				int productPrice = rs.getInt(2);
				price.put(id, productPrice);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			connection.close();
			ps.close();
		}
		return price;
	}

	@Override
	public void close() throws Exception {
	}

}
