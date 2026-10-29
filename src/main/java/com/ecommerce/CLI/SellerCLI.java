package com.ecommerce.CLI;

import com.ecommerce.models.Product;
import com.ecommerce.models.Seller;
import com.ecommerce.services.ProductService;
import java.math.BigDecimal;
import java.util.Scanner;

public class SellerCLI {
	private final Scanner scanner;
	private final Seller seller;
	private final ProductService products;

	public SellerCLI(Scanner scanner, Seller seller, ProductService products) {
		this.scanner = scanner;
		this.seller = seller;
		this.products = products;
	}

	public void run() {
		boolean running = true;
		while (running) {
			System.out.println("\nSeller: " + seller.getName());
			System.out.println("1. My products  2. Add product  3. Update price  4. Add stock  0. Back");
			switch (readInt("Choose: ")) {
				case 1 -> showProducts();
				case 2 -> addProduct();
				case 3 -> updatePrice();
				case 4 -> addStock();
				case 0 -> running = false;
				default -> System.out.println("Choose one of the listed options.");
			}
		}
	}

	private void showProducts() {
		var sellerProducts = products.listSellerProducts(seller.getId());
		if (sellerProducts.isEmpty()) System.out.println("No products listed.");
		for (Product product : sellerProducts) {
			System.out.printf("%d | %s | $%s | stock: %d%n", product.getId(), product.getName(),
					product.getPrice().toPlainString(), product.getStock());
		}
	}

	private void addProduct() {
		System.out.print("Name: ");
		String name = scanner.nextLine().trim();
		System.out.print("Description: ");
		String description = scanner.nextLine().trim();
		BigDecimal price = readDecimal("Price: ");
		int stock = readInt("Initial stock: ");
		try {
			Product product = products.addProduct(seller.getId(), name, description, price, stock);
			System.out.println("Created product #" + product.getId());
		} catch (IllegalArgumentException exception) {
			System.out.println("Could not create product: " + exception.getMessage());
		}
	}

	private void updatePrice() {
		try {
			products.updatePrice(seller.getId(), readLong("Product id: "), readDecimal("New price: "));
			System.out.println("Price updated.");
		} catch (IllegalArgumentException exception) {
			System.out.println("Could not update price: " + exception.getMessage());
		}
	}

	private void addStock() {
		try {
			products.updateStock(seller.getId(), readLong("Product id: "), readInt("Quantity to add: "));
			System.out.println("Stock updated.");
		} catch (IllegalArgumentException exception) {
			System.out.println("Could not update stock: " + exception.getMessage());
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