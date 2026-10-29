package com.ecommerce.repository;

import com.ecommerce.models.Order;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class OrderRepository {
	private final Map<Long, Order> orders = new LinkedHashMap<>();

	public Order save(Order order) { orders.put(order.getId(), order); return order; }
	public Optional<Order> findById(long id) { return Optional.ofNullable(orders.get(id)); }
	public Collection<Order> findAll() { return java.util.List.copyOf(orders.values()); }
	public Collection<Order> findByCustomerId(long customerId) {
		return orders.values().stream().filter(order -> order.getCustomerId() == customerId).toList();
	}
}