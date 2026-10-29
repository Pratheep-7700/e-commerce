package com.ecommerce.repository;

import com.ecommerce.models.Product;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class ProductRepository {
	private final Map<Long, Product> products = new LinkedHashMap<>();

	public Product save(Product product) { products.put(product.getId(), product); return product; }
	public Optional<Product> findById(long id) { return Optional.ofNullable(products.get(id)); }
	public Collection<Product> findAll() { return java.util.List.copyOf(products.values()); }
	public Collection<Product> findBySellerId(long sellerId) {
		return products.values().stream().filter(product -> product.getSellerId() == sellerId).toList();
	}
	public void deleteById(long id) { products.remove(id); }
}