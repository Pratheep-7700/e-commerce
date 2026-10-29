package com.ecommerce.CLI;

import com.ecommerce.models.Admin;
import com.ecommerce.models.Customer;
import com.ecommerce.models.Seller;
import com.ecommerce.services.CartService;
import com.ecommerce.services.OrderService;
import com.ecommerce.services.ProductService;
import com.ecommerce.services.UserService;
import com.ecommerce.repository.CouponRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.UserRepository;
import java.util.Scanner;

public class MainCLI {
	private final Scanner scanner;
	private final Customer customer;
	private final Seller seller;
	private final Admin admin;
	private final ProductService products;
	private final CartService carts;
	private final OrderService orders;
	private final CouponRepository coupons;

	public MainCLI() {
		scanner = new Scanner(System.in);
		UserRepository users = new UserRepository();
		ProductRepository productRepository = new ProductRepository();
		OrderRepository orderRepository = new OrderRepository();
		coupons = new CouponRepository();
		UserService userService = new UserService(users);
		customer = userService.registerCustomer("Demo Customer", "customer@example.com");
		seller = new Seller(2, "Demo Seller", "seller@example.com");
		admin = new Admin(3, "Administrator", "admin@example.com");
		users.save(seller);
		users.save(admin);
		products = new ProductService(productRepository);
		products.addProduct(seller.getId(), "Everyday Backpack", "Water-resistant 20L backpack", new java.math.BigDecimal("49.90"), 12);
		products.addProduct(seller.getId(), "Ceramic Mug", "350 ml stoneware mug", new java.math.BigDecimal("12.50"), 30);
		products.addProduct(seller.getId(), "Desk Lamp", "Adjustable LED desk lamp", new java.math.BigDecimal("34.00"), 8);
		carts = new CartService(products);
		orders = new OrderService(orderRepository, productRepository, coupons, carts,
				new com.ecommerce.services.PaymentService());
		coupons.save(new com.ecommerce.models.Coupon("WELCOME10", new java.math.BigDecimal("10")));
	}

	public void run() {
		boolean running = true;
		while (running) {
			System.out.println("\n=== E-commerce CLI ===");
			System.out.println("1. Customer (demo)  2. Seller (demo)  3. Admin  0. Exit");
			switch (readInt("Choose: ")) {
				case 1 -> new CustomerCLI(scanner, customer, products, carts, orders).run();
				case 2 -> new SellerCLI(scanner, seller, products).run();
				case 3 -> new AdminCLI(scanner, admin, products, orders, coupons).run();
				case 0 -> running = false;
				default -> System.out.println("Choose one of the listed options.");
			}
		}
		System.out.println("Goodbye.");
	}

	private int readInt(String prompt) {
		while (true) {
			System.out.print(prompt);
			try { return Integer.parseInt(scanner.nextLine().trim()); }
			catch (NumberFormatException exception) { System.out.println("Enter a whole number."); }
		}
	}
}