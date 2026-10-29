package com.ecommerce.services;

import com.ecommerce.models.Product;
import com.ecommerce.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

public class ProductService {
	private final ProductRepository products;
	private final AtomicLong nextId = new AtomicLong(1);

	public ProductService(ProductRepository products) { this.products = products; }
	public Collection<Product> listProducts() { return products.findAll(); }
	public Collection<Product> listSellerProducts(long sellerId) { return products.findBySellerId(sellerId); }
	public Product getProduct(long id) {
		return products.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
	}
	public Product addProduct(long sellerId, String name, String description, BigDecimal price, int stock) {
		Product product = new Product(nextId.getAndIncrement(), sellerId, name, description, price, stock);
		return products.save(product);
	}
	public void updatePrice(long sellerId, long productId, BigDecimal price) {
		Product product = getProduct(productId);
		requireOwner(sellerId, product);
		product.setPrice(price);
	}
	public void updateStock(long sellerId, long productId, int quantity) {
		Product product = getProduct(productId);
		requireOwner(sellerId, product);
		product.addStock(quantity);
	}
	private void requireOwner(long sellerId, Product product) {
		if (product.getSellerId() != sellerId) throw new IllegalArgumentException("You do not own this product");
	}
}