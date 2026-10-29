package com.ecommerce.CLI;

import com.ecommerce.models.Cart;
import com.ecommerce.models.Customer;
import com.ecommerce.models.Order;
import com.ecommerce.models.Payment;
import com.ecommerce.models.Product;
import com.ecommerce.services.CartService;
import com.ecommerce.services.OrderService;
import com.ecommerce.services.ProductService;
import com.ecommerce.strategy.CardPayment;
import com.ecommerce.strategy.CodPayment;
import com.ecommerce.strategy.NetBankingPayment;
import com.ecommerce.strategy.PaymentMethod;
import com.ecommerce.strategy.UpiPayment;
import java.math.BigDecimal;
import java.util.Scanner;

public class CustomerCLI {
	private final Scanner scanner;
	private final Customer customer;
	private final ProductService products;
	private final CartService carts;
	private final OrderService orders;

	public CustomerCLI(Scanner scanner, Customer customer, ProductService products, CartService carts, OrderService orders) {
		this.scanner = scanner;
		this.customer = customer;
		this.products = products;
		this.carts = carts;
		this.orders = orders;
	}

	public void run() {
		boolean running = true;
		while (running) {
			System.out.println("\nCustomer: " + customer.getName());
			System.out.println("1. Browse products  2. View cart  3. Add to cart  4. Remove from cart");
			System.out.println("5. Checkout  6. My orders  0. Back");
			switch (readInt("Choose: ")) {
				case 1 -> showProducts();
				case 2 -> showCart();
				case 3 -> addToCart();
				case 4 -> removeFromCart();
				case 5 -> checkout();
				case 6 -> showOrders();
				case 0 -> running = false;
				default -> System.out.println("Choose one of the listed options.");
			}
		}
	}

	private void showProducts() {
		System.out.println("\nProducts");
		for (Product product : products.listProducts()) {
			System.out.printf("%d | %s | $%s | stock: %d | %s%n", product.getId(), product.getName(),
					product.getPrice().toPlainString(), product.getStock(), product.getDescription());
		}
	}

	private void showCart() {
		Cart cart = carts.getCart(customer.getId());
		if (cart.isEmpty()) {
			System.out.println("Your cart is empty.");
			return;
		}
		for (Cart.CartItem item : cart.getItems()) {
			System.out.printf("%d | %s x%d | $%s%n", item.getProduct().getId(), item.getProduct().getName(),
					item.getQuantity(), item.getLineTotal().toPlainString());
		}
		System.out.println("Subtotal: $" + cart.subtotal().toPlainString());
	}

	private void addToCart() {
		try {
			long productId = readLong("Product id: ");
			int quantity = readInt("Quantity: ");
			carts.addItem(customer.getId(), productId, quantity);
			System.out.println("Added to cart.");
		} catch (IllegalArgumentException exception) {
			System.out.println("Could not add item: " + exception.getMessage());
		}
	}

	private void removeFromCart() {
		long productId = readLong("Product id to remove: ");
		carts.removeItem(customer.getId(), productId);
		System.out.println("Cart updated.");
	}

	private void checkout() {
		if (carts.getCart(customer.getId()).isEmpty()) {
			System.out.println("Your cart is empty.");
			return;
		}
		System.out.print("Coupon code (press Enter to skip): ");
		String coupon = scanner.nextLine().trim();
		System.out.print("Shipping address: ");
		String address = scanner.nextLine().trim();
		System.out.println("Payment: 1. Card  2. UPI  3. Net banking  4. Cash on delivery");
		PaymentMethod method = switch (readInt("Choose payment: ")) {
			case 1 -> new CardPayment();
			case 2 -> new UpiPayment();
			case 3 -> new NetBankingPayment();
			case 4 -> new CodPayment();
			default -> null;
		};
		if (method == null) {
			System.out.println("Invalid payment method; checkout cancelled.");
			return;
		}
		try {
			Order order = orders.checkout(customer.getId(), coupon, address, method);
			System.out.printf("Order #%d placed. Total: $%s | Payment: %s | Tracking: %s%n", order.getId(),
					order.getTotal().toPlainString(), order.getPayment().getMethod(), order.getShipment().getTrackingNumber());
		} catch (IllegalArgumentException | IllegalStateException exception) {
			System.out.println("Checkout failed: " + exception.getMessage());
		}
	}

	private void showOrders() {
		var customerOrders = orders.listCustomerOrders(customer.getId());
		if (customerOrders.isEmpty()) {
			System.out.println("No orders yet.");
			return;
		}
		for (Order order : customerOrders) {
			System.out.printf("#%d | %s | $%s | %s | tracking %s%n", order.getId(), order.getCreatedAt(),
					order.getTotal().toPlainString(), order.getStatus(), order.getShipment().getTrackingNumber());
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
}