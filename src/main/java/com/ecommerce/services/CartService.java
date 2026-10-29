package com.ecommerce.services;

import com.ecommerce.models.Cart;
import com.ecommerce.models.Product;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CartService {
	private final Map<Long, Cart> carts = new ConcurrentHashMap<>();
	private final ProductService products;

	public CartService(ProductService products) { this.products = products; }
	public Cart getCart(long customerId) { return carts.computeIfAbsent(customerId, Cart::new); }
	public Cart addItem(long customerId, long productId, int quantity) {
		Product product = products.getProduct(productId);
		Cart cart = getCart(customerId);
		cart.add(product, quantity);
		return cart;
	}
	public Cart removeItem(long customerId, long productId) {
		Cart cart = getCart(customerId);
		cart.remove(productId);
		return cart;
	}
	public void clear(long customerId) { getCart(customerId).clear(); }
}