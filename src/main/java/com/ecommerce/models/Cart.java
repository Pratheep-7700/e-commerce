package com.ecommerce.models;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Cart {
	private final long customerId;
	private final Map<Long, CartItem> items = new LinkedHashMap<>();

	public Cart(long customerId) {
		if (customerId <= 0) throw new IllegalArgumentException("Customer id must be positive");
		this.customerId = customerId;
	}

	public long getCustomerId() { return customerId; }
	public Collection<CartItem> getItems() { return ListCopy.copy(items.values()); }
	public boolean isEmpty() { return items.isEmpty(); }

	public void add(Product product, int quantity) {
		if (product == null || quantity <= 0) throw new IllegalArgumentException("Product and positive quantity are required");
		int nextQuantity = Math.addExact(items.getOrDefault(product.getId(), new CartItem(product, 0)).quantity, quantity);
		if (nextQuantity > product.getStock()) throw new IllegalArgumentException("Requested quantity exceeds available stock");
		items.put(product.getId(), new CartItem(product, nextQuantity));
	}

	public void remove(long productId) { items.remove(productId); }
	public void clear() { items.clear(); }

	public BigDecimal subtotal() {
		return items.values().stream()
				.map(item -> item.product.getPrice().multiply(BigDecimal.valueOf(item.quantity)))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public static final class CartItem {
		private final Product product;
		private final int quantity;

		private CartItem(Product product, int quantity) {
			this.product = product;
			this.quantity = quantity;
		}

		public Product getProduct() { return product; }
		public int getQuantity() { return quantity; }
		public BigDecimal getLineTotal() { return product.getPrice().multiply(BigDecimal.valueOf(quantity)); }
	}

	private static final class ListCopy {
		private static <T> Collection<T> copy(Collection<T> values) {
			return java.util.List.copyOf(values);
		}
	}
}