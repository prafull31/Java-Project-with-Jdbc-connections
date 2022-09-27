package OnlineShop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Cart implements AutoCloseable {
	static int eQuantity;
	static int finalTotal;
	static int quantity;
	static int productID;
	static HashSet<Integer> hashSet = new HashSet<Integer>();

	static void addToCart() throws Exception {
		Scanner s = new Scanner(System.in);
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			for (;;) {
				connection = Connect.getConnect();
				ps = connection.prepareStatement(
						"insert into cart(userId, productID, productName, productQuantity, productPrice,totalPrice)values (?,?,?,?,?,?)");
				System.out.println("Enter productId to enter into cart or press 99 for checkout");
				productID = s.nextInt();

				if (productID > 0 && productID < 11) {
					System.out.println("Enter quantity ");
					quantity = s.nextInt();
					HashMap<Integer, String> name = Product.getProductName();
					HashMap<Integer, Integer> price = Product.getProductPrice();

					int itemPrice = price.get(productID);
					int total = quantity * itemPrice;
					finalTotal = finalTotal + total;

					ps.setInt(1, LoginImpl.userId);
					ps.setInt(2, productID);
					ps.setString(3, name.get(productID));
					ps.setInt(4, quantity);
					ps.setInt(5, price.get(productID));
					ps.setInt(6, total);
					ps.executeUpdate();

					ps = connection.prepareStatement("select orderNum from cart where userId = ? and productId =?");
					ps.setInt(1, LoginImpl.userId);
					ps.setInt(2, productID);
					ResultSet rs = ps.executeQuery();
					while (rs.next()) {
						int order = rs.getInt(1);
						hashSet.add(order);
					}

					updateQuantity();

				} else if (productID > 11 && productID < 99 || productID < 0 || productID > 99) {
					System.out.println("Invalid input");
				} else if (productID == 99) {
					System.out.println("*********************************************************");
					viewCart();
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			s.close();
		}

	}

	static void updateQuantity() {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = Connect.getConnect();
			ps = connection.prepareStatement("select productQuantity from product_table");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				eQuantity = rs.getInt(1);
			}

			eQuantity = eQuantity - quantity;
			ps = connection.prepareStatement("update product_table set productQuantity = ? where productId =?");
			ps.setInt(1, eQuantity);
			ps.setInt(2, productID);
			ps.execute();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	static void viewCart() {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = Connect.getConnect();
			ps = connection.prepareStatement(
					"select productName, productQuantity, productPrice, totalPrice from cart where orderNum =?");
			System.out.println("*********************************************************");
			System.out.println("Product Name \t" + "Quantity \t" + "Price \t \t" + "Total \t");
			for (int i : hashSet) {
				ps.setInt(1, i);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					System.out.println(
							rs.getString(1) + "\t" + rs.getInt(2) + "\t \t" + rs.getInt(3) + "\t \t" + rs.getInt(4));
				}
			}
			System.out.println("\n Grand Total is " + finalTotal);
			System.out.println("*********************************************************");
			System.out.println(" Thanks for Shopping");
			System.out.println("*********************************************************");
			System.exit(1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void close() throws Exception {
	}

}
