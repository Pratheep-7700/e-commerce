package com.ecommerce.models;

import java.math.BigDecimal;

public class Product {
	private final long id;
	private final long sellerId;
	private String name;
	private String description;
	private BigDecimal price;
	private int stock;

	public Product(long id, long sellerId, String name, String description, BigDecimal price, int stock) {
		if (id <= 0 || sellerId <= 0) {
			throw new IllegalArgumentException("Product and seller ids must be positive");
		}
		this.id = id;
		this.sellerId = sellerId;
		setName(name);
		setDescription(description);
		setPrice(price);
		setStock(stock);
	}

	public long getId() { return id; }
	public long getSellerId() { return sellerId; }
	public String getName() { return name; }
	public String getDescription() { return description; }
	public BigDecimal getPrice() { return price; }
	public int getStock() { return stock; }

	public void setName(String name) {
		if (name == null || name.isBlank()) throw new IllegalArgumentException("Product name is required");
		this.name = name.trim();
	}

	public void setDescription(String description) {
		this.description = description == null ? "" : description.trim();
	}

	public void setPrice(BigDecimal price) {
		if (price == null || price.signum() <= 0) throw new IllegalArgumentException("Price must be positive");
		this.price = price;
	}

	public void setStock(int stock) {
		if (stock < 0) throw new IllegalArgumentException("Stock cannot be negative");
		this.stock = stock;
	}

	public void addStock(int quantity) {
		if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
		stock = Math.addExact(stock, quantity);
	}

	public void removeStock(int quantity) {
		if (quantity <= 0 || quantity > stock) throw new IllegalArgumentException("Insufficient stock");
		stock -= quantity;
	}
}