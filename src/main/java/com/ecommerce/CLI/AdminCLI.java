package com.ecommerce.CLI;

import com.ecommerce.models.Admin;
import com.ecommerce.models.Coupon;
import com.ecommerce.models.Order;
import com.ecommerce.models.Product;
import com.ecommerce.repository.CouponRepository;
import com.ecommerce.services.OrderService;
import com.ecommerce.services.ProductService;
import java.math.BigDecimal;
import java.util.Scanner;

public class AdminCLI {
	private final Scanner scanner;
	private final Admin admin;
	private final ProductService products;
	private final OrderService orders;
	private final CouponRepository coupons;

	public AdminCLI(Scanner scanner, Admin admin, ProductService products, OrderService orders, CouponRepository coupons) {
		this.scanner = scanner;
		this.admin = admin;
		this.products = products;
		this.orders = orders;
		this.coupons = coupons;
	}

	public void run() {
		boolean running = true;
		while (running) {
			System.out.println("\nAdmin: " + admin.getName());
			System.out.println("1. Product catalog  2. All orders  3. Update order status  4. Create coupon  0. Back");
			switch (readInt("Choose: ")) {
				case 1 -> showProducts();
				case 2 -> showOrders();
				case 3 -> updateOrderStatus();
				case 4 -> createCoupon();
				case 0 -> running = false;
				default -> System.out.println("Choose one of the listed options.");
			}
		}
	}

	private void showProducts() {
		for (Product product : products.listProducts()) {
			System.out.printf("%d | %s | $%s | stock: %d | seller: %d%n", product.getId(), product.getName(),
					product.getPrice().toPlainString(), product.getStock(), product.getSellerId());
		}
	}

	private void showOrders() {
		if (orders.listOrders().isEmpty()) System.out.println("No orders yet.");
		for (Order order : orders.listOrders()) {
			System.out.printf("#%d | customer %d | $%s | %s | %s%n", order.getId(), order.getCustomerId(),
					order.getTotal().toPlainString(), order.getStatus(), order.getShipment().getTrackingNumber());
		}
	}

	private void updateOrderStatus() {
		long id = readLong("Order id: ");
		System.out.println("1. Placed  2. Shipped  3. Delivered  4. Cancelled");
		Order.Status status = switch (readInt("New status: ")) {
			case 1 -> Order.Status.PLACED;
			case 2 -> Order.Status.SHIPPED;
			case 3 -> Order.Status.DELIVERED;
			case 4 -> Order.Status.CANCELLED;
			default -> null;
		};
		if (status == null) {
			System.out.println("Invalid status.");
			return;
		}
		try {
			orders.updateOrderStatus(id, status);
			System.out.println("Order status updated.");
		} catch (IllegalArgumentException exception) {
			System.out.println(exception.getMessage());
		}
	}

	private void createCoupon() {
		System.out.print("Coupon code: ");
		String code = scanner.nextLine().trim();
		BigDecimal percent = readDecimal("Discount percent (1-100): ");
		try {
			coupons.save(new Coupon(code, percent));
			System.out.println("Coupon saved.");
		} catch (IllegalArgumentException exception) {
			System.out.println("Could not create coupon: " + exception.getMessage());
		}
	}

	private int readInt(String prompt) {
		while (true) {
			System.out.print(prompt);
			try { return Integer.parseInt(scanner.nextLine().trim()); }
			catch (NumberFormatException exception) { System.out.println("Enter a whole number."); }
		}
	}

	private long readLong(String prompt) {
		while (true) {
			System.out.print(prompt);
			try { return Long.parseLong(scanner.nextLine().trim()); }
			catch (NumberFormatException exception) { System.out.println("Enter a valid id."); }
		}
	}

	private BigDecimal readDecimal(String prompt) {
		while (true) {
			System.out.print(prompt);
			try { return new BigDecimal(scanner.nextLine().trim()); }
			catch (NumberFormatException exception) { System.out.println("Enter a valid amount."); }
		}
	}
}